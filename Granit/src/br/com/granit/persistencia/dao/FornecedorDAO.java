package br.com.granit.persistencia.dao;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.to.FornecedorTO;

public class FornecedorDAO extends DAOGenerico<FornecedorTO, Integer>{
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.nome) ";
	}
}
