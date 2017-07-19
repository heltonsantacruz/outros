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
	<bean:message key="title.consulta.usuario"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/usuarioAction.event?operacao=processarConsulta">
<script type="text/javascript">
            function excluir(idUsuario) {            	
            	if(confirm('Deseja excluir o registro?')){            		
            		action = 'usuarioAction.event?operacao=processarExcluir&idUsuario='+idUsuario;
                	document.forms[0].action = action;
                	document.forms[0].submit();
            	}  
            	return false;                             
            }
            function editar(idUsuario) {
            	action = 'usuarioAction.event?operacao=abrirAlterar&idUsuario='+idUsuario;
                document.forms[0].action = action;
                document.forms[0].submit();            	                             
            }
         </script>   
<fieldset>
<br>
	 <div class="div_bloco">  
            <label for="login" class="form"> <bean:message key="label.login"/></label>
			<html:text property="login" size="20" maxlength="20"/>
		</div>		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>
			<html:text property="nome" size="40" maxlength="40"/>
		</div>		
		<div class="div_bloco">
			<label for="perfil" class="form"><bean:message key="title.usuario.perfil"/></label>			
			<html:select property="perfil">
					<html:option value=""></html:option>
    				<html:optionsCollection name="listaPerfis" value="idPerfil" label="nome"/>	
    			</html:select> 
		</div>		    	    	    	   

	<div class="botoes">
        <html:submit><bean:message key="btn.consultar"/></html:submit>
    </div>	
</fieldset>
</html:form>
</div>

<display:table cellspacing="3" cellpadding="0" 
				name="listaUsuarios" pagesize="10"	partialList="true"	size="listSize" 
				id="usuario" requestURI="/granit/usuarioAction.event?operacao=processarConsulta" class="tableResultadoConsulta"
				 >
  <display:setProperty name="paging.banner.some_items_found">
  <span class="pagebanner"> {0} registros em {5} páginas, mostrando página {4}: Registros: ${registros}.</span>
  </display:setProperty>	
  <display:column style="text-align: center;"		 
  		title="Editar" paramId="idUsuario" paramProperty="idUsuario">
  		<html:img page="/images/btEditar.gif" onclick="javascript:editar(${usuario.idUsuario});" alt="Editar"/>
  </display:column>
  
  <display:column  	style="text-align: center;"	 
  		title="Excluir" paramId="idUsuario" paramProperty="idUsuario">
  		<html:img page="/images/btExcluir.gif" onclick="javascript:excluir(${usuario.idUsuario});" alt="Excluir"/>	
  </display:column>
  
  <display:column property="idUsuario" title="Código" 
  		href="/granit/usuarioAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" paramId="idUsuario" paramProperty="idUsuario"/>
 
  <display:column property="login" title="Login" 
  		href="/granit/usuarioAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" 
  		paramId="idUsuario" paramProperty="idUsuario"/>
  
  <display:column property="nome" title="Nome" 
  		href="/granit/usuarioAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" 
  		paramId="idUsuario" paramProperty="idUsuario"/>		
  		
  <display:column property="perfilFormatado" title="Perfil"/>
 
</display:table>