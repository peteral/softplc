package de.peteral.softplc.memorytables;

import java.util.Arrays;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.MemoryTableVariable;

/**
 * Writes memory table variables to memory.
 *
 * @author peteral
 *
 */
public class MemoryTableWriteTask implements CommunicationTask {

	private final MemoryTableVariable[] variables;

	/**
	 * Initializes new instance.
	 *
	 * @param variables
	 *            variables to be written
	 */
	public MemoryTableWriteTask(MemoryTableVariable... variables) {
		this.variables = variables;
	}

	@Override
	public void execute(Cpu cpu) {
		Arrays.asList(variables).forEach(
				variable -> cpu.getMemory().parse(variable.getVariable().get(),
						variable.getNewValue().get()));
	}

	@Override
	public void onInvalidCpu(int slot) {
	}
}
