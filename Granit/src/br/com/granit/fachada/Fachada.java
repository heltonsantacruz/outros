package br.com.granit.fachada;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.granit.controlador.ControladorCliente;
import br.com.granit.controlador.ControladorEstado;
import br.com.granit.controlador.ControladorFornecedor;
import br.com.granit.controlador.ControladorHistoricoProduto;
import br.com.granit.controlador.ControladorItemPedido;
import br.com.granit.controlador.ControladorMunicipio;
import br.com.granit.controlador.ControladorPedido;
import br.com.granit.controlador.ControladorPerfil;
import br.com.granit.controlador.ControladorProduto;
import br.com.granit.controlador.ControladorProdutoFornecedor;
import br.com.granit.controlador.ControladorSubTipoProduto;
import br.com.granit.controlador.ControladorUsuario;
import br.com.granit.controlador.ControladorVenda;
import br.com.granit.persistencia.excecao.JPADeleteException;
import br.com.granit.persistencia.excecao.JPAInsertException;
import br.com.granit.persistencia.excecao.JPAQueryException;
import br.com.granit.persistencia.excecao.JPAUpdateException;
import br.com.granit.persistencia.filtro.FiltroHistoricoProduto;
import br.com.granit.persistencia.filtro.FiltroUsuario;
import br.com.granit.persistencia.to.ClienteTO;
import br.com.granit.persistencia.to.EstadoTO;
import br.com.granit.persistencia.to.FornecedorTO;
import br.com.granit.persistencia.to.HistoricoProdutoTO;
import br.com.granit.persistencia.to.MunicipioTO;
import br.com.granit.persistencia.to.PedidoTO;
import br.com.granit.persistencia.to.PerfilTO;
import br.com.granit.persistencia.to.ProdutoFornecedorTO;
import br.com.granit.persistencia.to.ProdutoTO;
import br.com.granit.persistencia.to.SubTipoProdutoTO;
import br.com.granit.persistencia.to.UsuarioTO;
import br.com.granit.persistencia.to.VendaTO;
import br.com.granit.util.Constantes;
import br.com.granit.util.ResultadoPaginacao;

public class Fachada {

	private static Fachada instancia;
	
	
	private Fachada(){		
		
	}
	
	public static Fachada getInstance(){
		if (instancia == null)
			instancia = new Fachada();
		return instancia;
	}
	
	/**
	 {@link EstadoTO}
	 */
	public List<EstadoTO> consultarEstado(){
		return ControladorEstado.getInstance().listar();
	}
	
	/**
	 {@link MunicipioTO}
	 */
	public List<MunicipioTO> consultarMuncipioPorEstado(Map<String, Object> parametros){			
		return ControladorMunicipio.getInstance().consultarPorQuery(
				Constantes.NAMEDQUERY_LISTA_MUNICIPIOS_POR_ESTADO, parametros);
	}
	
	public MunicipioTO getMunicipio(Integer idMunicipio) {
		return ControladorMunicipio.getInstance().get(idMunicipio);
	}

	/**
	 {@link ClienteTO}
	 */
	public ClienteTO salvaCliente(ClienteTO cliente) throws JPAInsertException {
		return ControladorCliente.getInstance().salva(cliente);		
	}
	
	public ClienteTO getCliente(Integer pk) {
		return ControladorCliente.getInstance().get(pk);
	}

	public void alterarCliente(ClienteTO cliente) throws JPAUpdateException {
		ControladorCliente.getInstance().atualiza(cliente);
	}

	public List<ClienteTO> consultarClientes() {
		return ControladorCliente.getInstance().listar();
	}

	public List<ClienteTO> consultarCliente(Map<String, Object> parametros) {
		return ControladorCliente.getInstance().consultar(parametros);
	}
	
	@SuppressWarnings("unchecked")
	public ResultadoPaginacao<ClienteTO> consultarClientePaginado(Map<String, Object> parametros, int pagina, int tuplasPorPagina) {
		return ControladorCliente.getInstance().consultarPaginado(parametros, pagina, tuplasPorPagina);
	}
	
	@SuppressWarnings("unchecked")
	public ResultadoPaginacao<FornecedorTO> consultarFornecedorPaginado(Map<String, Object> parametros, int pagina, int tuplasPorPagina) {
		return ControladorFornecedor.getInstance().consultarPaginado(parametros, pagina, tuplasPorPagina);
	}

	public void removeCliente(ClienteTO cliente) throws JPADeleteException {
		ControladorCliente.getInstance().remove(cliente);		
	}	
	
	public PerfilTO getPerfil(Integer pk) {
		return ControladorPerfil.getInstance().get(pk);
	}

	public List<PerfilTO> consultarPerfis() {
		ControladorPerfil controladorPerfil = ControladorPerfil.getInstance();
		return controladorPerfil.listar();
	}
	
	public UsuarioTO getUsuario(Integer pk) {
		return ControladorUsuario.getInstance().get(pk);
	}

	@SuppressWarnings("unchecked")
	public ResultadoPaginacao<UsuarioTO> consultarUsuarioPaginado(
			Map<String, Object> parametros, int pagina, int tuplasPorPagina) {
		return ControladorUsuario.getInstance().consultarPaginado(parametros, pagina, tuplasPorPagina);
	}
	
	public List<UsuarioTO> consultarUsuario(FiltroUsuario filtro) {
		return  ControladorUsuario.getInstance().consultarFiltro(filtro);
	}

	public void removeUsuario(UsuarioTO usuario)  throws JPADeleteException{
		ControladorUsuario.getInstance().remove(usuario);
		
	}

	public void alterarUsuario(UsuarioTO usuarioAlterado)throws JPAUpdateException {
			ControladorUsuario.getInstance().atualiza(usuarioAlterado);
	}
	
	/**
	 * {@link ProdutoTO}
	 */
	public ProdutoTO getProduto(Integer idProduto){		
		ControladorProduto controladorProduto = ControladorProduto.getInstance();
		return controladorProduto.get(idProduto);
	}	
	
	public ProdutoTO salvaProduto(ProdutoTO produto) throws JPAInsertException{
		ControladorProduto controladorProduto = ControladorProduto.getInstance();
		return controladorProduto.salva(produto);
	}
	
	public void alterarProduto(ProdutoTO produto) throws JPAUpdateException {
		ControladorProduto.getInstance().atualiza(produto);
	}
	
	public void removeProduto(ProdutoTO produto) throws JPADeleteException{
		ControladorProduto controladorProduto = ControladorProduto.getInstance();
		controladorProduto.remove(produto);
	}
	
	public List<ProdutoTO> consultarProduto(){
		ControladorProduto controladorProduto = ControladorProduto.getInstance();
		return controladorProduto.listar();
	}
	
	public List<ProdutoTO> consultarProduto(Map<String, Object> parametros) throws JPAQueryException{
		ControladorProduto controladorProduto = ControladorProduto.getInstance();
		return controladorProduto.consultar(parametros);
	}

	public FornecedorTO salvaFornecedor(FornecedorTO fornecedor) throws JPAInsertException {
		ControladorFornecedor controlador = ControladorFornecedor.getInstance();
		return controlador.salva(fornecedor);
	}

	public FornecedorTO getFornecedor(Integer pk) {
		return ControladorFornecedor.getInstance().get(pk);
	}

	public void removeFornecedor(FornecedorTO fornecedor) throws JPADeleteException {
		ControladorFornecedor.getInstance().remove(fornecedor);
		
	}

	public void alterarFornecedor(FornecedorTO fornecedor) throws JPAUpdateException {
		ControladorFornecedor.getInstance().atualiza(fornecedor);
	}

	public List<FornecedorTO> consultarFornecedor() {
		ControladorFornecedor controladorFornecedor = ControladorFornecedor.getInstance();
		return controladorFornecedor.listar();
	}

	public List<ClienteTO> listarClientes() {
		ControladorCliente controladorCliente = ControladorCliente.getInstance();
		return controladorCliente.listar();
	}

	public void salvaVenda(VendaTO venda) throws JPAInsertException {
		ControladorVenda controladorVenda = ControladorVenda.getInstance();
		controladorVenda.salva(venda);
	}

	public PedidoTO salvaPedido(PedidoTO pedido) throws JPAInsertException {
		ControladorPedido controlador = ControladorPedido.getInstance();
		return controlador.salva(pedido);	
		
	}

	public PedidoTO getPedido(Integer idPedido) {
		ControladorPedido controladorPedido = ControladorPedido.getInstance();
		return controladorPedido.get(idPedido);
	}

	@SuppressWarnings("unchecked")
	public ResultadoPaginacao<PedidoTO> consultarPedidoPaginado(
			Map<String, Object> parametros, int pagina, int tuplasPorPagina) {
		return ControladorPedido.getInstance().consultarPaginado(parametros, pagina, tuplasPorPagina);
	}

	public void removePedido(PedidoTO pedido) throws JPADeleteException {
		ControladorPedido.getInstance().remove(pedido);
		
	}
	
	public void alterarPedido(PedidoTO pedido) throws JPAUpdateException {
		ControladorPedido.getInstance().atualiza(pedido);
	}
	
	public void excluirItensDoPedido(Integer idPedido) {
		ControladorItemPedido.getInstance().excluirItensDoPedido(idPedido);	
		
	}

	public void excluirProdutosDoFornecedor(Integer idFornecedor) {
		ControladorFornecedor.getInstance().excluirProdutosDoFornecedor(idFornecedor);
		
	}	
	
	public VendaTO getVenda(Integer idVenda) {
		ControladorVenda controladorVenda = ControladorVenda.getInstance();
		return controladorVenda.get(idVenda);
	}
	
	public ResultadoPaginacao<VendaTO> consultarVendaPaginado(Map<String, Object> parametros, int pagina, int tuplasPorPagina, List<String> totalizadores, 
			List<String> totalizadores2, List<String> totalizadores3) {
		return ControladorVenda.getInstance().consultarPaginado(parametros, pagina, tuplasPorPagina, totalizadores, totalizadores2, totalizadores3);
	}

	public List<ProdutoFornecedorTO> listarProdutoFornecedor(Map<String, Object> parametros) {		
		ControladorProdutoFornecedor controladorProdutoFornecedor = ControladorProdutoFornecedor.getInstance();
		return controladorProdutoFornecedor.consultarPorQuery(Constantes.NAMEDQUERY_LISTA_PRODUTO_FORNECEDOR_POR_PRODUTO, parametros);
	}

	public void alterarVenda(VendaTO venda) throws JPAUpdateException {
		ControladorVenda.getInstance().atualiza(venda);
	}

	public void excluirVenda(VendaTO venda) throws JPADeleteException {
		ControladorVenda.getInstance().remove(venda);
	}

	public void alteraVenda(VendaTO venda) throws JPAUpdateException {
		ControladorVenda.getInstance().atualiza(venda);		
	}

	public List<SubTipoProdutoTO> consultarSubTipoPorTipo(Map<String, Object> parametros) {
		return ControladorSubTipoProduto.getInstance().consultarPorQuery(
				Constantes.NAMEDQUERY_LISTA_SUBTIPO_POR_TIPO_PRODUTO, parametros);
	}

	public SubTipoProdutoTO getSubTipo(Integer idSubTipo) {
		return ControladorSubTipoProduto.getInstance().get(idSubTipo);
	}
	
	public List<HistoricoProdutoTO> consultaHistoricosProduto(
			Integer idProduto, Date dataInicio, Date dataFim) {
		FiltroHistoricoProduto filtro = new FiltroHistoricoProduto();
		filtro.setIdProduto(idProduto);
		filtro.setDataFim(dataFim);
		filtro.setDataInicio(dataInicio);
		return ControladorHistoricoProduto.getInstance()
				.consultarHistoricosFiltro(filtro);
	}

	public List<HistoricoProdutoTO> consultaHistoricosProdutoPorFiltro(
			FiltroHistoricoProduto filtro) {
		return ControladorHistoricoProduto.getInstance()
				.consultarHistoricosFiltro(filtro);
	}

	public List<HistoricoProdutoTO> consultaHistoricosProdutoPorMes(
			Integer idProduto, int mes, int ano) {
		return ControladorHistoricoProduto.getInstance().consultaPorMes(
				idProduto, mes, ano);
	}

	public List<HistoricoProdutoTO> consultaHistoricosProdutoPorSemana(
			Integer idProduto, int mes, int ano, int semana) {
		return ControladorHistoricoProduto.getInstance().consultaPorSemana(
				idProduto, mes, ano, semana);
	}

	public Double recuperarEstoqueProdutoPorMes(Integer idProduto, int mes,
			int ano) {
		return ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorMes(idProduto, mes, ano);
	}

	/**
	 * @param idProduto
	 * @param mes Inteiro 1,2,...,12
	 * @param ano
	 * @param semana Inteiro: 1,2,3,4
	 * @return
	 */
	public Double recuperarEstoqueProdutoPorSemana(Integer idProduto, int mes,
			int ano, int semana) {
		return ControladorHistoricoProduto.getInstance()
				.recuperarEstoqueProdutoPorSemana(idProduto, mes, ano, semana);
	}

	public List<ProdutoTO> consultarProdutoTipo(int tipo) {
		ControladorProduto controladorProduto = ControladorProduto.getInstance();
		return controladorProduto.consultarProdutoTipo(tipo);
	}

	
	
	public List<HistoricoProdutoTO> consultarHistoricoProduto(
			Map<String, Object> parametros) {
		ControladorHistoricoProduto controladorHistoricoProduto = ControladorHistoricoProduto.getInstance();
		return controladorHistoricoProduto.consultar(parametros);
	}
}