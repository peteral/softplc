package de.peteral.softplc.program;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Translates programs into pure executable JavaScript.
 *
 * @author peteral
 */
public class Precompiler
{
    private static final List<Function<String, String>> OPERATIONS =
        new ArrayList<>();
    static
    {
        /* @formatter:off */
		// first replace all write accesses (start of line) before assignment
		OPERATIONS.add(s -> s.replaceAll("(\\s*)\\$\\{([^}]*)}\\s*\\=\\s*([^;]*);", "$1memory.write($2, $3);"));
		// what remains are read accesses
		OPERATIONS.add(s -> s.replaceAll("\\$\\{([^}]*)}", "memory.read($1)"));
	}
	/* @formatter:on */

    /**
     * Translates program with memory access tags into executable javascript.
     *
     * @param input
     * @return translated script.
     */
    public String translate(String input)
    {
        String result = input;

        for ( Function<String, String> operation : OPERATIONS )
        {
            result = operation.apply(result);
        }
        return result;
    }
}
