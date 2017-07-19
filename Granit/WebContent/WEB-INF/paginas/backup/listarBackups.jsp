<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
 
<div class="titulo">
	Lista de Backups
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/backupAction.event?operacao=listarBackups">
<script type="text/javascript">
            function download(idBackup) {            	
           		action = 'backupAction.event?operacao=downloadBackup&idBackup='+idBackup;
               	document.forms[0].action = action;
               	document.forms[0].submit();
            }  

            function excluir(idBackup) {            	
            	if(confirm('Deseja excluir o backup?')){            		
            		action = 'backupAction.event?operacao=processarExcluir&idBackup='+idBackup;
                	document.forms[0].action = action;
                	document.forms[0].submit();
            	}  
            	return false;                             
            }         
         </script>
</html:form>
</div>
<logic:present name="listaBackups" >
<display:table cellspacing="3" cellpadding="0" 
				name="listaBackups" pagesize="100" 
				id="backup" requestURI="/granit/backupAction.event?operacao=listarBackups" class="tableResultadoConsulta" >
  
   <display:column property="nome" title="Download" 
  		href="/granit/backupAction.event?operacao=downloadBackup" 
  		paramId="idBackup" paramProperty="idBackup"/>	
  
  <display:column property="data" title="Data"/>
  
  <display:column title="Excluir" paramId="idBackup" paramProperty="idBackup">
  	<html:img page="/images/delete-16.gif" onclick="javascript:excluir(${backup.idBackup});" alt="Excluir Backup"/>
  </display:column>
  
</display:table>
</logic:present>