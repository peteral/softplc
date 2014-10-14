package de.peteral.softplc.plc;

import java.io.IOException;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.PutGetServer;

/**
 * {@link Plc} implementation.
 *
 * @author peteral
 *
 */
public class PlcImpl implements Plc {

	private final Cpu[] cpus;
	private final PutGetServer server;

	/**
	 * Default constructor.
	 *
	 * @param server
	 *            {@link PutGetServer} implementation instance
	 * @param cpus
	 *            list of {@link Cpu} units managed by this PLC.
	 */
	public PlcImpl(PutGetServer server, Cpu... cpus) {
		this.server = server;
		this.cpus = cpus;

	}

	@Override
	public Cpu getCpu(int slot) {
		for (Cpu cpu : cpus) {
			if (cpu.getSlot() == slot) {
				return cpu;
			}
		}

		throw new ArrayIndexOutOfBoundsException("Invalid CPU slot: " + slot);
	}

	@Override
	public int getCpuCount() {
		return cpus.length;
	}

	@Override
	public void start() {
		for (Cpu cpu : cpus) {
			cpu.start();
		}

		try {
			server.start(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			server.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Cpu cpu : cpus) {
			cpu.stop();
		}
	}

	@Override
	public boolean hasCpu(int slot) {
		for (Cpu cpu : cpus) {
			if (cpu.getSlot() == slot) {
				return true;
			}
		}

		return false;
	}

}
