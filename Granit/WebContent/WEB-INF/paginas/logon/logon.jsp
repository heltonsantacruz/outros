<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>
 
<div class="titulo">
	<bean:message key="title.logon"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/logonAction.event?operacao=efetuarLogon" focus="login">
<script type="text/javascript">
            
            function logon() { 
            	action = '<html:rewrite page="/logonAction.event?operacao=efetuarLogon"/>';
                document.forms[0].action = action;
                document.forms[0].submit();                              
            }
            
            function alterarSenha() {
                action = '<html:rewrite page="/logonAction.event?operacao=abrirAlterarSenha"/>';
                document.forms[0].action = action;
                document.forms[0].submit();         
            }
                 
        </script>
<fieldset>
<legend><bean:message key="title.digite.senha"/></legend>
<br>
	<div class="div_bloco">
		<label for="login" class="form">Login:</label>
       	<html:text name="logonForm" property="login" maxlength="10" onkeydown="if(event.keyCode == 13){logon()}"/>
        *	
       	<br>      
	</div> 	   
	<div class="div_bloco">
		<label for="senha" class="form"><bean:message key="logon.senha"/></label>
       	<html:password name="logonForm" property="senha" maxlength="10" onkeydown="if(event.keyCode == 13){logon()}"/>
        
        *	
       	<br>      
	</div> 	   
</fieldset>
	<div class="botoes">
        <input type="button" onclick="logon()" value="<bean:message key='btn.logon'/>" />
        <input type="button" onclick="alterarSenha()" value="<bean:message key='btn.alterarSenha'/>" />       
    </div>	
</html:form>
</div>

