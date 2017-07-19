package br.com.granit.apresentacao.backup;

import java.io.Serializable;
import java.util.Date;

public class BackupTO implements Serializable, Comparable<BackupTO>{
	
	private static final long serialVersionUID = -4923606710212063464L;

	private Integer idBackup;
	private String path;
	private String data;
	private String nome;
	private Date date;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Integer getIdBackup() {
		return idBackup;
	}
	public void setIdBackup(Integer idBackup) {
		this.idBackup = idBackup;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public int compareTo(BackupTO o) {
		if (this.date != null && o.date != null){
			return o.date.compareTo(this.date);
		}else if (o.date != null){
			return 1;
		}else{
			return -1;
		}
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
