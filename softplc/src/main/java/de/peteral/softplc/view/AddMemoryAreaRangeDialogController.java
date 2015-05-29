package de.peteral.softplc.view;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import de.peteral.softplc.memory.MemoryAreaImpl;
import de.peteral.softplc.model.MemoryArea;

/**
 * Controller class for the add memory range area dialog.
 *
 * @author peteral
 *
 */
public class AddMemoryAreaRangeDialogController {
	@FXML
	private TextField prefixField;
	@FXML
	private TextField rangeFromField;
	@FXML
	private TextField rangeToField;
	@FXML
	private TextField sizeField;
	private Stage dialogStage;
	private boolean okClicked;

	@FXML
	private void initialize() {

	}

	@FXML
	private void handleOk() {
		// TODO add input validation
		okClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * assigns current stage
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 *
	 * @return true - user confirmed dialog input
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Creates memory areas based on user input
	 *
	 * @return list of created memory areas
	 */
	public List<MemoryArea> createMemoryAreas() {
		List<MemoryArea> result = new ArrayList<>();

		String prefix = prefixField.getText();
		int from = Integer.parseInt(rangeFromField.getText());
		int to = Integer.parseInt(rangeToField.getText());
		int size = Integer.parseInt(sizeField.getText());

		for (int i = from; i <= to; i++) {
			result.add(new MemoryAreaImpl(prefix + i, size, false));
		}

		return result;
	}
}
