<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<div class="titulo">
    <bean:message key="title.alteracao.usuario"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="login" action="/usuarioAction.event" >
	<script type="text/javascript">
            
            function atualizar() { 
                action = '<html:rewrite page="/usuarioAction.event?operacao=processarAlterar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function cancelar() { 
                action = '<html:rewrite page="/usuarioAction.event?operacao=cancelarAlterar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            } 
        
                       
        </script>
<fieldset >

	<html:hidden property="idUsuario"/>
	<legend> <bean:message key="title.dados.usuario"/></legend>
	
		<div class="div_bloco">  
            <label for="codigo" class="form"> <bean:message key="label.codigo"/></label>
			<bean:write name="usuarioForm" property="idUsuario"/>			
		</div>
		<div class="div_bloco">  
            <label for="login" class="form"> <bean:message key="label.login"/></label>
			<html:text name="usuarioForm" property="login" size="20" maxlength="20"/>
			*
		</div>
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>
			<html:text name="usuarioForm" property="nome" size="40" maxlength="40" style="TEXT-TRANSFORM: uppercase;"/>
			*
		</div>		
		<div class="div_bloco">
			<label for="perfil" class="form"><bean:message key="title.usuario.perfil"/></label>
			
			<html:select name="usuarioForm" property="perfil">
					<html:option value=""></html:option>
    				<html:optionsCollection name="listaPerfis" value="idPerfil" label="nome"/>	
    			</html:select> *	        	
		</div>	
		
				
		
</fieldset>
		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">
	    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="atualizar()" value="<bean:message key='btn.atualizar'/>" />
	</div> 
				
</html:form>	