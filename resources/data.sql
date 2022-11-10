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
UPDATE conta SET saldo = saldo + 5000 WHERE numero = 1;
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

/* Abertura de empréstimo */
START TRANSACTION;
SET @id_emprestimo = 1;
SET @juros_aa = 8.5;
SET @valor_total = 2000;
SET @qtd_parcelas = 5;
SET @juros_am = @juros_aa / 12;
SET @saldo_devedor = @valor_total;

INSERT INTO emprestimo
        (id, cliente, valor_total, saldo_devedor, taxa_juros, qtd_parcelas, dt_contrato)
VALUES
        (1, 4, @valor_total, @saldo_devedor, @juros_aa, @qtd_parcelas, CURRENT_DATE());

/* Abertura das parcelas, segundo a Tabela SAC (Sistema de Amortização Constante) */
/* Parcela 1 */
SET @numero_parcela = 1;
SET @juros = @saldo_devedor * @juros_am / 100;
SET @amortizacao = @valor_total / @qtd_parcelas;
SET @valor_parcela = @amortizacao + @juros;
SET @dt_vencto = DATE_ADD(CURRENT_DATE(), INTERVAL (30 * @numero_parcela) DAY);
INSERT INTO parcela (emprestimo, numero, valor_parcela, amortizacao, juros, dt_vencto)
VALUES (@id_emprestimo, @numero_parcela, @valor_parcela, @amortizacao, @juros, @dt_vencto);
SET @saldo_devedor = @saldo_devedor - @valor_parcela;

/* Parcela 2 */
SET @numero_parcela = 2;
SET @juros = @saldo_devedor * @juros_am / 100;
SET @amortizacao = @valor_total / @qtd_parcelas;
SET @valor_parcela = @amortizacao + @juros;
SET @dt_vencto = DATE_ADD(CURRENT_DATE(), INTERVAL (30 * @numero_parcela) DAY);
INSERT INTO parcela (emprestimo, numero, valor_parcela, amortizacao, juros, dt_vencto)
VALUES (@id_emprestimo, @numero_parcela, @valor_parcela, @amortizacao, @juros, @dt_vencto);
SET @saldo_devedor = @saldo_devedor - @valor_parcela;

/* Parcela 3 */
SET @numero_parcela = 3;
SET @juros = @saldo_devedor * @juros_am / 100;
SET @amortizacao = @valor_total / @qtd_parcelas;
SET @valor_parcela = @amortizacao + @juros;
SET @dt_vencto = DATE_ADD(CURRENT_DATE(), INTERVAL (30 * @numero_parcela) DAY);
INSERT INTO parcela (emprestimo, numero, valor_parcela, amortizacao, juros, dt_vencto)
VALUES (@id_emprestimo, @numero_parcela, @valor_parcela, @amortizacao, @juros, @dt_vencto);
SET @saldo_devedor = @saldo_devedor - @valor_parcela;

/* Parcela 4 */
SET @numero_parcela = 4;
SET @juros = @saldo_devedor * @juros_am / 100;
SET @amortizacao = @valor_total / @qtd_parcelas;
SET @valor_parcela = @amortizacao + @juros;
SET @dt_vencto = DATE_ADD(CURRENT_DATE(), INTERVAL (30 * @numero_parcela) DAY);
INSERT INTO parcela (emprestimo, numero, valor_parcela, amortizacao, juros, dt_vencto)
VALUES (@id_emprestimo, @numero_parcela, @valor_parcela, @amortizacao, @juros, @dt_vencto);
SET @saldo_devedor = @saldo_devedor - @valor_parcela;

/* Parcela 5 */
SET @numero_parcela = 5;
SET @juros = @saldo_devedor * @juros_am / 100;
SET @amortizacao = @valor_total / @qtd_parcelas;
SET @valor_parcela = @amortizacao + @juros;
SET @dt_vencto = DATE_ADD(CURRENT_DATE(), INTERVAL (30 * @numero_parcela) DAY);
INSERT INTO parcela (emprestimo, numero, valor_parcela, amortizacao, juros, dt_vencto)
VALUES (@id_emprestimo, @numero_parcela, @valor_parcela, @amortizacao, @juros, @dt_vencto);
SET @saldo_devedor = @saldo_devedor - @valor_parcela;

COMMIT;


/* Quitação da parcela número 1, com débito em conta */
START TRANSACTION;

SET @id_emprestimo = 1;
SET @num_parcela = 1;

SELECT id, amortizacao INTO @id_parcela, @amortizacao 
FROM parcela WHERE emprestimo = @id_emprestimo AND numero = @num_parcela;

/* Pagamento da parcela */
UPDATE parcela SET dt_pgto = CURRENT_DATE() WHERE id = @id_parcela;

UPDATE emprestimo
SET 
        parcelas_pagas = parcelas_pagas + 1,
        saldo_devedor = saldo_devedor - @amortizacao
WHERE id = @id_emprestimo;

/* Débito na conta do cliente */
SET @numero_conta = 4; /* conta 4 pertence ao cliente 4, que é tomou este empréstimo */
UPDATE conta SET saldo = saldo - @valor_parcela WHERE numero = @numero_conta;
INSERT INTO movimentacao (origem, valor, tipo) VALUES (@numero_conta, @valor_parcela, 'D');

COMMIT;


