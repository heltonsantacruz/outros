<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>

<div class="titulo">
	<bean:message key="title.cadastro.venda.passo2"/>
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
            function finalizar() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=avancarParaFinalizacao"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            function atualizaTotalAPagar() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=aplicaDescontoVenda"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }  
            function marcarCheckbox(cb){
            	cb.checked = true;
            }      
            function validaFormaPagamento(tipo){
            	if(tipo == 4){            		
            		document.getElementById('duplicata').checked = false;
            		document.getElementById('valorDuplicata').value = '';        
            		document.getElementById('valorDuplicata').disabled = true;            		    		
            		document.getElementById('numeroParcelasDuplicata').value = '';
            		document.getElementById('numeroParcelasDuplicata').disabled = true; 
            		document.getElementById('valorAberto').disabled = false;
            		document.getElementById('numeroParcelas').disabled = false;          		
            	}
            	else if(tipo == 5){
            		document.getElementById('aberto').checked = false;
            		document.getElementById('valorAberto').value = '';        
            		document.getElementById('valorAberto').disabled = true;            		    		
            		document.getElementById('numeroParcelas').value = '';
            		document.getElementById('numeroParcelas').disabled = true;
            		document.getElementById('valorDuplicata').disabled = false;
            		document.getElementById('numeroParcelasDuplicata').disabled = false; 
            		
            	}
            }         
      </script>

<fieldset>
<legend><bean:message key="label.dadoscliente.venda"/></legend>
<br>
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.cliente.venda"/></label>
       	<bean:write name="vendaCliente" property="cliente.nome"/>	
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
       	<html:text property="desconto" onblur="atualizaTotalAPagar()" onkeypress="reaisLimitado(this,event,4)"
	        	 onkeydown="backspace(this,event)" size="10" maxlength="4"/>	
	</div> 	
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.entrega.venda"/></label>
       	<html:text property="valorEntrega" onblur="atualizaTotalAPagar()" 
       				size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>	
	</div> 			   
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.total.venda"/></label>
       	<bean:write name="vendaCliente" property="totalFormatado"/>		
	</div>  

</fieldset>
<fieldset>
<legend><bean:message key="label.formas.venda"/></legend>
	<div class="div_bloco_1">
       <html:checkbox property="vista" value="true" tabindex="17" styleId="vista">
				<bean:message key="label.avista.vendas"/>
		</html:checkbox>
    	 <bean:write name="vendaForm" property="vista"/> 	
	</div> 		

	<div class="div_bloco_2">
		<label for="valor" class="form">
			<bean:message key="label.valor.vendas"/>
		</label>
 	
		<html:text property="valorVista" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<div class="div_bloco_1">
       <html:checkbox property="cheque" value="true" tabindex="17" styleId="cheque">
				<bean:message key="label.cheque.vendas"/>
		</html:checkbox>
    	  	
	</div> 		

	<div class="div_bloco_2">
		<label for="valor" class="form">
			<bean:message key="label.valor.vendas"/>
		</label>
 	
		<html:text property="valorCheque" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<div class="div_bloco_1">
       <html:checkbox property="credito" value="true" tabindex="17" styleId="credito">
				<bean:message key="label.credito.vendas"/>
		</html:checkbox>
    	  	
	</div> 		

	<div class="div_bloco_2">
		<label for="valor" class="form">
			<bean:message key="label.valor.vendas"/>
		</label>
 	
		<html:text property="valorCredito" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	
	<br>
	<div class="div_bloco_1">
       <html:checkbox property="aberto" value="true" tabindex="17" styleId="aberto" onclick="validaFormaPagamento('4')">
				<bean:message key="label.aberto.vendas"/>
		</html:checkbox>
    	  	
	</div> 		

	<div class="div_bloco_2">
		<label for="valor" class="form">
			<bean:message key="label.valor.vendas"/>
		</label>
 	
		<html:text property="valorAberto" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<div class="div_bloco_1">
		<bean:message key="label.parcelamento.vendas"/>
	  	
	</div>
	<div class="div_bloco_2">

  	
  		<label for="parcela" class="form">
			<bean:message key="label.numero.parcelas.vendas"/>
		</label>
 	
		<html:text property="numeroParcelas" size="10" onfocus="limpaCampo(this)" maxlength="3" onkeypress="mascara(this,'999');"/>
  	
	</div>
	
	<!-- Adição de duplicatas - Inicio -->
	<br>
	<div class="div_bloco_1">
       <html:checkbox property="duplicata" value="true" tabindex="17" styleId="duplicata" onclick="validaFormaPagamento('5')">
				<bean:message key="label.duplicata.vendas"/>
		</html:checkbox>
    	  	
	</div> 		

	<div class="div_bloco_2">
		<label for="valor" class="form">
			<bean:message key="label.valor.vendas"/>
		</label>
 	
		<html:text property="valorDuplicata" size="15" maxlength="15" 
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<div class="div_bloco_1">
		<bean:message key="label.parcelamento.vendas.duplicata"/>
	  	
	</div>
	<div class="div_bloco_2">

  	
  		<label for="parcela" class="form">
			<bean:message key="label.numero.parcelas.vendas"/>
		</label>
 	
		<html:text property="numeroParcelasDuplicata" size="10" onfocus="limpaCampo(this)" maxlength="3" onkeypress="mascara(this,'999');"/>
  	
	</div>
	
	<!-- Adição de duplicatas - Fim -->
	
</fieldset>
	<div class="botoes">
        <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
        <input type="button" onclick="finalizar()" value="<bean:message key='btn.avancar'/>" />
    </div>	
</html:form>
<logic:equal name="vista" value="true">
	<script>
		marcarCheckbox(document.getElementById('vista'));
	</script>
</logic:equal>
<logic:equal name="cheque" value="true">
	<script>
		marcarCheckbox(document.getElementById('cheque'));
	</script>
</logic:equal>
<logic:equal name="credito" value="true">
	<script>
		marcarCheckbox(document.getElementById('credito'));
	</script>
</logic:equal>
<logic:equal name="aberto" value="true">
	<script>
		marcarCheckbox(document.getElementById('aberto'));
		validaFormaPagamento(4);
	</script>
</logic:equal>
<logic:equal name="duplicata" value="true">
	<script>
		marcarCheckbox(document.getElementById('duplicata'));
		validaFormaPagamento(5);
	</script>
</logic:equal>

</div>
