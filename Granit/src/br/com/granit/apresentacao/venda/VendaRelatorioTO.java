package br.com.granit.apresentacao.venda;

public class VendaRelatorioTO {

	private String formaPagamento;
	private String codigo;
	private String nomeCliente;
	private Double valor;
	private Double desconto;
	private Double total;
	private String data;
	private Double valorEntrega;
	private Double quantidade;
	private String produtoString;
	
	public String getProdutoString() {
		return produtoString;
	}
	public void setProdutoString(String produtoString) {
		this.produtoString = produtoString;
	}
	public Double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	public Double getValorEntrega() {
		return valorEntrega;
	}
	public void setValorEntrega(Double valorEntrega) {
		this.valorEntrega = valorEntrega;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
}
