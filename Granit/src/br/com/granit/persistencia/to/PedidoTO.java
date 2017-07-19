package br.com.granit.persistencia.to;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.granit.util.Formatador;



@Entity
@Table(name="pedido")
public class PedidoTO {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer idPedido;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date dataPedido;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="idPedido")
	private List<ItemPedidoTO> itens;
	
	@ManyToOne 
	@JoinColumn(name="idFornecedor")
	private FornecedorTO fornecedor;
	
	private BigDecimal desconto;	
	
	private BigDecimal totalSemDesconto;
	
	private BigDecimal totalComDesconto;
	
	private Boolean finalizado;
	
	
	private String produtoString;
	
	

	@Transient
	public String getProdutoString() {
		ArrayList<String> listaProdutos = new ArrayList<String>();
		if(getItens() != null && !getItens().isEmpty()){
			for (Iterator iterator2 = getItens().iterator(); iterator2.hasNext();) {
				ItemPedidoTO item = (ItemPedidoTO) iterator2.next();
				if(!listaProdutos.contains(item.getProduto().getDescricaoTipoSubTipo())){
					listaProdutos.add(item.getProduto().getDescricaoTipoSubTipo());
				}			
			}	
		}		
		if(!listaProdutos.isEmpty()){
			return listaProdutos.toString();	
		}
		else{
			return "";
		}
	}
	
	@Transient
	public String getDataPedidoFormatada(){
		if (dataPedido == null)
			return "";
		return Formatador.converterDataString(dataPedido, Formatador.FORMATO_DATA_PADRAO);
	}
	
	@Transient
	public String getFinalizadoFormatado(){
		if(finalizado != null && finalizado.equals(Boolean.TRUE)){
			return "SIM";
		}
		return "NÃO";
	}

	public Boolean getFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}

	public Integer getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}

	public List<ItemPedidoTO> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedidoTO> itens) {
		this.itens = itens;
	}

	public FornecedorTO getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorTO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	

	public BigDecimal getTotalSemDesconto() {
		return totalSemDesconto;
	}

	public void setTotalSemDesconto(BigDecimal totalSemDesconto) {
		this.totalSemDesconto = totalSemDesconto;
	}

	public BigDecimal getTotalComDesconto() {
		return totalComDesconto;
	}
	
	
	
	public void setTotalComDesconto(BigDecimal totalComDesconto) {
		this.totalComDesconto = totalComDesconto;
	}

	@Transient
	public BigDecimal getSubTotal() {
		BigDecimal resultado = new BigDecimal(0);
		if (itens != null){
			for (Iterator iterator = itens.iterator(); iterator.hasNext();) {
				ItemPedidoTO item = (ItemPedidoTO) iterator.next();
				resultado = resultado.add(item.getPrecoTotal());
			}			
		}
		return resultado;
	}
	
	@Transient
	public String getSubTotalFormatado(){
		if (getSubTotal() == null)
			return "";
		return Formatador.currency(getSubTotal().doubleValue());
	}
	
	@Transient
	public String getTotalComDescontoFormatado() {
		if (totalComDesconto == null)
			return "";
		return Formatador.currency(totalComDesconto.doubleValue());
	}
	
	@Transient
	public String getTotalSemDescontoFormatado() {
		if (totalSemDesconto == null)
			return "";
		return Formatador.currency(totalSemDesconto.doubleValue());
	}
	
	@Transient
	public String getDescontoFormatado() {
		if (desconto == null)
			return "";
		return Formatador.currency(desconto.doubleValue());
	}
	
}
