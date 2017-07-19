package br.com.granit.persistencia.dao;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.filtro.FiltroHistoricoProduto;
import br.com.granit.persistencia.filtro.FiltroProduto;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.util.Constantes;

public class ProdutoDAO extends DAOGenerico<ProdutoTO, Integer>{	
	
	public List<ProdutoTO> listar() {
		@SuppressWarnings(Constantes.UNCHECKED)
		List<ProdutoTO> resultados = getEntityManager().createQuery(
				"select entidade from " + tipo.getSimpleName() + " entidade order by TRIM(entidade.descricao)")
				.getResultList();
		return resultados;
	}
	
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.descricao) ";
	}

	public List<ProdutoTO> consultarFiltro(FiltroProduto filtro) {
		List<ProdutoTO> resultados = new ArrayList<ProdutoTO>();		
		StringBuffer queryBuf = new StringBuffer(
				"select distinct entidade from " + tipo.getSimpleName()
						+ " entidade ");
		boolean firstClause = true;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		if (filtro.getTipo() > 0){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("idSubTipo in (select idSubTipo from SubTipoProdutoTO sub where sub.tipo.idTipo = :idTipo)");
			firstClause = false;
			parametros.put("idTipo", filtro.getTipo());
		}		
		
		//queryBuf.append(" ORDER BY idTipo");
		
		String hqlQuery = queryBuf.toString();
		
		Query query = getEntityManager().createQuery(hqlQuery);		
		for (Map.Entry<String, Object> entry : parametros.entrySet()) {
			if (entry.getValue() instanceof Date) {
				query.setParameter(entry.getKey(), (Date) entry.getValue(),
						TemporalType.DATE);
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		resultados = query.getResultList();
		
		return resultados;
	}
	
}
