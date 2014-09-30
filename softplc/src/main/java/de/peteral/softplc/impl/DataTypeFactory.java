package de.peteral.softplc.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.peteral.softplc.impl.converter.BoolConverter;
import de.peteral.softplc.impl.converter.ByteConverter;
import de.peteral.softplc.impl.converter.DateConverter;
import de.peteral.softplc.impl.converter.DintConverter;
import de.peteral.softplc.impl.converter.DwordConverter;
import de.peteral.softplc.impl.converter.IntConverter;
import de.peteral.softplc.impl.converter.RealConverter;
import de.peteral.softplc.impl.converter.S7StringConverter;
import de.peteral.softplc.impl.converter.StringConverter;
import de.peteral.softplc.impl.converter.WordConverter;
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
	private static final Map<String, Class> DATA_TYPES = new HashMap<>();
	private static final Map<String, Integer> ELEMENT_SIZES = new HashMap<>();
	private static final Map<String, Integer> ELEMENT_HEADER_SIZE = new HashMap<>();
	private static final Map<String, Converter<?>> CONVERTERS = new HashMap<>();

	static {
		defineType("B", Byte.class, 1, new ByteConverter());
		defineType("I", Short.class, 2, new IntConverter());
		defineType("W", Integer.class, 2, new WordConverter());
		defineType("DW", Long.class, 4, new DwordConverter());
		defineType("DI", Integer.class, 4, new DintConverter());
		defineType("REAL", Float.class, 4, new RealConverter());
		defineType("C", String.class, 1, new StringConverter());
		defineType("STRING", String.class, 1, 2, new S7StringConverter());
		defineType("DT", Date.class, 8, new DateConverter());
		defineType("X", Boolean.class, 1, new BoolConverter());
	}

	/**
	 * Returns java class associated with this data type.
	 *
	 * @param type
	 *            PLC data type
	 * @return associated java class
	 */
	public Class getType(String type) {
		assertValid(type);

		return DATA_TYPES.get(type);
	}

	private static void defineType(String key, Class dataType, int size,
			int headerSize, Converter<?> converter) {
		DATA_TYPES.put(key, dataType);
		ELEMENT_SIZES.put(key, size);
		ELEMENT_HEADER_SIZE.put(key, headerSize);
		CONVERTERS.put(key, converter);
	}

	private static void defineType(String key, Class dataType, int size,
			Converter<?> converter) {
		defineType(key, dataType, size, 0, converter);
	}

	/**
	 * Returns size of one element of given type in bytes.
	 *
	 * @param type
	 *            requested data type
	 * @return size of one element in bytes
	 */
	public int getElementSize(String type) {
		assertValid(type);

		return ELEMENT_SIZES.get(type);
	}

	private void assertValid(String type) {
		if (!DATA_TYPES.containsKey(type)) {
			throw new DataTypeException(type, "unknown data type");
		}
	}

	/**
	 * Some data types have require additional space for management structures
	 * (example STRING). This function returns the size of such structures.
	 *
	 * @param type
	 *            requested data type name
	 * @return size of the header
	 */
	public int getHeaderSize(String type) {
		return ELEMENT_HEADER_SIZE.get(type);
	}

	/**
	 * Translate byte data to java instances. Creates an array when necessary.
	 *
	 * @param bytes
	 *            byte data
	 * @param typeName
	 *            requested type name
	 * @return java class instance or array of java class instances
	 */
	public Object fromBytes(byte[] bytes, String typeName, int count) {
		assertValidBytes(bytes, typeName, count);

		int elementSize = getElementSize(typeName);
		int headerSize = getHeaderSize(typeName);

		Converter<?> converter = CONVERTERS.get(typeName);
		if (count == 1) {
			return converter.fromBytes(bytes, 0, count);
		}

		Object[] result = converter.createArray(count);

		for (int i = 0; i < count; i++) {
			result[i] = converter.fromBytes(bytes, headerSize
					+ (i * elementSize), count);
		}

		return result;
	}

	private void assertValidBytes(byte[] bytes, String typeName, int count) {
		int elementSize = getElementSize(typeName);
		int elementHeaderSize = getHeaderSize(typeName);

		if (bytes.length < ((count * elementSize) + elementHeaderSize)) {
			throw new DataTypeException(typeName, "invalid byte array length: "
					+ bytes.length);
		}
	}

	/**
	 * Translates java instances to byte array. Arrays are supported.
	 *
	 * @param value
	 *            java class or array to be transformed
	 * @param typeName
	 *            data type name
	 * @return byte array representation
	 */
	public byte[] toBytes(Object value, String typeName, int count) {
		int headerSize = getHeaderSize(typeName);
		int elementSize = getElementSize(typeName);

		byte[] result = new byte[headerSize + (count * elementSize)];

		Converter converter = CONVERTERS.get(typeName);
		if (count == 1) {
			converter.toBytes(value, headerSize, count, result);
		} else {
			Object[] values = (Object[]) value;
			for (int i = 0; i < count; i++) {
				converter.toBytes(values[i], headerSize + (i * elementSize),
						count, result);
			}
		}

		return result;
	}

}
