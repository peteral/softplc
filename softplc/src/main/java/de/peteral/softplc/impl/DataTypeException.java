package de.peteral.softplc.impl;

public class DataTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataTypeException(String typeName, String message) {
		super("Data type error [" + typeName + "]: " + message);
	}
}
