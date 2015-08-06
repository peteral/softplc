package de.peteral.softplc.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryArea;
import de.peteral.softplc.model.MemoryTable;
import de.peteral.softplc.model.MemoryTableVariable;
import de.peteral.softplc.model.Plc;
import javafx.collections.FXCollections;

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

		assertEquals(65535, memory.getMemoryArea("M").getSize().get());
		assertEquals(2000, memory.getMemoryArea("DB100").getSize().get());
		assertEquals(1000, memory.getMemoryArea("DB101").getSize().get());
		assertEquals(50, cpu.getTargetCycleTime());
		assertEquals(222, cpu.getMaxDataSize().get());

		assertNotNull(cpu.getProgram());

		cpu = plc.getCpu(2);
		MemoryTable table = cpu.getMemory().getMemoryTables().get(0);
		assertNotNull(table);
		assertEquals("table1", table.getName().get());
		MemoryTableVariable variable = table.getVariables().get(1);
		assertEquals("This is a comment", variable.getComment().get());
		assertEquals("DB100,W2", variable.getVariable().get());
		assertEquals("10", variable.getNewValue().get());
	}

	@Test
	public void create_CompositeConfigFile_ReturnsValidPlcInstance() {
		Plc plc = factory.create("./src/test/resources/composite.xml");

		assertEquals(2, plc.getCpuCount());

		Cpu cpu = plc.getCpu(1);
		Memory memory = cpu.getMemory();

		assertEquals(65535, memory.getMemoryArea("M").getSize().get());
		assertEquals(2000, memory.getMemoryArea("DB100").getSize().get());
		assertEquals(1000, memory.getMemoryArea("DB101").getSize().get());
		assertEquals(50, cpu.getTargetCycleTime());
		assertEquals(222, cpu.getMaxDataSize().get());

		assertNotNull(cpu.getProgram());
	}

	@Test
	public void createMemoryArea_None_ReturnsValidInstance() {
		MemoryArea area = factory.createMemoryArea();
		assertFalse(area.isDefaultArea());
		assertEquals(PlcFactory.DEFAULT_SIZE, area.getSize().get());
		assertEquals(PlcFactory.NEW_AREA_CODE, area.getAreaCode().get());
	}

	@Test
	public void createNew_None_ReturnsNewPlcInstance() {
		Plc plc = factory.createNew();
		assertNotNull(plc);
	}

	@Test
	public void createCpu_MockedPlc_ReturnsProperlyInitializedInstance() {
		Plc plc = mock(Plc.class);
		when(plc.getCpus()).thenReturn(FXCollections.emptyObservableList());

		Cpu cpu = factory.createCpu(plc);

		assertEquals(plc, cpu.getPlc());
		assertEquals(PlcFactory.NEW_CPU_NAME, cpu.getName().get());
		assertEquals(PlcFactory.DEFAULT_MEMORY_AREAS.size(), cpu.getMemory().getMemoryAreas().size());
	}
}
