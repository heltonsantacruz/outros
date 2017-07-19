<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<div class="titulo">
    <bean:message key="title.cadastro.fornecedor"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/fornecedorAction.event" >
	<script type="text/javascript">
            
            function adicionar() { 
                action = '<html:rewrite page="/fornecedorAction.event?operacao=adicionar"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }
            
            function cancelar() { 
                action = '<html:rewrite page="/fornecedorAction.event?operacao=cancelarIncluir"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            } 
                        
            function atualizaMunicipios(){
             	action = '<html:rewrite page="/fornecedorAction.event?operacao=carregarMunicipios"/>';
                document.forms[0].action = action;
                document.forms[0].submit();   
            }

            function associar() { 
            	action = '<html:rewrite page="/fornecedorAction.event?operacao=associarProduto"/>';
                document.forms[0].action = action;
                document.forms[0].submit();        
            }

                                           
     </script>
<fieldset>

	<legend> <bean:message key="title.dados.fornecedor"/></legend>
	
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>
			<html:text property="nome" size="100" maxlength="100" style="TEXT-TRANSFORM: uppercase;"/>
			*
		</div>
		<div class="div_bloco">             	
            <label for="cnpj" class="form" id="cnpj"> <bean:message key="label.cnpj"/></label>
            <html:text property="cnpj" onblur="verificarCpfCpnj(this)" onkeypress="mascara(this,'99.999.999/9999-99');" size="20" maxlength="20"/>
            *					
		</div>
		
		<div class="div_bloco">
			<label for="estado" class="form"><bean:message key="label.uf"/></label>				
				<html:select property="uf" onchange="javascript:atualizaMunicipios();">
					<html:option value=""></html:option>
    				<html:optionsCollection name="estados" value="sigla" label="nome"/>	
    			</html:select>
				
			<label for="cidade" class="form_2"><bean:message key="label.cidade"/> </label>
				<html:select property="cidade">
					<html:option value=""></html:option>
    				<html:optionsCollection name="municipios" value="idMunicipio" label="nome"/>	
    			</html:select>    			
    			
			<label for="cep" class="form_2"><bean:message key="label.cep"/></label>
      		<html:text property="cep" size="10" maxlength="10" onkeypress="mascara(this,'99.999-999')" onblur="verificarCep(this)"/>
		</div>
		
		<div class="div_bloco">
			<label for="logradouro" class="form"><bean:message key="label.logradouro"/></label>
	        <html:text property="endereco" size="100" maxlength="100" style="TEXT-TRANSFORM: uppercase;"/>	        	
		</div>
				
		<div class="div_bloco">
			<label for="bairro" class="form"><bean:message key="label.bairro"/></label>
	        <html:text property="bairro" size="40" maxlength="40" style="TEXT-TRANSFORM: uppercase;"/>	        	
		</div>
		
		<div class="div_bloco">
			<label class="form"><bean:message key="label.telefone.comercial"/></label>
	        <html:text property="telefoneComercial" size="14" onkeypress="mascara(this,'(99)9999-9999');" maxlength="13"/>
	        			
			<label class="form_2"><bean:message key="label.telefone.celular"/></label>
	        <html:text property="celular" size="15" onkeypress="mascara(this,'(99)99999-9999');" maxlength="14"/>			
	        
	        <label class="form_2"><bean:message key="label.telefone.fax"/></label>
	        <html:text property="fax" size="14" onkeypress="mascara(this,'(99)9999-9999');" maxlength="13"/>
		</div>		
		
		<div class="div_bloco">
			<label for="email" class="form"><bean:message key="label.email"/></label>
	        <html:text property="email" size="50" maxlength="50" style="TEXT-TRANSFORM: uppercase;"/>			
		</div>
		<div class="div_bloco">  
            <label for="contatoFornecedor" class="form"> <bean:message key="label.contato.fornecedor"/></label>
			<html:text property="contatoFornecedor" size="100" maxlength="100" style="TEXT-TRANSFORM: uppercase;"/>
			*
		</div>
		
		<div class="div_bloco">
			<label for="produto" class="form"><bean:message key="label.produto"/></label>				
				<html:select property="produto" >
					<html:option value=""></html:option>
    				<html:optionsCollection name="produtosCadastrados" value="idProduto" label="descricaoTipoSubTipo"/>	
    			</html:select>	
    			*
    		    <input type="button" onclick="associar()" value="<bean:message key='btn.associar'/>" />			 		
		</div>		
		
</fieldset>

<logic:notEmpty name="produtosAssociados">
<fieldset class="fieldset">
<legend><bean:message key="label.produtos.associados"/></legend>
<display:table cellspacing="3" cellpadding="0" 
				name="produtosAssociados" pagesize="100" 
				id="item" class="tableResultadoConsulta2" decorator="org.displaytag.decorator.TotalTableDecorator" varTotals="totals">
  <display:column property="produto.idProduto" title="C�digo" />
  <display:column property="produto.descricaoTipoSubTipo" title="Nome Produto"/> 
  <display:column title="" value="Remover" href="/granit/fornecedorAction.event?operacao=removerProduto" paramId="posicao" paramProperty="posicao"/>
   
</display:table>
</fieldset>
</logic:notEmpty>

		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">
	    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="adicionar()" value="<bean:message key='btn.adicionar'/>" />
	</div> 
			

</html:form>	