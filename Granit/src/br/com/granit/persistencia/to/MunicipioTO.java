package br.com.granit.persistencia.to;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="listaMunicipios", 
			query="Select municipio From MunicipioTO municipio " +
					" Order by municipio.nome ASC"),
					
	@NamedQuery(name="listaMunicipiosPorEstado", 
			query="Select municipio From MunicipioTO municipio " +
				  "Where municipio.estado.sigla = :sigla " +
				  " Order by municipio.nome ASC ")
})
@Entity
@Table(name="municipio")
public class MunicipioTO {
	
	private Integer idMunicipio;
	private String nome;
	private EstadoTO estado;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	
	@Column(nullable=false, length=250)
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sigla", nullable=false)
	public EstadoTO getEstado() {
		return estado;
	}

	public void setEstado(EstadoTO estado) {
		this.estado = estado;
	}
}
