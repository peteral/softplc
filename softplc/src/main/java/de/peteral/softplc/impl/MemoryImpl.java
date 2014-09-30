package de.peteral.softplc.impl;

import java.util.HashMap;
import java.util.Map;

import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.MemoryArea;

public class MemoryImpl implements Memory {

	private final Map<String, MemoryArea> memoryAreas = new HashMap<>();
	private final AddressParserFactory addressParserFactory;
	private final DataTypeFactory dataTypeFactory;

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
		AddressParser parser = addressParserFactory.createParser(address);

		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		byte[] bytes = memoryArea.readBytes(parser.getOffset(),
				parser.getSize());

		return dataTypeFactory.fromBytes(bytes, parser.getTypeName(),
				parser.getCount());
	}

	@Override
	public void write(String address, Object value) {
		AddressParser parser = addressParserFactory.createParser(address);

		MemoryArea memoryArea = getMemoryArea(parser.getAreaCode());

		memoryArea.writeBytes(
				parser.getOffset(),
				dataTypeFactory.toBytes(value, parser.getTypeName(),
						parser.getCount()));
	}

}
