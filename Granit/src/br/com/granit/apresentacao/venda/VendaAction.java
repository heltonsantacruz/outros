package br.com.granit.apresentacao.venda;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
import br.com.granit.controlador.ControladorItemBeneficiamentoVenda;
import br.com.granit.controlador.ControladorItemVenda;
import br.com.granit.controlador.ControladorProduto;
import br.com.granit.controlador.ControladorVenda;
import br.com.granit.fachada.Fachada;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAQueryException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.excecao.JPAUtilityException;
import br.com.granit.persistencia.pk.ItemBeneficiamentoVendaPK;
import br.com.granit.persistencia.pk.ItemVendaPK;
import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.ItemBeneficiamentoVendaTO;
import br.com.granit.persistencia.to.ItemVendaTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.persistencia.to.VendaTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.Formatador;
import br.com.granit.util.JPAUtility;
import br.com.granit.util.ResultadoPaginacao;

public class VendaAction extends PrincipalAction {

	private static final double VALOR_DESCONTO_PADRAO = 0.05;
	private static final String SECAO_ITENS_VENDA = "secaoItens";
	private static final String SECAO_ITENS_BENEFICIAMENTO_VENDA = "secaoItensBeneficiamento";
	private static final String SECAO_TOTALIZACAO_VENDA = "secaoTotalizacao";
	private static final String SECAO_SERVICO_MONTAGEM_VENDA = "secaoServicoMontagem";
	private static final String FORWARD_SECAO = "forwardSecao";
	private static final String AVANCAR_PARA_DETALHAR_VENDA = "avancarParaDetalharVenda";
	private static final String ID_VENDA = "idVenda";

	public ActionForward abrir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		cleanForm(formulario);

		formulario.setItemEmInclusao(new ItemVendaTO());
		formulario.setVenda(new VendaTO());

		List<ClienteTO> clientes = Fachada.getInstance().listarClientes();
		request.getSession().setAttribute(Constantes.LISTA_CLIENTES, clientes);

		VendaTO venda = new VendaTO();
		venda.setDataVenda(new Date());

		request.getSession().setAttribute(Constantes.VENDA_CLIENTE, venda);

		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_MATERIA_PRIMA);
		List<ProdutoTO> produtos = new ArrayList<ProdutoTO>();
		try {
			produtos = Fachada.getInstance().consultarProduto(parametros);
		} catch (JPAQueryException e) {
			e.printStackTrace();
		}
		request.getSession().setAttribute("produtosVenda", produtos);

		return (mapping.findForward(Constantes.FORWARD_ABRIR));
	}

	private Date getApenasDataSemHora(Date aDate) {
		final Calendar myCalendar = Calendar.getInstance();
		myCalendar.setTime(aDate);
		myCalendar.set(Calendar.HOUR_OF_DAY, 0);
		myCalendar.set(Calendar.MINUTE, 0);
		myCalendar.set(Calendar.SECOND, 0);
		myCalendar.set(Calendar.MILLISECOND, 0);
		return myCalendar.getTime();
	}

	public ActionForward abrirAlterar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String stringIdVenda = request.getParameter("idVenda");
		if (GenericValidator.isBlankOrNull(stringIdVenda)) {
			stringIdVenda = (String) request.getAttribute("idVenda");
		}
		Integer idVenda = new Integer(stringIdVenda);
		VendaTO venda = Fachada.getInstance().getVenda(idVenda);

		if (venda.getSituacao() == Constantes.VENDA_FINALIZADA) {
			adicionaErro(request, "venda.ja.finalizada");
			configurarResultadoConsultaAnterior(request);
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		}

		VendaForm formulario = (VendaForm) form;
		cleanForm(formulario);

		if (venda.getCliente() != null)
			formulario
					.setIdCliente(venda.getCliente().getIdPessoa().toString());

		double valorAvista = venda.getValorAVista() != null ? venda
				.getValorAVista().doubleValue() : 0.0;
		double valorCheque = venda.getValorCheque() != null ? venda
				.getValorCheque().doubleValue() : 0.0;
		double valorCredito = venda.getValorCredito() != null ? venda
				.getValorCredito().doubleValue() : 0.0;
		double valorAberto = venda.getValorAberto() != null ? venda
				.getValorAberto().doubleValue() : 0.0;
		double valorBoleto = venda.getValorBoleto() != null ? venda
				.getValorBoleto().doubleValue() : 0.0;
		double valorServicoMontagem = venda.getValorServicoMontagem() != null ? venda
				.getValorServicoMontagem().doubleValue() : 0.0;
		int numeroDeParcelas = venda.getNumeroParcelas() != null ? venda
				.getNumeroParcelas().intValue() : 0;

		if (valorAvista > 0.0) {
			formulario.setValorVista(Formatador.formatNumeroBR(valorAvista));
		}
		if (valorCheque > 0.0) {
			formulario.setValorCheque(Formatador.formatNumeroBR(valorCheque));
		}
		if (valorCredito > 0.0) {
			formulario.setValorCredito(Formatador.formatNumeroBR(valorCredito));
		}
		if (valorBoleto > 0.0) {
			formulario.setValorBoleto(Formatador.formatNumeroBR(valorBoleto));
		}
		if (valorAberto > 0.0) {
			formulario.setValorAberto(Formatador.formatNumeroBR(valorAberto));
		}
		if (numeroDeParcelas > 0) {
			formulario.setNumeroParcelas("" + numeroDeParcelas);
		}

		if (venda.getDesconto() != null
				&& venda.getDesconto().doubleValue() > 0.0) {
			formulario.setDesconto(Formatador.formatNumeroBR(venda
					.getDesconto().doubleValue()));
		}

		if (valorServicoMontagem > 0.0) {
			formulario.setValorServicoMontagem(Formatador
					.formatNumeroBR(valorServicoMontagem));
		}

		formulario.setItemEmInclusao(new ItemVendaTO());
		formulario.setDataEntrega(venda.getPrazoEntregaFormatado());
		formulario.setObservacao(venda.getObservacao());
		formulario.setEntregue(venda.isProdutosEntregues());

		List<ClienteTO> clientes = Fachada.getInstance().listarClientes();
		request.getSession().setAttribute(Constantes.LISTA_CLIENTES, clientes);

		request.getSession().setAttribute(Constantes.VENDA_CLIENTE, venda);

		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
		request.setAttribute(Constantes.LISTA_ITENS_VENDA_BENEFICIAMENTO,
				venda.getItensBeneficiamento());

		int i = 0;
		if (venda.getItens() != null && !venda.getItens().isEmpty()) {
			for (ItemVendaTO itemVenda : venda.getItens()) {
				itemVenda.setPosicao(i++);
			}
		}
		
		i = 0;
		if (venda.getItensBeneficiamento() != null && !venda.getItensBeneficiamento().isEmpty()) {
			for (ItemBeneficiamentoVendaTO itemBeneficiamentoVenda : venda.getItensBeneficiamento()) {
				itemBeneficiamentoVenda.setPosicao(i++);
			}
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("subTipo.tipo.idTipo", Constantes.TIPO_MATERIA_PRIMA);
		List<ProdutoTO> produtos = new ArrayList<ProdutoTO>();
		try {
			produtos = Fachada.getInstance().consultarProduto(parametros);
		} catch (JPAQueryException e) {
			e.printStackTrace();
		}
		request.getSession().setAttribute("produtosVenda", produtos);

		return (mapping.findForward("abrirAlterar"));
	}

	public ActionForward adicionarItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		boolean erro = adicionarItemGenerico(request, formulario);
		if (erro) {
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		}
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward adicionarItemAlterar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		boolean erro = adicionarItemGenerico(request, formulario);
		if (erro) {
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		}
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	private boolean adicionarItemGenerico(HttpServletRequest request,
			VendaForm formulario) {
		boolean erro = false;
		request.setAttribute(FORWARD_SECAO, SECAO_ITENS_VENDA);
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		ItemVendaTO item;
		if (formulario.getDescricaoItem() == null
				|| formulario.getDescricaoItem().isEmpty()) {
			erro = true;
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					"Descri��o do Item");
		}
		if (GenericValidator.isBlankOrNull(formulario.getValorItem())
				|| !GenericValidator.isDouble(formataDouble(formulario
						.getValorItem()))
				|| new Double(formataDouble(formulario.getValorItem()))
						.doubleValue() == 0) {
			erro = true;
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					"Valor do Item");
		}
		if (formulario.getIdProdutoItem() == null
				|| formulario.getIdProdutoItem().isEmpty()) {
			erro = true;
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					"Produto do Item");
		}
		if (formulario.getMetragem() == null
				|| formulario.getMetragem().isEmpty()) {
			erro = true;
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					"Metragem do Item");
		}

		if (formulario != null && !erro) {
			item = new ItemVendaTO();
			ItemVendaPK pk = new ItemVendaPK();
			item.setPk(pk);
			BigDecimal valor = new BigDecimal(new Double(
					formataDouble(formulario.getValorItem())));
			try {
				BigDecimal metragem = new BigDecimal(new Double(formataDouble(formulario.getMetragem())));				
				item.setPreco(valor);
				item.setMetragem(metragem);
				item.setProduto(ControladorProduto.getInstance().get(
						new Integer(formulario.getIdProdutoItem())));
				item.setDescricao(formulario.getDescricaoItem());
				if (venda.getItens() == null) {
					venda.setItens(new ArrayList<ItemVendaTO>());
				}
				item.setVenda(venda);
				item.setPosicao(venda.getItens().size());
				venda.getItens().add(item);
			} catch (Exception e) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"Metragem do Item");
				erro = true;
			}
		}

		configurarDadosDaVendaNaSessao(request, venda);

		if (formulario != null && !erro) {
			formulario.setDescricaoItem("");
			formulario.setIdProdutoItem("");
			formulario.setMetragem("");
			formulario.setValorItem("");
			adicionaMensagem(request, Constantes.ITEM_INCLUIDO_SUCESSO);
		}

		return erro;
	}

	public ActionForward adicionarItemBeneficiamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		boolean erro = adicionarItemBeneficiamentoGenerico(request, formulario);
		if (erro) {
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		}
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward adicionarItemBeneficiamentoAlterar(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		boolean erro = adicionarItemBeneficiamentoGenerico(request, formulario);
		if (erro) {
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		}
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	private boolean adicionarItemBeneficiamentoGenerico(
			HttpServletRequest request, VendaForm formulario) {
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		boolean erro = false;
		request.setAttribute(FORWARD_SECAO, SECAO_ITENS_BENEFICIAMENTO_VENDA);
		ItemBeneficiamentoVendaTO item;
		if (formulario.getDescricaoItemBeneficiamento() == null
				|| formulario.getDescricaoItemBeneficiamento().isEmpty()) {
			erro = true;
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					"Descri��o do Item de Beneficiamento");
		}
		if (GenericValidator.isBlankOrNull(formulario
				.getValorItemBeneficiamento())
				|| !GenericValidator.isDouble(formataDouble(formulario
						.getValorItemBeneficiamento()))
				|| new Double(
						formataDouble(formulario.getValorItemBeneficiamento()))
						.doubleValue() == 0) {
			erro = true;
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					"Valor do Item de Beneficiamento");
		}

		if (formulario != null && !erro) {
			item = new ItemBeneficiamentoVendaTO();
			ItemBeneficiamentoVendaPK pk = new ItemBeneficiamentoVendaPK();
			item.setPk(pk);
			BigDecimal valor = new BigDecimal(new Double(
					formataDouble(formulario.getValorItemBeneficiamento())));
			item.setValor(valor);
			item.setDescricao(formulario.getDescricaoItemBeneficiamento());
			if (venda.getItensBeneficiamento() == null) {
				venda.setItensBeneficiamento(new ArrayList<ItemBeneficiamentoVendaTO>());
			}
			item.setVenda(venda);
			item.setPosicao(venda.getItensBeneficiamento().size());
			venda.getItensBeneficiamento().add(item);
		}
		if (formulario != null && !erro) {
			formulario.setDescricaoItemBeneficiamento("");
			formulario.setValorItemBeneficiamento("");
			adicionaMensagem(request, Constantes.ITEM_INCLUIDO_SUCESSO);
		}
		configurarDadosDaVendaNaSessao(request, venda);
		return erro;
	}

	private void configurarDadosDaVendaNaSessao(HttpServletRequest request,
			VendaTO venda) {
		request.getSession().setAttribute(Constantes.VENDA_CLIENTE, venda);
		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
		request.setAttribute(Constantes.LISTA_ITENS_VENDA_BENEFICIAMENTO,
				venda.getItensBeneficiamento());
		request.getSession().setAttribute(Constantes.LISTA_ITENS_VENDA,
				venda.getItens());
		request.getSession().setAttribute(
				Constantes.LISTA_ITENS_VENDA_BENEFICIAMENTO,
				venda.getItensBeneficiamento());
	}

	public ActionForward removerItem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		removerItemGenerico(request);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward removerItemAlterar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		removerItemGenerico(request);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	private void removerItemGenerico(HttpServletRequest request) {
		request.setAttribute(FORWARD_SECAO, SECAO_ITENS_VENDA);
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		Integer posicao = new Integer(request.getParameter("posicao"));

		if (venda != null && !venda.getItens().isEmpty()) {
			venda.getItens().remove(posicao.intValue());
			int novaPosicao = 0;
			for (ItemVendaTO item : venda.getItens()) {
				item.setPosicao(novaPosicao++);
			}
		}

		adicionaMensagem(request, Constantes.ITEM_REMOVIDO_SUCESSO);
		configurarDadosDaVendaNaSessao(request, venda);
	}

	public ActionForward removerItemBeneficiamento(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		removerItemBeneficiamentoGenerico(request);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward removerItemBeneficiamentoAlterar(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		removerItemBeneficiamentoGenerico(request);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	private void removerItemBeneficiamentoGenerico(HttpServletRequest request) {
		request.setAttribute(FORWARD_SECAO, SECAO_ITENS_BENEFICIAMENTO_VENDA);
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		Integer posicao = new Integer(request.getParameter("posicao"));

		if (venda != null && !venda.getItensBeneficiamento().isEmpty()) {
			venda.getItensBeneficiamento().remove(posicao.intValue());
			int novaPosicao = 0;
			for (ItemBeneficiamentoVendaTO item : venda
					.getItensBeneficiamento()) {
				item.setPosicao(novaPosicao++);
			}
		}

		adicionaMensagem(request, Constantes.ITEM_REMOVIDO_SUCESSO);
		configurarDadosDaVendaNaSessao(request, venda);
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		cleanForm(form);
		request.getSession().removeAttribute(Constantes.LISTA_CLIENTES_VENDAS);
		request.getSession().removeAttribute(Constantes.VENDA_CLIENTE);
		return (mapping.findForward(Constantes.FORWARD_CANCELAR));
	}

	public ActionForward processarSalvar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		boolean entregue = formulario.isEntregue();
		formulario.setEntregue(false);
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
		boolean finalizar = !GenericValidator.isBlankOrNull(request
				.getParameter("finalizar"));
		ClienteTO cliente = null;
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		ArrayList<String> camposInvalidos = new ArrayList<String>();
		if (!GenericValidator.isBlankOrNull(formulario.getIdCliente())) {
			cliente = Fachada.getInstance().getCliente(
					new Integer(formulario.getIdCliente()));
		} else {
			camposObrigatorios.add("Cliente");
		}
		// 26/03/2011 - desabilitando datas de pedido e vendas
		// Date dataVenda = null;
		// if
		// (!GenericValidator.isBlankOrNull(formulario.getDataInformadaVenda()))
		// {
		// try {
		// dataVenda =
		// Formatador.converterStringParaDate(formulario.getDataInformadaVenda(),
		// Formatador.FORMATO_DATA_PADRAO);
		// venda.setDataVenda(dataVenda);
		// }catch(Exception e){
		// camposInvalidos.add("Data da Venda");
		// }
		// } else {
		// camposObrigatorios.add("Data da Venda");
		// }
		Double valorAvista = 0.0;
		Double valorCheque = 0.0;
		Double valorCredito = 0.0;
		Double valorAberto = 0.0;
		Double valorBoleto = 0.0;
		Integer numeroDeParcelas = 0;
		Date dataVenda = venda.getDataVenda();

		if (!GenericValidator.isBlankOrNull(formulario.getValorVista())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorVista()))) {
				camposInvalidos.add("Valor � Vista");
			}else{
				valorAvista = Formatador.parseDouble(formulario.getValorVista());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorCheque())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorCheque()))) {
				camposInvalidos.add("Valor Cheque");
			}else{
				valorCheque = Formatador.parseDouble(formulario.getValorCheque());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorCredito())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorCredito()))) {
				camposInvalidos.add("Valor Cr�dito");
			}else{
				valorCredito = Formatador.parseDouble(formulario.getValorCredito());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorAberto())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorAberto()))) {
				camposInvalidos.add("Valor em aberto");
			}else{
				valorAberto = Formatador.parseDouble(formulario.getValorAberto());
			}
			if (GenericValidator.isBlankOrNull(formulario.getNumeroParcelas())) {
				camposObrigatorios.add("N� parcelas em aberto");
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorBoleto())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorBoleto()))) {
				camposInvalidos.add("Valor Boleto");
			}else{
				valorBoleto = Formatador.parseDouble(formulario.getValorBoleto());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getNumeroParcelas())) {
			if (!GenericValidator.isInt(formulario.getNumeroParcelas())) {
				camposInvalidos.add("N� parcelas em aberto");
			}else{
				numeroDeParcelas = new Integer(formulario.getNumeroParcelas());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getObservacao())) {
			if (formulario.getObservacao().length() > 254){
				camposInvalidos.add("Observa��o s� pode ter no m�ximo 254 caracteres!");
			}else{
				venda.setObservacao(formulario.getObservacao());
			}
		}else{
			venda.setObservacao("");
		}
		if (entregue) {
			venda.setProdutosEntregues(true);
		} else {
			venda.setProdutosEntregues(false);
		}
		if (!GenericValidator.isBlankOrNull(formulario.getDataEntrega())) {
			try {
				Date dataEntrega = Formatador.converterStringParaDate(
						formulario.getDataEntrega(),
						Formatador.FORMATO_DATA_PADRAO);
				if (getApenasDataSemHora(dataEntrega).before(
						getApenasDataSemHora(dataVenda))) {
					camposInvalidos
							.add("Data de Entrega n�o pode ser antes da data da Venda");
				} else {
					venda.setPrazoEntrega(dataEntrega);
				}

			} catch (ParseException e) {
				if (!GenericValidator.isInt(formulario.getNumeroParcelas())) {
					camposInvalidos.add("Data de Entrega");
				}
			}
		}else{
			venda.setPrazoEntrega(null);
		}

		if (!camposInvalidos.isEmpty()) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					geraStringCamposObrigatorios(camposInvalidos));
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		}

		Double total = 0.0;
		total += valorAberto + valorAvista + valorBoleto + valorCheque
				+ valorCredito;

		if (total == 0.0 && finalizar) {
			camposObrigatorios.add("Forma(s) de pagamento");
		}

		boolean servicoMontagemInformado = false;
		if (!GenericValidator.isBlankOrNull(formulario
				.getValorServicoMontagem())) {
			BigDecimal servicoMontagem = new BigDecimal(
					Formatador.parseDouble(formulario.getValorServicoMontagem()));
			if (servicoMontagem.doubleValue() > 0.0) {
				servicoMontagemInformado = true;
			}
		}

		if ((venda.getItens() == null || venda.getItens().isEmpty())
				&& (venda.getItensBeneficiamento() == null || venda
						.getItensBeneficiamento().isEmpty()) && !servicoMontagemInformado) {
			camposObrigatorios.add("Para uma venda informe pelo menos um desses campos:" +
					" Item de Venda ou " +
					"Item de Beneficiamento ou" +
					"Servi�o de Montagem.");
		}

		if (!camposObrigatorios.isEmpty()) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					geraStringCamposObrigatorios(camposObrigatorios));
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		}

		if (arredondaDouble(total) != arredondaDouble(venda.getTotal()
				.doubleValue())) {
			if (finalizar || venda.isProdutosEntregues()) {
				String erros = Constantes.VALOR_DE_VENDA_INVALIDO;
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, erros);
				configurarDadosDaVendaNaSessao(request, venda);
				return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
			}
		}

		// 30/06/2011
		if (finalizar && !venda.isProdutosEntregues()) {
			adicionaErro(
					request,
					Constantes.ERRO_DADOS_INVALIDOS,
					"Uma venda s� poder� ser finalizada ap�s confirma��o da Entrega de Produtos e Servi�os");
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		}

		if (venda.isProdutosEntregues()) {
			String produtosSemEstoque = "";
			boolean first = true;
			for (ItemVendaTO item : venda.getItens()) {
				ProdutoTO produto = ControladorProduto.getInstance().get(
						item.getProduto().getIdProduto());
				if (item.getMetragem().doubleValue() > produto.getQtdEstoque()) {
					if (first) {
						first = false;
					} else {
						produtosSemEstoque += ", ";
					}
					produtosSemEstoque += produto.getDescricao();
				}
			}
			if (!produtosSemEstoque.isEmpty()) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"N�o h� estoque dispon�vel para os produtos: "
								+ produtosSemEstoque + ".");
				configurarDadosDaVendaNaSessao(request, venda);
				return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
			}
		}

		String numeroPedido = "";
		int ano = new GregorianCalendar().get(Calendar.YEAR);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ano", "%/" + ano + "%");
		List<VendaTO> vendasNoAno = ControladorVenda.getInstance()
				.consultarPorQuery("vendasNoAno", params);
		// atualiza��o do numero inicial de venda 04/04/2011
		int maiorNumero = 0;
		if (ano == 2011) {
			maiorNumero = 132;
		}
		for (VendaTO vendaNoAno : vendasNoAno) {
			if (vendaNoAno.getNumeroPedido() != null) {
				int numero = Integer.parseInt(new StringTokenizer(vendaNoAno
						.getNumeroPedido(), "/").nextToken());
				if (numero > maiorNumero) {
					maiorNumero = numero;
				}
			}
		}
		numeroPedido = (maiorNumero + 1) + "/" + ano;

		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			
			venda.setValorAberto(new BigDecimal(valorAberto));
			venda.setNumeroParcelas(numeroDeParcelas);
			if (venda.getValorAberto() == null || venda.getValorAberto().doubleValue() == 0.0){
				venda.setNumeroParcelas(0);
			}
			venda.setValorAVista(new BigDecimal(valorAvista));
			venda.setValorBoleto(new BigDecimal(valorBoleto));
			venda.setValorCheque(new BigDecimal(valorCheque));
			venda.setValorCredito(new BigDecimal(valorCredito));

			if (cliente != null)
				venda.setCliente(cliente);

			// 26/03/2011 - desabilitando datas de pedido e vendas - 
			// 18/07/2011 - A data da Venda � definida na cria��o e n�o muda mais!
			//venda.setDataVenda(dataVenda);
			// if (venda.getDataVenda() == null) {
			// venda.setDataVenda(new Date());
			// }

			venda.setNumeroPedido(numeroPedido);
			venda.setSituacao(Constantes.VENDA_PENDENTE);
			if (finalizar) {
				venda.setSituacao(Constantes.VENDA_FINALIZADA);
			}
			if (venda.isProdutosEntregues()) {
				registrarHistoricoSaidasItensVenda(request, venda);
			}

			Fachada.getInstance().salvaVenda(venda);
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAInsertException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		} catch (JPAUpdateException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
		}
		adicionaMensagem(request, "MSG02");
		request.setAttribute(ID_VENDA, venda.getIdVenda().toString());
		return (mapping.findForward(AVANCAR_PARA_DETALHAR_VENDA));
	}

	public ActionForward processarAlterar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		boolean entregue = formulario.isEntregue();
		formulario.setEntregue(false);
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
		boolean finalizar = !GenericValidator.isBlankOrNull(request
				.getParameter("finalizar"));
		ClienteTO cliente = null;
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if (!GenericValidator.isBlankOrNull(formulario.getIdCliente())) {
			cliente = Fachada.getInstance().getCliente(
					new Integer(formulario.getIdCliente()));
		} else {
			if (finalizar) {
				camposObrigatorios.add("Cliente");
			}
		}
		Double valorAvista = 0.0;
		Double valorCheque = 0.0;
		Double valorCredito = 0.0;
		Double valorAberto = 0.0;
		Double valorBoleto = 0.0;
		Integer numeroDeParcelas = 0;
		Date dataVenda = venda.getDataVenda();

		ArrayList<String> camposInvalidos = new ArrayList<String>();
		if (!GenericValidator.isBlankOrNull(formulario.getValorVista())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorVista()))) {
				camposInvalidos.add("Valor � Vista");
			}else{
				valorAvista = Formatador.parseDouble(formulario.getValorVista());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorCheque())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorCheque()))) {
				camposInvalidos.add("Valor Cheque");
			}else{
				valorCheque = Formatador.parseDouble(formulario.getValorCheque());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorCredito())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorCredito()))) {
				camposInvalidos.add("Valor Cr�dito");
			}else{
				valorCredito = Formatador.parseDouble(formulario.getValorCredito());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorAberto())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorAberto()))) {
				camposInvalidos.add("Valor em aberto");
			}else{
				valorAberto = Formatador.parseDouble(formulario.getValorAberto());
			}
			if (GenericValidator.isBlankOrNull(formulario.getNumeroParcelas())) {
				camposObrigatorios.add("N�0 parcelas em aberto");
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getValorBoleto())) {
			if (!GenericValidator.isDouble(formataDouble(formulario
					.getValorBoleto()))) {
				camposInvalidos.add("Valor Boleto");
			}else{
				valorBoleto = Formatador.parseDouble(formulario.getValorBoleto());
			}
		}
		if (!GenericValidator.isBlankOrNull(formulario.getNumeroParcelas())) {
			if (!GenericValidator.isInt(formulario.getNumeroParcelas())) {
				camposInvalidos.add("N� parcelas em aberto");
			}else{
				numeroDeParcelas = new Integer(formulario.getNumeroParcelas());
			}
		}
		
		if (!GenericValidator.isBlankOrNull(formulario.getObservacao())) {
			if (formulario.getObservacao().length() > 254){
				camposInvalidos.add("Observa��o s� pode ter no m�ximo 254 caracteres!");
			}else{
				venda.setObservacao(formulario.getObservacao());
			}
		}else{
			venda.setObservacao("");
		}
		
		boolean confirmandoEntregaProdutos = entregue && !venda.isProdutosEntregues();

		if (!GenericValidator.isBlankOrNull(formulario.getDataEntrega())) {
			try {
				Date dataEntrega = Formatador.converterStringParaDate(
						formulario.getDataEntrega(),
						Formatador.FORMATO_DATA_PADRAO);
				if (getApenasDataSemHora(dataEntrega).before(
						getApenasDataSemHora(dataVenda))) {
					camposInvalidos
							.add("Data de Entrega n�o pode ser antes da data da Venda");
				} else {
					venda.setPrazoEntrega(dataEntrega);
				}
			} catch (ParseException e) {
				if (!GenericValidator.isInt(formulario.getNumeroParcelas())) {
					camposInvalidos.add("Data de Entrega");
				}
			}
		}else{
			venda.setPrazoEntrega(null);
		}

		if (!camposInvalidos.isEmpty()) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					geraStringCamposObrigatorios(camposInvalidos));
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		}

		Double total = 0.0;
		total += valorAberto + valorAvista + valorBoleto + valorCheque
				+ valorCredito;

		if (total == 0.0 && finalizar) {
			camposObrigatorios.add("Forma(s) de pagamento");
		}

		boolean servicoMontagemInformado = false;
		if (!GenericValidator.isBlankOrNull(formulario
				.getValorServicoMontagem())) {
			BigDecimal servicoMontagem = new BigDecimal(
					Formatador.parseDouble(formulario.getValorServicoMontagem()));
			if (servicoMontagem.doubleValue() > 0.0) {
				servicoMontagemInformado = true;
			}
		}

		if ((venda.getItens() == null || venda.getItens().isEmpty())
				&& (venda.getItensBeneficiamento() == null || venda
						.getItensBeneficiamento().isEmpty()) && !servicoMontagemInformado) {
			camposObrigatorios.add("Para uma venda informe pelo menos um desses campos:" +
					" Item de Venda ou " +
					"Item de Beneficiamento ou" +
					"Servi�o de Montagem.");
		}

		if (!camposObrigatorios.isEmpty()) {
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					geraStringCamposObrigatorios(camposObrigatorios));
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		}

		// 30/06/2011
		if (finalizar && !venda.isProdutosEntregues()) {
			adicionaErro(
					request,
					Constantes.ERRO_DADOS_INVALIDOS,
					"Uma venda s� poder� ser finalizada ap�s confirma��o da Entrega de Produtos e Servi�os");
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		}

		if (arredondaDouble(total) != arredondaDouble(venda.getTotal()
				.doubleValue())) {
			if (confirmandoEntregaProdutos) {
				String erros = Constantes.VALOR_DE_VENDA_INVALIDO;
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, erros);
				configurarDadosDaVendaNaSessao(request, venda);
				return (mapping
						.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
			}
		}

		if (confirmandoEntregaProdutos) {
			String produtosSemEstoque = "";
			boolean first = true;
			for (ItemVendaTO item : venda.getItens()) {
				ProdutoTO produto = ControladorProduto.getInstance().get(
						item.getProduto().getIdProduto());
				if (item.getMetragem().doubleValue() > produto.getQtdEstoque()) {
					if (first) {
						first = false;
					} else {
						produtosSemEstoque += ", ";
					}
					produtosSemEstoque += produto.getDescricao();
				}
			}
			if (!produtosSemEstoque.isEmpty()) {
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"N�o h� estoque dispon�vel para os produtos: "
								+ produtosSemEstoque + ".");
				configurarDadosDaVendaNaSessao(request, venda);
				return (mapping
						.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
			}
		}
		if (entregue) {
			venda.setProdutosEntregues(true);
		} else {
			venda.setProdutosEntregues(false);
		}
		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			// venda = Fachada.getInstance().getVenda(venda.getIdVenda());

			// EXCLUIR ITENS DA VENDA NA MARRA!!!
			ControladorItemVenda.getInstance().excluirItensDaVenda(
					venda.getIdVenda());
			ControladorItemBeneficiamentoVenda.getInstance()
					.excluirItensDeBeneficiamentoDaVenda(venda.getIdVenda());
			//

			int i = 1;
			for (ItemVendaTO itemVenda : venda.getItens()) {
				itemVenda.setVenda(venda);
				itemVenda.getPk().setIdVenda(venda.getIdVenda());
				itemVenda.setPosicao(i++);
				itemVenda.getPk().setIdItemVenda(itemVenda.getPosicao());
			}

			i = 1;
			for (ItemBeneficiamentoVendaTO itemBenVenda : venda
					.getItensBeneficiamento()) {
				itemBenVenda.setVenda(venda);
				itemBenVenda.getPk().setIdVenda(venda.getIdVenda());
				itemBenVenda.setPosicao(i++);
				itemBenVenda.getPk().setIdItemBeneficiamentoVenda(
						itemBenVenda.getPosicao());
			}

			venda.setValorAberto(new BigDecimal(valorAberto));
			venda.setNumeroParcelas(numeroDeParcelas);
			if (venda.getValorAberto() == null || venda.getValorAberto().doubleValue() == 0.0){
				venda.setNumeroParcelas(0);
			}
			venda.setValorAVista(new BigDecimal(valorAvista));
			venda.setValorBoleto(new BigDecimal(valorBoleto));
			venda.setValorCheque(new BigDecimal(valorCheque));
			venda.setValorCredito(new BigDecimal(valorCredito));

			if (cliente != null)
				venda.setCliente(cliente);

			if (finalizar) {
				venda.setSituacao(Constantes.VENDA_FINALIZADA);
			}
			if (confirmandoEntregaProdutos) {
				registrarHistoricoSaidasItensVenda(request, venda);
			}
			// 26/03/2011 - desabilitando datas de pedido e vendas
			// 18/07/2011 - A data da Venda � definida na cria��o e n�o muda mais!
			//venda.setDataVenda(dataVenda);
			Fachada.getInstance().alteraVenda(venda);
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAUpdateException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		} catch (JPAInsertException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			configurarDadosDaVendaNaSessao(request, venda);
			return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
		}
		adicionaMensagem(request, "MSG06");
		request.setAttribute(ID_VENDA, venda.getIdVenda().toString());
		return (mapping.findForward(AVANCAR_PARA_DETALHAR_VENDA));
	}

	// DEVE SER EXECUTADO DENTRO DE UMA UNICA TRANSACAO COM COMMIT E ROLLBACK
	private void registrarHistoricoSaidasItensVenda(HttpServletRequest request,
			VendaTO venda) throws JPAUpdateException, JPAInsertException {
		for (ItemVendaTO item : venda.getItens()) {
			ProdutoTO produto = ControladorProduto.getInstance().get(
					item.getProduto().getIdProduto());
			produto.setQtdEstoque(produto.getQtdEstoque().doubleValue()
					- item.getMetragem().doubleValue());
			ControladorProduto.getInstance().atualiza(produto);
			HistoricoProdutoTO historico = new HistoricoProdutoTO();
			UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
					Constantes.USUARIO_LOGADO);
			historico.setUsuario(usuario);
			// 26/03/2011 - desabilitando datas de pedido e vendas
			// if (venda.getDataVenda() != null){
			// historico.setData(venda.getDataVenda());
			// }
			historico.setProduto(produto);
			historico.setQuantidade(item.getMetragem());
			historico.setTipo(Constantes.TIPO_SAIDA_PRODUTO);
			String codigoVenda = "N�:" + venda.getNumeroPedidoFormatado()
					+ " - Data:" + venda.getDataVendaFormatada();
			historico.setObservacao(Constantes.OBS1.replaceAll(
					"IdVenda/usuarioX", codigoVenda + "/"
							+ recuperaUsuarioLogado(request).getLogin() + ""));
			ControladorHistoricoProduto.getInstance().salva(historico);
		}
	}
	
	
	// DEVE SER EXECUTADO DENTRO DE UMA UNICA TRANSACAO COM COMMIT E ROLLBACK
		private void registrarHistoricoEntradasItensVenda(HttpServletRequest request,
				VendaTO venda) throws JPAUpdateException, JPAInsertException {
			for (ItemVendaTO item : venda.getItens()) {
				ProdutoTO produto = ControladorProduto.getInstance().get(item.getProduto().getIdProduto());
				produto.setQtdEstoque(produto.getQtdEstoque().doubleValue()
						+ item.getMetragem().doubleValue());
				ControladorProduto.getInstance().atualiza(produto);
				HistoricoProdutoTO historico = new HistoricoProdutoTO();
				UsuarioTO usuario = (UsuarioTO) request.getSession().getAttribute(
						Constantes.USUARIO_LOGADO);
				historico.setUsuario(usuario);
				// 26/03/2011 - desabilitando datas de pedido e vendas
				// if (venda.getDataVenda() != null){
				// historico.setData(venda.getDataVenda());
				// }
				historico.setProduto(produto);
				historico.setQuantidade(item.getMetragem());
				historico.setTipo(Constantes.TIPO_ENTRADA_PRODUTO);
				String codigoVenda = "N�:" + venda.getNumeroPedidoFormatado()
						+ " - Data:" + venda.getDataVendaFormatada();
				historico.setObservacao(Constantes.OBS10.replaceAll(
						"IdVenda/usuarioX", codigoVenda + "/"
								+ recuperaUsuarioLogado(request).getLogin() + ""));
				ControladorHistoricoProduto.getInstance().salva(historico);
			}
		}

	public ActionForward processarFinalizar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String stringIdVenda = request.getParameter("idVenda");
		if (GenericValidator.isBlankOrNull(stringIdVenda)) {
			stringIdVenda = (String) request.getAttribute("idVenda");
		}
		Integer idVenda = new Integer(stringIdVenda);

		VendaTO venda = Fachada.getInstance().getVenda(idVenda);
		ArrayList<String> camposObrigatorios = new ArrayList<String>();
		if (venda.getCliente() == null) {
			camposObrigatorios.add("Cliente");
		}
		double valorAvista = venda.getValorAVista() != null ? venda
				.getValorAVista().doubleValue() : 0.0;
		double valorCheque = venda.getValorCheque() != null ? venda
				.getValorCheque().doubleValue() : 0.0;
		double valorCredito = venda.getValorCredito() != null ? venda
				.getValorCredito().doubleValue() : 0.0;
		double valorAberto = venda.getValorAberto() != null ? venda
				.getValorAberto().doubleValue() : 0.0;
		double valorBoleto = venda.getValorBoleto() != null ? venda
				.getValorBoleto().doubleValue() : 0.0;
		int numeroDeParcelas = venda.getNumeroParcelas() != null ? venda
				.getNumeroParcelas().intValue() : -1;

		Date dataVenda = venda.getDataVenda();

		Double total = 0.0;
		total += valorAberto + valorAvista + valorBoleto + valorCheque
				+ valorCredito;

		if (total == 0.0) {
			camposObrigatorios.add("Forma(s) de pagamento");
		}

		boolean servicoMontagemInformado = venda.getValorServicoMontagem() != null && venda.getValorServicoMontagem().doubleValue() > 0.0;

		if ((venda.getItens() == null || venda.getItens().isEmpty())
				&& (venda.getItensBeneficiamento() == null || venda
						.getItensBeneficiamento().isEmpty()) && !servicoMontagemInformado) {
			camposObrigatorios.add("Para uma venda informe pelo menos um desses campos:" +
					" Item de Venda ou " +
					"Item de Beneficiamento ou" +
					"Servi�o de Montagem.");
		}


		if (valorAberto != 0 && numeroDeParcelas == -1) {
			camposObrigatorios.add("N� de parcelas em aberto");
		}

		if (!camposObrigatorios.isEmpty()) {
			adicionaErro(request, "venda.nao.finalizada");
			adicionaErro(request, Constantes.ERRO_OBRIGATORIOS,
					geraStringCamposObrigatorios(camposObrigatorios));
			configurarResultadoConsultaAnterior(request);

			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		}

		// 30/06/2011
		if (!venda.isProdutosEntregues()) {
			adicionaErro(request, "venda.nao.finalizada");
			adicionaErro(
					request,
					Constantes.ERRO_DADOS_INVALIDOS,
					"Uma venda s� poder� ser finalizada ap�s confirma��o da Entrega de Produtos e Servi�os");
			configurarResultadoConsultaAnterior(request);
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		}

		if (arredondaDouble(total) != arredondaDouble(venda.getTotal()
				.doubleValue())) {
			String erros = Constantes.VALOR_DE_VENDA_INVALIDO;
			adicionaErro(request, "venda.nao.finalizada");
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS, erros);
			configurarResultadoConsultaAnterior(request);
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		}

		// Valida��o da Data de Entrega
		Date dataEntrega = venda.getPrazoEntrega();
		if (dataEntrega != null) {
			if (getApenasDataSemHora(dataEntrega).before(
					getApenasDataSemHora(dataVenda))) {
				adicionaErro(request, "venda.nao.finalizada");
				adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
						"Data de Entrega n�o pode ser antes da data da Venda");
				configurarResultadoConsultaAnterior(request);
				return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
			}
		}

		/*
		 * 30/06/2011 - Comentando - S� finalizada se j� tiver entregue, se
		 * tiver entregue as saidas jah foram registradas
		 */
		/*
		 * String produtosSemEstoque = ""; boolean first = true; for
		 * (ItemVendaTO item : venda.getItens()) { ProdutoTO produto =
		 * ControladorProduto
		 * .getInstance().get(item.getProduto().getIdProduto()); if
		 * (item.getMetragem().doubleValue() > produto.getQtdEstoque()){ if
		 * (first){ first = false; }else{ produtosSemEstoque += ", "; }
		 * produtosSemEstoque+=produto.getDescricao(); } } if
		 * (!produtosSemEstoque.isEmpty()){ adicionaErro(request,
		 * Constantes.ERRO_DADOS_INVALIDOS,
		 * "N�o h� estoque dispon�vel para os produtos: " + produtosSemEstoque
		 * +"."); configurarResultadoConsultaAnterior(request); return
		 * (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA)); }
		 */

		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			venda.setSituacao(Constantes.VENDA_FINALIZADA);
			/*
			 * 30/06/2011 - Comentando - S� finalizada se j� tiver entregue, se
			 * tiver entregue as saidas jah foram registradas
			 */
			// registrarHistoricoSaidasItensVenda(request, venda);

			// 26/03/2011 - desabilitando datas de pedido e vendas
			// 18/07/2011 - A data da Venda � definida na cria��o e n�o muda mais!
			//venda.setDataVenda(dataVenda);
			Fachada.getInstance().alterarVenda(venda);
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAUpdateException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		} /*
		 * catch (JPAInsertException e) {
		 * JPAUtility.rollbackTransactionGroup(em);
		 * JPAUtility.setTransacoesAtivadas(true); e.printStackTrace();
		 * adicionaErro(request, e.getMessage()); return
		 * (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA)); }
		 */
		adicionaMensagem(request, "MSG20");
		request.setAttribute(ID_VENDA, venda.getIdVenda().toString());
		return (mapping.findForward(AVANCAR_PARA_DETALHAR_VENDA));
	}

	@SuppressWarnings("unchecked")
	private void configurarResultadoConsultaAnterior(HttpServletRequest request) {
		ResultadoPaginacao<VendaTO> resultadoPaginacao = (ResultadoPaginacao<VendaTO>) request
				.getSession().getAttribute(Constantes.RESULTADO_PAGINACAO);
		if (resultadoPaginacao == null) {
			request.setAttribute(LIST_SIZE, 0);
			request.setAttribute("totalizador", "-");
			return;
		}
		List<VendaTO> vendas = resultadoPaginacao.getResult();
		// DEFININDO OS PARAMETROS DA PAGINACAO
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);
		// TRATANDO OS TOTALIZADORES
		inserirTotalizadorNoRequest(request, resultadoPaginacao, "total",
				"totalizador", "desconto", "valorServicoMontagem");
		inserirTotalizadorNoRequestDouble(request, resultadoPaginacao,
				"quantidade", "totalizador2");
		vendas = resultadoPaginacao.getResult();
		request.setAttribute(Constantes.LISTA_VENDAS_CONSULTA, vendas);
	}

	public ActionForward processarExcluir(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String stringIdVenda = request.getParameter("idVenda");
		if (GenericValidator.isBlankOrNull(stringIdVenda)) {
			stringIdVenda = (String) request.getAttribute("idVenda");
		}
		Integer idVenda = new Integer(stringIdVenda);

		VendaTO venda = Fachada.getInstance().getVenda(idVenda);

		/*
		 * if (venda.getSituacao() == Constantes.VENDA_FINALIZADA) {
		 * adicionaErro(request, "MSG24");
		 * configurarResultadoConsultaAnterior(request); return
		 * (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA)); }
		 */

		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			JPAUtility.beginTransactionGroup(em);
			for (ItemVendaTO item : venda.getItens()) {
				ProdutoTO produto = ControladorProduto.getInstance().get(
						item.getProduto().getIdProduto());
				produto.setQtdEstoque(produto.getQtdEstoque().doubleValue()
						+ item.getMetragem().doubleValue());
				ControladorProduto.getInstance().atualiza(produto);
				HistoricoProdutoTO historico = new HistoricoProdutoTO();
				UsuarioTO usuario = (UsuarioTO) request.getSession()
						.getAttribute(Constantes.USUARIO_LOGADO);
				historico.setUsuario(usuario);
				historico.setProduto(produto);
				historico.setQuantidade(item.getMetragem());
				historico.setTipo(Constantes.TIPO_ENTRADA_PRODUTO);
				String codigoVenda = "N�:" + venda.getNumeroPedidoFormatado()
						+ " - Data:" + venda.getDataVendaFormatada();
				historico.setObservacao(Constantes.OBS4.replaceAll(
						"IdVenda/usuarioX", codigoVenda + "/"
								+ recuperaUsuarioLogado(request).getLogin()
								+ ""));
				ControladorHistoricoProduto.getInstance().salva(historico);
			}
			transacaoExcluirVenda(venda);
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAUtilityException e) {
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			configurarResultadoConsultaAnterior(request);
			JPAUtility.rollbackTransactionGroup(em);
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		}
		adicionaMensagem(request, "MSG22");
		return abrirConsulta(mapping, form, request, response);
	}

	// Deve ser executado dentro da mesma transa��o.
	private void transacaoExcluirVenda(VendaTO venda)
			throws JPAInsertException, JPAUpdateException, JPADeleteException {
		// EXCLUIR ITENS DA VENDA DIRETAMENTO NO BANCO DE DADOS VIA SQL!!!
		ControladorItemVenda.getInstance().excluirItensDaVenda(
				venda.getIdVenda());
		ControladorItemBeneficiamentoVenda.getInstance()
				.excluirItensDeBeneficiamentoDaVenda(venda.getIdVenda());
		venda.getItens().clear();
		venda.getItensBeneficiamento().clear();
		ControladorVenda.getInstance().remove(venda);
	}

	public ActionForward aplicaDescontoVenda(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute(FORWARD_SECAO, SECAO_TOTALIZACAO_VENDA);
		VendaForm formulario = (VendaForm) form;

		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		BigDecimal desconto = new BigDecimal(Formatador.parseDouble(formulario
				.getDesconto()));

		if (desconto.doubleValue() < 0.0) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					"Valor do Desconto: [ " + formulario.getDesconto()
							+ " ] - Informe um valor maior que Zero.");
			double valorDesconto = venda.getDesconto() != null ? venda
					.getDesconto().doubleValue() : 0.0;
			formulario.setDesconto(Formatador.formatNumeroBR(valorDesconto));
		} else {
			venda.setDesconto(desconto);
			if (venda.getDesconto().doubleValue() > venda.getSubTotal()
					.doubleValue()) {
				venda.setDesconto(new BigDecimal(venda.getSubTotal()
						.doubleValue()));
				formulario.setDesconto(Formatador.doubleValue(venda
						.getDesconto().doubleValue()));
			}
			adicionaMensagem(request, Constantes.DESCONTO_APLICADO_SUCESSO);
		}
		configurarDadosDaVendaNaSessao(request, venda);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward aplicaValorServicoMontagem(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute(FORWARD_SECAO, SECAO_SERVICO_MONTAGEM_VENDA);
		VendaForm formulario = (VendaForm) form;

		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);

		if (!GenericValidator.isBlankOrNull(formulario
				.getValorServicoMontagem())) {
			BigDecimal servicoMontagem = new BigDecimal(
					Formatador.parseDouble(formulario.getValorServicoMontagem()));
			if (servicoMontagem.doubleValue() == 0.0) {
				adicionaErro(
						request,
						Constantes.ERRO_DADOS_INVALIDOS,
						"Valor do Servi�o de Montagem: [ "
								+ formulario.getValorServicoMontagem()
								+ " ] - Informe um valor maior que Zero.");
				double valorServicoMontagem = venda.getValorServicoMontagem() != null ? venda
						.getValorServicoMontagem().doubleValue() : 0.0;
				formulario.setValorServicoMontagem(Formatador
						.formatNumeroBR(valorServicoMontagem));
			} else {
				venda.setValorServicoMontagem(servicoMontagem);
				adicionaMensagem(request,
						Constantes.MSG_VALOR_SERVICO_ATUALIZADO_SUCESSO);
			}
		} else {
			venda.setValorServicoMontagem(BigDecimal.ZERO);
		}

		configurarDadosDaVendaNaSessao(request, venda);

		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward aplicaValorServicoMontagemAlterar(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute(FORWARD_SECAO, SECAO_SERVICO_MONTAGEM_VENDA);
		VendaForm formulario = (VendaForm) form;

		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);

		if (!GenericValidator.isBlankOrNull(formulario
				.getValorServicoMontagem())) {
			BigDecimal servicoMontagem = new BigDecimal(
					Formatador.parseDouble(formulario.getValorServicoMontagem()));
			if (servicoMontagem.doubleValue() == 0.0) {
				adicionaErro(
						request,
						Constantes.ERRO_DADOS_INVALIDOS,
						"Valor do Servi�o de Montagem: [ "
								+ formulario.getValorServicoMontagem()
								+ " ] - Informe um valor maior que Zero.");
				double valorServicoMontagem = venda.getValorServicoMontagem() != null ? venda
						.getValorServicoMontagem().doubleValue() : 0.0;
				formulario.setValorServicoMontagem(Formatador
						.formatNumeroBR(valorServicoMontagem));
			} else {
				venda.setValorServicoMontagem(servicoMontagem);
				adicionaMensagem(request,
						Constantes.MSG_VALOR_SERVICO_ATUALIZADO_SUCESSO);
			}
		} else {
			venda.setValorServicoMontagem(BigDecimal.ZERO);
		}

		configurarDadosDaVendaNaSessao(request, venda);

		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	public ActionForward aplicaDescontoVendaPadrao(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		aplicaDescontoVendaPadraoGenerico(form, request);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR));
	}

	public ActionForward aplicaDescontoVendaPadraoAlterar(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		aplicaDescontoVendaPadraoGenerico(form, request);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	private void aplicaDescontoVendaPadraoGenerico(ActionForm form,
			HttpServletRequest request) {
		adicionaMensagem(request, Constantes.DESCONTO_APLICADO_SUCESSO);
		request.setAttribute(FORWARD_SECAO, SECAO_TOTALIZACAO_VENDA);
		VendaForm formulario = (VendaForm) form;
		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		BigDecimal valorDesconto = new BigDecimal(venda.getSubTotal()
				.doubleValue()).multiply(new BigDecimal(VALOR_DESCONTO_PADRAO));
		venda.setDesconto(valorDesconto);
		formulario.setDesconto(Formatador.formatNumeroBR(venda.getDesconto()
				.doubleValue()));
		request.getSession().setAttribute(Constantes.VENDA_CLIENTE, venda);
		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
		request.setAttribute(Constantes.LISTA_ITENS_VENDA_BENEFICIAMENTO,
				venda.getItensBeneficiamento());
		configurarDadosDaVendaNaSessao(request, venda);
	}

	public ActionForward aplicaDescontoVendaAlterar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute(FORWARD_SECAO, SECAO_TOTALIZACAO_VENDA);
		VendaForm formulario = (VendaForm) form;

		VendaTO venda = (VendaTO) request.getSession().getAttribute(
				Constantes.VENDA_CLIENTE);
		BigDecimal desconto = new BigDecimal(Formatador.parseDouble(formulario
				.getDesconto()));

		if (desconto.doubleValue() < 0.0) {
			adicionaErro(request, Constantes.ERRO_DADOS_INVALIDOS,
					"Valor do Desconto: [ " + formulario.getDesconto()
							+ " ] - Informe um valor maior que Zero.");
			double valorDesconto = venda.getDesconto() != null ? venda
					.getDesconto().doubleValue() : 0.0;
			formulario.setDesconto(Formatador.formatNumeroBR(valorDesconto));
		} else {
			venda.setDesconto(desconto);
			if (venda.getDesconto().doubleValue() > venda.getSubTotal()
					.doubleValue()) {
				venda.setDesconto(new BigDecimal(venda.getSubTotal()
						.doubleValue()));
				formulario.setDesconto(Formatador.doubleValue(venda
						.getDesconto().doubleValue()));
			}
			adicionaMensagem(request, Constantes.DESCONTO_APLICADO_SUCESSO);
		}

		configurarDadosDaVendaNaSessao(request, venda);
		return (mapping.findForward(Constantes.FORWARD_CONTINUAR_ALTERAR));
	}

	public ActionForward detalhar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String stringIdVenda = request.getParameter("idVenda");
		if (GenericValidator.isBlankOrNull(stringIdVenda)) {
			stringIdVenda = (String) request.getAttribute("idVenda");
		}
		Integer idVenda = new Integer(stringIdVenda);

		VendaTO venda = Fachada.getInstance().getVenda(idVenda);
		request.setAttribute(Constantes.VENDA_CLIENTE, venda);
		request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
		request.setAttribute(Constantes.LISTA_ITENS_VENDA_BENEFICIAMENTO,
				venda.getItensBeneficiamento());

		return (mapping.findForward(Constantes.FORWARD_DETALHAR));
	}

	public ActionForward abrirConsulta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
		cleanForm(formulario);
		List<ClienteTO> clientes = Fachada.getInstance().listarClientes();
		request.getSession().setAttribute(Constantes.LISTA_CLIENTES, clientes);
		request.setAttribute(LIST_SIZE, 0);
		request.setAttribute("totalizador", "-");

		return processarConsulta(mapping, formulario, request, response);
	}

	public ActionForward processarConsulta(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		VendaForm formulario = (VendaForm) form;
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
				parametros.put("dataFim",
						Formatador.converterStringParaDate(
								formulario.getDataFim(),
								Formatador.FORMATO_DATA_PADRAO));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (GenericValidator.isInt(formulario.getIdCliente())) {
			parametros.put("idCliente", new Integer(formulario.getIdCliente()));
		}

		if (!GenericValidator.isBlankOrNull(formulario.getSituacaoVenda())) {
			parametros.put("situacao", new Character(formulario
					.getSituacaoVenda().charAt(0)));
		}

		String numeroPedido = "";
		if (!GenericValidator.isBlankOrNull(formulario.getNumeroPedido())
				&& !GenericValidator.isBlankOrNull(formulario
						.getNumeroPedidoAno())) {
			numeroPedido = formulario.getNumeroPedido() + "/"
					+ formulario.getNumeroPedidoAno();
		} else if (!GenericValidator
				.isBlankOrNull(formulario.getNumeroPedido())) {
			numeroPedido = formulario.getNumeroPedido() + "/%";
		} else if (!GenericValidator.isBlankOrNull(formulario
				.getNumeroPedidoAno())) {
			numeroPedido = "%/" + formulario.getNumeroPedidoAno();
		}

		if (!numeroPedido.isEmpty()) {
			parametros.put("numeroPedido", numeroPedido);
		}

		List<VendaTO> vendas = null;
		int pageNumber = recuperarPaginaAtual(request);
		List<String> totalizadores = new ArrayList<String>();
		totalizadores.add("total");
		
		
		List<String> totalizadores2 = new ArrayList<String>();
		totalizadores2.add("total");
		
		List<String> totalizadores3 = new ArrayList<String>();		
		totalizadores3.add("desconto");
		totalizadores3.add("valorServicoMontagem");
		
		ResultadoPaginacao<VendaTO> resultadoPaginacao = Fachada.getInstance()
				.consultarVendaPaginado(parametros, pageNumber, getPageSize(),
						totalizadores, totalizadores2, totalizadores3);
		vendas = resultadoPaginacao.getResult();
		// DEFININDO OS PARAMETROS DA PAGINACAO
		request.setAttribute(LIST_SIZE, resultadoPaginacao.getTotalRegistros());
		defineParametroRegistros(request, resultadoPaginacao);

		// TRATANDO OS TOTALIZADORES
		inserirTotalizadorNoRequest(request, resultadoPaginacao, "total",
				"totalizador", "desconto", "valorServicoMontagem");

		inserirTotalizadorNoRequestDouble(request, resultadoPaginacao,
				"quantidade", "totalizador2");

		vendas = resultadoPaginacao.getResult();

		request.setAttribute(Constantes.LISTA_VENDAS_CONSULTA, vendas);
		request.getSession().setAttribute(Constantes.RESULTADO_PAGINACAO,
				resultadoPaginacao);

		return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward gerar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		List<VendaTO> vendas = null;
		ResultadoPaginacao<VendaTO> resultado = (ResultadoPaginacao<VendaTO>) request
				.getSession().getAttribute("resultadoPaginacao");
		if (resultado != null) {
			// Refazendo a consulta para recuperar os registros de todas as
			// p�gina e n�o apenas da p�gina atual.
			ResultadoPaginacao resultadoCompleto = Fachada.getInstance()
					.consultarVendaPaginado(
							resultado.getParametrosDaConsulta(), 1,
							resultado.getTotalRegistros() + 1, null, null, null);
			if (resultadoCompleto != null) {
				vendas = resultadoCompleto.getResult();
			}
		}
		if (vendas != null && !vendas.isEmpty()) {
			List<VendaRelatorioTO> vendasRelatorio = new ArrayList<VendaRelatorioTO>();
			for (VendaTO vendaTO : vendas) {
				VendaRelatorioTO vendaRelatorio = new VendaRelatorioTO();
				vendaRelatorio.setCodigo(vendaTO.getNumeroPedidoFormatado());
				vendaRelatorio
						.setData(Formatador.converterDataString(
								vendaTO.getDataVenda(),
								Formatador.FORMATO_DATA_PADRAO));
				if (vendaTO.getDesconto() != null) {
					vendaRelatorio.setDesconto(vendaTO.getDesconto()
							.doubleValue());
				} else {
					vendaRelatorio.setDesconto(0.0);
				}
				if (vendaTO.getCliente() != null
						&& vendaTO.getCliente().getNome() != null) {
					vendaRelatorio.setNomeCliente(vendaTO.getCliente()
							.getNome());
				}
				vendaRelatorio.setTotal(vendaTO.getTotal().doubleValue());
				vendaRelatorio.setValor(vendaTO.getSubTotal().doubleValue());
				vendaRelatorio.setValorEntrega(0.0);
				vendasRelatorio.add(vendaRelatorio);
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

				String pathImages = request.getSession().getServletContext()
						.getRealPath("/images");
				parametrosRelatorio.put("pathImages", pathImages);

				JasperPrint jprint = null;
				jprint = JasperFillManager.fillReport(pathJasper
						+ File.separator + "relatoriolistavendas.jasper",
						parametrosRelatorio, new JRBeanCollectionDataSource(
								vendasRelatorio));

				this.gerarSaidaPDF(response, jprint, "ListaDeVendas");

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

	public ActionForward imprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return imprimirGenerico(mapping, form, request, response,
				"detalhavenda.jasper");
	}

	public ActionForward imprimirParaCliente(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return imprimirGenerico(mapping, form, request, response,
				"detalhavendacliente.jasper");
	}

	public ActionForward imprimirGenerico(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String nomeRelatorio) {
		Integer idVenda = new Integer(request.getParameter("idVenda"));
		try {
			VendaTO venda = Fachada.getInstance().getVenda(idVenda);
			request.setAttribute(Constantes.VENDA_CLIENTE, venda);
			request.setAttribute(Constantes.LISTA_ITENS_VENDA, venda.getItens());
			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("venda.idVenda", venda.getIdVenda());
			int itens = 0;
			List<Object> clAttrs = new ArrayList<Object>();
			for (ItemVendaTO item : venda.getItens()) {
				RelatorioDetalharVendaTO relatorio = new RelatorioDetalharVendaTO();
				relatorio.setDescricao(item.getDescricao());
				relatorio.setProduto(item.getProduto()
						.getDescricaoTipoSubTipo());
				relatorio.setMetragem(item.getMetragem().doubleValue());
				relatorio.setPreco(item.getPreco().doubleValue());
				relatorio.setTotal(item.getPreco().doubleValue());
				clAttrs.add(relatorio);
				itens++;
			}
			for (ItemBeneficiamentoVendaTO item : venda
					.getItensBeneficiamento()) {
				RelatorioDetalharVendaTO relatorio = new RelatorioDetalharVendaTO();
				relatorio.setDescricao(item.getDescricao());
				relatorio.setProduto("Item Beneficiamento");
				relatorio.setMetragem(0.0);
				relatorio.setPreco(item.getValor().doubleValue());
				relatorio.setTotal(item.getValor().doubleValue());
				clAttrs.add(relatorio);
				itens++;
			}
			
			if (itens == 0){
				RelatorioDetalharVendaTO relatorio = new RelatorioDetalharVendaTO();
				relatorio.setDescricao("-");
				relatorio.setProduto("-");
				relatorio.setMetragem(0.0);
				relatorio.setPreco(0.0);
				relatorio.setTotal(0.0);
				clAttrs.add(relatorio);
			}
			// par�metros
			Map<String, Object> parametrosRelatorio = new HashMap<String, Object>();
			parametrosRelatorio.put("dataVenda", venda.getDataVendaFormatada());
			parametrosRelatorio
					.put("nomeCliente", venda.getCliente().getNome());
			parametrosRelatorio.put("subTotal", venda.getSubTotal()
					.doubleValue());

			parametrosRelatorio.put("cidade", venda.getCliente()
					.getNomeCidade());
			parametrosRelatorio.put("estado", venda.getCliente()
					.getNomeEstado());
			parametrosRelatorio.put("cep", venda.getCliente().getCep());
			parametrosRelatorio.put("endereco", venda.getCliente()
					.getEndereco());
			parametrosRelatorio.put("bairro", venda.getCliente().getBairro());
			parametrosRelatorio.put("residencial", venda.getCliente()
					.getTelefoneFixo());
			parametrosRelatorio.put("celular", venda.getCliente()
					.getTelefoneCelular());
			parametrosRelatorio.put("fax", venda.getCliente().getTelefoneFax());
			parametrosRelatorio.put("email", venda.getCliente().getEmail());

			if (venda.getValorServicoMontagem() != null) {
				parametrosRelatorio.put("montagem", venda
						.getValorServicoMontagem().doubleValue());
			} else {
				parametrosRelatorio.put("montagem", 0.0);
			}

			parametrosRelatorio.put("dataEntrega",
					venda.getPrazoEntregaFormatado());
			parametrosRelatorio.put("numeroPedido", venda.getNumeroPedido());

			parametrosRelatorio.put("aVista", venda.getValorVistaFormatado());

			parametrosRelatorio.put("aberto", venda.getValorAbertoFormatado());

			parametrosRelatorio.put("cheque", venda.getValorChequeFormatado());

			parametrosRelatorio
					.put("credito", venda.getValorCreditoFormatado());

			parametrosRelatorio.put("boleto", venda.getValorBoletoFormatado());

			parametrosRelatorio.put("observacoes", venda.getObservacao());

			if (venda.getDesconto() != null)
				parametrosRelatorio.put("desconto", venda.getDesconto()
						.doubleValue());

			parametrosRelatorio.put("totalVenda", venda.getTotal()
					.doubleValue());

			parametrosRelatorio.put("entregue",
					venda.isProdutosEntregues() ? "Sim" : "N�o");

			// Na variavel pathJasper ficara o caminho do diret�rio para
			// os relat�rios compilados (.jasper)
			String pathJasper = request.getSession().getServletContext()
					.getRealPath("/relatorios");

			String pathImages = request.getSession().getServletContext()
					.getRealPath("/images");
			parametrosRelatorio.put("pathImages", pathImages);

			JasperPrint jprint = null;
			jprint = JasperFillManager.fillReport(pathJasper + File.separator
					+ nomeRelatorio, parametrosRelatorio,
					new JRBeanCollectionDataSource(clAttrs));

			this.gerarSaidaPDF(response, jprint, "Venda");

			// Grava o relat�rio em disco em pdf
			// JasperExportManager.exportReportToPdfFile(jprint, path
			// + "/relatVenda.pdf");

			// Redireciona para o pdf gerado
			// response.sendRedirect("relatVenda.pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ActionForward processarRetornarStatusPendente(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String stringIdVenda = request.getParameter("idVenda");
		if (GenericValidator.isBlankOrNull(stringIdVenda)) {
			stringIdVenda = (String) request.getAttribute("idVenda");
		}
		Integer idVenda = new Integer(stringIdVenda);

		VendaTO venda = Fachada.getInstance().getVenda(idVenda);
		
		venda.setProdutosEntregues(false);
		venda.setPrazoEntrega(null);
		venda.setSituacao(Constantes.VENDA_PENDENTE);

		JPAUtility.setTransacoesAtivadas(false);
		EntityManager em = JPAUtility.getEntityManager();
		try {
			registrarHistoricoEntradasItensVenda(request, venda);
			JPAUtility.beginTransactionGroup(em);
			
			
			
			Fachada.getInstance().alterarVenda(venda);
			JPAUtility.commitTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
		} catch (JPAUpdateException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		} catch (JPAInsertException e) {
			JPAUtility.rollbackTransactionGroup(em);
			JPAUtility.setTransacoesAtivadas(true);
			e.printStackTrace();
			adicionaErro(request, e.getMessage());
			return (mapping.findForward(Constantes.FORWARD_ABRIR_CONSULTA));
		} 
		adicionaMensagem(request, "MSG25");
		request.setAttribute(ID_VENDA, venda.getIdVenda().toString());
		return (mapping.findForward(AVANCAR_PARA_DETALHAR_VENDA));
	}

	
	

	@Override
	protected String getTableId() {
		return "venda";
	}
}