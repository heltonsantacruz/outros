package br.com.granit.controlador.generico;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAQueryException;
import br.com.granit.persistencia.excecao.JPAUpdateException;

public interface Controlador<T, PK extends Serializable> {

	/**
	 * Listar todas as tuplas de uma determinada entidade.
	 * @return
	 */
	public List<T> listar();
	
	/**
	 * Retorna uma lista de <code>T</code> que segue o critério
	 * de consulta definido pela query <code>query</code>
	 * @param query
	 * @param parametros Os parâmetros da query
	 * @return
	 */
	public List<T> consultarPorQuery(String query, Map<String, Object> parametros);
	
	/**
	 * Retorna uma lista de <code>T</code> que segue o critério
	 * de consulta definido pela query <code>query</code>
	 * @param query
	 * @param parametros
	 * @return
	 */
	public List<T> consultarPorQuery(String query, Object[] parametros);
	
	/**
	 * Método para realizar consultar que são montadas dinamicamente a partir
	 * dos parâmetros que são passados para a mesma.
	 * @param parametros
	 * @return
	 * @throws JPAQueryException
	 */
	public List<T> consultar(Map<String, Object> parametros);
	
	/**
	 * Retorna uma instância de <code>T</code> pela chave <code>pk</code>
	 * @param pk
	 * @return
	 */
	public T get(PK pk);
	
	public T salva(T entidade) throws JPAInsertException;
	
	public void atualiza(T entidade) throws JPAUpdateException;
	
	public void remove(T entidade) throws JPADeleteException;	
	
	public DAO<T, PK> getDAO(EntityManager entityManager);
}
