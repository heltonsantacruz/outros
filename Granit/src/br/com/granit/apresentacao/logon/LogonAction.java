package br.com.granit.apresentacao.logon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.controlador.ControladorUsuario;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.Cryptex;

public class LogonAction extends PrincipalAction {
	
	
	public ActionForward logout(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);		
		request.getSession().setAttribute(Constantes.USUARIO_LOGADO, null);	
		request.getSession().invalidate();
		return (mapping.findForward(Constantes.FORWARD_ABRIR));
	}
	
	public ActionForward abrir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);		
		request.getSession().setAttribute(Constantes.USUARIO_LOGADO, null);		
		return (mapping.findForward(Constantes.FORWARD_ABRIR));
	}
	
	public ActionForward efetuarLogon(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		LogonForm formulario = (LogonForm) form;		
		if (GenericValidator.isBlankOrNull(formulario.getLogin())){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, "Login");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}
		if (GenericValidator.isBlankOrNull(formulario.getSenha())){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, "Senha");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("login", formulario.getLogin());
		UsuarioTO usuario = null;
		List<UsuarioTO> usuarios = new ArrayList<UsuarioTO>();
		try{
			usuarios = ControladorUsuario.getInstance().consultar(parametros);
		}catch (Exception e) {
			adicionaErro(request, "banco.de.dados.expirou");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}
		String senha = null;
		if (usuarios == null || usuarios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Login ou Senha");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}else{
			usuario = usuarios.get(0);
			senha = usuario.getSenha();
		}
		try {
			if (senha==null || !senha.equals(Cryptex.encrypt(formulario.getSenha()))){
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Senha");
				return (mapping.findForward(Constantes.FORWARD_ABRIR));
			}
		} catch (Exception e) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Senha");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}
		request.getSession().setAttribute(Constantes.USUARIO_LOGADO, usuario);		
		return (mapping.findForward("logon"));
	}
	
	public ActionForward abrirAlterarSenha(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);		
		return (mapping.findForward("alterar"));
	}
	
	public ActionForward alterarSenha(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		LogonForm formulario = (LogonForm) form;
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if (GenericValidator.isBlankOrNull(formulario.getLogin())){
			camposObrigatorios.add("Login");
		}
		if (GenericValidator.isBlankOrNull(formulario.getSenhaAtual())){
			camposObrigatorios.add("Senha Atual");
		}
		if (GenericValidator.isBlankOrNull(formulario.getNovaSenha())){
			camposObrigatorios.add("Nova Senha");
		}
		if (GenericValidator.isBlankOrNull(formulario.getConfirmaNovaSenha())){
			camposObrigatorios.add("Confirma Nova Senha");
		}
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, geraStringCamposObrigatorios(camposObrigatorios));
			return (mapping.findForward("alterar"));
		}
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("login", formulario.getLogin());
		UsuarioTO usuario = null;
		List<UsuarioTO> usuarios = ControladorUsuario.getInstance().consultar(parametros);
		String senha = null;
		if (usuarios == null || usuarios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Login ou Senha");
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}else{
			usuario = usuarios.get(0);
			senha = usuario.getSenha();
		}
		try{
			if (senha == null || !formulario.getSenhaAtual().equals(Cryptex.decrypt(senha))){
				adicionaErro(request, Constantes.SENHA_ATUAL_INVALIDA);
				return (mapping.findForward("alterar"));
			}
		}catch(Exception e){
			adicionaErro(request, Constantes.SENHA_ATUAL_INVALIDA);
			return (mapping.findForward("alterar"));
		}
				
		if (!formulario.getNovaSenha().equals(formulario.getConfirmaNovaSenha())){
			adicionaErro(request, Constantes.SENHA_ATUAL_DIFERENTE_CONFIRMA_SENHA);
			return (mapping.findForward("alterar"));
		}
		
		if (formulario.getNovaSenha().length() < 6){
			adicionaErro(request, Constantes.SENHA_MINIMO_CARACTERES);
			return (mapping.findForward("alterar"));
		}		
		
		try {
			usuario.setSenha((Cryptex.encrypt(formulario.getNovaSenha())));
			ControladorUsuario.getInstance().atualiza(usuario);
		} catch (Exception e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
		}
		request.getSession().setAttribute(Constantes.USUARIO_LOGADO, null);
		adicionaMensagem(request, Constantes.MSG_OPERACAO_SUCESSO);
		return abrir(mapping, form, request, response);
	}

	@Override
	protected String getTableId() {
		// TODO Auto-generated method stub
		return null;
	}	
}
