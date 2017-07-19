<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>


<div class="titulo">
    <bean:message key="title.detalhar.produto"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form action="/produtoAction.event" >
	<script type="text/javascript">            
            function fechar() {
                var tipoFechar =  "${tipoFechar}"; 
                document.forms[0].action = "produtoAction.event?operacao=fechar&tipoFechar="+tipoFechar;
				document.forms[0].submit();                
            }                        
        </script>
<fieldset >

	<legend> <bean:message key="title.dados.produto"/></legend>
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.codigo"/></label>:
            <bean:write name="produtoTO" property="idProduto" />
		</div>	
		<div class="div_bloco">  
            <label for="descricao" class="form"> <bean:message key="label.descricao"/></label>:
            <bean:write name="produtoTO" property="descricao"/>			
		</div>
		<logic:notEmpty name="produtoTO" property="subTipo">
			<div class="div_bloco">  
	            <label for="tipo" class="form"> <bean:message key="label.tipo"/></label>:
	            <bean:write name="produtoTO" property="subTipo.tipo.descricao"/>			
			</div>
		
			<div class="div_bloco">  
	            <label for="subtipo" class="form"> <bean:message key="label.subtipo"/></label>:
	            <bean:write name="produtoTO" property="subTipo.descricao"/>			
			</div>
		</logic:notEmpty>
		<logic:notEmpty name="produtoTO" property="tipo">
			<div class="div_bloco">  
	            <label for="tipo" class="form"> <bean:message key="label.tipo.materia.prima"/></label>:
	            <bean:write name="produtoTO" property="nomeTipoMateriaPrima"/>			
			</div>			
		</logic:notEmpty>	
		<div class="div_bloco">  
            <label for="quantidadeEstoque" class="form"> <bean:message key="label.quantidade.estoque"/></label>:
            <bean:write name="produtoTO" property="qtdEstoqueFormatado" />			
		</div>
		
				
</fieldset>

<logic:notEmpty name="fornecedoresAssociados">
<fieldset class="fieldset">
<legend><bean:message key="label.fornecedores.associados"/></legend>

<display:table cellspacing="3" cellpadding="0" 
				name="fornecedoresAssociados" pagesize="100" 
				id="item" class="tableResultadoConsulta">
  <display:column property="fornecedor.idFornecedor" title="Código" />
  <display:column property="fornecedor.nome" title="Nome"/> 
  <display:column property="fornecedor.cnpj" title="CNPJ"/>
</display:table>
</fieldset>
</logic:notEmpty>

<logic:present name="listaHistoricoEntradasSaidas">
<fieldset>

	<legend>Histórico de alterações de Estoque:</legend>

	<display:table cellspacing="3" cellpadding="0" 
				name="listaHistoricoEntradasSaidas" pagesize="100" 
				id="historico" class="tableResultadoConsulta" >
		<display:column property="nomeUsuario" title="Usuário" />
  		<display:column property="dataFormatada" title="Data" />
  		<display:column property="tipoFormatado" title="Tipo" />
  		<display:column property="qtdEstoqueFormatado" title="Quantidade" />	
	</display:table>
</fieldset>		
</logic:present>



	
	<div class="botoes">	    
	    <input type="button" onclick="fechar()" value="<bean:message key='btn.fechar'/>" />	    
	</div> 
				
</html:form>	