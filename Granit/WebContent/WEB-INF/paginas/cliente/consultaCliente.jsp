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
	            		alert("Dado Inv�lido!"); 
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
	<bean:message key="title.consulta.cliente"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/clienteAction.event?operacao=processarConsulta">
<script type="text/javascript">
            function excluir(idPessoa) {            	
            	if(confirm('Deseja excluir o registro?')){            		
            		action = 'clienteAction.event?operacao=processarExcluir&idPessoa='+idPessoa;
                	document.forms[0].action = action;
                	document.forms[0].submit();
            	}  
            	return false;                             
            }
            function editar(idPessoa) {
            	action = 'clienteAction.event?operacao=abrirAlterar&idPessoa='+idPessoa;
                document.forms[0].action = action;
                document.forms[0].submit();            	                             
            }

			function atualizaTipoPessoa(){
                var tipoPessoa = document.forms[0].tipoPessoa.value;
                if(tipoPessoa == 'FISICA'){
                	document.getElementById('cpf').style.display = 'block';
               	 	document.getElementById('rg').style.display = 'block';
               		document.getElementById('idCpf').style.display = 'block';
               		document.getElementById('idCnpj').style.display = 'none';
                    document.getElementById('cnpj').style.display = 'none';    
                }
                else if(tipoPessoa == 'JURIDICA'){    
                	document.getElementById('cpf').style.display = 'none';
               		document.getElementById('rg').style.display = 'none';                     
                    document.getElementById('idCpf').style.display = 'none';
               		document.getElementById('idCnpj').style.display = 'block'; 
               		document.getElementById('cnpj').style.display = 'block';                               	
            	} 
                else{
                	document.getElementById('idCnpj').style.display = 'none';
                    document.getElementById('cnpj').style.display = 'none'; 
                    document.getElementById('cpf').style.display = 'none';
               		document.getElementById('rg').style.display = 'none';                     
                    document.getElementById('idCpf').style.display = 'none';
                }    
            }
         </script>   
<fieldset>
<br>

	<div class="div_bloco">
		<label for="tipoPessoa" class="form"><bean:message key="title.tipo.pessoa.cliente"/></label>
        <html:select property="tipoPessoa" onchange="atualizaTipoPessoa()">
        	<html:option value=""></html:option>
        	<html:option value="FISICA">F�sica</html:option>
        	<html:option value="JURIDICA">Jur�dica</html:option>        	
        </html:select>     	
	</div>
	
	<div class="div_bloco" id="rg" >  
            <label for="rg" class="form"> <bean:message key="label.rg"/></label>
			<html:text property="rg" size="10" maxlength="10" onkeypress="mascara(this,'9999999999')" />			
	</div>
	
	<div class="div_bloco" >  			
            <label for="cpf" class="form" id="cpf"> <bean:message key="label.cpf"/></label>
            <html:text styleId="idCpf" property="cpf" onblur="verificarCpfCpnj(this)" onkeypress="mascara(this,'999.999.999-99');" size="20" maxlength="20"/>	
            <label for="cnpj" class="form" id="cnpj"> <bean:message key="label.cnpj"/></label>
            <html:text styleId="idCnpj" property="cnpj" onblur="verificarCpfCpnj(this)" onkeypress="mascara(this,'99.999.999/9999-99');" size="20" maxlength="20"/>	
	</div>	
	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="title.nome.cliente"/></label>
        <html:text property="nome" size="50" maxlength="100" 
        onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>      	
	</div>
	<div class="div_bloco">
		<label class="form"><bean:message key="label.telefone.residencial"/></label>
        <html:text property="telefoneResidencial" size="14" onkeypress="mascara(this,'(99)9999-9999');" maxlength="13"/>
        			
		<label class="form_2"><bean:message key="label.telefone.celular"/></label>
        <html:text property="celular" size="15" onkeypress="mascara(this,'(99)99999-9999');" maxlength="14"/>			
        
        <label class="form_2"><bean:message key="label.telefone.fax"/></label>
        <html:text property="fax" size="14" onkeypress="mascara(this,'(99)9999-9999');" maxlength="13"/>
	</div>	
	    	   

	<div class="botoes">
        <html:submit><bean:message key="btn.consultar"/></html:submit>
    </div>	
</fieldset>
<script type="text/javascript">		
	atualizaTipoPessoa();
</script>
</html:form>
</div>

<display:table cellspacing="3" cellpadding="0" 
				name="listaClientesConsulta" pagesize="100"	partialList="true"	size="listSize" 
				id="cliente" requestURI="/granit/clienteAction.event?operacao=processarConsulta" class="tableResultadoConsulta"
				 >
  <display:setProperty name="paging.banner.some_items_found">
  <span class="pagebanner"> {0} registros em {5} p�ginas, mostrando p�gina {4}: Registros: ${registros}.</span>
  </display:setProperty>	
  <display:column style="text-align: center;"		 
  		title="Editar" paramId="idPessoa" paramProperty="idPessoa">
  		<html:img page="/images/btEditar.gif" onclick="javascript:editar(${cliente.idPessoa});" alt="Editar"/>
  </display:column>
  
  <display:column  	style="text-align: center;"	 
  		title="Excluir" paramId="idPessoa" paramProperty="idPessoa">
  		<html:img page="/images/btExcluir.gif" onclick="javascript:excluir(${cliente.idPessoa});" alt="Excluir"/>	
  </display:column>
  
  <display:column property="idPessoa" title="C�digo" 
  		href="/granit/clienteAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" paramId="idPessoa" paramProperty="idPessoa"/>
  		
  <display:column property="tipoPessoaFormatado" title="Tipo Cliente" 
  		 paramId="tipoPessoaFormatado" paramProperty="tipoPessoaFormatado"/>		
 
  <display:column property="nome" title="Nome" 
  		href="/granit/clienteAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" 
  		paramId="idPessoa" paramProperty="idPessoa"/>
  		

  		
  <display:column property="cpfcnpj" title="CPF/CNPJ" style="width: 120px;"/>   
 
</display:table>

