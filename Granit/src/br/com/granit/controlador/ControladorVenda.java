package br.com.granit.controlador;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.VendaDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.filtro.FiltroVenda;
import br.com.granit.persistencia.to.VendaTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.JPAUtility;

public class ControladorVenda extends ControladorGenerico<VendaTO, Integer>{

	
	private static ControladorVenda instancia;
	
	private ControladorVenda(){
		super();
	}
	
	public static ControladorVenda getInstance() {
		if(instancia == null){
			instancia = new ControladorVenda();
		}
		return instancia;
	}	
	
	@Override
	public DAO<VendaTO, Integer> getDAO(EntityManager entityManager) {
		VendaDAO dao = new VendaDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

	@SuppressWarnings(Constantes.UNCHECKED)
	public List<VendaTO> consultarFiltro(FiltroVenda filtro){
		EntityManager em = JPAUtility.getEntityManager();
		VendaDAO dao = new VendaDAO();		
		dao.setEntityManager(em);
		return dao.consultarFiltro(filtro);
	}
}
