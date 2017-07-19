package br.com.granit.persistencia.dao.generico;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.util.Constantes;
import br.com.granit.util.ResultadoPaginacao;

public abstract class DAOGenerico<T, PK extends Serializable> implements
		DAO<T, PK> {

	private EntityManager entityManager;

	protected Class<T> tipo;

	/**
	 * Valor de substituição da string " . " por ele.
	 */
	public static final String STRING_UNDERLINE = "_";

	/**
	 * String ponto a ser substituida nas consutas do tipo:
	 * entidade.entidade.nome
	 */
	public static final String STRING_PONTO = "\\.";

	@SuppressWarnings(Constantes.UNCHECKED)
	public DAOGenerico() {
		this.tipo = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.granit.persistencia.util.dao.DAO#listar()
	 */
	public List<T> listar() {
		@SuppressWarnings(Constantes.UNCHECKED)
		List<T> resultados = entityManager.createQuery(
				"select entidade from " + tipo.getSimpleName() + " entidade"
						+ getOrderBy()).getResultList();
		return resultados;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.granit.persistencia.util.dao.DAO#consultarPorQuery(java.lang.String,
	 *      java.util.Map)
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public List<T> consultarNamedQuery(String nameQuery,
			Map<String, Object> parametros) {
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parametros != null) {
			for (Map.Entry<String, Object> entry : parametros.entrySet()) {
				if (entry.getValue() instanceof Date) {
					query.setParameter(entry.getKey(), (Date) entry.getValue(),
							TemporalType.DATE);
				} else {
					query.setParameter(entry.getKey(), entry.getValue());
				}
			}
		}
		List resultados = query.getResultList();
		return resultados;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.granit.persistencia.util.dao.DAO#consultarPorQuery(java.lang.String,
	 *      java.lang.Object[])
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public List<T> consultarNamedQuery(String nameQuery, Object[] parametros) {
		Query query = entityManager.createNamedQuery(nameQuery);
		if (parametros != null) {
			int posicao = 1;
			for (Object valor : parametros) {
				if (valor instanceof Date) {
					query.setParameter(posicao++, (Date) valor,
							TemporalType.DATE);
				} else {
					query.setParameter(posicao++, valor);
				}
			}
		}
		List resultados = query.getResultList();
		return resultados;
	}

	@SuppressWarnings("unchecked")
	public List<T> consultar(Map<String, Object> parametros) {
		List<T> resultados = new ArrayList<T>();
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
				queryBuf.append(entry.getKey()
						+ " = :"
						+ entry.getKey().replaceAll(STRING_PONTO,
								STRING_UNDERLINE));
				firstClause = false;
			}
		}

		if (getOrderBy() != null && !getOrderBy().isEmpty()) {
			queryBuf.append(getOrderBy());
		}

		String hqlQuery = queryBuf.toString();
		Query query = entityManager.createQuery(hqlQuery);		
		
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

	/**
	 * Método para realizar consultar que são montadas dinamicamente a partir
	 * dos parâmetros que são passados para a mesma.
	 * 
	 * @param parametros
	 * @return
	 */
	public ResultadoPaginacao consultarPaginado(Map<String, Object> parametros,
			int pagina, int tuplasPorPagina, List<String> totalizadores, List<String> totalizadores2,
			List<String> totalizadores3) {
		ResultadoPaginacao resultado = new ResultadoPaginacao();
		resultado.setParametrosDaConsulta(parametros);
		List<T> resultados = new ArrayList<T>();
		StringBuffer queryBuf = criarConsultaBasePaginada();

		StringBuffer queryCount = criarConsultaBaseCountPaginada();

		calculaTotalizadores(totalizadores, resultado, parametros);
		calculaTotalizadores2(totalizadores2, resultado, parametros);
		calculaTotalizadores3(totalizadores3, resultado, parametros);
		
		adicionarParametrosConsultaPaginada(parametros, queryBuf);
		adicionarParametrosConsultaPaginada(parametros, queryCount);

		queryBuf.append(getOrderBy());

		String hqlQuery = queryBuf.toString();
		String hqlCountQuery = queryCount.toString();
		Query query = entityManager.createQuery(hqlQuery);
		Query countQuery = entityManager.createQuery(hqlCountQuery);
		
		setarParametrosConsultaPaginada(parametros, query);
		setarParametrosConsultaPaginada(parametros, countQuery);
		
		//Paginação
		Long quantidade = (Long) countQuery.getSingleResult();
		if (quantidade != null) {
			int primeiro = ((pagina - 1) * tuplasPorPagina) + 1;
			query.setFirstResult(primeiro - 1);
			int maxResults = (tuplasPorPagina);
			// verifica se estourou o limite
			if ((primeiro + maxResults) > quantidade) {
				maxResults = (quantidade.intValue() - primeiro) + 1;
			}
			query.setMaxResults(maxResults);
			resultados = query.getResultList();
			resultado.setTotalRegistros(quantidade.intValue());
			resultado.setPaginaAtual(pagina);
			int totalPaginas = quantidade.intValue() / tuplasPorPagina;
			if ((quantidade.intValue() % tuplasPorPagina) != 0) {
				totalPaginas++;
			}
			resultado.setTotalPaginas(totalPaginas);
		} else {
			resultado.setPaginaAtual(0);
			resultado.setTotalPaginas(0);
			resultado.setTotalRegistros(0);
		}
		resultado.setResult(resultados);
		return resultado;
	}

	protected StringBuffer criarConsultaBaseCountPaginada() {
		StringBuffer queryCount = new StringBuffer("select count("
				+ recuperaStringIdEntidade() + ") from " + tipo.getSimpleName()
				+ " entidade ");
		return queryCount;
	}

	protected StringBuffer criarConsultaBasePaginada() {
		StringBuffer queryBuf = new StringBuffer(
				"select distinct entidade from " + tipo.getSimpleName()
						+ " entidade ");
		return queryBuf;
	}

	protected void setarParametrosConsultaPaginada(
			Map<String, Object> parametros, Query query) {
		for (Map.Entry<String, Object> entry : parametros.entrySet()) {
			if (entry.getValue() == null){
				continue;
			}
			
			if (entry.getValue() instanceof Date) {
				query.setParameter(entry.getKey(), (Date) entry.getValue(),
						TemporalType.TIMESTAMP);
			}
			if (entry.getValue() instanceof String) {
				query.setParameter(entry.getKey().replaceAll(STRING_PONTO,
						STRING_UNDERLINE), "%"
						+ ((String) entry.getValue()).toLowerCase() + "%");
			} else {
				query.setParameter(entry.getKey().replaceAll(STRING_PONTO,
						STRING_UNDERLINE), entry.getValue());
			}
		}
	}

	protected void adicionarParametrosConsultaPaginada(
			Map<String, Object> parametros, StringBuffer queryBuf) {
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
				queryBuf.append(entry.getKey()
						+ " = :"
						+ entry.getKey().replaceAll(STRING_PONTO,
								STRING_UNDERLINE));
				firstClause = false;
			}
		}
	}

	private void calculaTotalizadores(List<String> totalizadores,
			ResultadoPaginacao resultado, Map<String, Object> parametros) {
		if (totalizadores != null && !totalizadores.isEmpty()) {
			String totalProjection = preencheCriteriosAgregacao(totalizadores);
			StringBuffer queryTotalizadores = criarConsultaBaseTotalizadores(totalProjection);

			adicionarParametrosConsultaPaginada(parametros, queryTotalizadores);
			
			System.out.println(queryTotalizadores.toString());
			
			Query totalizadoresQuery = entityManager
					.createQuery(queryTotalizadores.toString());
			setarParametrosConsultaPaginada(parametros, totalizadoresQuery);

			Object result = totalizadoresQuery.getSingleResult();
			Map<String, BigDecimal> resultadoTotalizadores = new HashMap<String, BigDecimal>();
			if (totalizadores.size() == 1) {
				resultadoTotalizadores.put(totalizadores.get(0),
						(BigDecimal) result);
			} else {
				for (int i = 0; i < totalizadores.size(); i++) {
					Object totalizadorValor = (((Object[]) result)[i]);
					if (totalizadorValor instanceof Double){
						totalizadorValor = new BigDecimal((Double)totalizadorValor);
					}
					resultadoTotalizadores.put(totalizadores.get(i), (BigDecimal) totalizadorValor);
				}
			}
			resultado.setTotalizadores(resultadoTotalizadores);
		}
	}
	
	private void calculaTotalizadores2(List<String> totalizadores2,
			ResultadoPaginacao resultado, Map<String, Object> parametros) {
		if (totalizadores2 != null && !totalizadores2.isEmpty()) {
			String totalProjection = preencheCriteriosAgregacao2(totalizadores2);
			StringBuffer queryTotalizadores = criarConsultaBaseTotalizadores2(totalProjection);

			adicionarParametrosConsultaPaginada(parametros, queryTotalizadores);
			
			System.out.println(queryTotalizadores.toString());
			
			Query totalizadoresQuery = entityManager
					.createQuery(queryTotalizadores.toString());
			setarParametrosConsultaPaginada(parametros, totalizadoresQuery);

			Object result = totalizadoresQuery.getSingleResult();
			Map<String, BigDecimal> resultadoTotalizadores = new HashMap<String, BigDecimal>();
			if (totalizadores2.size() == 1) {
				resultadoTotalizadores.put(totalizadores2.get(0),
						(BigDecimal) result);
			} else {
				for (int i = 0; i < totalizadores2.size(); i++) {
					Object totalizadorValor = (((Object[]) result)[i]);
					if (totalizadorValor instanceof Double){
						totalizadorValor = new BigDecimal((Double)totalizadorValor);
					}
					resultadoTotalizadores.put(totalizadores2.get(i), (BigDecimal) totalizadorValor);
				}
			}
			resultado.setTotalizadores2(resultadoTotalizadores);
		}
	}
	
	private void calculaTotalizadores3(List<String> totalizadores3,
			ResultadoPaginacao resultado, Map<String, Object> parametros) {
		if (totalizadores3 != null && !totalizadores3.isEmpty()) {
			String totalProjection = preencheCriteriosAgregacao3(totalizadores3);
			StringBuffer queryTotalizadores = criarConsultaBaseTotalizadores3(totalProjection);

			adicionarParametrosConsultaPaginada(parametros, queryTotalizadores);
			
			System.out.println(queryTotalizadores.toString());
			
			Query totalizadoresQuery = entityManager
					.createQuery(queryTotalizadores.toString());
			setarParametrosConsultaPaginada(parametros, totalizadoresQuery);

			Object result = totalizadoresQuery.getSingleResult();
			Map<String, BigDecimal> resultadoTotalizadores = new HashMap<String, BigDecimal>();
			if (totalizadores3.size() == 1) {
				resultadoTotalizadores.put(totalizadores3.get(0),
						(BigDecimal) result);
			} else {
				for (int i = 0; i < totalizadores3.size(); i++) {
					Object totalizadorValor = (((Object[]) result)[i]);
					if (totalizadorValor instanceof Double){
						totalizadorValor = new BigDecimal((Double)totalizadorValor);
					}
					resultadoTotalizadores.put(totalizadores3.get(i), (BigDecimal) totalizadorValor);
				}
			}
			resultado.setTotalizadores3(resultadoTotalizadores);
		}
	}

	protected StringBuffer criarConsultaBaseTotalizadores2(String totalProjection) {
		return criarConsultaBaseTotalizadores(totalProjection);
	}

	protected String preencheCriteriosAgregacao2(List<String> totalizadores2) {
		return preencheCriteriosAgregacao(totalizadores2);
	}
	
	protected StringBuffer criarConsultaBaseTotalizadores3(String totalProjection) {
		StringBuffer queryTotalizadores = new StringBuffer("select "
				+ totalProjection + " from " + tipo.getSimpleName()
				+ " entidade ");
		return queryTotalizadores;
	}

	protected String preencheCriteriosAgregacao3(List<String> totalizadores3) {
		String totalProjection = "";
		for (String t : totalizadores3) {
			if (!totalProjection.isEmpty()) {
				totalProjection += ", ";
			}
			totalProjection += " SUM( entidade." + t + ") as " + t;
		}
		return totalProjection;
	}

	protected String preencheCriteriosAgregacao(List<String> totalizadores) {
		String totalProjection = "";
		for (String t : totalizadores) {
			if (!totalProjection.isEmpty()) {
				totalProjection += ", ";
			}
			totalProjection += " SUM( entidade." + t + ") as " + t;
		}
		return totalProjection;
	}

	protected StringBuffer criarConsultaBaseTotalizadores(String totalProjection) {
		StringBuffer queryTotalizadores = new StringBuffer("select "
				+ totalProjection + " from " + tipo.getSimpleName()
				+ " entidade ");
		return queryTotalizadores;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.granit.persistencia.util.dao.DAO#get(java.io.Serializable)
	 */
	public T get(PK pk) {
		return entityManager.find(tipo, pk);
	}

	public T salva(T entidade) throws JPAInsertException {
		try {
			entityManager.persist(entidade);
			return entidade;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new JPAInsertException(Constantes.MSG_ERRO_INSERCAO, e);
		}
	}

	public void atualiza(T entidade) throws JPAUpdateException {
		try {
			entityManager.merge(entidade);
		} catch (RuntimeException e) {
			throw new JPAUpdateException(Constantes.MSG_ERRO_ATUALIZACAO, e);
		}
	}
	

	

	public void remove(T entidade) throws JPADeleteException {
		try {
			Object toRemove = entityManager.merge(entidade);
			entityManager.remove(toRemove);
		} catch (Exception e) {
			throw new JPADeleteException(Constantes.MSG_ERRO_REMOCAO, e);
		}
	}

	public void flush() {
		entityManager.flush();
	}

	public void clear() {
		entityManager.clear();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	// @PersistenceContext(unitName="JPA")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public String getOrderBy() {
		return "";
	}

	public String recuperaStringIdEntidade() {
		return "entidade";
	}
}
