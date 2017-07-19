package br.com.granit.apresentacao.venda;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioDetalharVendaTO {
	
	private String dataVenda;
	private String nomeCliente;
	
	private Integer codigo;
	private String descricao;
	private Double quantidade;
	private Double total;
	
	private Double subTotal;
	private Double desconto;
	private Double totalVenda;
	
	private String produto;
	
	private Double preco;
	private Double metragem;
	
	public String getDataVenda() {
		return dataVenda;
	}
	public void setDataVenda(String dataVenda) {
		this.dataVenda = dataVenda;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getDesconto() {
		return desconto;
	}
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	public Double getTotalVenda() {
		return totalVenda;
	}
	public void setTotalVenda(Double totalVenda) {
		this.totalVenda = totalVenda;
	}
	
	public static void main(String[] args) {
		//testando o relatório
		RelatorioDetalharVendaTO to = new RelatorioDetalharVendaTO();
			
		to.setDataVenda("02-02-2008");
		to.setNomeCliente("nomeCliente andre gomes de sousa");
		
		to.setCodigo(1);
		to.setDescricao("descricao descricao ...");
		to.setQuantidade(10.0);
		to.setTotal(10.0);
		
		to.setTotalVenda(100.00);
		to.setSubTotal(1000.00);
		to.setDesconto(0.10);
		
		JasperPrint jprint = null;
		String pastaRelatorios =  "C:\\Sistemas\\SISTEMA VENDAS\\SistemaVendas\\relatorios\\detalhavenda.jasper";
		Map<String, Object> parametros = new HashMap<String,Object>();
		parametros.put("dataVenda", to.getDataVenda());
		parametros.put("nomeCliente", to.getNomeCliente());
		parametros.put("subTotal", to.getSubTotal());
		parametros.put("desconto", to.getDesconto());
		parametros.put("totalVenda", to.getTotalVenda());
		
		//parametros.put(CAMINHO_LOGO_ANEEL, IMAGENS_LOGO_ANEEL_JPG);
		String nomeRelatorio = "";
		try {		
			List<Object> clAttrs = new ArrayList<Object>();
			clAttrs.add(to);
			clAttrs.add(to);
			clAttrs.add(to);
			jprint = JasperFillManager.fillReport(pastaRelatorios , parametros,
					new JRBeanCollectionDataSource(clAttrs));
		} catch (JRException ex) {
			ex.printStackTrace();
		}
		
		JasperViewer jp = new JasperViewer(jprint, false);
		jp.setTitle(nomeRelatorio);
		jp.setVisible(true);



		
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Double getPreco() {
		return preco;
	}
	public void setPreco(Double preco) {
		this.preco = preco;
	}
	public Double getMetragem() {
		return metragem;
	}
	public void setMetragem(Double metragem) {
		this.metragem = metragem;
	}
}
