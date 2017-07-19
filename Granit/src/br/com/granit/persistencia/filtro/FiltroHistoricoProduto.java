package br.com.granit.persistencia.filtro;

import java.util.Date;

public class FiltroHistoricoProduto {
	private Date dataInicio;
	private Date dataFim;
	private Integer idProduto;
	
	
	
	
	private String descricao;
	
	private Integer tipo;
	
	private Integer subTipo;
	
	
	
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public Integer getSubTipo() {
		return subTipo;
	}
	public void setSubTipo(Integer subTipo) {
		this.subTipo = subTipo;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public Integer getIdProduto() {
		return idProduto;
	}
	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
}

