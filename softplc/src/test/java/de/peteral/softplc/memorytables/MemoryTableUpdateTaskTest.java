package de.peteral.softplc.memorytables;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;

@SuppressWarnings("javadoc")
public class MemoryTableUpdateTaskTest {
	private static final Integer[] VALUE2 = new Integer[] { 1, 2, 3 };
	private static final long VALUE1 = 10L;
	private static final String VAR2 = "var2";
	private static final String VAR1 = "var1";
	private MemoryTable memoryTable;
	private MemoryTableUpdateTask task;
	@Mock
	private Cpu cpu;
	@Mock
	private Memory memory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		memoryTable = new MemoryTable();
		memoryTable.getVariables().add(new MemoryTableVariable(VAR1, ""));
		memoryTable.getVariables().add(new MemoryTableVariable(VAR2, ""));

		when(cpu.getMemory()).thenReturn(memory);
		when(memory.read(VAR1)).thenReturn(VALUE1);
		when(memory.read(VAR2)).thenReturn(VALUE2);

		task = new MemoryTableUpdateTask(memoryTable);
	}

	@Test
	public void execute_ValidConfig_UpdatesVariablesInTable() {
		task.execute(cpu);

		assertEquals("10", memoryTable.getVariables().get(0).getCurrentValue()
				.get());
		assertEquals("[1, 2, 3]", memoryTable.getVariables().get(1)
				.getCurrentValue().get());
	}
}
