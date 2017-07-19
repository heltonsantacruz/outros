<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<div class="titulo">
    <bean:message key="title.cadastro.usuario"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/usuarioAction.event" >
	<script type="text/javascript">


			function adicionar() { 
		        action = '<html:rewrite page="/usuarioAction.event?operacao=adicionar"/>';
		        document.forms[0].action = action;
		        document.forms[0].submit();               
		    }
            
            function cancelar() { 
                action = '<html:rewrite page="/usuarioAction.event?operacao=cancelarIncluir"/>';
                document.forms[0].action = action;
                document.forms[0].submit();                    
            }           
        </script>
<fieldset>

	<legend> <bean:message key="title.dados.usuario"/></legend>
	
	    <div class="div_bloco">  
            <label for="login" class="form"> <bean:message key="label.login"/></label>
			<html:text property="login" size="20" maxlength="20"/>
			*
		</div>
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>
			<html:text property="nome" size="40" maxlength="40" style="TEXT-TRANSFORM: uppercase;"/>
			*
		</div>
		
		
		
		<div class="div_bloco">
			<label for="perfil" class="form"><bean:message key="title.usuario.perfil"/></label>
			
			<html:select property="perfil">
					<html:option value=""></html:option>
    				<html:optionsCollection name="listaPerfis" value="idPerfil" label="nome"/>	
    			</html:select> *
	        	
		</div>	
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="logon.senha"/></label>
			<html:password property="senha" size="10" maxlength="10"/>
			*
		</div>
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.confirmaSenha"/></label>
			<html:password property="confirmaSenha" size="10" maxlength="10"/>
			*
		</div>		
		
</fieldset>
		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">
	    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="adicionar()" value="<bean:message key='btn.adicionar'/>" />
	</div> 
				
</html:form>	