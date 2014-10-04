package de.peteral.softplc.dataType;

/**
 * Exception thrown by {@link DataTypeFactory} when invalid data type is
 * requested.
 *
 * @author peteral
 *
 */
public class DataTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance.
	 *
	 * @param typeName
	 *            name of the requested type
	 * @param message
	 *            additional message
	 */
	public DataTypeException(String typeName, String message) {
		super("Data type error [" + typeName + "]: " + message);
	}
}
