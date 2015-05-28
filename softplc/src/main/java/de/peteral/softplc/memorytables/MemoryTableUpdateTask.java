package de.peteral.softplc.memorytables;

import java.util.Arrays;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;

/**
 * Virtual communication task used to update a memory table
 *
 * @author peteral
 *
 */
public class MemoryTableUpdateTask implements CommunicationTask {

	private final MemoryTable memoryTable;

	/**
	 * Default constructor
	 *
	 * @param memoryTable
	 */
	public MemoryTableUpdateTask(MemoryTable memoryTable) {
		this.memoryTable = memoryTable;
	}

	@Override
	public void execute(Cpu cpu) {
		memoryTable.getVariables().forEach(
				variable -> {
					try {
						Object read = cpu.getMemory().read(
								variable.getVariable().get());
						String strValue = (read.getClass().isArray()) ? Arrays
								.toString((Object[]) read) : "" + read;

						variable.getCurrentValue().set(strValue);
					} catch (Exception e) {
						variable.getCurrentValue().set(e.getMessage());
					}
				});
	}

	@Override
	public void onInvalidCpu(int slot) {
	}

}
