package de.peteral.softplc.comm.tasks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.protocol.CommunicationTask;
import de.peteral.softplc.protocol.Protocol;
import de.peteral.softplc.protocol.ProtocolDefinition;
import de.peteral.softplc.protocol.ResponseFactory;
import de.peteral.softplc.protocol.TaskFactory;
import de.peteral.softplc.reflection.AnnotationProcessor;

/**
 * Translates byte arrays from / to communication tasks.
 *
 * @author peteral
 *
 */
public class CommunicationTaskFactory {
	private static final List<ProtocolDefinition> PROTOCOLS = new ArrayList<>();
	private static final Map<Integer, ProtocolDefinition> PROTOCOLS_BY_PORT = new ConcurrentHashMap<>();
	private static final Logger LOGGER = Logger.getLogger("communication");
	private static CommunicationTaskFactory instance = null;

	static {
		new AnnotationProcessor<ProtocolDefinition>(Protocol.class).loadAnnotations(PROTOCOLS);

		PROTOCOLS.forEach(protocol -> {
			protocol.getPorts().forEach(port -> {
				PROTOCOLS_BY_PORT.put(port, protocol);
			});
		});
	}

	private CommunicationTaskFactory() {

	}

	public static CommunicationTaskFactory getFactory() {
		if (instance == null) {
			instance = new CommunicationTaskFactory();
		}

		return instance;
	}

	/**
	 * Creates a {@link CommunicationTask} from FRC1006 request.
	 *
	 * @param dataEvent
	 *            client request containing the data of the RFC1006 request.
	 * @return communication task instance
	 */
	public CommunicationTask createTask(ServerDataEvent dataEvent) {
		InetSocketAddress addr;
		try {
			addr = (InetSocketAddress) dataEvent.getSocket().getLocalAddress();
			ProtocolDefinition protocol = PROTOCOLS_BY_PORT.get(addr.getPort());

			for (TaskFactory factory : protocol.getTaskFactories()) {
				if (factory.canHandle(dataEvent)) {
					return factory.createTask(dataEvent, this);
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error retrieving port number", e);
		}
		return null;
	}

	/**
	 * Creates a full RFC1006 response from Put/Get byte array returned by a
	 * CommunicationTask
	 *
	 * @param task
	 * @return response as byte array.
	 */
	public byte[] createResponse(CommunicationTask task) {
		// FIXME iterating through all protocols here - this can be optimized

		for (ProtocolDefinition protocol : PROTOCOLS) {
			for (ResponseFactory factory : protocol.getResponseFactories()) {
				if (factory.canHandle(task)) {
					return factory.createResponse(task);
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @return array containing all ports to listen to.
	 */
	public Integer[] getAllPorts() {
		return PROTOCOLS_BY_PORT.keySet().toArray(new Integer[PROTOCOLS_BY_PORT.size()]);
	}

	/**
	 * Logs all registered tasks into a string builder:
	 *
	 * @param builder
	 */
	public void logContents(StringBuilder builder) {
		PROTOCOLS_BY_PORT.entrySet().forEach(entry -> {
			builder.append(entry.getValue().getName());
			builder.append(" (");
			builder.append(entry.getKey());
			builder.append(")\n");
		});
	}
}
