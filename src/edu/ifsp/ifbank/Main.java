package edu.ifsp.ifbank;

import edu.ifsp.ifbank.gui.MainController;
import edu.ifsp.ifbank.gui.UseCase;

public class Main {
	public static void main(String[] args) {
		MainController mainController = new MainController();
		mainController.init();
		
		/* temporário (desenvolvimento) */
		// mainController.start(UseCase.CADASTRO_CLIENTE);
	}
}
