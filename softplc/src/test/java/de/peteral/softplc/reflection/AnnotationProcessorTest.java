package de.peteral.softplc.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.reflections.util.ClasspathHelper;

@SuppressWarnings("javadoc")
public class AnnotationProcessorTest {

	@Test
	public void loadAnnotations_TwoClassesInClasspath_ReturnsInstancesInCorrectOrder() {
		List<TestInterface> result = new ArrayList<>();
		new AnnotationProcessor<TestInterface>(TestAnnotation.class,
				ClasspathHelper.forClass(AnnotationProcessorTest.class))
				.loadAnnotations(result);

		assertEquals(2, result.size());

		assertTrue(result.get(0) instanceof TestClass2);
		assertTrue(result.get(1) instanceof TestClass1);
	}
}
