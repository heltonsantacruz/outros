package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.ProdutoFornecedorDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.to.ProdutoFornecedorTO;

public class ControladorProdutoFornecedor extends ControladorGenerico<ProdutoFornecedorTO, Integer>{

	
	private static ControladorProdutoFornecedor instancia;
	
	private ControladorProdutoFornecedor(){
		super();
	}
	
	public static ControladorProdutoFornecedor getInstance() {
		if(instancia == null){
			instancia = new ControladorProdutoFornecedor();
		}
		return instancia;
	}	
	
	@Override
	public DAO<ProdutoFornecedorTO, Integer> getDAO(EntityManager entityManager) {
		ProdutoFornecedorDAO dao = new ProdutoFornecedorDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
	

}
