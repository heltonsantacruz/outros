package br.com.granit.controlador.generico;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.exception.ConstraintViolationException;

import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.util.Constantes;
import br.com.granit.util.JPAUtility;
import br.com.granit.util.ResultadoPaginacao;

public abstract class ControladorGenerico<T, PK extends Serializable> implements Controlador<T,PK> {
		
	protected Class<T> tipoTO;	
	
	@SuppressWarnings(Constantes.UNCHECKED)
	public ControladorGenerico(){
		this.tipoTO = (Class<T>) 
			((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];		
	}
	
	public void atualiza(T entidade) throws JPAUpdateException {
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);		
		JPAUtility.beginTransaction(em);
		dao.atualiza((T)entidade);		
		JPAUtility.commitTransaction(em);
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#consultar(java.util.Map)
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public List<T> consultar(Map<String, Object> parametros){
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);		
		return dao.consultar(parametros);
	}
	
	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#consultar(java.util.Map)
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public ResultadoPaginacao consultarPaginado(Map<String, Object> parametros, int pagina, int tuplasPorPagina){
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);		
		return dao.consultarPaginado(parametros, pagina, tuplasPorPagina, null, null, null);
	}
	
	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#consultar(java.util.Map)
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public ResultadoPaginacao<T> consultarPaginado(Map<String, Object> parametros, int pagina, int tuplasPorPagina, List<String> totalizadores,
			List<String> totalizadores2, List<String> totalizadores3){
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);		
		return dao.consultarPaginado(parametros, pagina, tuplasPorPagina, totalizadores, totalizadores2, totalizadores3);
	}

	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#consultarPorQuery(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public List<T> consultarPorQuery(String nameQuery, Map<String, Object> parametros) {
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);
		return dao.consultarNamedQuery(nameQuery, parametros);
	}

	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#consultarPorQuery(java.lang.String, java.lang.Object[])
	 */
	public List<T> consultarPorQuery(String nameQuery, Object[] parametros) {
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);
		return dao.consultarNamedQuery(nameQuery, parametros);
	}

	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#get(java.io.Serializable)
	 */
	@SuppressWarnings(Constantes.UNCHECKED)
	public T get(PK pk) {
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);		
		return dao.get(pk);
	}

	/* (non-Javadoc)
	 * @see br.com.granit.persistencia.controlador.generico.Controlador#listar()
	 */
	public List<T> listar() {
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);
		return dao.listar();
	}

	@SuppressWarnings(Constantes.UNCHECKED)
	public void remove(T entidade) throws JPADeleteException {
		try{
			EntityManager em = JPAUtility.getEntityManager();
			DAO<T, PK> dao = getDAO(em);
			JPAUtility.beginTransaction(em);
			dao.remove((T)entidade);
			JPAUtility.commitTransaction(em);
		}catch (Exception e){
			if (e instanceof ConstraintViolationException || e.getCause() instanceof ConstraintViolationException){
				throw new JPADeleteException(Constantes.ERRO_CONSTRAINT_EXCLUSAO);
			}else{
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings(Constantes.UNCHECKED)
	public T salva(T entidade) throws JPAInsertException {	
		EntityManager em = JPAUtility.getEntityManager();
		DAO<T, PK> dao = getDAO(em);
		JPAUtility.beginTransaction(em);
		T retorno = dao.salva((T)entidade);
		JPAUtility.commitTransaction(em);
		return retorno;		
	}
	
	@SuppressWarnings(Constantes.UNCHECKED)
	public abstract DAO<T, PK> getDAO(EntityManager entityManager);

}
