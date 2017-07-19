package br.com.granit.persistencia.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.filtro.FiltroVenda;
import br.com.granit.persistencia.to.VendaTO;

public class VendaDAO extends DAOGenerico<VendaTO, Integer>{
	
	public String getOrderBy(){
		return " ORDER BY entidade.dataVenda DESC ";
	}
	
	public List<VendaTO> consultarFiltro(FiltroVenda filtro) {
		List<VendaTO> resultados = new ArrayList<VendaTO>();		
		StringBuffer queryBuf = new StringBuffer(
				"select distinct entidade from " + tipo.getSimpleName()
						+ " entidade ");		
		boolean firstClause = true;
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		if (filtro.getDataInicio() != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("dataVenda >= :dataInicio");
			firstClause = false;
			parametros.put("dataInicio", filtro.getDataInicio());
		}		
		if (filtro.getDataFim() != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("dataVenda <= :dataFim");
			firstClause = false;
			parametros.put("dataFim", filtro.getDataFim());
		}
		
		if (filtro.getIdCliente() != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append("cliente.idPessoa = :idCliente");
			firstClause = false;
			parametros.put("idCliente", filtro.getIdCliente());
		}
		
		
		String hqlQuery = queryBuf.toString();
		Query query = getEntityManager().createQuery(hqlQuery);		
		for (Map.Entry<String, Object> entry : parametros.entrySet()) {
			if (entry.getValue() instanceof Date) {
				query.setParameter(entry.getKey(), (Date) entry.getValue(),
						TemporalType.DATE);
			} else {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		resultados = query.getResultList();
		
		return resultados;
	}
	
	
	protected void adicionarParametrosConsultaPaginada(
			Map<String, Object> parametros, StringBuffer queryBuf) {
		boolean firstClause = true;
		Date dataInicio = (Date)parametros.get("dataInicio");		
		Date dataFim = (Date)parametros.get("dataFim");
		Integer idCliente = (Integer)parametros.get("idCliente");
		Character situacao = (Character) parametros.get("situacao");
		String numeroPedido = (String) parametros.get("numeroPedido");
		
		if (dataInicio != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.dataVenda >= :dataInicio ");
			firstClause = false;			
		}
		
		if (dataFim != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.dataVenda <= :dataFim");
			firstClause = false;
		}
		
		if (idCliente != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.cliente.idPessoa = :idCliente ");
			firstClause = false;
		}
		
		if (situacao != null && !situacao.equals("")) {
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.situacao = :situacao ");
			firstClause = false;
		}
		
		if (numeroPedido != null && !numeroPedido.equals("")) {
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.numeroPedido LIKE :numeroPedido ");
			firstClause = false;
		}
		
	}
	
	protected String preencheCriteriosAgregacao(List<String> totalizadores) {
		String totalProjection = "";
		for (String t : totalizadores) {
			if (!totalProjection.isEmpty()) {
				totalProjection += ", ";
			}
			if ("total".equals(t)){
				totalProjection += " SUM( item.preco ) as " + t;
			}
		}
		return totalProjection;
	}
	
	protected StringBuffer criarConsultaBaseTotalizadores(String totalProjection) {
		StringBuffer queryTotalizadores = new StringBuffer("select "
				+ totalProjection + " from " + tipo.getSimpleName()
				+ " entidade inner join entidade.itens item ");
		return queryTotalizadores;
	}
	
	protected String preencheCriteriosAgregacao2(List<String> totalizadores) {
		String totalProjection = "";
		for (String t : totalizadores) {
			if (!totalProjection.isEmpty()) {
				totalProjection += ", ";
			}
			if ("total".equals(t)){
				totalProjection += " SUM( item.valor ) as " + t;
			}
		}
		return totalProjection;
	}
	
	protected StringBuffer criarConsultaBaseTotalizadores2(String totalProjection) {
		StringBuffer queryTotalizadores = new StringBuffer("select "
				+ totalProjection + " from " + tipo.getSimpleName()
				+ " entidade inner join entidade.itensBeneficiamento item ");
		return queryTotalizadores;
	}

	/**
	  select a.idvenda, a.preco+ifnull(b.preco,0) from
		(select venda.idvenda, ifnull(sum(itemvenda.preco),0) preco, ifnull(sum(itemvenda.preco * venda.desconto)/100,0) desconto
		from venda, itemvenda
		where venda.idVenda = itemvenda.idVenda
		group by venda.idVenda) a left join 
		(select venda.idvenda, ifnull(sum(itemb.valor),0) preco, sum(itemb.valor * venda.desconto)/100 desconto
		from itembenefvenda itemb, venda
		where venda.idVenda = itemb.idvenda
		group by venda.idvenda) b on a.idvenda = b.idvenda
		group by a.idvenda
	 */
}
