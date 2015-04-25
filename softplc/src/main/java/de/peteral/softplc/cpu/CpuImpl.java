package de.peteral.softplc.cpu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.CpuStatus;
import de.peteral.softplc.model.ErrorLog;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.model.ProgramCycleObserver;

/**
 * {@link Cpu} implementation.
 *
 * @author peteral
 */
public class CpuImpl
    implements Cpu, ProgramCycleObserver
{
    private CpuStatus status = CpuStatus.STOP;
    private final ErrorLog errorlog;
    private final ScheduledThreadPoolExecutor executor;
    private Program program;
    private final List<CommunicationTask> pendingTasks = new ArrayList<>();
    private final Memory memory;
    private final int slot;
    private final int maxBlockSize;

    /**
     * Creates a new instance.
     *
     * @param slot
     *        slot number
     * @param errorlog
     *        {@link ErrorLog} instance associated with this {@link Cpu}
     * @param executor
     *        executor responsible for this {@link Cpu}
     * @param memory
     *        {@link Memory} instance of this {@link Cpu}
     * @param maxBlockSize
     *        maximum data block size transferable via one PUT/GET telegram
     */
    public CpuImpl(int slot,
                   ErrorLog errorlog,
                   ScheduledThreadPoolExecutor executor,
                   Memory memory,
                   int maxBlockSize)
    {
        this.slot = slot;
        this.errorlog = errorlog;
        this.executor = executor;
        this.memory = memory;
        this.maxBlockSize = maxBlockSize;
    }

    @Override
    public CpuStatus getStatus()
    {
        return status;
    }

    @Override
    public void start()
    {
        if ( getStatus() == CpuStatus.ERROR )
        {
            return;
        }

        setStatus(CpuStatus.RUN);

        program.addObserver(this);

        executor.scheduleAtFixedRate(program,
                                     0,
                                     getTargetCycleTime(),
                                     TimeUnit.MILLISECONDS);
    }

    private void setStatus(CpuStatus status)
    {
        this.status = status;
        errorlog.log(Level.INFO, this.toString(), "Status changed: " + status);
    }

    @Override
    public void stop()
    {
        if ( getStatus() != CpuStatus.RUN )
        {
            return;
        }

        setStatus(CpuStatus.STOP);

        executor.shutdown();

        program.removeObserver(this);
    }

    @Override
    public void loadProgram(Program program)
    {
        if ( !program.compile() )
        {
            setStatus(CpuStatus.ERROR);
        }

        this.program = program;
    }

    @Override
    public ErrorLog getErrorLog()
    {
        return errorlog;
    }

    @Override
    public long getTargetCycleTime()
    {
        return program.getTargetCycleTime();
    }

    @Override
    public void afterCycleEnd()
    {
        synchronized ( pendingTasks )
        {
            pendingTasks.forEach(task -> task.execute(this));
        }
    }

    @Override
    public void addCommunicationTask(CommunicationTask task)
    {
        synchronized ( pendingTasks )
        {
            pendingTasks.add(task);
        }
    }

    @Override
    public Memory getMemory()
    {
        return memory;
    }

    @Override
    public boolean onError(String context, Throwable e)
    {
        errorlog.log(Level.SEVERE, this.toString(), "Exception caught when "
            + context + " : " + e);
        setStatus(CpuStatus.ERROR);

        executor.shutdown();

        program.removeObserver(this);

        // TODO possibility to set CPU to "ignore error" mode
        return true;
    }

    @Override
    public Logger getLogger()
    {
        return Logger.getLogger(toString());
    }

    @Override
    public String toString()
    {
        return "cpu." + slot;
    }

    @Override
    public int getSlot()
    {
        return slot;
    }

    @Override
    public Program getProgram()
    {
        return program;
    }

    @Override
    public int getMaxDataSize()
    {
        return maxBlockSize;
    }
}
