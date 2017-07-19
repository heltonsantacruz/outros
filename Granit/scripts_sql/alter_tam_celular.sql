ALTER TABLE cliente
     modify  COLUMN telefoneCelular varchar(14) 


ALTER TABLE fornecedor
     modify  COLUMN telefoneCelular varchar(14) 
     
update cliente set telefoneCelular =  concat(substr(telefoneCelular, 1, 4), "9", substr(telefoneCelular, 5))
where telefoneCelular is not null and telefoneCelular <> ""

update fornecedor set telefoneCelular =  concat(substr(telefoneCelular, 1, 4), "9", substr(telefoneCelular, 5))
where telefoneCelular is not null and telefoneCelular <> ""     