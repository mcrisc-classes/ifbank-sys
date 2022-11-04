package edu.ifsp.ifbank.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.ifsp.ifbank.modelo.Conta;

public class ContaDAO {
	public Conta save(Conta conta) throws PersistenceException {
		try (Connection conn = DataSource.getConnection()) {
			String query = "INSERT INTO conta (numero, saldo, titular) VALUES (?, 0, ?);";
			
			try (PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setInt(1, conta.getNumero());
				ps.setInt(2, conta.getTitular().getId());
				
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}

		return conta;
	}
}
