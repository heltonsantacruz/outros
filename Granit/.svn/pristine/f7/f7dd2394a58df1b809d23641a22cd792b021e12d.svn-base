package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.FornecedorDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.pk.ItemPedidoPK;
import br.com.granit.persistencia.to.FornecedorTO;
import br.com.granit.persistencia.to.ItemPedidoTO;
import br.com.granit.util.JPAUtility;

public class ControladorFornecedor extends ControladorGenerico<FornecedorTO, Integer>{

	
	private static ControladorFornecedor instancia;
	
	private ControladorFornecedor(){
		super();
	}
	
	public static ControladorFornecedor getInstance() {
		if(instancia == null){
			instancia = new ControladorFornecedor();
		}
		return instancia;
	}	
	
	@Override
	public DAO<FornecedorTO, Integer> getDAO(EntityManager entityManager) {
		FornecedorDAO dao = new FornecedorDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

	public void excluirProdutosDoFornecedor(Integer idFornecedor) {
		EntityManager em = JPAUtility.getEntityManager();
		DAO<FornecedorTO, Integer> dao = getDAO(em);
		String query = "delete from produtofornecedor where idFornecedor = " + idFornecedor;
		System.out.println(em.createNativeQuery(query).executeUpdate());	
		
	}

}
