package edu.ifsp.ifbank.gui;

public enum UseCase {
	WELCOME(new WelcomeController(), " "),
	CADASTRO_CLIENTE(new CadastroClienteController(), "Cadastro de clientes"), 
	CADASTRO_CONTA(new CadastroContaController(), "Abertura de conta"), 
	NOVO_EMPRESTIMO(new EmprestimoController(), "Novo empréstimo"), 
	PAGAR_PARCELA(new PagarParcelaController(), "Pagar parcela"), 
	TRANSFERENCIA(new TransferenciaController(), "Transferência"), 
	DEPOSITO(new DepositoController(), "Depósito");
	
	
	private UseCaseController controller;
	private String title;
	
	private UseCase(UseCaseController controller, String title) {
		this.controller = controller;
		this.title = title;
	}
	
	public UseCaseController getController() {
		return controller;
	}
	
	public String getTitle() {
		return title;
	}
}