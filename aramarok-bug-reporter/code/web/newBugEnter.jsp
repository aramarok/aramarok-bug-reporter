<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
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
				
				<h:outputText value="#{NewBugEnterBean.loadData}" rendered="true" />
				
				<br />
				<br />
				
				<table>
					<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.reporter']}: " styleClass="label2"/>
						</td>
						<td>
							<h:outputText value="#{NewBugEnterBean.reporterUserName}" styleClass="label-right"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.product']}: " styleClass="label2"/>
						</td>
						<td>
							<h:outputText value="#{NewBugEnterBean.productName}" styleClass="label-right"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.component']}: " styleClass="label2"/>
						</td>
						<td>
							<h:selectOneMenu id="inputProductComponentList" value="#{NewBugEnterBean.productComponent}" onchange="submit();" styleClass="input">
		    					<f:selectItems value="#{NewBugEnterBean.productComponentList_out}"/> 
		    				</h:selectOneMenu>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td align="right">
							<h:outputText value="#{general['newBugEnter.version']}: " styleClass="label2"/>
						</td>
						<td>
							<h:selectOneMenu id="inputComponentVersionList" value="#{NewBugEnterBean.componentVersion}" onchange="submit();" styleClass="input">
		    					<f:selectItems value="#{NewBugEnterBean.componentVersionList_out}"/> 
		    				</h:selectOneMenu>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td align="right">
							<h:outputText value="#{general['newBugEnter.operatingSystem']}: " styleClass="label2"/>
						</td>
						<td>
							<h:selectOneMenu id="inputOperatingSystemList" value="#{NewBugEnterBean.operatingSystem}" styleClass="input">
		    					<f:selectItems value="#{NewBugEnterBean.operatingSystemList_out}"/> 
		    				</h:selectOneMenu>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td align="right">
		    				<h:outputText value="#{general['newBugEnter.platform']}: " styleClass="label2"/>
		    			</td>
		    			<td>
		    				<h:selectOneMenu id="inputPlatformList" value="#{NewBugEnterBean.platform}" styleClass="input">
		    					<f:selectItems value="#{NewBugEnterBean.platformList_out}"/> 
		    				</h:selectOneMenu>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td align="right">
		    				<h:outputText value="#{general['newBugEnter.priority']}: " styleClass="label2"/>
		    			</td>
		    			<td>
		    				<h:selectOneMenu id="inputPriorityList" value="#{NewBugEnterBean.priority}" styleClass="input">
		    					<f:selectItems value="#{NewBugEnterBean.priorityList_out}"/> 
		    				</h:selectOneMenu>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td align="right">
		    				<h:outputText value="#{general['newBugEnter.severity']}: " styleClass="label2"/>
		    			</td>
		    			<td>
		    				<h:selectOneMenu id="inputSeverityList" value="#{NewBugEnterBean.severity}" styleClass="input">
		    					<f:selectItems value="#{NewBugEnterBean.severityList_out}"/> 
		    				</h:selectOneMenu>
		    			</td>
		    		</tr>
		    		<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.dateObserved']}: " styleClass="label2"/>
						</td>
						<td>
							<t:inputCalendar id="inputObservedDate" value="#{NewBugEnterBean.dateObserved}" renderAsPopup="true" renderPopupButtonAsImage="true" popupDateFormat="#{NewBugEnterBean.dateFormat}" styleClass="medium-input"/>
						</td>
					</tr>
		    		<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.bugStatus']}: " styleClass="label2"/>
						</td>
						<td>
							<h:outputText value="#{NewBugEnterBean.bugState}" styleClass="label-right"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.assignedTo']}: " styleClass="label2"/>
						</td>
						<td>
							<h:outputText value="#{NewBugEnterBean.assignedTo}" styleClass="label-right"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.bugSummary']}: " styleClass="label2"/>
						</td>
						<td>
							<h:inputText value="#{NewBugEnterBean.summary}" styleClass="input"/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<h:outputText value="#{general['newBugEnter.bugDescription']}: " styleClass="label2"/>
						</td>
						<td>
							<h:inputTextarea value="#{NewBugEnterBean.description}" styleClass="inputTextArea"/>
						</td>
					</tr>
				</table>				
				<p class="P-align-center">
    				<f:subview rendered="#{NewBugEnterBean.componentNotSelected}" >
    					<h:outputText value="#{general['newBugEnter.errors.componentNotSelected']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.versionNotSelected}" >
    					<h:outputText value="#{general['newBugEnter.errors.versionNotSelected']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.operatingSystemNotSelected}" >
    					<h:outputText value="#{general['newBugEnter.errors.operatingSystemNotSelected']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.platformNotSelected}" >
    					<h:outputText value="#{general['newBugEnter.errors.platformNotSelected']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.priorityNotSelected}" >
    					<h:outputText value="#{general['newBugEnter.errors.priorityNotSelected']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.severityNotSelected}" >
    					<h:outputText value="#{general['newBugEnter.errors.severityNotSelected']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.summaryNotEntered}" >
    					<h:outputText value="#{general['newBugEnter.errors.summaryNotEntered']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.descriptionNotEntered}" >
    					<h:outputText value="#{general['newBugEnter.errors.descriptionNotEntered']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.userHasNoRight}" >
    					<h:outputText value="#{general['newBugEnter.errors.userHasNoRight']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.cannotChangeBugStatus}" >
    					<h:outputText value="#{general['newBugEnter.errors.cannotChangeBugStatus']}" styleClass="error"/>
    					<br />
    				</f:subview>
    				<f:subview rendered="#{NewBugEnterBean.componentVersionHasNoSolverAssigned}" >
    					<h:outputText value="#{general['newBugEnter.errors.componentVersionHasNoSolverAssigned']}" styleClass="error"/>
    					<br />
    				</f:subview>
    			</p>
				
				<h:commandButton id="commitButton" value="#{general['newBugEnter.commitBug']}" action="#{NewBugEnterBean.commitBug}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
								
			</ui:define>
		</ui:composition>
	</body>
</html>