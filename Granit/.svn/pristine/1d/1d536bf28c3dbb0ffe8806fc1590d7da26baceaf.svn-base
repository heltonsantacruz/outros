package br.com.granit.persistencia.dao;

import java.util.Date;
import java.util.Map;

import br.com.granit.persistencia.dao.generico.DAOGenerico;
import br.com.granit.persistencia.to.PedidoTO;

public class PedidoDAO extends DAOGenerico<PedidoTO, Integer>{
	@Override
	public String getOrderBy() {
		return " ORDER BY TRIM(entidade.dataPedido) desc ";
	}
	
	
	protected void adicionarParametrosConsultaPaginada(
			Map<String, Object> parametros, StringBuffer queryBuf) {
		boolean firstClause = true;
		Date dataInicio = (Date)parametros.get("dataInicio");		
		Date dataFim = (Date)parametros.get("dataFim");
		Boolean mostrarPedidosFinalizados = null;
		if(parametros.get("mostrarPedidosFinalizados") != null){
			mostrarPedidosFinalizados = (Boolean)parametros.get("mostrarPedidosFinalizados");
		}
		 
		Integer idFornecedor = (Integer)parametros.get("idFornecedor");
		
		
		if (dataInicio != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.dataPedido >= :dataInicio ");
			firstClause = false;			
		}
		
		if (dataFim != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.dataPedido <= :dataFim");
			firstClause = false;
		}
		
		if (mostrarPedidosFinalizados != null){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.finalizado = :mostrarPedidosFinalizados");
			firstClause = false;
		}
		
		if (idFornecedor != null && idFornecedor > 0){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.fornecedor.idFornecedor = :idFornecedor ");
			firstClause = false;
		}		
		
		Integer idTipo = (Integer)parametros.get("idTipo");
		if (idTipo != null && idTipo > 0){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.idPedido in (select item.pedido.idPedido from ItemPedidoTO item where item.pedido.idPedido = pedido.idPedido and item.produto.subTipo.tipo.idTipo = :idTipo)");
			firstClause = false;
		}
		
		Integer subTipo = (Integer)parametros.get("subTipo");
		if (subTipo != null && subTipo > 0){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.idPedido in (select item.pedido.idPedido from ItemPedidoTO item where item.pedido.idPedido = pedido.idPedido and item.produto.subTipo.idSubTipo = :subTipo)");
			firstClause = false;
		}
		
		Integer idProduto = (Integer)parametros.get("idProduto");
		if (idProduto != null && idProduto > 0){
			queryBuf.append(firstClause ? " where " : " and ");
			queryBuf.append(" entidade.idPedido in (select item.pedido.idPedido from ItemPedidoTO item where item.pedido.idPedido = pedido.idPedido and item.produto.idProduto = :idProduto)");
			firstClause = false;
		}
	
	}
	


}
