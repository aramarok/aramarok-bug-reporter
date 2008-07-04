<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['viewSavedFilters.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['viewSavedFilters.title']}" styleClass="title"/>
				</p>
			
				<br />
				<h:dataTable rendered="#{!ViewSavedFiltersBean.filteredBugListEmpty}" value="#{ViewSavedFiltersBean.filteredBugs}" var="bug" border="0"  columnClasses="output" rowClasses="table-light-row,table-dark-row" headerClass="table-th">
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.id']}" />
    					</f:facet>
    					<h:commandLink value="#{bug.id}" action="#{bug.viewBug}" rendered="true" />
    				</h:column>
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.summary']}" />
    					</f:facet>
    					<h:outputText value="#{bug.summary}" styleClass="label-right" />
    				</h:column>
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.openDate']}" />
    					</f:facet>
    					<h:outputText value="#{bug.openDate}" styleClass="label-right" />
    				</h:column>
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.priority']}" />
    					</f:facet>
    					<h:outputText value="#{bug.priority}" styleClass="label-right" />
    				</h:column>
    			
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.severity']}" />
    					</f:facet>
    					<h:outputText value="#{bug.severity}" styleClass="label-right" />
    				</h:column>
    			
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.platform']}" />
    					</f:facet>
    					<h:outputText value="#{bug.platform}" styleClass="label-right" />
    				</h:column>
    					
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.operatingSystem']}" />
    					</f:facet>
    					<h:outputText value="#{bug.operatingSystem}" styleClass="label-right" />
    				</h:column>
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.bugStatus']}" />
    					</f:facet>
    					<h:outputText value="#{bug.bugStatus}" styleClass="label-right" />
    				</h:column>
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.reporter']}" />
    					</f:facet>
    					<h:outputText value="#{bug.reporter}" styleClass="label-right" />
    				</h:column>
    				
    				
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewSavedFilters.table.assignedTo']}" />
    					</f:facet>
    					<h:outputText value="#{bug.assignedTo}" styleClass="label-right" />
    				</h:column>
				</h:dataTable>
	   			
	   			<f:subview rendered="#{ViewSavedFiltersBean.filteredBugListEmpty}" >
   					<h:outputText value="#{general['viewSavedFilters.error.filteredBugListEmpty']}" styleClass="error"/>
   				</f:subview>
	   			
			</ui:define>
		</ui:composition>
	</body>
</html>