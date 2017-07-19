package br.com.granit.persistencia.dao.generico;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.util.ResultadoPaginacao;

public interface DAO<T , PK extends Serializable> {

	/**
	 * Listar todas as tuplas de uma determinada entidade.
	 * @return
	 */
	public List<T> listar();

	/**
	 * Retorna uma lista de <code>T</code> a partir de uma
	 * query <code>query</code> pre-definida utilizando a anotação namedYuery 
	 * de JPA 
	 * @param query
	 * @param parametros Os parâmetros da query
	 * @return
	 */
	public List<T> consultarNamedQuery(String query,
			Map<String, Object> parametros);

	/**
	 * Retorna uma lista de <code>T</code> a partir de uma
	 * query <code>query</code> pre-definida utilizando a anotação namedYuery 
	 * de JPA 
	 * @param query
	 * @param parametros
	 * @return
	 */
	public List<T> consultarNamedQuery(String query, Object[] parametros);

	/**
	 * Método para realizar consultar que são montadas dinamicamente a partir
	 * dos parâmetros que são passados para a mesma.
	 * @param parametros
	 * @return
	 */
	public List<T> consultar(Map<String, Object> parametros);
	
	/**
	 * Método para realizar consultar que são montadas dinamicamente a partir
	 * dos parâmetros que são passados para a mesma.
	 * @param parametros
	 * @return
	 */
	public ResultadoPaginacao consultarPaginado(Map<String, Object> parametros, int pagina, int tuplasPorPagina, List<String> totalizadores,
			List<String> totalizadores2, List<String> totalizadores3);

	/**
	 * Retorna uma instância de <code>T</code> pela chave <code>pk</code>
	 * @param pk
	 * @return
	 */
	public T get(PK pk);

	public T salva(T entidade) throws JPAInsertException;

	public void atualiza(T entidade) throws JPAUpdateException;

	public void remove(T entidade) throws JPADeleteException;

	public void flush();

	public void clear();
	
	public String getOrderBy();
	
}
