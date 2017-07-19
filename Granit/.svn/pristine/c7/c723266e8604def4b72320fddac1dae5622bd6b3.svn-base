package br.com.granit.apresentacao;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import br.com.granit.persistencia.to.FuncionalidadeTO;
import br.com.granit.persistencia.to.PerfilTO;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.Formatador;
import br.com.granit.util.ResultadoPaginacao;
import br.com.granit.util.StringUtil;

public abstract class PrincipalAction extends DispatchAction{
	
	protected static final String REGISTROS = "registros";
	protected static final String LIST_SIZE = "listSize";
	
	/**
	 * Adicionar um erro no request a partir da chave <code>msg</code>. 
	 * @param request
	 * @param msg
	 */
	protected void adicionaErro(HttpServletRequest request, String msg){
		ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(msg));
		addErrors(request, errors);	
	}
	
	/**
	 * Adicionar um erro no request a partir da chave <code>msg</code> e
	 * o argumento <code>arg</code>. 
	 * @param request
	 * @param msg
	 */
	protected void adicionaErro(HttpServletRequest request, String msg, String arg){
		ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(msg,arg));
		addErrors(request, errors);	
	}
	
	protected String geraStringCamposObrigatorios(String... campos){
		String obrigatorios = "";
		for (int i = 0; i < campos.length; i++) {
			obrigatorios += " "+campos[i];
			if(i < campos.length -1){
				obrigatorios += ",";
			}
		}		
		return obrigatorios;
	}
	
	protected String geraStringCamposObrigatorios(List<String> campos){
		String obrigatorios = "";
		int i = 0;
		for (String campo : campos) {
			obrigatorios += " "+campo;
			if(i++ < campos.size() -1){
				obrigatorios += ",";
			}			
		}		
		return obrigatorios;
	}
	
	/**
	 * Adicionar um erro no request a partir da chave <code>msg</code> e
	 * o argumento <code>args</code>. 
	 * @param request
	 * @param msg
	 */
	protected void adicionaErro(HttpServletRequest request, String msg, Object[] args){
		ActionErrors errors = new ActionErrors();
		errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(msg,args));
		addErrors(request, errors);				
	}
	
	
	/**
	 * Adicionar uma mensagem no request a partir da chave <code>msg</code> e
	 * o argumento <code>arg</code>. 
	 * @param request
	 * @param msg
	 */
	protected void adicionaMensagem(HttpServletRequest request, String msg){
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(msg));
		addMessages(request, messages);
	}
	
	/**
	 * Adicionar uma mensagem no request a partir da chave <code>msg</code> e
	 * o argumento <code>arg</code>. 
	 * @param request
	 * @param msg
	 */
	protected void adicionaMensagem(HttpServletRequest request, String msg, String arg){
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(msg,arg));
		addMessages(request, messages);
	}
	
	/**
	 * Adicionar uma mensagem no request a partir da chave <code>msg</code> e
	 * o argumento <code>arg</code>. 
	 * @param request
	 * @param msg
	 */
	protected void adicionaMensagem(HttpServletRequest request, String msg, Object[] args){
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(msg,args));
		addMessages(request, messages);
	}

	/**
	 * Limpa todos campos do form.
	 * 
	 * @param form
	 *            Form
	 *             Caso ocorra qualquer erro ao limpar os campos.
	 */
	@SuppressWarnings("unchecked")
	public void cleanForm(ActionForm form){
		Map atts;
		try {
			atts = BeanUtils.describe(form);
			Map resp = new LinkedHashMap();
			Iterator it = atts.keySet().iterator();
			while (it.hasNext()) {
				Object key = it.next();
				resp.put(key, (Object) null);
			}
			BeanUtils.populate(form, resp);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	protected String formataDouble(String quantidadeItem) {
		if (quantidadeItem == null){
			return "";
		}
		String retorno = quantidadeItem;
		retorno = retorno.replaceAll("\\.", "");
		retorno = retorno.replace(',', '.');
		return retorno;
		
	}
	
	protected double arredondaDouble(double valor){
		return ( Math.round( (valor * 100.0) ) ) / 100.0;
	}
	
	public ActionForward dispatchMethod(ActionMapping mapping, ActionForm form,  HttpServletRequest request,
			HttpServletResponse response, String param) throws Exception{
		HttpSession session = request.getSession(false);

		if (session == null){
			adicionaErro(request, Constantes.SESSAO_EXPIROU);
			return mapping.findForward("paginaLogon");
		}		
		if (!"logonForm".equals(mapping.getName())){//Não valida se for a ação de logon.
			
			UsuarioTO usuario = (UsuarioTO)request.getSession().getAttribute(Constantes.USUARIO_LOGADO);
			if (request.getSession().getAttribute(Constantes.USUARIO_LOGADO) == null){
				return mapping.findForward("paginaLogon");
			}	
			String actionPath = mapping.getPath();
			if(!validaPerfilFuncionalidade(usuario,actionPath,request.getParameter("operacao"))){
				adicionaErro(request, Constantes.ERRO_OPERACAO_NAO_PERMITIDA,usuario.getLogin());				
				return mapping.findForward("funcionalidadeNaoPermitidaParaPerfil");
			}					
		}
		return super.dispatchMethod(mapping,form, request, response, param);
	}
	
	private boolean validaPerfilFuncionalidade(UsuarioTO usuario,
			String actionPath, String operacao) {
		for (Iterator iterator = usuario.getPerfis().iterator(); iterator.hasNext();) {
			PerfilTO perfil = (PerfilTO)iterator.next();
			for (Iterator iterator2 = perfil.getFuncionalidades().iterator(); iterator2
					.hasNext();) {
				FuncionalidadeTO funcionalidade = (FuncionalidadeTO) iterator2.next();
				if(funcionalidade.getActionPath().equalsIgnoreCase(actionPath)){
					if(funcionalidade.getOperacao()== null || funcionalidade.getOperacao().equalsIgnoreCase(operacao)){
						return true;
					}
					
				}				
			}		
		}
		return false;
	}

	/**
	 * 
	 * Objetivo: Gerar um relatório no formato PDF  
	 * @param response Resposta da requisição que retornará o relatório
	 * @param impressao Estrutura do relatório
	 * @param nomeRelatorio Nome do arquivo pdf gerado
	 * @throws IOException 
	 */
	public void gerarSaidaPDF(HttpServletResponse response,
			JasperPrint impressao, String nomeRelatorio) throws IOException {
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

		response.setBufferSize(bytes.length + 1000);
			out = response.getOutputStream();
			out.write(bytes, 0, bytes.length);
			out.flush();
			out.close();
		
		response.setContentLength(bytes.length);
	}
	
	protected int defineParametroRegistros(HttpServletRequest request, ResultadoPaginacao<?> resultadoPaginacao) {
		int pageNumberInt = resultadoPaginacao.getPaginaAtual();
		int recordNumber = 1;//Valor Inicial
		recordNumber = (pageNumberInt - 1) * getPageSize(); // recuperando o valor do primeiro registro da página atual.
		recordNumber++;		
		int lastPageRecordNumber = (recordNumber + getPageSize() - 1);
		if (lastPageRecordNumber > resultadoPaginacao.getTotalRegistros()){
			lastPageRecordNumber = resultadoPaginacao.getTotalRegistros();			
		}
		request.setAttribute(REGISTROS, (recordNumber) + " - " + lastPageRecordNumber);
		return pageNumberInt;
	}
	
	

	protected int recuperarPaginaAtual(HttpServletRequest request) {
		//INICIO DO TRATAMENTO DA PAGINACAO		
		//Recuperando o nome do parametro usado pelo dysplaytag para indicar a página corrente.
		String paramPageNumber = new ParamEncoder(getTableId()).encodeParameterName(TableTagParameters.PARAMETER_PAGE);
		//Número da página, será vazio antes de o usuário selecionar alguma página.
		String pageNumber = request.getParameter(paramPageNumber);
		int pageNumberInt = 1; //Valor Inicial
		if (!StringUtil.isBlankOrNull(pageNumber)){
			pageNumberInt = Integer.parseInt(pageNumber);//recuperando o número da página atual.
		}
		return pageNumberInt;
	}	
	
	// código definido para a tabela na página JSP
	protected abstract String getTableId();

	
	protected void inserirTotalizadorNoRequest(HttpServletRequest request,
			ResultadoPaginacao<?> resultadoPaginacao, String nomeTotalizador, String nomeParametroRequest) {
		String totalizador = "-";
		if (resultadoPaginacao.getTotalizadores()!= null && !resultadoPaginacao.getTotalizadores().isEmpty()){
			if (resultadoPaginacao.getTotalizadores().containsKey(nomeTotalizador)){
				if (resultadoPaginacao.getTotalizadores().get(nomeTotalizador)!=null){
					totalizador = Formatador.currency(resultadoPaginacao.getTotalizadores().get(nomeTotalizador).doubleValue());				
				}
			}			
		}
		request.setAttribute(nomeParametroRequest, totalizador);
	}
	
	protected void inserirTotalizadorNoRequestDouble(HttpServletRequest request,
			ResultadoPaginacao<?> resultadoPaginacao, String nomeTotalizador, String nomeParametroRequest) {
		String totalizador = "-";
		if (resultadoPaginacao.getTotalizadores()!= null && !resultadoPaginacao.getTotalizadores().isEmpty()){
			if (resultadoPaginacao.getTotalizadores().containsKey(nomeTotalizador)){
				if (resultadoPaginacao.getTotalizadores().get(nomeTotalizador)!=null){
					totalizador = Formatador.doubleValue(resultadoPaginacao.getTotalizadores().get(nomeTotalizador).doubleValue());				
				}
			}			
		}
		request.setAttribute(nomeParametroRequest, totalizador);
	}
	
	protected void inserirTotalizadorNoRequest(HttpServletRequest request,
			ResultadoPaginacao<?> resultadoPaginacao, String nomeTotalizador, String nomeParametroRequest, String nomeTotalizadorDesconto, 
			String nomeTotalizador2) {
		String totalizador = "-";
		double valor = 0.0;
		if (resultadoPaginacao.getTotalizadores()!= null && !resultadoPaginacao.getTotalizadores().isEmpty()){
			if (resultadoPaginacao.getTotalizadores().containsKey(nomeTotalizador)){
				if (resultadoPaginacao.getTotalizadores().get(nomeTotalizador)!=null){
					valor += resultadoPaginacao.getTotalizadores().get(nomeTotalizador).doubleValue();					
				}
			}			
		}
		if (resultadoPaginacao.getTotalizadores2()!= null && !resultadoPaginacao.getTotalizadores2().isEmpty()){
			if (resultadoPaginacao.getTotalizadores2().containsKey(nomeTotalizador)){
				if (resultadoPaginacao.getTotalizadores2().get(nomeTotalizador)!=null){
					valor += resultadoPaginacao.getTotalizadores2().get(nomeTotalizador).doubleValue();					
				}				
			}			
		}
		if (resultadoPaginacao.getTotalizadores3()!= null && !resultadoPaginacao.getTotalizadores3().isEmpty()){			
			if (nomeTotalizadorDesconto != null && resultadoPaginacao.getTotalizadores3().get(nomeTotalizadorDesconto)!=null){
				double desconto = resultadoPaginacao.getTotalizadores3().get(nomeTotalizadorDesconto).doubleValue();
				valor = valor - desconto;
			}
			if (nomeTotalizador2!=null && !nomeTotalizador2.isEmpty()){
				if (nomeTotalizador2 != null && resultadoPaginacao.getTotalizadores3().get(nomeTotalizador2)!=null){
					double valor2 = resultadoPaginacao.getTotalizadores3().get(nomeTotalizador2).doubleValue();
					valor = valor + valor2;
				}
			}
		}
		totalizador = Formatador.currency(valor);
		request.setAttribute(nomeParametroRequest, totalizador);
	}	
	
	
	public int getPageSize(){
		return 100;
	}
	
	protected UsuarioTO recuperaUsuarioLogado(HttpServletRequest request){
		return (UsuarioTO) request.getSession().getAttribute(Constantes.USUARIO_LOGADO);
	}
	
}
