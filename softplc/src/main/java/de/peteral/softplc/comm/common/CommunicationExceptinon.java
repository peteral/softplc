package de.peteral.softplc.comm.common;


public class CommunicationExceptinon extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommunicationExceptinon(String message, Throwable reason) {
		super(message, reason);
	}

}
