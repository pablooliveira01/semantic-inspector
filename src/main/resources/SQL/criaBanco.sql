-- Criando o banco de dados no PgAdmin 4
--Use o Qery Tool para executar este script e criar o banco de dados

CREATE DATABASE db_springboot
  WITH
  OWNER = postgres
  ENCODING = 'UTF8'
  LC_COLLATE = 'Portuguese_Brazil.1252'
  LC_CTYPE = 'Portuguese_Brazil.1252'
  TEMPLATE = template0;