package de.peteral.softplc.plc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javafx.beans.property.SimpleIntegerProperty;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.model.PutGetServer;

@SuppressWarnings("javadoc")
public class PlcImplTest {

	private Plc plc;

	@Mock
	private Cpu cpu1;
	@Mock
	private Cpu cpu2;
	@Mock
	private PutGetServer server;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(cpu2.getSlot()).thenReturn(new SimpleIntegerProperty(1));
		when(cpu1.getSlot()).thenReturn(new SimpleIntegerProperty(3));
		Cpu[] cpus = { cpu1, cpu2 };

		plc = new PlcImpl(null, server, cpus);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void getCpu_InvalidSlot_ThrowsException() {
		plc.getCpu(2);
	}

	@Test
	public void hasCpu_ValidSlot_ReturnsTrue() {
		assertTrue(plc.hasCpu(1));
	}

	@Test
	public void hasCpu_InvalidSlot_ReturnsFalse() {
		assertFalse(plc.hasCpu(2));
	}

	@Test
	public void getCpu_ValidSlot_ReturnsCorrectInstance() {
		assertSame(cpu2, plc.getCpu(1));
	}

	@Test
	public void getCpuCount_None_ReturnsCorrectCount() {
		assertEquals(2, plc.getCpuCount());
	}

	@Test
	public void start_None_StartsAllCpus() {
		plc.start();

		verify(cpu1).start();
		verify(cpu2).start();
	}

	@Test
	public void start_None_StartsPutGetServer() throws IOException {
		plc.start();

		verify(server).start(plc);
	}

	@Test
	public void stop_None_StopsPutGetServer() throws IOException {
		plc.stop();

		verify(server).stop();
	}

	@Test
	public void stop_None_StopsAllCpus() {
		plc.stop();

		verify(cpu1).stop();
		verify(cpu2).stop();
	}
}
