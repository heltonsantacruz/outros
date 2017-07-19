/**
 * 
 */
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

/**
 * @author Helton
 *
 */
@Entity
@Table(name="perfil")
public class PerfilTO {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idPerfil;
	
	@Column(unique=true, nullable=false)
	private String nome;
	
	@Column(nullable=false)
	private String descricao;
	
	
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="usuario_perfil", 
	joinColumns=@JoinColumn(name="perfis_idPerfil"),
	inverseJoinColumns=@JoinColumn(name="usuarios_idUsuario"))
	private List<UsuarioTO> usuarios;
	
	@ManyToMany
	private List<FuncionalidadeTO> funcionalidades;
	
	
    

	public List<FuncionalidadeTO> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<FuncionalidadeTO> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public List<UsuarioTO> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<UsuarioTO> usuarios) {
		this.usuarios = usuarios;
	}

	public Integer getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


}
