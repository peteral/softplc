package de.peteral.softplc.cpu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import de.peteral.softplc.executor.ScheduledThreadPoolExecutorFactory;
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
public class CpuImpl implements Cpu, ProgramCycleObserver {
	private CpuStatus status = CpuStatus.STOP;
	private final StringProperty statusProperty = new SimpleStringProperty(
			status.toString());
	private final ErrorLog errorlog;
	private ScheduledThreadPoolExecutor executor;
	private Program program;
	private final List<CommunicationTask> pendingTasks = new ArrayList<>();
	private final Memory memory;
	private final IntegerProperty slot;
	private final int maxBlockSize;
	private final ScheduledThreadPoolExecutorFactory executorFactory;
	private final IntegerProperty maxConnections = new SimpleIntegerProperty();
	private final StringProperty name = new SimpleStringProperty();

	/**
	 * Creates a new instance.
	 *
	 * @param name
	 *            CPU name (only information)
	 *
	 * @param slot
	 *            slot number
	 * @param errorlog
	 *            {@link ErrorLog} instance associated with this {@link Cpu}
	 * @param executorFactory
	 *            executor responsible for this {@link Cpu}
	 * @param memory
	 *            {@link Memory} instance of this {@link Cpu}
	 * @param maxBlockSize
	 *            maximum data block size transferable via one PUT/GET telegram
	 * @param maxConnections
	 */
	public CpuImpl(String name, int slot, ErrorLog errorlog,
			ScheduledThreadPoolExecutorFactory executorFactory, Memory memory,
			int maxBlockSize, int maxConnections) {
		this.executorFactory = executorFactory;
		this.slot = new SimpleIntegerProperty(slot);
		this.errorlog = errorlog;
		this.memory = memory;
		this.maxBlockSize = maxBlockSize;
		this.maxConnections.set(maxConnections);
		this.name.set(name);
	}

	@Override
	public CpuStatus getStatus() {
		return status;
	}

	// TODO possibly we need warm and cold restart
	@Override
	public void start() {
		if (getStatus() == CpuStatus.ERROR) {
			return;
		}

		executor = executorFactory.createExecutor();

		setStatus(CpuStatus.RUN);

		program.addObserver(this);

		executor.scheduleAtFixedRate(program, 0, getTargetCycleTime(),
				TimeUnit.MILLISECONDS);
	}

	private void setStatus(CpuStatus status) {
		this.status = status;
		getStatusProperty().set(status.toString());
		errorlog.log(Level.INFO, this.toString(), "Status changed: " + status);
	}

	@Override
	public void stop() {
		if (getStatus() != CpuStatus.RUN) {
			return;
		}

		setStatus(CpuStatus.STOP);

		program.resetCycleTime();

		executor.shutdown();

		program.removeObserver(this);
	}

	@Override
	public void loadProgram(Program program) {
		if (!program.compile()) {
			setStatus(CpuStatus.ERROR);
		}

		this.program = program;
	}

	@Override
	public ErrorLog getErrorLog() {
		return errorlog;
	}

	@Override
	public long getTargetCycleTime() {
		return program.getTargetCycleTime().get();
	}

	@Override
	public void afterCycleEnd() {
		synchronized (pendingTasks) {
			try {
				pendingTasks.forEach(task -> task.execute(this));
			} catch (Exception e) {
				e.printStackTrace();
			}

			pendingTasks.clear();
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
	public boolean onError(String context, Throwable e) {
		errorlog.log(Level.SEVERE, this.toString(), "Exception caught when "
				+ context + " : " + e);
		setStatus(CpuStatus.ERROR);

		if (executor != null) {
			executor.shutdown();
		}

		program.removeObserver(this);

		// TODO possibility to set CPU to "ignore error" mode
		return true;
	}

	@Override
	public Logger getLogger() {
		return Logger.getLogger(toString());
	}

	@Override
	public String toString() {
		return "cpu." + slot.get();
	}

	@Override
	public IntegerProperty getSlot() {
		return slot;
	}

	@Override
	public Program getProgram() {
		return program;
	}

	@Override
	public int getMaxDataSize() {
		return maxBlockSize;
	}

	@Override
	public StringProperty getStatusProperty() {
		return statusProperty;
	}

	@Override
	public IntegerProperty getMaxConnections() {
		return maxConnections;
	}

	@Override
	public StringProperty getName() {
		return name;
	}
}
