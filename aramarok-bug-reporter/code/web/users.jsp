<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">

			<ui:define name="title">
				#{general['users.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['users.title']}" styleClass="title"/>
				</p>
    			
    	    	
    	    	<table>
    	    		<tr>
    	    			<td>
    	    				<h:outputLabel value="#{general['users.existingUsers']}: " for="inputUserName" styleClass="label-right"/>
							<h:selectOneMenu id="inputUserName" disabled="#{UsersBean.editUser}" valueChangeListener="#{UsersBean.valueChangeListener}" value="#{UsersBean.userNameSelected}" onchange="submit();" styleClass="inputDefaultSizeLeft">
								<f:selectItems value="#{UsersBean.userNameList_out}" />
							</h:selectOneMenu>
						</td>
					</tr>					
				</table>
				
				<table>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.userName']}: " for="inputUserName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputUserName" disabled="#{!UsersBean.editUser}" styleClass="input" value="#{UsersBean.userName}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.password']}: " for="inputPassword" styleClass="label-right"/>
							</td>
							<td>
								<h:inputSecret id="inputPassword" disabled="#{!UsersBean.editUser}" styleClass="input" value="#{UsersBean.password}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.emailAddress']}: " for="inputEmailAddress" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputEmailAddress" disabled="#{!UsersBean.editUser}" styleClass="input" value="#{UsersBean.emailAddress}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.firstName']}: " for="inputFirstName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputFirstName" disabled="#{!UsersBean.editUser}" styleClass="input" value="#{UsersBean.firstName}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.lastName']}: " for="inputLastName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputLastName" disabled="#{!UsersBean.editUser}" styleClass="input" value="#{UsersBean.lastName}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.middleName']}: " for="inputMiddleName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputMiddleName" disabled="#{!UsersBean.editUser}" styleClass="input" value="#{UsersBean.middleName}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['users.role']}: " for="inputRoleList" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputRoleList" disabled="#{!UsersBean.editUser}" value="#{UsersBean.roleSelected}" styleClass="input">
    								<f:selectItems value="#{UsersBean.roleList_out}"/> 
    							</h:selectOneMenu>
							</td>
						</tr>
						<tr>
	    					<td>
	    						<h:outputLabel value="#{general['users.status']}: " styleClass="label-right" for="inputUserStatus"/>
	    					</td>
	    					<td>
	   	 						<h:selectOneMenu id="inputUserStatus" disabled="#{!UsersBean.editUser}" value="#{UsersBean.userStatusSelected}" styleClass="input">
	    							<f:selectItems value="#{UsersBean.userStatusList}"/>
	    						</h:selectOneMenu>
	    					</td>
    					</tr>
    					<tr>
							<td>
								<h:outputText value="#{general['users.registerDate']}: " styleClass="label-right"/>
							</td>
							<td>
								<h:outputText id="registerDateText" styleClass="label-right" value="#{UsersBean.registerDate}" />
							</td>
						</tr>
    					<tr>
    						<td>
	    						<p class="P-align-center">
				    				<f:subview rendered="#{UsersBean.editedUserNameAlreadyExists}" >
				    					<h:outputText value="#{general['users.errors.userNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{UsersBean.editedUserNameNotFound}" >
				    					<h:outputText value="#{general['users.errors.userNameNotFound']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{UsersBean.editedUserNameInvalid}" >
				    					<h:outputText value="#{general['users.errors.userNameInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{UsersBean.editedPasswordInvalid}" >
				    					<h:outputText value="#{general['users.errors.passwordInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{UsersBean.editedEmailInvalid}" >
				    					<h:outputText value="#{general['users.errors.emailInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    			</p>
			    			</td>
			    		</tr>
						<tr>
							<td colspan="2">
								<h:commandButton id="editButton" disabled="#{UsersBean.editUser}" value="#{general['users.edit.editButton']}" action="#{UsersBean.editUserButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								<h:outputText value=" "/>
								
								<f:subview rendered="#{UsersBean.editUser}">
									<h:commandButton id="saveButton" value="#{general['users.edit.saveButton']}" action="#{UsersBean.saveEditedUser}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
								
								<f:subview rendered="#{UsersBean.editUser}">
									<h:commandButton id="doNotSaveButton" value="#{general['users.edit.doNotSaveButton']}" action="#{UsersBean.doNotSaveEditedUser}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
							</td>
						</tr>
					</table>
				
    	    </ui:define>			
	    </ui:composition>
    </body>
</html>