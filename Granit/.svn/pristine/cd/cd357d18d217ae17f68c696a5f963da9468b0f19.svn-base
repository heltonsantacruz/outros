package br.com.granit.apresentacao.cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.brazilutils.br.cpfcnpj.CpfCnpj;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.fachada.Fachada;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.persistencia.to.EstadoTO;
import br.com.granit.persistencia.to.MunicipioTO;
import br.com.granit.relatorios.GeradorRelatorio;
import br.com.granit.relatorios.clientes.RelatorioClientes;
import br.com.granit.util.Constantes;
import br.com.granit.util.ResultadoPaginacao;
import br.com.granit.util.StringUtil;

public class ClienteAction extends PrincipalAction {
		
	private static final String TIPO = "Tipo";
	private static final String CLASSIFICACAO = "classificacao";
	private static final String PAGINA_RETORNO = "paginaRetorno";
	private static final int TAMANHO_CPF = 14; 
	private static final String RG = "rg";
	private static final String ID_PESSOA = "idPessoa";
	private static final String CNPJ = "cnpj";
	private static final String CPF = "cpf";
	private static final String NOME = "Nome";
	private static final String SIGLA = "sigla";
	protected static final String JURIDICA = "JURIDICA";
	protected static final String FISICA = "FISICA";
	
	public ActionForward detalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		String idCliente = request.getParameter(ID_PESSOA);		
		if (idCliente == null || idCliente.isEmpty()){
			idCliente = request.getAttribute(ID_PESSOA) != null ? 
				request.getAttribute(ID_PESSOA).toString() :
				myForm.getCodigo(); 
		}
		ClienteTO cliente = Fachada.getInstance().getCliente(new Integer(idCliente));
		if (cliente == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		String paginaRetorno = myForm.getPaginaRetorno();
		if ( !GenericValidator.isBlankOrNull(request.getParameter(PAGINA_RETORNO))){
			paginaRetorno = request.getParameter(PAGINA_RETORNO);
		}
		/*Limpa o Form, mantendo apenas a página de retorno ou definindo uma nova se tiver
		sido passada como parâmetro na requisição.*/
		cleanForm(myForm);
		myForm.setPaginaRetorno(paginaRetorno);
		povoaForm(cliente, myForm);		
		return mapping.findForward(Constantes.FORWARD_DETALHAR);
	}
	
	public ActionForward processarExcluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		String idCliente = request.getParameter(ID_PESSOA);
		if (idCliente == null || idCliente.isEmpty()){
			idCliente = myForm.getCodigo(); 
		}
		ClienteTO cliente = Fachada.getInstance().getCliente(new Integer(idCliente));
		if (cliente == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		//RN04 - TODO validar se não existe nenhuma venda realizada para o cliente.
		//Só poderá ser feita após o caso de uso de vendas
		try {
			Fachada.getInstance().removeCliente(cliente);
		} catch (JPADeleteException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);			
		}
		
		myForm.reset(mapping, request);				
		request.getSession().setAttribute(Constantes.LISTA_ESTADOS, this.getEstados());
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		adicionaMensagem(request, Constantes.EXCLUSAO_SUCESSO);
		return abrirConsulta(mapping, form, request, response);
	}
	
	public ActionForward abrirAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		String idCliente = request.getParameter(ID_PESSOA);
		if (idCliente == null || idCliente.isEmpty()){
			idCliente = myForm.getCodigo(); 
		}
		ClienteTO cliente = Fachada.getInstance().getCliente(new Integer(idCliente));
		if (cliente == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		cleanForm(myForm);
		povoaForm(cliente, myForm);
		if(cliente.getTipo() == 'F'){
			myForm.setTipoPessoa(FISICA);
		}
		else{
			myForm.setTipoPessoa(JURIDICA);
		}
		
		request.setAttribute("clienteForm", myForm);
		request.getSession().setAttribute(Constantes.LISTA_ESTADOS, this.getEstados());
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	public ActionForward processarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		if (!valida(myForm, mapping, request)){
			myForm.setErro(true);
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}
		ClienteTO cliente = Fachada.getInstance().getCliente(new Integer(myForm.getCodigo()));			
		copiaPropriedades(myForm, cliente);		
		if(existeCpfCnpjRg(myForm,request,cliente)){
			myForm.setErro(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}	
		if(existeNome(cliente.getNome(), request, cliente)){
			myForm.setErro(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		try {
			Fachada.getInstance().alterarCliente(cliente);
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			mapping.findForward(Constantes.FORWARD_ALTERAR);
		}
		adicionaMensagem(request, Constantes.ALTERACAO_SUCESSO);
		return detalhar(mapping, form, request, response);
	}
	
	private void povoaForm(ClienteTO cliente, ClienteForm myForm) {
		myForm.setCodigo(cliente.getIdPessoa() + "");
		myForm.setTipoPessoa(String.valueOf(cliente.getTipo()));
		myForm.setBairro(cliente.getBairro());
		myForm.setCelular(cliente.getTelefoneCelular());
		myForm.setCep(cliente.getCep());
		if (cliente.getMunicipio() != null){
			myForm.setCidade(cliente.getMunicipio().getIdMunicipio() + "");
			myForm.setUf(cliente.getMunicipio().getEstado().getSigla());
			myForm.setNomeCidade(cliente.getMunicipio().getNome());
			myForm.setNomeEstado(cliente.getMunicipio().getEstado().getNome());
		}
		myForm.setEmail(cliente.getEmail());
		if (!GenericValidator.isBlankOrNull(cliente.getCpf()))
			myForm.setCpf(StringUtil.colocaMascaraCpf(cliente.getCpf()));
		if (!GenericValidator.isBlankOrNull(cliente.getCnpj()))
			myForm.setCnpj(StringUtil.colocaMascaraCnpj(cliente.getCnpj()));
		myForm.setFax(cliente.getTelefoneFax());
		myForm.setEndereco(cliente.getEndereco());
		myForm.setNome(cliente.getNome());
		myForm.setRg(cliente.getRg());
		myForm.setTelefoneResidencial(cliente.getTelefoneFixo());
	
	}

	public ActionForward abrir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;		
		if (!myForm.isErro()){ 
			cleanForm(myForm);
			myForm.setTipoPessoa(FISICA);
			request.setAttribute("clienteForm", myForm);
			request.getSession().setAttribute(Constantes.LISTA_ESTADOS, this.getEstados());
			request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, new ArrayList<MunicipioTO>());
		}
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}
	
	public ActionForward carregarMunicipios(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		myForm.setCidade("");
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}
	
	public ActionForward carregarMunicipiosAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		myForm.setCidade("");
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	private List<EstadoTO> getEstados() {
		return Fachada.getInstance().consultarEstado();
	}
	
	private List<MunicipioTO> getMunicipios(String sigla){		
		if (GenericValidator.isBlankOrNull(sigla))
			return new ArrayList<MunicipioTO>();
	
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put(SIGLA, sigla);
		return Fachada.getInstance().consultarMuncipioPorEstado(parametros);
	}

	public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		
		if (!valida(myForm, mapping, request)){
			myForm.setErro(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}		
		if(existeCpfCnpjRg(myForm,request,null)){
			myForm.setErro(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}		
		if(existeNome(myForm.getNome(),request,null)){
			myForm.setErro(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		ClienteTO cliente = new ClienteTO(); 
		copiaPropriedades(myForm, cliente);
		try {
			cliente = Fachada.getInstance().salvaCliente(cliente);
		} catch (JPAInsertException e) {
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		request.setAttribute(ID_PESSOA, cliente.getIdPessoa());
		adicionaMensagem(request, Constantes.MSG_CADASDTRO_SUCESSO);
		myForm.setPaginaRetorno(Constantes.FORWARD_PAGINA_INICIAL);
		return detalhar(mapping, form, request, response);
	}
	
	private boolean existeNome(String nome,HttpServletRequest request, ClienteTO cliente) {
		Map<String,Object> parametros = new HashMap<String, Object>();
		if (!GenericValidator.isBlankOrNull(nome)){
			parametros = new HashMap<String, Object>();
			parametros.put(NOME,"%" + nome.toUpperCase() + "%");
			ResultadoPaginacao<ClienteTO> resultadoPaginacao = Fachada.getInstance().consultarClientePaginado(parametros, 1, getPageSize());
			if(resultadoPaginacao.getResult() != null && !resultadoPaginacao.getResult().isEmpty()){
				if(cliente != null){
					for (Iterator iterator = resultadoPaginacao.getResult().iterator(); iterator.hasNext();) {
						ClienteTO prx = (ClienteTO) iterator.next();
						if( (prx.getIdPessoa().intValue() != cliente.getIdPessoa().intValue()) && (prx.getNome().equalsIgnoreCase(cliente.getNome()))){
							adicionaErro(request, Constantes.MSG01, new String[]{NOME} );
							return true;
						}
					}
				}
				else{
					adicionaErro(request, Constantes.MSG01, new String[]{NOME} );
					return true;
				}
				
				
				
			}			
		}	
		return false;
	}

	private boolean existeCpfCnpjRg(ClienteForm myForm,HttpServletRequest request, ClienteTO cliente) {
		
		Map<String,Object> parametros = new HashMap<String, Object>();		
		if (!GenericValidator.isBlankOrNull(myForm.getCpf())){
			if (CpfCnpj.isValid(myForm.getCpf())){
				parametros.put(CPF,"%" + StringUtil.retiraMascaraCpf(myForm.getCpf()) + "%");
				ResultadoPaginacao<ClienteTO> resultadoPaginacao = Fachada.getInstance().consultarClientePaginado(parametros, 1, getPageSize());
				if(resultadoPaginacao.getResult() != null && !resultadoPaginacao.getResult().isEmpty()){
					if(cliente != null){
						ClienteTO prx = (ClienteTO)resultadoPaginacao.getResult().iterator().next();
						if(!prx.getIdPessoa().equals(cliente.getIdPessoa())){
							adicionaErro(request, Constantes.MSG01, new String[]{CPF} );
							return true;
						}
					}
					else{
						adicionaErro(request, Constantes.MSG01, new String[]{CPF} );
						return true;
					}				
				}
			}
			if (!GenericValidator.isBlankOrNull(myForm.getRg())){
				parametros = new HashMap<String, Object>();
				parametros.put(RG,"%" + myForm.getRg() + "%");
				ResultadoPaginacao<ClienteTO> resultadoPaginacao = Fachada.getInstance().consultarClientePaginado(parametros, 1, getPageSize());
				if(resultadoPaginacao.getResult() != null && !resultadoPaginacao.getResult().isEmpty()){
					if(cliente != null){
						ClienteTO prx = (ClienteTO)resultadoPaginacao.getResult().iterator().next();
						if(!prx.getIdPessoa().equals(cliente.getIdPessoa())){
							adicionaErro(request, Constantes.MSG01, new String[]{RG} );
							return true;
						}
					}
					else{
						adicionaErro(request, Constantes.MSG01, new String[]{RG} );
						return true;
					}					
				}
			}	
			
		}
		else if(!GenericValidator.isBlankOrNull(myForm.getCnpj())){
			if (CpfCnpj.isValid(myForm.getCnpj())){
				parametros.put(CNPJ,"%" + StringUtil.retiraMascaraCnpj(myForm.getCnpj()) + "%");
				ResultadoPaginacao<ClienteTO> resultadoPaginacao = Fachada.getInstance().consultarClientePaginado(parametros, 1, getPageSize());
				if(resultadoPaginacao.getResult() != null && !resultadoPaginacao.getResult().isEmpty()){
					if(cliente != null){
						ClienteTO prx = (ClienteTO)resultadoPaginacao.getResult().iterator().next();
						if(!prx.getIdPessoa().equals(cliente.getIdPessoa())){
							adicionaErro(request, Constantes.MSG01, new String[]{CNPJ} );
							return true;
						}
					}
					else{
						adicionaErro(request, Constantes.MSG01, new String[]{CNPJ} );
						return true;
					}					
				}
			}
		}			
		return false;				
	}

	private boolean valida(ClienteForm myForm, ActionMapping mapping,
			HttpServletRequest request) {
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if (GenericValidator.isBlankOrNull(myForm.getTipoPessoa())){
			camposObrigatorios.add(TIPO);
		}	
		/* retirar obrigatoriedade de CPF 04/04/2011 */
		/*else{
			if(myForm.getTipoPessoa().equals(FISICA)){
				if(GenericValidator.isBlankOrNull(myForm.getCpf())){
					camposObrigatorios.add("CPF");
				}
			}
			else if(myForm.getTipoPessoa().equals(JURIDICA)){
				if(GenericValidator.isBlankOrNull(myForm.getCnpj())){
					camposObrigatorios.add("CNPJ");
				}
			}		
		}*/
		if (GenericValidator.isBlankOrNull(myForm.getNome())){
			camposObrigatorios.add(NOME);
		}
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, new Object[]{geraStringCamposObrigatorios(
					camposObrigatorios)});
			return false;
		}
		if(myForm.getTipoPessoa().equals(FISICA)){
			if(!GenericValidator.isBlankOrNull(myForm.getCpf())){//Desbloquear obrigatoriedade de CPF
				if (!CpfCnpj.isValid(myForm.getCpf())){
					adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{CPF} );
					return false;
				}
			}//Desbloquear obrigatoriedade de CPF	
		}
		else if(myForm.getTipoPessoa().equals(JURIDICA)){
			if(!GenericValidator.isBlankOrNull(myForm.getCnpj())){//Desbloquear obrigatoriedade de CPF
				if (!CpfCnpj.isValid(myForm.getCnpj())){
					adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{CNPJ} );
					return false;
				}
			}	
		}	
		return true;
	}

	private void copiaPropriedades(ClienteForm myForm, ClienteTO cliente) {
		cliente.setBairro(myForm.getBairro().toUpperCase());
		cliente.setTipo(myForm.getTipoPessoa().charAt(0));
		cliente.setCep(myForm.getCep());
		cliente.setEmail(myForm.getEmail().toUpperCase());
		cliente.setEndereco(myForm.getEndereco().toUpperCase());
		if (!GenericValidator.isBlankOrNull(myForm.getCidade()))
			cliente.setMunicipio(Fachada.getInstance().getMunicipio(new Integer(myForm.getCidade())));
		cliente.setNome(myForm.getNome().toUpperCase());
		cliente.setTelefoneCelular(myForm.getCelular());
		cliente.setTelefoneFax(myForm.getFax());
		cliente.setTelefoneFixo(myForm.getTelefoneResidencial());	
		if(myForm.getTipoPessoa().equals(FISICA)){
			if (CpfCnpj.isValid(myForm.getCpf())){
				cliente.setCpf(StringUtil.retiraMascaraCpf(myForm.getCpf()));
				cliente.setRg(myForm.getRg());
				cliente.setCnpj("");
			}
		}
		else{
			if (CpfCnpj.isValid(myForm.getCnpj())){
				cliente.setCnpj(StringUtil.retiraMascaraCnpj(myForm.getCnpj()));
				cliente.setCpf("");
				cliente.setRg("");
			}
		}		
	}

	public ActionForward cancelarIncluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return (mapping.findForward(Constantes.FORWARD_PAGINA_INICIAL));		
	}
	
	public ActionForward cancelarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return abrirConsulta(mapping, form, request, response);	
	}
		
	public ActionForward abrirConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		int pageNumber = recuperarPaginaAtual(request);
		Map<String,Object> parameters = new HashMap<String, Object>();
		ResultadoPaginacao resultadoPaginacao = Fachada.getInstance().consultarClientePaginado(parameters, pageNumber, getPageSize());		
		cleanForm(form);		
		request.setAttribute(Constantes.LISTA_CLIENTES_CONSULTA, resultadoPaginacao.getResult());	
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());	
		defineParametroRegistros(request, resultadoPaginacao);
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	public ActionForward processarConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		Map<String,Object> parametros = new HashMap<String, Object>();
		

		if (!GenericValidator.isBlankOrNull(myForm.getTipoPessoa()))
			parametros.put(TIPO,"%" + new Character(myForm.getTipoPessoa().charAt(0)) + "%");
		if (!GenericValidator.isBlankOrNull(myForm.getCpf())){
			if (CpfCnpj.isValid(myForm.getCpf())){
				parametros.put(CPF,"%" + StringUtil.retiraMascaraCpf(myForm.getCpf()) + "%");
			}else{
				adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{CPF} );
			}
		}
		if (!GenericValidator.isBlankOrNull(myForm.getCnpj())){
			if (CpfCnpj.isValid(myForm.getCnpj())){
				parametros.put(CNPJ,"%" + StringUtil.retiraMascaraCnpj(myForm.getCnpj()) + "%");
			}else{
				adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{CNPJ} );
			}
		}	
		if (!GenericValidator.isBlankOrNull(myForm.getNome()))
			parametros.put(NOME,"%" + myForm.getNome() + "%");
		if (!GenericValidator.isBlankOrNull(myForm.getRg()))
			parametros.put(RG,"%" + myForm.getRg() + "%");
		if (!GenericValidator.isBlankOrNull(myForm.getTelefoneResidencial()))
			parametros.put("telefoneFixo","%" + myForm.getTelefoneResidencial() + "%");
		if (!GenericValidator.isBlankOrNull(myForm.getCelular()))
			parametros.put("telefoneCelular","%" + myForm.getCelular() + "%");
		if (!GenericValidator.isBlankOrNull(myForm.getFax()))
			parametros.put("telefoneFax","%" + myForm.getFax() + "%");
		
		int pageNumber = recuperarPaginaAtual(request);
		
		ResultadoPaginacao<ClienteTO> resultadoPaginacao = Fachada.getInstance().consultarClientePaginado(parametros, pageNumber, getPageSize());
		request.setAttribute(Constantes.LISTA_CLIENTES_CONSULTA, resultadoPaginacao.getResult());
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
				
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	
	
	
	public ActionForward voltarDetalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		ClienteForm myForm = (ClienteForm) form;
		String retorno = Constantes.FORWARD_CONSULTA_ABRIR;
		if (!GenericValidator.isBlankOrNull(myForm.getPaginaRetorno())){
			retorno = myForm.getPaginaRetorno();
		}
		return (mapping.findForward(retorno));
	}
	
	/**
	 * 
	 * Objetivo: Método responsável pela geração do relatório. 
	 * Autor:    Antonio Jaime Moreira 
	 * Data:     23/11/2007
	 * @param mapping Mapping do Action
	 * @param form Formulário do Action
	 * @param request Requisição do Action
	 * @param response Resposta do Action
	 * @return Redirecionamento configurado no descritor do Struts 
	 */
	public ActionForward gerar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response){
		
		int tipoRelatorio = 0;
		
		GeradorRelatorio gerador = new GeradorRelatorio();
		RelatorioClientes relatorio = new RelatorioClientes();
				
		try {
			List<ClienteTO> listaClientes = Fachada.getInstance().consultarClientes();
			if (!listaClientes.isEmpty()){ 
				JasperPrint impressao = relatorio.getEstruturaRelatorio(listaClientes, this.getServlet());
				gerador.gerar(response, impressao, Constantes.VAZIO, tipoRelatorio);
			}
		} catch (Exception e) {
            
        } 
		
		//return (mapping.findForward(Constantes.FORWARD_INDEX));
		return null;
		
	}
	
	public String getTableId(){
		String tableId = "cliente"; // código definido para a tabela na página JSP
		return tableId;
	}
}
