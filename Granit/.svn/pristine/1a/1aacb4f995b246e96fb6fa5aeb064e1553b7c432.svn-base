package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.persistencia.dao.EstadoDAO;
import br.com.granit.persistencia.to.EstadoTO;
import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.generico.DAO;

public class ControladorEstado extends ControladorGenerico<EstadoTO, String>{

	
	private static ControladorEstado instancia;
	
	private ControladorEstado(){
		super();
	}
	
	public static ControladorEstado getInstance() {
		if(instancia == null){
			instancia = new ControladorEstado();
		}
		return instancia;
	}	
	
	@Override
	public DAO<EstadoTO, String> getDAO(EntityManager entityManager) {
		EstadoDAO dao = new EstadoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
}
