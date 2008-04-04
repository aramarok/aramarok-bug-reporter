<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['newBug.title']}
			</ui:define>
			
			<ui:define name="body">
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