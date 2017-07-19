<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>

<div class="titulo">
    <bean:message key="title.cadastro.pedido"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="idFornecedor" action="/pedidoAction.event" >

<%java.util.ArrayList lista = (java.util.ArrayList)request.getAttribute("listaItensPedido"); %>

	<script type="text/javascript">

			function habilitaComboFornecedores(){						
				var fornecedor = document.forms[0].idFornecedor.value;
				var listaVazia = <%= (lista == null || lista.isEmpty()) %> ;	
				
				if(listaVazia){					
					document.forms[0].idFornecedor.disabled = false;
                	              		
                }
                else {    
                	document.forms[0].idFornecedor.disabled = true;                 	                   	
            	} 				
			}                         			
            
            function associar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=associarProdutoPedido&caminhoAssociacaoProdutoPedido=incluir"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function cancelar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=cancelarIncluir"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            } 

            function adicionar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=adicionar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function finalizar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=finalizarCriacaoInicial"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function atualizaTotal() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=aplicaDescontoPedido&tipoAcao=S"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }  


            function exibirHistoricoProduto() {   
                        	
           		 action = '<html:rewrite page="/pedidoAction.event?operacao=exibirHistoricoProduto&caminhoAssociacaoProdutoPedido=incluir"/>';           	
                 document.forms[0].action = action;                     
                 document.forms[0].submit();  
                   	            
            }
                 
           
       
                                           
     </script>
<fieldset>

	<legend> <bean:message key="title.dados.pedido"/></legend>
	
		<!-- Retirar apos carga inicial de historico -->
		<!-- //26/03/2011 - desabilitando datas de pedido e vendas -->
		<!-- <div class="div_bloco">  
            <label for="dataPedido" class="form"> <bean:message key="title.data.pedido"/></label>
			<html:text name="pedidoForm" property="dataPedido" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>
			*
		</div>
		-->
		
		<div class="div_bloco">
			<label for="fornecedor" class="form"><bean:message key="label.fornecedor"/></label>				
				<html:select property="idFornecedor" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="fornecedoresCadastrados" value="idFornecedor" label="nome"/>	
    			</html:select>	
    			*    		    		 		
		</div>	
		
		<div class="div_bloco">
			<label for="produto" class="form"><bean:message key="label.produto"/></label>				
				<html:select property="idProduto" onchange="exibirHistoricoProduto()" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="produtosCadastrados" value="idProduto" label="descricaoTipoSubTipo"/>	
    			</html:select>	
    			*               				 		
		</div>		
		
		<div class="div_bloco">
			<label for="tipo" class="form" id="tipo"> <bean:message key="label.tipo"/></label>
           	<logic:present name="pedidoForm" property="tipoProduto">
           		<bean:write name="pedidoForm" property="tipoProduto"/>	
           	</logic:present>
	    </div>     
	    <div class="div_bloco">
			<label for="subTipo" class="form" id="subTipo"> <bean:message key="label.subtipo"/></label>
           	<logic:present name="pedidoForm" property="subTipoProduto">
           		<bean:write name="pedidoForm" property="subTipoProduto"/>	
           	</logic:present>
	    </div>        	
		
		<div class="div_bloco">  
            <label for="quantidadeProduto" class="form"> <bean:message key="label.quantidade.produto"/></label>
			<html:text property="quantidadeProduto" size="15" maxlength="15"
	        	onkeypress="reaisLimitado(this,event,15,4)"
	        	onkeydown="backspace(this,event,4)"/>*
		</div>
		
		<div class="div_bloco_1">
			<label for="precoProduto" class="form"><bean:message key="label.preco.produto"/></label>
	        <html:text property="precoProduto" size="15" maxlength="15"
	        	onkeypress="reaisLimitado(this,event,15)"
	        	onkeydown="backspace(this,event)"/>*		
	        <input type="button" onclick="associar()" value="<bean:message key='btn.associar'/>" />	        	
		</div>	
		
</fieldset>

<logic:notEmpty name="listaHistoricoProduto">
	<fieldset class="fieldset">
	<legend><bean:message key="label.produtos.ultimas.compras"/></legend>
	<display:table cellspacing="3" cellpadding="0" 
					name="listaHistoricoProduto" pagesize="3" 
					id="pf" class="tableResultadoConsulta2" requestURI="/pedidoAction.do?operacao=exibirHistoricoProduto&caminhoAssociacaoProdutoPedido=incluir" >
	  <display:column property="produto.idProduto" title="Código" />
	  <display:column property="produto.descricao" title="Descrição"/>
	  <display:column property="produto.tipoSubtipo" title="Tipo/Subtipo"/>
	  <display:column property="dataUltimaCompra" title="Data" />
	  <display:column property="valorUnitarioUltimaCompra" title="Valor" />	 
	  <display:column property="fornecedor.nome" title="Fornecedor"/>  		   
	</display:table>
	</fieldset>
</logic:notEmpty>


<logic:notEmpty name="listaItensPedido">
	<fieldset class="fieldset">
	<legend><bean:message key="label.produtos.associados"/></legend>
	<display:table cellspacing="3" cellpadding="0" 
					name="listaItensPedido" pagesize="100" 
					id="item" class="tableResultadoConsulta2" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
	  <display:column property="produto.idProduto" title="Código" />
	  <display:column property="produto.descricao" title="Descrição"/>
	  <display:column property="produto.tipoSubtipo" title="Tipo/Subtipo"/> 
	  <display:column property="quantidadeFormatada" title="Quantidade" />
	  <display:column property="precoUnitario" title="Preço Unitário"  decorator="br.com.granit.util.decoradores.MoedaDecorator"/>	 
	  <display:column property="precoTotal" title="Preço Total"  total="true" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>  	  
	  <display:column title="" value="Remover" href="/granit/pedidoAction.event?operacao=removerItemPedido" paramId="posicao" paramProperty="posicao"/>
	   
	</display:table>
	</fieldset>
</logic:notEmpty>

<fieldset>
	<legend> <bean:message key="title.dados.pedido"/></legend>
<br>
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.subtotal.pedido"/></label>
       	<bean:write name="pedido" property="subTotalFormatado"/>		
	</div>
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.desconto.pedido"/></label>
       	<html:text property="desconto" onblur="atualizaTotal()" onkeypress="reaisLimitado(this,event,15)"
	        	onkeydown="backspace(this,event)" size="15" maxlength="15"/>	
	</div> 	
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.total.pedido"/></label>
		<logic:notEmpty name="pedido">		
       		<html:text name="pedido"  property="totalComDescontoFormatado" size="15" maxlength="15" disabled="true"/>	
       	</logic:notEmpty>
       	<logic:empty name="pedido">		
       		<html:text property="totalSemDesconto" size="15" maxlength="15" disabled="true"/>	
       	</logic:empty>
	</div> 	
</fieldset>
		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">
	    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="adicionar()" value="<bean:message key='btn.salvar'/>" />
	    <input type="button" onclick="finalizar()" value="<bean:message key='btn.finalizar'/>" />
	</div> 


<script type="text/javascript">		
	habilitaComboFornecedores();
</script>

</html:form>	