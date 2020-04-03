package gui;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	// incluindo os dados da lista do departmentservice na tela
	private SellerService service;

	// tratando tableView da tela SellerList
	@FXML
	private TableView<Seller> tableViewSellers;

	// tratando coluna id da tela SellerList
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	// tratando coluna Name da tela SellerList
	@FXML
	private TableColumn<Seller, String> tableColumName;

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	// (aula 281) para realizar delete de linhas departamento
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	// tratando Botao da tela SellerList
	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {

		Stage parentStage = Utils.currentStage(event);

		// para iniciar vazio
		Seller obj = new Seller();

		// chamada da função createDialogForm para o botão New
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);

		System.out.println("onBtNewAction");
	}

	// Injeção de dependencia do controller da classe departmentService
	public void setSellerService(SellerService service) {
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
		tableViewSellers.prefHeightProperty().bind(stage.heightProperty());
	}

	// Metodo responsavel para acessar o serviço carregar os departamentos e jogar
	// os dados no obsList
	public void updadteTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		// para pegar a lista la da classe departmentservice
		List<Seller> list = service.findAll();
		// transfere os dados do list para o obsList
		obsList = FXCollections.observableArrayList(list);
		// para carregar na tela do departmentlist.fxml
		tableViewSellers.setItems(obsList);

		// para acrescentar um novo botao em cada linha da lista de departamento
		initEditButtons();
		
		// para acrescentar um novo botao em cada linha da lista de departamento
		initRemoveButtons();
	}

	
	// Para instaciar a janela de dialogo
	// a função createDialogForm será chamada no botao NEW (onBtNewAction)
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
	/*	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// para carregar a view
			Pane pane = loader.load();

			// aula 276 ()
			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			// quando carrega uma janela modal na frente da janela principal necessário
			// estanciar um novo stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			// criar uma nova cena
			dialogStage.setScene(new Scene(pane));
			// item abaixo diz se a tela pode ou nao ser redimensionada (no caso não pode
			// ser redimensionada)
			dialogStage.setResizable(false);
			// quem o pai da janela
			dialogStage.initOwner(parentStage);
			// vai dizer se a janela será modal ou terá um outro comportamento (NO caso será
			// modal, ou seja, ela ficará travada. Enquanto não fechar a janela não pode
			// acessar a janela anterior)
			dialogStage.initModality(Modality.WINDOW_MODAL);

			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error Loading View", e.getMessage(), AlertType.ERROR);
		}
		*/
	}


	@Override
	public void onDataChanged() {

		updadteTableView();

	}

	// metodo para adicionar um botao de edit em cada linha da tabela de
	// departamento
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// metodo para adicionar um botao de delete em cada linha da tabela departamento
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	private void removeEntity(Seller obj) {
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
