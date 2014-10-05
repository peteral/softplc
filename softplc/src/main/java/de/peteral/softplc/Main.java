package de.peteral.softplc;

import de.peteral.softplc.model.Plc;
import de.peteral.softplc.plc.PlcFactory;

/**
 * Application entry point.
 *
 * @author peteral
 *
 */
public final class Main {
	private Main() {

	}

	/**
	 * Application entry point.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if (!checkParameters(args)) {
			return;
		}

		Plc plc = new PlcFactory().create(args[0]);

		plc.start();

	}

	private static boolean checkParameters(String[] args) {
		if (args.length == 1) {
			return true;
		}

		System.out.println("Usage: java -jar softplc.jar config-file.xml");

		return false;
	}
}
