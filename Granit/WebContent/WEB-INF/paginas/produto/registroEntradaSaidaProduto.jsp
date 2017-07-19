<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>

<div class="titulo">
    <bean:message key="title.entradasaida.produto"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/produtoAction.event" >
	<script type="text/javascript">
            function registrar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=registrarEntradaSaida"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function cancelar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=cancelar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }            
            
        </script>
<fieldset>

	<legend> Informe os dados para atualização do Estoque do produto:</legend>
	    <br><br>
	    <html:hidden property="descricao"/>
	    <html:hidden property="idProduto"/>
	    <html:hidden property="quantidadeEstoque"/>
		<div class="div_bloco">  
            <label for="descricao" class="form">Produto:</label>
            <bean:write name="produtoForm" property="descricao"/>
		</div>
		<div class="div_bloco">  
            <label for="descricao" class="form">Estoque atual:</label>
            <bean:write name="produtoForm" property="quantidadeEstoque"/>
		</div>
	
		<div class="div_bloco">  
            <label for="tipo" class="form"> <bean:message key="label.tipo"/></label>:
            <bean:write name="produtoForm" property="tipoDescricao"/>			
		</div>
	
		<div class="div_bloco">  
            <label for="subtipo" class="form"> <bean:message key="label.subtipo"/></label>:
            <bean:write name="produtoForm" property="subTipoDescricao"/>			
		</div>
		
		<div class="div_bloco">  
            <label for="tipoEntradaSaida" class="form">Tipo:</label>
			<html:select property="tipoEntradaSaida">
	        	<html:option value=""></html:option>
	        	<html:option value="E">Entrada</html:option>
	        	<html:option value="S">Saída</html:option>        	
        	</html:select>    
		</div>
		<div class="div_bloco">  
            <label for="quantidadeEntradaSaida" class="form"> <bean:message key="label.quantidade.estoque"/>:</label>
			<html:text name="produtoForm" property="quantidadeEntradaSaida" size="15" maxlength="15"
	        	onkeypress="reaisLimitado(this,event,15)"
	        	onkeydown="backspace(this,event)"/>	
		</div>
</fieldset>
		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">	    
	    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="registrar()" value="<bean:message key='btn.registrar'/>" />
	</div> 
</html:form>	