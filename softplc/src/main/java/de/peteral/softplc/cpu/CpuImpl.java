package de.peteral.softplc.cpu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.CpuStatus;
import de.peteral.softplc.model.ErrorLog;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.model.ProgramCycleObserver;

/**
 *
 * {@link Cpu} implementation.
 *
 * @author peteral
 *
 */
public class CpuImpl implements Cpu, ProgramCycleObserver {
	private CpuStatus status = CpuStatus.STOP;
	private final ErrorLog errorlog;
	private final long targetCycleTime;
	private final ScheduledThreadPoolExecutor executor;
	private Program program;
	private final List<CommunicationTask> pendingTasks = new ArrayList<>();
	private final Memory memory;

	/**
	 * Creates a new instance.
	 *
	 * @param targetCycleTime
	 *            requested program cycle duration [ms]
	 * @param errorlog
	 *            {@link ErrorLog} instance associated with this {@link Cpu}
	 * @param executor
	 *            executor responsible for this {@link Cpu}
	 * @param memory
	 *            {@link Memory} instance of this {@link Cpu}
	 */
	public CpuImpl(long targetCycleTime, ErrorLog errorlog,
			ScheduledThreadPoolExecutor executor, Memory memory) {
		this.targetCycleTime = targetCycleTime;
		this.errorlog = errorlog;
		this.executor = executor;
		this.memory = memory;
	}

	@Override
	public CpuStatus getStatus() {
		return status;
	}

	@Override
	public void start() {
		if (getStatus() == CpuStatus.ERROR) {
			return;
		}

		status = CpuStatus.RUN;

		program.addObserver(this);

		executor.scheduleAtFixedRate(program, 0, getTargetCycleTime(),
				TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop() {
		if (getStatus() != CpuStatus.RUN) {
			return;
		}

		status = CpuStatus.STOP;

		executor.shutdown();

		program.removeObserver(this);
	}

	@Override
	public void loadProgram(Program program) {
		if (!program.compile()) {
			status = CpuStatus.ERROR;
		}

		this.program = program;
	}

	@Override
	public ErrorLog getErrorLog() {
		return errorlog;
	}

	@Override
	public long getTargetCycleTime() {
		return targetCycleTime;
	}

	@Override
	public void afterCycleEnd() {
		synchronized (pendingTasks) {
			for (CommunicationTask task : pendingTasks) {
				task.execute(this);
			}
		}
	}

	@Override
	public void addCommunicationTask(CommunicationTask task) {
		synchronized (pendingTasks) {
			pendingTasks.add(task);
		}
	}

	@Override
	public Memory getMemory() {
		return memory;
	}

	@Override
	public boolean onError(Throwable e) {
		// TODO Auto-generated method stub
		return true;
	}

}
