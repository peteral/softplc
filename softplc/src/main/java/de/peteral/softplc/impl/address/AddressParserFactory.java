package de.peteral.softplc.impl.address;

/**
 * {@link ParsedAddress} factory for easier unit testing.
 *
 * @author peteral
 *
 */
public class AddressParserFactory {

	/**
	 * Parses the address.
	 *
	 * @param address
	 *            address to be parsed.
	 * @return {@link ParsedAddress} instance according the address.
	 */
	public ParsedAddress parse(String address) {
		return new ParsedAddress(address);
	}

}
