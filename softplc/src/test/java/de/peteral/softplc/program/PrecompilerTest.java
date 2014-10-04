package de.peteral.softplc.program;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.program.Precompiler;

@SuppressWarnings("javadoc")
public class PrecompilerTest {

	private Precompiler precompiler;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		precompiler = new Precompiler();
	}

	@Test
	public void translate_ValidFile_ReturnsCorrectScript() throws IOException {
		String input = readFile("./src/test/resources/program/main.js");
		String output = readFile("./src/test/resources/program/main_translated.js");

		assertEquals(output, precompiler.translate(input));
	}

	private String readFile(String filename) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(filename));
		return new String(bytes);
	}
}
