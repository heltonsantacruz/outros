package br.com.granit.apresentacao.cliente;

import org.apache.struts.validator.ValidatorForm;

public class ClienteForm extends ValidatorForm {

	private static final long serialVersionUID = 2075869271385721922L;

	private String codigo;
	private String tipoPessoa;
	private String nome;
	private String bairro;
	private String endereco;
	private String uf;
	private String cep;
	private String cidade;
	private String telefoneResidencial;
	private String celular;
	private String fax;
	private String rg;
	private String cpf;
	private String cnpj;
	private String email;
	private String comentarios;
	private boolean erro;
	private String paginaRetorno;
	

	public String getTipoPessoaFormatado(){
		if(tipoPessoa != null){
			if(tipoPessoa.equals("F")){
				return "Física";
			}
			else if(tipoPessoa.equals("J")){
				return "Jurídica";
			}
		}
		
		return "";
	}
	
	
	
	
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	//Para o Detalhar
	private String nomeCidade;
	private String nomeEstado;
		
	/**
	 * @return the erro
	 */
	public boolean isErro() {
		return erro;
	}

	/**
	 * @param erro the erro to set
	 */
	public void setErro(boolean erro) {
		this.erro = erro;
	}

	/**
	 * 
	 */
	public ClienteForm() {
		super();
	}
	
	/**
	 * @param codigo
	 * @param nome
	 * @param logradouro
	 * @param uf
	 * @param cep
	 * @param cidade
	 * @param telefoneResidencial
	 * @param celular
	 * @param fax
	 * @param rg
	 * @param cpfcnpj
	 * @param email
	 * @param comentarios
	 */
	
	public ClienteForm(String codigo, String nome, String endereco, String uf, String cep, 
			String cidade, String telefoneResidencial, String celular, String fax, String rg, 
			String cpfcnpj, String email, String comentarios, String tipoPessoa) {
		super();
		this.codigo = codigo;
		this.tipoPessoa = tipoPessoa;
		this.nome = nome;
		this.endereco = endereco;
		this.uf = uf;
		this.cep = cep;
		this.cidade = cidade;
		this.telefoneResidencial = telefoneResidencial;
		this.celular = celular;
		this.fax = fax;
		this.rg = rg;
		if(tipoPessoa.equals(ClienteAction.FISICA)){
			this.cpf = cnpj;
		}
		else{
			this.cnpj = cpfcnpj;
		}		
		this.email = email;
		this.comentarios = comentarios;
	}

	/**
	 * @return the celular
	 */
	public String getCelular() {
		return celular;
	}

	/**
	 * @param celular the celular to set
	 */
	public void setCelular(String celular) {
		this.celular = celular;
	}

	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return cidade;
	}

	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the comentarios
	 */
	public String getComentarios() {
		return comentarios;
	}

	/**
	 * @param comentarios the comentarios to set
	 */
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
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
	 * @return the telefoneResidencial
	 */
	public String getTelefoneResidencial() {
		return telefoneResidencial;
	}

	/**
	 * @param telefoneResidencial the telefoneResidencial to set
	 */
	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}

	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;		
	}	
	
	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the nomeCidade
	 */
	public String getNomeCidade() {
		return nomeCidade;
	}

	/**
	 * @param nomeCidade the nomeCidade to set
	 */
	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	/**
	 * @return the nomeEstado
	 */
	public String getNomeEstado() {
		return nomeEstado;
	}

	/**
	 * @param nomeEstado the nomeEstado to set
	 */
	public void setNomeEstado(String nomeEstado) {
		this.nomeEstado = nomeEstado;
	}

	/**
	 * @return the paginaRetorno
	 */
	public String getPaginaRetorno() {
		return paginaRetorno;
	}

	/**
	 * @param paginaRetorno the paginaRetorno to set
	 */
	public void setPaginaRetorno(String paginaRetorno) {
		this.paginaRetorno = paginaRetorno;
	}

	

}
