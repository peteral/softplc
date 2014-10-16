package de.peteral.softplc.cpu;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.model.ErrorLog;

/**
 * Default {@link ErrorLog} implementation.
 *
 * @author peteral
 *
 */
public class ErrorLogImpl implements ErrorLog {

	@Override
	public void log(Level level, String module, String message) {
		// TODO implement error log properly

		Logger.getLogger(module).log(level, module + ": " + message);
	}

}
