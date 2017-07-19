<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<style media="screen" type="text/css">
     @import url(<html:rewrite page='/css/estilos.css'/>);
     @import url(<html:rewrite page='/css/estilos_granit.css'/>);
</style>

<div align="left" id="divMessages">  
	<ul>
		<logic:messagesPresent>
			<div class="divMessagesMessage">
				<html:messages id="error">													 		
					 	<li><p class="erro">		
								<bean:write name="error"/>			     		
							</p>	
						</li>	
									
				</html:messages>
			</div>
	 	</logic:messagesPresent>	
	 	<logic:messagesPresent message="true">
	 		<div class="divMessagesMessage">
			    <html:messages id="message" message="true">  
			    	<li><p class="mensagem">	
			      		<bean:write name="message"/>
			      		</p>
			      	</li>	  
			    </html:messages>
			</div>  
		</logic:messagesPresent> 
  	 </ul>
</div>		


