package br.com.granit.controlador;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.UsuarioDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.filtro.FiltroUsuario;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.JPAUtility;

public class ControladorUsuario extends ControladorGenerico<UsuarioTO, Integer>{

	
	private static ControladorUsuario instancia;
	
	private ControladorUsuario(){
		super();
	}
	
	public static ControladorUsuario getInstance() {
		if(instancia == null){
			instancia = new ControladorUsuario();
		}
		return instancia;
	}	
	
	@Override
	public DAO<UsuarioTO, Integer> getDAO(EntityManager entityManager) {
		UsuarioDAO dao = new UsuarioDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

	@SuppressWarnings(Constantes.UNCHECKED)
	public List<UsuarioTO> consultarFiltro(FiltroUsuario filtro){
		EntityManager em = JPAUtility.getEntityManager();
		UsuarioDAO dao = new UsuarioDAO();		
		dao.setEntityManager(em);
		return dao.consultarFiltro(filtro);
	}
}
