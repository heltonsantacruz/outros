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
				var fornecedor = document.getElementById('idFornecedor').value;
				var listaVazia = <%= (lista == null || lista.isEmpty()) %> ;							
				if(listaVazia){					
                	document.getElementById('idFornecedor').disabled = false;
                	              		
                }
                else {    
                	document.getElementById('idFornecedor').disabled = true;                 	                   	
            	} 				
			}                         			
            
            function associar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=associarProdutoPedido&caminhoAssociacaoProdutoPedido=alterar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function cancelar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=cancelarAlterar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            } 

            function alterar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=alterar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function finalizar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=finalizarAlteracao"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function atualizaTotal() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=aplicaDescontoPedido&tipoAcao=A"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }  

            function exibirHistoricoProduto() {   
            	
          		action = '<html:rewrite page="/pedidoAction.event?operacao=exibirHistoricoProduto&caminhoAssociacaoProdutoPedido=alterar"/>';           	
                document.forms[0].action = action;                     
                document.forms[0].submit();  
                  	            
           }
                
                        
           
       
                                           
     </script>
<fieldset>

	<legend> <bean:message key="title.dados.pedido"/></legend>
	
		<div class="div_bloco">
			<label for="dataPedido" class="form">Data:</label>
	       	<bean:write name="pedido" property="dataPedidoFormatada"/>	
		</div>		
		
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
				<html:select property="idProduto" onchange="exibirHistoricoProduto()">
					<html:option value=""></html:option>
    				<html:optionsCollection name="produtosCadastrados" value="idProduto" label="descricaoTipoSubTipo"/>	
    			</html:select>	
    			*    		    		 		
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
					id="pf" class="tableResultadoConsulta2" requestURI="/pedidoAction.do?operacao=exibirHistoricoProduto&caminhoAssociacaoProdutoPedido=alterar" >
	  <display:column property="produto.idProduto" title="C�digo" />
	  <display:column property="produto.descricao" title="Descri��o"/>
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
	  <display:column property="produto.idProduto" title="C�digo" />
	  <display:column property="produto.descricao" title="Descri��o"/>
	  <display:column property="quantidadeFormatada" title="Quantidade" />
	  <display:column property="precoUnitario" title="Pre�o Unit�rio"  decorator="br.com.granit.util.decoradores.MoedaDecorator"/>	 
	  <display:column property="precoTotal" title="Pre�o Total"  total="true" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>  	  
	  <display:column title="" value="Remover" href="/granit/pedidoAction.event?operacao=removerItemPedidoAlteracao" paramId="posicao" paramProperty="posicao"/>
	   
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
	    <input type="button" onclick="alterar()" value="<bean:message key='btn.atualizar'/>" />
	    <input type="button" onclick="finalizar()" value="<bean:message key='btn.finalizar'/>" />
	</div> 


<script type="text/javascript">		
	habilitaComboFornecedores();
</script>

</html:form>	