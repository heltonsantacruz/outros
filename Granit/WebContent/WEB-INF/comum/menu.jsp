<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<script type="text/javascript">
	
	function imprimir() { 
            	abreMenor("ticketAction.do?operacao=imprimir");           	                    
            }       
            
    function abreMenor(action){
		var width =670;
		var height =600;
		var left = ((screen.width)/2)-(width/2);
		var top = ((screen.height)/2)-(height/2);	
		window.open(action, "", 'width='+width+', height='+height+', top='+top+', left='+left+', menuBar=no, toolbar=no, status=yes, resizeable=no, scrollBars=yes');
	}
	

</script>

<div id="menu"><script type="text/javascript" src="./js/menu.js"></script>
<!-- inicio da arvore de menu -->
<ul class="udm" id="udm">
	<li><a href="#">Usuários</a>
	<ul>
		<li><a
			href="/granit/usuarioAction.do?operacao=abrir">Novo Usuário</a>
		</li>
		<li><a
			href="/granit/usuarioAction.do?operacao=abrirConsulta">Consultar</a>
		</li>
	</ul>
	</li>
	
	<li><a href="#">Clientes</a>
	<ul>
		<li><a
			href="/granit/clienteAction.do?operacao=abrir">Novo Cliente</a>
		</li>
		<li><a
			href="/granit/clienteAction.do?operacao=abrirConsulta">Consultar</a>
		</li>
	</ul>
	</li>
	
	<li><a href="#">Produtos</a>
	<ul>
		<li><a
			href="/granit/produtoAction.do?operacao=abrir">Novo Produto</a>
		</li>
		<li><a
			href="/granit/produtoAction.do?operacao=abrirConsulta">Consultar</a>
		</li>
		<li><a
			href="/granit/produtoAction.do?operacao=abrirRegistrarEntradaSaida">Registrar Saídas</a>
		</li>
	</ul>
	</li>
	
	<li><a href="#">Fornecedores</a>
	<ul>
		<li><a
			href="/granit/fornecedorAction.do?operacao=abrir">Novo Fornecedor</a>
		</li>
		<li><a
			href="/granit/fornecedorAction.do?operacao=abrirConsulta">Consultar</a>
		</li>
	</ul>
	</li>
	
	<li><a href="#">Vendas</a>
	<ul>
		<li><a
			href="/granit/vendaAction.do?operacao=abrir">Nova Venda</a>
		</li>
		<li><a
			href="/granit/vendaAction.do?operacao=abrirConsulta">Consultar</a>
		</li>
	</ul>
	</li>
	
	<li><a href="#">Pedidos</a>
	<ul>
		<li><a
			href="/granit/pedidoAction.do?operacao=abrir">Novo Pedido</a>
		</li>
		<li><a
			href="/granit/pedidoAction.do?operacao=abrirConsulta">Consultar</a>
		</li>
	</ul>
	</li>
	
	<li><a href="#">Backup</a>
	<ul>
		<li><a
			href="/granit/backupAction.do?operacao=criarBackup">Criar Backup</a>
		</li>
		<li><a
			href="/granit/backupAction.do?operacao=listarBackups">Listar Backups</a>
		</li>		
	</ul>
	</li>

	<li><a href="#">Relatórios</a>
	<ul>
		<li>
			<a href="/granit/produtoAction.do?operacao=abrirConsultaRelatorioEntradaSaida">Produtos Entrada/Saida</a>			
		</li>
		<li>
			<a href="/granit/produtoAction.do?operacao=relatorios">Produtos</a>
		</li>
	</ul>
	</li>

	<li><a href="/granit/logonAction.do?operacao=logout">Sair</a>
	<ul>
		<li><a href="/granit/logonAction.do?operacao=logout">Sair</a>
		</li>
	</ul>
	</li>
</ul>
</div>