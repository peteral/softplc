package de.peteral.softplc.reflection;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import de.peteral.softplc.classloader.FolderClassLoader;
import de.peteral.softplc.protocol.ResponseFactory;
import de.peteral.softplc.protocol.TaskFactory;

/**
 * Scans for annotated classes in class path and instantiates them using default
 * constructor.
 * <p>
 * Used for loading {@link ResponseFactory} and {@link TaskFactory} instances
 * from 3rd party archives on classpath.
 *
 * @author peteral
 *
 * @param <T>
 *            result type
 */
public class AnnotationProcessor<T> {

	private static final Logger LOGGER = Logger.getLogger("reflection");
	private final Class<? extends Annotation> annotation;
	private final URL[] url;

	/**
	 * Creates a new instance.
	 *
	 * @param annotation
	 *            annotation to be looked for
	 * @param url
	 */
	public AnnotationProcessor(Class<? extends Annotation> annotation, URL... url) {
		this.annotation = annotation;
		this.url = url;
	}

	/**
	 * Loads instances of annotated classes into a list. The instances are
	 * ordered by priority if such field is available in the annotation.
	 * <p>
	 * Priority = 0 is the lowest and default.
	 *
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	public void loadAnnotations(List<T> result) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.addUrls(((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs());
		FolderClassLoader folderClassLoader = new FolderClassLoader(new File("protocols"));
		Thread.currentThread().setContextClassLoader(folderClassLoader);
		cb.addUrls(folderClassLoader.getURLs());
		cb.addUrls(url);

		Reflections reflections = new Reflections(cb);
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);

		List<Class<?>> sorted = sort(classes);

		for (Class<?> clazz : sorted) {
			try {
				result.add((T) clazz.newInstance());
				LOGGER.config("Loaded factory: " + clazz.getName());

			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.log(Level.SEVERE, "Failed creating factory [" + clazz.getName() + "]: ", e);
			}
		}

		LOGGER.info("Found " + result.size() + " @" + annotation.getSimpleName());
	}

	private List<Class<?>> sort(Set<Class<?>> classes) {
		List<Class<?>> result = new ArrayList<>();

		result.addAll(classes);

		Collections.sort(result, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				try {
					Annotation annotation1 = o1.getAnnotation(annotation);
					Annotation annotation2 = o2.getAnnotation(annotation);

					int priority1 = (int) annotation1.annotationType().getMethod("priority").invoke(annotation1);
					int priority2 = (int) annotation2.annotationType().getMethod("priority").invoke(annotation2);

					return Integer.compare(priority2, priority1);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {

					return 0;
				}

			}
		});

		return result;
	}
}
