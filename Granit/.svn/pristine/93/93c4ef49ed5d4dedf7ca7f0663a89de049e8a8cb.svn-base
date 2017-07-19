package br.com.granit.persistencia.to;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.granit.persistencia.pk.ItemPedidoPK;
import br.com.granit.util.CurrencyUtil;


@Entity
@Table(name="itemPedido")
public class ItemPedidoTO {
	
	@EmbeddedId
    private ItemPedidoPK pk;
	
	private BigDecimal quantidade;
	
	private BigDecimal precoUnitario;	
	
	
	private BigDecimal precoTotal;
	
	
	
	
	public BigDecimal getPrecoTotal() {
		return precoTotal;
	}


	public void setPrecoTotal(BigDecimal precoTotal) {
		this.precoTotal = precoTotal;
	}

	@Transient
	private Integer posicao;
	
	
	
	public Integer getPosicao() {
		return posicao;
	}


	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	@ManyToOne
	@JoinColumn(name="idProduto", nullable=false, insertable=false, updatable=false)
	private ProdutoTO produto;
	
	@ManyToOne
	@JoinColumn(name="idPedido", nullable=false, insertable=false, updatable=false)
	private PedidoTO pedido;

	
	public ItemPedidoTO(){
		pk = new ItemPedidoPK();
	}
	
	
	public ItemPedidoPK getPk() {
		return pk;
	}

	public void setPk(ItemPedidoPK pk) {
		this.pk = pk;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}


	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}


	public ProdutoTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoTO produto) {
		this.produto = produto;
	}

	public PedidoTO getPedido() {
		return pedido;
	}

	public void setPedido(PedidoTO pedido) {
		this.pedido = pedido;
	}
	
	
	public String getPrecoUnitarioFormatado(){
		if(precoUnitario != null && precoUnitario.doubleValue() > 0){
			return "R$ " + CurrencyUtil.getNumberFormatStringQuatroCasas(precoUnitario.toString()); 
		}		
		return "";	
	}	
	
	public String getQuantidadeFormatada(){
		if(quantidade != null && quantidade.doubleValue() > 0){		
			return CurrencyUtil.getNumberFormatStringQuatroCasas(quantidade.doubleValue() + ""); 
		}		
		return "";	
	}
	
	@PrePersist
	@PreUpdate	
	public void setIdPedidoPk(){		
		if (this.pedido != null && pedido.getIdPedido() != null 
				&& this.pk != null){
			this.pk.setIdPedido(this.pedido.getIdPedido());
		}
		if (this.posicao != null && this.pk != null){
			this.pk.setIdItemPedido(this.posicao);
		}
	}
	
	

}
