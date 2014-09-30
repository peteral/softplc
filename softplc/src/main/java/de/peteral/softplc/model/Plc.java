package de.peteral.softplc.model;

/**
 * Represents the virtual PLC system.
 * <p>
 * Manages a set of {@link Cpu} instances organized by their slot number.
 * <p>
 * Contains a PUT/GET Server which is started / stopped as the PLC starts /
 * stops.
 *
 * @author peteral
 */
public interface Plc {
	/**
	 * Returns {@link Cpu} instance for given slot number.
	 *
	 * @param slot
	 *            slot number (index in the cpu array)
	 * @return {@link Cpu} instance
	 * @throws ArrayIndexOutOfBoundsException
	 *             for invalid slot numbers
	 */
	Cpu getCpu(int slot);

	/**
	 *
	 * @return number of actually configured {@link Cpu} units
	 */
	int getCpuCount();

	/**
	 * Starts the PLC and all managed services.
	 */
	void start();

	/**
	 * Stops the PLC and all managed services.
	 */
	void stop();
}
