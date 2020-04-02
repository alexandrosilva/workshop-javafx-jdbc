package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

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
	
	// dependencia para o departamentoService
	private DepartmentService service;
	
	// criado a lista para atualizar a lista apos apertar o botao save
	private List<DataChangeListener> dataChangeListeners = new ArrayList<DataChangeListener>();
	
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
	private void onBtSaveAction(ActionEvent event) {
		
		// Adicionado os dois if´s para indicar para o programador que nao foi injetado ou entity ou service. Isso precisa porque a injeção de dependencia está sendo feito de forma manual
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			// getFormData - responsavel por pegar os dados na caixa de text name e instanciar um departamento
			entity = getFormData();
			
			service.saveOrUpdate(entity);
			
			// para atualizar a tela apos o botao save
			notifyDataChangeListeners();
			
			// para fechar a janela apos apertar o botao save
			Utils.currentStage(event).close();
			
			System.out.println("onBtSaveAction");	
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Department getFormData() {
		
		Department obj = new Department();
		
		// a função utils.tryParseToInt vai converter o texto que esta na caixa ID para inteiro
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		obj.setName(txtName.getText());
		
		return obj;
	}

	@FXML
	private void onBtCancelAction(ActionEvent event) {	
		// para fechar a janela apos apertar o botao save
		Utils.currentStage(event).close();
		System.out.println("onBtCancelAction");
	}	
	
	public void setDepartment(Department entity) {
		
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
