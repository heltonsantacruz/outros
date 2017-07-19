package br.com.granit.apresentacao.produto;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import br.com.granit.persistencia.to.FornecedorTO;
import br.com.granit.persistencia.to.ProdutoTO;

public class ProdutoForm extends ValidatorForm {

	private static final long serialVersionUID = 2075869271385721922L;

	private String idProduto;
	private String descricao;
	private String quantidadeEstoque;
	
	private String tipoEntradaSaida;
	
	private String quantidadeEntradaSaida;
	
	private String fornecedor;
	private String tipo;
	private String subTipo;
	private String tipoDescricao;
	private String subTipoDescricao;
	
	private List listaFornecedores = new ArrayList<FornecedorTO>();
	private String tipoMateriaPrima;
	
	private String mes;
	private String ano;
	
	private String dataInicio;
	private String dataFim;
	
	
	
	public String getDataInicio() {
		return dataInicio;
	}



	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}



	public String getDataFim() {
		return dataFim;
	}



	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}



	/**
	 * 
	 */
	public ProdutoForm() {
		super();
	}
	
	

	public String getTipoMateriaPrima() {
		return tipoMateriaPrima;
	}



	public void setTipoMateriaPrima(String tipoMateriaPrima) {
		this.tipoMateriaPrima = tipoMateriaPrima;
	}



	public String getTipoDescricao() {
		return tipoDescricao;
	}



	public void setTipoDescricao(String tipoDescricao) {
		this.tipoDescricao = tipoDescricao;
	}



	public String getSubTipoDescricao() {
		return subTipoDescricao;
	}



	public void setSubTipoDescricao(String subTipoDescricao) {
		this.subTipoDescricao = subTipoDescricao;
	}



	/**
	 * 
	 * @param codigo
	 * @param nome
	 * @param descricao
	 * @param quantidadeEstoque
	 * @param precoVenda
	 * @param precoCusto
	 */
	public ProdutoForm(String idProduto, String descricao, String quantidadeEstoque) {
		super();
		this.idProduto = idProduto;
		this.descricao = descricao;
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(String quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public void reset() {
		idProduto = "";
		descricao= "";
		quantidadeEstoque= "";
		tipo = "";
		subTipo = "";
	}

	public String getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(String idProduto) {
		this.idProduto = idProduto;
	}

	public String getTipoEntradaSaida() {
		return tipoEntradaSaida;
	}

	public void setTipoEntradaSaida(String tipoEntradaSaida) {
		this.tipoEntradaSaida = tipoEntradaSaida;
	}

	public String getQuantidadeEntradaSaida() {
		return quantidadeEntradaSaida;
	}

	public void setQuantidadeEntradaSaida(String quantidadeEntradaSaida) {
		this.quantidadeEntradaSaida = quantidadeEntradaSaida;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List getListaFornecedores() {
		return listaFornecedores;
	}

	public void setListaFornecedores(List listaFornecedores) {
		this.listaFornecedores = listaFornecedores;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(String subTipo) {
		this.subTipo = subTipo;
	}



	public String getMes() {
		return mes;
	}



	public void setMes(String mes) {
		this.mes = mes;
	}



	public String getAno() {
		return ano;
	}



	public void setAno(String ano) {
		this.ano = ano;
	}
	
	
}
