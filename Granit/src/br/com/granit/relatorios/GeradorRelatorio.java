package br.com.granit.relatorios;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;

/**
 * 
 * Projeto: 																				<BR>
 * Descri��o: Esta classe � respons�vel pela gera��o de relat�rios nos mais diversos formatos.	<BR>
 * Persistente: N�o																				<BR>
 * Data de cria��o: 14/11/2007 																	<BR>
 * 
 * @author ANTONIO JAIME ALMEIDA
 * @version 1.0 
 */

public class GeradorRelatorio {

	private static final int TIPO_RELATORIO_PDF = 0;
	private static final int TIPO_RELATORIO_HTML = 1;
	private static final int TIPO_RELATORIO_XLS = 2;
	private static final int TIPO_RELATORIO_DOC = 3;

	/**
	 * Objetivo: Este m�todo � respons�vel por construir a estrutura do relat�rio 
	 * 			 com unindo Datasource, aquivo jasper e par�metros
	 *  
	 * Autor:    Antonio Jaime Almeida
	 * Data:     14/11/2007
	 * @param dataSource Dados do relat�rio
	 * @param arquivoEstruturaRelatorio Arquivo ".jasper" 
	 * @param parametros Par�metros do relat�rio
	 * @return
	 */
	public JasperPrint gerarEstrutura(JRDataSource dataSource,
										String arquivoEstruturaRelatorio,		
											Map<String, Object> parametros) {
		JasperPrint impressao = null;
		try {
			try {
				impressao = JasperFillManager.fillReport(
						arquivoEstruturaRelatorio, parametros, dataSource);
			} catch (JRException jre) {
				jre.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return impressao;
	}
	
	
	/**
	 * 
	 * Objetivo: Gerar um relat�rio no formato PDF  
	 * Autor:    Antonio Jaime Moreira de Almeida
	 * Data:     14/11/2007
	 * @param response Resposta da requisi��o que retornar� o relat�rio
	 * @param impressao Estrutura do relat�rio
	 * @param nomeRelatorio Nome do arquivo pdf gerado
	 */
	public void gerarSaidaPDF(HttpServletResponse response,
			JasperPrint impressao, String nomeRelatorio) {
		response.setContentType("application/pdf");
	  	//response.setHeader("Content-disposition", "attachment;filename=" + nomeRelatorio + ".pdf");		
		byte[] bytes = null;
		try {
			bytes = JasperExportManager.exportReportToPdf(impressao);
		} catch (JRException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ServletOutputStream out;

		try {
			response.setBufferSize(bytes.length + 1000);
			out = response.getOutputStream();
			out.write(bytes, 0, bytes.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setContentLength(bytes.length);
	}

	
	/**
	 * 
	 * Objetivo: Gerar um relat�rio no formato HTML  
	 * Autor:   Antonio Jaime Moreira de Almeida
	 * Data:     14/11/2007
	 * @param response Resposta da requisi��o que retornar� o relat�rio
	 * @param impressao Estrutura do relat�rio
	 * @param nomeRelatorio Nome do arquivo HTML gerado
	 */
	public void gerarSaidaHtml(HttpServletResponse response,
			JasperPrint impressao, String nomeRelatorio) {
		response.setContentType("text/html");
		try {
			//JasperExportManager.exportReportToHtmlFile(impressao, "c:\\teste.html");
			JRHtmlExporter exporter = new JRHtmlExporter();   
			exporter.setParameter(JRExporterParameter.JASPER_PRINT,   
			                impressao);   
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,   
			                response.getOutputStream()); 
			exporter.exportReport(); 
			
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * Objetivo: Gerar um relat�rio no formato DOC  
	 * Autor:   Antonio Jaime Moreira de Almeida
	 * Data:     14/11/2007
	 * @param response Resposta da requisi��o que retornar� o relat�rio
	 * @param impressao Estrutura do relat�rio
	 * @param nomeRelatorio Nome do arquivo DOC gerado
	 */
	public void gerarSaidaDOC(HttpServletResponse response,
			JasperPrint impressao, String nomeRelatorio) {
		try {
			response.setContentType("application/msword");
			response.addHeader("Content-disposition", "attachment;filename=" + nomeRelatorio + ".doc");
		
			JRRtfExporter rtfExporter = new JRRtfExporter();
			rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, impressao);
			rtfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			rtfExporter.exportReport();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Objetivo: Gerar um relat�rio no formato XLS  
	 * Autor:    Antonio Jaime Moreira de Almeida
	 * Data:     14/11/2007
	 * @param response Resposta da requisi��o que retornar� o relat�rio
	 * @param impressao Estrutura do relat�rio
	 * @param nomeRelatorio Nome do arquivo xls gerado
	 */
	public void gerarSaidaXLS(HttpServletResponse response,
			JasperPrint impressao, String nomeRelatorio) {

		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-disposition", "attachment;filename=" + nomeRelatorio + ".xls");

		byte[] bytes = null;
		try {

			JExcelApiExporter exporter = new JExcelApiExporter();
			//		  		JRXlsExporter exporter = new JRXlsExporter();   
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			exporter.setParameter(JExcelApiExporterParameter.JASPER_PRINT,
					impressao);
			exporter.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM,
					xlsReport);
			exporter
					.setParameter(
							JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
							Boolean.TRUE);
			exporter.setParameter(
					JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.TRUE);
			exporter.setParameter(
					JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.TRUE);
			exporter.setParameter(
					JExcelApiExporterParameter.IS_DETECT_CELL_TYPE,
					Boolean.TRUE);

			exporter.exportReport();
			bytes = xlsReport.toByteArray();

			xlsReport.close();

			ServletOutputStream out = response.getOutputStream();

			out = response.getOutputStream();
			out.write(bytes, 0, bytes.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentLength(bytes.length);

	}
	
	
	/**
	 * 
	 * Objetivo: Este m�todo objetiva redirecionar para a m�todo adequado
	 * 			 a escolha de usu�rio com rela��o ao tipo de relat�rio	 
	 * Autor:    Antonio Jaime Moreira de Almeida
	 * Data:     15/11/2007
	 * @param response Resposta da requisi��o que retornar� o relat�rio
	 * @param impressao Estrutura do relat�rio
	 * @param nomeRelatorio Nome do arquivo gerado
	 * @param tipoRelatorio Tipo escolido pelo usu�rio
	 */
	public void gerar(HttpServletResponse response, JasperPrint estrutura,
			String nomeRelatorio, int tipoRelatorio) {
		
		if (desejaGerarHTML(tipoRelatorio))
			this.gerarSaidaHtml(response, estrutura, nomeRelatorio);
		else if (desejaGerarPDF(tipoRelatorio))
			this.gerarSaidaPDF(response, estrutura, nomeRelatorio);
		else if (desejaGerarXLS(tipoRelatorio))
			this.gerarSaidaXLS(response, estrutura, nomeRelatorio);
		else if (desejaGerarDOC(tipoRelatorio)) 
			this.gerarSaidaDOC(response, estrutura, nomeRelatorio);
		else this.gerarSaidaHtml(response, estrutura, nomeRelatorio);
	}

	/**
	 * Objetivo: Este m�todo verifica se o usu�rio escolheu gerar um DOC 
	 * Autor:    Antonio Jaime M. de Almeida
	 * Data:     15/11/2007
	 * @param tipoRelatorio Tipo escolhido de relat�rio 
	 * @return Se escolheu DOC ou n�o
	 */	
	private boolean desejaGerarDOC(int tipoRelatorio) {
		return TIPO_RELATORIO_DOC == tipoRelatorio;
	}

	/**
	 * Objetivo: Este m�todo verifica se o usu�rio escolheu gerar um XLS 
	 * Autor:    Antonio Jaime M. de Almeida
	 * Data:     15/11/2007
	 * @param tipoRelatorio Tipo escolhido de relat�rio 
	 * @return Se escolheu XLS ou n�o
	 */
	private boolean desejaGerarXLS(int tipoRelatorio) {
		return TIPO_RELATORIO_XLS == tipoRelatorio;
	}

	/**
	 * Objetivo: Este m�todo verifica se o usu�rio escolheu gerar um PDF 
	 * Autor:    Antonio Jaime M. de Almeida
	 * Data:     15/11/2007
	 * @param tipoRelatorio Tipo escolhido de relat�rio 
	 * @return Se escolheu PDF ou n�o
	 */
	private boolean desejaGerarPDF(int tipoRelatorio) {
		return TIPO_RELATORIO_PDF == tipoRelatorio;
	}

	/**
	 * Objetivo: Este m�todo verifica se o usu�rio escolheu gerar um HTML 
	 * Autor:    Antonio Jaime M. de Almeida
	 * Data:     15/11/2007
	 * @param tipoRelatorio Tipo escolhido de relat�rio 
	 * @return Se escolheu HTML ou n�o
	 */
	private boolean desejaGerarHTML(int tipoRelatorio) {
		return TIPO_RELATORIO_HTML == tipoRelatorio;
	}


}
