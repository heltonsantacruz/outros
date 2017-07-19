package br.com.granit.apresentacao.usuario;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.controlador.ControladorUsuario;
import br.com.granit.fachada.Fachada;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.filtro.FiltroUsuario;
import br.com.granit.persistencia.to.PerfilTO;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.Cryptex;
import br.com.granit.util.ResultadoPaginacao;

public class UsuarioAction extends PrincipalAction{
	
	private static final String ID_USUARIO = "idUsuario";
	



	public ActionForward abrir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);		
		request.getSession().setAttribute(Constantes.LISTA_PERFIS, this.getPerfis());
		return (mapping.findForward(Constantes.FORWARD_ABRIR));		
	}
	
	public ActionForward cancelarIncluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return (mapping.findForward(Constantes.FORWARD_PAGINA_INICIAL));		
	}
	
	public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		UsuarioForm formulario = (UsuarioForm) form;	
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if(!validaCamposObrigatorios(formulario,camposObrigatorios,true)){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, geraStringCamposObrigatorios(camposObrigatorios));
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}		
		else{
			if (formulario.getConfirmaSenha().length() < 6){
				adicionaErro(request, Constantes.SENHA_MINIMO_CARACTERES);
				return (mapping.findForward(Constantes.FORWARD_ABRIR));
			}	
			if (!formulario.getConfirmaSenha().equals(formulario.getSenha())){				
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Senha e confirmação de senha diferentes");
				return (mapping.findForward(Constantes.FORWARD_ABRIR));
			}	
		}		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("login", formulario.getLogin());		
		List<UsuarioTO> usuarios = ControladorUsuario.getInstance().consultar(parametros);		
		if (usuarios != null && !usuarios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Já existe o Login cadastrado");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}		
		try {
			UsuarioTO usuario = criaUsuario(formulario,true,null);			
			ControladorUsuario.getInstance().salva(usuario);
			adicionaMensagem(request, Constantes.MSG_OPERACAO_SUCESSO);
		} catch (Exception e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
		}				
		return (mapping.findForward(Constantes.FORWARD_SUCESSO));
	
	}
	
	
	public ActionForward abrirConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute(Constantes.LISTA_PERFIS, this.getPerfis());
		int pageNumber = recuperarPaginaAtual(request);
		Map<String,Object> parameters = new HashMap<String, Object>();
		ResultadoPaginacao resultadoPaginacao = Fachada.getInstance().consultarUsuarioPaginado(parameters, pageNumber, getPageSize());
		cleanForm(form);
		request.setAttribute(Constantes.LISTA_USUARIOS_CONSULTA, resultadoPaginacao.getResult());	
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	public ActionForward processarConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		UsuarioForm myForm = (UsuarioForm) form;
		FiltroUsuario filtro = new FiltroUsuario();
		if (!GenericValidator.isBlankOrNull(myForm.getLogin()))
			filtro.setLogin("%"+myForm.getLogin()+"%");			
		if (!GenericValidator.isBlankOrNull(myForm.getNome()))
			filtro.setNome("%"+myForm.getNome()+"%");	
		if (!GenericValidator.isBlankOrNull(myForm.getPerfil()))
			filtro.setPerfil(myForm.getPerfil());		
		filtro.setPerfil(myForm.getPerfil());
		List<UsuarioTO> listaUsuarios = Fachada.getInstance().consultarUsuario(filtro);
		
	
		request.setAttribute(Constantes.LISTA_USUARIOS_CONSULTA, listaUsuarios);
		request.setAttribute(LIST_SIZE, 10);		
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	public ActionForward voltarDetalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		UsuarioForm myForm = (UsuarioForm) form;		
		cleanForm(myForm);
		return abrirConsulta(mapping, myForm, request, response);
	}
	
	
	public ActionForward processarExcluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		String idUsuario = request.getParameter(ID_USUARIO);		
		UsuarioTO usuario = Fachada.getInstance().getUsuario(new Integer(idUsuario));
		if (usuario == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		try {
			Fachada.getInstance().removeUsuario(usuario);
			adicionaMensagem(request, Constantes.EXCLUSAO_SUCESSO);
		} catch (JPADeleteException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);			
		}
		return abrirConsulta(mapping, form, request, response);
	}
	
	public ActionForward abrirAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		String idUsuario = request.getParameter(ID_USUARIO);		
		UsuarioTO usuario = Fachada.getInstance().getUsuario(new Integer(idUsuario));
		if (usuario == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}		
		UsuarioForm myForm = new UsuarioForm();
		povoaForm(usuario, myForm);		
		request.getSession().setAttribute(Constantes.LISTA_PERFIS, this.getPerfis());
		request.setAttribute("usuarioForm", myForm);
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	public ActionForward cancelarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return abrirConsulta(mapping, form, request, response);	
	}
	
	public ActionForward processarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		UsuarioForm formulario = (UsuarioForm) form;
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if(!validaCamposObrigatorios(formulario,camposObrigatorios,false)){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, geraStringCamposObrigatorios(camposObrigatorios));
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}		
		UsuarioTO usuario = Fachada.getInstance().getUsuario(new Integer(formulario.getIdUsuario()));
			
		try {
			UsuarioTO usuarioAlterado = criaUsuario(formulario,false,usuario);	
			Fachada.getInstance().alterarUsuario(usuarioAlterado);
			adicionaMensagem(request, Constantes.ALTERACAO_SUCESSO);
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			mapping.findForward(Constantes.FORWARD_ALTERAR);
		} catch (Exception e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
		}
		
		return detalhar(mapping, form, request, response);
	}
	
	public ActionForward detalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		UsuarioForm myForm = (UsuarioForm) form;
		String idUsuario = request.getParameter(ID_USUARIO);		
		if (idUsuario == null || idUsuario.isEmpty()){
			idUsuario = request.getAttribute(idUsuario) != null ? 
				request.getAttribute(idUsuario).toString() :
				myForm.getIdUsuario().toString(); 
		}
		UsuarioTO usuario = Fachada.getInstance().getUsuario(new Integer(idUsuario));
		if (usuario == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}			
		
		cleanForm(myForm);		
		povoaForm(usuario, myForm);			
		return mapping.findForward(Constantes.FORWARD_DETALHAR);
	}

	private void povoaForm(UsuarioTO usuario, UsuarioForm myForm) {
		myForm.setNome(usuario.getNome());
		myForm.setLogin(usuario.getLogin());
		myForm.setPerfil(usuario.getPerfis().get(0).getIdPerfil().toString());
		myForm.setIdUsuario(usuario.getIdUsuario());
		myForm.setPerfilFormatado(usuario.getPerfilFormatado());
		
		
	}

	private boolean validaCamposObrigatorios(UsuarioForm formulario,
			ArrayList<String> camposObrigatorios, boolean comSenha) {
		if (GenericValidator.isBlankOrNull(formulario.getLogin())){
			camposObrigatorios.add("Login");
		}
		if (GenericValidator.isBlankOrNull(formulario.getNome())){
			camposObrigatorios.add("Nome");
		}
		if (GenericValidator.isBlankOrNull(formulario.getPerfil())){
			camposObrigatorios.add("Perfil");
		}
		if(comSenha){
			if (GenericValidator.isBlankOrNull(formulario.getSenha())){
				camposObrigatorios.add("Senha");
			}
			if (GenericValidator.isBlankOrNull(formulario.getConfirmaSenha())){
				camposObrigatorios.add("Confirmação de Senha");
			}		
		}		
		if(camposObrigatorios == null || camposObrigatorios.isEmpty()){
			return true;
		}
		return false;
	}

	private UsuarioTO criaUsuario(UsuarioForm formulario, boolean comSenha, UsuarioTO usuarioBase) throws InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		UsuarioTO usuario = new UsuarioTO();
		if(formulario.getIdUsuario() != null && formulario.getIdUsuario() > 0){
			usuario.setIdUsuario(formulario.getIdUsuario());
		}
		usuario.setLogin(formulario.getLogin());
		usuario.setNome(formulario.getNome());
		if(comSenha){
			usuario.setSenha(Cryptex.encrypt(formulario.getSenha()));
		}
		else if(usuarioBase != null){
			usuario.setSenha(usuarioBase.getSenha());
		}
		PerfilTO perfil = Fachada.getInstance().getPerfil(Integer.parseInt(formulario.getPerfil()));
		List<PerfilTO> perfis = new ArrayList<PerfilTO>();
		perfis.add(perfil);
		usuario.setPerfis(perfis);
		return usuario;
	}



	private List<PerfilTO> getPerfis() {
		List<PerfilTO> perfis = Fachada.getInstance().consultarPerfis();			
		return perfis;
	}
	


	@Override
	protected String getTableId() {
		// TODO Auto-generated method stub
		return null;
	}

	


}
