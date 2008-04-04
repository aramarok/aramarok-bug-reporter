<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">

			<ui:define name="title">
				#{general['login.title']}
			</ui:define>
			
			<ui:define name="horizontalMenu">
			</ui:define>
			
			<ui:define name="body">
				<table width="100%" height="100%" align="center" style="vertical-align:middle; horizontal-align:middle;">
					<tr height="100%" align="center" style="vertical-align:middle; horizontal-align:middle;">
						<td width="100%" height="100%" align="center" style="vertical-align:middle; horizontal-align:middle;">
							<table class="login-table">
								<tr>
			        		 		<td colspan="2" height="20px">   
			        		 			<h:outputText styleClass="error" rendered="#{LoginBean.invalidData}" value="#{general['login.error.message.validation']}"/> 
				        		 		<h:outputText styleClass="error" rendered="#{LoginBean.invalidUserName}" value="#{general['login.error.message.invalidUsername']}"/> 
				        		 		<h:outputText styleClass="error" rendered="#{LoginBean.invalidPassword}" value="#{general['login.error.message.invalidPassword']}"/>
				        		 		<h:outputText styleClass="error" rendered="#{LoginBean.disabledAccount}" value="#{general['login.error.message.disabledAccount']}"/> 
				        		 	</td>
				    	     	</tr>
								<tr>
			         				<td class="label-right">#{general['login.user.name']}:</td>
			        	 			<td class="left-align"><h:inputText value="#{LoginBean.userName}" styleClass="input" id="userNameField" /></td>
				    	     	</tr>
					         	<tr>
			        		 		<td class="label-right">#{general['login.user.password']}:</td>
			    	    	 		<td class="left-align"><h:inputSecret value="#{LoginBean.password}" styleClass="input" id="passwordField" /></td>
					         	</tr>
			    	     		<tr>
			    	     			<td> </td>
			        		 		<td style="padding-top:5px;" align="left"><h:commandButton value="#{general['login.loginButton']}" action="#{LoginBean.login}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" id="loginButton"/></td>
				    	     	</tr>
							</table>
							<br/>
							<h:commandLink id="registerNewAccount" value="#{general['login.register']}" action="#{LoginBean.register}"/>
						</td>
					</tr>
				</table>
			</ui:define>
	    </ui:composition>
    </body>
</html>