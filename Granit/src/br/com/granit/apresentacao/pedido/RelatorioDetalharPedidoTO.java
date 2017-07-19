package br.com.granit.apresentacao.pedido;

public class RelatorioDetalharPedidoTO {
	
	private String codigo;
	private String descricao;
	private Double quantidade;
	private Double precoUnitario;
	private Double precoTotal;
	private String tipoSubtipo;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	public Double getPrecoUnitario() {
		return precoUnitario;
	}
	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
	public Double getPrecoTotal() {
		return precoTotal;
	}
	public void setPrecoTotal(Double precoTotal) {
		this.precoTotal = precoTotal;
	}
	public String getTipoSubtipo() {
		return tipoSubtipo;
	}
	public void setTipoSubtipo(String tipoSubtipo) {
		this.tipoSubtipo = tipoSubtipo;
	}
	
	
	
}
