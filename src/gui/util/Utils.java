package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {
	
	// Funciolidade de pegar o stage quando aperta o botão
	public static Stage currentStage(ActionEvent event) {
		// tem dois down cast NODE e Stage (TUdo isso para pegar a janela da cena)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
		
	}

}
