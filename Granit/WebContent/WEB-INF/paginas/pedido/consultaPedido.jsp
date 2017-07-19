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
         window.open('/granit/pedidoAction.event?operacao=gerar','pdfwindow','765','600');             
    }   

 	function excluir(idPedido) {            	
    	if(confirm('Deseja excluir o pedido do sistema?')){            		
    		action = 'pedidoAction.event?operacao=processarExcluir&idPedido='+idPedido;
        	document.forms[0].action = action;
        	document.forms[0].submit();
    	}  
    	return false;                             
    }
    function editar(idPedido) {
    	action = 'pedidoAction.event?operacao=abrirAlterar&idPedido='+idPedido;
        document.forms[0].action = action;
        document.forms[0].submit();            	                             
    }

    function atualizaSubTipos(){
     	action = '<html:rewrite page="/pedidoAction.event?operacao=carregarSubTiposConsulta"/>';
        document.forms[0].action = action;
        document.forms[0].submit();   
    }

    function habilitaComboProduto(){						
		var tipoProduto = document.forms[0].idTipo.value;	
		if(tipoProduto == null || tipoProduto == '' || tipoProduto == 0 ){					
			document.forms[0].idProduto.disabled = false;
        	              		
        }
        else {    
        	document.forms[0].idProduto.value = 0;
        	document.forms[0].idProduto.disabled = true;                 	                   	
    	} 				
	}     


    
      
</script>
 
<div class="titulo">
	<bean:message key="title.consulta.pedido"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/pedidoAction.event?operacao=processarConsulta">

<fieldset>
<br>
	<div class="div_bloco">
		<label for="data" class="form"><bean:message key="title.data.pedido"/>:</label>
        De: <html:text property="dataInicio" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>
        <label for="data" class="form_2"></label>	
        Até: <html:text property="dataFim" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/> 	      	
	</div>	
	<div class="div_bloco">
			<label for="fornecedor" class="form"><bean:message key="label.fornecedor"/></label>				
				<html:select property="idFornecedor" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="fornecedoresCadastrados" value="idFornecedor" label="nome"/>	
    			</html:select>  					    		 		
	</div>
	
	<div class="div_bloco">
			<label for="tipo" class="form"><bean:message key="label.tipo"/></label>				
				<html:select property="idTipo" onchange="javascript:atualizaSubTipos();">
					<html:option value=""></html:option>
    				<html:optionsCollection name="tipos" value="idTipo" label="descricao"/>	
    			</html:select>	    		    		
	</div>
	<div class="div_bloco">
		<label for="fornecedor" class="form"><bean:message key="label.subtipo"/></label>				
			<html:select property="idSubTipo" >
				<html:option value=""></html:option>
   				<html:optionsCollection name="subtipos" value="idSubTipo" label="descricao"/>	
   			</html:select>	
   			
	</div>
	
	<div class="div_bloco">
			<label for="produto" class="form"><bean:message key="label.produto"/></label>				
				<html:select property="idProduto" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="produtosCadastrados" value="idProduto" label="descricaoTipoSubTipo"/>	
    			</html:select>  					    		 		
	</div>
	
	
	<div class="div_bloco">  
            <label for="mostrarPedidosFinalizados" class="form"> <bean:message key="label.mostrar.pedidos.finalizados"/></label>
           	Sim<html:radio property="mostrarPedidosFinalizados" value="true" />
           	Não<html:radio property="mostrarPedidosFinalizados" value="false"/>
    </div>	
	
	
	<div class="botoes">
        <html:submit><bean:message key="btn.consultar"/></html:submit>
    </div>	
</fieldset>

</div>

<display:table cellspacing="3" cellpadding="0" 	name="listaPedidosConsulta" pagesize="100"  partialList="true"	size="listSize"
				id="pedido" requestURI="/granit/pedidoAction.event?operacao=processarConsulta" class="tableResultadoConsulta" 
				>
    
  <display:setProperty name="paging.banner.some_items_found">
		<span class="pagebanner"> {0} registros em {5} páginas,
		mostrando página {4}: Registros: ${registros}.</span>
	</display:setProperty>
  
		
   <display:column title="Editar" style="text-align: center;">
  		<logic:present name="pedido" property="finalizado">
  				<logic:equal name="pedido" property="finalizadoFormatado" value="NÃO">
  					<html:img page="/images/btEditar.gif" onclick="javascript:editar(${pedido.idPedido});" alt="Editar"/>
  				</logic:equal>			
  		</logic:present>
  		<logic:notPresent name="pedido" property="finalizado">  				
  			<html:img page="/images/btEditar.gif" onclick="javascript:editar(${pedido.idPedido});" alt="Editar"/>  				
  		</logic:notPresent>
  </display:column>
  
   	
   <display:column title="Excluir">
   		<html:img page="/images/btExcluir.gif" onclick="javascript:excluir(${pedido.idPedido});" alt="Excluir"/>	  		
  </display:column>   
  		
 
  
  <display:column property="dataPedidoFormatada" title="Data"/> 		
   		
  <display:column property="fornecedor.nome" title="Fornecedor"/>
  
  <display:column property="produtoString" title="Produto" href="/granit/pedidoAction.event?operacao=detalhar&paginaRetorno=consultaAbrir" paramId="idPedido" paramProperty="idPedido"/>    
  
  <display:column property="totalSemDescontoFormatado" title="Subtotal"/> 
  
  <display:column property="descontoFormatado" title="Desconto"/> 
  
  <display:column property="totalComDescontoFormatado" title="Total"/>  
  
  <display:column property="finalizadoFormatado" title="Finalizado"/>  
  
 
 
</display:table>

<logic:present name="listaPedidosConsulta">	

<logic:notEmpty name="listaPedidosConsulta">
	<div class="botoes">
	    <input type="button" onclick="imprimir()" value="<bean:message key='btn.imprimir'/>" />
	</div> 
</logic:notEmpty>
</logic:present>


<script type="text/javascript">		
	habilitaComboProduto();
</script>
</html:form>