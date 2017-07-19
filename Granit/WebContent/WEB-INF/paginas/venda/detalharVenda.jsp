<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

 
<div class="titulo">
	Detalhar Venda:
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/vendaAction.event">
	<script type="text/javascript">           
            function cancelar() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=cancelar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }                        
            
            function imprimir() { 
                window.open('/granit/vendaAction.event?operacao=imprimir&idVenda=${vendaCliente.idVenda}','pdfwindow','765','600')            
            }   

            function imprimirParaCliente() { 
                window.open('/granit/vendaAction.event?operacao=imprimirParaCliente&idVenda=${vendaCliente.idVenda}','pdfwindowCliente','765','600')            
            }   
            
            function imprimirTicket() { 
                action = '<html:rewrite page="/consultaVendaAction.event?operacao=imprimirTicket&idVenda=${vendaCliente.idVenda}"/>';
                document.forms[0].action = action;
                document.forms[0].submit();            
            }            
    </script>

<fieldset>
<legend>Dados gerais da Venda</legend>
	<div class="div_bloco">
		<label for="dataVenda" class="form">Nº Pedido:</label>
       	<bean:write name="vendaCliente" property="numeroPedidoFormatado"/>	
	</div> 
	<div class="div_bloco">
		<label for="dataVenda" class="form">Data:</label>
       	<bean:write name="vendaCliente" property="dataVendaFormatada"/>	
	</div>
	<div class="div_bloco">
		<label for="dataVenda" class="form">Data Entrega:</label>
       	<bean:write name="vendaCliente" property="prazoEntregaFormatado"/>	
	</div>  
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.cliente.venda"/>:</label>
       	<bean:write name="vendaCliente" property="cliente.nome"/>	
	</div>
	<div class="div_bloco">
		<label for="nome" class="form">Situação:</label>
       	<bean:write name="vendaCliente" property="situacaoVendaFormatada"/>	
	</div>  
</fieldset>


<logic:present name="listaItensVenda">
<fieldset class="fieldset">
<legend>Itens da Venda:</legend>
<display:table cellspacing="3" cellpadding="0" 
				name="listaItensVenda" pagesize="100" 
				id="item" class="tableResultadoConsulta" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
  <display:column property="descricao" title="Descrição" />
  <display:column property="preco" title="Valor(R$)" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>
  <display:column property="produto.descricaoTipoSubTipo" title="Produto" />
  <display:column property="metragemFormatada" title="Metragem" />
</display:table>
</fieldset>
</logic:present>

<logic:present name="listaItensVendaBeneficiamento">
<fieldset class="fieldset">
<legend>Itens de Beneficiamento da Venda:</legend>
<display:table cellspacing="3" cellpadding="0" 
				name="listaItensVendaBeneficiamento" pagesize="100" 
				id="item" class="tableResultadoConsulta" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
  <display:column property="descricao" title="Descrição" />
  <display:column property="valor" title="Valor(R$)" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>  
</display:table>
</fieldset>
</logic:present>

<fieldset>
<legend>Serviço de Montagem:</legend>
	<div class="div_bloco">
		<label for="valorServicoMontagemVenda" class="form">
			Valor do serviço(R$):
		</label> 	
	  	<bean:write name="vendaCliente" property="valorServicoMontagemFormatado"/>
	</div>	
</fieldset>	

<fieldset>
<legend><bean:message key="label.totalizacao.venda"/></legend>
<br>
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.subtotal.venda"/></label>
       	<bean:write name="vendaCliente" property="subTotalFormatado"/>		
	</div>
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.desconto.venda"/></label>
       	<bean:write name="vendaCliente" property="descontoFormatado"/>	
	</div>			   
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.total.venda"/></label>
       	<bean:write name="vendaCliente" property="totalFormatado"/>		
	</div>   
</fieldset>

<fieldset>
<legend>Formas de pagamento:</legend>
<br>
	<logic:notEqual name="vendaCliente" property="valorVistaFormatado" value="">
		<div class="div_bloco">
			<label for="nome" class="form"><bean:message key="label.avista.vendas"/></label>
	       	<bean:write name="vendaCliente" property="valorVistaFormatado"/>	
		</div> 
	</logic:notEqual>
	<logic:notEqual name="vendaCliente" property="valorChequeFormatado" value="">
		<div class="div_bloco">
			<label for="nome" class="form"><bean:message key="label.cheque.vendas"/></label>
       		<bean:write name="vendaCliente" property="valorChequeFormatado"/>		
		</div>
	</logic:notEqual>
	<logic:notEqual name="vendaCliente" property="valorCreditoFormatado" value="">
		<div class="div_bloco">
			<label for="nome" class="form"><bean:message key="label.credito.vendas"/></label>
       		<bean:write name="vendaCliente" property="valorCreditoFormatado"/>	
		</div>
	</logic:notEqual> 			   
	<logic:notEqual name="vendaCliente" property="valorAbertoFormatado" value="">
		<div class="div_bloco">
			<label for="nome" class="form"><bean:message key="label.aberto.vendas"/></label>
       		<bean:write name="vendaCliente" property="valorAbertoFormatado"/> 		
    	</div>
    	<div class="div_bloco">
			<label for="nome" class="form">Parcelas:</label>
       		<bean:write name="vendaCliente" property="numeroParcelas"/>       		
    	</div>
       		<br>
    </logic:notEqual> 
    <logic:notEqual name="vendaCliente"  property="valorBoletoFormatado" value="">
		<div class="div_bloco">
			<label for="nome" class="form"><bean:message key="label.boleto.vendas"/></label>
       		<bean:write name="vendaCliente" property="valorBoletoFormatado"/>
       		<br>
    	</div>
    </logic:notEqual> 
    <div class="div_bloco">
			<label for="nome" class="form">Observação:</label>       		
       		<html:textarea property="observacao" name="vendaCliente" readonly="true" cols="80" rows="10"/>
       		<br>
    	</div>
     <div class="div_bloco">
			<label for="nome" class="form">Produtos/Serviços entregues:</label>
       		<bean:write name="vendaCliente" property="produtosEntreguesFormatado"/>
       		<br>
    	</div>
</fieldset>

<div class="botoes">
        <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
        <input type="button" onclick="imprimir()" value="<bean:message key='btn.imprimir'/>" />
        <input type="button" onclick="imprimirParaCliente()" value="Imprimir para Cliente" />
</div>	
</html:form>
</div>
