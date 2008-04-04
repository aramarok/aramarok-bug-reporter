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
    			
    	    	
    	    	<table>
    	    		<tr>
    	    			<td>
    	    				<h:outputLabel value="#{general['products.existingProducts']}: " for="inputProductName" styleClass="label-right"/>
							<h:selectOneMenu id="inputProductName" disabled="#{ProductsBean.editProduct}" valueChangeListener="#{ProductsBean.valueChangeListener}" value="#{ProductsBean.productNameSelected}" onchange="submit();" styleClass="inputDefaultSizeLeft">
								<f:selectItems value="#{ProductsBean.productNameList_out}" />
							</h:selectOneMenu>
						</td>
					</tr>					
				</table>
				
				<table>
						<tr>
							<td>
								<h:outputLabel value="#{general['products.name']}: " for="inputProductName" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductName" disabled="#{!ProductsBean.editProduct}" styleClass="input" value="#{ProductsBean.name}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['products.description']}: " for="inputProductDescription" styleClass="label-right"/>
							</td>
							<td>
								<h:inputText id="inputProductDescription" disabled="#{!ProductsBean.editProduct}" styleClass="input" value="#{ProductsBean.description}" />
							</td>
						</tr>
						<tr>
							<td>
								<h:outputLabel value="#{general['products.components']}: " for="productComponentList" styleClass="label-right"/>
							</td>
							<td>
							
								<h:selectOneListbox id="productComponentList" disabled="#{!ProductsBean.editProduct}" value="#{ProductsBean.productComponentSelected}" size="5" styleClass="selectManyListBox">
    								<f:selectItems value="#{ProductsBean.productComponents}"/> 
    							</h:selectOneListbox>
    							 
							</td>
						</tr>
						<f:subview rendered="#{ProductsBean.editProduct}">
							<tr>
								<td>
								</td>
								<td>
									<h:commandButton rendered="false" id="togleAddProductComponentButton" disabled="#{ProductsBean.addAProductComponent}" value="#{general['products.addProductComponent.togleAddButton']}" action="#{ProductsBean.addAProductComponentToogleButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<f:subview rendered="#{ProductsBean.addAProductComponent}" >
										<table>
											<tr>
				    							<td>
													<h:outputLabel value="#{general['products.addProductComponent.name']}: " for="inputProductComponentName" styleClass="label-right"/>
												</td>
												<td>
													<h:inputText id="inputProductComponentName" styleClass="input100" value="#{ProductsBean.newProductComponentName}" />
												</td>
											</tr>
											<tr>
				    							<td>
													<h:outputLabel value="#{general['products.addProductComponent.description']}: " for="inputProductComponentDescription" styleClass="label-right"/>
												</td>
												<td>
													<h:inputText id="inputProductComponentDescription" styleClass="input100" value="#{ProductsBean.newProductComponentDescription}" />
												</td>
											</tr>
											<tr>
												<td>
													<!-- ERROR MESSAGES FOR ADD NEW PRODUCT COMPONENT - start -->
													<f:subview rendered="#{ProductsBean.newProductComponentnameAlreadyExists}" >
								    					<h:outputText value="#{general['products.addProductComponent.errors.productComponentNameAlreadyExists']}" styleClass="error"/>
								    					<br />
								    				</f:subview>
								    				<f:subview rendered="#{ProductsBean.newProductComponentNameIsInvalid}" >
								    					<h:outputText value="#{general['products.addProductComponent.errors.productComponentNameInvalid']}" styleClass="error"/>
								    					<br />
								    				</f:subview>
								    				<!-- ERROR MESSAGES FOR ADD NEW PRODUCT COMPONENT - end -->
								    				
													<h:commandButton id="addProductComponentButton" value="#{general['products.addProductComponent.addButton']}" action="#{ProductsBean.addAProductComponentToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
													<h:outputText value=" " />
													<h:commandButton id="cancelAddProductComponentButton" value="#{general['products.addProductComponent.cancelButton']}" action="#{ProductsBean.cancelAddOfAProductComponent}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
													
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
					</table>
					
					<h:commandButton id="addButton" disabled="#{ProductsBean.editProduct}" value="#{general['products.add.addButton']}" action="#{ProductsBean.addNewProductButton}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
					
					<f:view rendered="#{ProductsBean.addAProduct}">
						<table>
							<tr>
								<td>
								<f:subview rendered="#{ProductsBean.addAProduct}">
									<h:outputLabel value="#{general['products.add.name']}: " styleClass="label-right"/>
								</f:subview>
								</td>
								<td>
								<f:subview rendered="#{ProductsBean.addAProduct}">
									<h:inputText disabled="#{ProductsBean.editProduct}" styleClass="input" value="#{ProductsBean.newName}" />
								</f:subview>
								</td>
							</tr>
							<tr>
								<td>
								<f:subview rendered="#{ProductsBean.addAProduct}">
									<h:outputLabel value="#{general['products.add.description']}: "  styleClass="label-right"/>
								</f:subview>
								</td>
								<td>
								<f:subview rendered="#{ProductsBean.addAProduct}">
									<h:inputText  disabled="#{ProductsBean.editProduct}" styleClass="input" value="#{ProductsBean.newDescription}" />
								</f:subview>
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
							<tr>
								<td colspan="2">
									<f:subview rendered="#{ProductsBean.addAProduct}">
										<h:commandButton disabled="#{ProductsBean.editProduct}" id="saveNewProdButton" value="#{general['products.add.saveButton']}" action="#{ProductsBean.addNewProductToDB}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
										<h:outputText value=" "/>
									</f:subview>
									
									<f:subview rendered="#{ProductsBean.addAProduct}">
										<h:commandButton disabled="#{ProductsBean.editProduct}" id="doNotSaveNewProdButton" value="#{general['products.add.doNotSaveButton']}" action="#{ProductsBean.cancelAddNewProduct}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
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