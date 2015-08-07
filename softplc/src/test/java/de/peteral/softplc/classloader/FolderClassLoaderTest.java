package de.peteral.softplc.classloader;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class FolderClassLoaderTest {

	private FolderClassLoader classLoader;

	@Before
	public void setup() throws URISyntaxException {
		classLoader = new FolderClassLoader(new File(getClass().getResource("/cl").toURI()));
	}

	@Test
	public void getURLs_FolderWith2JarFiles_ReturnsCorrectURLs() {
		URL[] urls = classLoader.getURLs();

		assertArrayEquals(urls,
				new URL[] { getClass().getResource("/cl/file1.jar"), getClass().getResource("/cl/file2.jar"), });
	}
}
