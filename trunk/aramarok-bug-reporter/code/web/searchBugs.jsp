<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['searchBugs.title']}
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['searchBugs.title']}" styleClass="title"/>
				</p>
				
				<h:outputText value="#{SearchBugsBean.loadData}" rendered="true" />
					<table>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.bugStatus']}: " styleClass="label2"/>
							</td>
							<td>
								<t:selectManyCheckbox layout="lineDirection" value="#{SearchBugsBean.selectedBugStatuses}" styleClass="label2">
									<f:selectItems  value="#{SearchBugsBean.bugStatuses}"/>
								</t:selectManyCheckbox>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.assignedTo']}: " styleClass="label2"/>
							</td>
							<td>
			    				<h:selectOneMenu id="assignedTo" value="#{SearchBugsBean.assignedToSelected}" styleClass="input">
			    					<f:selectItems value="#{SearchBugsBean.assignedToList_out}"/> 
			    				</h:selectOneMenu>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.reportedBy']}: " styleClass="label2"/>
							</td>
							<td>
			    				<h:selectOneMenu id="reportedBy" value="#{SearchBugsBean.reportedBySelected}" styleClass="input">
			    					<f:selectItems value="#{SearchBugsBean.reportedByList_out}"/> 
			    				</h:selectOneMenu>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.priority']}: " styleClass="label2"/>
							</td>
							<td>
								<t:selectManyCheckbox layout="lineDirection" value="#{SearchBugsBean.selectedPriorities}" styleClass="label2">
									<f:selectItems  value="#{SearchBugsBean.priorities}"/>
								</t:selectManyCheckbox>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.severity']}: " styleClass="label2"/>
							</td>
							<td>
								<t:selectManyCheckbox layout="lineDirection" value="#{SearchBugsBean.selectedSeverities}" styleClass="label2">
									<f:selectItems  value="#{SearchBugsBean.severities}"/>
								</t:selectManyCheckbox>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.operatingSystem']}: " styleClass="label2"/>
							</td>
							<td >
								<h:selectOneMenu id="operatingSystems" value="#{SearchBugsBean.operatingSystemSelected}" styleClass="input">
			    					<f:selectItems value="#{SearchBugsBean.operatingSystemsList_out}"/> 
			    				</h:selectOneMenu>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:outputText value="#{general['searchBugs.platform']}: " styleClass="label2"/>
							</td>
							<td>
								<t:selectManyCheckbox layout="lineDirection" value="#{SearchBugsBean.selectedPlatforms}" styleClass="label2">
									<f:selectItems  value="#{SearchBugsBean.platforms}"/>
								</t:selectManyCheckbox>
			    			</td>
						</tr>
						<tr>
							<td align="right">
								<h:commandButton id="searchButton" value="#{general['searchBugs.searchButton']}" action="#{SearchBugsBean.searchBugs}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
							</td>
							<td>
								<f:subview rendered ="true">
									<h:commandButton id="saveFilterButton" value="#{general['searchBugs.saveFilter']}" action="#{SearchBugsBean.saveSearchBugFilter}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									<h:inputText value="#{SearchBugsBean.filterName}" styleClass="input"/>
									<br />
									<f:subview rendered="#{SearchBugsBean.filterNameEmpty}" >
				    					<h:outputText value="#{general['searchBugs.error.specifyANameForFilter']}" styleClass="error"/>
				    				</f:subview>
				    				<f:subview rendered="#{SearchBugsBean.nameOfTheSearchAlreadyExists}" >
				    					<h:outputText value="#{general['searchBugs.error.nameOfTheSearchAlreadyExists']}" styleClass="error"/>
				    				</f:subview>
				    			</f:subview>
							</td>
						</tr>
					</table>
					
					<br />
					<br />
					
					<h:dataTable rendered="#{!SearchBugsBean.filteredBugListEmpty}" value="#{SearchBugsBean.filteredBugs}" var="bug" border="0"  columnClasses="output" rowClasses="table-light-row,table-dark-row" headerClass="table-th">
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.id']}" />
	    					</f:facet>
	    					<h:commandLink value="#{bug.id}" action="#{bug.viewBug}" rendered="true" />
	    				</h:column>
	    				
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.summary']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.summary}" styleClass="label-right" />
	    				</h:column>
	    				
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.openDate']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.openDate}" styleClass="label-right" />
	    				</h:column>
	    				
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.priority']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.priority}" styleClass="label-right" />
	    				</h:column>
	    			
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.severity']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.severity}" styleClass="label-right" />
	    				</h:column>
	    			
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.platform']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.platform}" styleClass="label-right" />
	    				</h:column>
	    					
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.operatingSystem']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.operatingSystem}" styleClass="label-right" />
	    				</h:column>
	    				
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.bugStatus']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.bugStatus}" styleClass="label-right" />
	    				</h:column>
	    				
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.reporter']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.reporter}" styleClass="label-right" />
	    				</h:column>
	    				
	    				
	    				<h:column>
	    					<f:facet name="header">
	    						<h:outputText value="#{general['searchBugs.table.assignedTo']}" />
	    					</f:facet>
	    					<h:outputText value="#{bug.assignedTo}" styleClass="label-right" />
	    				</h:column>
					</h:dataTable>
					
			</ui:define>
		</ui:composition>
	</body>
</html>