-- Tabela de usuarios do sistema
-- Compatível com MySQL 8+ para Reverse Engineering no MySQL Workbench

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    UNIQUE KEY uk_usuario_email (email),
    UNIQUE KEY uk_usuario_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert inicial (opcional)
INSERT INTO usuario (nome, email, username, senha, ativo) VALUES
('Administrador', 'admin@cafearomaesabor.com', 'admin', 'senha123', TRUE);
