package br.com.granit.persistencia.to;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedQueries({
	
					
	@NamedQuery(name="listaSubTipoPorTipoProduto", 
			query="Select sub From SubTipoProdutoTO sub " +
				  "Where sub.tipo.idTipo = :tipo " +
				  " Order by sub.descricao ASC ")
})

@Entity
@Table(name="subtipoproduto")
public class SubTipoProdutoTO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1639866999139540864L;
	@Id
	private Integer idSubTipo;
	private String descricao;
	
	@ManyToOne 
	@JoinColumn(name="idTipo")
	private TipoProdutoTO tipo;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idSubTipo")
	private List<ProdutoTO> listaProdutos;
		
	
	

	public List<ProdutoTO> getListaProdutos() {
		return listaProdutos;
	}

	public void setListaProdutos(List<ProdutoTO> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public Integer getIdSubTipo() {
		return idSubTipo;
	}

	public void setIdSubTipo(Integer idSubTipo) {
		this.idSubTipo = idSubTipo;
	}

	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoProdutoTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoProdutoTO tipo) {
		this.tipo = tipo;
	}

	
	
}
