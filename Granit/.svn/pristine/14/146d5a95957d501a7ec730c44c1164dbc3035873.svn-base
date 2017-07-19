package br.com.granit.persistencia.dao;

import java.util.List;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.to.SubTipoProdutoTO;
import br.com.granit.util.Constantes;

public class SubTipoProdutoDAO extends DAOGenerico<SubTipoProdutoTO, Integer>{	
	
	public List<SubTipoProdutoTO> listar() {
		@SuppressWarnings(Constantes.UNCHECKED)
		List<SubTipoProdutoTO> resultados = getEntityManager().createQuery(
				"select entidade from " + tipo.getSimpleName() + " entidade order by TRIM(entidade.descricao)")
				.getResultList();
		return resultados;
	}
	
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.descricao) ";
	}

}
