<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">

			<ui:define name="title">
				#{general['register.title']}
			</ui:define>
			
			<ui:define name="horizontalMenu">
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['register.title']}" styleClass="title"/>
				</p>
    			
    			<table align="center">
    				<tr>
    					<td>
			    			<p class="P-align-center">
			    				<f:subview rendered="#{RegisterBean.userNameAlreadyExists}" >
			    					<h:outputText value="#{general['register.errors.userNameAlreadyExists']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    				
			    				<f:subview rendered="#{RegisterBean.userNameInvalid}" >
			    					<h:outputText rendered="#{RegisterBean.userNameInvalid}" value="#{general['register.errors.userNameInvalid']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    				
			    				<f:subview rendered="#{RegisterBean.passwordInvalid}" >
			    					<h:outputText rendered="#{RegisterBean.passwordInvalid}" value="#{general['register.errors.passwordInvalid']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    				
			    				<f:subview rendered="#{RegisterBean.notSamePassword}" >
			    					<h:outputText rendered="#{RegisterBean.notSamePassword}" value="#{general['register.errors.notSamePassword']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    				
			    				<f:subview rendered="#{RegisterBean.emailInvalid}" >
			    					<h:outputText rendered="#{RegisterBean.emailInvalid}" value="#{general['register.errors.emailInvalid']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    			</p>
			    		</td>
			    	</tr>
			    </table>
    			
    			<br />
    			
				<table align="center">
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.userName']}" styleClass="label-right" for="inputUserName"/>
						</td>
						<td>
							<h:inputText value="#{RegisterBean.userName}" styleClass="input" id="inputUserName">
							</h:inputText>
						</td>
					</tr>
					
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.password']}" styleClass="label-right" for="inputPassword"/>
						</td>
						<td>
							<h:inputSecret value="#{RegisterBean.password}" styleClass="input" id="inputPassword">
							</h:inputSecret>
						</td>
					</tr>
					
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.reenterPassword']}" styleClass="label-right" for="inputReenterPassword"/>
						</td>
						<td>
							<h:inputSecret value="#{RegisterBean.reenterPassword}" styleClass="input" id="inputReenterPassword">
							</h:inputSecret>
						</td>
					</tr>
					
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.emailAddress']}" styleClass="label-right" for="inputEmailAddress"/>
						</td>
						<td>
							<h:inputText value="#{RegisterBean.emailAddress}" styleClass="input" id="inputEmailAddress"/>
						</td>
					</tr>
					
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.lastName']}" styleClass="label-right" for="intpuLastName"/>
						</td>
						<td>
							<h:inputText value="#{RegisterBean.lastName}" styleClass="input" id="intpuLastName"/>
						</td>
					</tr>
					
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.firstName']}" styleClass="label-right" for="intpuFirstName"/>
						</td>
						<td>
							<h:inputText value="#{RegisterBean.firstName}" styleClass="input" id="intpuFirstName"/>
						</td>
					</tr>
					
					<tr>
						<td class="align-right">
							<h:outputLabel value="#{general['register.middleName']}" styleClass="label-right" for="inputMiddleName"/>
						</td>
						<td>
							<h:inputText value="#{RegisterBean.middleName}" styleClass="input" id="inputMiddleName"/>
						</td>
					</tr>
				</table>
				
				<br />
				
				<center>
				<h:commandButton value="#{general['register.registerUser']}" action="#{RegisterBean.registerUser}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" id="registerUserButton"/>
				</center>
					
					
    				
    				
			</ui:define>
			
	    </ui:composition>
    </body>
    
</html>