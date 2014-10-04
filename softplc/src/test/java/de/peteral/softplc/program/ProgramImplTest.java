package de.peteral.softplc.program;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.logging.Logger;

import javax.script.ScriptEngineManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.model.ProgramCycleObserver;

@SuppressWarnings("javadoc")
public class ProgramImplTest {
	/* @formatter:off */
	private static final String SOURCE =
			"function main() {\n" +
					"	${M,W100} = 10;\n" +
					"}";
	/* @formatter:on */
	private Program program;
	@Mock
	private ProgramCycleObserver observer;
	@Mock
	private Cpu cpu;
	@Mock
	private Memory memory;
	@Mock
	private Logger logger;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(cpu.getMemory()).thenReturn(memory);
		when(cpu.getLogger()).thenReturn(logger);

		program = new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), SOURCE);
	}

	@Test
	public void addObserver_Mock_RunInvokesObserver() {
		program.addObserver(observer);

		program.compile();

		program.run();

		verify(memory).write("M,W100", 10.0);
		verify(observer).afterCycleEnd();
	}

	@Test
	public void run_CompiledProgram_ExecutesSuccessfully() {
		program.addObserver(observer);

		program.compile();

		program.run();

		verify(observer, never()).onError(any(Throwable.class));
	}

	@Test
	public void addObserver_MockAddedTwice_RunInvokesObserverOnce() {
		program.addObserver(observer);
		program.addObserver(observer);

		program.compile();

		program.run();

		verify(observer).afterCycleEnd();
	}

	@Test
	public void removeObserver_Mock_RunDoesNotInvokeObserver() {
		program.addObserver(observer);
		program.removeObserver(observer);

		program.compile();

		program.run();

		verify(observer, never()).afterCycleEnd();
	}
}
