<%@page import="br.com.granit.persistencia.to.VendaTO"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>
 
<div class="titulo">
	Informe os dados da Venda.
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />

<logic:present name="forwardSecao">
	<script>
		var anchor = '${forwardSecao}';
	</script>		
</logic:present>
<logic:notPresent name="forwardSecao">
	<script>
		var anchor = '';
	</script>
</logic:notPresent>
<div class="conteudo">
<html:form action="/vendaAction.event?operacao=processarSalvar">
	<script type="text/javascript">
            
            function adicionarItem() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=adicionarItem"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function adicionarItemBeneficiamento() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=adicionarItemBeneficiamento"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function removerItem(posicao) {                
                action = '<html:rewrite page="/vendaAction.event?operacao=removerItem&posicao="/>' + posicao;
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function removerItemBeneficiamento(posicao) {                
                action = '<html:rewrite page="/vendaAction.event?operacao=removerItemBeneficiamento&posicao="/>' + posicao;
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function salvar() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=processarSalvar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function cancelar() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=cancelar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function finalizar() { 
            	if(confirm('Deseja realmente finalizar a Venda?Caso confirme, n�o poder� mais alterar nada nessa venda!')){
	            	action = '<html:rewrite page="/vendaAction.event?operacao=processarSalvar&finalizar=true"/>';
	                document.forms[0].action = action;
	                document.forms[0].submit();               
            	}    
            }   

            function atualizaTotalAPagar() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=aplicaDescontoVenda"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function atualizaTotalAPagarServicoMontagem() { 
                action = '<html:rewrite page="/vendaAction.event?operacao=aplicaValorServicoMontagem"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }    

            function marcarCheckbox(cb){
            	cb.checked = true;
            }      

            function aplicaDesconto(desconto){
            	 action = '<html:rewrite page="/vendaAction.event?operacao=aplicaDescontoVendaPadrao"/>';
                 document.forms[0].action = action;
                 document.forms[0].submit();
            }    
            
            function goToAnchor() {
            	if (anchor != ''){
            		var divMessages = document.getElementById('divMessages');
            		var divAnchor = document.getElementById(anchor + 'Message');
            		divAnchor.innerHTML = divMessages.innerHTML;
            		divMessages.innerHTML = '';
            		window.location.hash = anchor; 
            	}
			}
            
            function produtoEntregue(){
            	var entregue = document.forms[0].entregue.checked;
            	if(entregue){
            		if(!confirm('O produto realmente foi entregue? Caso confirme, a �nica opera��o permitida ser� a de Finalizar a venda!')){   
            			document.forms[0].entregue.checked = false;
            		}
            	}
            }
        </script>


<fieldset>
<%
	VendaTO venda = (VendaTO) request.getSession().getAttribute("vendaCliente");
%>
<legend>Dados da Venda:</legend>
<br>

	<div class="div_bloco">
		<label for="cliente" class="form">Cliente:</label>				
		<html:select property="idCliente" styleId="idCliente">
			<html:option value=""></html:option>
   				<html:optionsCollection name="listaClientesVendas" value="idPessoa" label="nome"/>	
		</html:select>						    		      	
	</div>	
	
	<div class="div_bloco">
		<label for="data" class="form">Prazo de Entrega:</label>
        <html:text property="dataEntrega" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>
     </div>
     <!-- //26/03/2011 - desabilitando datas de pedido e vendas -->
     <!--
     <div class="div_bloco">
		<label for="data" class="form">Data da Venda:</label>
        <html:text property="dataInformadaVenda" size="10" maxlength="10" onblur="validaCampoData(this)" onkeypress="mascara(this,'99/99/9999');" 
        	onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"/>
     </div>
     -->
</fieldset>

<a name="secaoItens"><br/></a>
<div id="secaoItensMessage" align="left"></div>

<fieldset>
<legend>Defina os itens para a Venda.</legend>
	<div class="div_bloco">
		<label for="itemVenda" class="form">
			Descri��o do item:
		</label>       	
       	<html:text property="descricaoItem" size="50" style="TEXT-TRANSFORM: uppercase;"/>    	  	
	</div> 		
	<div class="div_bloco">
		<label for="quantidade" class="form">
			Valor do Item (R$):
		</label> 	
		<html:text property="valorItem" onfocus="limpaCampo(this)"
				 size="15" maxlength="15" onkeypress="reaisLimitado(this,event,15)" onkeydown="backspace(this,event)"/>  	
	</div>
	
	<div class="div_bloco_1">
		<label for="itemVenda" class="form">
			Produto:
		</label>       	
       	<html:select property="idProdutoItem">
			<html:option value=""></html:option>
    		<html:optionsCollection name="produtosVenda" value="idProduto" label="descricaoTipoSubTipo"/>	
    	</html:select>    	  	
	</div> 		
	
	<div class="div_bloco_2">
		<label for="metragem" class="form">
			Metragem:
		</label> 	
		<html:text property="metragem" 
				 size="15" maxlength="15" onkeypress="reaisLimitado(this,event,15,4)" 
				 onkeydown="backspace(this,event,4)"/>			 	
	</div>


</fieldset>
	<div class="botoes">
        <input type="button" onclick="adicionarItem()" value="<bean:message key='btn.adicionarItem'/>" />        
    </div>	

</div>

<fieldset class="fieldset">
<logic:present name="listaItensVenda">
<legend>Itens da Venda:</legend>
<display:table cellspacing="3" cellpadding="0" 
				name="listaItensVenda" pagesize="100" 
				id="item" class="tableResultadoConsulta" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
  <display:column property="descricao" title="Descri��o" />
  <display:column property="preco" title="Valor(R$)" decorator="br.com.granit.util.decoradores.MoedaDecorator"/>
  <display:column property="produto.descricaoTipoSubTipo" title="Produto" />
  <display:column property="metragemFormatada" title="Metragem" />
  <display:column title="Remover">
  		<html:link href="#" onclick="removerItem(${item.posicao})"><font color="#004080"><u>Remover</u></font></html:link>
  </display:column>
</display:table>
<br>
</logic:present>
</fieldset>


<a name="secaoItensBeneficiamento"><br/></a>
<div id="secaoItensBeneficiamentoMessage" align="left"></div>
<fieldset>
<legend>Defina os itens de beneficiamento.</legend>
	<div class="div_bloco">
		<label for="itemVenda" class="form">
			Descri��o do item:
		</label>       	
       	<html:text property="descricaoItemBeneficiamento" size="50" style="TEXT-TRANSFORM: uppercase;"/>    	  	
	</div> 		
	<div class="div_bloco">
		<label for="quantidade" class="form">
			Valor do Item (R$):
		</label> 	
		<html:text property="valorItemBeneficiamento" onfocus="limpaCampo(this)"
				 size="15" maxlength="15" onkeypress="reaisLimitado(this,event,15)" onkeydown="backspace(this,event)"/>  	
	</div>
		
</fieldset>
	<div class="botoes">
        <input type="button" onclick="adicionarItemBeneficiamento()" value="<bean:message key='btn.adicionarItem'/>" />        
    </div>	



<logic:present name="listaItensVendaBeneficiamento">
<fieldset class="fieldset">
<legend>Itens de Beneficiamento da Venda:</legend>
<display:table cellspacing="3" cellpadding="0" 
				name="listaItensVendaBeneficiamento" pagesize="100" 
				id="item" class="tableResultadoConsulta" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
  <display:column property="descricao" title="Descri��o" />
  <display:column property="valor" title="Valor(R$)" decorator="br.com.granit.util.decoradores.MoedaDecorator"/> 
  <display:column title="Remover">
  		<html:link href="#" onclick="removerItemBeneficiamento(${item.posicao})"><font color="#004080"><u>Remover</u></font></html:link>
  </display:column>
</display:table>
<br>
</fieldset>
</logic:present>

<a name="secaoServicoMontagem"><br/></a>
<div id="secaoServicoMontagemMessage" align="left"></div>
<fieldset>
<legend>Servi�o de Montagem:</legend>
<div class="div_bloco">
		<label for="valorServicoMontagemVenda" class="form">
			Valor do servi�o(R$):
		</label> 	
		<html:text onblur="atualizaTotalAPagarServicoMontagem()" property="valorServicoMontagem"
				 size="15" maxlength="15" onkeypress="reaisLimitado(this,event,15)" onkeydown="backspace(this,event)"/>  	
	</div>	
</fieldset>	

<a name="secaoTotalizacao"><br/></a>
<div id="secaoTotalizacaoMessage" align="left"></div>
<fieldset>
<legend>Totaliza��o da Venda:</legend>
<br>

	<div class="div_bloco">
		<label for="nome" class="form"><bean:message key="label.subtotal.venda"/></label>
       	<bean:write name="vendaCliente" property="subTotalFormatado"/>	
       	<%if (!venda.isVendaFinalizada()) { %>
       		<html:button property="aplicarDesconto" onclick="aplicaDesconto(5)">Aplicar desconto</html:button>
       	<%}%>			
       	
	</div>

	<div class="div_bloco">
		<label for="desconto" class="form">DESCONTO:</label>
       	<html:text property="desconto" onblur="atualizaTotalAPagar()" onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)" size="10" maxlength="4"/>
	        	 <bean:write name="vendaCliente" property="percentualDescontoFormatado"/>		
	</div>
			   
	<div class="div_bloco">
		<label for="total" class="form">TOTAL:</label>
       	<bean:write name="vendaCliente" property="totalFormatado"/>		
	</div>  

</fieldset>

<fieldset>
<legend>Forma(s) de Pagamento:</legend>
	<br>
	<div class="div_bloco_1">
		<label for="valor" class="form">
			� Vista:
		</label> 	
		<html:text property="valorVista" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<br>
	<div class="div_bloco_1">
		<label for="valor" class="form">
			Cheque:
		</label>
 			<html:text property="valorCheque" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<br>
	<div class="div_bloco_1">
		<label for="valor" class="form">
			Cart�o de Cr�dito:
		</label>
 	
		<html:text property="valorCredito" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<br>
	<div class="div_bloco_1">
		<label for="valor" class="form">
			Boleto:
		</label>
 	
		<html:text property="valorBoleto" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>
	<br>
	<div class="div_bloco_1">
		<label for="valor" class="form">
			Em Aberto:
		</label> 	
		<html:text property="valorAberto" size="15" maxlength="15"
	        	 onkeypress="reaisLimitado(this,event,15)"
	        	 onkeydown="backspace(this,event)"/>
  	
	</div>	
	<div class="div_bloco_2">
  		<label for="parcela" class="form">
			N� de Parcelas:
		</label>
		<html:text property="numeroParcelas" size="10" onfocus="limpaCampo(this)" maxlength="3" onkeypress="mascara(this,'999');"/>
	</div>
	<br><br>
	<div class="div_bloco_2">
		<label for="observacao" class="form">
			Observa��o:
		</label>       	
       	<html:textarea property="observacao" rows="10" cols="80"/>
	</div>
	<br><br>	
	<div class="div_bloco_1">
		<label for="entregue" class="form">
			Produtos/Servi�os entregues:
		</label>       	
       	<%if (venda.isVendaFinalizada()) { //S� edita se ainda n�o tiver confirmado a entrega %>	
       		<html:checkbox property="entregue" disabled="true"/>
       	<%}else {%>
       		<!-- Retirado a pedido de Isana em 12/07/2017 -->
       	 	<!--<html:checkbox property="entregue" onclick="produtoEntregue()"/> -->
       	 	<html:checkbox property="entregue" />
       	<%}%> 	  	
	</div> 		
</fieldset>

<div class="botoes">
	<input type="button" onclick="salvar()" value="<bean:message key='btn.salvar'/>" />
    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	<input type="button" onclick="finalizar()" value="<bean:message key='btn.finalizar'/>" />
</div>	
</html:form>
<script>
	goToAnchor();
</script>