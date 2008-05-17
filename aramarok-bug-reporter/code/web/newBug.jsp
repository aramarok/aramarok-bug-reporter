<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['newBug.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['newBug.title']}" styleClass="title"/>
				</p>
				
				<h:outputText value="#{general['newBug.selectProduct']}: " styleClass="label-right"/>
				
				<br />
				<br />
				
    			<h:dataTable rendered="#{!NewBugBean.productListEmpty}" value="#{NewBugBean.productsList}" var="product" border="0"  columnClasses="output" rowClasses="table-light-row,table-dark-row" headerClass="table-th">
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['newBug.table.name']}" />
    					</f:facet>
    					<h:commandLink value="#{product.productName}" action="#{product.enterNewBug}" />
    				</h:column>
    				
    				<h:column rendered="#{product.URLLink}">
    					<f:facet name="header">
    						<h:outputText value="#{general['newBug.table.URL']}" />
    					</f:facet>
    					<a href="#{product.productURL}" target="_blank">
    						<h:outputText value="#{product.productURL}" styleClass="label-right" />
    					</a>
    				</h:column>
    				
    				<h:column rendered="#{!product.URLLink}">
    					<f:facet name="header">
    						<h:outputText value="#{general['newBug.table.URL']}" />
    					</f:facet>
    					<h:outputText value="#{product.productURL}" styleClass="label-right" />
    				</h:column>
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['newBug.table.description']}" />
    					</f:facet>
    					<h:outputText value="#{product.productDescription}" styleClass="label-right" />
    				</h:column>
				</h:dataTable>
				
			</ui:define>
		</ui:composition>
	</body>
</html>