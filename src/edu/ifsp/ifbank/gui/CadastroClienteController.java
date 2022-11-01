package edu.ifsp.ifbank.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.MaskFormatter;

import edu.ifsp.ifbank.modelo.Cliente;
import edu.ifsp.ifbank.persistencia.ClienteDAO;
import edu.ifsp.ifbank.persistencia.PersistenceException;

public class CadastroClienteController extends UseCaseController {
	private JPanel panel = new JPanel();
	private JFormattedTextField textCodigo;
	private JTextField textNome;
	private JTextField textTelefone;
	private ClienteDAO dao = new ClienteDAO();
	private Cliente cliente;
	private JButton botaoBuscar;
	private JButton botaoSalvar;
	private JButton botaoCancelar;
	private JButton botaoExcluir;

	@Override
	public JPanel buildUI() {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		
		gbc = createPrototype(0, 0, 2);
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Código:"), gbc);
		
		gbc = createPrototype(2, 0, 2);
		gbc.anchor = GridBagConstraints.WEST;
		textCodigo = new JFormattedTextField(NumberFormat.getIntegerInstance());
		textCodigo.addPropertyChangeListener("value", new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				botaoBuscar.setEnabled(textCodigo.getValue() != null);
			}
		});
		textCodigo.setColumns(4);
		panel.add(textCodigo);
		
		gbc = createPrototype(4, 0, 1);
		gbc.insets.bottom = 5;
		gbc.insets.left = 10;
		gbc.anchor = GridBagConstraints.WEST;
		botaoBuscar = new JButton("Buscar");
		botaoBuscar.addActionListener((e) -> buscar());;
		panel.add(botaoBuscar, gbc);
		
		gbc = createPrototype(0, 1, 2);
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel("Nome:"), gbc);
		
		gbc = createPrototype(2, 1, 3);
		gbc.anchor = GridBagConstraints.WEST;
		textNome = new JTextField(30);
		panel.add(textNome, gbc);
		
		gbc = createPrototype(0,  2, 2);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.top = 5;
		panel.add(new JLabel("Telefone:"), gbc);

		gbc = createPrototype(2, 2, 3);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets.top = 5;
		try {
			textTelefone = new JFormattedTextField(new MaskFormatter("(##)#####-####"));
		} catch (ParseException pe) {
			textTelefone = new JFormattedTextField();
			pe.printStackTrace();
		}
		textTelefone.setColumns(14);
		panel.add(textTelefone, gbc);

		gbc = createPrototype(2, 3, 2);
		gbc.insets.top = 10;
		gbc.anchor = GridBagConstraints.WEST;
		botaoSalvar = new JButton("Salvar");
		botaoSalvar.addActionListener((e) -> this.salvar());
		panel.add(botaoSalvar, gbc);

		gbc = createPrototype(4, 3, 2);
		gbc.insets.top = 10;
		gbc.anchor = GridBagConstraints.WEST;
		botaoCancelar = new JButton("Cancelar");
		botaoCancelar.addActionListener((e) -> getMainController().finishUseCase());
		panel.add(botaoCancelar, gbc);

		gbc = createPrototype(6, 3, 2);
		gbc.insets.top = 10;
		gbc.insets.left = 10;
		gbc.anchor = GridBagConstraints.WEST;
		botaoExcluir = new JButton("Excluir");
		botaoExcluir.addActionListener((e) -> this.excluir());
		panel.add(botaoExcluir, gbc);

		
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
	
	@Override
	public void init() {
		cliente = null;
		resetForm();		
		getMainController().setStatus("Cadastro iniciado");
	}

	private void salvar() {
		if (cliente == null) {
			cliente = new Cliente();
		}
		cliente.setNome(textNome.getText());
		cliente.setTelefone(textTelefone.getText());

		SwingWorker<Void, Void> worker = new SwingWorker<>() {
			@Override
			protected Void doInBackground() throws PersistenceException {
				dao.save(cliente);
				return null;
			}
			
			@Override
			protected void done() {
				try {
					get();
					fillForm();
					getMainController().setStatus("Cliente salvo com sucesso!");
					botaoSalvar.setEnabled(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		};

		botaoSalvar.setEnabled(false);
		worker.execute();
	}
	
	private void buscar() {
		int id = ((Number)textCodigo.getValue()).intValue();
		
		SwingWorker<Cliente, Void> worker = new SwingWorker<>() {
			@Override
			protected Cliente doInBackground() throws Exception {
				Cliente cliente = dao.findById(id);	
				return cliente;
			}
			
			@Override
			protected void done() {
				try {
					cliente = get();
					if (cliente != null) {
						fillForm();
					} else {
						resetForm();
						JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
						textCodigo.requestFocus();
					}
					
					botaoBuscar.setEnabled(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		};
		botaoBuscar.setEnabled(false);
		getMainController().setStatus("Buscando cliente...");
		worker.execute();
	}
	
	private void excluir() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				dao.delete(cliente);
				return null;
			}
			
			@Override
			protected void done() {
				try {
					get();
					resetForm();
					getMainController().setStatus("Cliente excluído!");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		};
		botaoExcluir.setEnabled(false);
		worker.execute();
	}
	
	private void resetForm() {
		textCodigo.setValue(null);
		textNome.setText("");
		textTelefone.setText("");
		botaoExcluir.setEnabled(false);
		botaoBuscar.setEnabled(false);
		textNome.requestFocus();
	}
	
	private void fillForm() {
		textCodigo.setValue(cliente.getId());
		textNome.setText(cliente.getNome());
		textTelefone.setText(cliente.getTelefone());
		botaoExcluir.setEnabled(true);
	}
}
