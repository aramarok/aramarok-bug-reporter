<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['viewBugActivity.title']}
			</ui:define>
			
			<ui:define name="body">
			
				<p class="P-align-center">
					<h:outputText value="#{general['viewBugActivity.title']}" styleClass="title"/>
				</p>
				<br />
				<f:subview rendered="#{ViewBugActivityBean.bugOk}">
					<h:outputText value="#{general['viewBugActivity.bugId']}: " rendered="true"  styleClass="label-right"/>
					<h:commandLink value="#{ViewBugActivityBean.bugId}" action="#{ViewBugActivityBean.viewBug}" rendered="true"/>
				</f:subview>
				<br />
				<br />
				
				<h:dataTable rendered="#{ViewBugActivityBean.bugOk}" value="#{ViewBugActivityBean.bugActivityList}" var="activity" border="0"  columnClasses="output" rowClasses="table-light-row,table-dark-row" headerClass="table-th">
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewBugActivity.table.who']}" />
    					</f:facet>
    					<h:outputText value="#{activity.who}" styleClass="label-center" />
    				</h:column>
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewBugActivity.table.actionDate']}" />
    					</f:facet>
    					<h:outputText value="#{activity.actionDate}" styleClass="label-center" />
    				</h:column>
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewBugActivity.table.what']}" />
    					</f:facet>
    					<h:outputText value="#{activity.what}" styleClass="label-center" />
    				</h:column>
    				<h:column>
    					<f:facet name="header">
    						<h:outputText value="#{general['viewBugActivity.table.bugStatus']}" />
    					</f:facet>
    						<h:outputText value="#{activity.bugStatus}" styleClass="label-center" />
    				</h:column>
				</h:dataTable>
			</ui:define>
		</ui:composition>
	</body>
</html>