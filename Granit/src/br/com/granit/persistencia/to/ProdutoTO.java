package br.com.granit.persistencia.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.granit.util.CurrencyUtil;

@Entity
@Table(name="produto")
@NamedQueries({
	@NamedQuery(
		name="produto_nome_descricao", 
		query="SELECT produto FROM ProdutoTO produto WHERE" +
			" nome like :nome And descricao like :descricao"),
	@NamedQuery(
		name="produto_estoque_precovenda", 
		query="SELECT produto FROM ProdutoTO produto WHERE " +
				"qtdEstoque = ?1 And precoVenda = ?2 ")
})
public class ProdutoTO implements Serializable, Comparable<ProdutoTO>{

	private static final long serialVersionUID = 2315670551664761700L;
	
	private Integer idProduto;
	private String descricao;
	private Double qtdEstoque;
	private List<HistoricoProdutoTO> historicosEstoque;
	private List<ProdutoFornecedorTO> fornecedores;
	
	//caso seja matéria prima o tipo corresponderá a 'C' = Chapa e 'L' = Ladrilho
	//Caso seja insumo o campo será null
	private Character tipo;
	
	
	
	@Column(nullable=true, length=1)
	public Character getTipo() {
		return tipo;
	}


	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}
	
	@Transient
	public boolean existeEntradaSaidaMaiorQueZero(){
		List<HistoricoProdutoTO> hist = getHistoricosEstoque();
		for (HistoricoProdutoTO historicoProdutoTO : hist) {
			if(historicoProdutoTO.getQuantidade().doubleValue() > 0){
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public String getTipoMateriaPrima(){
		String retorno = "";
		if(getNomeTipoMateriaPrima().equals("")){
			retorno = getSubTipo().getTipo().getDescricao();
		}
		else{
			retorno =  getSubTipo().getTipo().getDescricao() + "/" + getNomeTipoMateriaPrima();
		}
		return retorno;
	}
	
	@Transient
	public String getDescricaoTipoSubTipo(){	
		String retorno = "";
		if(getNomeTipoMateriaPrima().equals("")){
			retorno = getSubTipo().getTipo().getDescricao() +"/" + getSubTipo().getDescricao() + "/" + getDescricao();
		}
		else{
			retorno =  getSubTipo().getTipo().getDescricao() + "/" + getNomeTipoMateriaPrima()+ "/" + getSubTipo().getDescricao() + "/" + getDescricao(); 
		}
		return retorno;
	}
	
	@Transient
	public String getNomeTipoMateriaPrima(){
		if(tipo != null){
			if(tipo.equals('C')){
				return "Chapa";
			}
			else if (tipo.equals('L')){
				return "Ladrilho";
			}
		}
		return "";
	}


	@Transient
	public String getTipoDetalhado(){
		if(subTipo != null){
			if(subTipo.getTipo().getIdTipo() == 1){
				return subTipo.getTipo().getDescricao() + "/" + getNomeTipoMateriaPrima();
			}
			else if (subTipo.getTipo().getIdTipo() == 2){
				return subTipo.getTipo().getDescricao();
			}
			else if (subTipo.getTipo().getIdTipo() == 3){
				return subTipo.getTipo().getDescricao();
			}
		}		
		return "";
	}

	@Transient
	public String getTipoSubtipo(){
		if(subTipo != null){
			return subTipo.getTipo().getDescricao() + "/" + subTipo.getDescricao();
		}
		return "";
	}
	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)	
	public Integer getIdProduto() {
		return idProduto;
	}
	
	
	
	private SubTipoProdutoTO subTipo;

	@ManyToOne 
	@JoinColumn(name="idSubTipo")
	public SubTipoProdutoTO getSubTipo() {
		return subTipo;
	}


	public void setSubTipo(SubTipoProdutoTO subTipo) {
		this.subTipo = subTipo;
	}


	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}


	@Column(nullable=false, length=50)
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Double getQtdEstoque() {
		return qtdEstoque;
	}

	public void setQtdEstoque(Double qtdEstoque) {
		this.qtdEstoque = qtdEstoque;
	}	
	
	@Transient 
	public String getQtdEstoqueFormatado() {
		if(qtdEstoque != null && qtdEstoque.doubleValue() >= 0){	
			return CurrencyUtil.getNumberFormatStringQuatroCasas(qtdEstoque.doubleValue() + "");
			
		}		
		return "";
	}

	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="idProduto")
	public List<HistoricoProdutoTO> getHistoricosEstoque() {
		return historicosEstoque;
	}

	public void setHistoricosEstoque(List<HistoricoProdutoTO> historicosEstoque) {
		this.historicosEstoque = historicosEstoque;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="idProduto")
	public List<ProdutoFornecedorTO> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<ProdutoFornecedorTO> fornecedores) {
		this.fornecedores = fornecedores;
	}


	@Override
	public int compareTo(ProdutoTO o) {
		//Verifica se ambas são matéria prima
		if (!this.getNomeTipoMateriaPrima().isEmpty() && !o.getNomeTipoMateriaPrima().isEmpty()){
			if (!this.getNomeTipoMateriaPrima().equals(o.getNomeTipoMateriaPrima())){
				//Se for os mesmo tipo entao compara o subtipo abaixo!
				return this.getNomeTipoMateriaPrima().compareTo(o.getNomeTipoMateriaPrima());
			}
		}
		if (this.subTipo != null && o.getSubTipo() != null){
			return this.subTipo.getDescricao().compareTo(o.getSubTipo().getDescricao());
		}
		return this.descricao.compareTo(o.descricao);
	}
	
	@PrePersist
	@PreUpdate	
	public void toUpperCase(){
		if (this.descricao != null)
			this.descricao = this.descricao.toUpperCase();
	}
}
