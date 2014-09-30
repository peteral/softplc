package de.peteral.softplc.model;

/**
 * Defines the possible states the {@link Cpu} can enter.
 *
 * @author peteral
 *
 */
public enum CpuStatus {
	/**
	 * Invalid program.
	 */
	ERROR,

	/**
	 * Running
	 */
	RUN,

	/**
	 * Stopped - program execution interrupted by user
	 */
	STOP;
}
