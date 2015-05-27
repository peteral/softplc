package de.peteral.softplc.executor;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * An shut down executor cannot be reused. We need to create a new instance upon
 * every CPU start.
 * <p>
 * This factory allows testability.
 *
 * @author peteral
 *
 */
public class ScheduledThreadPoolExecutorFactory {

	/**
	 *
	 * @return new {@link ScheduledThreadPoolExecutor} for cpu execution
	 */
	public ScheduledThreadPoolExecutor createExecutor() {
		return new ScheduledThreadPoolExecutor(1);
	}

}
