package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.ItemPedidoDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.pk.ItemPedidoPK;
import br.com.granit.persistencia.pk.ItemVendaPK;
import br.com.granit.persistencia.to.ItemPedidoTO;
import br.com.granit.persistencia.to.ItemVendaTO;
import br.com.granit.util.JPAUtility;

public class ControladorItemPedido extends ControladorGenerico<ItemPedidoTO, ItemPedidoPK>{

	
	private static ControladorItemPedido instancia;
	
	private ControladorItemPedido(){
		super();
	}
	
	public static ControladorItemPedido getInstance() {
		if(instancia == null){
			instancia = new ControladorItemPedido();
		}
		return instancia;
	}	
	
	@Override
	public DAO<ItemPedidoTO, ItemPedidoPK> getDAO(EntityManager entityManager) {
		ItemPedidoDAO dao = new ItemPedidoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
	
	public void excluirItensDoPedido(Integer idPedido){
		EntityManager em = JPAUtility.getEntityManager();
		DAO<ItemPedidoTO, ItemPedidoPK> dao = getDAO(em);
		String query = "delete from itempedido where idPedido = " + idPedido;
		System.out.println(em.createNativeQuery(query).executeUpdate());		
	}
	
	

}
