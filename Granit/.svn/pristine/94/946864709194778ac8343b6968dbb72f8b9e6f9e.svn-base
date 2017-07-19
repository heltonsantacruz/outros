package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.persistencia.dao.MunicipioDAO;
import br.com.granit.persistencia.to.MunicipioTO;
import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.generico.DAO;

public class ControladorMunicipio extends ControladorGenerico<MunicipioTO, Integer>{

	
	private static ControladorMunicipio instancia;
	
	private ControladorMunicipio(){
		super();
	}
	
	public static ControladorMunicipio getInstance() {
		if(instancia == null){
			instancia = new ControladorMunicipio();
		}
		return instancia;
	}	
	
	@Override
	public DAO<MunicipioTO, Integer> getDAO(EntityManager entityManager) {
		MunicipioDAO dao = new MunicipioDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
}
