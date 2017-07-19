package br.com.granit.apresentacao.pedido;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.controlador.ControladorHistoricoProduto;
import br.com.granit.controlador.ControladorPedido;
import br.com.granit.controlador.ControladorTipoProduto;
import br.com.granit.fachada.Fachada;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.pk.ItemPedidoPK;
import br.com.granit.persistencia.to.FornecedorTO;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.ItemPedidoTO;
import br.com.granit.persistencia.to.PedidoTO;
import br.com.granit.persistencia.to.ProdutoFornecedorTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.persistencia.to.SubTipoProdutoTO;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.Formatador;
import br.com.granit.util.JPAUtility;
import br.com.granit.util.ResultadoPaginacao;

public class PedidoAction extends PrincipalAction{
	
	
	private static final String PAGINA_RETORNO = "paginaRetorno";
	

	

	public ActionForward abrir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute(Constantes.PEDIDO);
		request.getSession().removeAttribute("fornecedor");		
		PedidoForm myForm = (PedidoForm) form;		
		if (!myForm.isErro()){ 
			myForm.cleanForm();		
			
			//Retirar ap�s carga de hist�rico
			//26/03/2011 - desabilitando datas de pedido e vendas
			//myForm.setDataPedido("");
			
			myForm.setQuantidadeProduto("");
			myForm.setPrecoProduto("");
			myForm.setIdProduto(0);
			request.setAttribute("pedidoForm", myForm);	
			request.removeAttribute(Constantes.PEDIDO);
			request.getSession().setAttribute("produtosCadastrados",getProdutosCadastrados());
			request.getSession().setAttribute("fornecedoresCadastrados",getFornecedoresCadastrados());
			request.getSession().setAttribute(Constantes.LISTA_ITENS_PEDIDO,new ArrayList());
			PedidoTO pedido = new PedidoTO();			
			request.getSession().setAttribute(Constantes.PEDIDO, pedido);
		}
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}  
	
	public ActionForward associarProdutoPedido(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		PedidoForm formulario = (PedidoForm)form;
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);
		String telaOrigem = (String)request.getParameter("caminhoAssociacaoProdutoPedido");
		ProdutoTO p = (ProdutoTO)request.getSession().getAttribute("produto");		
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		if(fornecedor != null){
			formulario.setIdFornecedor(fornecedor.getIdFornecedor());
		}
		if(p != null && p.getSubTipo() != null){
			formulario.setTipoProduto(p.getSubTipo().getTipo().getDescricao());
			formulario.setSubTipoProduto(p.getSubTipo().getDescricao());
		}	
		if(pedido == null){			
			pedido = new PedidoTO();			
		}	
		if(!fornecedorPreenchido(formulario,request)){
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO,new ArrayList(pedido.getItens()));
			pedido.setTotalSemDesconto(pedido.getSubTotal());
			if(pedido.getDesconto() != null){
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
			}
			else{
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
			}
			if(telaOrigem != null){
				if(telaOrigem.equals("incluir")){
					request.setAttribute("pedidoForm", formulario);	
					return mapping.findForward(Constantes.FORWARD_ABRIR); 
				}
				else{
					request.setAttribute("pedidoForm", formulario);	
					return mapping.findForward(Constantes.FORWARD_ALTERAR); 
				}
			}
			return mapping.findForward(Constantes.FORWARD_ABRIR); 
		}
		else{
			fornecedor = Fachada.getInstance().getFornecedor(formulario.getIdFornecedor());			
			pedido.setFornecedor(fornecedor);
			request.getSession().setAttribute("fornecedor", fornecedor);
			formulario.setIdFornecedor(fornecedor.getIdFornecedor());
	
		}
		ItemPedidoTO item = null;
		boolean erro = false;
		
	
		if(formulario != null){
			if(validaObrigatoriosItem(formulario,request)){
				item = new ItemPedidoTO();
				ItemPedidoPK pk = new ItemPedidoPK();					
				pk.setIdProduto(formulario.getIdProduto());						
				ProdutoTO produto = Fachada.getInstance().getProduto(formulario.getIdProduto());
				item.setProduto(produto);
				item.setPk(pk);
				BigDecimal quantidade = new BigDecimal(new Double(formataDouble(formulario.getQuantidadeProduto())));
				item.setQuantidade(quantidade);
				double quantidadeTotalDoProduto = quantidade.doubleValue();
				if (pedido.getItens() == null)
					pedido.setItens(new ArrayList<ItemPedidoTO>());
				for (ItemPedidoTO itemVenda : pedido.getItens()) {
					if (itemVenda.getPk().getIdProduto().intValue() == produto.getIdProduto().intValue()){
						quantidadeTotalDoProduto += itemVenda.getQuantidade().doubleValue();
					}
				}
				if(jaExisteProdutoAssociadoPedido(pedido.getItens(),item)){
					adicionaErro(request, "label.produto.ja.associado.pedido");
					request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, new ArrayList(pedido.getItens()));
					pedido.setTotalSemDesconto(pedido.getSubTotal());
					if(pedido.getDesconto() != null){
						pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
					}
					else{
						pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
					}
					if(telaOrigem != null){
						if(telaOrigem.equals("incluir")){
							request.setAttribute("pedidoForm", formulario);	
							return mapping.findForward(Constantes.FORWARD_ABRIR); 
						}
						else{
							request.setAttribute("pedidoForm", formulario);	
							return mapping.findForward(Constantes.FORWARD_ALTERAR); 
						}
					}
					request.setAttribute("pedidoForm", formulario);	
					return mapping.findForward(Constantes.FORWARD_ABRIR); 
				}
				
				item.setPedido(pedido);
				item.setPrecoUnitario(new BigDecimal(new Double(formataDouble(formulario.getPrecoProduto()))));
				item.setPrecoTotal(item.getQuantidade().multiply(item.getPrecoUnitario()));				
				item.setPosicao(pedido.getItens().size());
				//Utilizado sen�o n�o cria usando o merge. D� problema
				if(telaOrigem.equals("alterar")){					
					pk.setIdPedido(pedido.getIdPedido());									
					pk.setIdItemPedido(item.getPosicao());
				}
				pedido.getItens().add(item);				
				
			}	
			else{
				erro = true;
			}
		}		
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, new ArrayList(pedido.getItens()));
		if (erro){
			pedido.setTotalSemDesconto(pedido.getSubTotal());
			if(pedido.getDesconto() != null){
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
			}
			else{
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
			}
			if(telaOrigem != null){
				if(telaOrigem.equals("incluir")){
					request.setAttribute("pedidoForm", formulario);	
					return mapping.findForward(Constantes.FORWARD_ABRIR); 
				}
				else{
					request.setAttribute("pedidoForm", formulario);	
					return mapping.findForward(Constantes.FORWARD_ALTERAR); 
				}
			}
			return (mapping.findForward(Constantes.FORWARD_ABRIR));			
		}
			
		request.getSession().setAttribute(Constantes.PEDIDO, pedido);	
		formulario.setQuantidadeProduto("");
		formulario.setPrecoProduto("");
		formulario.setIdProduto(0);
		request.setAttribute("pedidoForm",formulario);
		pedido.setTotalSemDesconto(pedido.getSubTotal());
		if(pedido.getDesconto() != null){
			pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
		}
		else{
			pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
		}		
		if(telaOrigem != null){
			if(telaOrigem.equals("incluir")){
				return mapping.findForward(Constantes.FORWARD_ABRIR); 
			}
			else{
				return mapping.findForward(Constantes.FORWARD_ALTERAR); 
			}
		}
		return mapping.findForward(Constantes.FORWARD_ABRIR); 
	}
	
	
	public ActionForward exibirHistoricoProduto(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		PedidoForm formulario = (PedidoForm)form;
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);
		String telaOrigem = (String)request.getParameter("caminhoAssociacaoProdutoPedido");
		FornecedorTO fornecedor = (FornecedorTO)request.getSession().getAttribute("fornecedor");
		if(fornecedor != null){
			formulario.setIdFornecedor(fornecedor.getIdFornecedor());
		}
		
		if(pedido == null){			
			pedido = new PedidoTO();				
		}			
		if(pedido.getItens() == null){
			pedido.setItens(new ArrayList<ItemPedidoTO>());
		}
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, new ArrayList(pedido.getItens()));
		
		Integer idProduto = formulario.getIdProduto();
		List novalista = null;
		if (idProduto != null){		
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idProduto", idProduto);
			List l = Fachada.getInstance().listarProdutoFornecedor(parametros);
			novalista = filtraListaProduto(l);
			request.setAttribute(Constantes.LISTA_HISTORICO_PRODUTO, novalista);
		}
			
		request.getSession().setAttribute(Constantes.PEDIDO, pedido);	
		ProdutoTO p = Fachada.getInstance().getProduto(idProduto);	
		request.getSession().setAttribute("produto", p);	
		if(p.getSubTipo() != null){
			formulario.setTipoProduto(p.getSubTipo().getTipo().getDescricao());
			formulario.setSubTipoProduto(p.getSubTipo().getDescricao());
		}			
		
		
		request.setAttribute("pedidoForm",formulario);
		pedido.setTotalSemDesconto(pedido.getSubTotal());
		if(pedido.getDesconto() != null){
			pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
		}
		else{
			pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
		}		
		if(telaOrigem != null){
			if(telaOrigem.equals("incluir")){
				return mapping.findForward(Constantes.FORWARD_ABRIR); 
			}
			else{
				return mapping.findForward(Constantes.FORWARD_ALTERAR); 
			}
		}
		return mapping.findForward(Constantes.FORWARD_ABRIR); 
	}
	
	

	private List<ProdutoFornecedorTO> filtraListaProduto(List l) {
		List listaFiltrada = new ArrayList<ProdutoFornecedorTO>();
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			ProdutoFornecedorTO object = (ProdutoFornecedorTO) iterator.next();
			if(object.getDataUltimaCompra() != null){
				listaFiltrada.add(object);
			}
		}
		return listaFiltrada;
		
	}

	private void associaProdutoFornecedor(FornecedorTO fornecedor,
			ItemPedidoTO item, Date data) {
		ProdutoFornecedorTO produtoFornecedor = new ProdutoFornecedorTO();
		produtoFornecedor.setProduto(Fachada.getInstance().getProduto(item.getProduto().getIdProduto()));
		produtoFornecedor.setPosicao(fornecedor.getProdutos().size());		
		produtoFornecedor.setDataUltimaCompra(data);
		produtoFornecedor.setValorUnitarioUltimaCompra(item.getPrecoUnitario());
		fornecedor.getProdutos().add(produtoFornecedor);		
	}

	public ActionForward adicionar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ParseException {
		PedidoForm formulario = (PedidoForm)form;
		formulario.setFinalizado(Boolean.FALSE);		
		return gravaPedido(request,formulario, mapping,response,"adicionar");		
		
	}
	
	public ActionForward detalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		PedidoForm formulario = (PedidoForm)form;
		String idPedidoString = request.getParameter("idPedido");	
		Integer idPedido = null;
		if (idPedidoString == null || idPedidoString.isEmpty()){
			idPedido = (Integer)request.getAttribute("idPedido"); 
		}
		else{
			idPedido = new Integer(idPedidoString);
		}
		PedidoTO pedido = Fachada.getInstance().getPedido(idPedido);
		if (pedido == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		String paginaRetorno = formulario.getPaginaRetorno();
		if ( !GenericValidator.isBlankOrNull(request.getParameter(PAGINA_RETORNO))){
			paginaRetorno = request.getParameter(PAGINA_RETORNO);
		}
		formulario.setPaginaRetorno(paginaRetorno);
		request.setAttribute(Constantes.PEDIDO, pedido);
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
		return mapping.findForward(Constantes.FORWARD_DETALHAR);
	}
	
	public ActionForward cancelarIncluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		request.removeAttribute(Constantes.PEDIDO);
		return (mapping.findForward(Constantes.FORWARD_PAGINA_INICIAL));		
	}
	
	public ActionForward voltarDetalhar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		PedidoForm formulario = (PedidoForm)form;
		String retorno = Constantes.FORWARD_CONSULTA_ABRIR;
		if (!GenericValidator.isBlankOrNull(formulario.getPaginaRetorno())){
			retorno = formulario.getPaginaRetorno();
		}
		return (mapping.findForward(retorno));
	}
	
	public ActionForward processarExcluir(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		PedidoForm formulario = (PedidoForm)form;
		String idPedidoString = request.getParameter("idPedido");	
		UsuarioTO usuario = (UsuarioTO)request.getSession().getAttribute(Constantes.USUARIO_LOGADO);
		Integer idPedido = null;
		if (idPedidoString == null || idPedidoString.isEmpty()){
			idPedido = (Integer)request.getAttribute("idPedido"); 
		}
		else{
			idPedido = new Integer(idPedidoString);
		}		
		if (idPedido == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}		
		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			transacaoExcluirPedido(idPedido,usuario);			
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);			
		} catch (JPAInsertException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);		
		} catch (JPAUpdateException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);		
		} catch (JPADeleteException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_CONSULTA_ABRIR);		
		}	
		formulario.reset(mapping, request);					
		adicionaMensagem(request, Constantes.MSG18);
		return abrirConsulta(mapping, form, request, response);
	}
	
	
	public ActionForward abrirAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		PedidoForm formulario = (PedidoForm)form;
		String idPedidoString = request.getParameter("idPedido");	
		
		
		PedidoTO pedido = Fachada.getInstance().getPedido(new Integer(idPedidoString));
		if (pedido == null){
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}	
		formulario.reset(mapping, request);
		povoaForm(pedido, formulario);	
		request.getSession().setAttribute("fornecedor", pedido.getFornecedor());
		request.getSession().setAttribute(Constantes.PEDIDO, pedido);
		request.getSession().setAttribute("produtosCadastrados",getProdutosCadastrados());
		request.getSession().setAttribute("fornecedoresCadastrados",getFornecedoresCadastrados());
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
		pedido.setTotalSemDesconto(pedido.getSubTotal());
		if(pedido.getDesconto() != null){
			pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
		}
		else{
			pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
		}	
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}
	
	
	public ActionForward removerItemPedidoAlteracao(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {				
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);	
		Integer posicao = new Integer(request.getParameter("posicao"));
		
		if (pedido != null
				&& !pedido.getItens().isEmpty()){
			ItemPedidoTO  itemRemovido = (ItemPedidoTO)pedido.getItens().remove(posicao.intValue());		
			int novaPosicao = 0;
			for (ItemPedidoTO item : pedido.getItens()) {
				item.setPosicao(novaPosicao++);
			}
			pedido.setTotalSemDesconto(pedido.getSubTotal());
			if(pedido.getDesconto() != null){
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
			}
			else{
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
			}
		}
		
		request.getSession().setAttribute(Constantes.PEDIDO, pedido);
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, new ArrayList(pedido.getItens()));	
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
	}	
	
	private ArrayList criaPosicoesAlteracao(List itens) {
		
		int posicao = 0;
		for (Iterator iterator = itens.iterator(); iterator.hasNext();) {
			ItemPedidoTO itemPedidoTO = (ItemPedidoTO) iterator.next();
			itemPedidoTO.setPosicao(posicao++);				
		}
		ArrayList lista = new ArrayList(itens);
		return lista;		
	}

	public ActionForward finalizarAlteracao(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {	
		PedidoForm formulario = (PedidoForm)form;
		formulario.setFinalizado(Boolean.TRUE);		
		return alteraPedido(request,formulario, mapping,response,"finalizar");		
	}
	
	public ActionForward alterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		PedidoForm formulario = (PedidoForm)form;
		formulario.setFinalizado(Boolean.FALSE);
		return alteraPedido(request,formulario, mapping,response,"alterar");		
		
	}
	
	
	
	private ActionForward alteraPedido(HttpServletRequest request,
			PedidoForm formulario, ActionMapping mapping,
			HttpServletResponse response, String acao) {
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);	
		UsuarioTO usuario = (UsuarioTO)request.getSession().getAttribute(Constantes.USUARIO_LOGADO);
		formulario.setListaItens(criaPosicoesAlteracao(pedido.getItens()));
		if (!valida(formulario, request, pedido)){
			formulario.setErro(true);
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			pedido.setTotalSemDesconto(pedido.getSubTotal());
			if(pedido.getDesconto() != null){
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
			}
			else{
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
			}
			return mapping.findForward(Constantes.FORWARD_ABRIR); 
		}			
		associaProdutosFornecedor(pedido);
		pedido.setFinalizado(formulario.getFinalizado());
		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);			
			Fachada.getInstance().excluirItensDoPedido(pedido.getIdPedido());
			Fachada.getInstance().alterarFornecedor(pedido.getFornecedor());	
			Fachada.getInstance().alterarPedido(pedido);
			if(pedido.getFinalizado().booleanValue()){
				atualizaEstoque(pedido,usuario,'E');
			}
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		}catch (JPAInsertException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		 	
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		} 
		request.getSession().removeAttribute(Constantes.PEDIDO);
		request.setAttribute("idPedido", pedido.getIdPedido());		
		if(acao.equals("alterar")){
			adicionaMensagem(request, Constantes.ALTERACAO_SUCESSO);			
		}	
		else if(acao.equals("finalizar")){
			adicionaMensagem(request, Constantes.MSG_PEDIDO_FINALIZADO_SUCESSO);
		}	
		formulario.setPaginaRetorno(Constantes.FORWARD_PAGINA_INICIAL);
		return 	detalhar(mapping, formulario, request, response);
	}

	public ActionForward cancelarAlterar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		return abrirConsulta(mapping, form, request, response);	
	}
	
	private void povoaForm(PedidoTO pedido, PedidoForm formulario) {
		formulario.setIdFornecedor(pedido.getFornecedor().getIdFornecedor());
		
	}

	//Deve ser executado dentro da mesma transa��o.
	private void transacaoExcluirPedido(Integer idPedido, UsuarioTO usuario)
			throws JPAInsertException, JPAUpdateException, JPADeleteException {
		PedidoTO pedido = Fachada.getInstance().getPedido(idPedido);
		
		//EXCLUIR ITENS DE PEDIDO NA MARRA!!!
		if(pedido.getFinalizado().booleanValue()){
			atualizaEstoque(pedido,usuario,'S');
		}
		Fachada.getInstance().excluirItensDoPedido(pedido.getIdPedido());
		pedido.getItens().clear();	
		ControladorPedido.getInstance().remove(pedido);
	}

	
	
	public ActionForward processarConsulta(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		PedidoForm formulario = (PedidoForm)form;
		Map<String, Object> parametros = new HashMap<String, Object>();
		if (!GenericValidator.isBlankOrNull(formulario.getDataInicio())) {
			try {
				parametros.put("dataInicio", Formatador
						.converterStringParaDate(formulario.getDataInicio(),
								Formatador.FORMATO_DATA_PADRAO));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getDataFim())) {
			try {
				parametros.put("dataFim", Formatador
						.converterStringParaDate(formulario.getDataFim(),
								Formatador.FORMATO_DATA_PADRAO));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!formulario.isMostrarPedidosFinalizados()){
			parametros.put("mostrarPedidosFinalizados", Boolean.FALSE);		
		}else{
			//parametros.put("mostrarPedidosFinalizados", Boolean.TRUE);
			//N�o seta, pois pegar� todos os pedidos finalizados ou n�o
		}
		
		if (formulario.getIdFornecedor() != null && formulario.getIdFornecedor() > 0) {
			parametros.put("idFornecedor", new Integer(formulario.getIdFornecedor()));
		}
		if (formulario.getIdProduto() != null && formulario.getIdProduto() > 0) {
			parametros.put("idProduto", new Integer(formulario.getIdProduto()));
		}
		if (formulario.getIdTipo() != null && formulario.getIdTipo() > 0) {
			parametros.put("idTipo", new Integer(formulario.getIdTipo()));
		}
		if (formulario.getIdSubTipo()!= null && formulario.getIdSubTipo() > 0) {
			parametros.put("subTipo", new Integer(formulario.getIdSubTipo()));
		}
		
		
		List<PedidoTO> pedidos = null;
		int pageNumber = recuperarPaginaAtual(request);			
		ResultadoPaginacao<PedidoTO> resultadoPaginacao = Fachada.getInstance()
				.consultarPedidoPaginado(parametros, pageNumber, getPageSize());
		request.getSession().setAttribute("resultadoPaginacaoCarregarTipo", resultadoPaginacao);
		pedidos = resultadoPaginacao.getResult();
		// DEFININDO OS PARAMETROS DA PAGINACAO
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
		
		pedidos = resultadoPaginacao.getResult();

		request.setAttribute(Constantes.LISTA_PEDIDOS_CONSULTA, pedidos);
		request.getSession().setAttribute(Constantes.RESULTADO_PAGINACAO,
				resultadoPaginacao);
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	
	public ActionForward carregarSubTiposConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {		
		PedidoForm myForm = (PedidoForm) form;
		List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto();
		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.getSession().setAttribute("subtipos", getSubTipos(myForm.getIdTipo()+""));	
		request.setAttribute("pedidoForm", myForm);
		ResultadoPaginacao resultadoPaginacao = (ResultadoPaginacao)request.getSession().getAttribute("resultadoPaginacaoCarregarTipo");
		request.setAttribute(Constantes.LISTA_PEDIDOS_CONSULTA, resultadoPaginacao.getResult());	
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		return mapping.findForward(Constantes.FORWARD_CONSULTA);
	}
	
	private List<SubTipoProdutoTO> getSubTipos(String tipo) {
		if (GenericValidator.isBlankOrNull(tipo))
			return new ArrayList<SubTipoProdutoTO>();
	
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("tipo", new Integer(tipo));
		return Fachada.getInstance().consultarSubTipoPorTipo(parametros);		
		
		
	}
	
	
	public ActionForward abrirConsulta(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		int pageNumber = recuperarPaginaAtual(request);
		Map<String,Object> parameters = new HashMap<String, Object>();
		ResultadoPaginacao resultadoPaginacao = Fachada.getInstance().consultarPedidoPaginado(parameters, pageNumber, getPageSize());
		PedidoForm formulario = (PedidoForm)form;
		formulario.setDataFim("");
		formulario.setDataInicio("");
		formulario.setFinalizado(null);
		formulario.setIdFornecedor(0);
		formulario.setIdProduto(0);
		formulario.setMostrarPedidosFinalizados(true);
		request.setAttribute("pedidoForm", formulario);
		request.getSession().setAttribute("resultadoPaginacaoCarregarTipo", resultadoPaginacao);
		request.setAttribute(Constantes.LISTA_PEDIDOS_CONSULTA, resultadoPaginacao.getResult());	
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
		request.getSession().setAttribute("fornecedoresCadastrados",getFornecedoresCadastrados());
		request.getSession().setAttribute("produtosCadastrados",getProdutosCadastrados());
		request.getSession().setAttribute(Constantes.RESULTADO_PAGINACAO,resultadoPaginacao);
		request.getSession().setAttribute("tipos", getTipos());
		request.getSession().setAttribute("subtipos", new ArrayList());
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	private Object getTipos() {
		return ControladorTipoProduto.getInstance().listar();
	}

	
	
	private ActionForward gravaPedido(HttpServletRequest request, PedidoForm formulario, ActionMapping mapping,HttpServletResponse response, String acao) throws ParseException {
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);	
		formulario.setListaItens(criaPosicoesAlteracao(pedido.getItens()));
		UsuarioTO usuario = (UsuarioTO)request.getSession().getAttribute(Constantes.USUARIO_LOGADO);
		
		if (!validaGravacao(formulario, request, pedido)){
			formulario.setErro(true);
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			pedido.setTotalSemDesconto(pedido.getSubTotal());
			if(pedido.getDesconto() != null){
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
			}
			else{
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
			}
			return mapping.findForward(Constantes.FORWARD_ABRIR); 
		}			
		
		
			
			associaProdutosFornecedor(pedido);
			pedido.setFinalizado(formulario.getFinalizado());
			JPAUtility.setTransacoesAtivadas(false);
			EntityManager em = JPAUtility.getEntityManager();
			try {
				JPAUtility.beginTransactionGroup(em);
				Fachada.getInstance().alterarFornecedor(pedido.getFornecedor());	
				Fachada.getInstance().salvaPedido(pedido);
				if(pedido.getFinalizado().booleanValue()){
					atualizaEstoque(pedido,usuario,'E');
				}
				
				JPAUtility.commitTransactionGroup(em);
				JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAInsertException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		request.getSession().removeAttribute(Constantes.PEDIDO);
		request.setAttribute("idPedido", pedido.getIdPedido());		
		if(acao.equals("adicionar")){
			adicionaMensagem(request, Constantes.MSG_CADASDTRO_SUCESSO);			
		}	
		else if(acao.equals("finalizar")){
			adicionaMensagem(request, Constantes.MSG_PEDIDO_FINALIZADO_SUCESSO);
		}	
		formulario.setPaginaRetorno(Constantes.FORWARD_PAGINA_INICIAL);
		return 	detalhar(mapping, formulario, request, response);		
	}

	private void atualizaEstoque(PedidoTO pedido, UsuarioTO usuario, char tipo ) throws JPAInsertException, JPAUpdateException {
		for (Iterator iterator = pedido.getItens().iterator(); iterator.hasNext();) {
			ItemPedidoTO item = (ItemPedidoTO) iterator.next();
			ProdutoTO produto = item.getProduto();
			
			HistoricoProdutoTO historico = new HistoricoProdutoTO();
			historico.setProduto(produto);		
			historico.setTipo(tipo);
			historico.setUsuario(usuario);
			if(tipo == 'E'){
				produto.setQtdEstoque(produto.getQtdEstoque() + item.getQuantidade().doubleValue());
				historico.setObservacao(Constantes.OBS2.replaceFirst("IdPedido/usuarioX", pedido.getIdPedido()+"/" + usuario.getLogin()));
			}
			else{
				produto.setQtdEstoque(produto.getQtdEstoque() - item.getQuantidade().doubleValue());
				historico.setObservacao(Constantes.OBS3.replaceFirst("IdPedido/usuarioX", pedido.getIdPedido()+"/" + usuario.getLogin()));
			}
			
			//26/03/2011 - desabilitando datas de pedido e vendas		
			//if(pedido.getDataPedido() != null){
			//	historico.setData(pedido.getDataPedido());
			//}
			historico.setQuantidade(item.getQuantidade());			
			ControladorHistoricoProduto.getInstance().salva(historico);
			Fachada.getInstance().alterarProduto(produto);			
		}
		
	}

	public ActionForward finalizarCriacaoInicial(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ParseException {	
		PedidoForm formulario = (PedidoForm)form;
		formulario.setFinalizado(Boolean.TRUE);		
		return gravaPedido(request,formulario, mapping,response,"finalizar");		
	}
	
	private void associaProdutosFornecedor(PedidoTO pedido) {
		//26/03/2011 - desabilitando datas de pedido e vendas
		Date data = new Date();
		List itens = pedido.getItens();
		List listaProdutoFornecedorOriginal = pedido.getFornecedor().getProdutos();
		
		for (Iterator iterator = itens.iterator(); iterator
				.hasNext();) {
			ItemPedidoTO item = (ItemPedidoTO) iterator.next();
			boolean novo = true;
			for (Iterator iterator2 = listaProdutoFornecedorOriginal.iterator(); iterator2.hasNext();) {
				ProdutoFornecedorTO pf = (ProdutoFornecedorTO) iterator2.next();
				if(item.getProduto().getIdProduto().equals(pf.getProduto().getIdProduto())){
					novo = false;
					//26/03/2011 - desabilitando datas de pedido e vendas
					pf.setDataUltimaCompra(data);
					
					//pf.setDataUltimaCompra(pedido.getDataPedido());
					pf.setValorUnitarioUltimaCompra(item.getPrecoUnitario());				
					break;
				}
			}
			if(novo){
				//26/03/2011 - desabilitando datas de pedido e vendas
				associaProdutoFornecedor(pedido.getFornecedor(),item,data);
				//associaProdutoFornecedor(pedido.getFornecedor(),item,pedido.getDataPedido());
			}			
		}
		
		//26/03/2011 - desabilitando datas de pedido e vendas
		pedido.setDataPedido(data);		
	}

	private boolean valida(PedidoForm formulario, HttpServletRequest request, PedidoTO pedido) {
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		ArrayList<String> camposInvalidos = new ArrayList<String>();
		
//		if (formulario.getIdFornecedor() == null || formulario.getIdFornecedor() == 0){
//			camposObrigatorios.add("Fornecedor");
//		}
//		if (pedido.getFornecedor() == null){
//			camposObrigatorios.add("Fornecedor");
//		}
		
		if (pedido.getItens() == null || pedido.getItens().isEmpty()){
			camposObrigatorios.add("Produtos");
		}		
		if (pedido.getTotalComDesconto() == null){
			camposInvalidos.add("Valor total");
		}
		
		if(pedido.getDesconto() != null){
			if (pedido.getTotalComDesconto().subtract(pedido.getDesconto()).doubleValue() <= 0){
				camposInvalidos.add("Desconto inv�lido");
			}
		}
		
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, new Object[]{geraStringCamposObrigatorios(
					camposObrigatorios)});
			return false;
		}	
		if (!camposInvalidos.isEmpty()){
			adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{"Valor total"} );
			return false;
		}	
		return true;
	}
	
	
	//retirar apos carga inicial de historico
	private boolean validaGravacao(PedidoForm formulario, HttpServletRequest request, PedidoTO pedido) throws ParseException {
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		ArrayList<String> camposInvalidos = new ArrayList<String>();
		
//		if (formulario.getIdFornecedor() == null || formulario.getIdFornecedor() == 0){
//			camposObrigatorios.add("Fornecedor");
//		}
//		if (pedido.getFornecedor() == null){
//			camposObrigatorios.add("Fornecedor");
//		}
			
		//26/03/2011 - desabilitando datas de pedido e vendas		
		//if (formulario.getDataPedido()== null || formulario.getDataPedido().equals("")){
		//	camposObrigatorios.add("Data Pedido");
		//}
		//else{
		//	pedido.setDataPedido(Formatador.converterStringParaDate(formulario.getDataPedido(),Formatador.FORMATO_DATA_PADRAO));
		//}
		
		if (pedido.getItens() == null || pedido.getItens().isEmpty()){
			camposObrigatorios.add("Produtos");
		}	
		
		
		if (pedido.getTotalComDesconto() == null){
			camposInvalidos.add("Valor total");
		}
		
		if(pedido.getDesconto() != null){
			if (pedido.getTotalComDesconto().subtract(pedido.getDesconto()).doubleValue() <= 0){
				camposInvalidos.add("Desconto inv�lido");
			}
		}
		
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, new Object[]{geraStringCamposObrigatorios(
					camposObrigatorios)});
			return false;
		}	
		if (!camposInvalidos.isEmpty()){
			adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, new String[]{"Valor total"} );
			return false;
		}	
		
		
		return true;
	}

	private boolean fornecedorPreenchido(PedidoForm formulario, HttpServletRequest request) {
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if (formulario.getIdFornecedor() == null || formulario.getIdFornecedor() == 0){
			camposObrigatorios.add("Fornecedor");
		}		
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, new Object[]{geraStringCamposObrigatorios(
					camposObrigatorios)});
			return false;
		}
		return true;
	}

	public ActionForward removerItemPedido(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {				
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);	
		Integer posicao = new Integer(request.getParameter("posicao"));
		
		if (pedido != null
				&& !pedido.getItens().isEmpty()){
			ItemPedidoTO  itemRemovido = (ItemPedidoTO)pedido.getItens().remove(posicao.intValue());		
			int novaPosicao = 0;
			for (ItemPedidoTO item : pedido.getItens()) {
				item.setPosicao(novaPosicao++);
			}
			pedido.setTotalSemDesconto(pedido.getSubTotal());
			if(pedido.getDesconto() != null){
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));
			}
			else{
				pedido.setTotalComDesconto(pedido.getTotalSemDesconto());
			}
		}
		
		request.getSession().setAttribute(Constantes.PEDIDO, pedido);
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));	
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}	
	
//	private void atualizaRemoveListaProdutoFornecedor(FornecedorTO fornecedor,
//			ItemPedidoTO itemRemovido) {
//		for (Iterator iterator = fornecedor.getProdutos().iterator(); iterator.hasNext();) {
//			ProdutoFornecedorTO pf = (ProdutoFornecedorTO) iterator.next();
//			if(pf.getProduto().getIdProduto() == itemRemovido.getProduto().getIdProduto()){
//				fornecedor.getProdutos().re
//			}
//			
//		}
//		
//	}

	private boolean jaExisteProdutoAssociadoPedido(List<ItemPedidoTO> itens,
			ItemPedidoTO item) {
		for (Iterator iterator = itens.iterator(); iterator.hasNext();) {
			ItemPedidoTO prx = (ItemPedidoTO) iterator.next();
			if(prx.getProduto().getIdProduto().equals(item.getProduto().getIdProduto())){
				return true;
			}			
		}
		return false;
	}

	private boolean validaObrigatoriosItem(PedidoForm formulario,
			HttpServletRequest request) {
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		
		if (formulario.getIdFornecedor() == null || formulario.getIdFornecedor() == 0){
			camposObrigatorios.add("Fornecedor");
		}
		if(formulario.getIdProduto() == null || formulario.getIdProduto() == 0){
			camposObrigatorios.add("Produto");
		}
		if (GenericValidator.isBlankOrNull(formulario.getPrecoProduto())){
			camposObrigatorios.add("Pre�o");
		}
		if (GenericValidator.isBlankOrNull(formulario.getQuantidadeProduto())){
			camposObrigatorios.add("Quantidade");
		}		
		if (!camposObrigatorios.isEmpty()){
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, new Object[]{geraStringCamposObrigatorios(
					camposObrigatorios)});
			return false;
		}			
		return true;
	}
	
	
	public ActionForward aplicaDescontoPedido(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		PedidoTO pedido = (PedidoTO)request.getSession().getAttribute(Constantes.PEDIDO);
		pedido.setDesconto(new BigDecimal(Formatador.parseDouble(((PedidoForm)form).getDesconto())));
		pedido.setTotalSemDesconto(pedido.getSubTotal());
		pedido.setTotalComDesconto(pedido.getTotalSemDesconto().subtract(pedido.getDesconto()));		
		request.getSession().setAttribute(Constantes.PEDIDO, pedido);
		request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
		
		String acao = (String)request.getParameter("tipoAcao");
		if(acao.equals("S")){
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		return mapping.findForward(Constantes.FORWARD_ALTERAR);
		
	}
	

	private Object getFornecedoresCadastrados() {
		return Fachada.getInstance().consultarFornecedor();
	}

	private Object getProdutosCadastrados() {
		return Fachada.getInstance().consultarProduto();
	}
	
	@Override
	protected String getTableId() {
		String tableId = "pedido"; // c�digo definido para a tabela na p�gina JSP
		return tableId;
	}
	
	public ActionForward imprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Integer idPedido = new Integer(request.getParameter("idPedido"));
		try {
			PedidoTO pedido = Fachada.getInstance().getPedido(idPedido);
			request.setAttribute(Constantes.PEDIDO, pedido);
			request.setAttribute(Constantes.LISTA_ITENS_PEDIDO, criaPosicoesAlteracao(pedido.getItens()));
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("pedido.idPedido", pedido.getIdPedido());

			List<Object> clAttrs = new ArrayList<Object>();
			for (ItemPedidoTO item : pedido.getItens()) {
				RelatorioDetalharPedidoTO relatorio = new RelatorioDetalharPedidoTO();
				relatorio.setCodigo(item.getProduto().getIdProduto()+"");
				relatorio.setDescricao(item.getProduto().getDescricaoTipoSubTipo());
				relatorio.setQuantidade(item.getQuantidade().doubleValue());
				relatorio.setPrecoUnitario(item.getPrecoUnitario().doubleValue());
				relatorio.setPrecoTotal(item.getPrecoTotal().doubleValue());
				//relatorio.setTipoSubtipo(item.getProduto().getTipoSubtipo());
				clAttrs.add(relatorio);
			}
			// par�metros
			Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
			parametrosRelatorio.put("dataPedido", pedido.getDataPedidoFormatada());
			parametrosRelatorio
					.put("nomeFornecedor", pedido.getFornecedor().getNome());
			parametrosRelatorio.put("subTotal", pedido.getSubTotalFormatado());					
			if (pedido.getDesconto() != null)
				parametrosRelatorio.put("desconto", pedido.getDescontoFormatado());
			parametrosRelatorio.put("total", pedido.getTotalComDescontoFormatado());

			// Na variavel pathJasper ficara o caminho do diret�rio para
			// os relat�rios compilados (.jasper)
			String pathJasper = request.getSession().getServletContext()
					.getRealPath("/relatorios");
			
			String pathImages = request.getSession().getServletContext()
			.getRealPath("/images");
			parametrosRelatorio.put("pathImages", pathImages);
			

			// A variavel path armazena o caminho real para o contexto
			// isso � util pois o seu web container pode estar instalado em
			// lugares diferentes
			String path = request.getSession().getServletContext().getRealPath(
					"/");

			JasperPrint jprint = null;
			jprint = JasperFillManager.fillReport(pathJasper + File.separator
					+ "detalhapedido.jasper", parametrosRelatorio,
					new JRBeanCollectionDataSource(clAttrs));

			
			this.gerarSaidaPDF(response, jprint, "Pedido");
			
			// Grava o relat�rio em disco em pdf
			//JasperExportManager.exportReportToPdfFile(jprint, path
			//		+ "/relatVenda.pdf");

			// Redireciona para o pdf gerado
			//response.sendRedirect("relatVenda.pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward gerar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		
		List<PedidoTO> pedidos = null;
		ResultadoPaginacao<PedidoTO> resultado = (ResultadoPaginacao<PedidoTO>) request
				.getSession().getAttribute(Constantes.RESULTADO_PAGINACAO);
		if (resultado != null) {
			// Refazendo a consulta para recuperar os registros de todas as
			// p�gina e n�o apenas da p�gina atual.
			ResultadoPaginacao resultadoCompleto = Fachada.getInstance()
					.consultarPedidoPaginado(resultado.getParametrosDaConsulta(), 1, resultado.getTotalRegistros() + 1);					
			if (resultadoCompleto != null) {
				pedidos = resultadoCompleto.getResult();
			}
		}
		if (pedidos != null && !pedidos.isEmpty()) {
			List<PedidoRelatorioTO> pedidosRelatorio = new ArrayList<PedidoRelatorioTO>();
			for (PedidoTO pedido : pedidos) {
				PedidoRelatorioTO pedidoRelatorio = new PedidoRelatorioTO();
				pedidoRelatorio.setCodigo(pedido.getIdPedido().toString());
				pedidoRelatorio.setNomeFornecedor(pedido.getFornecedor().getNome());
				pedidoRelatorio.setData(Formatador.converterDataString(pedido.getDataPedido(), Formatador.FORMATO_DATA_PADRAO));
				pedidoRelatorio.setDesconto(pedido.getDesconto() != null ? pedido.getDesconto().doubleValue() : 0);				
				pedidoRelatorio.setSubTotal(pedido.getTotalSemDesconto() != null ? pedido.getTotalSemDesconto().doubleValue() : 0);
				pedidoRelatorio.setTotal(pedido.getTotalComDesconto() != null ? pedido.getTotalComDesconto().doubleValue() : 0);
				pedidoRelatorio.setProdutoString(pedido.getProdutoString());
				pedidoRelatorio.setFinalizado(pedido.getFinalizadoFormatado());
				pedidosRelatorio.add(pedidoRelatorio);
			}

			try {
				// par�metros
				Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
				parametrosRelatorio.put("horaAtual", Formatador.getHoraAtual());

				// Na variavel pathJasper ficara o caminho do diret�rio para
				// os relat�rios compilados (.jasper)
				String pathJasper = request.getSession().getServletContext()
						.getRealPath("/relatorios");
				parametrosRelatorio.put("pathReports", pathJasper
						+ File.separator);

				// A variavel path armazena o caminho real para o contexto
				// isso � util pois o seu web container pode estar instalado em
				// lugares diferentes
				String path = request.getSession().getServletContext()
						.getRealPath("/");
				
				String pathImages = request.getSession().getServletContext()
				.getRealPath("/images");
				parametrosRelatorio.put("pathImages", pathImages);

				JasperPrint jprint = null;
				jprint = JasperFillManager.fillReport(pathJasper
						+ File.separator + "relatoriolistapedidos.jasper",
						parametrosRelatorio, new JRBeanCollectionDataSource(
								pedidosRelatorio));

				this.gerarSaidaPDF(response, jprint, "ListaDePedidos");

				// Grava o relat�rio em disco em pdf
				// JasperExportManager.exportReportToPdfFile(jprint, path +
				// "/relatoriolistavendas.pdf");

				// Redireciona para o pdf gerado
				// response.sendRedirect("relatoriolistavendas.pdf");

			} catch (JRException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
