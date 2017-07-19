package br.com.granit.persistencia.to;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Transient;

@Entity
@Table(name="fornecedor")
public class FornecedorTO implements Serializable{
	private Integer idFornecedor;
	private String nome;
	private String cnpj;
	private String endereco;
	private String bairro;
	private String email;
	private String cep;
	private MunicipioTO municipio;
	private String contatoFornecedor;
	private List<ProdutoFornecedorTO> produtos = new ArrayList<ProdutoFornecedorTO>(); 
	private String telefoneComercial;
	private String telefoneFax;
	private String telefoneCelular;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)	
	public Integer getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Integer idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idMunicipio")
	public MunicipioTO getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioTO municipio) {
		this.municipio = municipio;
	}
	
	@Column(nullable=false, length=100)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(nullable=true, length=50)
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Column(nullable=true, length=50)
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	@Column(nullable=true, length=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable=true, length=10)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@Column(nullable=true, length=100)
	public String getContatoFornecedor() {
		return contatoFornecedor;
	}

	public void setContatoFornecedor(String contatoFornecedor) {
		this.contatoFornecedor = contatoFornecedor;
	}

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="idFornecedor")
	public List<ProdutoFornecedorTO> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoFornecedorTO> produtos) {
		this.produtos = produtos;
	}
	
	public void addProdutoFornecedor(ProdutoFornecedorTO produto){
		this.produtos.add(produto);
	}

	@Column(nullable=true, length=13)
	public String getTelefoneComercial() {
		return telefoneComercial;
	}

	public void setTelefoneComercial(String telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}

	@Column(nullable=true, length=13)
	public String getTelefoneFax() {
		return telefoneFax;
	}

	public void setTelefoneFax(String telefoneFax) {
		this.telefoneFax = telefoneFax;
	}

	@Column(nullable=true, length=14)
	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	@Column(nullable=false, length=18)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	@Transient
	public String getNomeCnpj(){
		return nome + " - " + cnpj;
	}
	
	
}
