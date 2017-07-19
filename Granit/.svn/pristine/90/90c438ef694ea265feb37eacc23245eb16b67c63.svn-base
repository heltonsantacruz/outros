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
<html:form action="/produtoAction.event?operacao=consultaRegistrarEntradaSaida">
		<script type="text/javascript">

			selecionar = true;
		
            function atualizaSubTipos(){
             	action = '<html:rewrite page="/produtoAction.event?operacao=carregarSubTiposConsultaRegistroSaidas"/>';
                document.forms[0].action = action;
                document.forms[0].submit();   
            }    

            function consultar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=consultaRegistrarEntradaSaida"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }        

            function registrarS() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=registrarEntradaSaidaPasso2"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }      

            function selecionar_tudo(){
               if (!selecionar){
            	   deselecionar_tudo();
            	   return;
               }
               for (i=0;i<document.forms[0].elements.length;i++){
                  if(document.forms[0].elements[i].type == "checkbox"){
                     document.forms[0].elements[i].checked=1
                  }
               }
               selecionar = false;
            } 

            function deselecionar_tudo(){
           	   for (i=0;i<document.forms[0].elements.length;i++){
           	      if(document.forms[0].elements[i].type == "checkbox"){
           	         document.forms[0].elements[i].checked=0
           	      }
           	   }
           	   selecionar = true;	
           	}
            	            
               
         </script>   
<fieldset>
<br>
	<div class="div_bloco">  
        <label for="descricao" class="form"> <bean:message key="label.descricao"/></label>
		<html:text name="produtoForm" property="descricao" size="100" maxlength="50" onkeydown="if(event.keyCode == 13){event.cancelBubble = true; event.returnValue = false; return false;}" style="TEXT-TRANSFORM: uppercase;" />		
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
        <input type="button" onclick="consultar()" value="Consultar" />
    </div>	
</fieldset>
<fieldset>
<p align="center"><i>Selecione os produtos para registrar a saída ou clique no botão abaixo para selecionar todos.</i></p>
<br>
<input type="button" onclick="selecionar_tudo()" value="Selecionar Todos" />
<br><br>
<%int i = 0; %>
<display:table cellspacing="3" cellpadding="0" 
				name="listaProdutosConsulta"
				id="produto" requestURI="/produtoAction.do?operacao=filtrarConsulta" class="tableResultadoConsulta" >
  <display:column>
  	<input type="checkbox" name="produto<%=i++%>"/>
  </display:column>
  <display:column property="descricao" title="Descrição" />
  <display:column property="tipoMateriaPrima" title="Tipo" />
  <display:column property="subTipo.descricao" title="SubTipo" />
  <display:column property="qtdEstoqueFormatado" title="Quant. Estoque" />
</display:table>

</fieldset>

</html:form>
</div>
<div class="botoes">
       <input type="button" onclick="registrarS()" value="Registrar Saídas" />
</div>	