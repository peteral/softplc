package de.peteral.softplc.memorytables;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryTableVariable;

@SuppressWarnings("javadoc")
public class MemoryTableWriteTaskTest {

	private static final String VALUE2 = "[1, 2, 3]";
	private static final String VALUE1 = "10";
	private static final String VAR2 = "var2";
	private static final String VAR1 = "var1";
	private MemoryTableWriteTask task;
	@Mock
	private Cpu cpu;
	@Mock
	private Memory memory;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(cpu.getMemory()).thenReturn(memory);

		task = new MemoryTableWriteTask(new MemoryTableVariable(VAR1, VALUE1),
				new MemoryTableVariable(VAR2, VALUE2));
	}

	@Test
	public void execute_MockedCpu_WriteInvokedWithCorrectParametersOnMemory() {
		task.execute(cpu);

		verify(memory).parse(VAR1, VALUE1);
		verify(memory).parse(VAR2, VALUE2);
	}
}
