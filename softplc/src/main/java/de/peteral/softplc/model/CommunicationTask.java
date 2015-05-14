package de.peteral.softplc.model;

/**
 * Defines a task created by {@link PutGetServer} to be executed by {@link Cpu}
 * between two {@link Program} cycles.
 *
 * @author peteral
 *
 */
public interface CommunicationTask {

	/**
	 * This method will be invoked by the {@link Cpu}
	 *
	 * @param cpu
	 *            {@link Cpu} instance executing this task (for memory
	 *            interaction)
	 */
	void execute(Cpu cpu);

	/**
	 * Invoked when task requests invalid CPU slot
	 *
	 * @param slot
	 *            requested cpu slot
	 */
	void onInvalidCpu(int slot);
}
