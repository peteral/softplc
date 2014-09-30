package de.peteral.softplc.model;

/**
 * Represent a Central Processing Unit of a {@link Plc}.
 * <p>
 * Compiles and cyclically executes javascript program modules (entry point is
 * main()).
 * <p>
 * Attempts to keep the requested cycle time.
 * <p>
 * Maintains a set of memory areas.
 *
 * @author peteral
 *
 */
public interface Cpu {
	/**
	 *
	 * @return actual status of this CPU
	 */
	CpuStatus getStatus();

	/**
	 *
	 * @return target cycle time in [ms]
	 */
	long getTargetCycleTime();

	/**
	 * Attempts to start executing the program.
	 * <p>
	 * Following results possible:
	 * <ul>
	 * <li>Valid program loaded - {@link CpuStatus} changes to RUN, Program
	 * execution is started.
	 * <li>No valid program loaded - {@link CpuStatus} will change to ERROR or
	 * remain ERROR
	 * </ul>
	 */
	void start();

	/**
	 * Stops program execution.
	 * <p>
	 * {@link CpuStatus} changes to STOP
	 */
	void stop();

	/**
	 * Attempts to load the program to the CPU.
	 * <p>
	 * Possible outcomes:
	 * <ul>
	 * <li>Program is valid - program is compiled, {@link CpuStatus} changes to
	 * STOP
	 * <li>Program is invalid - {@link CpuStatus} changes to ERROR
	 * </ul>
	 *
	 * @param program
	 */
	void loadProgram(Program program);

	/**
	 *
	 * @return {@link ErrorLog} instance associated with this CPU
	 */
	ErrorLog getErrorLog();

	/**
	 * Invoked by {@link PutGetServer} server. Schedules a
	 * {@link CommunicationTask} to be executed after the actual {@link Program}
	 * cycle ends.
	 *
	 * @param task
	 *            task to be executed
	 */
	void addCommunicationTask(CommunicationTask task);

	/**
	 *
	 * @return {@link Memory} instance of this {@link Cpu}
	 */
	Memory getMemory();
}
