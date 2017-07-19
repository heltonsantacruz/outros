package br.com.granit.persistencia.to;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="funcionalidade")
public class FuncionalidadeTO {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idFuncionalidade;
	
	@Column(nullable=false)
	private String nome;
	
	@Column(nullable=false)
	private String actionPath;
	
	@Column(nullable=true)
	private String operacao;
	
	@ManyToMany(mappedBy="funcionalidades",cascade=CascadeType.ALL)
	private List<PerfilTO> perfis;	
	
	public List<PerfilTO> getPerfis() {
		return perfis;
	}
	public void setPerfis(List<PerfilTO> perfis) {
		this.perfis = perfis;
	}
	public Integer getIdFuncionalidade() {
		return idFuncionalidade;
	}
	public void setIdFuncionalidade(Integer idFuncionalidade) {
		this.idFuncionalidade = idFuncionalidade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getActionPath() {
		return actionPath;
	}
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	

}
