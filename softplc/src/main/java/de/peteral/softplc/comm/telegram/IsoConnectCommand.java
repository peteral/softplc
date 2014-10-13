package de.peteral.softplc.comm.telegram;

import de.peteral.softplc.comm.CommunicationTaskFactory;
import de.peteral.softplc.comm.ServerDataEvent;
import de.peteral.softplc.comm.tasks.IsoConnectTask;
import de.peteral.softplc.datatype.DataTypeUtils;
import de.peteral.softplc.model.CommunicationTask;
import de.peteral.softplc.model.TaskFactory;

/**
 * This telegram is sent by the client in order to create ISO connection.
 *
 * @author peteral
 *
 */
// TODO - network byte order = big endian, intel - little endian
public class IsoConnectCommand implements TaskFactory {
	private static final int OFFSET_RACK_AND_SLOT = 18;
	// +0:4 - RFC header
	// ++0:1 - RFC protocol version
	private final byte version = 0x03;
	// ++1:1 - reserved
	private final byte reserved = 0x00;
	// ++2:2 - packet length - RFC Header + ISO Header + source + destination +
	// tpud =
	// 4 + 7 + 4 + 4 + 3 = 26
	private final int packetLength = 26;

	// +4:7 - ISO header
	// ++0:1 - length of ISO data
	private final byte lengthIso = packetLength - 5;
	// ++1:1 - TODO
	private final byte code = (byte) 0xE0;
	// ++2:2 - destination TSAP
	private final int destinationTsap = 0x00;
	// ++4:2 - source TSAP
	private final int sourceTsap = 0x01;
	// ++6:1 - class
	private final byte cls = 0x00;

	// +11:4 - source
	// ++0:1 - destination code TSAP ID
	private final byte sourceCodeTsapId = (byte) 0xC1;
	// ++1:1 - destination length TSAP ID
	private final byte sourceCodeTsapLength = 0x02;
	// ++2:2 - destination data
	private final byte[] sourceData = { 0x01, 0x00 };

	// +15:4 - destination
	// ++0:1 - destination code TSAP ID
	private final byte destinationCodeTsapId = (byte) 0xC2;
	// ++1:1 - destination length TSAP ID
	private final byte destinationCodeTsapLength = 0x02;
	// +2:2 - destination data
	// +0 - 1 = PG, 2 = visu, 3 = other; use 2
	// +1 - rack << 5 | slot
	// this is the only interesting stuff - rack + slot number
	private byte[] destinationData;

	// +19:3 - TPUD
	// +0:1 - TDPU size code id
	private final byte tdpuSizeCodeId = (byte) 0xC0;
	// +1:1 - TDPU size length
	private final byte tdpuSizeLength = 0x01;
	// +2:1 - TDPU data
	private final byte[] tdpuData = { 0x09 };

	@Override
	public boolean canHandle(ServerDataEvent dataEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CommunicationTask createTask(ServerDataEvent dataEvent,
			CommunicationTaskFactory factory) {
		int rackAndSlot = DataTypeUtils
				.byteToInt(dataEvent.getData()[OFFSET_RACK_AND_SLOT]);

		int rack = rackAndSlot & (0xE0 >> 5);
		int slot = rackAndSlot & 0x1F;

		ClientChannelCache.getInstance()
				.addChannel(dataEvent.getSocket(), slot);

		return new IsoConnectTask(dataEvent.getServer(), dataEvent.getSocket(),
				rack, slot, factory);
	}

}
