package br.com.granit.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ResultadoPaginacao<T> {

	private int paginaAtual;
	
	private int totalRegistros;
	
	private int totalPaginas;
	
	private List<T> result;
	
	private Map<String, BigDecimal> totalizadores;
	
	private Map<String, BigDecimal> totalizadores2;
	
	private Map<String, BigDecimal> totalizadores3;
	
	public Map<String, BigDecimal> getTotalizadores3() {
		return totalizadores3;
	}

	public void setTotalizadores3(Map<String, BigDecimal> totalizadores3) {
		this.totalizadores3 = totalizadores3;
	}

	public Map<String, BigDecimal> getTotalizadores2() {
		return totalizadores2;
	}

	public void setTotalizadores2(Map<String, BigDecimal> totalizadores2) {
		this.totalizadores2 = totalizadores2;
	}

	private Map<String, Object> parametrosDaConsulta;

	public int getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public int getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public Map<String, BigDecimal> getTotalizadores() {
		return totalizadores;
	}

	public void setTotalizadores(Map<String, BigDecimal> totalizadores) {
		this.totalizadores = totalizadores;
	}

	public Map<String, Object> getParametrosDaConsulta() {
		return parametrosDaConsulta;
	}

	public void setParametrosDaConsulta(Map<String, Object> parametrosDaConsulta) {
		this.parametrosDaConsulta = parametrosDaConsulta;
	}		
}
