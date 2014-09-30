package de.peteral.softplc.impl;

public class AddressParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AddressParserException(String address) {
		super("Invalid address: [" + address + "]");
	}

}
