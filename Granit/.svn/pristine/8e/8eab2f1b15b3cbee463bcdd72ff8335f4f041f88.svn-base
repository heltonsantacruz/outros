package br.com.granit.persistencia.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.sun.istack.internal.Nullable;

import br.com.granit.util.Constantes;
import br.com.granit.util.CurrencyUtil;
import br.com.granit.util.Formatador;

@Entity
@Table(name="historicoProduto")

public class HistoricoProdutoTO implements Serializable{	

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8022039762795201752L;
	
	private Integer idHistoricoProduto;
	
	private Date data = new Date();
	
	private char tipo; //'E' - entrada ou 'S' - saída
	
	private BigDecimal quantidade;
	
	private ProdutoTO produto;
	
	private BigDecimal estoque;
	
	private UsuarioTO usuario;
	
	@Transient
	public String getNomeUsuario(){
		if(usuario == null){
			return "N/A";
		}
		else{
			return usuario.getNome();
		}
	}
	
	@ManyToOne 
	@JoinColumn(name="idUsuario", nullable=true)
	public UsuarioTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioTO usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getEstoque() {
		return estoque;
	}
	
	@Transient
	public BigDecimal getEstoqueAnterior() {
		if (tipo == 'E')
			return estoque.subtract(quantidade);
		else
			return estoque.add(quantidade);
	}

	public void setEstoque(BigDecimal estoque) {
		this.estoque = estoque;
	}

	private String observacao;
	
	
	@Column(nullable=true)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public char getTipo() {
		return tipo;
	}

	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getIdHistoricoProduto() {
		return idHistoricoProduto;
	}

	public void setIdHistoricoProduto(Integer idHistoricoProduto) {
		this.idHistoricoProduto = idHistoricoProduto;
	}

	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	@ManyToOne 
	@JoinColumn(name="idProduto")
	public ProdutoTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoTO produto) {
		this.produto = produto;
	}
	
	@Transient 
	public String getQtdEstoqueFormatado() {
		if(estoque != null && estoque.doubleValue() >= 0){
			return CurrencyUtil.getNumberFormatStringQuatroCasas(estoque+""); 
		}		
		return "";
	}
	
	
	@Transient
	public String getQuantidadeFormatado() {
		if(quantidade != null && quantidade.doubleValue() >= 0){
			return CurrencyUtil.getNumberFormatStringQuatroCasas((quantidade+"")); 
		}		
		return "";
	}
	
	@Transient 
	public String getTipoFormatado() {
		return (tipo == Constantes.TIPO_ENTRADA_PRODUTO) ? "Entrada" : "Saída";
	}
	
	@Transient 
	public String getDataFormatada() {
		return Formatador.converterDataString(data, Formatador.FORMATO_DATA_PADRAO);
	}
	
	@PrePersist
	@PreUpdate	
	public void setEstoqueProduto(){
		if (produto != null && produto.getQtdEstoque() != null){
			setEstoque(new BigDecimal(produto.getQtdEstoque()));
		}
	}	
}
