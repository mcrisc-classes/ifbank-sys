CREATE DATABASE  IF NOT EXISTS `financas`;
USE financas;

DROP TABLE IF EXISTS parcela;
DROP TABLE IF EXISTS emprestimo;
DROP TABLE IF EXISTS movimentacao;
DROP TABLE IF EXISTS conta;
DROP TABLE IF EXISTS cliente_endereco;
DROP TABLE IF EXISTS endereco;
DROP TABLE IF EXISTS cliente;


/* Tabela: cliente */
CREATE TABLE cliente (
        id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
        nome VARCHAR(40) NOT NULL,
        telefone VARCHAR(14)
);

/* Tabela: conta */
CREATE TABLE conta (
        numero INT NOT NULL PRIMARY KEY,
        saldo DECIMAL(11,2) DEFAULT 0,
        titular INT NOT NULL
);

ALTER TABLE conta ADD CONSTRAINT
FOREIGN KEY titular_fk(titular) REFERENCES cliente(id);


/* Tabela: movimentacao */
CREATE TABLE movimentacao (
        id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
        origem INT NOT NULL,
        destino INT DEFAULT NULL,
        valor DECIMAL(11, 2) NOT NULL,
        tipo CHAR NOT NULL,
        instante DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE movimentacao ADD CONSTRAINT FOREIGN KEY origem_fk (origem) REFERENCES conta(numero);
ALTER TABLE movimentacao ADD CONSTRAINT FOREIGN KEY destino_fk(destino) REFERENCES conta(numero);
ALTER TABLE movimentacao ADD CONSTRAINT CHECK (tipo IN ('C', 'D', 'T')); /* Tipos: (C, D, T) = (Crédito, Débito, Transferência) */


/* Tabela: emprestimo */
CREATE TABLE emprestimo (
        id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
        cliente INT NOT NULL,
        valor_total DECIMAL(11, 2) NOT NULL,
        saldo_devedor DECIMAL(11, 2) NOT NULL,
        taxa_juros DECIMAL(5,2) NOT NULL,  /* juros ao ano (a.a.)*/
        qtd_parcelas INT NOT NULL,
        parcelas_pagas INT NOT NULL DEFAULT 0,
        dt_contrato DATE,
        dt_quitacao DATE DEFAULT NULL
);
ALTER TABLE emprestimo ADD CONSTRAINT
FOREIGN KEY cliente_emprestimo_fk(cliente) REFERENCES cliente(id);


/* Tabela: parcela */
CREATE TABLE parcela (
        id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
        emprestimo INT NOT NULL,
        numero INT NOT NULL,
        valor_parcela DECIMAL(11,2) DEFAULT 0,
        amortizacao DECIMAL(11,2) NOT NULL,
        juros DECIMAL(11,2) NOT NULL,
        dt_vencto DATE NOT NULL,
        dt_pgto DATETIME DEFAULT NULL        
);
ALTER TABLE parcela ADD CONSTRAINT
FOREIGN KEY parcela_fk(emprestimo) REFERENCES emprestimo(id);
CREATE INDEX idx_parcela ON parcela (emprestimo, numero);


/* Tabela: endereco */
CREATE TABLE endereco (
        id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
        cep VARCHAR(9),
        logradouro VARCHAR(40),
        numero VARCHAR(10),
        complemento VARCHAR(20),
        cidade VARCHAR(40),
        uf CHAR(2)
);

CREATE TABLE cliente_endereco (
	cliente INT NOT NULL,
    endereco INT NOT NULL,
	ativo TINYINT NOT NULL DEFAULT 1,
    principal TINYINT NOT NULL DEFAULT 0
);
ALTER TABLE cliente_endereco ADD CONSTRAINT PRIMARY KEY (cliente, endereco);
ALTER TABLE cliente_endereco ADD CONSTRAINT CHECK (ativo IN (0, 1));
ALTER TABLE cliente_endereco ADD CONSTRAINT CHECK (principal IN (0, 1));
ALTER TABLE cliente_endereco ADD CONSTRAINT FOREIGN KEY cliente_endereco_cliente_fk(cliente) REFERENCES cliente(id);
ALTER TABLE cliente_endereco ADD CONSTRAINT FOREIGN KEY cliente_endereco_endereco_fk(endereco) REFERENCES endereco(id);
