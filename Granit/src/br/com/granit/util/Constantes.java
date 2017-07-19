package br.com.granit.util;

public class Constantes {

	
	public static final String LISTA_ESTADOS = "estados";
	public static final String LISTA_MUNICIPIOS = "municipios";
	public static final String LISTA_REFERENCIA_TEMPO = "referenciasTempo";	
	public static final String LISTA_CLIENTES_CONSULTA = "listaClientesConsulta";
	public static final String LISTA_FORNECEDORES_CONSULTA = "listaFornecedoresConsulta";
	public static String FORWARD_SUCESSO = "cadastroSucesso";	
	public static final String FORWARD_CANCELAR = "cancelar";
	public static final String VALOR_PADRAO_INT = "0";
	public static final String VALOR_PADRAO_REAL = "0,00";
	public static final String VALOR_PADRAO_REAL_3_CASAS = "0,000";
	
	
	
	public static final String FORWARD_CONTINUAR = "continuar";
	public static final String FORWARD_CONTINUAR_ALTERAR = "continuarAlterar";
	public static final String LISTA_CLIENTES_VENDAS = "listaClientesVendas";
	
	/**
	 * C�digo da mensagem de erro de inser��o no banco de dados.
	 */
	public static final String MSG_ERRO_INSERCAO = "MSG_ERRO_INSERCAO";
	
	/**
	 * C�digo da mensagem de erro de atualiza��o no banco de dados.
	 */
	public static final String MSG_ERRO_ATUALIZACAO = "MSG_ERRO_ATUALIZACAO";
	
	/**
	 * C�digo da mensagem de erro de remo��o no banco de dados.
	 */
	public static final String MSG_ERRO_REMOCAO = "MSG_ERRO_REMOCAO";
	
	/**
	 * C�digo da mensagem de erro de consulta no banco de dados.
	 */
	public static final String MSG_ERRO_CONSULTA = "MSG_ERRO_CONSULTA";
	
	
	/**
	 * Constantes JPA
	 */
	public static final String UNCHECKED = "unchecked";
	
	public static final String NAMEDQUERY_PRODUTO_NOME_DESCRICAO = "produto_nome_descricao";
	
	public static final String NAMEDQUERY_PRODUTO_ESTOQUE_PRECOVENDA = "produto_estoque_precovenda";
	
	/**
	 * Lista munic�pios ordenados pelo nome.
	 */
	public static final String NAMEDQUERY_LISTA_MUNICIPIOS = "listaMunicipios";
	
	/**
	 * Par�metro "sigla", a sigla do estado. Lista munic�pios de um estado ordenados pelo nome.
	 * */
	public static final String NAMEDQUERY_LISTA_MUNICIPIOS_POR_ESTADO = "listaMunicipiosPorEstado";
	public static final String NAMEDQUERY_LISTA_SUBTIPO_POR_TIPO_PRODUTO = "listaSubTipoPorTipoProduto";
	
	/**
	 * String vazia
	 */
	public static final String VAZIO = "";
	
	public static final String CODIGO = "codigo";	
	public static final String FORWARD_ABRIR = "abrir";
	public static final String FORWARD_ADICIONAR = "adicionar";
	public static final String FORWARD_CONSULTA = "consulta";
	public static final String FORWARD_CONSULTA_ABRIR = "consultaAbrir";
	public static final String FORWARD_ALTERAR = "alterar";
	public static final String FORWARD_EDITAR = "editar";
	public static final String FORWARD_DETALHAR = "detalhar";
	public static final String FORWARD_REGISTRAR_ENTRADA_SAIDA = "registrarEntradaSaida";
	public static final String MSG_EXIBIR_SUCESSO = "exibirMensagemSucesso";
	public static final String MSG_CADASDTRO_SUCESSO = "cadastro.realizado.sucesso";
	public static final String FORWARD_CADASTRO_SUCESSO = "cadastroSucesso";
	public static final String ERRO_OBRIGATORIOS = "erro.campos.obrigatorios";	
	public static final String ERRO_TESTE = "erro.teste";
	
	

	public static final String FALSE = "false";
	public static final String TRUE = "true";	
	public static final String ERRO_CAMPO_MAIOR_IGUAL_ZERO_MONETARIO = "erro.campo.maior.igual.zero.monetario";
	public static final String ERRO_CAMPO_MAIOR_ZERO = "erro.campo.maior.zero";	
	public static final String FORWARD_EXIBIR = "exibir";
	
	public static final String ITEM_INCLUIDO_SUCESSO = "item.incluido.sucesso";
	
	public static final String ITEM_REMOVIDO_SUCESSO = "item.removido.sucesso";
	
	public static final String DESCONTO_APLICADO_SUCESSO = "desconto.aplicado.sucesso";
	
	public static final String INCLUSAO_SUCESSO = "MSG02";
	
	public static final String ALTERACAO_SUCESSO = "MSG06";
	
	public static final String EXCLUSAO_SUCESSO = "msg.exclusao.sucesso";
	
	public static final String ERRO_SELECIONE_ENTIDADE = "erro.selecione.entidade";
	
	public static final String MSG_CAMPOS_INVALIDOS = "erro.campos.invalidos";
	
	public static final String TIPO_FECHAR = "tipoFechar";
	public static final String FORWARD_INDEX = "index";
	
	public static final String ERRO_DADOS_INVALIDOS = "erro.dados.invalidos";
	
	public static final String FORWARD_PAGINA_INICIAL = "paginaInicial";
	
	
	
	public static final String USUARIO_LOGADO = "usuarioLogado";
	
	public static final String SENHA_ATUAL_DIFERENTE_CONFIRMA_SENHA = "confirma.senha.invalida";
	
	public static final String SENHA_MINIMO_CARACTERES = "senha.minimo.caracteres";
	
	public static final String SENHA_ATUAL_INVALIDA = "senha.atual.incorreta";
	
	public static final String SESSAO_EXPIROU = "sessao.expirou";
	
	public static final String VALOR_PADRAO_REAL_4_CASAS = "0,0000";	
	

	//Relatorios
	public static final String PARAM_PADROES_HORA_ATUAL = "horaAtual";
	public static final String REL_CABECALHO_COL_CODIGO = "codigo";
	public static final String REL_CABECALHO_COL_NOME = "nome";
	public static final String REL_CABECALHO_COL_CNPJ_CPF = "cnpjcpf";
	
	public static final String RESULTADO_PAGINACAO = "resultadoPaginacao";	
	
	public static final String ERRO_CONSTRAINT_EXCLUSAO = "erro.constraint.exclusao";
	
	public static final String ERRO_FINALIZAR_PARCELA_RECEBER = "erro.finalizar.parcela.receber";
	
	public static final String ERRO_FINALIZAR_PARCELA_RECEBER_EMPRESTIMO = "erro.finalizar.parcela.receber.emprestimo";
	
	public static final String ERRO_FINALIZAR_PARCELA_CONTA_PAGAR = "erro.finalizar.parcela.conta.pagar";
	

	public static final String ERRO_OPERACAO_NAO_PERMITIDA = "erro.operacao.nao.permitida";
	public static final String LISTA_PERFIS = "listaPerfis";
	public static final String LISTA_USUARIOS_CONSULTA = "listaUsuarios";
	public static final String MSG_OPERACAO_SUCESSO = "operacao.realizada.sucesso";
	
	public static final String MSG_VALOR_SERVICO_ATUALIZADO_SUCESSO = "valor.servico.atualizado.sucesso";
	
	//Cliente j� cadastrado
	public static final String MSG01 = "MSG01";
	
	//fornecedor j� cadastrado
	public static final String MSG07 = "MSG07";
	
	//remo��o de fornecedor com sucesso
	public static final String MSG09 = "MSG09";
	
	public static final String MSG10 = "MSG10";
	
	public static final String LISTA_PRODUTOS_CONSULTA = "listaProdutosConsulta";
	
	public static final String LISTA_HISTORICO_PRODUTOS_CONSULTA = "listaHistoricoProdutosConsulta";
	
	
	
	public static final String LISTA_PRODUTOS_CONSULTA_REGISTRA_SAIDA = "listaProdutosConsultaRegistraSaida";
	
	public static final char TIPO_ENTRADA_PRODUTO = 'E';
	
	public static final char TIPO_SAIDA_PRODUTO = 'S';
	
	public static final String LISTA_CLIENTES = "listaClientesVendas";
	
	public static final String VENDA_CLIENTE = "vendaCliente";
	
	public static final String LISTA_ITENS_VENDA = "listaItensVenda";
	
	public static final String LISTA_ITENS_VENDA_BENEFICIAMENTO = "listaItensVendaBeneficiamento";
	
	public static final String LISTA_ITENS_PEDIDO = "listaItensPedido";
	
	public static final String PEDIDO = "pedido";
	public static final String MSG_PEDIDO_FINALIZADO_SUCESSO = "MSG16";
	public static final String LISTA_PEDIDOS_CONSULTA = "listaPedidosConsulta";
	public static final String MSG18 = "MSG18";
	
	public static final String VALOR_DE_VENDA_INVALIDO = " O valor informado nas formas de pagamento est� DIFERENTE do valor da Venda.";
	
	public static final char VENDA_PENDENTE = 'P';
	
	public static final char VENDA_FINALIZADA = 'F';
	
	public static final String LISTA_VENDAS_CONSULTA = "listaVendasConsulta";
	
	public static final String FORWARD_ABRIR_CONSULTA = "abrirConsulta";
	public static final String LISTA_HISTORICO_PRODUTO = "listaHistoricoProduto";
	
	public static final String NAMEDQUERY_LISTA_PRODUTO_FORNECEDOR_POR_PRODUTO= "listaProdutoFornecedorPorProduto";

	public static final int TIPO_MATERIA_PRIMA = 1;
	public static final int TIPO_INSUMO = 2;
	public static final int TIPO_DIVERSOS = 3;
	public static final String FORWARD_CONSULTA_RELATORIO_ENTRADA_SAIDA = "consultaRelatorioEntradaSaida";
	
	public static String OBS1 = "Venda/Sa�da/IdVenda/usuarioX";
	public static String OBS2 = "Pedido/Entrada/IdPedido/usuarioX";
	public static String OBS3 = "Pedido/Sa�da/IdPedido/usuarioX/Cancelamento de pedido";
	public static String OBS4 = "Venda/Entrada/IdVenda/usuarioX/Cancelamento de venda";
	public static String OBS5 = "Registro feito pelo usuarioX"; 
	public static String OBS6 = "Cria��o inicial/Entrada/usuarioX";
	public static String OBS7 = "Altera��o de produto sem historico de entrada/saida registrado/Entrada/usuarioX";
	public static String OBS8 = "Altera��o de produto com historico de entrada/saida registrado/Entrada/usuarioX";
	public static String OBS9 = "Altera��o de produto com historico de entrada/saida registrado/Saida/usuarioX";
	public static String OBS10 = "Venda/Entrada/IdVenda/usuarioX/Altera��o de status para pendente";
}
