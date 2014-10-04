package de.peteral.softplc.impl.address;

import de.peteral.softplc.impl.DataTypeFactory;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;

/**
 * Parses addresses for access to {@link Memory}.
 * <p>
 * Syntax: <b>A,Tnnn[.b][:ss][,mm]</b>
 * <ul>
 * <li><b>A</b> - {@link MemoryArea} code
 * <li><b>T</b> - data type - see {@link DataTypeFactory}
 * <li><b>nnn</b> - byte offset
 * <li><b>b</b> - bit number
 * <li><b>ss</b> - size (for strings)
 * <li><b>mm</b> - number of elements (arrays used if mm > 1)
 * </ul>
 *
 * <p>
 * Examples:
 * <ul>
 * <li><b>DB100,X100.2</b>
 * <li><b>M,DI55</b>
 * <li><b>DB50,C20:20</b>
 * <li><b>I,B0,5</b>
 * </ul>
 *
 * @author peteral
 *
 */
public class ParsedAddress {

	private final String areaCode;
	private int offset;
	private final int size;
	private final int bitNumber;
	private final int count;
	private final String typeName;
	private final String address;

	/**
	 * Creates a new instance.
	 * <p>
	 *
	 * @param address
	 *            address to be parsed.
	 */
	public ParsedAddress(String address) {
		this.address = address;
		try {
			String[] split = address.split(",");

			if ((split.length < 2) || (split.length > 3)) {
				throw new AddressParserException(address);
			}

			areaCode = split[0];
			count = (split.length == 3) ? Integer.parseInt(split[2]) : 1;

			String typeOffsetAndSize = split[1];
			String typeAndOffset = typeOffsetAndSize;

			int index = typeOffsetAndSize.indexOf(':');
			if (index >= 0) {
				size = Integer.parseInt(typeOffsetAndSize.substring(index + 1));
				typeAndOffset = typeOffsetAndSize.substring(0, index);
			} else {
				size = 1;
			}

			index = typeAndOffset.indexOf('.');
			if (index >= 0) {
				bitNumber = Integer
						.parseInt(typeAndOffset.substring(index + 1));
				typeAndOffset = typeAndOffset.substring(0, index);
			} else {
				bitNumber = 0;
			}

			typeName = typeAndOffset.replaceAll("[0-9].*$", "");
			offset = Integer.parseInt(typeAndOffset.substring(getTypeName()
					.length()));

		} catch (NumberFormatException e) {
			throw new AddressParserException(address);
		}
	}

	/**
	 * @return {@link MemoryArea} code
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @return offset in bytes
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return size (optional, used for example for Strings).
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return bit number - optional, used for binary addresses.
	 */
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

	@Override
	public String toString() {
		return address;
	}
}
