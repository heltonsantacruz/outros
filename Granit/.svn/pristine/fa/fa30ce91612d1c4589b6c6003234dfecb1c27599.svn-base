package br.com.granit.persistencia.dao;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.to.ProdutoFornecedorTO;

public class ProdutoFornecedorDAO extends DAOGenerico<ProdutoFornecedorTO, Integer>{
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.dataUltimaCompra) desc";
	}
	
	
}
