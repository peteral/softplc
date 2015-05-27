package de.peteral.softplc.model;

import javafx.beans.property.LongProperty;
import javafx.collections.ObservableList;
import de.peteral.softplc.program.ScriptFile;

/**
 * Represents the program which is cyclically executed by a {@link Cpu}
 * instance.
 * <p>
 * Maintains it's own scripting context.
 *
 * @author peteral
 *
 */
public interface Program extends Runnable {
	/**
	 * Compiles this program.
	 * <p>
	 * Creates and initializes scripting context.
	 *
	 * @return true - success.
	 */
	boolean compile();

	/**
	 * Adds a new {@link ProgramCycleObserver} which s informed about the
	 * program execution life cycle.
	 *
	 * @param observer
	 */
	void addObserver(ProgramCycleObserver observer);

	/**
	 * Removes an existing {@link ProgramCycleObserver}.
	 *
	 * @param observer
	 */
	void removeObserver(ProgramCycleObserver observer);

	/**
	 *
	 * @return target cycle time in [ms]
	 */
	LongProperty getTargetCycleTime();

	/**
	 *
	 * @return list of all source files for UI
	 */
	ObservableList<ScriptFile> getScriptFiles();

}
