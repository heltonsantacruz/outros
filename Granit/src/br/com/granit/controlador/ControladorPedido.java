package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.PedidoDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.to.PedidoTO;

public class ControladorPedido  extends ControladorGenerico<PedidoTO, Integer>{

private static ControladorPedido instancia;
	
	private ControladorPedido(){
		super();
	}
	
	public static ControladorPedido getInstance() {
		if(instancia == null){
			instancia = new ControladorPedido();
		}
		return instancia;
	}	
	
	@Override
	public DAO<PedidoTO, Integer> getDAO(EntityManager entityManager) {
		PedidoDAO dao = new PedidoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}


}
