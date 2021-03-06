<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<div class="titulo">
    <bean:message key="title.detalhar.cliente"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/clienteAction.event" >
	<script type="text/javascript">          
                        
            function fechar() { 
                action = '<html:rewrite page="/clienteAction.event?operacao=voltarDetalhar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }           
                       
        </script>
<fieldset >

	<html:hidden property="codigo"/>
	<legend> <bean:message key="title.dados.cliente"/></legend>
	
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.codigo"/></label>
			<bean:write name="clienteForm" property="codigo"/>			
		</div>
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="title.tipo.pessoa.cliente"/></label>					
			<html:text name="clienteForm" property="tipoPessoaFormatado" size="40" maxlength="40" readonly="true"/>	
		</div>
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>					
			<html:text name="clienteForm" property="nome" size="100" maxlength="100" readonly="true"/>	
		</div>
		
		<logic:equal name="clienteForm" property="tipoPessoa" value="F">
			<div class="div_bloco_1">  
            <label for="rg" class="form"> <bean:message key="label.rg"/></label>						
			<html:text name="clienteForm" property="rg" size="15" maxlength="15" readonly="true"/>
			</div>
			<div class="div_bloco_2">  
	            <label for="cpfcnpj" class="form"> <bean:message key="label.cpf"/></label>					
				<html:text name="clienteForm" property="cpf" readonly="true"/>	
			</div>		
		</logic:equal>
		<logic:equal name="clienteForm" property="tipoPessoa" value="J">
			<div class="div_bloco_2">  
            <label for="cpfcnpj" class="form"> <bean:message key="label.cnpj"/></label>					
			<html:text name="clienteForm" property="cnpj" readonly="true"/>	
			</div>		
		</logic:equal>		
		<div class="div_bloco">
			<label for="estado" class="form"><bean:message key="label.uf"/></label>				
			<html:text name="clienteForm" property="nomeEstado" readonly="true"/>			
				
			<label for="cidade" class="form_2"><bean:message key="label.cidade"/> </label>			
			<html:text name="clienteForm" property="nomeCidade" readonly="true"/>
    			
			<label for="cep" class="form_2"><bean:message key="label.cep"/></label>      		
      		<html:text name="clienteForm" styleId="cep" property="cep" size="10" maxlength="10" readonly="true"/>
		</div>		
		<div class="div_bloco">
			<label for="logradouro" class="form"><bean:message key="label.logradouro"/></label>	        	 
	        <html:text name="clienteForm" property="endereco" size="100" maxlength="100" readonly="true"/>       	
		</div>		
		
		<div class="div_bloco">
			<label for="bairro" class="form"><bean:message key="label.bairro"/></label>	        	    
	        <html:text name="clienteForm" property="bairro" size="100" maxlength="100" readonly="true"/>    	
		</div>
		
		<div class="div_bloco">
			<label class="form"><bean:message key="label.telefone.residencial"/></label>	        
	        <html:text  name="clienteForm" property="telefoneResidencial" readonly="true"/>
	        			
			<label class="form_1"><bean:message key="label.telefone.celular"/></label>	        	
	        <html:text name="clienteForm" property="celular"  size="15" maxlength="14" readonly="true"/>		
	        
	        <label class="form_2"><bean:message key="label.telefone.fax"/></label>	        
	        <html:text name="clienteForm" property="fax"  size="14" maxlength="13" readonly="true"/>
		</div>		

		<div class="div_bloco">
			<label for="email" class="form"><bean:message key="label.email"/></label>	        	
	        <html:text  name="clienteForm"  property="email" size="50" maxlength="50" readonly="true"/>		
		</div>		
		
</fieldset>	
	<div class="botoes">	    
	    <input type="button" onclick="fechar()" value="<bean:message key='btn.fechar'/>" />
	</div> 				
</html:form>	