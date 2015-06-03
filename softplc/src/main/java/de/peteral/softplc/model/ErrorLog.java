package de.peteral.softplc.model;

import java.util.logging.Level;

import javafx.collections.ObservableList;

/**
 * Each {@link Cpu} posesses an {@link ErrorLog} instance. This is used by
 * {@link Memory} and {@link Program} to log problems and helps locate errors in
 * user programs.
 *
 * @author peteral
 *
 */
public interface ErrorLog {

	/**
	 * Logs a message.
	 *
	 * @param level
	 *            log level
	 * @param module
	 *            module reporting the error
	 * @param message
	 *            message text
	 */
	void log(Level level, String module, String message);

	/**
	 *
	 * @return observable list of last n logged entries for this CPU
	 */
	ObservableList<ErrorLogEntry> getEntries();

}
