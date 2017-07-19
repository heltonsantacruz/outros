<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<div class="titulo">
    <bean:message key="title.detalhar.pedido"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form action="/fornecedorAction.event" >
	<script type="text/javascript">          
                        
            function cancelar() { 
                action = '<html:rewrite page="/pedidoAction.event?operacao=voltarDetalhar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }         

            function imprimir() { 
                window.open('/granit/pedidoAction.event?operacao=imprimir&idPedido=${pedido.idPedido}','pdfwindow','765','600')            
            }  
                       
        </script>
<fieldset >

	<html:hidden property="codigo"/>
	<legend> <bean:message key="title.dados.pedido"/></legend>
		<div class="div_bloco">
			<label for="dataPedido" class="form">Data:</label>
	       	<bean:write name="pedido" property="dataPedidoFormatada"/>	
		</div>	
		
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.fornecedor"/></label>
			<bean:write name="pedido" property="fornecedor.nome"/>			
		</div>
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.pedido.subtotal"/></label>
			<bean:write name="pedido" property="totalSemDescontoFormatado"/>			
		</div>
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.pedido.desconto"/></label>
			<bean:write name="pedido" property="descontoFormatado"/>			
		</div>
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.pedido.total"/></label>
			<bean:write name="pedido" property="totalComDescontoFormatado"/>			
		</div>
		
</fieldset>	

<logic:notEmpty name="listaItensPedido">
	<fieldset class="fieldset">
	<legend><bean:message key="label.produtos.associados"/></legend>
	<display:table cellspacing="3" cellpadding="0" 
					name="listaItensPedido" pagesize="100" 
					id="item" class="tableResultadoConsulta2" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
	 
	  <display:column property="produto.descricao" title="Nome Produto"/>
	  <display:column property="produto.tipoSubtipo" title="Tipo/Subtipo"/>	  
	  <display:column property="quantidadeFormatada" title="Quantidade" />
	  <display:column property="precoUnitario" title="Preço Unitário"  decorator="br.com.granit.util.decoradores.MoedaDecorator"/>	 
	  <display:column property="precoTotal" title="Preço Total"  total="true" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>  	 	 	
	   
	</display:table>
	</fieldset>
</logic:notEmpty>
	<div class="botoes">	    
	    <div class="botoes">
        <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
        <input type="button" onclick="imprimir()" value="<bean:message key='btn.imprimir'/>" />
</div>	

	</div> 				
</html:form>	