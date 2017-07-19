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
          
     function imprimir() { 
          window.open('/granit/vendaAction.event?operacao=gerar','pdfwindow','765','600');             
     }    

     function excluir(idVenda) {        
    	if(confirm('Deseja excluir o registro?')){  
	    	action = 'vendaAction.event?operacao=processarExcluir&idVenda='+idVenda;
	       	document.forms[0].action = action;
	       	document.forms[0].submit();
    	}
    	return false;
     }

     function finalizarVenda(idVenda) {        
    	 if(confirm('Deseja realmente finalizar a Venda?Caso confirme, não poderá mais alterar nada nessa venda!')){
     		action = 'vendaAction.event?operacao=processarFinalizar&idVenda='+idVenda;
        	document.forms[0].action = action;
        	document.forms[0].submit();
    	 }
    	 return false;
      }
     
     
     function retornaStatusPendenteVenda(idVenda) {        
    	 if(confirm('Deseja realmente retornar para o status de Pendente?')){
     		action = 'vendaAction.event?operacao=processarRetornarStatusPendente&idVenda='+idVenda;
        	document.forms[0].action = action;
        	document.forms[0].submit();
    	 }
    	 return false;
      }

     function editar(idVenda) {            
     	action = 'vendaAction.event?operacao=abrirAlterar&idVenda='+idVenda;
        document.forms[0].action = action;
        document.forms[0].submit();
     }
     
</script>
 
<div class="titulo">
	<bean:message key="title.consulta.venda"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
	<html:form action="/vendaAction.event?operacao=processarConsulta">   
<fieldset>
<br>
	
	<div class="div_bloco">
		<label for="numeroPedido" class="form">Pedido (Nº / Ano):</label>				
		<html:text property="numeroPedido" size="10" maxlength="10" onkeypress="mascara(this,'9999999999');" />
		/ 					    		      	
		<html:text property="numeroPedidoAno" size="6" maxlength="4" onkeypress="mascara(this,'9999');" />
	</div>	
	
	<div class="div_bloco">
		<label for="data" class="form">Data da Venda:</label>
        De: <html:text property="dataInicio" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>
        <label for="data" class="form_2"></label>	
        Até: <html:text property="dataFim" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/> 	      	
	</div>
	
	<div class="div_bloco">
		<label for="cliente" class="form"><bean:message key="label.cliente.venda"/>:</label>				
		<html:select property="idCliente"  styleId="idCliente">
			<html:option value=""></html:option>
   				<html:optionsCollection name="listaClientesVendas" value="idPessoa" label="nome"/>	
		</html:select>						    		      	
	</div>	
	
	<div class="div_bloco">
		<label for="cliente" class="form">Situação:</label>				
		<html:select property="situacaoVenda">
			<html:option value="">Todas</html:option>
   			<html:option value="P">Pendente</html:option>
   			<html:option value="F">Finalizada</html:option>
		</html:select>						    		      	
	</div>	
	
	<div class="botoes">
        <html:submit><bean:message key="btn.consultar"/></html:submit>
    </div>	
</fieldset>

</div>

<display:table cellspacing="3" cellpadding="0" 	name="listaVendasConsulta" pagesize="100"  partialList="true"	size="listSize"
				id="venda" requestURI="/granit/vendaAction.event?operacao=processarConsulta" class="tableResultadoConsulta" 
				>
    
  <display:setProperty name="paging.banner.some_items_found">
		<span class="pagebanner"> {0} registros em {5} páginas,
		mostrando página {4}: Registros: ${registros}.</span>
	</display:setProperty>
  
  <display:column property="numeroPedidoFormatado" title="Número Pedido" 
  		href="/granit/vendaAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" paramId="idVenda" paramProperty="idVenda"/>
  
  <display:column style="text-align: center;"		 
  		title="Editar" paramId="idVenda" paramProperty="idVenda">
  		<html:img page="/images/btEditar.gif" onclick="javascript:editar(${venda.idVenda});" alt="Editar"/>
  </display:column>
  		
  <display:column style="text-align: center;" title="Excluir" paramId="idVenda" paramProperty="idVenda"><html:img page="/images/btExcluir.gif" onclick="javascript:excluir(${venda.idVenda});" alt="Excluir"/></display:column>
  
  
  <logic:equal name="usuarioLogado" property="perfilFormatado" value="Administrador">
  	<display:column style="text-align: center;" title="Finalizar / Retornar Status Pedente" paramId="idVenda" paramProperty="idVenda">
  		<logic:notEqual name="venda" property="situacao" value="F">
	  		<html:img page="/images/btFinish.png" onclick="javascript:finalizarVenda(${venda.idVenda});" alt="Finalizar"/>
	    </logic:notEqual>	
	    <logic:equal name="venda" property="situacao" value="F">
	  		<html:img page="/images/btRetornarStatus.png" onclick="javascript:retornaStatusPendenteVenda(${venda.idVenda});" alt="Retornar Status Pedente"/> 
	    </logic:equal>
  	</display:column>
  </logic:equal>
  
  <logic:notEqual name="usuarioLogado" property="perfilFormatado" value="Administrador">
  	<display:column style="text-align: center;" title="Finalizar" paramId="idVenda" paramProperty="idVenda">
  		<logic:notEqual name="venda" property="situacao" value="F">
	  		<html:img page="/images/btFinish.png" onclick="javascript:finalizarVenda(${venda.idVenda});" alt="Finalizar"/>
	    </logic:notEqual>	
	    <logic:equal name="venda" property="situacao" value="F">
	  		
	    </logic:equal>
  	</display:column>
  </logic:notEqual>
  
  <display:column property="cliente.nome" title="Cliente"/> 		
   		
  <display:column property="dataVendaFormatada" title="Data"/>
  
  <display:column property="prazoEntregaFormatado" title="Entrega"/>     
  
  <display:column property="produtosEntreguesFormatado" title="Entregue?"/>
  
  <display:column property="situacaoVendaFormatada" title="Situação"/>
  
  <display:column property="total" title="Valor" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>
 
 <display:footer>
    <tr bgcolor="#FFFF80">
      <td>TOTAL</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td>${totalizador}</td>
    <tr>
  </display:footer> 
</display:table>

<logic:present name="listaVendasConsulta">	

<logic:notEmpty name="listaVendasConsulta">
	<div class="botoes">
	    <input type="button" onclick="imprimir()" value="<bean:message key='btn.imprimir'/>" />
	</div> 
</logic:notEmpty>
</logic:present>

</html:form>

