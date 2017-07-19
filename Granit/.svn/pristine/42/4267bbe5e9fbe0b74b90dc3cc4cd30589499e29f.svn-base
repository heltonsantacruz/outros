package br.com.granit.persistencia.to;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tipoProduto")
public class TipoProdutoTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2755754547183827860L;
	@Id
	private Integer idTipo;
	private String descricao;
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="idTipo")
	private List<SubTipoProdutoTO> subtipos;

	public Integer getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(Integer idTipo) {
		this.idTipo = idTipo;
	}

	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<SubTipoProdutoTO> getSubtipos() {
		return subtipos;
	}

	public void setSubtipos(List<SubTipoProdutoTO> subtipos) {
		this.subtipos = subtipos;
	}
	
	

}
