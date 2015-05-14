package de.peteral.softplc.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.peteral.softplc.comm.tasks.TaskFactory;

/**
 * Marks a class as a TaskFactory. This class must implement the
 * {@link TaskFactory} interface.
 *
 * @author peteral
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoftplcTaskFactory {
	/**
	 *
	 * @return priority of this factory 0 = lowest
	 */
	int priority() default 0;
}
