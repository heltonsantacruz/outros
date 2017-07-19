package br.com.granit.apresentacao.produto;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import sun.util.calendar.CalendarUtils;

import br.com.granit.apresentacao.PrincipalAction;
import br.com.granit.apresentacao.venda.VendaRelatorioTO;
import br.com.granit.controlador.ControladorFornecedor;
import br.com.granit.controlador.ControladorHistoricoProduto;
import br.com.granit.controlador.ControladorProduto;
import br.com.granit.controlador.ControladorTipoProduto;
import br.com.granit.fachada.Fachada;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAQueryException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.filtro.FiltroHistoricoProduto;
import br.com.granit.persistencia.to.FornecedorTO;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.ProdutoFornecedorTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.persistencia.to.SubTipoProdutoTO;
import br.com.granit.persistencia.to.TipoProdutoTO;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.persistencia.to.VendaTO;
import br.com.granit.util.CalendarUtil;
import br.com.granit.util.Constantes;
import br.com.granit.util.CurrencyUtil;
import br.com.granit.util.Formatador;
import br.com.granit.util.JPAUtility;
import br.com.granit.util.ResultadoPaginacao;

public class ProdutoAction extends PrincipalAction {

	private static final String ID_PRODUTO = "idProduto";
	private static final String PRODUTO_TO = "produtoTO";
	private static final String PRODUTO_FORM = "produtoForm";
	private static final String CODIGO = "codigo";
	private static final String DESCRICAO = "descricao";

	public ActionForward abrir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ProdutoForm produtoForm = new ProdutoForm();
		produtoForm.setQuantidadeEstoque(Constantes.VALOR_PADRAO_REAL_4_CASAS);
		request.setAttribute(PRODUTO_FORM, produtoForm);
		request.getSession().setAttribute("fornecedoresCadastrados",
				getFornecedoresCadastrados());
		request.getSession().setAttribute("tipos", getTipos());
		request.getSession().setAttribute("subtipos", new ArrayList());
		ProdutoTO produto = new ProdutoTO();
		produto.setFornecedores(new ArrayList<ProdutoFornecedorTO>());
		request.getSession().setAttribute("produto", produto);
		return (mapping.findForward(Constantes.FORWARD_ABRIR));
	}

	private Object getTipos() {
		return ControladorTipoProduto.getInstance().listar();
	}

	private List<FornecedorTO> getFornecedoresCadastrados() {
		return ControladorFornecedor.getInstance().listar();
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return (mapping.findForward(Constantes.FORWARD_PAGINA_INICIAL));
	}

	public ActionForward adicionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("ProdutoAction.adicionar()");

		ProdutoForm produtoForm = (ProdutoForm) form;
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");

		String camposObrigatorios = verificaObrigatorios(produtoForm);
		if (!camposObrigatorios.equals("")) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					camposObrigatorios);
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}
		double quant = verificaQuantidade(produtoForm);
		if (quant < 0) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					"Quantidade em Estoque");
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return (mapping.findForward(Constantes.FORWARD_ABRIR));
		}

		copiaPropriedades(produtoForm, produto);
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put(DESCRICAO, produto.getDescricao().toUpperCase());
			parametros.put("tipo", produto.getTipo());
			parametros.put("idSubTipo", produto.getSubTipo().getIdSubTipo());		
			if (!Fachada.getInstance().consultarProduto(parametros).isEmpty()) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"Já existe um produto cadastrado com essa descrição.");
				request.setAttribute("fornecedoresAssociados", produto
						.getFornecedores());
				return (mapping.findForward(Constantes.FORWARD_ABRIR));
			}
			UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
					Constantes.USUARIO_LOGADO);
			produto = Fachada.getInstance().salvaProduto(produto);
			HistoricoProdutoTO historico = new HistoricoProdutoTO();
			historico.setUsuario(usuario);
			historico.setProduto(produto);
			historico.setQuantidade(new BigDecimal(produto.getQtdEstoque()));
			historico.setTipo(Constantes.TIPO_ENTRADA_PRODUTO);
			historico.setObservacao(Constantes.OBS6.replaceFirst("usuarioX", recuperaUsuarioLogado(request).getLogin()+""));			
			ControladorHistoricoProduto.getInstance().salva(historico);				
		} catch (JPAInsertException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		request.setAttribute(Constantes.TIPO_FECHAR,
				Constantes.FORWARD_ADICIONAR);
		request.setAttribute(ID_PRODUTO, produto.getIdProduto() + "");
		adicionaMensagem(request, Constantes.INCLUSAO_SUCESSO);

		List<HistoricoProdutoTO> listaHistorico = produto
				.getHistoricosEstoque();
		if (listaHistorico == null) {
			listaHistorico = new ArrayList<HistoricoProdutoTO>();
		}
		request.setAttribute("listaHistoricoEntradasSaidas", listaHistorico);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());

		return exibir(mapping, produtoForm, request, response);
	}

	public ActionForward abrirConsulta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("ProdutoAction.abrirConsulta()");

		ProdutoForm formulario = (ProdutoForm) form;
		formulario.reset();
		request.getSession().setAttribute("tipos", getTipos());
		request.getSession().setAttribute("subtipos", new ArrayList());
		List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto();

		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.setAttribute(PRODUTO_FORM, formulario);

		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}
	
	private Object getTiposInsumosDiversos() {
		TipoProdutoTO tp1 = new TipoProdutoTO();
		tp1.setDescricao("Insumos");
		tp1.setIdTipo(2);
		TipoProdutoTO tp2 = new TipoProdutoTO();
		tp2.setDescricao("Diversos");
		tp2.setIdTipo(3);
		List lista = new ArrayList<TipoProdutoTO>();
		lista.add(tp1);	
		lista.add(tp2);	
		return lista;
	}

	public ActionForward abrirRegistrarEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoForm formulario = (ProdutoForm) form;
		formulario.reset();

		//request.getSession().setAttribute("tipos", getTipos());
		request.getSession().setAttribute("tipos", getTiposInsumosDiversos());
		request.getSession().setAttribute("subtipos", new ArrayList());
		//  CONSULTAR APENAS INSUMOS
		List<ProdutoTO> produtos = Fachada.getInstance().consultarProdutoTipo(2);//tipo insumo

		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.getSession().setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA,
				produtos);
		request.setAttribute(PRODUTO_FORM, formulario);
		// Primeira tela para selecionar os INSUMOS os quais serao registradas
		// as saÃ­das dos INSUMOS!!
		return (mapping.findForward("registroEntradaSaidaProdutosPasso1"));
	}

	public ActionForward consultaRegistrarEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws JPAQueryException {
		ProdutoForm myForm = (ProdutoForm) form;
		Map<String, Object> parametros = new HashMap<String, Object>();

		if (!GenericValidator.isBlankOrNull(myForm.getIdProduto())) {
			try {
				Integer.parseInt(myForm.getIdProduto());
			} catch (Exception e) {
				adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, CODIGO);
				return (mapping.findForward(Constantes.FORWARD_CONSULTA));
			}
		}
		if (!GenericValidator.isBlankOrNull(myForm.getDescricao())) {
			parametros.put(DESCRICAO, '%' + myForm.getDescricao() + "%");
		}
		if (!GenericValidator.isBlankOrNull(myForm.getTipo())) {
			parametros.put("subTipo.tipo.idTipo", Integer.valueOf(myForm
					.getTipo()));
		}
		else{
			parametros.put("subTipo.tipo.idTipo", Integer.valueOf("2"));//apenas insumos
		}
		if (!GenericValidator.isBlankOrNull(myForm.getSubTipo())) {
			parametros.put("subTipo.idSubTipo", Integer.valueOf(myForm
					.getSubTipo()));
		}

		List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto(
				parametros);

		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.getSession().setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA,
				produtos);
		// Primeira tela para selecionar os INSUMOS os quais serao registradas
		// as saÃ­das dos INSUMOS!!
		return (mapping.findForward("registroEntradaSaidaProdutosPasso1"));
	}

	@SuppressWarnings("unchecked")
	public ActionForward registrarEntradaSaidaPasso2(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		List<ProdutoTO> produtos = (List<ProdutoTO>) request.getSession()
				.getAttribute(Constantes.LISTA_PRODUTOS_CONSULTA);
		List<ProdutoTO> produtosSelecionados = new ArrayList<ProdutoTO>();
		if (produtos == null || produtos.size() == 0) {
			adicionaErro(request, "erro.nenhum.produto.selecionado");
			request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
			return (mapping.findForward("registroEntradaSaidaProdutosPasso1"));
		}
		for (int i = 0; i < produtos.size(); i++) {
			if (request.getParameter("produto" + i) != null) {
				produtosSelecionados.add(produtos.get(i));
			}
		}
		if (produtosSelecionados.isEmpty()) {
			adicionaErro(request, "erro.nenhum.produto.selecionado");
			request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
			return (mapping.findForward("registroEntradaSaidaProdutosPasso1"));
		}
		// RECUPERAR PRODUTOS SELECIONADOS
		// COLOCAR OS PRODUTOS NA SESSAO
		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA,
				produtosSelecionados);
		request.getSession().setAttribute(
				Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA,
				produtosSelecionados);
		return (mapping.findForward("registroEntradaSaidaProdutosPasso2"));
	}

	@SuppressWarnings("unchecked")
	public ActionForward finalizarRegistrarEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		List<ProdutoTO> produtosSelecionados = (List<ProdutoTO>) request
				.getSession().getAttribute(
						Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA);
		List<Double> quantidades = new ArrayList<Double>();
		List<String> quantidadesSaidas = new ArrayList<String>();
		for (int i = 0; i < produtosSelecionados.size(); i++) {
			String quantidade = request.getParameter("quantidade" + i);
			quantidadesSaidas.add(quantidade);
		}
		request.setAttribute("quantidadesSaidas", quantidadesSaidas);
		for (int i = 0; i < produtosSelecionados.size(); i++) {
			String quantidade = request.getParameter("quantidade" + i);
			double quant = verificaQuantidade(quantidade);
			if (quant <= 0.0) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"Quantidade do Produto <"
								+ produtosSelecionados.get(i).getDescricao()
								+ ">");
				request.setAttribute(
						Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA,
						produtosSelecionados);
				request.setAttribute("exibirErro", "true");
				return (mapping
						.findForward("registroEntradaSaidaProdutosPasso2"));
			}
			quantidades.add(quant);
		}

		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			for (int i = 0; i < produtosSelecionados.size(); i++) {
				ProdutoTO produto = ControladorProduto.getInstance().get(
						produtosSelecionados.get(i).getIdProduto());

				Double quantidade = quantidades.get(i);

				if (produto.getQtdEstoque() < quantidade) {
					adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
							"Quantidade do Produto <"
									+ produtosSelecionados.get(i)
											.getDescricao()
									+ "> excede a quantidade atual em Estoque.");
					JPAUtility.rollbackTransactionGroup(em);
					JPAUtility.setTransacoesAtivadas(true);
					request.setAttribute(
							Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA,
							produtosSelecionados);
					request.setAttribute("exibirErro", "true");
					return (mapping
							.findForward("registroEntradaSaidaProdutosPasso2"));
				} else {
					produto.setQtdEstoque(produto.getQtdEstoque() - quantidade);
				}
				UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
						Constantes.USUARIO_LOGADO);

				HistoricoProdutoTO historico = new HistoricoProdutoTO();
				historico.setUsuario(usuario);
				historico.setProduto(produto);
				historico.setQuantidade(new BigDecimal(quantidade));
				historico.setTipo(Constantes.TIPO_SAIDA_PRODUTO);
				historico.setObservacao("Registro feito pelo usuário "
						+ recuperaUsuarioLogado(request).getLogin());
				ControladorHistoricoProduto.getInstance().salva(historico);
				Fachada.getInstance().alterarProduto(produto);
			}
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			request.setAttribute(
					Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA,
					produtosSelecionados);
			request.setAttribute("exibirErro", "true");
			mapping.findForward("registroEntradaSaidaProdutosPasso2");
		} catch (JPAInsertException e) {
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			request.setAttribute(
					Constantes.LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA,
					produtosSelecionados);
			request.setAttribute("exibirErro", "true");
			mapping.findForward("registroEntradaSaidaProdutosPasso2");
		}
		adicionaMensagem(request, "saidas.registradas.sucesso");
		return abrirRegistrarEntradaSaida(mapping, form, request, response);
	}

	public ActionForward filtrarConsulta(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws JPAQueryException {

		ProdutoForm myForm = (ProdutoForm) form;
		Map<String, Object> parametros = new HashMap<String, Object>();

		if (!GenericValidator.isBlankOrNull(myForm.getIdProduto())) {
			try {
				Integer.parseInt(myForm.getIdProduto());
			} catch (Exception e) {
				adicionaErro(request, Constantes.MSG_CAMPOS_INVALIDOS, CODIGO);
				return (mapping.findForward(Constantes.FORWARD_CONSULTA));
			}
		}
		if (!GenericValidator.isBlankOrNull(myForm.getDescricao())) {
			parametros.put(DESCRICAO, '%' + myForm.getDescricao().toUpperCase() + "%");
		}
		if (!GenericValidator.isBlankOrNull(myForm.getTipo())) {
			parametros.put("subTipo.tipo.idTipo", Integer.valueOf(myForm
					.getTipo()));
		}
		if (!GenericValidator.isBlankOrNull(myForm.getSubTipo())) {
			parametros.put("subTipo.idSubTipo", Integer.valueOf(myForm
					.getSubTipo()));
		}

		List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto(
				parametros);
		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		return (mapping.findForward(Constantes.FORWARD_CONSULTA));
	}

	public ActionForward exibir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ProdutoForm myForm = (ProdutoForm) form;
		System.out.println("ProdutoAction.exibir()");

		String id = request.getParameter(ID_PRODUTO);
		if (id == null) {
			id = (String) request.getAttribute(ID_PRODUTO);
		}

		ProdutoTO produto = Fachada.getInstance().getProduto(new Integer(id));
		if (produto == null) {
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		myForm.reset(mapping, request);
		request.setAttribute(PRODUTO_TO, produto);

		String tipoFechar = (String) request
				.getAttribute(Constantes.TIPO_FECHAR);
		if (tipoFechar == null) {
			request.setAttribute(Constantes.TIPO_FECHAR,
					Constantes.FORWARD_EXIBIR);
		} else {
			request.setAttribute(Constantes.TIPO_FECHAR, tipoFechar);
		}
		//List<HistoricoProdutoTO> listaHistorico = produto.getHistoricosEstoque();

		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put(ID_PRODUTO, produto.getIdProduto());
		
		List<HistoricoProdutoTO> listaHistorico = Fachada.getInstance().consultarHistoricoProduto(parametros);
		
		if (listaHistorico == null) {
			listaHistorico = new ArrayList<HistoricoProdutoTO>();
		}
		else{
			if(listaHistorico.size() > 100){
				listaHistorico = listaHistorico.subList(0, 100);
			}
		}
		request.setAttribute("listaHistoricoEntradasSaidas", listaHistorico);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());
		return (mapping.findForward(Constantes.FORWARD_EXIBIR));
	}

	public ActionForward carregarSubTiposIncluir(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");
		ProdutoForm myForm = (ProdutoForm) form;
		request.getSession().setAttribute("subtipos",
				getSubTipos(myForm.getTipo()));
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}
	
	public ActionForward relatorios(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoForm myForm = (ProdutoForm) form;
		myForm.setAno(Calendar.getInstance().get(Calendar.YEAR) + "");
		myForm.setMes((Calendar.getInstance().get(Calendar.MONTH) + 1) + "");
		return mapping.findForward("relatorios");
	}

	public ActionForward carregarSubTiposAlterar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");
		ProdutoForm myForm = (ProdutoForm) form;
		request.getSession().setAttribute("subtipos",
				getSubTipos(myForm.getTipo()));
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());
		return mapping.findForward(Constantes.FORWARD_EDITAR);
	}

	public ActionForward carregarSubTiposConsulta(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoForm myForm = (ProdutoForm) form;
		List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto();
		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.getSession().setAttribute("subtipos",
				getSubTipos(myForm.getTipo()));
		return mapping.findForward(Constantes.FORWARD_CONSULTA);
	}

	public ActionForward carregarSubTiposConsultaRegistroSaidas(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoForm myForm = (ProdutoForm) form;
		List<ProdutoTO> produtos = Fachada.getInstance().consultarProdutoTipo(2);//tipo insumo
		//List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto();
		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.getSession().setAttribute("subtipos",
				getSubTipos(myForm.getTipo()));
		return mapping.findForward("registroEntradaSaidaProdutosPasso1");
	}
	
	public ActionForward carregarSubTiposConsultaRelatorioEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoForm myForm = (ProdutoForm) form;		
		request.getSession().setAttribute("subtipos",
				getSubTipos(myForm.getTipo()));
		return mapping.findForward(Constantes.FORWARD_CONSULTA_RELATORIO_ENTRADA_SAIDA);
	}

	private List<SubTipoProdutoTO> getSubTipos(String tipo) {
		if (GenericValidator.isBlankOrNull(tipo))
			return new ArrayList<SubTipoProdutoTO>();

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("tipo", new Integer(tipo));
		return Fachada.getInstance().consultarSubTipoPorTipo(parametros);

	}

	/**
	 * Retorna >= 0 se o valor for maior igual a zero e -1 caso contrário
	 * 
	 * @param produtoForm
	 * @return
	 */
	private double verificaQuantidade(ProdutoForm produtoForm) {
		double qt = 0;
		try {
			qt = CurrencyUtil.getNumberFormatRealQuatroCasas(produtoForm.getQuantidadeEstoque());
//			qt = CurrencyUtil.getNumberFormatReal(produtoForm
//					.getQuantidadeEstoque());
		} catch (Exception e) {
			return -1;
		}
		return qt;
	}

	/**
	 * Retorna >= 0 se o valor for maior igual a zero e -1 caso contrário
	 * 
	 * @param produtoForm
	 * @return
	 */
	private double verificaQuantidade(String quantidade) {
		double qt = 0;
		try {
			qt = CurrencyUtil.getNumberFormatRealQuatroCasas(quantidade);
		} catch (Exception e) {
			return -1;
		}
		return qt;
	}

	public ActionForward fechar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String operacao = request.getParameter(Constantes.TIPO_FECHAR);
		if (operacao.equals(Constantes.FORWARD_EXIBIR)) {
			return abrirConsulta(mapping, form, request, response);
		}
		return (mapping.findForward(Constantes.FORWARD_INDEX));
	}

	private String verificaObrigatorios(ProdutoForm form) {
		String obrigatorios = "";
		if (form.getDescricao().equals("")) {
			if (obrigatorios.length() > 0) {
				obrigatorios += ", ";
			}
			obrigatorios += "Descrição";
		}
		if (form.getTipo().equals("")) {
			if (obrigatorios.length() > 0) {
				obrigatorios += ", ";
			}
			obrigatorios += "Tipo";
		} else {
			if (form.getTipo().equals("1")) {// matéria prima
				if (form.getTipoMateriaPrima() == null
						|| form.getTipoMateriaPrima().equals("")) {
					if (obrigatorios.length() > 0) {
						obrigatorios += ", ";
					}
					obrigatorios += "Tipo Matéria Prima";
				}
			}
		}
		if (form.getSubTipo().equals("")) {
			if (obrigatorios.length() > 0) {
				obrigatorios += ", ";
			}
			obrigatorios += "SubTipo";
		}
		return obrigatorios;
	}

	private String verificaObrigatoriosEntradaSaida(ProdutoForm form) {
		String obrigatorios = "";
		if (form.getDescricao().equals("")) {
			if (obrigatorios.length() > 0) {
				obrigatorios += ", ";
			}
			obrigatorios += "Descrição:";
		}
		if (form.getQuantidadeEntradaSaida().equals("")
				|| Formatador.parseDouble(form.getQuantidadeEntradaSaida()) == 0.0) {
			if (obrigatorios.length() > 0) {
				obrigatorios += ", ";
			}
			obrigatorios += "Quantidade";
		}
		if (form.getTipoEntradaSaida().equals("")) {
			if (obrigatorios.length() > 0) {
				obrigatorios += ", ";
			}
			obrigatorios += "Tipo";
		}
		return obrigatorios;
	}

	private void copiaPropriedades(ProdutoForm myForm, ProdutoTO produto)
			throws Exception {
		if (!GenericValidator.isBlankOrNull(myForm.getIdProduto())) {
			produto.setIdProduto(Integer.parseInt(myForm.getIdProduto()));
		}
		produto.setDescricao(myForm.getDescricao().toUpperCase());
		BigDecimal qt = new BigDecimal(new Double(formataDouble(myForm.getQuantidadeEstoque())));		
		produto.setQtdEstoque(qt.doubleValue());
		produto.setSubTipo(getSubTipo(new Integer(myForm.getSubTipo())));
		produto.setTipo(getTipoMateriaPrimaChar(myForm.getTipoMateriaPrima()));
	}

	private Character getTipoMateriaPrimaChar(String tipoMateriaPrima) {
		if (tipoMateriaPrima == null || tipoMateriaPrima.equals("")) {
			return null;
		}
		if (tipoMateriaPrima.equals("1")) {
			return 'C';
		} else if (tipoMateriaPrima.equals("2")) {
			return 'L';
		}
		return null;
	}

	private String getTipoMateriaPrimaCodigo(Character tipoMateriaPrima) {
		if (tipoMateriaPrima == null || tipoMateriaPrima.equals("")) {
			return null;
		}
		if (tipoMateriaPrima.equals('C')) {
			return "1";
		} else if (tipoMateriaPrima.equals('L')) {
			return "2";
		}
		return null;
	}

	private SubTipoProdutoTO getSubTipo(Integer idSubTipo) {
		return Fachada.getInstance().getSubTipo(idSubTipo);
	}

	public ActionForward processarExcluir(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoForm myForm = (ProdutoForm) form;
		String idProduto = request.getParameter(ID_PRODUTO);
		if (idProduto == null || idProduto.isEmpty()) {
			idProduto = myForm.getIdProduto();
		}
		ProdutoTO produto = Fachada.getInstance().getProduto(
				new Integer(idProduto));
		if (produto == null) {
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		try {
			if (produto.getFornecedores() != null
					&& !produto.getFornecedores().isEmpty()) {
				adicionaErro(request, "MSG14");
				return abrirConsulta(mapping, myForm, request, response);
			}
			Fachada.getInstance().removeProduto(produto);
		} catch (JPADeleteException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return mapping.findForward(Constantes.FORWARD_CONSULTA);
		}
		myForm.reset();
		List<ProdutoTO> produtos = Fachada.getInstance().consultarProduto();
		request.setAttribute(Constantes.LISTA_PRODUTOS_CONSULTA, produtos);
		request.setAttribute(PRODUTO_FORM, myForm);
		adicionaMensagem(request, Constantes.EXCLUSAO_SUCESSO);
		return mapping.findForward(Constantes.FORWARD_CONSULTA);
	}

	public ActionForward iniciarEditar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ProdutoForm myForm = (ProdutoForm) form;

		String id = request.getParameter(ID_PRODUTO);
		if (id == null) {
			id = (String) request.getAttribute(ID_PRODUTO);
		}
		ProdutoTO produto = Fachada.getInstance().getProduto(new Integer(id));
		if (produto == null) {
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		} else {
			request.getSession().setAttribute("produto", produto);
			int i = 0;
			for (ProdutoFornecedorTO produtoFornecedor : produto
					.getFornecedores()) {
				produtoFornecedor.setPosicao(i++);
			}
			request.getSession().setAttribute("tipos", getTipos());
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			request.getSession().setAttribute("fornecedoresCadastrados",
					getFornecedoresCadastrados());
			carregaForm(produto, myForm);
			request.getSession().setAttribute("subtipos",
					getSubTipos(myForm.getTipo()));
		}
		return (mapping.findForward(Constantes.FORWARD_EDITAR));
	}

	public ActionForward iniciarRegistroEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ProdutoForm myForm = (ProdutoForm) form;

		String id = request.getParameter(ID_PRODUTO);
		if (id == null) {
			id = (String) request.getAttribute(ID_PRODUTO);
		}
		ProdutoTO produto = Fachada.getInstance().getProduto(new Integer(id));
		if (produto == null) {
			adicionaErro(request, Constantes.ERRO_SELECIONE_ENTIDADE);
			mapping.findForward(Constantes.FORWARD_CONSULTA);
		} else {
			carregaForm(produto, myForm);
		}
		return (mapping.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA));
	}

	private void carregaForm(ProdutoTO produto, ProdutoForm myForm) {
		myForm.setIdProduto(produto.getIdProduto() + "");
		myForm.setDescricao(produto.getDescricao());
		if (produto.getSubTipo() != null) {
			myForm.setTipo(String.valueOf(produto.getSubTipo().getTipo()
					.getIdTipo()));
			myForm.setTipoDescricao(produto.getTipoMateriaPrima());
			myForm.setSubTipo(String.valueOf(produto.getSubTipo()
					.getIdSubTipo()));
			myForm.setSubTipoDescricao(produto.getSubTipo().getDescricao());
			if (produto.getSubTipo().getTipo().getIdTipo().intValue() == 1) {// matéria prima
				myForm.setTipoMateriaPrima(getTipoMateriaPrimaCodigo(produto
						.getTipo()));
			}
		}
		String quant = produto.getQtdEstoqueFormatado();
		if (quant == null || quant.equals("")) {
			quant = "0,00";
		}
		myForm.setQuantidadeEstoque(quant);
	}

	public ActionForward atualizar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("ProdutoAction.atualizar()");

		ProdutoForm produtoForm = (ProdutoForm) form;
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");

		String camposObrigatorios = verificaObrigatorios(produtoForm);
		if (!camposObrigatorios.equals("")) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					camposObrigatorios);
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return (mapping.findForward(Constantes.FORWARD_EDITAR));
		}
		double quant = verificaQuantidade(produtoForm);
		double diferenca = quant - produto.getQtdEstoque();
		if (quant < 0) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					"Quantidade em Estoque");
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return (mapping.findForward(Constantes.FORWARD_EDITAR));
		}

		boolean descricaoMudou = !produto.getDescricao().equalsIgnoreCase(
				produtoForm.getDescricao());
		copiaPropriedades(produtoForm, produto);
		try {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put(DESCRICAO, produto.getDescricao().toUpperCase());
			parametros.put("tipo", produto.getTipo());
			parametros.put("idSubTipo", produto.getSubTipo().getIdSubTipo());
			if (descricaoMudou
					&& !Fachada.getInstance().consultarProduto(parametros)
							.isEmpty()) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"Já existe um produto cadastrado com essa descrição!");
				request.setAttribute("fornecedoresAssociados", produto
						.getFornecedores());
				return (mapping.findForward(Constantes.FORWARD_EDITAR));
			}	
			
			char tipo = Constantes.TIPO_ENTRADA_PRODUTO;
			if(diferenca < 0){
				tipo = Constantes.TIPO_SAIDA_PRODUTO;
			}
			if (produto.existeEntradaSaidaMaiorQueZero()){					
//04/05/2011 alteração para permissão de ajuste no estoque a partir da consulta
//				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
//						"O Produto já teve registro de entrada/saída. Para ajuste de estoque utiliza as funções de Pedido/Venda!");
//				request.setAttribute("fornecedoresAssociados", produto
//						.getFornecedores());
//				return (mapping.findForward(Constantes.FORWARD_EDITAR));		
				UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
						Constantes.USUARIO_LOGADO);
				Fachada.getInstance().alterarProduto(produto);
				HistoricoProdutoTO historico = new HistoricoProdutoTO();
				historico.setUsuario(usuario);
				historico.setProduto(produto);
				historico.setQuantidade(new BigDecimal(Math.abs(diferenca)));
				historico.setTipo(tipo);
				if(tipo == Constantes.TIPO_ENTRADA_PRODUTO){
					historico.setObservacao(Constantes.OBS8.replaceFirst("usuarioX", recuperaUsuarioLogado(request).getLogin()+""));
				}
				else{
					historico.setObservacao(Constantes.OBS9.replaceFirst("usuarioX", recuperaUsuarioLogado(request).getLogin()+""));
				}							
				ControladorHistoricoProduto.getInstance().salva(historico);
//04/05/2011 alteração para permissão de ajuste no estoque a partir da consulta				
			}	
			else{
				UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
						Constantes.USUARIO_LOGADO);
				Fachada.getInstance().alterarProduto(produto);
				HistoricoProdutoTO historico = new HistoricoProdutoTO();
				historico.setUsuario(usuario);
				historico.setProduto(produto);
				historico.setQuantidade(new BigDecimal(quant));
				historico.setTipo(Constantes.TIPO_ENTRADA_PRODUTO);
				historico.setObservacao(Constantes.OBS7.replaceFirst("usuarioX", recuperaUsuarioLogado(request).getLogin()+""));			
				ControladorHistoricoProduto.getInstance().salva(historico);
			}
			
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			mapping.findForward(Constantes.FORWARD_EDITAR);
		}
		produto = Fachada.getInstance().getProduto(
				Integer.parseInt(produtoForm.getIdProduto()));
		adicionaMensagem(request, Constantes.ALTERACAO_SUCESSO);
		request.setAttribute(Constantes.TIPO_FECHAR,
				Constantes.FORWARD_ADICIONAR);

		request.setAttribute(PRODUTO_TO, produto);
		List<HistoricoProdutoTO> listaHistorico = produto
				.getHistoricosEstoque();
		if (listaHistorico == null) {
			listaHistorico = new ArrayList<HistoricoProdutoTO>();
		}
		request.setAttribute("listaHistoricoEntradasSaidas", listaHistorico);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());

		return mapping.findForward(Constantes.FORWARD_EXIBIR);
	}

	

	public ActionForward registrarEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ProdutoForm produtoForm = (ProdutoForm) form;

		String camposObrigatorios = verificaObrigatoriosEntradaSaida(produtoForm);
		if (!camposObrigatorios.equals("")) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					camposObrigatorios);
			return (mapping
					.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA));
		}
		double quant = verificaQuantidade(produtoForm
				.getQuantidadeEntradaSaida());
		if (quant <= 0.0) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, "Quantidade");
			return (mapping
					.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA));
		}
		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			ProdutoTO produto = Fachada.getInstance().getProduto(
					new Integer(produtoForm.getIdProduto()));
			if (produto == null) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"Produto Inexistente!");
				JPAUtility.rollbackTransactionGroup(em);
				JPAUtility.setTransacoesAtivadas(true);
				return (mapping
						.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA));
			}
			if (produtoForm.getTipoEntradaSaida().charAt(0) == Constantes.TIPO_SAIDA_PRODUTO) {
				if (produto.getQtdEstoque() < quant) {
					adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
							"Quantidade excede a quantidade atual em Estoque.");
					JPAUtility.rollbackTransactionGroup(em);
					JPAUtility.setTransacoesAtivadas(true);
					return (mapping
							.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA));
				}
				produto.setQtdEstoque(produto.getQtdEstoque() - quant);
			} else {
				produto.setQtdEstoque(produto.getQtdEstoque() + quant);
			}
			HistoricoProdutoTO historico = new HistoricoProdutoTO();
			historico.setProduto(produto);
			UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
					Constantes.USUARIO_LOGADO);
			historico.setUsuario(usuario);
			historico.setObservacao(Constantes.OBS5.replaceFirst("usuarioX",
					usuario.getLogin()));
			historico.setQuantidade(new BigDecimal(quant));
			historico.setTipo(produtoForm.getTipoEntradaSaida().charAt(0));
			ControladorHistoricoProduto.getInstance().salva(historico);
			Fachada.getInstance().alterarProduto(produto);
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAUpdateException e) {
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			mapping.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA);
		} catch (JPAInsertException e) {
			adicionaErro(request, e.getMessage());
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			mapping.findForward(Constantes.FORWARD_REGISTRAR_ENTRADA_SAIDA);
		}
		adicionaMensagem(request, "MSG15");
		return abrirConsulta(mapping, produtoForm, request, response);
	}

	public ActionForward associarFornecedor(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ProdutoForm formulario = (ProdutoForm) form;
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");
		ProdutoFornecedorTO produtoFornecedor = null;

		if (GenericValidator.isBlankOrNull(formulario.getFornecedor())) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, "Fornecedor");
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}
		produtoFornecedor = new ProdutoFornecedorTO();
		produtoFornecedor.setFornecedor(Fachada.getInstance().getFornecedor(
				Integer.parseInt(formulario.getFornecedor())));
		produtoFornecedor.setPosicao(produto.getFornecedores().size());
		if (jaExisteFornecedorAssociado(produto.getFornecedores(),
				produtoFornecedor)) {
			adicionaErro(request, "label.produto.ja.associado");
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return mapping.findForward(Constantes.FORWARD_ABRIR);
		}

		produto.getFornecedores().add(produtoFornecedor);
		request.getSession().setAttribute("produto", produto);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());

		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}

	public ActionForward associarFornecedorAlteracao(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ProdutoForm formulario = (ProdutoForm) form;
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");
		ProdutoFornecedorTO produtoFornecedor = null;

		if (GenericValidator.isBlankOrNull(formulario.getFornecedor())) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS, "Fornecedor");
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return (mapping.findForward(Constantes.FORWARD_EDITAR));
		}
		produtoFornecedor = new ProdutoFornecedorTO();
		produtoFornecedor.setFornecedor(Fachada.getInstance().getFornecedor(
				Integer.parseInt(formulario.getFornecedor())));
		produtoFornecedor.setPosicao(produto.getFornecedores().size());
		if (jaExisteFornecedorAssociado(produto.getFornecedores(),
				produtoFornecedor)) {
			adicionaErro(request, "label.fornecedor.ja.associado");
			request.setAttribute("fornecedoresAssociados", produto
					.getFornecedores());
			return (mapping.findForward(Constantes.FORWARD_EDITAR));
		}

		produto.getFornecedores().add(produtoFornecedor);
		request.getSession().setAttribute("produto", produto);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());

		return (mapping.findForward(Constantes.FORWARD_EDITAR));
	}

	public ActionForward removerFornecedorAlteracao(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");
		Integer posicao = new Integer(request.getParameter("posicao"));

		if (produto != null && !produto.getFornecedores().isEmpty()) {

			if (fornecedorAssociadoCompra(posicao, produto.getFornecedores())) {
				adicionaErro(request, "label.fornecedor.associado.compra");
				request.setAttribute("fornecedoresAssociados", produto
						.getFornecedores());
				return (mapping.findForward(Constantes.FORWARD_EDITAR));
			}
			produto.getFornecedores().remove(posicao.intValue());
			int novaPosicao = 0;
			for (ProdutoFornecedorTO produtoFornecedor : produto
					.getFornecedores()) {
				produtoFornecedor.setPosicao(novaPosicao++);
			}
		}
		request.getSession().setAttribute("produto", produto);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());
		return (mapping.findForward(Constantes.FORWARD_EDITAR));
	}

	private boolean fornecedorAssociadoCompra(Integer posicao,
			List<ProdutoFornecedorTO> produtos) {
		ProdutoFornecedorTO pf = produtos.get(posicao);
		if (pf != null && pf.getDataUltimaCompra() != null) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public ActionForward removerFornecedor(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ProdutoTO produto = (ProdutoTO) request.getSession().getAttribute(
				"produto");
		Integer posicao = new Integer(request.getParameter("posicao"));

		if (produto != null && !produto.getFornecedores().isEmpty()) {
			produto.getFornecedores().remove(posicao.intValue());
			int novaPosicao = 0;
			for (ProdutoFornecedorTO produtoFornecedor : produto
					.getFornecedores()) {
				produtoFornecedor.setPosicao(novaPosicao++);
			}
		}
		request.getSession().setAttribute("produto", produto);
		request.setAttribute("fornecedoresAssociados", produto
				.getFornecedores());
		return mapping.findForward(Constantes.FORWARD_ABRIR);
	}

	private boolean jaExisteFornecedorAssociado(List fornecedoresAssociados,
			ProdutoFornecedorTO produtoFornecedor) {
		for (Iterator iterator = fornecedoresAssociados.iterator(); iterator
				.hasNext();) {
			ProdutoFornecedorTO prx = (ProdutoFornecedorTO) iterator.next();
			if (prx.getFornecedor().getIdFornecedor().equals(
					produtoFornecedor.getFornecedor().getIdFornecedor())) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public ActionForward gerarInsumoMesPorSemana(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		int ano = Integer.parseInt(request.getParameter("ano"));
		int mes = Integer.parseInt(request.getParameter("mes"));
		int tipo = Integer.parseInt(request.getParameter("tipo"));
		Map<String, Object> parametros = new HashMap<String, Object>();
		if(tipo == Constantes.TIPO_INSUMO){
			parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_INSUMO);
		}
		else if(tipo == Constantes.TIPO_DIVERSOS){
			parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_DIVERSOS);
		}
		
		List<ProdutoTO> produtos = ControladorProduto.getInstance().consultar(parametros);
		//TODO ordernar por subtipo!
		Collections.sort(produtos);
		String subTipo = "";
		if (produtos != null && !produtos.isEmpty()) {
			
			CalendarUtil.getInstance().defineMesAno(mes, ano);
			
			// parâmetros
			Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
			parametrosRelatorio.put("ano", ano + "");
			parametrosRelatorio.put("mes", mes + "");
			if(tipo == Constantes.TIPO_INSUMO){
				parametrosRelatorio.put("nomeRelatorio", "INSUMOS");
			}
			else if(tipo == Constantes.TIPO_DIVERSOS){
				parametrosRelatorio.put("nomeRelatorio", "DIVERSOS");
			}
					
			
			definirIntervalosDeSemanas(parametrosRelatorio);

			// Na variavel pathJasper ficara o caminho do diretório para
			// os relatórios compilados (.jasper)
			String pathJasper = request.getSession().getServletContext()
					.getRealPath("/relatorios");
			parametrosRelatorio.put("pathReports", pathJasper
					+ File.separator);

			// A variavel path armazena o caminho real para o contexto
			// isso é util pois o seu web container pode estar instalado em
			// lugares diferentes
			String path = request.getSession().getServletContext()
					.getRealPath("/");

			String pathImages = request.getSession().getServletContext()
					.getRealPath("/images");
			parametrosRelatorio.put("pathImages", pathImages);
			
			List<ProdutoRelatorioTO> produtosRelatorio = new ArrayList<ProdutoRelatorioTO>();
			for (ProdutoTO produto : produtos) {
				if (subTipo.equals("")){
					subTipo = produto.getSubTipo().getDescricao().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
					produtoRelatorioSubTipo.setDiscriminacao(subTipo);
					produtoRelatorioSubTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioSubTipo);
				}else{
					if (!subTipo.equalsIgnoreCase(produto.getSubTipo().getDescricao())){
						subTipo = produto.getSubTipo().getDescricao().toUpperCase();
						ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
						produtoRelatorioSubTipo.setDiscriminacao(subTipo);
						produtoRelatorioSubTipo.setSubTipo(true);
						produtosRelatorio.add(produtoRelatorioSubTipo);
					}
				}
				ProdutoRelatorioTO produtoRelatorio = new ProdutoRelatorioTO();
				produtoRelatorio.setSubTipo(false);
				produtoRelatorio.setDiscriminacao(produto.getDescricao());
				
				double semana1 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 1); 
				double semana2 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 2);
				double semana3 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 3);
				double semana4 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 4);
				
				//produtoRelatorio.setEstoqueSem1(Formatador.doubleValue(semana1));
				produtoRelatorio.setEstoqueSem1(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana1)+"")); 
				produtoRelatorio.setEstoqueSem2(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana2)+""));
				produtoRelatorio.setEstoqueSem3(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana3)+""));
				produtoRelatorio.setEstoqueSem4(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana4)+""));
				
				produtosRelatorio.add(produtoRelatorio);
			}

			try {
				JasperPrint jprint = null;
				jprint = JasperFillManager.fillReport(pathJasper
						+ File.separator + "insumoMesPorSemana.jasper",
						parametrosRelatorio, new JRBeanCollectionDataSource(
								produtosRelatorio));

				this.gerarSaidaPDF(response, jprint, "ListaDeInsumos");

				// Grava o relatório em disco em pdf
				// JasperExportManager.exportReportToPdfFile(jprint, path +
				// "/relatoriolistavendas.pdf");

				// Redireciona para o pdf gerado
				// response.sendRedirect("relatoriolistavendas.pdf");

			} catch (JRException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward gerarMateriaPrimaMesPorSemana(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		int ano = Integer.parseInt(request.getParameter("ano"));
		int mes = Integer.parseInt(request.getParameter("mes"));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_MATERIA_PRIMA);
		List<ProdutoTO> produtos = ControladorProduto.getInstance().consultar(parametros);
		//TODO ordernar por subtipo!
		Collections.sort(produtos);
		if (produtos != null && !produtos.isEmpty()) {
			
			CalendarUtil.getInstance().defineMesAno(mes, ano);
			
			// parâmetros
			Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
			parametrosRelatorio.put("ano", ano + "");
			parametrosRelatorio.put("mes", mes + "");
			
			definirIntervalosDeSemanas(parametrosRelatorio);

			// Na variavel pathJasper ficara o caminho do diretório para
			// os relatórios compilados (.jasper)
			String pathJasper = request.getSession().getServletContext()
					.getRealPath("/relatorios");
			parametrosRelatorio.put("pathReports", pathJasper
					+ File.separator);

			// A variavel path armazena o caminho real para o contexto
			// isso é util pois o seu web container pode estar instalado em
			// lugares diferentes
			String path = request.getSession().getServletContext()
					.getRealPath("/");

			String pathImages = request.getSession().getServletContext()
					.getRealPath("/images");
			parametrosRelatorio.put("pathImages", pathImages);
			
			List<ProdutoRelatorioTO> produtosRelatorio = new ArrayList<ProdutoRelatorioTO>();
			String tipo = "";
			String subTipo = "";
			for (ProdutoTO produto : produtos) {
				if (produto.getNomeTipoMateriaPrima().isEmpty()){
					continue;
				}
				if (tipo.equals("")){
					tipo = produto.getNomeTipoMateriaPrima().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioTipo = new ProdutoRelatorioTO();
					produtoRelatorioTipo.setDiscriminacao("** " + tipo + "S" + " **");
					produtoRelatorioTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioTipo);					
				}else if (!tipo.equalsIgnoreCase(produto.getNomeTipoMateriaPrima())){
					produtosRelatorio.add(linhaEmBranco());
					tipo = produto.getNomeTipoMateriaPrima().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioTipo = new ProdutoRelatorioTO();
					produtoRelatorioTipo.setDiscriminacao("** " + tipo + "S" + " **");
					produtoRelatorioTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioTipo);
				}
				if (subTipo.equals("")){
					subTipo = produto.getSubTipo().getDescricao().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
					produtoRelatorioSubTipo.setDiscriminacao(subTipo);
					produtoRelatorioSubTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioSubTipo);
				}else{
					if (!subTipo.equalsIgnoreCase(produto.getSubTipo().getDescricao())){
						subTipo = produto.getSubTipo().getDescricao().toUpperCase();
						ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
						produtoRelatorioSubTipo.setDiscriminacao(subTipo);
						produtoRelatorioSubTipo.setSubTipo(true);
						produtosRelatorio.add(produtoRelatorioSubTipo);
					}
				}
				ProdutoRelatorioTO produtoRelatorio = new ProdutoRelatorioTO();
				produtoRelatorio.setSubTipo(false);
				produtoRelatorio.setDiscriminacao(produto.getDescricao());
				
				double semana1 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 1); 
				double semana2 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 2);
				double semana3 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 3);
				double semana4 = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(
						produto.getIdProduto(), mes, ano, 4);
				
				produtoRelatorio.setEstoqueSem1(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana1)+""));
				produtoRelatorio.setEstoqueSem2(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana2)+""));
				produtoRelatorio.setEstoqueSem3(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana3)+""));
				produtoRelatorio.setEstoqueSem4(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(semana4)+""));
				
				produtosRelatorio.add(produtoRelatorio);
			}

			try {
				JasperPrint jprint = null;
				jprint = JasperFillManager.fillReport(pathJasper
						+ File.separator + "materiaPrimaMesPorSemana.jasper",
						parametrosRelatorio, new JRBeanCollectionDataSource(
								produtosRelatorio));

				this.gerarSaidaPDF(response, jprint, "ListaDeMateriaPrima");

				// Grava o relatório em disco em pdf
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

	private ProdutoRelatorioTO linhaEmBranco() {
		ProdutoRelatorioTO linhaEmBranco = new ProdutoRelatorioTO();
		linhaEmBranco.setDiscriminacao("");
		linhaEmBranco.setSubTipo(true);
		return linhaEmBranco;
	}
	
	public ActionForward gerarConsumoMateriaisMes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		int ano = Integer.parseInt(request.getParameter("ano"));
		int mes = Integer.parseInt(request.getParameter("mes"));
		int tipo = Integer.parseInt(request.getParameter("tipo"));
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		if(tipo == Constantes.TIPO_INSUMO){
			parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_INSUMO);
		}
		else if(tipo == Constantes.TIPO_DIVERSOS){
			parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_DIVERSOS);
		}		
		List<ProdutoTO> produtos = ControladorProduto.getInstance().consultar(parametros);
		//TODO ordernar por subtipo!
		Collections.sort(produtos);
		String subTipo = "";
		if (produtos != null && !produtos.isEmpty()) {
			
			CalendarUtil.getInstance().defineMesAno(mes, ano);
			
			// parâmetros
			Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
			parametrosRelatorio.put("ano", ano + "");
			parametrosRelatorio.put("mes", mes + "");
			if(tipo == Constantes.TIPO_INSUMO){
				parametrosRelatorio.put("nomeRelatorio", "(INSUMOS)");
			}
			else if(tipo == Constantes.TIPO_DIVERSOS){
				parametrosRelatorio.put("nomeRelatorio", "(DIVERSOS)");
			}
			
			definirIntervalosDeSemanas(parametrosRelatorio);

			// Na variavel pathJasper ficara o caminho do diretório para
			// os relatórios compilados (.jasper)
			String pathJasper = request.getSession().getServletContext()
					.getRealPath("/relatorios");
			parametrosRelatorio.put("pathReports", pathJasper
					+ File.separator);

			// A variavel path armazena o caminho real para o contexto
			// isso é util pois o seu web container pode estar instalado em
			// lugares diferentes
			String path = request.getSession().getServletContext()
					.getRealPath("/");

			String pathImages = request.getSession().getServletContext()
					.getRealPath("/images");
			parametrosRelatorio.put("pathImages", pathImages);
			
			List<ProdutoRelatorioTO> produtosRelatorio = new ArrayList<ProdutoRelatorioTO>();
			for (ProdutoTO produto : produtos) {
				if (subTipo.equals("")){
					subTipo = produto.getSubTipo().getDescricao().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
					produtoRelatorioSubTipo.setDiscriminacao(subTipo);
					produtoRelatorioSubTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioSubTipo);
				}else{
					if (!subTipo.equalsIgnoreCase(produto.getSubTipo().getDescricao())){
						subTipo = produto.getSubTipo().getDescricao().toUpperCase();
						ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
						produtoRelatorioSubTipo.setDiscriminacao(subTipo);
						produtoRelatorioSubTipo.setSubTipo(true);
						produtosRelatorio.add(produtoRelatorioSubTipo);
					}
				}
				ProdutoRelatorioTO produtoRelatorio = new ProdutoRelatorioTO();
				produtoRelatorio.setSubTipo(false);
				produtoRelatorio.setDiscriminacao(produto.getDescricao());
				
				//Calculando o estoque do mês anterior!!
				int mesAnt = mes - 1;
				int anoAnt = ano;
				if (mes == 1){
					mesAnt = 12;
					anoAnt = ano - 1;
				}
				CalendarUtil.getInstance().defineMesAno(mesAnt, anoAnt);
				double estoqueMesAnt = ControladorHistoricoProduto.getInstance()
						.recuperarEstoqueProdutoPorMes(produto.getIdProduto(),
								mesAnt, anoAnt); 
				//FIM - Calculando o estoque do mês anterior!!
				
				
				CalendarUtil.getInstance().defineMesAno(mes, ano);
				double estoqueMesAtual = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorMes(produto.getIdProduto(),
						mes, ano); 
				
				List<HistoricoProdutoTO> historicosMes = ControladorHistoricoProduto
						.getInstance().consultaPorMesToDebug(produto.getIdProduto(),
								mes, ano);
				String dataEnt = "";
				
				Date dateEnt = null;
				double totalEntradas = 0.0;
				for (HistoricoProdutoTO historicoProdutoTO : historicosMes) {
					if (historicoProdutoTO.getTipo() == 'E'){
						totalEntradas += historicoProdutoTO.getQuantidade().doubleValue();
						if (dateEnt == null || dateEnt.compareTo(historicoProdutoTO.getData()) < 0){
							dateEnt = historicoProdutoTO.getData();
						}
					}
				}				
				if (dateEnt != null)
					dataEnt = Formatador.converterDataString(dateEnt, Formatador.FORMATO_DATA_DIA_MES);

				
				double estoqueAcumulado = estoqueMesAnt + totalEntradas;
				
							
				produtoRelatorio.setEstoqueMesAnt(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(estoqueMesAnt)+""));
				produtoRelatorio.setDataEnt(dataEnt);
				produtoRelatorio.setQtdEntradas(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(totalEntradas)+""));
				produtoRelatorio.setEstoqueAcum(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(estoqueAcumulado)+""));
				produtoRelatorio.setEstoqueMesAtual(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(estoqueMesAtual)+""));
				
				double consumo = estoqueAcumulado - estoqueMesAtual;
				produtoRelatorio.setConsumo(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(consumo)+""));
				
				produtosRelatorio.add(produtoRelatorio);
			}

			try {
				JasperPrint jprint = null;
				jprint = JasperFillManager.fillReport(pathJasper
						+ File.separator + "consumoMateriaisMes.jasper",
						parametrosRelatorio, new JRBeanCollectionDataSource(
								produtosRelatorio));

				this.gerarSaidaPDF(response, jprint, "ConsumoDeMateriaisMes");

				// Grava o relatório em disco em pdf
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
	
	public ActionForward gerarConsumoMateriaPrimaMes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		int ano = Integer.parseInt(request.getParameter("ano"));
		int mes = Integer.parseInt(request.getParameter("mes"));
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_MATERIA_PRIMA);
		List<ProdutoTO> produtos = ControladorProduto.getInstance().consultar(parametros);
		//TODO ordernar por subtipo!
		Collections.sort(produtos);
		
		if (produtos != null && !produtos.isEmpty()) {
			
			CalendarUtil.getInstance().defineMesAno(mes, ano);
			
			// parâmetros
			Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
			parametrosRelatorio.put("ano", ano + "");
			parametrosRelatorio.put("mes", mes + "");
			
			definirIntervalosDeSemanas(parametrosRelatorio);

			// Na variavel pathJasper ficara o caminho do diretório para
			// os relatórios compilados (.jasper)
			String pathJasper = request.getSession().getServletContext()
					.getRealPath("/relatorios");
			parametrosRelatorio.put("pathReports", pathJasper
					+ File.separator);

			// A variavel path armazena o caminho real para o contexto
			// isso é util pois o seu web container pode estar instalado em
			// lugares diferentes
			String path = request.getSession().getServletContext()
					.getRealPath("/");

			String pathImages = request.getSession().getServletContext()
					.getRealPath("/images");
			parametrosRelatorio.put("pathImages", pathImages);

			String subTipo = "";
			String tipo = "";
			List<ProdutoRelatorioTO> produtosRelatorio = new ArrayList<ProdutoRelatorioTO>();
			for (ProdutoTO produto : produtos) {
				if (produto.getNomeTipoMateriaPrima().isEmpty()){
					continue;
				}
				if (tipo.equals("")){
					tipo = produto.getNomeTipoMateriaPrima().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioTipo = new ProdutoRelatorioTO();
					produtoRelatorioTipo.setDiscriminacao("** " + tipo + "S" + " **");
					produtoRelatorioTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioTipo);					
				}else if (!tipo.equalsIgnoreCase(produto.getNomeTipoMateriaPrima())){
					produtosRelatorio.add(linhaEmBranco());
					tipo = produto.getNomeTipoMateriaPrima().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioTipo = new ProdutoRelatorioTO();
					produtoRelatorioTipo.setDiscriminacao("** " + tipo + "S" + " **");
					produtoRelatorioTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioTipo);
				}
				if (subTipo.equals("")){
					subTipo = produto.getSubTipo().getDescricao().toUpperCase();
					ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
					produtoRelatorioSubTipo.setDiscriminacao(subTipo);
					produtoRelatorioSubTipo.setSubTipo(true);
					produtosRelatorio.add(produtoRelatorioSubTipo);
				}else{
					if (!subTipo.equalsIgnoreCase(produto.getSubTipo().getDescricao())){
						subTipo = produto.getSubTipo().getDescricao().toUpperCase();
						ProdutoRelatorioTO produtoRelatorioSubTipo = new ProdutoRelatorioTO();
						produtoRelatorioSubTipo.setDiscriminacao(subTipo);
						produtoRelatorioSubTipo.setSubTipo(true);
						produtosRelatorio.add(produtoRelatorioSubTipo);
					}
				}
				ProdutoRelatorioTO produtoRelatorio = new ProdutoRelatorioTO();
				produtoRelatorio.setSubTipo(false);
				produtoRelatorio.setDiscriminacao(produto.getDescricao());
				
				//Calculando o estoque do mês anterior!!
				int mesAnt = mes - 1;
				int anoAnt = ano;
				if (mes == 1){
					mesAnt = 12;
					anoAnt = ano - 1;
				}
				CalendarUtil.getInstance().defineMesAno(mesAnt, anoAnt);
				double estoqueMesAnt = ControladorHistoricoProduto.getInstance()
						.recuperarEstoqueProdutoPorMes(produto.getIdProduto(),
								mesAnt, anoAnt); 
				//FIM - Calculando o estoque do mês anterior!!
				
				
				CalendarUtil.getInstance().defineMesAno(mes, ano);
				double estoqueMesAtual = ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorMes(produto.getIdProduto(),
						mes, ano); 
				
				List<HistoricoProdutoTO> historicosMes = ControladorHistoricoProduto
						.getInstance().consultaPorMesToDebug(produto.getIdProduto(),
								mes, ano);
				String dataEnt = "";
				String estoqueEnt = "";
				Date dateEnt = null;
				double totalEntradas = 0.0;
				for (HistoricoProdutoTO historicoProdutoTO : historicosMes) {
					if (historicoProdutoTO.getTipo() == 'E'){
						totalEntradas += historicoProdutoTO.getQuantidade().doubleValue();
						if (dateEnt == null || dateEnt.compareTo(historicoProdutoTO.getData()) < 0){
							dateEnt = historicoProdutoTO.getData();
						}
					}
				}				
				if (dateEnt != null)
					dataEnt = Formatador.converterDataString(dateEnt, Formatador.FORMATO_DATA_DIA_MES);
				if (totalEntradas > 0.0)
					estoqueEnt = CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(totalEntradas)+"");
				
				double estoqueAcumulado = estoqueMesAnt + totalEntradas;
				String estoqueAcum = CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(estoqueAcumulado)+"");
				
				produtoRelatorio.setEstoqueMesAnt(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(estoqueMesAnt)+""));
				produtoRelatorio.setDataEnt(dataEnt);
				produtoRelatorio.setQtdEntradas(estoqueEnt);
				produtoRelatorio.setEstoqueAcum(estoqueAcum);
				produtoRelatorio.setEstoqueMesAtual(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(estoqueMesAtual)+""));
				
				double consumo = estoqueAcumulado - estoqueMesAtual;
				produtoRelatorio.setConsumo(CurrencyUtil.getNumberFormatStringQuatroCasas(new BigDecimal(consumo)+""));
				
				produtosRelatorio.add(produtoRelatorio);
			}

			try {
				JasperPrint jprint = null;
				jprint = JasperFillManager.fillReport(pathJasper
						+ File.separator + "consumoMateriaPrimaMes.jasper",
						parametrosRelatorio, new JRBeanCollectionDataSource(
								produtosRelatorio));

				this.gerarSaidaPDF(response, jprint, "ConsumoDeMateriaPrimaMes");

				// Grava o relatório em disco em pdf
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

	private void definirIntervalosDeSemanas(
			Map<String, Object> parametrosRelatorio) {
		String inicioSem1 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtInicioSemana1(),
				Formatador.FORMATO_DATA_PADRAO);
		String fimSem1 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtFimSemana1(),
				Formatador.FORMATO_DATA_PADRAO);
		String intervaloSem1 = inicioSem1 + " - " + fimSem1;
		parametrosRelatorio.put("intervSem1", intervaloSem1);
		
		String inicioSem2 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtInicioSemana2(),
				Formatador.FORMATO_DATA_PADRAO);
		String fimSem2 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtFimSemana2(),
				Formatador.FORMATO_DATA_PADRAO);
		String intervaloSem2 = inicioSem2 + " - " + fimSem2;
		parametrosRelatorio.put("intervSem2", intervaloSem2);
		
		String inicioSem3 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtInicioSemana3(),
				Formatador.FORMATO_DATA_PADRAO);
		String fimSem3 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtFimSemana3(),
				Formatador.FORMATO_DATA_PADRAO);
		String intervaloSem3 = inicioSem3 + " - " + fimSem3;
		parametrosRelatorio.put("intervSem3", intervaloSem3);
		
		String inicioSem4 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtInicioSemana4(),
				Formatador.FORMATO_DATA_PADRAO);
		String fimSem4 = Formatador.converterDataString(CalendarUtil
				.getInstance().getDtFimSemana4(),
				Formatador.FORMATO_DATA_PADRAO);
		String intervaloSem4 = inicioSem4 + " - " + fimSem4;
		parametrosRelatorio.put("intervSem4", intervaloSem4);
	}
	
	public ActionForward abrirConsultaRelatorioEntradaSaida(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("ProdutoAction.abrirConsultaRelatorioEntradaSaida()");

		ProdutoForm formulario = (ProdutoForm) form;
		formulario.reset();
		request.getSession().setAttribute("tipos", getTipos());
		request.getSession().setAttribute("subtipos", new ArrayList());
		request.setAttribute(PRODUTO_FORM, formulario);

		return (mapping.findForward(Constantes.FORWARD_CONSULTA_RELATORIO_ENTRADA_SAIDA));
	}
	
	public ActionForward filtrarConsultaRelatorioEntradaSaida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws JPAQueryException {

		ProdutoForm myForm = (ProdutoForm) form;
		Map<String, Object> parametros = new HashMap<String, Object>();

		if (!GenericValidator.isBlankOrNull(myForm.getDataInicio())) {
			try {
				parametros.put("dataInicio", Formatador
						.converterStringParaDate(myForm.getDataInicio(),
								Formatador.FORMATO_DATA_PADRAO));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!GenericValidator.isBlankOrNull(myForm.getDataFim())) {
			try {
				parametros.put("dataFim", Formatador
						.converterStringParaDate(myForm.getDataFim(),
								Formatador.FORMATO_DATA_PADRAO));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}		
		if (!GenericValidator.isBlankOrNull(myForm.getDescricao())) {
			parametros.put("produto.descricao", '%' + myForm.getDescricao().toUpperCase() + "%");
		}
		if (!GenericValidator.isBlankOrNull(myForm.getSubTipo())) {
			parametros.put("produto.subTipo.idSubTipo", Integer.valueOf(myForm
					.getSubTipo()));
		}
		else{
			if (!GenericValidator.isBlankOrNull(myForm.getTipo())) {
				parametros.put("produto.subTipo.tipo.idTipo", Integer.valueOf(myForm
						.getTipo()));
			}
		}
		
		

		List<HistoricoProdutoTO> historico = Fachada.getInstance().consultarHistoricoProduto(
				parametros);
//		FiltroHistoricoProduto f = new FiltroHistoricoProduto();
//		f.setTipo(Integer.valueOf(myForm.getTipo()));
//		f.setSubTipo(Integer.valueOf(myForm.getSubTipo()));
//		f.setDescricao(myForm.getDescricao().toUpperCase());
//		List<HistoricoProdutoTO> historico = Fachada.getInstance().consultaHistoricosProdutoPorFiltro(f);
		request.setAttribute(Constantes.LISTA_HISTORICO_PRODUTOS_CONSULTA, historico);
		return (mapping.findForward(Constantes.FORWARD_CONSULTA_RELATORIO_ENTRADA_SAIDA));
	}

	
	@Override
	protected String getTableId() {
		// TODO Auto-generated method stub
		return null;
	}

}
