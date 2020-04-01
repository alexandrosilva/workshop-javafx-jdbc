package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
	
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
	
	// tratando Botao da tela DepartmentList
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	//Injeção de dependencia do controller da classe departmentService
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
	
	// Metodo responsavel para acessar o serviço carregar os departamentos e jogar os dados no obsList
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
	}
}
