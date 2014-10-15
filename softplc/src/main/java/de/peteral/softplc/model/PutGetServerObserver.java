package de.peteral.softplc.model;

/**
 * Classes implementing this interface can register with the
 * {@link PutGetServer} and receive information about data exchange with
 * clients.
 *
 * @author peteral
 *
 */
public interface PutGetServerObserver {
	/**
	 * Invoked after each processed telegram processed.
	 *
	 * @param e
	 *            event containing information about the data exchange.
	 */
	void onTelegram(PutGetServerEvent e);
}
