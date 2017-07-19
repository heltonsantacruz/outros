package br.com.granit.persistencia.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ItemVendaPK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3565185156274136491L;

	private Integer idItemVenda;
	
	private Integer idVenda;
	
	private Integer idProduto;
	
	/**
	 * @return the idVenda
	 */
	public Integer getIdVenda() {
		return idVenda;
	}

	/**
	 * @param idVenda the idVenda to set
	 */
	public void setIdVenda(Integer idVenda) {
		this.idVenda = idVenda;
	}

	/**
	 * @return the idItemVenda
	 */	
	public Integer getIdItemVenda() {
		return idItemVenda;
	}

	/**
	 * @param idItemVenda the idItemVenda to set
	 */
	public void setIdItemVenda(Integer idItemVenda) {
		this.idItemVenda = idItemVenda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idItemVenda == null) ? 0 : idItemVenda.hashCode());
		result = prime * result
				+ ((idProduto == null) ? 0 : idProduto.hashCode());
		result = prime * result + ((idVenda == null) ? 0 : idVenda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemVendaPK other = (ItemVendaPK) obj;
		if (idItemVenda == null) {
			if (other.idItemVenda != null)
				return false;
		} else if (!idItemVenda.equals(other.idItemVenda))
			return false;
		if (idProduto == null) {
			if (other.idProduto != null)
				return false;
		} else if (!idProduto.equals(other.idProduto))
			return false;
		if (idVenda == null) {
			if (other.idVenda != null)
				return false;
		} else if (!idVenda.equals(other.idVenda))
			return false;
		return true;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
}
