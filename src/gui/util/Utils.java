package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	// Funciolidade de pegar o stage quando aperta o bot�o
	public static Stage currentStage(ActionEvent event) {
		// tem dois down cast NODE e Stage (TUdo isso para pegar a janela da cena)
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	// ajuda para tratar o numero de uma caixa de texto para converter para numero
	// inteiro
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
