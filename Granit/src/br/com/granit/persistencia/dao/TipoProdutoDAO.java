package br.com.granit.persistencia.dao;

import java.util.List;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.to.TipoProdutoTO;
import br.com.granit.util.Constantes;

public class TipoProdutoDAO extends DAOGenerico<TipoProdutoTO, Integer>{	
	
	public List<TipoProdutoTO> listar() {
		@SuppressWarnings(Constantes.UNCHECKED)
		List<TipoProdutoTO> resultados = getEntityManager().createQuery(
				"select entidade from " + tipo.getSimpleName() + " entidade order by TRIM(entidade.descricao)")
				.getResultList();
		return resultados;
	}
	
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.descricao) ";
	}

}
