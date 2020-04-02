package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/*
 * Classe para realizar o controle:
 * 	Caixa de texto ID;
 * 	Caixa de texto Name;
 * 	Botao Save;
 * 	Botão Cancelar;
 * 	Mensagem de erro;
 */

public class DepartmentFormController implements Initializable{

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
}
