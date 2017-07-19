package br.com.granit.apresentacao.fornecedor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.brazilutils.br.cpfcnpj.CpfCnpj;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.controlador.ControladorPedido;
import br.com.granit.fachada.Fachada;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.to.EstadoTO;
import br.com.granit.persistencia.to.FornecedorTO;
import br.com.granit.persistencia.to.ItemPedidoTO;
import br.com.granit.persistencia.to.MunicipioTO;
import br.com.granit.persistencia.to.PedidoTO;
import br.com.granit.persistencia.to.ProdutoFornecedorTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.JPAUtility;
import br.com.granit.util.ResultadoPaginacao;

public class FornecedorAction extends PrincipalAction {
		
	private static final String PRODUTO = "Produto";
	private static final String CNPJ = "CNPJ";
	private static final String SIGLA = "sigla";
	private static final String NOME = "nome";
	private static final String PAGINA_RETORNO = "paginaRetorno";
	
	public ActionForward abrir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;		
		if (!myForm.isErro()){ 
			cleanForm(myForm);			
			request.setAttribute("fornecedorForm", myForm);
			request.getSession().setAttribute(Constantes.LISTA_ESTADOS, this.getEstados());
			request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, new ArrayList<MunicipioTO>());
			request.getSession().setAttribute("produtosCadastrados",getProdutosCadastrados());
			FornecedorTO fornecedor = new FornecedorTO();
			request.getSession().setAttribute("fornecedor",fornecedor);
		}
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}  
	
	private Object getProdutosCadastrados() {		
		return Fachada.getInstance().consultarProduto();
	}

	public ActionForward carregarMunicipios(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		myForm.setCidade("");
		return mapping.findForward(Constantes.FORWARD_ABRIR);
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

	
	private boolean valida(FornecedorForm myForm, ActionMapping mapping,
			HttpServletRequest request) {
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		
		if (GenericValidator.isBlankOrNull(myForm.getNome())){
			camposObrigatorios.add(NOME);
		}
		if(GenericValidator.isBlankOrNull(myForm.getCnpj())){
			camposObrigatorios.add(CNPJ);
		}
		if(myForm.getListaProdutos() == null || myForm.getListaProdutos().isEmpty()){
			camposObrigatorios.add(PRODUTO);
		}
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, new Object[]{geraStringCamposObrigatorios(
					camposObrigatorios)});
			return false;
		}
		if (!CpfCnpj.isValid(myForm.getCnpj())){
			adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{CNPJ} );
			return false;
		}		
		return true;
	}
	
	public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		myForm.setListaProdutos(fornecedor.getProdutos());
		if (!valida(myForm, mapping, request)){
			myForm.setErro(true);
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}		
		if(existeCnpjFornecedor(myForm,request,fornecedor)){
			myForm.setErro(true);
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}		
	
		copiaPropriedades(myForm, fornecedor);		
		try {
			fornecedor = Fachada.getInstance().salvaFornecedor(fornecedor);
		} catch (JPAInsertException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		
			
		
		request.setAttribute("idFornecedor", fornecedor.getIdFornecedor());			
		adicionaMensagem(request, Constantes.MSG_CADASDTRO_SUCESSO);
		myForm.setPaginaRetorno(Constantes.FORWARD_PAGINA_INICIAL);
		return detalhar(mapping, form, request, response);
		
	}
	
	public ActionForward cancelarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return abrirConsulta(mapping, form, request, response);	
	}
	
	
	public ActionForward detalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		String idFornecedor = request.getParameter("idFornecedor");		
		if (idFornecedor == null || idFornecedor.isEmpty()){
			idFornecedor = request.getAttribute("idFornecedor") != null ? 
				request.getAttribute("idFornecedor").toString() :
				myForm.getCodigo(); 
		}
		FornecedorTO fornecedor = Fachada.getInstance().getFornecedor(new Integer(idFornecedor));
		if (fornecedor == null){
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
		povoaForm(fornecedor, myForm);		
		request.setAttribute("produtosAssociados", fornecedor.getProdutos());
		return mapping.findForward(Constantes.FORWARD_DETALHAR);
	}
	
	public ActionForward voltarDetalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		String retorno = Constantes.FORWARD_CONSULTA_ABRIR;
		if (!GenericValidator.isBlankOrNull(myForm.getPaginaRetorno())){
			retorno = myForm.getPaginaRetorno();
		}
		return (mapping.findForward(retorno));
	}
	
	public ActionForward abrirConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		int pageNumber = recuperarPaginaAtual(request);
		Map<String,Object> parameters = new HashMap<String, Object>();
		ResultadoPaginacao resultadoPaginacao = Fachada.getInstance().consultarFornecedorPaginado(parameters, pageNumber, getPageSize());
		cleanForm(form);
		request.setAttribute(Constantes.LISTA_FORNECEDORES_CONSULTA, resultadoPaginacao.getResult());	
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	
	public ActionForward processarConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		Map<String,Object> parametros = new HashMap<String, Object>();
		if (!GenericValidator.isBlankOrNull(myForm.getCnpj())){
			if (CpfCnpj.isValid(myForm.getCnpj())){
				parametros.put(CNPJ,"%" + myForm.getCnpj() + "%");
			}else{
				adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{CNPJ} );
			}
		}	
		if (!GenericValidator.isBlankOrNull(myForm.getNome()))
			parametros.put(NOME,"%" + myForm.getNome() + "%");
		
		int pageNumber = recuperarPaginaAtual(request);
		
		ResultadoPaginacao<FornecedorTO> resultadoPaginacao = Fachada.getInstance().consultarFornecedorPaginado(parametros, pageNumber, getPageSize());
		request.setAttribute(Constantes.LISTA_FORNECEDORES_CONSULTA, resultadoPaginacao.getResult());
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
				
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	public ActionForward processarExcluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		String idFornecedor = request.getParameter("idFornecedor");
		if (idFornecedor == null || idFornecedor.isEmpty()){
			idFornecedor = myForm.getCodigo(); 
		}
		FornecedorTO fornecedor = Fachada.getInstance().getFornecedor(new Integer(idFornecedor));
		if (fornecedor == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		
		if(existeCompraFornecedor(fornecedor)){
			adicionaErro(request, Constantes.MSG10);
			return abrirConsulta(mapping, form, request, response);	
		}
		
		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);			
			transacaoExcluirFornecedor(fornecedor.getIdFornecedor());			
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);			
		} catch (JPAInsertException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);		
		} catch (JPAUpdateException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);		
		} catch (JPADeleteException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);	
		}	

		myForm.reset(mapping, request);				
		request.getSession().setAttribute(Constantes.LISTA_ESTADOS, this.getEstados());
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		adicionaMensagem(request, Constantes.MSG09);
		return abrirConsulta(mapping, form, request, response);
	}
	
	private void transacaoExcluirFornecedor(Integer idFornecedor) throws JPAInsertException, JPAUpdateException, JPADeleteException {
		FornecedorTO fornecedor = Fachada.getInstance().getFornecedor(new Integer(idFornecedor));
		
		//EXCLUIR PRODUTOS DO FORNECEDOR NA MARRA!!!
		Fachada.getInstance().excluirProdutosDoFornecedor(fornecedor.getIdFornecedor());
		fornecedor.getProdutos().clear();	
		
		Fachada.getInstance().removeFornecedor(fornecedor);
		
	}

	private boolean existeCompraFornecedor(FornecedorTO fornecedor) {
		for (Iterator iterator = fornecedor.getProdutos().iterator(); iterator.hasNext();) {
			ProdutoFornecedorTO pf = (ProdutoFornecedorTO) iterator.next();
			if(pf.getDataUltimaCompra() != null){
				return true;
			}			
		}
		return false;
	}

	public ActionForward abrirAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		String idFornecedor = request.getParameter("idFornecedor");
		if (idFornecedor == null || idFornecedor.isEmpty()){
			idFornecedor = myForm.getCodigo(); 
		}
		FornecedorTO fornecedor = Fachada.getInstance().getFornecedor(new Integer(idFornecedor));
		if (fornecedor == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		myForm.reset(mapping, request);
		povoaForm(fornecedor, myForm);	
		request.getSession().setAttribute("fornecedor",fornecedor);
		request.getSession().setAttribute("produtosCadastrados",getProdutosCadastrados());		
		request.setAttribute("produtosAssociados", criaPosicoesAlteracao(fornecedor.getProdutos()));
		request.getSession().setAttribute(Constantes.LISTA_ESTADOS, this.getEstados());
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	
	private ArrayList criaPosicoesAlteracao(List produtos) {
		
		int posicao = 0;
		for (Iterator iterator = produtos.iterator(); iterator.hasNext();) {
			ProdutoFornecedorTO pf = (ProdutoFornecedorTO) iterator.next();
			pf.setPosicao(posicao++);				
		}
		ArrayList lista = new ArrayList(produtos);
		return lista;		
	}

	public ActionForward processarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		myForm.setListaProdutos(fornecedor.getProdutos());
		if (!valida(myForm, mapping, request)){
			myForm.setErro(true);
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}	
		
		if(existeCnpjFornecedor(myForm,request,fornecedor)){
			myForm.setErro(true);
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}		
	
		copiaPropriedades(myForm, fornecedor);		
		try {
			Fachada.getInstance().alterarFornecedor(fornecedor);
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}			
		
		request.setAttribute("idFornecedor", fornecedor.getIdFornecedor());			
		adicionaMensagem(request, Constantes.ALTERACAO_SUCESSO);
		return detalhar(mapping, form, request, response);
	}
	
	
	public ActionForward carregarMunicipiosAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		FornecedorForm myForm = (FornecedorForm) form;
		request.getSession().setAttribute(Constantes.LISTA_MUNICIPIOS, getMunicipios(myForm.getUf()));
		myForm.setCidade("");
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		request.setAttribute("produtosAssociados", fornecedor.getProdutos());
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	private void povoaForm(FornecedorTO fornecedor, FornecedorForm myForm) {
		myForm.setBairro(fornecedor.getBairro());
		myForm.setCep(fornecedor.getCep());
		myForm.setCnpj(fornecedor.getCnpj());		
		if (fornecedor.getMunicipio() != null){
			myForm.setCidade(fornecedor.getMunicipio().getIdMunicipio() + "");
			myForm.setUf(fornecedor.getMunicipio().getEstado().getSigla());
			myForm.setNomeCidade(fornecedor.getMunicipio().getNome());
			myForm.setNomeEstado(fornecedor.getMunicipio().getEstado().getNome());
		}
		myForm.setContatoFornecedor(fornecedor.getContatoFornecedor());
		myForm.setEmail(fornecedor.getEmail());
		myForm.setEndereco(fornecedor.getEndereco());
		myForm.setFax(fornecedor.getTelefoneFax());
		myForm.setNome(fornecedor.getNome());
		myForm.setTelefoneComercial(fornecedor.getTelefoneComercial());
		myForm.setCelular(fornecedor.getTelefoneCelular());
		
	}

	private void copiaPropriedades(FornecedorForm myForm,
			FornecedorTO fornecedor) {
		fornecedor.setBairro(myForm.getBairro().toUpperCase());
		fornecedor.setCep(myForm.getCep());
		fornecedor.setCnpj(myForm.getCnpj());
		fornecedor.setContatoFornecedor(myForm.getContatoFornecedor().toUpperCase());
		fornecedor.setEmail(myForm.getEmail().toUpperCase());
		fornecedor.setEndereco(myForm.getEndereco().toUpperCase());
		if (!GenericValidator.isBlankOrNull(myForm.getCidade()))
			fornecedor.setMunicipio(Fachada.getInstance().getMunicipio(new Integer(myForm.getCidade())));
		fornecedor.setNome(myForm.getNome().toUpperCase());
		fornecedor.setTelefoneCelular(myForm.getCelular());
		fornecedor.setTelefoneFax(myForm.getFax());
		fornecedor.setTelefoneComercial(myForm.getTelefoneComercial());		
	}

	private boolean existeCnpjFornecedor(FornecedorForm myForm,
			HttpServletRequest request, FornecedorTO fornecedor) {
		
		Map<String,Object> parametros = new HashMap<String, Object>();		
		if(!GenericValidator.isBlankOrNull(myForm.getCnpj())){
			if (CpfCnpj.isValid(myForm.getCnpj())){
				parametros.put(CNPJ,"%" + myForm.getCnpj() + "%");
				ResultadoPaginacao<FornecedorTO> resultadoPaginacao = Fachada.getInstance().consultarFornecedorPaginado(parametros, 1, getPageSize());
				if(resultadoPaginacao.getResult() != null && !resultadoPaginacao.getResult().isEmpty()){
					FornecedorTO prx = (FornecedorTO)resultadoPaginacao.getResult().iterator().next();
					if(!prx.getIdFornecedor().equals(fornecedor.getIdFornecedor())){
						adicionaErro(request, Constantes.MSG07);
						return true;
					}	
				}
			}
		}					
		return false;
	}

	public ActionForward cancelarIncluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return (mapping.findForward(Constantes.FORWARD_PAGINA_INICIAL));		
	}
	

	public ActionForward associarProduto(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		FornecedorForm formulario = (FornecedorForm)form;
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		ProdutoFornecedorTO produtoFornecedor = null;
		
		if (GenericValidator.isBlankOrNull(formulario.getProduto())){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, "Produto");
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ABRIR); 
		}		
		produtoFornecedor = new ProdutoFornecedorTO();
		produtoFornecedor.setProduto(Fachada.getInstance().getProduto(Integer.parseInt(formulario.getProduto())));
		produtoFornecedor.setPosicao(fornecedor.getProdutos().size());
		if(jaExisteProdutoAssociado(fornecedor.getProdutos(),produtoFornecedor)){
			adicionaErro(request, "label.produto.ja.associado");
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ABRIR); 
		}
		
		fornecedor.getProdutos().add(produtoFornecedor);
		request.getSession().setAttribute("fornecedor",fornecedor);
		request.setAttribute("produtosAssociados", fornecedor.getProdutos());
		
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}
	
	public ActionForward associarProdutoAlteracao(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		FornecedorForm formulario = (FornecedorForm)form;
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		ProdutoFornecedorTO produtoFornecedor = null;
		
		if (GenericValidator.isBlankOrNull(formulario.getProduto())){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, "Produto");
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}		
		produtoFornecedor = new ProdutoFornecedorTO();
		produtoFornecedor.setProduto(Fachada.getInstance().getProduto(Integer.parseInt(formulario.getProduto())));
		produtoFornecedor.setPosicao(fornecedor.getProdutos().size());
		if(jaExisteProdutoAssociado(fornecedor.getProdutos(),produtoFornecedor)){
			adicionaErro(request, "label.produto.ja.associado");
			request.setAttribute("produtosAssociados", fornecedor.getProdutos());
			return mapping.findForward(Constantes.FORWARD_ALTERAR);
		}
		
		fornecedor.getProdutos().add(produtoFornecedor);
		request.getSession().setAttribute("fornecedor",fornecedor);
		request.setAttribute("produtosAssociados", fornecedor.getProdutos());
		
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	public ActionForward removerProdutoAlteracao(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {				
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");		
		Integer posicao = new Integer(request.getParameter("posicao"));
		
		if (fornecedor != null
				&& !fornecedor.getProdutos().isEmpty()){
			
			if(produtoAssociadoCompra(posicao,fornecedor.getProdutos())){
				adicionaErro(request, "label.produto.associado.compra");
				request.setAttribute("produtosAssociados", fornecedor.getProdutos());
				return mapping.findForward(Constantes.FORWARD_ALTERAR);
			}
			fornecedor.getProdutos().remove(posicao.intValue());			
			int novaPosicao = 0;
			for (ProdutoFornecedorTO produtoFornecedor : fornecedor.getProdutos()) {
				produtoFornecedor.setPosicao(novaPosicao++);
			}
		}
		request.getSession().setAttribute("fornecedor",fornecedor);
		request.setAttribute("produtosAssociados", fornecedor.getProdutos());		
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}	
	
	
	private boolean produtoAssociadoCompra(Integer posicao,
			List<ProdutoFornecedorTO> produtos) {
		ProdutoFornecedorTO pf = produtos.get(posicao);
		if(pf != null && pf.getDataUltimaCompra() != null){
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public ActionForward removerProduto(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {				
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");		
		Integer posicao = new Integer(request.getParameter("posicao"));
		
		if (fornecedor != null
				&& !fornecedor.getProdutos().isEmpty()){
			fornecedor.getProdutos().remove(posicao.intValue());
			int novaPosicao = 0;
			for (ProdutoFornecedorTO produtoFornecedor : fornecedor.getProdutos()) {
				produtoFornecedor.setPosicao(novaPosicao++);
			}
		}
		request.getSession().setAttribute("fornecedor",fornecedor);
		request.setAttribute("produtosAssociados", fornecedor.getProdutos());		
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}	
	
	private boolean jaExisteProdutoAssociado(List produtosAssociados,
			ProdutoFornecedorTO produtoFornecedor) {
		for (Iterator iterator = produtosAssociados.iterator(); iterator
				.hasNext();) {
			ProdutoFornecedorTO prx = (ProdutoFornecedorTO) iterator.next();
			if(prx.getProduto().getIdProduto().equals(produtoFornecedor.getProduto().getIdProduto())){
				return true;
			}			
		}
		return false;
	}

	public String getTableId(){
		String tableId = "cliente"; // código definido para a tabela na página JSP
		return tableId;
	}
}
