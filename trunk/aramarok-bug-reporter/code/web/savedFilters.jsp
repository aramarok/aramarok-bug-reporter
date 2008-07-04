<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['savedFilters.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['savedFilters.title']}" styleClass="title"/>
				</p>
			
				<br />
				<h:dataTable rendered="#{!SavedFiltersBean.savedFilterListEmpty}" value="#{SavedFiltersBean.savedFilters}" var="filters" border="0"  columnClasses="output" rowClasses="table-light-row,table-dark-row" headerClass="table-th">
	   				<h:column>
	   					<f:facet name="header">
	   						<h:outputText value="#{general['savedFilters.table.savedFilterName']}" />
	   					</f:facet>
	   					<h:commandLink value="#{filters.name}" action="#{filters.viewFilter}" rendered="true" /> 
	   					<h:outputText value="   " />
	   					<h:commandLink value="(#{general['savedFilters.table.deleteSavedFilterName']})" action="#{filters.deleteFilter}" rendered="true" />
	   				</h:column>
	   			</h:dataTable>
	   			
	   			<f:subview rendered="#{SavedFiltersBean.savedFilterListEmpty}" >
   					<h:outputText value="#{general['savedFilters.error.noSavedFilters']}" styleClass="error"/>
   				</f:subview>
	   			
			</ui:define>
		</ui:composition>
	</body>
</html>