package de.peteral.softplc.comm.tasks;

import de.peteral.softplc.comm.common.ClientChannelCache;
import de.peteral.softplc.comm.common.ServerDataEvent;
import de.peteral.softplc.datatype.DataTypeUtils;
import de.peteral.softplc.model.CommunicationTask;

/**
 * This telegram is sent by the client in order to create ISO connection.
 *
 * @author peteral
 *
 */
public class IsoConnectCommand implements TaskFactory {
	private static final int OFFSET_RACK_AND_SLOT = 18;

	private static final byte WILDCARD = (byte) 0xFF;
	// 1 = PG, 2 = visu, 3 = other; use 2
	private static final byte TYPE = WILDCARD;
	// rack << 5 | slot
	private static final byte RACK_AND_SLOT = WILDCARD;

	/* @formatter:off */
	private static final byte[] DATA = {
		0x03, 0x00, 0x00, 0x1A, // +0 RFC header
		0x15, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, // +4 ISO header
		(byte) 0xC1, 0x02, 0x01, 0x00, // +11 source
		(byte) 0xC2, 0x02, TYPE, RACK_AND_SLOT, // +15 destination
		(byte) 0xC0, 0x01, 0x09 // +19 TPUD
	};
	/* @formatter:on */

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		if (dataEvent.getData().length < DATA.length) {
			return false;
		}

		for (int i = 0; i < DATA.length; i++) {
			if ((DATA[i] != WILDCARD) && (DATA[i] != dataEvent.getData()[i])) {
				return false;
			}
		}

		return true;
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory) {
		int rackAndSlot = DataTypeUtils
				.byteToInt(dataEvent.getData()[OFFSET_RACK_AND_SLOT]);

		// rack is ignored
		int slot = rackAndSlot & 0x1F;

		ClientChannelCache.getInstance()
		.addChannel(dataEvent.getSocket(), slot);

		return new IsoConnectTask(dataEvent.getServer(), dataEvent.getSocket(),
				slot, factory);
	}

}
