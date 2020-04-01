package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable{

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
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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
}
