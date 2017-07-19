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
import br.com.granit.persistencia.to.HistoricoProdutoTO;

public class HistoricoProdutoDAO extends DAOGenerico<HistoricoProdutoTO, Integer>{	
	
	@Override
	public String getOrderBy() {
		return " ORDER BY (entidade.data) DESC ";
	}
	
	
	public List<HistoricoProdutoTO> consultarFiltro(FiltroHistoricoProduto filtro) {
		List<HistoricoProdutoTO> resultados = new ArrayList<HistoricoProdutoTO>();		
		StringBuffer queryBuf = new StringBuffer(
				"select distinct entidade from " + tipo.getSimpleName()
						+ " entidade ");
		boolean firstClause = true;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		if (filtro.getDataInicio() != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("data >= :dataInicio");
			firstClause = false;
			parametros.put("dataInicio", filtro.getDataInicio());
		}		
		if (filtro.getDataFim() != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("data <= :dataFim");
			firstClause = false;
			parametros.put("dataFim", filtro.getDataFim());
		}
		
		if (filtro.getIdProduto() != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("produto.idProduto = :idProduto");
			firstClause = false;
			parametros.put("idProduto", filtro.getIdProduto());
		}	
		
		
		queryBuf.append(" ORDER BY data asc");
		
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
	
	
	@SuppressWarnings("unchecked")
	public List<HistoricoProdutoTO> consultar(Map<String, Object> parametros) {
		List<HistoricoProdutoTO> resultados = new ArrayList<HistoricoProdutoTO>();
		StringBuffer queryBuf = criarConsultaBasePaginada();
		boolean firstClause = true;
		/**
		 * Obs: se tiver consultar por campos de outras entidades tem que passar
		 * o nome completo do atributo, por exemplo: Key:
		 * "entidade.entidade.nome " Value:"valor"
		 */
		for (Map.Entry<String, Object> entry : parametros.entrySet()) {
			if (entry.getValue() instanceof String) {
				queryBuf.append(firstClause ? " where " : " and ");
				queryBuf.append("LOWER("
						+ entry.getKey()
						+ ")"
						+ " LIKE :"
						+ entry.getKey().replaceAll(STRING_PONTO,
								STRING_UNDERLINE));
				firstClause = false;
			} else {
				queryBuf.append(firstClause ? " where " : " and ");
				if(entry.getValue() instanceof Date){
					if(entry.getKey().equals("dataInicio")){
						queryBuf.append("data >= :"
								+ entry.getKey());
						firstClause = false;
					}
					else if(entry.getKey().equals("dataFim")){
						queryBuf.append("data <= :"
								+ entry.getKey());
						firstClause = false;
					}
				}
				else{
					queryBuf.append(entry.getKey()
							+ " = :"
							+ entry.getKey().replaceAll(STRING_PONTO,
									STRING_UNDERLINE));
					firstClause = false;
				}
				
			}
		}

		if (getOrderBy() != null && !getOrderBy().isEmpty()) {
			queryBuf.append(getOrderBy());
		}

		String hqlQuery = queryBuf.toString();
		Query query = getEntityManager().createQuery(hqlQuery);
		
		for (Map.Entry<String, Object> entry : parametros.entrySet()) {
			if (entry.getValue() instanceof Date) {
				query.setParameter(entry.getKey(), (Date) entry.getValue(),
						TemporalType.DATE);
			}
			if (entry.getValue() instanceof String) {
				query.setParameter(entry.getKey().replaceAll(STRING_PONTO,
						STRING_UNDERLINE), ((String) entry.getValue())
						.toLowerCase());
			} else {
				query.setParameter(entry.getKey().replaceAll(STRING_PONTO,
						STRING_UNDERLINE), entry.getValue());
			}
		}
		resultados = query.getResultList();

		return resultados;
	}

}
