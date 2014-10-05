package de.peteral.softplc.program;

/**
 * Translates programs into pure executable JavaScript.
 *
 * @author peteral
 *
 */
public class Precompiler {

	/**
	 * Translates program with memory access tags into executable javascript.
	 *
	 * @param input
	 * @return translated script.
	 */
	/* @formatter:off */
	public String translate(String input) {
		// first replace all write accesses (start of line) before assignment
		String translated = input.replaceAll("(\\s*)\\$\\{([^}]*)}\\s*\\=\\s*([^;]*);", "$1memory.write(\"$2\", $3);");

		// what remains are read accesses
		translated = translated.replaceAll("\\$\\{([^}]*)}", "memory.read(\"$1\")");

		return translated;
	}
	/* @formatter:on */
}
