package br.com.granit.persistencia.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@NamedQueries({
	
	
	@NamedQuery(name="listaProdutoFornecedorPorProduto", 
			query="Select pf From ProdutoFornecedorTO pf " +
				  "Where pf.produto.idProduto = :idProduto " +
				  " Order by pf.dataUltimaCompra DESC ")
})

@Entity
@Table(name="produtoFornecedor")

public class ProdutoFornecedorTO implements Serializable{
	
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 2898963487372995350L;
	
	private Integer idProdutoFornecedor;
	
	private FornecedorTO fornecedor;
	
	private ProdutoTO produto;
	
	private Date dataUltimaCompra;
	
	private BigDecimal valorUnitarioUltimaCompra;
	
	private Integer posicao;
	
	
	@Transient
	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	@ManyToOne
	@JoinColumn(name="idFornecedor")
	public FornecedorTO getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorTO fornecedor) {
		this.fornecedor = fornecedor;
	}

	@ManyToOne
	@JoinColumn(name="idProduto")
	public ProdutoTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoTO produto) {
		this.produto = produto;
	}

	//@Column(nullable=true)
	//helton = comenteisenão não tem como associar produto a fornecedor no momento da criação
	//de fornecedor.
	@Temporal(TemporalType.DATE)
	public Date getDataUltimaCompra() {
		return dataUltimaCompra;
	}

	public void setDataUltimaCompra(Date dataUltimaCompra) {
		this.dataUltimaCompra = dataUltimaCompra;
	}

	public BigDecimal getValorUnitarioUltimaCompra() {
		return valorUnitarioUltimaCompra;
	}

	public void setValorUnitarioUltimaCompra(BigDecimal valorUnitarioUltimaCompra) {
		this.valorUnitarioUltimaCompra = valorUnitarioUltimaCompra;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getIdProdutoFornecedor() {
		return idProdutoFornecedor;
	}

	public void setIdProdutoFornecedor(Integer idProdutoFornecedor) {
		this.idProdutoFornecedor = idProdutoFornecedor;
	}
}
