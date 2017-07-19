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

import br.com.granit.persistencia.pk.ItemBeneficiamentoVendaPK;

@Entity
@Table(name="itembenefvenda")
public class ItemBeneficiamentoVendaTO {
	
	private ItemBeneficiamentoVendaPK pk;

	private String descricao;

	private BigDecimal valor;	
	
	private VendaTO venda;
	
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

	public ItemBeneficiamentoVendaTO(){
		pk = new ItemBeneficiamentoVendaPK();
	}
	
	/**
	 * @return the pk
	 */
	@EmbeddedId
	public ItemBeneficiamentoVendaPK getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(ItemBeneficiamentoVendaPK pk) {
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
		if (this.posicao != null && this.pk != null){
			this.pk.setIdItemBeneficiamentoVenda(this.posicao);
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}

