<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>


<!-- INICIO DO LAYOUT PRINCIPAL -->
<html:html>
	<head>
	   <tiles:get name="head" />
	</head>
	<body>
	<!-- table  id="conteudo" class="layout" width="775" cellspacing="0" cellpadding="0" align="center"-->
		<div id="conteudo" align="center"> 
            <tiles:get name="topo" />

            <tiles:get name="menu" />

    		 <div id="navegacao">
                <p class="pNavegacao">
		          <tiles:getAsString name="navegacao"/>
		        </p>
		      </div>            

            <div id="conteudo">         
                <tiles:get name="conteudo" />
            </div>
            
            <tiles:get name="rodape" />
	</div>  
		<!-- /table-->
	</body>
</html:html>
<!-- FIM DO LAYOUT PRINCIPAL -->
