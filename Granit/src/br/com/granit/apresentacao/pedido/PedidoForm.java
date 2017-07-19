package br.com.granit.apresentacao.pedido;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Transient;

import org.apache.struts.validator.ValidatorForm;

import br.com.granit.persistencia.to.ItemPedidoTO;

/**
 * @author Helton
 *
 */
public class PedidoForm extends ValidatorForm{

	private boolean erro;
	private Integer idProduto;
	private Integer idFornecedor;
	private String quantidadeProduto;
	private String precoProduto;
	private List listaItens;
	private Boolean finalizado;
	private String desconto;
	private BigDecimal totalSemDesconto;
	private String totalComDesconto;
	private String paginaRetorno;
	private String dataInicio;
	private String dataFim;
	private boolean mostrarPedidosFinalizados;
	private String tipoProduto;
	private String subTipoProduto;
	private Integer idTipo;
	private Integer idSubTipo;
	
	
	//retirar após carga inicial de historico
	private String dataPedido;
	
	
	public void cleanForm(){
		this.erro = false;
		this.idProduto = 0;
		this.idFornecedor = 0;
		this.quantidadeProduto = "";
		this.precoProduto = "";
		this.listaItens = null;
		this.finalizado = false;
		this.desconto = "";
		this.totalSemDesconto = null;
		this.totalComDesconto = null;
		this.paginaRetorno = "";
		this.dataInicio = "";
		this.dataFim = "";
		this.mostrarPedidosFinalizados = false;
		this.tipoProduto = "";
		this.subTipoProduto = "";
		this.idTipo = null;
		this.idSubTipo = null;
		this.dataPedido = "";
		
	}
	
	
	
	public String getDataPedido() {
		return dataPedido;
	}



	public void setDataPedido(String dataPedido) {
		this.dataPedido = dataPedido;
	}



	public Integer getIdTipo() {
		return idTipo;
	}



	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}



	public Integer getIdSubTipo() {
		return idSubTipo;
	}



	public void setIdSubTipo(Integer idSubTipo) {
		this.idSubTipo = idSubTipo;
	}



	public String getTipoProduto() {
		return tipoProduto;
	}



	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}



	public String getSubTipoProduto() {
		return subTipoProduto;
	}



	public void setSubTipoProduto(String subTipoProduto) {
		this.subTipoProduto = subTipoProduto;
	}



	public boolean isMostrarPedidosFinalizados() {
		return mostrarPedidosFinalizados;
	}

	public void setMostrarPedidosFinalizados(boolean mostrarPedidosFinalizados) {
		this.mostrarPedidosFinalizados = mostrarPedidosFinalizados;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getPaginaRetorno() {
		return paginaRetorno;
	}

	public void setPaginaRetorno(String paginaRetorno) {
		this.paginaRetorno = paginaRetorno;
	}

	public BigDecimal getTotalSemDesconto() {
		return totalSemDesconto;
	}

	public void setTotalSemDesconto(BigDecimal totalSemDesconto) {
		this.totalSemDesconto = totalSemDesconto;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	

	public String getTotalComDesconto() {
		return totalComDesconto;
	}

	public void setTotalComDesconto(String totalComDesconto) {
		this.totalComDesconto = totalComDesconto;
	}

	public Boolean getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}

	public List getListaItens() {
		return listaItens;
	}

	public void setListaItens(List listaItens) {
		this.listaItens = listaItens;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}

	public Integer getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Integer idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public String getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(String quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public String getPrecoProduto() {
		return precoProduto;
	}

	public void setPrecoProduto(String precoProduto) {
		this.precoProduto = precoProduto;
	}

	public boolean isErro() {
		return erro;
	}

	public void setErro(boolean erro) {
		this.erro = erro;
	}

	
	
	
	
	
}
