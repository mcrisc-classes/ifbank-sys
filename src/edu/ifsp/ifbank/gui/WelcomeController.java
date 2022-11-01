package edu.ifsp.ifbank.gui;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomeController extends UseCaseController {
	@Override
	public JPanel buildUI() {
		URL url = getClass().getResource("/images/background.png");
		JLabel labelImagem = new JLabel(new ImageIcon(url));
		labelImagem.setOpaque(true);
		labelImagem.setBackground(Theme.MAIN_BACKGROUND);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(labelImagem, BorderLayout.CENTER);
		return panel;
	}

}
