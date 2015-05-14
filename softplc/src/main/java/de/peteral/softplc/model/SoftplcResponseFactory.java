package de.peteral.softplc.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a response factory. Must implement {@link ResponseFactory}
 * interface,
 *
 * @author peteral
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoftplcResponseFactory {
	/**
	 *
	 * @return priority of this factory 0 = lowest
	 */
	int priority() default 0;
}
