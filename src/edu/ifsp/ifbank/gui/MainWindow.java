package edu.ifsp.ifbank.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainWindow {
	private JFrame frame = new JFrame("IFBank-Sys");

	public MainWindow() {
		buildMenu();		
	}
	
	private void buildMenu() {
		
		/* menu: Cadastro */
		JMenu menuCadastro = new JMenu("Cadastro");
		menuCadastro.setMnemonic('C');
		JMenuItem clienteNovo = new JMenuItem("Cliente...");
		clienteNovo.setMnemonic('C');
		menuCadastro.add(clienteNovo);
		JMenuItem contaNova = new JMenuItem("Conta...");
		contaNova.setMnemonic('t');
		menuCadastro.add(contaNova);
		menuCadastro.addSeparator();
		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.setMnemonic('r');
		menuCadastro.add(menuSair);

		/* menu: Empréstimo */
		JMenu menuEmprestimo = new JMenu("Empréstimo");
		menuEmprestimo.setMnemonic('E');
		JMenuItem emprestimoNovo = new JMenuItem("Novo...");
		emprestimoNovo.setMnemonic('N');
		menuEmprestimo.add(emprestimoNovo);
		JMenuItem emprestimoPagarParcela = new JMenuItem("Pagar parcela...");
		emprestimoPagarParcela.setMnemonic('P');
		menuEmprestimo.add(emprestimoPagarParcela);

		/* menu: Transações */
		JMenu menuTransacoes = new JMenu("Transações");
		menuTransacoes.setMnemonic('T');
		JMenuItem transferenciaContas = new JMenuItem("Transferência entre contas...");
		transferenciaContas.setMnemonic('T');
		menuTransacoes.add(transferenciaContas);
		JMenuItem deposito = new JMenuItem("Depósito...");
		deposito.setMnemonic('D');
		menuTransacoes.add(deposito);
		
		/* Barra de menus */
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuCadastro);
		menuBar.add(menuEmprestimo);
		menuBar.add(menuTransacoes);
		frame.setJMenuBar(menuBar);
	}
	
	public void exibir() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
