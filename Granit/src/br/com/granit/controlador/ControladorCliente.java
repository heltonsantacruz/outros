package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.persistencia.dao.ClienteDAO;
import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.generico.DAO;

public class ControladorCliente extends ControladorGenerico<ClienteTO, Integer>{

	
	private static ControladorCliente instancia;
	
	private ControladorCliente(){
		super();
	}
	
	public static ControladorCliente getInstance() {
		if(instancia == null){
			instancia = new ControladorCliente();
		}
		return instancia;
	}	
	
	@Override
	public DAO<ClienteTO, Integer> getDAO(EntityManager entityManager) {
		ClienteDAO dao = new ClienteDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

}
