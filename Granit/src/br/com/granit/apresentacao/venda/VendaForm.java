package br.com.granit.apresentacao.venda;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import br.com.granit.persistencia.to.ItemVendaTO;
import br.com.granit.persistencia.to.VendaTO;
import br.com.granit.util.Formatador;

public class VendaForm extends ValidatorForm {

	private static final long serialVersionUID = -1538824507608470278L;

	private String valorParcela;
	private String estado;	
	private String dataParcela;
	private VendaTO venda;
	private ItemVendaTO itemEmInclusao;
	private String codigoCliente;
	
	private String idCliente;
	
	private String desconto;
	private String valorEntrega;
	
	/*Forma de Pagamento*/
	private String vista;
	private String valorVista;
	private String cheque;
	private String valorCheque;
	private String credito;
	private String valorCredito;
	private String aberto;
	private String valorAberto;
	private String numeroParcelas;
	private String senhaInadimplencia;
	private String valorBoleto;
	
	private String valorJaPago;
	
	private String descricaoItem;
	
	private String valorItem;
	
	/*Campos parâmetros da consulta*/
	private String dataInicio;
	private String dataFim;
	
	private String situacaoVenda;

	private String dataInformadaVenda;
	private String dataEntrega;
	private String metragem;
	private String idProdutoItem;
	private String observacao;
	
	private String numeroPedido;
	private String numeroPedidoAno;
	
	private String valorServicoMontagem;
	
	public String getValorServicoMontagem() {
		return valorServicoMontagem;
	}

	public void setValorServicoMontagem(String valorServicoMontagem) {
		this.valorServicoMontagem = valorServicoMontagem;
	}

	/* Dados de cobrança de serviços */
	private String descricaoServicoConfeccao;
	
	private String valorServicoConfeccao;
	
	
   
	
	private String valorServicoMontagemVenda;
	
	
	private String descricaoItemBeneficiamento;
	
	private String valorItemBeneficiamento;
	
	private String valorServicoMontagemBeneficiamento;
	
	
	private boolean aplicarDesconto;
	
	
	
	
	
	



	public boolean isAplicarDesconto() {
		return aplicarDesconto;
	}

	public void setAplicarDesconto(boolean aplicarDesconto) {
		this.aplicarDesconto = aplicarDesconto;
	}

	public String getValorServicoMontagemBeneficiamento() {
		return valorServicoMontagemBeneficiamento;
	}

	public void setValorServicoMontagemBeneficiamento(
			String valorServicoMontagemBeneficiamento) {
		this.valorServicoMontagemBeneficiamento = valorServicoMontagemBeneficiamento;
	}

	public String getValorItemBeneficiamento() {
		return valorItemBeneficiamento;
	}

	public void setValorItemBeneficiamento(String valorItemBeneficiamento) {
		this.valorItemBeneficiamento = valorItemBeneficiamento;
	}

	public String getDescricaoItemBeneficiamento() {
		return descricaoItemBeneficiamento;
	}

	public void setDescricaoItemBeneficiamento(String descricaoItemBeneficiamento) {
		this.descricaoItemBeneficiamento = descricaoItemBeneficiamento;
	}

	public String getValorServicoMontagemVenda() {
		return valorServicoMontagemVenda;
	}

	public void setValorServicoMontagemVenda(String valorServicoMontagemVenda) {
		this.valorServicoMontagemVenda = valorServicoMontagemVenda;
	}

	

	public String getDescricaoServicoConfeccao() {
		return descricaoServicoConfeccao;
	}

	public void setDescricaoServicoConfeccao(String descricaoServicoConfeccao) {
		this.descricaoServicoConfeccao = descricaoServicoConfeccao;
	}

	public String getValorServicoConfeccao() {
		return valorServicoConfeccao;
	}

	public void setValorServicoConfeccao(String valorServicoConfeccao) {
		this.valorServicoConfeccao = valorServicoConfeccao;
	}

	public String getValorJaPago() {
		return valorJaPago;
	}

	public void setValorJaPago(String valorJaPago) {
		this.valorJaPago = valorJaPago;
	}

	public String getSenhaInadimplencia() {
		return senhaInadimplencia;
	}

	public void setSenhaInadimplencia(String senhaInadimplencia) {
		this.senhaInadimplencia = senhaInadimplencia;
	}

	/*Adição de duplicata Inicio 04/12/09 */
	private String duplicata;
	private String numeroParcelasDuplicata;
	private String valorDuplicata;
	
	/*Adição de duplicata Fim 04/12/09 */
	
	
	private boolean entregue;
	
	

	public boolean isEntregue() {
		return entregue;
	}

	public void setEntregue(boolean entregue) {
		this.entregue = entregue;
	}

	public VendaForm() {
		super();
		this.venda = new VendaTO();
		this.itemEmInclusao = new ItemVendaTO();
		
	}	
	
	/**
	 * @return the codigo
	 */
	public String getCodigoCliente() {
		return codigoCliente;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigoCliente(String codigo) {
		this.codigoCliente = codigo;
	}
		
	/**
	 * @return the dataVenda
	 */
	public String getDataVenda() {		
		return Formatador.getDataFormatada(getVenda().getDataVenda());
	}
	/**
	 * @return the subTotal
	 */
	public String getSubTotal() {
		return getVenda().getTotalFormatado();
	}

	/**
	 * @return the desconto
	 */
	public String getDesconto() {
		return desconto;
	}
	
	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}
	
	/**
	 * @return the total
	 */
	public String getTotal() {
		return getVenda().getTotalFormatado();
	}
	
	/**
	 * @return the itemEmInclusao
	 */
	public ItemVendaTO getItemEmInclusao() {
		return itemEmInclusao;
	}
	/**
	 * @param itemEmInclusao the itemEmInclusao to set
	 */
	public void setItemEmInclusao(ItemVendaTO itemEmInclusao) {
		this.itemEmInclusao = itemEmInclusao;
	}
	/**
	 * @return the itensVenda
	 */
	public List<ItemVendaTO> getItensVenda() {
		return getVenda().getItens();
	}
	/**
	 * @return the aVista
	 */
	public String getVista() {
		return vista;
	}
	/**
	 * @param vista the aVista to set
	 */
	public void setVista(String vista) {
		this.vista = vista;
	}
	/**
	 * @return the valorAVista
	 */
	public String getValorVista() {
		return valorVista;
	}
	/**
	 * @param valorVista the valorVista to set
	 */
	public void setValorVista(String valorVista) {
		this.valorVista = valorVista;
	}
	
	/**
	 * @return the cheque
	 */
	public String getCheque() {
		return cheque;
	}
	/**
	 * @param cheque the cheque to set
	 */
	public void setCheque(String cheque) {
		this.cheque = cheque;		
	}
	/**
	 * @return the valorCheque
	 */
	public String getValorCheque() {
		return valorCheque;
	}
	/**
	 * @param valorCheque the valorCheque to set
	 */
	public void setValorCheque(String valorCheque) {
		this.valorCheque = valorCheque;		
	}
	/**
	 * @return the credito
	 */
	public String getCredito() {
		return credito;
	}
	/**
	 * @param credito the credito to set
	 */
	public void setCredito(String credito) {
		this.credito = credito;
	}
	/**
	 * @return the valorCredito
	 */
	public String getValorCredito() {
		return valorCredito;
	}
	/**
	 * @param valorCredito the valorCredito to set
	 */
	public void setValorCredito(String valorCredito) {
		this.valorCredito = valorCredito;
	}
	/**
	 * @return the aberto
	 */
	public String getAberto() {
		return aberto;
	}
	/**
	 * @param aberto the aberto to set
	 */
	public void setAberto(String aberto) {
		this.aberto = aberto;
	}
	/**
	 * @return the valorAberto
	 */
	public String getValorAberto() {
		return valorAberto;		
	}
	
	
	public String getValorDuplicataFormatado(){
		Double valorDuplicata = Formatador.parseDouble(getValorDuplicata());
		return Formatador.currency(valorDuplicata);
	}
	
	public String getValorAbertoFormatado(){		
		Double valorAberto = Formatador.parseDouble(getValorAberto());
		return Formatador.currency(valorAberto);	
	}
	
	public String getValorVistaFormatado(){		
		Double valorVista = Formatador.parseDouble(getValorVista());
		return Formatador.currency(valorVista);
	}
	
	public String getValorChequeFormatado(){
		Double valorCheque = Formatador.parseDouble(getValorCheque());
		return Formatador.currency(valorCheque);
	}
	
	public String getValorCreditoFormatado(){
		Double valorCredito = Formatador.parseDouble(getValorCredito());
		return Formatador.currency(valorCredito);
	}
	
	/**
	 * @param valorAberto the valorAberto to set
	 */
	public void setValorAberto(String valorAberto) {
		this.valorAberto = valorAberto;
	}


	/**
	 * @return the numeroParcelas
	 */
	public String getNumeroParcelas() {
		return numeroParcelas;
	}


	/**
	 * @param numeroParcelas the numeroParcelas to set
	 */
	public void setNumeroParcelas(String numeroParcelas) {
		this.numeroParcelas = numeroParcelas;
	}


	/**
	 * @return the valorParcela
	 */
	public String getValorParcela() {
		return valorParcela;
	}


	/**
	 * @param valorParcela the valorParcela to set
	 */
	public void setValorParcela(String valorParcela) {
		this.valorParcela = valorParcela;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the dataParcela
	 */
	public String getDataParcela() {
		return dataParcela;
	}


	/**
	 * @param dataParcela the dataParcela to set
	 */
	public void setDataParcela(String dataParcela) {
		this.dataParcela = dataParcela;
	}

	/**
	 * @return the venda
	 */
	public VendaTO getVenda() {
		if (venda == null)
			venda = new VendaTO();
		return venda;
	}

	/**
	 * @param venda the venda to set
	 */
	public void setVenda(VendaTO venda) {
		this.venda = venda;
	}

	public String getValorEntrega() {
		return valorEntrega;
	}

	public void setValorEntrega(String valorEntrega) {
		this.valorEntrega = valorEntrega;
	}

	public String getDuplicata() {
		return duplicata;
	}

	public void setDuplicata(String duplicata) {
		this.duplicata = duplicata;
	}

	public String getNumeroParcelasDuplicata() {
		return numeroParcelasDuplicata;
	}

	public void setNumeroParcelasDuplicata(String numeroParcelasDuplicata) {
		this.numeroParcelasDuplicata = numeroParcelasDuplicata;
	}

	public String getValorDuplicata() {
		return valorDuplicata;
	}

	public void setValorDuplicata(String valorDuplicata) {
		this.valorDuplicata = valorDuplicata;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public String getValorItem() {
		return valorItem;
	}

	public void setValorItem(String valorItem) {
		this.valorItem = valorItem;
	}

	public String getValorBoleto() {
		return valorBoleto;
	}

	public void setValorBoleto(String valorBoleto) {
		this.valorBoleto = valorBoleto;
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

	public String getSituacaoVenda() {
		return situacaoVenda;
	}

	public void setSituacaoVenda(String situacaoVenda) {
		this.situacaoVenda = situacaoVenda;
	}

	public String getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getMetragem() {
		return metragem;
	}

	public void setMetragem(String metragem) {
		this.metragem = metragem;
	}

	public String getIdProdutoItem() {
		return idProdutoItem;
	}

	public void setIdProdutoItem(String idProdutoItem) {
		this.idProdutoItem = idProdutoItem;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getNumeroPedidoAno() {
		return numeroPedidoAno;
	}

	public void setNumeroPedidoAno(String numeroPedidoAno) {
		this.numeroPedidoAno = numeroPedidoAno;
	}

	public String getDataInformadaVenda() {
		return dataInformadaVenda;
	}

	public void setDataInformadaVenda(String dataInformadaVenda) {
		this.dataInformadaVenda = dataInformadaVenda;
	}	
}
