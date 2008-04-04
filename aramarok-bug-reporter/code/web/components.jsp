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
    			
    	    	
    	    	<table>
    	    		<tr>
    	    			<td>
    	    				<h:outputLabel value="#{general['components.existingComponents']}: " for="inputComponentName" styleClass="label-right"/>
							<h:selectOneMenu id="inputComponentName" disabled="#{ComponentsBean.editProduct}" valueChangeListener="#{ComponentsBean.valueChangeListener}" value="#{ComponentsBean.productNameSelected}" onchange="submit();" styleClass="inputDefaultSizeLeft">
								<f:selectItems value="#{ComponentsBean.productNameList_out}" />
							</h:selectOneMenu>
						</td>
					</tr>					
				</table>
				
				<table>
						<tr>
							<td>
								<h:outputLabel value="#{general['components.name']}: " for="inputProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductName" disabled="#{!ComponentsBean.editProduct}" styleClass="input" value="#{ComponentsBean.name}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['components.description']}: " for="inputProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductDescription" disabled="#{!ComponentsBean.editProduct}" styleClass="input" value="#{ComponentsBean.description}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['components.components']}: " for="productComponentList" styleClass="label-right"/>
							</td>
							<td>
							
								<h:selectOneListbox id="productComponentList" disabled="#{!ComponentsBean.editProduct}" value="#{ComponentsBean.productComponentSelected}" size="5" styleClass="selectManyListBox">
    								<f:selectItems value="#{ComponentsBean.productComponents}"/> 
    							</h:selectOneListbox>
    							 
							</td>
						</tr>
						<f:subview rendered="#{ComponentsBean.editProduct}">
							<tr>
								<td>
								</td>
								<td>
									<h:commandButton rendered="false" id="togleAddProductComponentButton" disabled="#{ComponentsBean.addAProductComponent}" value="#{general['components.addProductComponent.togleAddButton']}" action="#{ComponentsBean.addAProductComponentToogleButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<f:subview rendered="#{ComponentsBean.addAProductComponent}" >
										<table>
											<tr>
				    							<td>
													<h:outputLabel value="#{general['components.addProductComponent.name']}: " for="inputProductComponentName" styleClass="label-right"/>
												</td>
												<td>
													<h:inputText id="inputProductComponentName" styleClass="input100" value="#{ComponentsBean.newProductComponentName}" />
												</td>
											</tr>
											<tr>
				    							<td>
													<h:outputLabel value="#{general['components.addProductComponent.description']}: " for="inputProductComponentDescription" styleClass="label-right"/>
												</td>
												<td>
													<h:inputText id="inputProductComponentDescription" styleClass="input100" value="#{ComponentsBean.newProductComponentDescription}" />
												</td>
											</tr>
											<tr>
												<td>
													<!-- ERROR MESSAGES FOR ADD NEW PRODUCT COMPONENT - start -->
													<f:subview rendered="#{ComponentsBean.newProductComponentnameAlreadyExists}" >
								    					<h:outputText value="#{general['components.addProductComponent.errors.productComponentNameAlreadyExists']}" styleClass="error"/>
								    					<br />
								    				</f:subview>
								    				<f:subview rendered="#{ComponentsBean.newProductComponentNameIsInvalid}" >
								    					<h:outputText value="#{general['components.addProductComponent.errors.productComponentNameInvalid']}" styleClass="error"/>
								    					<br />
								    				</f:subview>
								    				<!-- ERROR MESSAGES FOR ADD NEW PRODUCT COMPONENT - end -->
								    				
													<h:commandButton id="addProductComponentButton" value="#{general['components.addProductComponent.addButton']}" action="#{ComponentsBean.addAProductComponentToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
													<h:outputText value=" " />
													<h:commandButton id="cancelAddProductComponentButton" value="#{general['components.addProductComponent.cancelButton']}" action="#{ComponentsBean.cancelAddOfAProductComponent}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
													
												</td>
											</tr>
										</table>
					    			</f:subview>
								</td>
							</tr>
						</f:subview>
    					<tr>
    						<td>
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
					</table>
					
					<h:commandButton id="addButton" disabled="#{ComponentsBean.editProduct}" value="#{general['components.add.addButton']}" action="#{ComponentsBean.addNewProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
					
					<f:view rendered="#{ComponentsBean.addAProduct}">
						<table>
							<tr>
								<td>
								<f:subview rendered="#{ComponentsBean.addAProduct}">
									<h:outputLabel value="#{general['components.add.name']}: " styleClass="label-right"/>
								</f:subview>
								</td>
								<td>
								<f:subview rendered="#{ComponentsBean.addAProduct}">
									<h:inputText disabled="#{ComponentsBean.editProduct}" styleClass="input" value="#{ComponentsBean.newName}" />
								</f:subview>
								</td>
							</tr>
							<tr>
								<td>
								<f:subview rendered="#{ComponentsBean.addAProduct}">
									<h:outputLabel value="#{general['components.add.description']}: "  styleClass="label-right"/>
								</f:subview>
								</td>
								<td>
								<f:subview rendered="#{ComponentsBean.addAProduct}">
									<h:inputText  disabled="#{ComponentsBean.editProduct}" styleClass="input" value="#{ComponentsBean.newDescription}" />
								</f:subview>
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
							<tr>
								<td colspan="2">
									<f:subview rendered="#{ComponentsBean.addAProduct}">
										<h:commandButton disabled="#{ComponentsBean.editProduct}" id="saveNewProdButton" value="#{general['components.add.saveButton']}" action="#{ComponentsBean.addNewProductToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
										<h:outputText value=" "/>
									</f:subview>
									
									<f:subview rendered="#{ComponentsBean.addAProduct}">
										<h:commandButton disabled="#{ComponentsBean.editProduct}" id="doNotSaveNewProdButton" value="#{general['components.add.doNotSaveButton']}" action="#{ComponentsBean.cancelAddNewProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
										<h:outputText value=" "/>
									</f:subview>
								</td>
							</tr>
						</table>
					</f:view>
				
    	    </ui:define>			
	    </ui:composition>
    </body>
</html>