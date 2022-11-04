package edu.ifsp.ifbank.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import edu.ifsp.ifbank.modelo.Cliente;
import edu.ifsp.ifbank.modelo.Conta;
import edu.ifsp.ifbank.persistencia.ClienteDAO;

public class CadastroContaController extends UseCaseController {
	private ClienteDAO clienteDao = new ClienteDAO();
	private Conta conta = null;
	
	private JPanel panel = new JPanel();
	private JFormattedTextField textTitular;
	private JTextField textNomeTitular;
	private JFormattedTextField textNumero;
	private JButton botaoSalvar;

	@Override
	public void init() {
		conta = new Conta();
	}
	
	@Override
	public JPanel buildUI() {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		gbc = createPrototype(0, 0, 2);
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Titular:"), gbc);

		gbc = createPrototype(2, 0, 2);
		gbc.anchor = GridBagConstraints.WEST;
		textTitular = new JFormattedTextField(NumberFormat.getIntegerInstance());
		textTitular.addPropertyChangeListener("value", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				buscarTitular();
			}
		});
		textTitular.setColumns(4);
		panel.add(textTitular);
		
		gbc = createPrototype(4, 0, 3);
		textNomeTitular = new JTextField(20);
		textNomeTitular.setEditable(false);
		panel.add(textNomeTitular);
		

		gbc = createPrototype(0, 1, 2);
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Número:"), gbc);
		
		gbc = createPrototype(2, 1, 3);
		gbc.anchor = GridBagConstraints.WEST;
		textNumero = new JFormattedTextField(NumberFormat.getIntegerInstance());
		textNumero.setColumns(8);
		panel.add(textNumero, gbc);

		gbc = createPrototype(2, 2, 2);
		gbc.insets.top = 10;
		gbc.anchor = GridBagConstraints.WEST;
		botaoSalvar = new JButton("Salvar");
		botaoSalvar.addActionListener((e) -> salvar());
		panel.add(botaoSalvar, gbc);
		
		
		return panel;
	}
	
	private GridBagConstraints createPrototype(int x, int y, int width) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 5);
		
		return gbc;
	}
	
	private void buscarTitular() {
		if (textTitular.getValue() == null) {
			return;
		}
		
		int id = ((Number)textTitular.getValue()).intValue();

		SwingWorker<Cliente, Void> worker = new SwingWorker<>() {

			@Override
			protected Cliente doInBackground() throws Exception {
				Cliente cliente = clienteDao.findById(id);
				return cliente;
			}
			
			@Override
			protected void done() {
				try {
					Cliente cliente = get();
					if (cliente != null) {
						conta.setTitular(cliente);
						textNomeTitular.setText(cliente.getNome());
					} else {
						textNomeTitular.setText("Código inválido!");
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		};
		worker.execute();
	}
	
	private void salvar() {
		
	}
}
