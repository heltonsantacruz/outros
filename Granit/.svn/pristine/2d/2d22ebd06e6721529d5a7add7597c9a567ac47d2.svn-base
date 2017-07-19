<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>

<div class="titulo">
    <bean:message key="title.alterar.produto"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form action="/produtoAction.event" >
	<script type="text/javascript">
           
            function atualizar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=atualizar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function voltar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=abrirConsulta"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }   

            function removerFornecedor(posicao) {                
                action = '<html:rewrite page="/produtoAction.event?operacao=removerFornecedorAlteracao&posicao="/>' + posicao;
                document.forms[0].action = action;
                document.forms[0].submit();               
            }      

            function associar() { 
            	action = '<html:rewrite page="/produtoAction.event?operacao=associarFornecedorAlteracao"/>';
                document.forms[0].action = action;
                document.forms[0].submit();        
            }    

            function atualizaSubTipos(){
             	action = '<html:rewrite page="/produtoAction.event?operacao=carregarSubTiposAlterar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();   
            }         
            
        </script>
<fieldset >

	<legend> <bean:message key="title.dados.produto"/></legend>
	
		<div class="div_bloco">  
            <label for="idProduto" class="form"> <bean:message key="label.codigo"/></label>            
			<html:text name="produtoForm" property="idProduto" readonly="true"/>			
		</div>		
		<div class="div_bloco">  
            <label for="descricao" class="form"> <bean:message key="label.descricao"/></label>
			<html:text name="produtoForm" property="descricao" size="100" maxlength="50" style="TEXT-TRANSFORM: uppercase;"/>
			*
		</div>
		<div class="div_bloco">  
            <label for="quantidadeEstoque" class="form"> <bean:message key="label.quantidade.estoque"/></label>
			<html:text name="produtoForm" property="quantidadeEstoque" size="15" maxlength="15"
	        	onkeypress="reaisLimitado(this,event,15,4)"
	        	onkeydown="backspace(this,event,4)"/>	
		</div>		
		<div class="div_bloco">
			<label for="tipo" class="form"><bean:message key="label.tipo"/></label>				
				<html:select property="tipo" onchange="javascript:atualizaSubTipos();">
					<html:option value=""></html:option>
    				<html:optionsCollection name="tipos" value="idTipo" label="descricao"/>	
    			</html:select>	    		    		
		</div>
		<div class="div_bloco">
			
    		<logic:equal name="produtoForm" property="tipo" value="1">
    			<label for="tipoMateria" class="form"><bean:message key="label.tipo.materia.prima"/></label>	
    			<html:radio name="produtoForm" property="tipoMateriaPrima" value="1">Chapa</html:radio> 
    			<html:radio name="produtoForm" property="tipoMateriaPrima" value="2">Ladrilho</html:radio>   		
    		</logic:equal>  		    		
		</div>
		<div class="div_bloco">
			<label for="fornecedor" class="form"><bean:message key="label.subtipo"/></label>				
				<html:select property="subTipo" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="subtipos" value="idSubTipo" label="descricao"/>	
    			</html:select>	
    			
		</div>	
		
		<div class="div_bloco">
			<label for="fornecedor" class="form"><bean:message key="label.fornecedor"/></label>				
				<html:select property="fornecedor" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="fornecedoresCadastrados" value="idFornecedor" label="nomeCnpj"/>	
    			</html:select>	    			
    		    <input type="button" onclick="associar()" value="<bean:message key='btn.associar'/>" />			 		
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
  <display:column title="Remover">
  		<html:link href="#" onclick="removerFornecedor(${item.posicao})"><font color="#004080"><u>Remover</u></font></html:link>
  </display:column> 
</display:table>
</fieldset>
</logic:notEmpty>
		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">	    
	    <input type="button" onclick="voltar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="atualizar()" value="<bean:message key='btn.atualizar'/>" />
	</div> 

				
</html:form>	