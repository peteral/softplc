package de.peteral.softplc.memory;

import java.util.HashMap;
import java.util.Map;

import de.peteral.softplc.address.AddressParserFactory;
import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.dataType.DataTypeFactory;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;

/**
 * Default {@link Memory} implementation.
 * <p>
 * Maintains a set of {@link MemoryArea}.
 *
 * @author peteral
 *
 */
public class MemoryImpl implements Memory {

	private final Map<String, MemoryArea> memoryAreas = new HashMap<>();
	private final AddressParserFactory addressParserFactory;
	private final DataTypeFactory dataTypeFactory;

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
	public MemoryImpl(AddressParserFactory addressParserFactory,
			DataTypeFactory dataTypeFactory, MemoryArea... areas) {
		this.addressParserFactory = addressParserFactory;
		this.dataTypeFactory = dataTypeFactory;
		for (MemoryArea memoryArea : areas) {
			memoryAreas.put(memoryArea.getAreaCode(), memoryArea);
		}
	}

	@Override
	public MemoryArea getMemoryArea(String key) {
		MemoryArea result = memoryAreas.get(key);

		if (result != null) {
			return result;
		}

		throw new MemoryAccessViolationException("Invalid memory area [" + key
				+ "]");
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
	public Object read(String address) {
		ParsedAddress parsedAddress = addressParserFactory.parse(address);

		MemoryArea memoryArea = getMemoryArea(parsedAddress.getAreaCode());

		// TODO: special handling for boolean
		byte[] bytes = memoryArea.readBytes(parsedAddress.getOffset(),
				dataTypeFactory.getTotalSize(parsedAddress));

		return dataTypeFactory.fromBytes(bytes, parsedAddress);
	}

	@Override
	public void write(String address, Object value) {
		ParsedAddress parser = addressParserFactory.parse(address);

		// TODO: special handling for boolean
		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		memoryArea.writeBytes(parser.getOffset(),
				dataTypeFactory.toBytes(value, parser));
	}

}
