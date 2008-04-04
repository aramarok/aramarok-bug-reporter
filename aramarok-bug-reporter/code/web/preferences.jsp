<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['preferences.title']}
			</ui:define>
			
			<ui:define name="body">
				<h:outputText value="#{PreferencesBean.loadData}" />
				<table>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.userName']}: " for="inputUserName" styleClass="label-right"/>
						</td>
						<td>
							<h:inputText id="inputUserName" disabled="true" styleClass="input" value="#{PreferencesBean.userName}" />
						</td>
					</tr>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.password']}: " for="inputPassword" styleClass="label-right"/>
						</td>
						<td>
							<h:inputSecret id="inputPassword" disabled="#{!PreferencesBean.editUser}" styleClass="input" value="#{PreferencesBean.password}" />
						</td>
					</tr>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.emailAddress']}: " for="inputEmailAddress" styleClass="label-right"/>
						</td>
						<td>
							<h:inputText id="inputEmailAddress" disabled="#{!PreferencesBean.editUser}" styleClass="input" value="#{PreferencesBean.emailAddress}" />
						</td>
					</tr>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.firstName']}: " for="inputFirstName" styleClass="label-right"/>
						</td>
						<td>
							<h:inputText id="inputFirstName" disabled="#{!PreferencesBean.editUser}" styleClass="input" value="#{PreferencesBean.firstName}" />
						</td>
					</tr>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.lastName']}: " for="inputLastName" styleClass="label-right"/>
						</td>
						<td>
							<h:inputText id="inputLastName" disabled="#{!PreferencesBean.editUser}" styleClass="input" value="#{PreferencesBean.lastName}" />
						</td>
					</tr>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.middleName']}: " for="inputMiddleName" styleClass="label-right"/>
						</td>
						<td>
							<h:inputText id="inputMiddleName" disabled="#{!PreferencesBean.editUser}" styleClass="input" value="#{PreferencesBean.middleName}" />
						</td>
					</tr>
					<tr>
						<td>
							<h:outputLabel value="#{general['preferences.role']}: " for="inputRoleList" styleClass="label-right"/>
						</td>
						<td>
							<h:selectOneMenu id="inputRoleList" disabled="true" value="#{PreferencesBean.roleSelected}" styleClass="input">
   								<f:selectItems value="#{PreferencesBean.roleList_out}"/> 
   							</h:selectOneMenu>
						</td>
					</tr>
   					<tr>
						<td>
							<h:outputText value="#{general['preferences.registerDate']}: " styleClass="label-right"/>
						</td>
						<td>
							<h:outputText id="registerDateText" styleClass="label-right" value="#{PreferencesBean.registerDate}" />
						</td>
					</tr>
   					<tr>
   						<td>
    						<p class="P-align-center">			    				
			    				<f:subview rendered="#{PreferencesBean.editedUserNameNotFound}" >
			    					<h:outputText value="#{general['preferences.errors.userNameNotFound']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    				
			    				<f:subview rendered="#{PreferencesBean.editedEmailInvalid}" >
			    					<h:outputText value="#{general['preferences.errors.emailInvalid']}" styleClass="error"/>
			    					<br />
			    				</f:subview>
			    			</p>
		    			</td>
		    		</tr>
					<tr>
						<td colspan="2">
							<h:commandButton id="editButton" disabled="#{PreferencesBean.editUser}" value="#{general['preferences.edit.editButton']}" action="#{PreferencesBean.editUserButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
							<h:outputText value=" "/>
							
							<f:subview rendered="#{PreferencesBean.editUser}">
								<h:commandButton id="saveButton" value="#{general['preferences.edit.saveButton']}" action="#{PreferencesBean.saveEditedUser}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								<h:outputText value=" "/>
							</f:subview>
							
							<f:subview rendered="#{PreferencesBean.editUser}">
								<h:commandButton id="doNotSaveButton" value="#{general['preferences.edit.doNotSaveButton']}" action="#{PreferencesBean.doNotSaveEditedUser}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								<h:outputText value=" "/>
							</f:subview>
						</td>
					</tr>
				</table>
			</ui:define>
		</ui:composition>
	</body>
</html>