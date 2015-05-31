package de.peteral.softplc.program;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.script.ScriptEngineManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Memory;
import de.peteral.softplc.model.MemoryAccessViolationException;
import de.peteral.softplc.model.Program;
import de.peteral.softplc.model.ProgramCycleObserver;

@SuppressWarnings("javadoc")
public class ProgramImplTest {
	private static final int CYCLE_TIME = 50;
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
	public void setup() throws URISyntaxException, IOException {
		MockitoAnnotations.initMocks(this);

		when(cpu.getMemory()).thenReturn(memory);
		when(cpu.getLogger()).thenReturn(logger);

		ScriptFile scriptFile = new ScriptFile("test", new File(
				ProgramImplTest.class.getResource("/program/valid.js").toURI()));
		scriptFile.reload();
		program = new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), CYCLE_TIME, scriptFile);
	}

	@Test
	public void addObserver_Mock_RunInvokesObserver() {
		program.addObserver(observer);

		program.compile();

		program.run();

		verify(memory).write("M,W100", 10);
		verify(observer).afterCycleEnd();
	}

	@Test
	public void run_CompiledProgram_ExecutesSuccessfully() {
		program.addObserver(observer);

		program.compile();

		program.run();

		verify(observer, never()).onError(anyString(), any(Throwable.class));
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

	@Test
	public void compile_InvalidScript_RegisteredObserverNotified()
			throws URISyntaxException, IOException {
		ScriptFile scriptFile = new ScriptFile("test", new File(
				ProgramImplTest.class.getResource("/program/invalid.js")
						.toURI()));
		scriptFile.reload();
		program = new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), CYCLE_TIME, scriptFile);

		program.addObserver(observer);

		program.compile();

		verify(observer).onError(anyString(), any(Throwable.class));
	}

	@Test
	public void run_ProgramThrowsException_RegisteredObserverNotified()
			throws URISyntaxException, IOException {
		doThrow(new MemoryAccessViolationException("")).when(memory).write(
				anyString(), anyObject());
		ScriptFile scriptFile = new ScriptFile("test", new File(
				ProgramImplTest.class.getResource("/program/valid.js").toURI()));
		scriptFile.reload();
		program = new ProgramImpl(cpu, new ScriptEngineManager(),
				new Precompiler(), CYCLE_TIME, scriptFile);

		program.addObserver(observer);

		program.compile();

		program.run();

		verify(observer).onError(anyString(), any(Throwable.class));
	}
}
