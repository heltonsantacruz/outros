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

import br.com.granit.persistencia.pk.ItemVendaPK;
import br.com.granit.util.CurrencyUtil;

@Entity
@Table(name="itemvenda")
public class ItemVendaTO {
	
	private ItemVendaPK pk;

	private String descricao;

	private BigDecimal preco;
	
	private BigDecimal metragem;
	
	private VendaTO venda;
	
	private ProdutoTO produto;
	
	/**
	 * Campo não armazenado no banco, apenas utilizado nas páginas JSP.
	 */
	private Integer posicao;

	/**
	 * @return the posicao
	 */
	@Transient
	public Integer getPosicao() {
		return posicao;
	}

	/**
	 * @param posicao the posicao to set
	 */
	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public ItemVendaTO(){
		pk = new ItemVendaPK();
	}
	
	/**
	 * @return the pk
	 */
	@EmbeddedId
	public ItemVendaPK getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(ItemVendaPK pk) {
		this.pk = pk;
	}

	@ManyToOne
	@JoinColumn(name="idVenda", nullable=false, insertable=false, updatable=false)
	public VendaTO getVenda() {
		return venda;
	}
	
	public void setVenda(VendaTO venda) {
		this.venda = venda;
	}
	
	
	@PrePersist
	@PreUpdate
	public void setIdVendaPk(){		
		if (this.venda != null && venda.getIdVenda() != null 
				&& this.pk != null){
			this.pk.setIdVenda(this.venda.getIdVenda());
		}
		if (this.produto != null && produto.getIdProduto() != null 
				&& this.pk != null){
			this.pk.setIdProduto(this.produto.getIdProduto());
		}
		if (this.posicao != null && this.pk != null){
			this.pk.setIdItemVenda(this.posicao);
		}
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		if(descricao != null){
			this.descricao = descricao.toUpperCase();
		}
		else{
			this.descricao = descricao;
		}
		
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@ManyToOne
	@JoinColumn(name="idProduto", nullable=false, insertable=false, updatable=false)
	public ProdutoTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoTO produto) {
		this.produto = produto;
	}

	public BigDecimal getMetragem() {
		return metragem;
	}

	public void setMetragem(BigDecimal metragem) {
		this.metragem = metragem;
	}
	
	@Transient 
	public String getMetragemFormatada() {
		if(metragem != null && metragem.doubleValue() >= 0){
			return CurrencyUtil.getNumberFormatStringQuatroCasas(metragem.doubleValue() + ""); 
		}		
		return "";
	}
}

