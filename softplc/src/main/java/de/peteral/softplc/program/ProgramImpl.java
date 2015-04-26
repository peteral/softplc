package de.peteral.softplc.program;

import java.util.ArrayList;
import java.util.List;

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

/**
 * Default {@link Program} implementation.
 *
 * @author peteral
 */
public class ProgramImpl
    implements Program
{
    private static final String MAIN = "main();";
    private static final String JAVASCRIPT = "text/javascript";
    private final List<ProgramCycleObserver> observers = new ArrayList<>();
    private final ScriptEngineManager scriptEngineManager;
    private final Precompiler precompiler;
    private final String[] sources;
    private ScriptContext context;
    private CompiledScript compiled;
    private final Cpu cpu;
    private final long targetCycleTime;

    /**
     * Creates a new instance.
     *
     * @param cpu
     *        Associated {@link Cpu} instance.
     * @param scriptEngineManager
     *        script engine manager instance
     * @param precompiler
     *        {@link Precompiler} instance
     * @param targetCycleTime
     *        target cycle time in [ms]
     * @param sources
     *        java script code with memory access tags
     */
    public ProgramImpl(Cpu cpu,
                       ScriptEngineManager scriptEngineManager,
                       Precompiler precompiler,
                       long targetCycleTime,
                       String... sources)
    {

        this.cpu = cpu;
        this.scriptEngineManager = scriptEngineManager;
        this.precompiler = precompiler;
        this.targetCycleTime = targetCycleTime;
        this.sources = sources;
    }

    @Override
    public void run()
    {
        try
        {
            compiled.eval(context);
        }
        catch ( Exception e )
        {
            observers.forEach(observer -> observer.onError("executing main()",
                                                           e));
        }

        // notify observers
        observers.forEach(observer -> observer.afterCycleEnd());
    }

    @Override
    public boolean compile()
    {
        try
        {
            ScriptEngine engine =
                scriptEngineManager.getEngineByMimeType(JAVASCRIPT);
            Compilable compiler = (Compilable) engine;

            context = new SimpleScriptContext();
            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
            bindings.put("memory", cpu.getMemory());
            bindings.put("logger", cpu.getLogger());

            for ( String source : sources )
            {
                String precompiled = precompiler.translate(source);
                CompiledScript compiledScript = compiler.compile(precompiled);
                compiledScript.eval(context);
            }
            compiled = compiler.compile(MAIN);

            return true;
        }
        catch ( ScriptException e )
        {
            for ( ProgramCycleObserver observer : observers )
            {
                observer.onError("Compilation error: ", e);
            }
            return false;
        }
    }

    @Override
    public void addObserver(ProgramCycleObserver observer)
    {
        if ( !observers.contains(observer) )
        {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ProgramCycleObserver observer)
    {
        observers.remove(observer);
    }

    @Override
    public long getTargetCycleTime()
    {
        return targetCycleTime;
    }

}
