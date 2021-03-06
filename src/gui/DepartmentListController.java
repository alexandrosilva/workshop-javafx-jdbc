package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// incluindo os dados da lista do departmentservice na tela
	private DepartmentService service;

	// tratando tableView da tela DepartmentList
	@FXML
	private TableView<Department> tableViewDepartments;

	// tratando coluna id da tela DepartmentList
	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	// tratando coluna Name da tela DepartmentList
	@FXML
	private TableColumn<Department, String> tableColumName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	// (aula 281) para realizar delete de linhas departamento
	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	// tratando Botao da tela DepartmentList
	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {

		Stage parentStage = Utils.currentStage(event);

		// para iniciar vazio
		Department obj = new Department();

		// chamada da fun��o createDialogForm para o bot�o New
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);

		System.out.println("onBtNewAction");
	}

	// Inje��o de dependencia do controller da classe departmentService
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// para fazer a tabela com as colunas name e Id funcionarem
		initializeNodes();

	}

	// apos inserir initializenodes no metodo initialize
	private void initializeNodes() {

		// iniciar o comportamento das tabelas
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// para a tableView ficar bem posicionada ate o fim da janela principal
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartments.prefHeightProperty().bind(stage.heightProperty());
	}

	// Metodo responsavel para acessar o servi�o carregar os departamentos e jogar
	// os dados no obsList
	public void updadteTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		// para pegar a lista la da classe departmentservice
		List<Department> list = service.findAll();
		// transfere os dados do list para o obsList
		obsList = FXCollections.observableArrayList(list);
		// para carregar na tela do departmentlist.fxml
		tableViewDepartments.setItems(obsList);

		// para acrescentar um novo botao em cada linha da lista de departamento
		initEditButtons();
		
		// para acrescentar um novo botao em cada linha da lista de departamento
		initRemoveButtons();
	}

	// Para instaciar a janela de dialogo
	// a fun��o createDialogForm ser� chamada no botao NEW (onBtNewAction)
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// para carregar a view
			Pane pane = loader.load();

			// aula 276 ()
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// quando carrega uma janela modal na frente da janela principal necess�rio
			// estanciar um novo stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");
			// criar uma nova cena
			dialogStage.setScene(new Scene(pane));
			// item abaixo diz se a tela pode ou nao ser redimensionada (no caso n�o pode
			// ser redimensionada)
			dialogStage.setResizable(false);
			// quem o pai da janela
			dialogStage.initOwner(parentStage);
			// vai dizer se a janela ser� modal ou ter� um outro comportamento (NO caso ser�
			// modal, ou seja, ela ficar� travada. Enquanto n�o fechar a janela n�o pode
			// acessar a janela anterior)
			dialogStage.initModality(Modality.WINDOW_MODAL);

			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error Loading View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {

		updadteTableView();

	}

	// metodo para adicionar um botao de edit em cada linha da tabela de
	// departamento
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// metodo para adicionar um botao de delete em cada linha da tabela departamento
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updadteTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
	}

}
