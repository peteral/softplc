package de.peteral.softplc.cpu;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.peteral.softplc.model.ErrorLog;
import de.peteral.softplc.model.ErrorLogEntry;

/**
 * Default {@link ErrorLog} implementation.
 *
 * @author peteral
 *
 */
public class ErrorLogImpl implements ErrorLog {
	private static final int MAX_ENTRIES = 300;

	private final ObservableList<ErrorLogEntry> entries = FXCollections
			.observableArrayList();

	@Override
	public void log(Level level, String module, String message) {
		getEntries().add(new ErrorLogEntry(level, module, message));

		while (getEntries().size() > MAX_ENTRIES) {
			getEntries().remove(0);
		}

		Logger.getLogger(module).log(level, module + ": " + message);
	}

	@Override
	public ObservableList<ErrorLogEntry> getEntries() {
		return entries;
	}

}
