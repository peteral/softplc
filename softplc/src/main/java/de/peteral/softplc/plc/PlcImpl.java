package de.peteral.softplc.plc;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.PutGetServer;
import de.peteral.softplc.view.error.ErrorDialog;

/**
 * {@link Plc} implementation.
 *
 * @author peteral
 */
public class PlcImpl implements Plc {

	private final ObservableList<Cpu> cpus;
	private final PutGetServer server;
	private File path;

	/**
	 * Default constructor.
	 *
	 * @param path
	 *            configuration file path
	 *
	 * @param server
	 *            {@link PutGetServer} implementation instance
	 * @param cpus
	 *            list of {@link Cpu} units managed by this PLC.
	 */
	public PlcImpl(String path, PutGetServer server, Cpu... cpus) {
		this.setPath((path == null) ? null : new File(path));
		this.server = server;
		this.cpus = FXCollections.observableArrayList(cpus);
	}

	@Override
	public Cpu getCpu(int slot) {
		for (Cpu cpu : cpus) {
			if (cpu.getSlot().get() == slot) {
				return cpu;
			}
		}

		throw new ArrayIndexOutOfBoundsException("Invalid CPU slot: " + slot);
	}

	@Override
	public int getCpuCount() {
		return cpus.size();
	}

	@Override
	public void start() {
		cpus.forEach(cpu -> cpu.start());

		try {
			server.start(this);
		} catch (IOException e) {
			ErrorDialog.show("Failed starting server", e);
		}
	}

	@Override
	public void stop() {
		try {
			server.stop();
		} catch (IOException e) {
			ErrorDialog.show("Failed stopping server", e);
		}

		cpus.forEach(cpu -> cpu.stop());
	}

	@Override
	public boolean hasCpu(int slot) {
		for (Cpu cpu : cpus) {
			if (cpu.getSlot().get() == slot) {
				return true;
			}
		}

		return false;
	}

	@Override
	public ObservableList<Cpu> getCpus() {
		return cpus;
	}

	@Override
	public File getPath() {
		return path;
	}

	@Override
	public void setPath(File path) {
		this.path = path;
	}

}
