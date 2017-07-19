package br.com.granit.persistencia.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ItemBeneficiamentoVendaPK implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 557815207277125807L;

	private Integer idItemBeneficiamentoVenda;
	
	private Integer idVenda;
	
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

	public Integer getIdItemBeneficiamentoVenda() {
		return idItemBeneficiamentoVenda;
	}

	public void setIdItemBeneficiamentoVenda(Integer idItemBeneficiamentoVenda) {
		this.idItemBeneficiamentoVenda = idItemBeneficiamentoVenda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idItemBeneficiamentoVenda == null) ? 0
						: idItemBeneficiamentoVenda.hashCode());
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
		ItemBeneficiamentoVendaPK other = (ItemBeneficiamentoVendaPK) obj;
		if (idItemBeneficiamentoVenda == null) {
			if (other.idItemBeneficiamentoVenda != null)
				return false;
		} else if (!idItemBeneficiamentoVenda
				.equals(other.idItemBeneficiamentoVenda))
			return false;
		if (idVenda == null) {
			if (other.idVenda != null)
				return false;
		} else if (!idVenda.equals(other.idVenda))
			return false;
		return true;
	}
}
