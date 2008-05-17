<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">

			<ui:define name="title">
				#{general['versions.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['versions.title']}" styleClass="title"/>
				</p>
    			
    	    	<center>
    	    	<table>
    	    		<tr>
    	    			<td>
    	    				<h:outputLabel value="#{general['versions.existingProducts']}: " for="inputProductName" styleClass="label-right"/>
							<h:selectOneMenu id="inputProductName" disabled="#{VersionsBean.editOrAddProduct}" valueChangeListener="#{VersionsBean.valueChangeListener}" value="#{VersionsBean.productNameSelected}" onchange="submit();" styleClass="inputDefaultSizeLeft">
								<f:selectItems value="#{VersionsBean.productNameList_out}" />
							</h:selectOneMenu>
						</td>
						<td>
							<h:commandButton id="addButton" disabled="#{VersionsBean.editOrAddProduct}" value="#{general['versions.add.addButton']}" action="#{VersionsBean.addNewProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
						</td>
					</tr>
					<f:subview rendered="#{VersionsBean.addAProduct}">
						<tr>
							<td colspan="2" align="right">
									<h:commandButton id="saveNewProdButton" value="#{general['versions.add.saveButton']}" action="#{VersionsBean.addNewProductToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								
									<h:commandButton id="doNotSaveNewProdButton" value="#{general['versions.add.doNotSaveButton']}" action="#{VersionsBean.cancelAddNewProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
							</td>
						</tr>
					</f:subview>
				</table>
				</center>
				
				<f:subview rendered="#{!VersionsBean.addAProduct}">
					<table>
						<tr>								
							<td colspan="2">
								<h:commandButton id="editButton" disabled="#{VersionsBean.editProduct}" value="#{general['versions.edit.editButton']}" action="#{VersionsBean.editProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								<h:outputText value=" "/>
								
								<f:subview rendered="#{VersionsBean.editProduct}">
									<h:commandButton id="saveButton" value="#{general['versions.edit.saveButton']}" action="#{VersionsBean.saveEditedProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
								
								<f:subview rendered="#{VersionsBean.editProduct}">
									<h:commandButton id="doNotSaveButton" value="#{general['versions.edit.doNotSaveButton']}" action="#{VersionsBean.doNotSaveEditedProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['versions.name']}: " for="inputProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductName" disabled="#{!VersionsBean.editProduct}" styleClass="input" value="#{VersionsBean.name}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['versions.description']}: " for="inputProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputTextarea id="inputProductDescription" styleClass="inputTextArea" disabled="#{!VersionsBean.editProduct}" rows="3" cols="35" value="#{VersionsBean.description}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['versions.userAsssigned']}: " for="inputProductUserAssigned" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputProductUserAssigned" disabled="#{!VersionsBean.editProduct}" value="#{VersionsBean.userAsssignedSelected}" styleClass="inputDefaultSizeLeft">
									<f:selectItems value="#{VersionsBean.userNameList_out}" />
								</h:selectOneMenu>
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputText value="#{general['versions.parentComponentOfVersion']}: " styleClass="label-right"/>
							</td>
							<td>
								<h:outputText value="#{VersionsBean.parentComponentOfVersion}" />
							</td>
						</tr>
						<tr>
    						<td colspan="2">
	    						<p class="P-align-center">
				    				<f:subview rendered="#{VersionsBean.editedProductNameAlreadyExists}" >
				    					<h:outputText value="#{general['versions.errors.productNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{VersionsBean.editedProductNameInvalid}" >
				    					<h:outputText value="#{general['versions.errors.productNameInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{VersionsBean.editedProductNameNotFound}" >
				    					<h:outputText value="#{general['versions.errors.productNameNotFound']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    			</p>
			    			</td>
			    		</tr>
					</table>
				</f:subview>
				
				<f:subview rendered="#{VersionsBean.addAProduct}">
					<table>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['versions.name']}: " for="inputNewProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputNewProductName" styleClass="input" value="#{VersionsBean.newName}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['versions.description']}: " for="inputNewProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputTextarea id="inputNewProductDescription" styleClass="inputTextArea" rows="3" cols="35" value="#{VersionsBean.newDescription}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['versions.userAsssigned']}: " for="inputNewProductUserAssigned" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputNewProductUserAssigned" value="#{VersionsBean.newUserAsssignedSelected}" styleClass="inputDefaultSizeLeft">
									<f:selectItems value="#{VersionsBean.userNameList_out}" />
								</h:selectOneMenu>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<p class="P-align-center">
				    				<f:subview rendered="#{VersionsBean.newProductNameAlreadyExists}" >
				    					<h:outputText value="#{general['versions.errors.productNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{VersionsBean.newProductNameIsInvalid}" >
				    					<h:outputText value="#{general['versions.errors.productNameInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    			</p>
							</td>
						</tr>
					</table>
				</f:subview>
				
    	    </ui:define>			
	    </ui:composition>
    </body>
</html>