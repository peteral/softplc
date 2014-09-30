package de.peteral.softplc.impl;

import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;

/**
 * Parses addresses for access to {@link Memory}.
 * <p>
 * Syntax: <b>A,Tnnn[.b][,mm]</b>
 * <ul>
 * <li><b>A</b> - {@link MemoryArea} code
 * <li><b>T</b> - data type - see {@link DataTypeFactory}
 * <li><b>nnn</b> - byte offset
 * <li><b>b</b> - bit number
 * <li><b>mm</b> - number of elements (arrays used if mm > 1)
 * </ul>
 *
 * <p>
 * Examples:
 * <ul>
 * <li><b>DB100,X100.2</b>
 * <li><b>M,DI55</b>
 * <li><b>DB50,C20,20</b>
 * <li><b>I,B0,5</b>
 * </ul>
 *
 * @author peteral
 *
 */
public class AddressParser {

	private final String areaCode;
	private int offset;
	private final int size;
	private final int bitNumber;
	private final int count;
	private final String typeName;

	public AddressParser(DataTypeFactory dataTypeFactory, String address) {
		try {
			String[] split = address.split(",");

			if ((split.length < 2) || (split.length > 3)) {
				throw new AddressParserException(address);
			}

			areaCode = split[0];
			count = (split.length == 3) ? Integer.parseInt(split[2]) : 1;

			String typeAndOffset = split[1];
			int index = typeAndOffset.indexOf('.');
			if (index >= 0) {
				bitNumber = Integer
						.parseInt(typeAndOffset.substring(index + 1));
				typeAndOffset = typeAndOffset.substring(0, index);
			} else {
				bitNumber = 0;
			}

			typeName = typeAndOffset.replaceAll("[0-9].*$", "");
			size = (getCount() * dataTypeFactory.getElementSize(getTypeName()))
					+ dataTypeFactory.getHeaderSize(getTypeName());
			offset = Integer.parseInt(typeAndOffset.substring(getTypeName()
					.length()));

		} catch (NumberFormatException e) {
			throw new AddressParserException(address);
		}
	}

	public String getAreaCode() {
		return areaCode;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}

	public int getBitNumber() {
		return bitNumber;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

}
