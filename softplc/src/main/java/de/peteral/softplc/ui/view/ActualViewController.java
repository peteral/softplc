package de.peteral.softplc.ui.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.ui.SoftplcApplication;

/**
 * Actual PLC view controller.
 *
 * @author peteral
 *
 */
public class ActualViewController {

	@FXML
	private TableView<Cpu> cpuTable;
	@FXML
	private TableColumn<Cpu, Number> cpuSlotColumn;

	private SoftplcApplication mainApp;
	private Plc plc;

	/**
	 * Initializes the controller with main application reference.
	 *
	 * @param mainApp
	 */
	public void setMainApp(SoftplcApplication mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the person table with the two columns.
		cpuSlotColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSlot());
	}

	public void setPlc(Plc plc) {
		this.plc = plc;

		cpuTable.setItems(plc.getCpus());
	}
}
