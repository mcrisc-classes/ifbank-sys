package edu.ifsp.ifbank.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.ifsp.ifbank.modelo.Cliente;

public class ClienteDAO {

	
	public Cliente findById(int id) throws PersistenceException {
		Cliente cliente = null;

		try (Connection conn = DataSource.getConnection()) {
			final String query = "SELECT id, nome, telefone FROM cliente WHERE id = ?;";
			
			try (PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				
				if (rs.next()) {
					cliente = new Cliente();
					cliente.setId(rs.getInt("id"));
					cliente.setNome(rs.getString("nome"));
					cliente.setTelefone(rs.getString("telefone"));
				}
			}
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
		
		return cliente;
	}
	
	public List<Cliente> listAll() throws PersistenceException {
		List<Cliente> clientes = new ArrayList<>();
		
		try (Connection conn = DataSource.getConnection()) {
			final String query = "SELECT id, nome, telefone FROM cliente ORDER BY nome;";
			
			try (Statement ps = conn.createStatement()) {
				ResultSet rs = ps.executeQuery(query);
				
				while (rs.next()) {
					Cliente cliente = new Cliente();
					cliente.setId(rs.getInt("id"));
					cliente.setNome(rs.getString("nome"));
					cliente.setTelefone(rs.getString("telefone"));
					
					clientes.add(cliente);
				}
			}
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
		
		return clientes;
	}
}
