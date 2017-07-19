//inser��o de novo tipo diversos
insert into tipoProduto values (3,'Diversos');

//inser��o de novos subtipos
insert into subtipoProduto values(31,'Ferramentas',3);
insert into subtipoProduto values(32,'Limpeza',3);
insert into subtipoProduto values(33,'Polimento',3);
insert into subtipoProduto values(34,'E.P.I.',3);
insert into subtipoProduto values(35,'Cubas',3);

//atualiza��o de mudan�as de tipos/subtipos
UPDATE produto SET idSubTipo=31 where idSubTipo = 19; //ferramentas

UPDATE produto SET idSubTipo=32 where idSubTipo = 21; //limpeza

UPDATE produto SET idSubTipo=33 where idSubTipo = 20; //polimento

UPDATE produto SET idSubTipo=34 where idSubTipo = 22; //E.P.I.

UPDATE produto SET idSubTipo=35 where idSubTipo = 23; //cubas
  

//remo��o de subtipos de insumos
DELETE FROM subtipoproduto WHERE idSubTipo = 19; //ferramentas

DELETE FROM subtipoproduto WHERE idSubTipo = 21; //limpeza

DELETE FROM subtipoproduto WHERE idSubTipo = 20; //polimento

DELETE FROM subtipoproduto WHERE idSubTipo = 22; //E.P.I.

DELETE FROM subtipoproduto WHERE idSubTipo = 23;//cubas



//inser��o de novo subtipo insumos/qu�micos

insert into subtipoProduto values(19,'Qu�micos',2);

COMMIT