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
<html:form action="/produtoAction.event?operacao=filtrarConsulta">
		<script type="text/javascript">
            function excluir(idProduto) {            	
            	if(confirm('Deseja excluir o registro?')){            		
            		action = 'produtoAction.event?operacao=processarExcluir&idProduto='+idProduto;
                	document.forms[0].action = action;
                	document.forms[0].submit();
            	}  
            	return false;                             
            }
            
            function editar(idProduto) {
            	action = 'produtoAction.event?operacao=iniciarEditar&idProduto='+idProduto;
                document.forms[0].action = action;
                document.forms[0].submit();            	                             
            }
            
            function registrarEntradaSaida(idProduto) {
            	action = 'produtoAction.event?operacao=iniciarRegistroEntradaSaida&idProduto='+idProduto;
                document.forms[0].action = action;
                document.forms[0].submit();            	                             
            }

            function atualizaSubTipos(){
             	action = '<html:rewrite page="/produtoAction.event?operacao=carregarSubTiposConsulta"/>';
                document.forms[0].action = action;
                document.forms[0].submit();   
            }     
         </script>   
<fieldset>
<br>
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
				name="listaProdutosConsulta" pagesize="100" 
				id="produto" requestURI="/produtoAction.do?operacao=filtrarConsulta" class="tableResultadoConsulta" >
  
  <display:column title="Editar" paramId="idProduto" paramProperty="idProduto">
  	<html:img page="/images/edit-16.gif" onclick="javascript:editar(${produto.idProduto});" alt="Editar Produto"/>
  </display:column>  
  <display:column title="Excluir" paramId="idProduto" paramProperty="idProduto">
  	<html:img page="/images/delete-16.gif" onclick="javascript:excluir(${produto.idProduto});" alt="Excluir Produto"/>
  </display:column>
  <!-- retirada da coluna de Entrada/Saida conforme passo a passo do projeto 04/04/2011-->
  
  <display:column property="idProduto" title="Código" href="/granit/produtoAction.do?operacao=exibir" paramId="idProduto" paramProperty="idProduto"/>
  <display:column property="descricao" title="Descrição" href="/granit/produtoAction.do?operacao=exibir" paramId="idProduto" paramProperty="idProduto"/>
  <display:column property="tipoDetalhado" title="Tipo" />
  <display:column property="subTipo.descricao" title="SubTipo" />
  <display:column property="qtdEstoqueFormatado" title="Quant. Estoque" />

</display:table>

