package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.PerfilDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.to.PerfilTO;

public class ControladorPerfil extends ControladorGenerico<PerfilTO, Integer>{

private static ControladorPerfil instancia;
	
	private ControladorPerfil(){
		super();
	}
	
	public static ControladorPerfil getInstance() {
		if(instancia == null){
			instancia = new ControladorPerfil();
		}
		return instancia;
	}	
	
	@Override
	public DAO<PerfilTO, Integer> getDAO(EntityManager entityManager) {
		PerfilDAO dao = new PerfilDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
}
