package edu.ifsp.ifbank.gui;

import javax.swing.JPanel;

public abstract class UseCaseController {
	private MainController mainController = null;
	private JPanel panel = null;
	
	public abstract JPanel buildUI();
	public void init() {};
	

	protected MainController getMainController() {
		return mainController;
	}

	public JPanel getPanel() {
		if (panel == null) {
			panel = buildUI();
		}
		return panel;
	}
	
	public void setMainController(MainController mainController) {
		if (mainController == null) {
			throw new IllegalArgumentException("mainController: null");
		}
		this.mainController = mainController;
	}

}
