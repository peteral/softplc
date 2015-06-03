package de.peteral.softplc.view;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import de.peteral.softplc.model.Cpu;
import de.peteral.softplc.model.Plc;
import de.peteral.softplc.plc.PlcFactory;

/**
 * Actual PLC view controller.
 *
 * @author peteral
 *
 */
public class CpuTableViewController {

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
	private TableColumn<Cpu, Number> cpuActualConnectionsColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuMaxConnectionsColumn;
	@FXML
	private TableColumn<Cpu, Number> cpuMaxBlockSizeColumn;
	@FXML
	private AnchorPane cpuDetailPane;
	private CpuDetailViewController cpuDetailController;

	private Plc plc;
	private Stage primaryStage;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		cpuSlotColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getSlot());
		cpuSlotColumn.setCellFactory(TextFieldTableCell
				.forTableColumn(new NumberStringConverter()));

		cpuModeColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getStatusProperty());

		cpuCycleTarColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getProgram().getTargetCycleTime());
		cpuCycleTarColumn.setCellFactory(TextFieldTableCell
				.forTableColumn(new NumberStringConverter()));

		cpuCycleActColumn.setCellValueFactory(cellData -> cellData.getValue()
				.getProgram().getCurrentCycleTime());

		cpuActualConnectionsColumn.setCellValueFactory(cellData -> cellData
				.getValue().getCurrentConnections());

		cpuMaxConnectionsColumn.setCellValueFactory(cellData -> cellData
				.getValue().getMaxConnections());
		cpuMaxConnectionsColumn.setCellFactory(TextFieldTableCell
				.forTableColumn(new NumberStringConverter()));

		cpuMaxBlockSizeColumn.setCellValueFactory(cellData -> cellData
				.getValue().getMaxDataSize());
		cpuMaxBlockSizeColumn.setCellFactory(TextFieldTableCell
				.forTableColumn(new NumberStringConverter()));

		cpuNameColumn.setCellValueFactory(data -> data.getValue().getName());
		cpuNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		initCpuDetailScene();

		cpuDetailController.update(null);

		cpuTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		cpuTable.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> cpuDetailController
								.update(newValue));

	}

	private void initCpuDetailScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(CpuTableViewController.class
					.getResource("CpuDetailView.fxml"));
			AnchorPane layout = loader.load();

			cpuDetailPane.getChildren().add(layout);

			cpuDetailController = loader.getController();
			cpuDetailController.setPrimaryStage(primaryStage);
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

	@FXML
	private void handleAdd() {
		plc.getCpus().add(new PlcFactory().createCpu(plc));
	}

	@FXML
	private void handleDelete() {
		handleStop();
		plc.getCpus().removeAll(getSelectedCpus());
	}

	/**
	 * Assigns current stage
	 *
	 * @param primaryStage
	 */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
