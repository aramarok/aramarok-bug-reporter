<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">

			<ui:define name="title">
				#{general['components.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['components.title']}" styleClass="title"/>
				</p>
    			
    			<h:outputText value="#{ComponentsBean.loadData}" />
    			
    	    	<center>
    	    	<table>
    	    		<tr>
    	    			<td>
    	    				<h:outputLabel value="#{general['components.existingProducts']}: " for="inputProductName" styleClass="label-right"/>
							<h:selectOneMenu id="inputProductName" disabled="#{ComponentsBean.editOrAddProduct}" valueChangeListener="#{ComponentsBean.valueChangeListener}" value="#{ComponentsBean.productNameSelected}" onchange="submit();" styleClass="inputDefaultSizeLeft">
								<f:selectItems value="#{ComponentsBean.productNameList_out}" />
							</h:selectOneMenu>
						</td>
						<td>
							<h:commandButton id="addButton" disabled="#{ComponentsBean.editOrAddProduct}" value="#{general['components.add.addButton']}" action="#{ComponentsBean.addNewProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
						</td>
					</tr>
					<f:subview rendered="#{ComponentsBean.addAProduct}">
						<tr>
							<td colspan="2" align="right">
									<h:commandButton id="saveNewProdButton" value="#{general['components.add.saveButton']}" action="#{ComponentsBean.addNewProductToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								
									<h:commandButton id="doNotSaveNewProdButton" value="#{general['components.add.doNotSaveButton']}" action="#{ComponentsBean.cancelAddNewProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
							</td>
						</tr>
					</f:subview>
				</table>
				</center>
				
				<f:subview rendered="#{!ComponentsBean.addAProduct}">
					<table>
						<tr>								
							<td colspan="2">
								<h:commandButton id="editButton" disabled="#{ComponentsBean.editProduct}" value="#{general['components.edit.editButton']}" action="#{ComponentsBean.editProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								<h:outputText value=" "/>
								
								<f:subview rendered="#{ComponentsBean.editProduct}">
									<h:commandButton id="saveButton" value="#{general['components.edit.saveButton']}" action="#{ComponentsBean.saveEditedProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
								
								<f:subview rendered="#{ComponentsBean.editProduct}">
									<h:commandButton id="doNotSaveButton" value="#{general['components.edit.doNotSaveButton']}" action="#{ComponentsBean.doNotSaveEditedProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['components.name']}: " for="inputProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductName" disabled="#{!ComponentsBean.editProduct}" styleClass="input" value="#{ComponentsBean.name}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['components.description']}: " for="inputProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputTextarea id="inputProductDescription" styleClass="inputTextArea" disabled="#{!ComponentsBean.editProduct}" rows="3" cols="35" value="#{ComponentsBean.description}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['components.userAsssigned']}: " for="inputProductUserAssigned" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputProductUserAssigned" disabled="#{!ComponentsBean.editProduct}" value="#{ComponentsBean.userAsssignedSelected}" styleClass="inputDefaultSizeLeft">
									<f:selectItems value="#{ComponentsBean.userNameList_out}" />
								</h:selectOneMenu>
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputText value="#{general['components.parentProductOfComponent']}: " styleClass="label-right"/>
							</td>
							<td>
								<h:outputText value="#{ComponentsBean.parentProductOfComponent}" styleClass="label-right" />
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
	    						<table>
	    							<tr>
	    								<td>
				    						<h:outputLabel value="#{general['components.productComponents']}:" styleClass="output" for="productSelectedComponents"  />
				    						<br />
				    						<h:selectOneListbox disabled="#{!ComponentsBean.editProduct}" id="productSelectedComponents" value="#{ComponentsBean.userSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ComponentsBean.userList}"/>
					        				</h:selectOneListbox>
					        				<br />
					        				<div align="right">
					        					<h:commandButton rendered="#{ComponentsBean.editProduct}" id="removeComponentButton" value="#{general['components.removeComponentButton']}"  action="#{ComponentsBean.removeRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
					        				</div>
					        			</td>
					        			<td>
					        				<h:outputLabel value="#{general['components.availableComponents']}:" styleClass="output" for="availableSelectedComponents"/>
					        				<br/>
				    						<h:selectOneListbox disabled="#{!ComponentsBean.editProduct}" id="availableSelectedComponents"  value="#{ComponentsBean.systemSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ComponentsBean.systemList}"/>
					        				</h:selectOneListbox>	
					        				<br />
					        				<h:commandButton rendered="#{ComponentsBean.editProduct}" id="addComponentButton" value="#{general['components.addComponentButton']}"  action="#{ComponentsBean.addRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
				    					</td>
	    							</tr>
	    						</table>
		    				</td>
						</tr>
						<tr>
    						<td colspan="2">
	    						<p class="P-align-center">
				    				<f:subview rendered="#{ComponentsBean.editedProductNameAlreadyExists}" >
				    					<h:outputText value="#{general['components.errors.productNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{ComponentsBean.editedProductNameInvalid}" >
				    					<h:outputText value="#{general['components.errors.productNameInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{ComponentsBean.editedProductNameNotFound}" >
				    					<h:outputText value="#{general['components.errors.productNameNotFound']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    			</p>
			    			</td>
			    		</tr>
					</table>
				</f:subview>
				
				<f:subview rendered="#{ComponentsBean.addAProduct}">
					<table>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['components.name']}: " for="inputNewProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputNewProductName" styleClass="input" value="#{ComponentsBean.newName}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['components.description']}: " for="inputNewProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputTextarea id="inputNewProductDescription" styleClass="inputTextArea" rows="3" cols="35" value="#{ComponentsBean.newDescription}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['components.userAsssigned']}: " for="inputNewProductUserAssigned" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputNewProductUserAssigned" value="#{ComponentsBean.newUserAsssignedSelected}" styleClass="inputDefaultSizeLeft">
									<f:selectItems value="#{ComponentsBean.userNameList_out}" />
								</h:selectOneMenu>
							</td>
						</tr>
						<tr>
							<td colspan="2">
	    						<table>
	    							<tr>
	    								<td>
				    						<h:outputLabel value="#{general['components.productComponents']}:" styleClass="output" for="productSelectedComponents"  />
				    						<br />
				    						<h:selectOneListbox id="productSelectedComponents" value="#{ComponentsBean.newUserSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ComponentsBean.newUserList}"/>
					        				</h:selectOneListbox>
					        				<br />
					        				<div align="right">
					        					<h:commandButton id="removeComponentButton" value="#{general['components.removeComponentButton']}"  action="#{ComponentsBean.removeNewRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
					        				</div>
					        			</td>
					        			<td>
					        				<h:outputLabel value="#{general['components.availableComponents']}:" styleClass="output" for="availableSelectedComponents"/>
					        				<br/>
				    						<h:selectOneListbox id="availableSelectedComponents"  value="#{ComponentsBean.newSystemSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ComponentsBean.newSystemList}"/>
					        				</h:selectOneListbox>	
					        				<br />
					        				<h:commandButton  id="addComponentButton" value="#{general['components.addComponentButton']}"  action="#{ComponentsBean.addNewRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
				    					</td>
	    							</tr>
	    						</table>
		    				</td>
						</tr>
						<tr>
							<td colspan="2">
								<p class="P-align-center">
				    				<f:subview rendered="#{ComponentsBean.newProductNameAlreadyExists}" >
				    					<h:outputText value="#{general['components.errors.productNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{ComponentsBean.newProductNameIsInvalid}" >
				    					<h:outputText value="#{general['components.errors.productNameInvalid']}" styleClass="error"/>
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