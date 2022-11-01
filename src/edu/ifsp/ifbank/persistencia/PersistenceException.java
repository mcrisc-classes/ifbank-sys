package edu.ifsp.ifbank.persistencia;

public class PersistenceException extends Exception {
	private static final long serialVersionUID = 1L;

	public PersistenceException(Throwable cause) {
		super(cause);
	}
	
	public PersistenceException(String message) {
		super(message);
	}
}
