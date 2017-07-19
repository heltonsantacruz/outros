<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<script type="text/javascript">
 	function verificar(objeto) {           
        if (objeto.value != ""){
	           	if (objeto.value.length != 14
	            			&& objeto.value.length != 18){
	            		alert("Dado Inválido!"); 
						objeto.value = "";	
						objeto.focus();
	           	}else if (objeto.value.length == 14){
				   validaCPF(objeto);
				}else if (objeto.value.length == 18){
				   validaCNPJ(objeto);  
				}
		}    
    }        
</script>
 
<div class="titulo">
	<bean:message key="title.consulta.fornecedor"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/fornecedorAction.event?operacao=processarConsulta">
<script type="text/javascript">
            function excluir(idFornecedor) {            	
            	if(confirm('Deseja excluir o fornecedor do sistema?')){            		
            		action = 'fornecedorAction.event?operacao=processarExcluir&idFornecedor='+idFornecedor;
                	document.forms[0].action = action;
                	document.forms[0].submit();
            	}  
            	return false;                             
            }
            function editar(idFornecedor) {
            	action = 'fornecedorAction.event?operacao=abrirAlterar&idFornecedor='+idFornecedor;
                document.forms[0].action = action;
                document.forms[0].submit();            	                             
            }

			
         </script>   
<fieldset>
<br>


	<div class="div_bloco" >  			
            <label for="cnpj" class="form" id="cnpj"> <bean:message key="label.cnpj"/></label>
            <html:text styleId="idCnpj" property="cnpj" onblur="verificarCpfCpnj(this)" onkeypress="mascara(this,'99.999.999/9999-99');" size="20" maxlength="20"/>	
	</div>	
	
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="title.nome.cliente"/></label>
        <html:text property="nome" size="50" maxlength="100" 
        onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>      	
	</div>
	    	   

	<div class="botoes">
        <html:submit><bean:message key="btn.consultar"/></html:submit>
    </div>	
</fieldset>

</html:form>
</div>

<display:table cellspacing="3" cellpadding="0" 
				name="listaFornecedoresConsulta" pagesize="100" 
				id="fornecedor" requestURI="/granit/fornecedorAction.event?operacao=processarConsulta" class="tableResultadoConsulta">
				   
  <display:column style="text-align: center;"		 
  		title="Editar" paramId="idFornecedor" paramProperty="idFornecedor">
  		<html:img page="/images/btEditar.gif" onclick="javascript:editar(${fornecedor.idFornecedor});" alt="Editar"/>
  </display:column>
  
  <display:column  	style="text-align: center;"	 
  		title="Excluir" paramId="idFornecedor" paramProperty="idFornecedor">
  		<html:img page="/images/btExcluir.gif" onclick="javascript:excluir(${fornecedor.idFornecedor});" alt="Excluir"/>	
  </display:column>
  
  <display:column property="idFornecedor" title="Código" 
  		href="/granit/fornecedorAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" paramId="idFornecedor" paramProperty="idFornecedor"/>
  		
   
  <display:column property="nome" title="Nome" 
  		href="/granit/fornecedorAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" 
  		paramId="idFornecedor" paramProperty="idFornecedor"/>
  		
  <display:column property="cnpj" title="CNPJ" style="width: 120px;"/>   
 
</display:table>

