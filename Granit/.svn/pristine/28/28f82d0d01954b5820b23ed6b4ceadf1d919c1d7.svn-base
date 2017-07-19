<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<script language="javascript" src="<%=request.getContextPath() %>/js/mascarasReais.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/jsutil.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/js/trataValor.js"></script>
 
<div class="titulo">
	Registro de Saídas de produtos
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<div class="conteudo">
<html:form action="/produtoAction.event?operacao=finalizarRegistrarEntradaSaida">
		<script type="text/javascript">

            function finalizar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=finalizarRegistrarEntradaSaida"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }

            function cancelar() { 
                action = '<html:rewrite page="/produtoAction.event?operacao=consultaRegistrarEntradaSaida"/>';
                document.forms[0].action = action;
                document.forms[0].submit();               
            }          

         </script>   
<fieldset>
<p align="center"><i>Informe as quantidades de saída e depois clique em finalizar.</i></p>
<br>
<br><br>
<%
  	int i = 0;
  %>
<display:table cellspacing="3" cellpadding="0" 
				name="listaProdutosConsultaRegistraSaida"
				id="produto" class="tableResultadoConsulta" >
  <display:column property="descricao" title="Descrição" />
  <display:column property="tipoMateriaPrima" title="Tipo" />
  <display:column property="subTipo.descricao" title="SubTipo" />
  <display:column property="qtdEstoqueFormatado" title="Quant. Estoque" />
  <display:column title="Quantidade">
  	<input type="text" name="quantidade<%=i++%>" size="15" maxlength="15" onkeypress="reaisLimitado(this,event,15,4)"
  	  		onkeydown="backspace(this,event,4)"/>
  </display:column>
</display:table>

</fieldset>

</html:form>

<logic:equal name="exibirErro" value="true" scope="request">
	<logic:iterate name="quantidadesSaidas" scope="request" id="valor" indexId="index">
		<script>
			document.forms[0].quantidade${index}.value = "${valor}";
		</script>
	</logic:iterate>
</logic:equal>


</div>
<div class="botoes">
    <input type="button" onclick="finalizar()" value="Registrar" />
    <input type="button" onclick="cancelar()" value="Cancelar" />
</div>	