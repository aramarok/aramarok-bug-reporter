<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">

			<ui:define name="title">
				#{general['products.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['products.title']}" styleClass="title"/>
				</p>
    			
    			<h:outputText value="#{ProductsBean.loadData}" />
    			
    	    	<center>
    	    	<table>
    	    		<tr>
    	    			<td>
    	    				<h:outputLabel value="#{general['products.existingProducts']}: " for="inputProductName" styleClass="label-right"/>
							<h:selectOneMenu id="inputProductName" disabled="#{ProductsBean.editOrAddProduct}" valueChangeListener="#{ProductsBean.valueChangeListener}" value="#{ProductsBean.productNameSelected}" onchange="submit();" styleClass="inputDefaultSizeLeft">
								<f:selectItems value="#{ProductsBean.productNameList_out}" />
							</h:selectOneMenu>
						</td>
						<td>
							<h:commandButton id="addButton" disabled="#{ProductsBean.editOrAddProduct}" value="#{general['products.add.addButton']}" action="#{ProductsBean.addNewProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
						</td>
					</tr>
					<f:subview rendered="#{ProductsBean.addAProduct}">
						<tr>
							<td colspan="2" align="right">
									<h:commandButton id="saveNewProdButton" value="#{general['products.add.saveButton']}" action="#{ProductsBean.addNewProductToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								
									<h:commandButton id="doNotSaveNewProdButton" value="#{general['products.add.doNotSaveButton']}" action="#{ProductsBean.cancelAddNewProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
							</td>
						</tr>
					</f:subview>
				</table>
				</center>
				
				<f:subview rendered="#{!ProductsBean.addAProduct}">
					<table>
						<tr>								
							<td colspan="2">
								<h:commandButton id="editButton" disabled="#{ProductsBean.editProduct}" value="#{general['products.edit.editButton']}" action="#{ProductsBean.editProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								<h:outputText value=" "/>
								
								<f:subview rendered="#{ProductsBean.editProduct}">
									<h:commandButton id="saveButton" value="#{general['products.edit.saveButton']}" action="#{ProductsBean.saveEditedProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
								
								<f:subview rendered="#{ProductsBean.editProduct}">
									<h:commandButton id="doNotSaveButton" value="#{general['products.edit.doNotSaveButton']}" action="#{ProductsBean.doNotSaveEditedProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:outputText value=" "/>
								</f:subview>
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.name']}: " for="inputProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductName" disabled="#{!ProductsBean.editProduct}" styleClass="input" value="#{ProductsBean.name}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.description']}: " for="inputProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputTextarea id="inputProductDescription" styleClass="inputTextArea" disabled="#{!ProductsBean.editProduct}" rows="3" cols="35" value="#{ProductsBean.description}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.productURL']}: " for="inputProductURL" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductURL" disabled="#{!ProductsBean.editProduct}" styleClass="input" value="#{ProductsBean.productURL}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.closeForBugEntry']}: " for="closeForBugEntry" styleClass="label-right"/>
							</td>
							<td>
								<h:selectBooleanCheckbox disabled="#{!ProductsBean.editProduct}" id="closeForBugEntry" value="#{ProductsBean.closeForBugEntry}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.userAsssigned']}: " for="inputProductUserAssigned" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputProductUserAssigned" disabled="#{!ProductsBean.editProduct}" value="#{ProductsBean.userAsssignedSelected}" styleClass="inputDefaultSizeLeft">
									<f:selectItems value="#{ProductsBean.userNameList_out}" />
								</h:selectOneMenu>
							</td>
						</tr>
						<tr>
							<td colspan="2">
	    						<table>
	    							<tr>
	    								<td>
				    						<h:outputLabel value="#{general['products.productComponents']}:" styleClass="output" for="productSelectedComponents"  />
				    						<br />
				    						<h:selectOneListbox disabled="#{!ProductsBean.editProduct}" id="productSelectedComponents" value="#{ProductsBean.userSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ProductsBean.userList}"/>
					        				</h:selectOneListbox>
					        				<br />
					        				<div align="right">
					        					<h:commandButton rendered="#{ProductsBean.editProduct}" id="removeComponentButton" value="#{general['products.removeComponentButton']}"  action="#{ProductsBean.removeRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
					        				</div>
					        			</td>
					        			<td>
					        				<h:outputLabel value="#{general['products.availableComponents']}:" styleClass="output" for="availableSelectedComponents"/>
					        				<br/>
				    						<h:selectOneListbox disabled="#{!ProductsBean.editProduct}" id="availableSelectedComponents"  value="#{ProductsBean.systemSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ProductsBean.systemList}"/>
					        				</h:selectOneListbox>	
					        				<br />
					        				<h:commandButton rendered="#{ProductsBean.editProduct}" id="addComponentButton" value="#{general['products.addComponentButton']}"  action="#{ProductsBean.addRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
				    					</td>
	    							</tr>
	    						</table>
		    				</td>
						</tr>
						<tr>
    						<td colspan="2">
	    						<p class="P-align-center">
				    				<f:subview rendered="#{ProductsBean.editedProductNameAlreadyExists}" >
				    					<h:outputText value="#{general['products.errors.productNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{ProductsBean.editedProductNameInvalid}" >
				    					<h:outputText value="#{general['products.errors.productNameInvalid']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{ProductsBean.editedProductNameNotFound}" >
				    					<h:outputText value="#{general['products.errors.productNameNotFound']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    			</p>
			    			</td>
			    		</tr>
					</table>
				</f:subview>
				
				<f:subview rendered="#{ProductsBean.addAProduct}">
					<table>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.name']}: " for="inputNewProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputNewProductName" styleClass="input" value="#{ProductsBean.newName}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.description']}: " for="inputNewProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputTextarea id="inputNewProductDescription" styleClass="inputTextArea" rows="3" cols="35" value="#{ProductsBean.newDescription}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.productURL']}: " for="inputNewProductURL" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputNewProductURL" styleClass="input" value="#{ProductsBean.newProductURL}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.closeForBugEntry']}: " for="newCloseForBugEntry" styleClass="label-right"/>
							</td>
							<td>
								<h:selectBooleanCheckbox id="newCloseForBugEntry" value="#{ProductsBean.newCloseForBugEntry}" />
							</td>
						</tr>
						<tr>
							<td align="left">
								<h:outputLabel value="#{general['products.userAsssigned']}: " for="inputNewProductUserAssigned" styleClass="label-right"/>
							</td>
							<td>
								<h:selectOneMenu id="inputNewProductUserAssigned" value="#{ProductsBean.newUserAsssignedSelected}" styleClass="inputDefaultSizeLeft">
									<f:selectItems value="#{ProductsBean.userNameList_out}" />
								</h:selectOneMenu>
							</td>
						</tr>
						<tr>
							<td colspan="2">
	    						<table>
	    							<tr>
	    								<td>
				    						<h:outputLabel value="#{general['products.productComponents']}:" styleClass="output" for="productSelectedComponents"  />
				    						<br />
				    						<h:selectOneListbox id="productSelectedComponents" value="#{ProductsBean.newUserSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ProductsBean.newUserList}"/>
					        				</h:selectOneListbox>
					        				<br />
					        				<div align="right">
					        					<h:commandButton id="removeComponentButton" value="#{general['products.removeComponentButton']}"  action="#{ProductsBean.removeNewRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
					        				</div>
					        			</td>
					        			<td>
					        				<h:outputLabel value="#{general['products.availableComponents']}:" styleClass="output" for="availableSelectedComponents"/>
					        				<br/>
				    						<h:selectOneListbox id="availableSelectedComponents"  value="#{ProductsBean.newSystemSelectedRole}" size="5" styleClass="selectManyListBox180">
					        					<f:selectItems value="#{ProductsBean.newSystemList}"/>
					        				</h:selectOneListbox>	
					        				<br />
					        				<h:commandButton  id="addComponentButton" value="#{general['products.addComponentButton']}"  action="#{ProductsBean.addNewRole}" onclick="submit()" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'" />
				    					</td>
	    							</tr>
	    						</table>
		    				</td>
						</tr>
						<tr>
							<td colspan="2">
								<p class="P-align-center">
				    				<f:subview rendered="#{ProductsBean.newProductNameAlreadyExists}" >
				    					<h:outputText value="#{general['products.errors.productNameAlreadyExists']}" styleClass="error"/>
				    					<br />
				    				</f:subview>
				    				
				    				<f:subview rendered="#{ProductsBean.newProductNameIsInvalid}" >
				    					<h:outputText value="#{general['products.errors.productNameInvalid']}" styleClass="error"/>
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