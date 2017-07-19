package br.com.granit.controlador;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.granit.controlador.generico.ControladorGenerico;
import br.com.granit.persistencia.dao.HistoricoProdutoDAO;
import br.com.granit.persistencia.dao.generico.DAO;
import br.com.granit.persistencia.filtro.FiltroHistoricoProduto;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.util.CalendarUtil;
import br.com.granit.util.JPAUtility;

public class ControladorHistoricoProduto extends ControladorGenerico<HistoricoProdutoTO, Integer>{
	
	private static ControladorHistoricoProduto instancia;
	
	private ControladorHistoricoProduto(){
		super();
	}
	
	public static ControladorHistoricoProduto getInstance() {
		if(instancia == null){
			instancia = new ControladorHistoricoProduto();
		}
		return instancia;
	}	
	
	/* (non-Javadoc)
	 * @see br.com.sisvendas.persistencia.controlador.generico.ControladorGeneric#getDAO(javax.persistence.EntityManager)
	 */
	@Override
	public DAO<HistoricoProdutoTO, Integer> getDAO(EntityManager entityManager) {
		HistoricoProdutoDAO dao = new HistoricoProdutoDAO();
		dao.setEntityManager(entityManager);		
		return dao;
	}

	public List<HistoricoProdutoTO> consultarHistoricosFiltro(
			FiltroHistoricoProduto filtro) {
		EntityManager em = JPAUtility.getEntityManager();
		HistoricoProdutoDAO dao = new HistoricoProdutoDAO();		
		dao.setEntityManager(em);
		return dao.consultarFiltro(filtro);
	}

	public List<HistoricoProdutoTO> consultaPorMesToDebug(Integer idProduto, int mes, int ano) {
		CalendarUtil.getInstance().defineMesAno(mes, ano);
		FiltroHistoricoProduto filtro = new FiltroHistoricoProduto();
		filtro.setIdProduto(idProduto);
		filtro.setDataFim(CalendarUtil.getInstance().getDataFimMes());
		filtro.setDataInicio(CalendarUtil.getInstance().getDataInicioMes());
		return consultarHistoricosFiltro(filtro);
	}
	
	public List<HistoricoProdutoTO> consultaPorMes(Integer idProduto, int mes, int ano) {
		CalendarUtil.getInstance().defineMesAno(mes, ano);
		FiltroHistoricoProduto filtro = new FiltroHistoricoProduto();
		filtro.setIdProduto(idProduto);
		filtro.setDataFim(CalendarUtil.getInstance().getDataFimMes());
		filtro.setDataInicio(CalendarUtil.getInstance().getDataInicioMes());
		return consultarHistoricosFiltro(filtro);
	}
	
	public List<HistoricoProdutoTO> consultaPorSemana(Integer idProduto, int mes, int ano, int semana) {
		CalendarUtil.getInstance().defineMesAno(mes, ano);
		FiltroHistoricoProduto filtro = new FiltroHistoricoProduto();
		filtro.setIdProduto(idProduto);
		filtro.setDataFim(CalendarUtil.getInstance().getDataFimSemana(semana));
		filtro.setDataInicio(CalendarUtil.getInstance().getDataInicioSemana(semana));
		return consultarHistoricosFiltro(filtro);
	}
	
	//Algoritmo para recuperar o estoque de um produto em um determinado mês.
	public Double recuperarEstoqueProdutoPorMes(Integer idProduto, int mes, int ano){
		double estoque = 0.0;
		CalendarUtil.getInstance().defineMesAno(mes, ano);		
		List<HistoricoProdutoTO> historicos = consultaPorMes(idProduto, mes, ano);
		if (historicos == null || historicos.isEmpty()){
			//Buscar do próximo mês em diante!!!
			CalendarUtil.getInstance().defineMesAno(mes+1, ano);
			FiltroHistoricoProduto filtro = new FiltroHistoricoProduto();
			filtro.setIdProduto(idProduto);			
			filtro.setDataInicio(CalendarUtil.getInstance().getDataInicioMes());
			historicos = consultarHistoricosFiltro(filtro);
			if (historicos == null || historicos.isEmpty()){
				//Nenhum histórico encontrado, recuperar o estoque atual do produto.
				ProdutoTO produto = ControladorProduto.getInstance().get(idProduto);
				estoque = produto.getQtdEstoque();
			}else{
				HistoricoProdutoTO historico = historicos.get(0);//Recuperar o primeiro historico após o intervalo
				estoque = historico.getEstoqueAnterior().doubleValue();
			}
		}else{
			HistoricoProdutoTO historico = historicos.get(historicos.size()-1);//Recuperar o último historico
			estoque = historico.getEstoque().doubleValue();
		}			
		return estoque;
	}
	
	//Algoritmo para recuperar o estoque de um produto em uma semana de um determinado mês.
	/**
	 * @param idProduto
	 * @param mes Inteiro 1,2,...,12
	 * @param ano
	 * @param semana Inteiro: 1,2,3,4
	 * @return
	 */
	public Double recuperarEstoqueProdutoPorSemana(Integer idProduto, int mes, int ano, int semana){
		double estoque = 0.0;
		CalendarUtil.getInstance().defineMesAno(mes, ano);		
		List<HistoricoProdutoTO> historicos = consultaPorSemana(idProduto, mes, ano, semana);
		if (historicos == null || historicos.isEmpty()){
			//Buscar da próxima semana em diante!!!
			FiltroHistoricoProduto filtro = new FiltroHistoricoProduto();
			filtro.setIdProduto(idProduto);			
			if (semana == 4){//Última semana do mês! Passar para o próximo mês.
				CalendarUtil.getInstance().defineMesAno(mes+1, ano);
				filtro.setDataInicio(CalendarUtil.getInstance().getDataInicioMes());
			}else{
				filtro.setDataInicio(CalendarUtil.getInstance().getDataInicioSemana(semana+1));
			}
			historicos = consultarHistoricosFiltro(filtro);
			if (historicos == null || historicos.isEmpty()){
				//Nenhum histórico encontrado, recuperar o estoque atual do produto.
				ProdutoTO produto = ControladorProduto.getInstance().get(idProduto);
				estoque = produto.getQtdEstoque();
			}else{
				HistoricoProdutoTO historico = historicos.get(0);//Recuperar o primeiro historico após o intervalo
				estoque = historico.getEstoqueAnterior().doubleValue();
			}
		}else{
			HistoricoProdutoTO historico = historicos.get(historicos.size()-1);//Recuperar o último historico
			estoque = historico.getEstoque().doubleValue();
		}			
		return estoque;
	}
}
