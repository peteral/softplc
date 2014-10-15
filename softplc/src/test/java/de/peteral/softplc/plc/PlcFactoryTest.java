package de.peteral.softplc.plc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.Plc;

@SuppressWarnings("javadoc")
public class PlcFactoryTest {
	private PlcFactory factory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		factory = new PlcFactory();
	}

	@Test
	public void create_ValidFile_ReturnsValidPlcInstance() {
		Plc plc = factory.create("./src/test/resources/config.xml");

		assertEquals(2, plc.getCpuCount());

		Cpu cpu = plc.getCpu(1);
		Memory memory = cpu.getMemory();

		assertEquals(65535, memory.getMemoryArea("M").getSize());
		assertEquals(2000, memory.getMemoryArea("DB100").getSize());
		assertEquals(1000, memory.getMemoryArea("DB101").getSize());
		assertEquals(50, cpu.getTargetCycleTime());
		assertEquals(222, cpu.getMaxDataSize());

		assertNotNull(cpu.getProgram());
	}
}
