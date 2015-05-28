package de.peteral.softplc.view;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;

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
	private TableColumn<Cpu, String> cpuNameColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuSlotColumn;
	@FXML
	private TableColumn<Cpu, String> cpuModeColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuCycleActColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuCycleTarColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuConnectionsColumn;
	@FXML
	private AnchorPane cpuDetailPane;
	private CpuViewController cpuDetailController;

	private Plc plc;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		cpuSlotColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSlot());
		cpuModeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getStatusProperty());
		cpuCycleTarColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getProgram().getTargetCycleTime());
		cpuCycleActColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getProgram().getCurrentCycleTime());
		cpuNameColumn.setCellValueFactory(data -> data.getValue().getName());
		cpuNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		initCpuDetailScene();

		cpuDetailController.update(null);

		cpuTable.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> cpuDetailController
								.update(newValue));

	}

	private void initCpuDetailScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ActualViewController.class
					.getResource("CpuView.fxml"));
			AnchorPane layout = loader.load();

			cpuDetailPane.getChildren().add(layout);

			cpuDetailController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the PLC to be observed by the ui.
	 *
	 * @param plc
	 */
	public void setPlc(Plc plc) {
		this.plc = plc;

		cpuTable.setItems(plc.getCpus());
	}

	private ObservableList<Cpu> getSelectedCpus() {
		return cpuTable.getSelectionModel().getSelectedItems();
	}

	@FXML
	private void handleStart() {
		getSelectedCpus().forEach(cpu -> cpu.start());
	}

	@FXML
	private void handleStop() {
		getSelectedCpus().forEach(cpu -> cpu.stop());
	}

	@FXML
	private void handleStartAll() {
		plc.getCpus().forEach(cpu -> cpu.start());
	}

	@FXML
	private void handleStopAll() {
		plc.getCpus().forEach(cpu -> cpu.stop());
	}
}
