package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.ItemVendaDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.pk.ItemVendaPK;
import br.com.granit.persistencia.to.ItemVendaTO;
import br.com.granit.util.JPAUtility;


public class ControladorItemVenda extends ControladorGenerico<ItemVendaTO, ItemVendaPK>{

	
	private static ControladorItemVenda instancia;
	
	private ControladorItemVenda(){
		super();
	}
	
	public static ControladorItemVenda getInstance() {
		if(instancia == null){
			instancia = new ControladorItemVenda();
		}
		return instancia;
	}	
	
	@Override
	public DAO<ItemVendaTO, ItemVendaPK> getDAO(EntityManager entityManager) {
		ItemVendaDAO dao = new ItemVendaDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
	
	public void excluirItensDaVenda(Integer idVenda){
		EntityManager em = JPAUtility.getEntityManager();
		DAO<ItemVendaTO, ItemVendaPK> dao = getDAO(em);
		String query = "delete from itemvenda where idVenda = " + idVenda;
		System.out.println(em.createNativeQuery(query).executeUpdate());		
	}
}

