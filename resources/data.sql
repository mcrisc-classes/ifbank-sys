USE `financas`;

/* Cadastro de clientes */
INSERT INTO cliente (id, nome, telefone) VALUES (1, 'Alberto Alvarenga', '(16)99123-4567');
INSERT INTO cliente (id, nome, telefone) VALUES (2, 'Bento Botelho', '(16)99123-4568');
INSERT INTO cliente (id, nome, telefone) VALUES (3, 'Célia Cintra', '(16)99123-4569');
INSERT INTO cliente (id, nome, telefone) VALUES (4, 'Décio Dantas', '(16)99123-4560');


/* Cadastro de endereço para o cliente 1 */
START TRANSACTION;
INSERT INTO endereco (cep, logradouro, numero, cidade, uf) VALUES ('17201-970', 'Rua Tenente Lopes', '465', 'Jaú', 'SP');
INSERT INTO cliente_endereco (cliente, endereco, ativo, principal) VALUES (1, LAST_INSERT_ID(), 1, 1);
COMMIT;


/* Cadastro (abertura) de contas */
INSERT INTO conta (numero, saldo, titular) VALUES (1, 0, 1);
INSERT INTO conta (numero, saldo, titular) VALUES (2, 0, 2);
INSERT INTO conta (numero, saldo, titular) VALUES (3, 0, 3);
INSERT INTO conta (numero, saldo, titular) VALUES (4, 0, 4);


/* 
 * Operações entre contas
 */

/* Crédito (C) de 5000,00 na conta 1 */
START TRANSACTION;
UPDATE conta SET saldo = 5000 WHERE numero = 1;
INSERT INTO movimentacao (origem, valor, tipo) VALUES (1, 5000, 'C');
COMMIT;

/* Transferência (T) de 50,00 da conta 1 para a conta 4 */
START TRANSACTION;
UPDATE conta SET saldo = saldo - 50 WHERE numero = 1;
UPDATE conta SET saldo = saldo + 50 WHERE numero = 4;
INSERT INTO movimentacao (origem, destino, valor, tipo) VALUES (1, 4, 50, 'T');
COMMIT;

/* Débito (D) de 300,00 na conta 1  */
START TRANSACTION;
UPDATE conta SET saldo = saldo - 300 WHERE numero = 1;
INSERT INTO movimentacao (origem, valor, tipo) VALUES (1, 300, 'D');
COMMIT;


/*
 * Operações com empréstimos
 */

/* Cadastro de empréstimo */
INSERT INTO emprestimo
        (id, cliente, valor_total, saldo_devedor, taxa_juros, qtd_parcelas, dt_contrato)
VALUES
        (1, 4, 2000, 2000, 8.5, 12, '2022-10-20');


/* Pagamento da parcela número 1, com débito em conta */
/* Tabela SAC (Sistema de Amortização Constante) */
START TRANSACTION;

SET @id_emprestimo = 1;
SET @num_parcela = 1;

SELECT valor_total, saldo_devedor, taxa_juros, qtd_parcelas
INTO @valor_total, @saldo_devedor, @juros_aa, @qtd_parcelas
FROM emprestimo WHERE id = @id_emprestimo;

SET @juros_am = @juros_aa / 12;
SET @juros = @saldo_devedor * @juros_am / 100;
SET @amortizacao = @valor_total / @qtd_parcelas;
SET @valor_parcela = @amortizacao + @juros;

/* Pagamento da parcela */
INSERT INTO parcela (emprestimo, numero, valor_parcela, amortizacao, juros)
VALUES (@id_emprestimo, @num_parcela, @valor_parcela, @amortizacao, @juros);

UPDATE emprestimo
SET 
        parcelas_pagas = parcelas_pagas + 1,
        saldo_devedor = saldo_devedor - @amortizacao
WHERE id = 1;

/* Débito na conta do cliente */
SET @numero_conta = 4; /* conta 4 pertence ao cliente 4, que é tomou este empréstimo */
UPDATE conta SET saldo = saldo - @valor_parcela WHERE numero = @numero_conta;
INSERT INTO movimentacao (origem, valor, tipo) VALUES (@numero_conta, @valor_parcela, 'D');

COMMIT;


