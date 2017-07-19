<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

 
<div class="titulo">
	<bean:message key="title.consulta.produto"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/produtoAction.event?operacao=filtrarConsultaRelatorioEntradaSaida">
		<script type="text/javascript">
            
            function atualizaSubTipos(){
             	action = '<html:rewrite page="/produtoAction.event?operacao=carregarSubTiposConsultaRelatorioEntradaSaida"/>';
                document.forms[0].action = action;
                document.forms[0].submit();   
            }     
         </script>   
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
        <label for="descricao" class="form"> <bean:message key="label.descricao"/></label>
		<html:text name="produtoForm" property="descricao" size="50" maxlength="50" onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}"  />		
	</div>
	<div class="div_bloco">
			<label for="tipo" class="form"><bean:message key="label.tipo"/></label>				
				<html:select name="produtoForm" property="tipo" onchange="javascript:atualizaSubTipos();">
					<html:option value=""></html:option>
    				<html:optionsCollection name="tipos" value="idTipo" label="descricao"/>	
    			</html:select>	    		    		
		</div>
		<div class="div_bloco">
			<label for="fornecedor" class="form"><bean:message key="label.subtipo"/></label>				
				<html:select name="produtoForm" property="subTipo" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="subtipos" value="idSubTipo" label="descricao"/>	
    			</html:select>	
    			
		</div>	

	<div class="botoes">
        <html:submit><bean:message key="btn.consultar"/></html:submit>
    </div>	
</fieldset>
</html:form>
</div>

<display:table cellspacing="3" cellpadding="0" 
				name="listaHistoricoProdutosConsulta" pagesize="100" 
				id="historico" requestURI="/produtoAction.do?operacao=filtrarConsulta" class="tableResultadoConsulta" >
  
  <display:column property="produto.descricao" title="Descrição" />
  <display:column property="produto.tipoDetalhado" title="Tipo" />
  <display:column property="produto.subTipo.descricao" title="SubTipo" />
  <display:column property="quantidadeFormatado" title="Quant." />
  <display:column property="tipoFormatado" title="Movimentação" />  
  <display:column property="dataFormatada" title="Data" />
  <display:column property="qtdEstoqueFormatado" title="Quant. Estoque" />
  <display:column property="usuario.nome" title="Usuário" />
  
 
  
 

</display:table>

