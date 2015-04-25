package de.peteral.softplc.cpu;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.CpuStatus;
import de.peteral.softplc.model.ErrorLog;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.Program;

@SuppressWarnings("javadoc")
public class CpuImplTest
{

    private static final int MAX_BLOCK_SIZE = 123;
    private static final long TARGET_CYCLE_TIME = 50;
    private CpuImpl cpu;
    @Mock
    private Program program;
    @Mock
    private ErrorLog errorlog;
    @Mock
    private ScheduledThreadPoolExecutor executor;
    @Mock
    private CommunicationTask task1;
    @Mock
    private Memory memory;
    @Mock
    private Throwable exception;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        when(program.getTargetCycleTime()).thenReturn(TARGET_CYCLE_TIME);

        cpu = new CpuImpl(0, errorlog, executor, memory, MAX_BLOCK_SIZE);
    }

    @Test
    public void getMemory_None_ReturnsMemory()
    {
        assertSame(memory, cpu.getMemory());
    }

    @Test
    public void getStatus_NewInstance_ReturnsStop()
    {
        assertEquals(CpuStatus.STOP, cpu.getStatus());
    }

    @Test
    public void loadProgram_ValidProgram_StatusRemainsStop()
    {
        when(program.compile()).thenReturn(true);

        cpu.loadProgram(program);

        assertEquals(CpuStatus.STOP, cpu.getStatus());
    }

    @Test
    public void loadProgram_InvalidProgram_StatusReturnsError()
    {
        when(program.compile()).thenReturn(false);

        cpu.loadProgram(program);

        assertEquals(CpuStatus.ERROR, cpu.getStatus());
    }

    @Test
    public void start_StateError_RemainsInErrorNoExecutionScheduled()
    {
        when(program.compile()).thenReturn(false);
        cpu.loadProgram(program);

        cpu.start();

        assertEquals(CpuStatus.ERROR, cpu.getStatus());

        verify(executor, never()).scheduleAtFixedRate(any(Runnable.class),
                                                      anyLong(),
                                                      anyLong(),
                                                      any(TimeUnit.class));
    }

    @Test
    public void start_StateStop_SchedulesProgramExecutionImmediatellyAndGoesToRunStatus()
    {
        when(program.compile()).thenReturn(true);
        cpu.loadProgram(program);

        cpu.start();

        assertEquals(CpuStatus.RUN, cpu.getStatus());

        verify(executor).scheduleAtFixedRate(program,
                                             0,
                                             TARGET_CYCLE_TIME,
                                             TimeUnit.MILLISECONDS);
    }

    @Test
    public void start_StateStop_ProgramObserverAdded()
    {
        when(program.compile()).thenReturn(true);
        cpu.loadProgram(program);

        cpu.start();

        verify(program).addObserver(cpu);
    }

    @Test
    public void stop_StateRun_ShutsExecutorDownGoesToStopState()
    {
        when(program.compile()).thenReturn(true);
        cpu.loadProgram(program);

        cpu.start();

        cpu.stop();

        assertEquals(CpuStatus.STOP, cpu.getStatus());
        verify(executor).shutdown();
        verify(program).removeObserver(cpu);
    }

    @Test
    public void stop_StatusNotRunning_DoesNothing()
    {
        cpu.stop();

        verify(executor, never()).shutdown();
    }

    @Test
    public void afterCycleEnd_OneCommunicationTaskPending_ExecutesCommunicationTask()
    {
        cpu.addCommunicationTask(task1);

        cpu.afterCycleEnd();

        verify(task1).execute(cpu);
    }

    @Test
    public void getErrorLog_None_ReturnsErrorlog()
    {
        assertEquals(errorlog, cpu.getErrorLog());
    }

    @Test
    public void onError_Mock_LogsToErrorlog()
    {
        cpu.loadProgram(program);

        cpu.onError("", exception);

        verify(errorlog).log(eq(Level.SEVERE), anyString(), anyString());
    }

    @Test
    public void onError_Mock_StatusChangesToError()
    {
        cpu.loadProgram(program);

        cpu.onError("", exception);

        assertEquals(CpuStatus.ERROR, cpu.getStatus());
    }

    @Test
    public void onError_Mock_UnregistersWithProgram()
    {
        cpu.loadProgram(program);

        cpu.onError("", exception);

        verify(program).removeObserver(cpu);
    }

    @Test
    public void onError_Mock_ShutsDownExecutor()
    {
        cpu.loadProgram(program);

        cpu.onError("", exception);

        verify(executor).shutdown();
    }
}
