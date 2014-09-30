package de.peteral.softplc.main;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

public class JavascriptTest {

	public static void main(String[] args) throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();

		ScriptEngine engine = manager.getEngineByMimeType("text/javascript");
		Compilable compiler = (Compilable) engine;

		CompiledScript compiledScript = compiler.compile("a = 20;");

		ScriptContext context = new SimpleScriptContext();
		Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

		compiledScript.eval(context);

		System.out.println(bindings.get("a"));
	}
}
