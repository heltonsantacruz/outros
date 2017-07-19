<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<div class="titulo">
    <bean:message key="title.detalhar.fornecedor"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/fornecedorAction.event" >
	<script type="text/javascript">          
                        
            function fechar() { 
                action = '<html:rewrite page="/fornecedorAction.event?operacao=voltarDetalhar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }           
                       
        </script>
<fieldset >

	<html:hidden property="codigo"/>
	<legend> <bean:message key="title.dados.fornecedor"/></legend>
	
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.codigo"/></label>
			<bean:write name="fornecedorForm" property="codigo"/>			
		</div>
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>					
			<html:text name="fornecedorForm" property="nome" size="100" maxlength="100" readonly="true"/>	
		</div>		
		
		<div class="div_bloco_2">  
            <label for="cpfcnpj" class="form"> <bean:message key="label.cnpj"/></label>					
			<html:text name="fornecedorForm" property="cnpj" readonly="true"/>	
		</div>			
		<div class="div_bloco">
			<label for="estado" class="form"><bean:message key="label.uf"/></label>				
			<html:text name="fornecedorForm" property="nomeEstado" readonly="true"/>			
				
			<label for="cidade" class="form_2"><bean:message key="label.cidade"/> </label>			
			<html:text name="fornecedorForm" property="nomeCidade" readonly="true"/>
    			
			<label for="cep" class="form_2"><bean:message key="label.cep"/></label>      		
      		<html:text name="fornecedorForm" styleId="cep" property="cep" size="10" maxlength="10" readonly="true"/>
		</div>		
		<div class="div_bloco">
			<label for="logradouro" class="form"><bean:message key="label.logradouro"/></label>	        	 
	        <html:text name="fornecedorForm" property="endereco" size="100" maxlength="100" readonly="true"/>       	
		</div>		
		
		<div class="div_bloco">
			<label for="bairro" class="form"><bean:message key="label.bairro"/></label>	        	    
	        <html:text name="fornecedorForm" property="bairro" size="100" maxlength="100" readonly="true"/>    	
		</div>
		
		<div class="div_bloco">
			<label class="form"><bean:message key="label.telefone.comercial"/></label>	        
	        <html:text  name="fornecedorForm" property="telefoneComercial" readonly="true"/>
	        			
			<label class="form_1"><bean:message key="label.telefone.celular"/></label>	        	
	        <html:text name="fornecedorForm" property="celular"  size="15" maxlength="14" readonly="true"/>		
	        
	        <label class="form_2"><bean:message key="label.telefone.fax"/></label>	        
	        <html:text name="fornecedorForm" property="fax"  size="14" maxlength="13" readonly="true"/>
		</div>		

		<div class="div_bloco">
			<label for="email" class="form"><bean:message key="label.email"/></label>	        	
	        <html:text  name="fornecedorForm"  property="email" size="50" maxlength="50" readonly="true"/>		
		</div>	
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.contato.fornecedor"/></label>					
			<html:text name="fornecedorForm" property="contatoFornecedor" size="100" maxlength="100" readonly="true"/>	
		</div>
		
		<logic:notEmpty name="produtosAssociados">
			<fieldset class="fieldset">
			<legend><bean:message key="label.produtos.associados"/></legend>
			<display:table cellspacing="3" cellpadding="0" 
							name="produtosAssociados" pagesize="100" 
							id="item" class="tableResultadoConsulta2" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
			  <display:column property="produto.idProduto" title="Código" />
			  <display:column property="produto.descricaoTipoSubTipo" title="Nome Produto"/> 			   
			</display:table>
		</fieldset>
		</logic:notEmpty>
			
		
</fieldset>	
	<div class="botoes">	    
	    <input type="button" onclick="fechar()" value="<bean:message key='btn.fechar'/>" />
	</div> 				
</html:form>	