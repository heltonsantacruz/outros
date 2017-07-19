package br.com.granit.apresentacao.fornecedor;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import br.com.granit.persistencia.to.ProdutoTO;

public class FornecedorForm extends ValidatorForm {

	private String codigo;
	private String nome;
	private String cnpj;
	private String uf;
	private String cidade;
	private String cep;
	private String endereco;
	private String bairro;
	private String telefoneComercial;
	private String celular;
	private String fax;
	private String email;
	private String produto;
	private String contatoFornecedor;
	private boolean erro;
	private String paginaRetorno;
	private List listaProdutos = new ArrayList<ProdutoTO>();
	private String nomeCidade;
	private String nomeEstado;

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public String getNomeEstado() {
		return nomeEstado;
	}

	public void setNomeEstado(String nomeEstado) {
		this.nomeEstado = nomeEstado;
	}

	public List getListaProdutos() {
		return listaProdutos;
	}

	public void setListaProdutos(List listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public void addProduto(ProdutoTO produto) {
		this.listaProdutos.add(produto);
	}

	public void removeProduto(ProdutoTO produto) {
		this.listaProdutos.remove(produto);
	}

	public String getPaginaRetorno() {
		return paginaRetorno;
	}

	public void setPaginaRetorno(String paginaRetorno) {
		this.paginaRetorno = paginaRetorno;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public boolean isErro() {
		return erro;
	}

	public void setErro(boolean erro) {
		this.erro = erro;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getTelefoneComercial() {
		return telefoneComercial;
	}

	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getContatoFornecedor() {
		return contatoFornecedor;
	}

	public void setContatoFornecedor(String contatoFornecedor) {
		this.contatoFornecedor = contatoFornecedor;
	}
}
