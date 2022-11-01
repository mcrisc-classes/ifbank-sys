# Melhorias

- Reduzir duplicação de código nos DAOs. Exemplos:
    - `try (Connection conn = ... ) catch (SQLException e)`
    - Criar um `mapRow()`  
- Reduzir duplicação de código nos `SwingWorker`s: ver mensagens de erro com `JOptionPane` no `CadastroClienteController`, por exemplo.
