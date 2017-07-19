package br.com.granit.persistencia.dao;

import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.persistencia.dao.generico.DAOGenerico;

public class ClienteDAO extends DAOGenerico<ClienteTO, Integer>{
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.nome) ";
	}
}
