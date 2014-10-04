package de.peteral.softplc.address;

/**
 * This exception is thrown by the {@link AddressParserFactory} in case of an
 * invalid address.
 *
 * @author peteral
 */
public class AddressParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance.
	 * <p>
	 *
	 * @param address
	 *            requested address
	 */
	public AddressParserException(String address) {
		super("Invalid address: [" + address + "]");
	}

}
