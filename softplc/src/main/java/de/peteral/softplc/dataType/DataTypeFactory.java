package de.peteral.softplc.dataType;

import java.util.HashMap;
import java.util.Map;

import de.peteral.softplc.address.ParsedAddress;
import de.peteral.softplc.model.Converter;

/**
 * Defines all supported data types:
 * <ul>
 * <li><b>B</b> - byte - 8 bit unsigned
 * <li><b>I</b> - int - 16 bit signed
 * <li><b>W</b> - word - 16 bit unsigned
 * <li><b>DW</b> - dword - 32 bit unsigned
 * <li><b>DI</b> - dint - 32 bit signed
 * <li><b>REAL</b> - real - 32 bit floating point
 * <li><b>C</b> - character - 8 bit
 * <li><b>STRING</b> - S7STRING - header with 1 byte total length + 1 byte
 * actual length
 * <li><b>DT</b> - S7_DATE_AND_TIME - 8 byte
 * <li><b>X</b> - bit
 * </ul>
 *
 * @author peteral
 *
 */
@SuppressWarnings("rawtypes")
public class DataTypeFactory {
	private static final Map<String, Integer> ELEMENT_SIZES = new HashMap<>();
	private static final Map<String, Integer> ELEMENT_HEADER_SIZE = new HashMap<>();
	private static final Map<String, Converter<?>> CONVERTERS = new HashMap<>();

	static {
		defineType("B", 1, new ByteConverter());
		defineType("I", 2, new IntConverter());
		defineType("W", 2, new WordConverter());
		defineType("DW", 4, new DwordConverter());
		defineType("DI", 4, new DIntConverter());
		defineType("REAL", 4, new RealConverter());
		defineType("C", 1, new StringConverter());
		defineType("STRING", 1, 2, new S7StringConverter());
		defineType("DT", 8, new DateConverter());
	}

	private static void defineType(String key, int size, int headerSize,
			Converter<?> converter) {
		ELEMENT_SIZES.put(key, size);
		ELEMENT_HEADER_SIZE.put(key, headerSize);
		CONVERTERS.put(key, converter);
	}

	private static void defineType(String key, int size, Converter<?> converter) {
		defineType(key, size, 0, converter);
	}

	/**
	 * Returns size of one element of given type in bytes.
	 *
	 * @param address
	 *
	 * @return size of one element in bytes
	 */
	public int getElementSize(ParsedAddress address) {
		assertValid(address);

		return ELEMENT_SIZES.get(address.getTypeName());
	}

	private void assertValid(ParsedAddress address) {
		if (!CONVERTERS.containsKey(address.getTypeName())) {
			throw new DataTypeException(address.getTypeName(),
					"unknown data type");
		}
	}

	/**
	 * Some data types have require additional space for management structures
	 * (example STRING). This function returns the size of such structures.
	 *
	 * @param address
	 *
	 * @return size of the header
	 */
	public int getHeaderSize(ParsedAddress address) {
		assertValid(address);

		return ELEMENT_HEADER_SIZE.get(address.getTypeName());
	}

	/**
	 * Translate byte data to java instances. Creates an array when necessary.
	 *
	 * @param bytes
	 *            byte data
	 * @param address
	 * @return java class instance or array of java class instances
	 */
	public Object fromBytes(byte[] bytes, ParsedAddress address) {
		assertValidBytes(bytes, address);

		Converter<?> converter = CONVERTERS.get(address.getTypeName());
		if (address.getCount() == 1) {
			return converter.fromBytes(bytes, address, 0);
		}

		Object[] result = converter.createArray(address.getCount());

		for (int i = 0; i < address.getCount(); i++) {
			result[i] = converter.fromBytes(bytes, address, i
					* getByteArraySize(address));
		}

		return result;
	}

	private void assertValidBytes(byte[] bytes, ParsedAddress address) {
		int elementSize = getElementSize(address);
		int elementHeaderSize = getHeaderSize(address);

		if (bytes.length < ((address.getCount() * elementSize) + elementHeaderSize)) {
			throw new DataTypeException(address.getTypeName(),
					"invalid byte array length: " + bytes.length);
		}
	}

	/**
	 * Translates java instances to byte array. Arrays are supported.
	 *
	 * @param value
	 *            java class or array to be transformed
	 * @param address
	 * @return byte array representation
	 */
	@SuppressWarnings("unchecked")
	public byte[] toBytes(Object value, ParsedAddress address) {
		byte[] result = new byte[getByteArraySize(address) * address.getCount()];

		Converter converter = CONVERTERS.get(address.getTypeName());
		if (address.getCount() == 1) {
			converter.toBytes(value, address, result, 0);
		} else {
			Object[] values = (Object[]) value;
			for (int i = 0; i < address.getCount(); i++) {
				converter.toBytes(values[i], address, result, i
						* getByteArraySize(address));
			}
		}

		return result;
	}

	private int getByteArraySize(ParsedAddress address) {
		int headerSize = getHeaderSize(address);
		int elementSize = getElementSize(address);

		return (headerSize + (elementSize * address.getSize()));
	}

	/**
	 * Calculates total size in bytes addressed by this address.
	 *
	 * @param address
	 * @return total size in bytes
	 */
	public int getTotalSize(ParsedAddress address) {
		return address.getCount()
				* (getHeaderSize(address) + (address.getSize() * getElementSize(address)));
	}
}
