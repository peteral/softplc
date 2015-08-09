package de.peteral.softplc.memory;

import java.util.logging.Level;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.datatype.DataTypeFactory;
import de.peteral.softplc.datatype.DataTypeUtils;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.MemoryTable;
import de.peteral.softplc.model.SymbolTable;
import de.peteral.softplc.symbol.SymbolTableImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Default {@link Memory} implementation.
 * <p>
 * Maintains a set of {@link MemoryArea}.
 *
 * @author peteral
 */
public class MemoryImpl implements Memory {

	private static final transient String BOOL_PREFIX = "X";
	private final ObservableList<MemoryArea> memoryAreas = FXCollections.observableArrayList();
	private final transient ObservableList<MemoryTable> memoryTables = FXCollections.observableArrayList();
	private final transient AddressParserFactory addressParserFactory;
	private final transient DataTypeFactory dataTypeFactory;
	private final transient SymbolTable symbolTable = new SymbolTableImpl();

	/**
	 * Creates a new instance.
	 * <p>
	 *
	 * @param addressParserFactory
	 *            {@link AddressParserFactory} instance.
	 * @param dataTypeFactory
	 *            {@link DataTypeFactory} instance.
	 * @param areas
	 *            set of {@link MemoryArea} managed within this {@link Memory}
	 */
	public MemoryImpl(AddressParserFactory addressParserFactory, DataTypeFactory dataTypeFactory, MemoryArea... areas) {
		this.addressParserFactory = addressParserFactory;
		this.dataTypeFactory = dataTypeFactory;
		for (MemoryArea memoryArea : areas) {
			addMemoryArea(memoryArea);
		}
	}

	@Override
	public MemoryArea getMemoryArea(String key) {
		for (MemoryArea area : memoryAreas) {
			if (area.getAreaCode().get().equals(key)) {
				return area;
			}
		}

		throw new MemoryAccessViolationException("Invalid memory area [" + key + "]");
	}

	@Override
	public byte[] readBytes(String area, int offset, int length) {
		return getMemoryArea(area).readBytes(offset, length);
	}

	@Override
	public void writeBytes(String area, int offset, byte[] data) {
		getMemoryArea(area).writeBytes(offset, data);
	}

	@Override
	public Object read(String addr) {
		String address = getSymbolTable().getAddress(addr);
		if (address == null) {
			address = addr;
		}

		ParsedAddress parsedAddress = addressParserFactory.parse(address);

		if (BOOL_PREFIX.equals(parsedAddress.getTypeName())) {
			return getBit(address);
		}

		MemoryArea memoryArea = getMemoryArea(parsedAddress.getAreaCode());

		byte[] bytes = memoryArea.readBytes(parsedAddress.getOffset(), dataTypeFactory.getTotalSize(parsedAddress));

		Object result = dataTypeFactory.fromBytes(bytes, parsedAddress);

		if ((memoryArea.getLogger() != null) && memoryArea.getLogger().isLoggable(Level.FINER)) {
			memoryArea.getLogger().finer("Read: " + address + " = " + result);
		}

		return result;
	}

	@Override
	public void write(String addr, Object value) {
		String address = getSymbolTable().getAddress(addr);
		if (address == null) {
			address = addr;
		}

		ParsedAddress parser = addressParserFactory.parse(address);

		if (BOOL_PREFIX.equals(parser.getTypeName())) {
			setBit(address, (Boolean) value);
			return;
		}

		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		memoryArea.writeBytes(parser.getOffset(), dataTypeFactory.toBytes(value, parser));

		if ((memoryArea.getLogger() != null) && memoryArea.getLogger().isLoggable(Level.FINE)) {
			memoryArea.getLogger().fine("Write: " + address + " = " + value);
		}
	}

	@Override
	public boolean getBit(String address) {
		ParsedAddress parser = addressParserFactory.parse(address);

		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		int byteValue = DataTypeUtils.byteToInt(memoryArea.readBytes(parser.getOffset(), 1)[0]);
		int mask = 1 << parser.getBitNumber();

		return (byteValue & mask) == mask;
	}

	@Override
	public void setBit(String address, boolean value) {
		ParsedAddress parser = addressParserFactory.parse(address);

		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		if ((memoryArea.getLogger() != null) && memoryArea.getLogger().isLoggable(Level.FINE)) {
			memoryArea.getLogger().fine("setBit: " + address + " = " + value);
		}

		int byteValue = DataTypeUtils.byteToInt(memoryArea.readBytes(parser.getOffset(), 1)[0]);

		if (value) {
			int mask = 1 << parser.getBitNumber();
			byteValue = byteValue | mask;
		} else {
			int mask = 1 << parser.getBitNumber();
			mask = ~mask;
			byteValue = byteValue & mask;
		}
		memoryArea.writeBytes(parser.getOffset(), new byte[] { (byte) byteValue });
	}

	@Override
	public ObservableList<MemoryArea> getMemoryAreas() {
		return memoryAreas;
	}

	@Override
	public ObservableList<MemoryTable> getMemoryTables() {
		return memoryTables;
	}

	private boolean parseBoolean(String value) {
		// check 1 first
		if (value.trim().equals("1")) {
			return true;
		}

		return Boolean.parseBoolean(value);
	}

	@Override
	public void parse(String addr, String value) {
		String address = getSymbolTable().getAddress(addr);
		if (address == null) {
			address = addr;
		}

		ParsedAddress parser = addressParserFactory.parse(address);

		if (BOOL_PREFIX.equals(parser.getTypeName())) {
			setBit(address, parseBoolean(value));
			return;
		}

		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		memoryArea.writeBytes(parser.getOffset(), dataTypeFactory.parseToBytes(value, parser));

		if ((memoryArea.getLogger() != null) && memoryArea.getLogger().isLoggable(Level.FINE)) {
			memoryArea.getLogger().fine("Parse: " + address + " = " + value);
		}
	}

	@Override
	public void removeMemoryAreas(ObservableList<MemoryArea> toDelete) {
		memoryAreas.removeAll(toDelete);
	}

	@Override
	public void addMemoryArea(MemoryArea memoryArea) {
		memoryAreas.add(memoryArea);

		memoryAreas.sort((m1, m2) -> m1.getAreaCode().get().compareTo(m2.getAreaCode().get()));
	}

	@Override
	public void reset() {
		memoryAreas.forEach(memoryArea -> memoryArea.reset());
	}

	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
}
