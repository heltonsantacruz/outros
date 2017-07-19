package br.com.granit.controlador;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.HistoricoProdutoDAO;
import br.com.granit.persistencia.dao.ProdutoDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.filtro.FiltroHistoricoProduto;
import br.com.granit.persistencia.filtro.FiltroProduto;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.util.JPAUtility;

public class ControladorProduto extends ControladorGenerico<ProdutoTO, Integer>{
	
	private static ControladorProduto instancia;
	
	private ControladorProduto(){
		super();
	}
	
	public static ControladorProduto getInstance() {
		if(instancia == null){
			instancia = new ControladorProduto();
		}
		return instancia;
	}	
	
	/* (non-Javadoc)
	 * @see br.com.sisvendas.persistencia.controlador.generico.ControladorGeneric#getDAO(javax.persistence.EntityManager)
	 */
	@Override
	public DAO<ProdutoTO, Integer> getDAO(EntityManager entityManager) {
		ProdutoDAO dao = new ProdutoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

	public List<ProdutoTO> consultarProdutoTipo(int tipo) {
		FiltroProduto filtro = new FiltroProduto();		
		filtro.setTipo(tipo);		
		return consultarProdutosFiltro(filtro);
	}
	
	public List<ProdutoTO> consultarProdutosFiltro(
			FiltroProduto filtro) {
		EntityManager em = JPAUtility.getEntityManager();
		ProdutoDAO dao = new ProdutoDAO();		
		dao.setEntityManager(em);
		return dao.consultarFiltro(filtro);
	}
	
	
	
}
