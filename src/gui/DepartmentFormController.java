package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

/*
 * Classe para realizar o controle:
 * 	Caixa de texto ID;
 * 	Caixa de texto Name;
 * 	Botao Save;
 * 	Botão Cancelar;
 * 	Mensagem de erro;
 */

public class DepartmentFormController implements Initializable{

	// dependencia para o departamento (entidade relacionada a esse formulario)
	private Department entity;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	// para mensagem de erro caso ocorra erro no preenchimento do nome
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private void onBtSaveAction() {
		
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	private void onBtCancelAction() {
		System.out.println("onBtCancelAction");

	}	
	
	public void setDepartment(Department entity) {
		
		this.entity = entity;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	@FXML
	private void initializeNodes() {
		// limita para a caixa de texto textId somente numeros inteiros
		Constraints.setTextFieldInteger(txtId);
		// limita para a caixa de texto textName somente no máximo 30 caracteres
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	// Esse metodo fica responsavel para popular as caixas de texto ID e Name (Isso para update da informação)
	public void updateFormData() {
		
		// para checar se o entity está valendo nulo (programação defensiva - caso programador tenha esquecido de injetar info)
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		// para converter o id da entidade (que é inteiro) para string
		txtId.setText(String.valueOf(entity.getId()));
		
		txtName.setText(entity.getName());
	
	}
}
