package de.peteral.softplc.view;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.peteral.softplc.file.FileManager;
import javafx.stage.Stage;

@SuppressWarnings("javadoc")
public class ApplicationControllerTest {

	@Rule
	public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
	private ApplicationController controller;
	@Mock
	private FileManager fileManager;
	@Mock
	private Stage stage;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		controller = new ApplicationController();
	}

	@Test
	public void handleNew_MockedFileManager_InvokesCorrectMethod() {
		controller.setFileManager(fileManager);

		controller.handleNew();

		verify(fileManager).newPlc();
	}

	@Test
	public void handleAlwaysOnTop_OptionActive_InvokesCorrectStageMethod() {
		// TODO Java FX uses tons of final methods making mocking with mockito
		// impossible
		// need to use power-mock if I want to test this, same problem with Java
		// NIO API
		// fuk dis sheet

		// controller.setStage(stage);
		// controller.alwaysOnTopMenuItem = mock(CheckMenuItem.class);
		// when(controller.alwaysOnTopMenuItem.isSelected()).thenReturn(true);
		//
		// controller.handleAlwaysOnTop();
		//
		// verify(stage).setAlwaysOnTop(true);
	}
}
