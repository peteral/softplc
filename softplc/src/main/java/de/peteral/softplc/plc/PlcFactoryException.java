package de.peteral.softplc.plc;

/**
 * This exception is thrown by {@link PlcFactory} in case of an invalid
 * configuration file.
 *
 * @author peteral
 *
 */
public class PlcFactoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance.
	 *
	 * @param path
	 *            configuration file
	 * @param reason
	 *            causing exception
	 */
	public PlcFactoryException(String path, Throwable reason) {
		super("Failed parsing configuration [" + path + "]: ", reason);
	}

	/**
	 * Creates instance with additional information.
	 *
	 * @param path
	 * @param reason
	 */
	public PlcFactoryException(String path, String reason) {
		super("Failed parsing configuration [" + path + "]: " + reason);
	}
}
