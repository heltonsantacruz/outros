package br.com.granit.persistencia.to;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="usuario")
public class UsuarioTO {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idUsuario;
	
	@Column(unique=true, nullable=false)
	private String login;
	
	@Column(unique=true, nullable=false)
	private String nome;
	
	
	@Column(nullable=false)
	private String senha;
	
	

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="usuario_perfil", 
	joinColumns=@JoinColumn(name="usuarios_idUsuario"),
	inverseJoinColumns=@JoinColumn(name="perfis_idPerfil"))
	private List<PerfilTO> perfis;
	
	public String getPerfilFormatado(){
		if(perfis != null && !perfis.isEmpty()){
			return ((PerfilTO)perfis.get(0)).getNome();
		}
		return "";
	}

	/**
	 * @return the idUsuario
	 */
	
	public Integer getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the login
	 */
	
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the senha
	 */
	
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

		
	
	public List<PerfilTO> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilTO> perfis) {
		this.perfis = perfis;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
