package br.com.granit.relatorios.clientes;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.util.Constantes;

public class DataSourceClientes implements JRDataSource {
	
	private boolean temProximo;
	private Iterator<ClienteTO> clientesIter;
	private ClienteTO cliente;

	public DataSourceClientes(List<ClienteTO> clientes) {
		this.temProximo = true;
		this.clientesIter = clientes.iterator();
	}

	public Object getFieldValue(JRField campo) throws JRException {
		String nome = campo.getName();
		
		Object valor = null;
		if(nome.equals(Constantes.REL_CABECALHO_COL_CODIGO)) {
			valor = cliente.getIdPessoa(); 
		}
		
		if(nome.equals(Constantes.REL_CABECALHO_COL_NOME)) {
			valor = cliente.getNome(); 
		}
		if(nome.equals(Constantes.REL_CABECALHO_COL_CNPJ_CPF)) {
			valor = cliente.getCpfcnpj(); 
		}
		return valor;
	}

	public boolean next() throws JRException {
		if (clientesIter != null) {
			cliente = clientesIter.hasNext() ? clientesIter.next() : null;
		}
		temProximo = (cliente != null);
		return temProximo;
	}
	
}