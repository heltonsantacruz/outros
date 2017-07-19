package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.SubTipoProdutoDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.to.SubTipoProdutoTO;

public class ControladorSubTipoProduto extends ControladorGenerico<SubTipoProdutoTO, Integer>{

	private static ControladorSubTipoProduto instancia;
	
	private ControladorSubTipoProduto(){
		super();
	}
	
	public static ControladorSubTipoProduto getInstance() {
		if(instancia == null){
			instancia = new ControladorSubTipoProduto();
		}
		return instancia;
	}	
	
	/* (non-Javadoc)
	 * @see br.com.sisvendas.persistencia.controlador.generico.ControladorGeneric#getDAO(javax.persistence.EntityManager)
	 */
	@Override
	public DAO<SubTipoProdutoTO, Integer> getDAO(EntityManager entityManager) {
		SubTipoProdutoDAO dao = new SubTipoProdutoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

}
