package br.com.granit.persistencia.to;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.granit.util.StringUtil;

@Entity
@Table(name="cliente")
public class ClienteTO {
	private Integer idPessoa;
	private String nome;
	private String endereco;
	private String bairro;
	private String telefoneFixo;
	private String telefoneFax;
	private String telefoneCelular;
	private String email;
	private String cep;
	private String cpf;
	private String cnpj;
	private String rg;
	private String cpfcnpj;
	private char tipo;
	private MunicipioTO municipio;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	public Integer getIdPessoa() {
		return idPessoa;
	}
	
	public void setIdPessoa(Integer idPessoa) {
		this.idPessoa = idPessoa;
	}
	
	@Column(nullable=false, length=100)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(nullable=true, length=200)
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Column(nullable=true, length=100)
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	@Column(nullable=true, length=13)
	public String getTelefoneFixo() {
		return telefoneFixo;
	}
		
	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
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
	
	/**
	 * Getter
	 * @return tipo. 0-Cliente, 1-Funcionário.
	 */
	@Column(nullable=false, length=1)
	public char getTipo() {
		return tipo;
	}
	
	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idMunicipio")
	public MunicipioTO getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioTO municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {	
		return this.cpf;
	}

	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}

	/**
	 * @param rg the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}

	/**
	 * @return the cpfcnpj
	 */
	@Transient
	public String getCpfcnpj() {
		if (!StringUtil.isBlankOrNull(cpf)){
			return StringUtil.colocaMascaraCpf(cpf);
		}else if (!StringUtil.isBlankOrNull(cnpj)){
			return StringUtil.colocaMascaraCnpj(cnpj);
		}
		return cpfcnpj;
	}

	/**
	 * @param cpfcnpj the cpfcnpj to set
	 */
	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}
	
	@Transient
	public String getNomeCidade(){
		if(municipio != null){
			return municipio.getNome();
		}
		return "";
	}
	
	@Transient
	public String getNomeEstado(){
		if(municipio != null){
			return municipio.getEstado().getNome();
		}
		return "";
	}
	
	@Transient
	public String getSiglaEstado(){
		if(municipio != null){
			return municipio.getEstado().getSigla();
		}
		return "";
	}
	
	@Transient
	public int getCodCidade() {
		if(municipio != null){
			return municipio.getIdMunicipio();
		}
		return 0;
	}	
	
	@Transient
	public String getTipoPessoaFormatado(){
		if(tipo == 'F'){
			return "Física";
		}
		else if(tipo == 'J'){
			return "Jurídica";
		}	
		return "";
	}
}
