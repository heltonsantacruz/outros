<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<div class="titulo">
    <bean:message key="title.detalhar.usuario"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/usuarioAction.event" >
	<script type="text/javascript">          
                        
            function fechar() { 
                action = '<html:rewrite page="/usuarioAction.event?operacao=voltarDetalhar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }           
                       
        </script>
<fieldset >

	<legend> <bean:message key="title.dados.usuario"/></legend>
	
		<div class="div_bloco">  
            <label for="login" class="form"> <bean:message key="label.login"/></label>
			<html:text name="usuarioForm" property="login" size="20" maxlength="20" readonly="true"/>			
		</div>
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>
			<html:text name="usuarioForm" property="nome" size="40" maxlength="40" readonly="true"/>			
		</div>		
		<div class="div_bloco">
			<label for="perfil" class="form"><bean:message key="title.usuario.perfil"/></label>
			<html:text name="usuarioForm" property="perfilFormatado" size="40" maxlength="40" readonly="true"/>		        	
		</div>			
	
</fieldset>	
	<div class="botoes">	    
	    <input type="button" onclick="fechar()" value="<bean:message key='btn.fechar'/>" />
	</div> 				
</html:form>	