package de.peteral.softplc.impl.program;

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
		// ${M,W100} = ${M,W100}.intValue() + 1; -> memory.write("M,W100", ${M,W100}.intValue() + 1);
		String translated = input.replaceAll("(\\s*)\\$\\{([^}]*)}\\s*\\=\\s*([^;]*);", "$1memory.write(\"$2\", $3);");

		// second replace all read accesses
		// ${M,W100} -> memory.read("M,W100")
		translated = translated.replaceAll("\\$\\{([^}]*)}", "memory.read(\"$1\")");

		return translated;
	}
	/* @formatter:on */
}
