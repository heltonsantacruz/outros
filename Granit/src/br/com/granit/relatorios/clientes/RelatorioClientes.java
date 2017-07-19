package br.com.granit.relatorios.clientes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.ActionServlet;

import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.Formatador;


public class RelatorioClientes {

	private static final String PATH_RELATORIO_CLIENTES = "WEB-INF\\relatorios\\clientes.jasper";

	public RelatorioClientes() {
	}

	public JasperPrint getEstruturaRelatorio(List<ClienteTO> dados, ActionServlet servlet) {
		JasperPrint impressao = null;
		DataSourceClientes ds = new DataSourceClientes(dados);
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put(Constantes.PARAM_PADROES_HORA_ATUAL, Formatador.getHoraAtual());
			try {
				impressao = JasperFillManager.fillReport(Formatador.converteEmCaminhoReal(
						PATH_RELATORIO_CLIENTES, servlet), parametros, ds);
			} catch (JRException jre) {
				jre.printStackTrace();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return impressao;
		
	}
}
