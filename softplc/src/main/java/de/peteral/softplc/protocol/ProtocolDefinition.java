package de.peteral.softplc.protocol;

import java.util.List;

/**
 * Defines a communication protocol.
 *
 * @author peteral
 *
 */
public interface ProtocolDefinition {
	/**
	 *
	 * @return array containing a list of communication task factories of this
	 *         protocol (byte array -> command)
	 */
	List<TaskFactory> getTaskFactories();

	/**
	 *
	 * @return array containing list of response factories (executed command ->
	 *         byte array)
	 */
	List<ResponseFactory> getResponseFactories();

	/**
	 *
	 * @return array containing list of ports this protocol is bound to
	 */
	List<Integer> getPorts();

	/**
	 *
	 * @return protocol name (for showing active protocols)
	 */
	String getName();
}
