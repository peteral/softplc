package de.peteral.softplc.program;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.model.ProgramCycleObserver;
import de.peteral.softplc.view.ErrorDialog;

/**
 * Default {@link Program} implementation.
 *
 * @author peteral
 */
public class ProgramImpl implements Program {
	private static final String MAIN = "main();";
	private static final String JAVASCRIPT = "text/javascript";
	private final List<ProgramCycleObserver> observers = new ArrayList<>();
	private final ScriptEngineManager scriptEngineManager;
	private final Precompiler precompiler;
	private final ObservableList<ScriptFile> scriptFiles = FXCollections
			.observableArrayList();
	private ScriptContext context;
	private CompiledScript compiled;
	private final Cpu cpu;
	private final LongProperty targetCycleTime;
	private final LongProperty currentCycleTime = new SimpleLongProperty();

	private int currentIndex = 0;
	private final long[] executionDuration = new long[10];

	/**
	 * Creates a new instance.
	 *
	 * @param cpu
	 *            Associated {@link Cpu} instance.
	 * @param scriptEngineManager
	 *            script engine manager instance
	 * @param precompiler
	 *            {@link Precompiler} instance
	 * @param targetCycleTime
	 *            target cycle time in [ms]
	 * @param sources
	 *            java script code with memory access tags
	 */
	public ProgramImpl(Cpu cpu, ScriptEngineManager scriptEngineManager,
			Precompiler precompiler, long targetCycleTime,
			ScriptFile... sources) {

		this.cpu = cpu;
		this.scriptEngineManager = scriptEngineManager;
		this.precompiler = precompiler;
		this.targetCycleTime = new SimpleLongProperty(targetCycleTime);
		getScriptFiles().addAll(sources);

	}

	@Override
	public void run() {
		long before = System.currentTimeMillis();
		try {

			compiled.eval(context);

		} catch (Exception e) {
			observers.forEach(observer -> observer.onError("executing main()",
					e));
		}

		// notify observers
		observers.forEach(observer -> observer.afterCycleEnd());
		long duration = System.currentTimeMillis() - before;

		updateCycleTime(duration);

	}

	private void updateCycleTime(long duration) {
		executionDuration[currentIndex] = duration;
		currentIndex++;

		if (currentIndex > 9) {
			currentIndex = 0;

			long total = 0;
			for (long x : executionDuration) {
				total += x;
			}

			currentCycleTime.set(total / 10);
		}

	}

	@Override
	public boolean compile() {
		String currentStep = "start";
		try {
			ScriptEngine engine = scriptEngineManager
					.getEngineByMimeType(JAVASCRIPT);
			Compilable compiler = (Compilable) engine;

			context = new SimpleScriptContext();
			Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
			bindings.put("memory", cpu.getMemory());
			bindings.put("logger", cpu.getLogger());

			for (ScriptFile file : getScriptFiles()) {
				currentStep = "File: " + file.getFileName().get();
				String precompiled = precompiler.translate(file.getSource()
						.get());
				CompiledScript compiledScript = compiler.compile(precompiled);
				compiledScript.eval(context);
			}
			currentStep = "main()";
			compiled = compiler.compile(MAIN);

			return true;
		} catch (ScriptException e) {
			Logger.getLogger("compiler").log(Level.SEVERE,
					"Compiler error during [" + currentStep + "]", e);
			for (ProgramCycleObserver observer : observers) {
				observer.onError("Compilation error [" + currentStep + "]: ", e);
			}
			return false;
		}
	}

	@Override
	public void addObserver(ProgramCycleObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeObserver(ProgramCycleObserver observer) {
		observers.remove(observer);
	}

	@Override
	public LongProperty getTargetCycleTime() {
		return targetCycleTime;
	}

	@Override
	public ObservableList<ScriptFile> getScriptFiles() {
		return scriptFiles;
	}

	@Override
	public LongProperty getCurrentCycleTime() {
		return currentCycleTime;
	}

	@Override
	public void resetCycleTime() {
		currentIndex = 0;
		currentCycleTime.set(0L);
	}

	@Override
	public void reloadFromDisk() {
		getScriptFiles().forEach(
				file -> {
					try {
						file.reload();
					} catch (Exception e) {
						ErrorDialog.show("Failed loading file ["
								+ file.getFileName().get() + "]", e);
					}
				});
	}

}
