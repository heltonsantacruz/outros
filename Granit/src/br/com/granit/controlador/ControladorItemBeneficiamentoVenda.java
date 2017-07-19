package br.com.granit.controlador;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.ItemBeneficiamentoVendaDAO;
import br.com.granit.persistencia.dao.ItemVendaDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.pk.ItemBeneficiamentoVendaPK;
import br.com.granit.persistencia.pk.ItemVendaPK;
import br.com.granit.persistencia.to.ItemBeneficiamentoVendaTO;
import br.com.granit.persistencia.to.ItemVendaTO;
import br.com.granit.util.JPAUtility;


public class ControladorItemBeneficiamentoVenda extends ControladorGenerico<ItemBeneficiamentoVendaTO, ItemBeneficiamentoVendaPK>{

	
	private static ControladorItemBeneficiamentoVenda instancia;
	
	private ControladorItemBeneficiamentoVenda(){
		super();
	}
	
	public static ControladorItemBeneficiamentoVenda getInstance() {
		if(instancia == null){
			instancia = new ControladorItemBeneficiamentoVenda();
		}
		return instancia;
	}	
	
	@Override
	public DAO<ItemBeneficiamentoVendaTO, ItemBeneficiamentoVendaPK> getDAO(EntityManager entityManager) {
		ItemBeneficiamentoVendaDAO dao = new ItemBeneficiamentoVendaDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}
	
	public void excluirItensDeBeneficiamentoDaVenda(Integer idVenda){
		EntityManager em = JPAUtility.getEntityManager();
		DAO<ItemBeneficiamentoVendaTO, ItemBeneficiamentoVendaPK> dao = getDAO(em);
		String query = "delete from itembenefvenda where idVenda = " + idVenda;
		System.out.println(em.createNativeQuery(query).executeUpdate());		
	}
}

