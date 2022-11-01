package edu.ifsp.ifbank.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

public class MainController {
	private static final int STATUS_DELAY = 3000;
	
	private JFrame frame = new JFrame("IFBank-Sys");
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel();
	private JLabel titleBar = new JLabel(" ");
	private JLabel statusBar = new JLabel(" ");
	private boolean operationInProgress = false;
	private Timer clearStatusTimer;
	private Set<UseCase> loaded = new HashSet<>();


	public MainController() {
		setupGUI();
		addWindowListener();
		buildMenu();		
		setupUseCases();
	}
	
	public void init() {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(650, 400);
		frame.setLocationRelativeTo(null);
		finishUseCase();
		frame.setVisible(true);
	}
	
	public void finishUseCase() {
		operationInProgress = false;
		start(UseCase.WELCOME);
	}

	public void setStatus(String message) {
		statusBar.setText(message);
		clearStatusTimer.start();
	}
	
	public void start(UseCase uc) {
		if (!loaded.contains(uc)) {
			return;
		}

		uc.getController().init();
		titleBar.setText(uc.getTitle());
		cardLayout.show(mainPanel, uc.toString());
	}
	
	public void setOperationInProgress(boolean inProgress) {
		this.operationInProgress = inProgress;
	}
	
	private void exit() {
		if (operationInProgress) {
			int res = JOptionPane.showConfirmDialog(frame, 
					"Há uma operação em andamento. Deseja realmente sair?", 
					"Sair", JOptionPane.YES_NO_OPTION);
			
			if (res == JOptionPane.YES_OPTION) {
				frame.dispose();
			}			
		} else {
			frame.dispose();
		}
	}
	
	private void setupUseCases() {
		for (UseCase uc : UseCase.values()) {
			UseCaseController controller = uc.getController();
			controller.setMainController(this);		
			
			JPanel panel = controller.getPanel();
			if (panel != null) {
				mainPanel.add(panel, uc.toString());
				loaded.add(uc);
			} else {
				System.out.println("Caso de uso não implementado: " + uc);
			}
		}
		
	}

	private void setupGUI() {
		mainPanel.setLayout(cardLayout);
		frame.setLayout(new BorderLayout());
		frame.add(mainPanel, BorderLayout.CENTER);
		
		titleBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 0));
		titleBar.setFont(titleBar.getFont().deriveFont(16f));
		titleBar.setForeground(Theme.TITLE);
		frame.add(titleBar, BorderLayout.NORTH);
		
		statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		frame.add(statusBar, BorderLayout.SOUTH);
		clearStatusTimer = new Timer(STATUS_DELAY, (e) -> statusBar.setText(" "));
		clearStatusTimer.setRepeats(false);		
	}

	private void buildMenu() {
		/* menu: Cadastro */
		JMenu menuCadastro = new JMenu("Cadastro");
		menuCadastro.setMnemonic('C');
		JMenuItem itemNovoCliente = new JMenuItem("Cliente...");
		itemNovoCliente.setMnemonic('C');
		itemNovoCliente.addActionListener((e) -> start(UseCase.CADASTRO_CLIENTE));		
		menuCadastro.add(itemNovoCliente);
		
		JMenuItem itemNovaConta = new JMenuItem("Conta...");
		itemNovaConta.setMnemonic('t');
		itemNovaConta.addActionListener((e) -> start(UseCase.CADASTRO_CONTA));
		menuCadastro.add(itemNovaConta);
		menuCadastro.addSeparator();
		
		JMenuItem itemSair = new JMenuItem("Sair");
		itemSair.setMnemonic('r');
		itemSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		itemSair.addActionListener((e) -> exit());
		menuCadastro.add(itemSair);
	
		/* menu: Empréstimo */
		JMenu menuEmprestimo = new JMenu("Empréstimo");
		menuEmprestimo.setMnemonic('E');
		JMenuItem itemNovoEmprestimo = new JMenuItem("Novo...");
		itemNovoEmprestimo.setMnemonic('N');
		itemNovoEmprestimo.addActionListener((e) -> start(UseCase.NOVO_EMPRESTIMO));
		menuEmprestimo.add(itemNovoEmprestimo);
		
		JMenuItem itemPagarParcela = new JMenuItem("Pagar parcela...");
		itemPagarParcela.setMnemonic('P');
		itemPagarParcela.addActionListener((e) -> start(UseCase.PAGAR_PARCELA));
		menuEmprestimo.add(itemPagarParcela);
	
		/* menu: Transações */
		JMenu menuTransacoes = new JMenu("Transações");
		menuTransacoes.setMnemonic('T');
		JMenuItem itemTransferenciaContas = new JMenuItem("Transferência entre contas...");
		itemTransferenciaContas.setMnemonic('T');
		itemTransferenciaContas.addActionListener((e) -> start(UseCase.TRANSFERENCIA));
		menuTransacoes.add(itemTransferenciaContas);
		
		JMenuItem itemDeposito = new JMenuItem("Depósito...");
		itemDeposito.setMnemonic('D');
		itemDeposito.addActionListener((e) -> start(UseCase.DEPOSITO));
		menuTransacoes.add(itemDeposito);
		
		/* Barra de menus */
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menuCadastro);
		menuBar.add(menuEmprestimo);
		menuBar.add(menuTransacoes);
		frame.setJMenuBar(menuBar);
	}

	private void addWindowListener() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();				
			}
		});
	}
}
