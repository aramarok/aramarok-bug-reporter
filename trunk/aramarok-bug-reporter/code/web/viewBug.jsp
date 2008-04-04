<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:t="http://myfaces.apache.org/tomahawk"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['viewBug.title']}
			</ui:define>
			
			<ui:define name="body">
				<h:outputText value="#{ViewBugBean.loadData}" rendered="true" />
				<f:subview rendered="#{!ViewBugBean.bugIdNullOrNotFound}" >
				<table>
					<tr>
						<td>
							<table>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.bugId']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.bugId}" styleClass="label-right"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.dateReported']}: " styleClass="label2"/>
									</td>
									<td>
										<t:inputCalendar disabled="true" id="inputReportedDate" value="#{ViewBugBean.dateReported}" renderAsPopup="true" renderPopupButtonAsImage="true" popupDateFormat="#{ViewBugBean.dateFormat}" styleClass="medium-input"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.reporter']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.reporterUserName}" styleClass="label-right"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.product']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.productName}" styleClass="label-right"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.component']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.componentName}" styleClass="label-right"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.version']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.versionName}" styleClass="label-right"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.assignedTo']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.assignedTo}" styleClass="label-right"/>
									</td>
								</tr>
							</table>
						</td>
						<td>
							<table>
					    		<tr>
					    			<td align="right">
										<h:outputText value="#{general['viewBug.operatingSystem']}: " styleClass="label2"/>
									</td>
									<td>
										<h:selectOneMenu disabled="#{!ViewBugBean.canEditBug}" id="operatingSystemList" value="#{ViewBugBean.operatingSystem}" styleClass="input">
					    					<f:selectItems value="#{ViewBugBean.operatingSystemList_out}"/> 
					    				</h:selectOneMenu>
					    			</td>
					    		</tr>
					    		<tr>
					    			<td align="right">
					    				<h:outputText value="#{general['viewBug.platform']}: " styleClass="label2"/>
					    			</td>
					    			<td>
					    				<h:selectOneMenu disabled="#{!ViewBugBean.canEditBug}" id="inputPlatformList" value="#{ViewBugBean.platform}" styleClass="input">
					    					<f:selectItems value="#{ViewBugBean.platformList_out}"/> 
					    				</h:selectOneMenu>
					    			</td>
					    		</tr>
					    		<tr>
					    			<td align="right">
					    				<h:outputText value="#{general['viewBug.priority']}: " styleClass="label2"/>
					    			</td>
					    			<td>
					    				<h:selectOneMenu disabled="#{!ViewBugBean.canEditBug}" id="inputPriorityList" value="#{ViewBugBean.priority}" styleClass="input">
					    					<f:selectItems value="#{ViewBugBean.priorityList_out}"/> 
					    				</h:selectOneMenu>
					    			</td>
					    		</tr>
					    		<tr>
					    			<td align="right">
					    				<h:outputText value="#{general['viewBug.severity']}: " styleClass="label2"/>
					    			</td>
					    			<td>
					    				<h:selectOneMenu disabled="#{!ViewBugBean.canEditBug}" id="inputSeverityList" value="#{ViewBugBean.severity}" styleClass="input">
					    					<f:selectItems value="#{ViewBugBean.severityList_out}"/> 
					    				</h:selectOneMenu>
					    			</td>
					    		</tr>
					    		<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.dateObserved']}: " styleClass="label2"/>
									</td>
									<td>
										<t:inputCalendar disabled="#{!ViewBugBean.canEditBug}" id="inputObservedDate" value="#{ViewBugBean.dateObserved}" renderAsPopup="true" renderPopupButtonAsImage="true" popupDateFormat="#{ViewBugBean.dateFormat}" styleClass="medium-input"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.bugSummary']}: " styleClass="label2"/>
									</td>
									<td>
										<h:inputText disabled="#{!ViewBugBean.canEditBug}" value="#{ViewBugBean.summary}" styleClass="input"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<center>
							<table>
					    		<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.bugStatus']}: " styleClass="label2"/>
									</td>
									<td>
					    				<h:selectOneMenu disabled="#{!ViewBugBean.canChangeStatus}" id="bugStatus" value="#{ViewBugBean.bugState}" styleClass="input">
					    					<f:selectItems value="#{ViewBugBean.bugGeneralStatusList_out}"/> 
					    				</h:selectOneMenu>
					    			</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.additionalComment']}: " styleClass="label2"/>
									</td>
									<td>
										<h:inputTextarea disabled="#{!ViewBugBean.canAddComment}" value="#{ViewBugBean.additionalComment}" styleClass="inputTextArea"/>
									</td>
								</tr>
								<tr>
									<td>
										<h:outputText value=" " />
									</td>
									<td>
										<h:commandButton id="commitButton" disabled="false" value="#{general['viewBug.commitBug']}" action="#{ViewBugBean.commitBug}" styleClass="normal-button" onmouseover="this.className='normal-button2'" onmouseout="this.className='normal-button'"/>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<f:subview rendered="#{ViewBugBean.summaryNotEntered}" >
					    					<h:outputText value="#{general['viewBug.errors.summaryNotEntered']}" styleClass="error"/>
					    					<br />
					    				</f:subview>
					    				<f:subview rendered="#{ViewBugBean.observedDateNotEnterd}" >
					    					<h:outputText value="#{general['viewBug.errors.observedDateNotEnterd']}" styleClass="error"/>
					    					<br />
					    				</f:subview>
					    				<f:subview rendered="#{ViewBugBean.userHasNoRight}" >
					    					<h:outputText value="#{general['viewBug.errors.userHasNoRight']}" styleClass="error"/>
					    					<br />
					    				</f:subview>
					    				<f:subview rendered="#{ViewBugBean.cannotChangeBugStatus}" >
					    					<h:outputText value="#{general['viewBug.errors.cannotChangeBugStatus']}" styleClass="error"/>
					    					<br />
					    				</f:subview>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.bugDescription']}: " styleClass="label2"/>
									</td>
									<td>
										<h:outputText value="#{ViewBugBean.description}" styleClass="label-right"/>
									</td>
								</tr>
								<tr>
									<td align="right">
										<h:outputText value="#{general['viewBug.additionalComments']}: " styleClass="label2"/>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<h:dataTable rendered="#{!ViewBugBean.commentsListEmpty}" value="#{ViewBugBean.comments}" var="comm" border="0"  columnClasses="output" rowClasses="table-light-row,table-dark-row" headerClass="table-th">
						    				<h:column>
						    					<f:facet name="header">
						    						<h:outputText value="#{general['viewBug.additionalComments.table.datePosted']}" />
						    					</f:facet>
						    					<h:outputText value="#{comm.commentDate}" styleClass="label-right"  />
						    				</h:column>
						    				
						    				<h:column>
						    					<f:facet name="header">
						    						<h:outputText value="#{general['viewBug.additionalComments.table.comment']}" />
						    					</f:facet>
						    					<h:outputText value="#{comm.commentText}" styleClass="label-right" />
						    				</h:column>
										</h:dataTable>
										<h:outputText rendered="#{ViewBugBean.commentsListEmpty}" value="#{general['viewBug.additionalComments.noAdditionalComments']}" />
									</td>
								</tr>
							</table>
							</center>
						</td>
					</tr>
				</table>
				</f:subview>
				<f:subview rendered="#{ViewBugBean.bugIdNullOrNotFound}" >
					<h:outputText rendered="#{!ViewBugBean.bugNotFoundInTheDataBase}" value="#{general['viewBug.errors.bugIdWasNull']}" styleClass="error"/>
					<h:outputText rendered="#{ViewBugBean.bugNotFoundInTheDataBase}" value="#{general['viewBug.errors.bugIdWasNotFoundInTheDatabase']}" styleClass="error"/>
				</f:subview>
			</ui:define>
		</ui:composition>
	</body>
</html>