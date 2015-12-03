package de.peteral.softplc.protocol;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.model.NetworkInterface;

/**
 * Defines a task created by {@link NetworkInterface} to be executed by {@link Cpu}
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
