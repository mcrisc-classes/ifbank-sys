package edu.ifsp.ifbank;

import java.util.List;

import edu.ifsp.ifbank.modelo.Cliente;
import edu.ifsp.ifbank.persistencia.ClienteDAO;
import edu.ifsp.ifbank.persistencia.PersistenceException;

public class Main {
	
	public static void main(String[] args) {
		ClienteDAO dao = new ClienteDAO();
		
		try {
			List<Cliente> clientes = dao.listAll();
			for (Cliente c : clientes) {
				System.out.printf("%2d\t%s\n", c.getId(), c.getNome());
			}
			
			Cliente cliente = dao.findById(4);
			System.out.println(cliente.getTelefone());
			
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
