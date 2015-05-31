package de.peteral.softplc.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("javadoc")
public class JavaVersionTest {

	private static final String JAVA_VERSION = "1.8.0_45";
	private JavaVersion version;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		version = new JavaVersion(JAVA_VERSION);

	}

	@Test
	public void isValid_ValidVersion_ReturnsTrue() {
		assertTrue(version.isValid());
	}

	@Test
	public void isValid_InvalidVersion_ReturnsTrue() {
		assertFalse(new JavaVersion("1.8").isValid());
	}

	@Test
	public void getMajor_ValidVersion_ReturnsCorrectMajor() {
		assertEquals(1, version.getMajor());
	}

	@Test
	public void getMinor_ValidVersion_ReturnsCorrectMinor() {
		assertEquals(8, version.getMinor());
	}

	@Test
	public void getBuildPrefix_ValidVersion_ReturnsCorrectBuildPrefix() {
		assertEquals(0, version.getBuildPrefix());
	}

	@Test
	public void getBuildSuffix_ValidVersion_ReturnsCorrectBuildPrefix() {
		assertEquals(45, version.getBuildSuffix());
	}

	@Test
	public void compareTo_EqualVersions_ReturnsZero() {
		assertEquals(0, version.compareTo(new JavaVersion(JAVA_VERSION)));
	}

	@Test
	public void compareTo_SourceSmallerVersionThanTarget_ReturnsNegative() {
		assertTrue(version.compareTo(new JavaVersion("1.8.0_50")) < 0);
	}

	@Test
	public void compareTo_SourceBiggerVersionThanTarget_ReturnsPositive() {
		assertTrue(version.compareTo(new JavaVersion("1.8.0_25")) > 0);
	}

	@Test
	public void compareTo_SourceBiggerVersionThanTargetInMinor_ReturnsPositive() {
		assertTrue(version.compareTo(new JavaVersion("1.7.0_50")) > 0);
	}
}
