package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.TipoProdutoDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.to.TipoProdutoTO;

public class ControladorTipoProduto extends ControladorGenerico<TipoProdutoTO, Integer>{

	private static ControladorTipoProduto instancia;
	
	private ControladorTipoProduto(){
		super();
	}
	
	public static ControladorTipoProduto getInstance() {
		if(instancia == null){
			instancia = new ControladorTipoProduto();
		}
		return instancia;
	}	
	
	/* (non-Javadoc)
	 * @see br.com.sisvendas.persistencia.controlador.generico.ControladorGeneric#getDAO(javax.persistence.EntityManager)
	 */
	@Override
	public DAO<TipoProdutoTO, Integer> getDAO(EntityManager entityManager) {
		TipoProdutoDAO dao = new TipoProdutoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

}
