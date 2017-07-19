<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<div class="titulo">
    <bean:message key="title.alteracao.cliente"/>
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form focus="nome" action="/clienteAction.event" >
	<script type="text/javascript">
            
			function atualizar() { 
		        action = '<html:rewrite page="/clienteAction.event?operacao=processarAlterar"/>';
		        document.forms[0].action = action;
		        document.forms[0].submit();               
		    }
		    
		    function cancelar() { 
		        action = '<html:rewrite page="/clienteAction.event?operacao=cancelarIncluir"/>';
		        document.forms[0].action = action;
		        document.forms[0].submit();               
		    } 
		                
		    function atualizaMunicipios(){
		     	action = '<html:rewrite page="/clienteAction.event?operacao=carregarMunicipiosAlterar"/>';
		        document.forms[0].action = action;
		        document.forms[0].submit();   
		    }
		
		    function atualizaTipoPessoa(tipoPessoa){
		        
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
		    }
		
		    function atualizaTipoPessoaInicial(){   			    	          
				if(document.getElementById('radioCpf').checked == true){				
					atualizaTipoPessoa('FISICA');	
				}
				else{				
					atualizaTipoPessoa('JURIDICA');
				}
		    }    	   
                       
        </script>
<fieldset>

	<legend> <bean:message key="title.dados.cliente"/></legend>
	
		<div class="div_bloco">
			<label for="tipoPessoa" class="form"><bean:message key="title.tipo.pessoa.cliente"/></label> *
			<html:radio styleId="radioCpf"  property="tipoPessoa" value="FISICA" onclick="atualizaTipoPessoa('FISICA')" >Física</html:radio>
			<html:radio styleId="radioCnjp"  property="tipoPessoa" value="JURIDICA" onclick="atualizaTipoPessoa('JURIDICA')">Jurídica</html:radio>	           	
		</div>
		
		<div class="div_bloco" id="rg">  
            <label for="rg" class="form"> <bean:message key="label.rg"/></label>
			<html:text property="rg" size="10" maxlength="10" onkeypress="mascara(this,'9999999999')" />			
		</div>
		<div class="div_bloco">  			
            <label for="cpf" class="form" id="cpf"> <bean:message key="label.cpf"/></label>
            <html:text styleId="idCpf" property="cpf" onblur="verificarCpfCpnj(this)" onkeypress="mascara(this,'999.999.999-99');" size="20" maxlength="20"/>	
            <label for="cnpj" class="form" id="cnpj"> <bean:message key="label.cnpj"/></label>
            <html:text styleId="idCnpj" property="cnpj" onblur="verificarCpfCpnj(this)" onkeypress="mascara(this,'99.999.999/9999-99');" size="20" maxlength="20"/>	
					
		</div>
		
		
		<div class="div_bloco">  
            <label for="nome" class="form"> <bean:message key="label.nome"/></label>
			<html:text property="nome" size="100" maxlength="100" style="TEXT-TRANSFORM: uppercase;"/>
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
			<label class="form"><bean:message key="label.telefone.residencial"/></label>
	        <html:text property="telefoneResidencial" size="14" onkeypress="mascara(this,'(99)9999-9999');" maxlength="13"/>
	        			
			<label class="form_2"><bean:message key="label.telefone.celular"/></label>
	        <html:text property="celular" size="15" onkeypress="mascara(this,'(99)99999-9999');" maxlength="14"/>			
	        
	        <label class="form_2"><bean:message key="label.telefone.fax"/></label>
	        <html:text property="fax" size="14" onkeypress="mascara(this,'(99)9999-9999');" maxlength="13"/>
		</div>		

		<div class="div_bloco">
			<label for="email" class="form"><bean:message key="label.email"/></label>
	        <html:text property="email" size="50" maxlength="50" style="TEXT-TRANSFORM: uppercase;"/>			
		</div>		
		
</fieldset>
		
	<div class="observacao"><bean:message key="label.campo.obrigatorio"/></div>
	<div class="botoes">
	    <input type="button" onclick="cancelar()" value="<bean:message key='btn.cancelar'/>" />
	    <input type="button" onclick="atualizar()" value="<bean:message key='btn.atualizar'/>" />
	</div> 
			
<script type="text/javascript">		
	atualizaTipoPessoaInicial();
</script>
				
</html:form>	