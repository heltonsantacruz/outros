--Mudando metragem para 4 casas 
ALTER TABLE itemvenda MODIFY COLUMN metragem decimal(19,4) NULL
GO

--Aumentando limite de caracteres de endereco e bairro
ALTER TABLE cliente MODIFY COLUMN bairro varchar(100) NULL
GO
ALTER TABLE cliente MODIFY COLUMN endereco varchar(200) NULL
GO

--Depois que rodar a primeira vez e adicionar o campo produtosEntregues na venda, setar todas para TRUE OU FALSE
-- FINALIZADA --> TRUE
update venda set produtosEntregues = 1 where situacao = 'F'; 
--FALSE
update venda set produtosEntregues = 0 where situacao <> 'F'; 