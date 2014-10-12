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
	 *
	 * @return slot number of the handling cpu
	 */
	int getCpuSlot();

}
