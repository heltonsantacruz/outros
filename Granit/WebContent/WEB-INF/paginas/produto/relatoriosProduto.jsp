<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>


<div class="titulo">
    Relatórios de Produtos
</div>
<jsp:include page="/WEB-INF/comum/mensagens.jsp" />
<html:form action="/produtoAction.event" >
	<script type="text/javascript">            
            function fechar() {
                var tipoFechar =  "${tipoFechar}"; 
                document.forms[0].action = "produtoAction.event?operacao=fechar&tipoFechar="+tipoFechar;
				document.forms[0].submit();                
            }

            

            function imprimirRelacaoInsumoMesPorSemana(tipo){
            	var mes = document.forms[0].mes.value;
            	mes = parseInt(mes);
            	if (isNaN(mes) || mes < 1 || mes > 12){
            		alert("MÊS INVÁLIDO");
            		return;
            	}

            	var ano = document.forms[0].ano.value;
            	ano = parseInt(ano);
            	if (isNaN(ano) || ano == 0){
            		alert("ANO INVÁLIDO");
            		return;
            	}

            	abreMenor("produtoAction.do?operacao=gerarInsumoMesPorSemana&mes="+mes+"&ano="+ano+"&tipo="+tipo); 
            }
            
           
            function imprimirRelacaoMateriaPrimaMesPorSemana(){
            	var mes = document.forms[0].mes.value;
            	mes = parseInt(mes);
            	if (isNaN(mes) || mes < 1 || mes > 12){
            		alert("MÊS INVÁLIDO");
            		return;
            	}

            	var ano = document.forms[0].ano.value;
            	ano = parseInt(ano);
            	if (isNaN(ano) || ano == 0){
            		alert("ANO INVÁLIDO");
            		return;
            	}

            	abreMenor("produtoAction.do?operacao=gerarMateriaPrimaMesPorSemana&mes="+mes+"&ano="+ano); 
            }

            function imprimirConsumoDeMateriaisMes(tipo){
            	var mes = document.forms[0].mes.value;
            	mes = parseInt(mes);
            	if (isNaN(mes) || mes < 1 || mes > 12){
            		alert("MÊS INVÁLIDO");
            		return;
            	}

            	var ano = document.forms[0].ano.value;
            	ano = parseInt(ano);
            	if (isNaN(ano) || ano == 0){
            		alert("ANO INVÁLIDO");
            		return;
            	}

            	abreMenor("produtoAction.do?operacao=gerarConsumoMateriaisMes&mes="+mes+"&ano="+ano+"&tipo="+tipo); 
            }

            function imprimirConsumoDeMateriaPrimaMes(){
            	var mes = document.forms[0].mes.value;
            	mes = parseInt(mes);
            	if (isNaN(mes) || mes < 1 || mes > 12){
            		alert("MÊS INVÁLIDO");
            		return;
            	}

            	var ano = document.forms[0].ano.value;
            	ano = parseInt(ano);
            	if (isNaN(ano) || ano == 0){
            		alert("ANO INVÁLIDO");
            		return;
            	}

            	abreMenor("produtoAction.do?operacao=gerarConsumoMateriaPrimaMes&mes="+mes+"&ano="+ano); 
            }                
            
            function abreMenor(action){
        		var width =670;
        		var height =600;
        		var left = ((screen.width)/2)-(width/2);
        		var top = ((screen.height)/2)-(height/2);	
        		window.open(action, "", 'width='+width+', height='+height+', top='+top+', left='+left+', menuBar=no, toolbar=no, status=yes, resizeable=no, scrollBars=yes');
        	}                        
        </script>
<fieldset>
	<legend>Dados para relatório:</legend>
<div class="div_bloco">  
            <label for="mes" class="form">Mês:</label>
			<html:select property="mes">
				<html:option value="">-</html:option>
				<html:option value="1">Janeiro</html:option>
				<html:option value="2">Fevereiro</html:option>
				<html:option value="3">Março</html:option>
				<html:option value="4">Abril</html:option>
				<html:option value="5">Maio</html:option>
				<html:option value="6">Junho</html:option>
				<html:option value="7">Julho</html:option>
				<html:option value="8">Agosto</html:option>
				<html:option value="9">Setembro</html:option>
				<html:option value="10">Outubro</html:option>
				<html:option value="11">Novembro</html:option>
				<html:option value="12">Dezembro</html:option>
			</html:select>*
		</div>
		<div class="div_bloco">  			
            <label for="ano" class="form" id="ano">Ano:</label>
            <html:text styleId="ano" property="ano" onkeypress="mascara(this,'9999');" size="5" maxlength="4"/>*	
		</div>
</fieldset>

<fieldset>
<legend>Selecione o relatório</legend>
<br><br>
<html:link href="#" onclick="imprimirRelacaoInsumoMesPorSemana(2)">RELAÇÃO DE INSUMO MÊS</html:link>
<br><br>
<html:link href="#" onclick="imprimirConsumoDeMateriaisMes(2)">CONSUMO DE MATERIAIS(INSUMOS) MÊS</html:link>
<br><br>
<html:link href="#" onclick="imprimirRelacaoMateriaPrimaMesPorSemana()">RELAÇÃO DE MATÉRIA PRIMA MÊS</html:link>
<br><br>
<html:link href="#" onclick="imprimirConsumoDeMateriaPrimaMes()">CONSUMO DE MATÉRIA PRIMA MÊS</html:link>
<br><br>
<html:link href="#" onclick="imprimirRelacaoInsumoMesPorSemana(3)">RELAÇÃO DE DIVERSOS MÊS</html:link>
<br><br>
<html:link href="#" onclick="imprimirConsumoDeMateriaisMes(3)">CONSUMO DE MATERIAIS(DIVERSOS) MÊS</html:link>
<br><br>

</fieldset>

	<div class="botoes">	    
	    <input type="button" onclick="fechar()" value="<bean:message key='btn.fechar'/>" />	    
	</div> 
				
</html:form>	