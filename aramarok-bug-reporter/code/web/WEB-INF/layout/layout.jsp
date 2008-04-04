<html xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:ui="http://java.sun.com/jsf/facelets">
	
	<head>
		<title>
			<ui:insert name="title">#{general['aramarok.default.page.title']}</ui:insert>
		</title>
		<link rel="stylesheet" type="text/css" href="./css/styles.css" />
	</head>
	
	<body> 
	<f:view>
	<f:loadBundle basename="com.drey.aramarok.web.aramarok" var="general"/>
		<h:messages />
		<h:form>
		<table class="layout-div" align="center">
		<tr align="center">
		<td width="100%" align="center">
			<br/>
			<table class="layout-table">
				<tr> 
					<td class="td-header">
						<ui:insert name="header">
							<table width="100%" border="0">
								<tr>
									<td>
										<img src="./images/logo.bmp"/>
									</td>
									<td width="30%" align="left" valign="bottom">
										#{general['header.main.label']}
									</td>
									<td width="50%" align="right" valign="bottom">
										<h:outputText rendered="#{LayoutBean.loggedIn}" value="#{general['aramarok.default.logedIn.userName']}" styleClass="label2"/>
										<h:outputText rendered="#{LayoutBean.loggedIn}" value=" #{LayoutBean.userName}" styleClass="label2"/>
									</td>
								</tr>
							</table>        	 		
		         		</ui:insert>
		         	</td>
				</tr>
				<tr> 
					<td class="td-horizontal-menu">
						<ui:insert name="horizontalMenu">
							<table width="100%">
								<tr>
									<td class="align-center">
										<h:commandLink id="homeButton" action="HOME_OUTCOME" value="#{general['horizontal.menu.home']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.enterNewBugRight}" id="newBugButton" action="NEW_BUG_OUTCOME" value="#{general['horizontal.menu.newBug']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.searchBugsRight}" id="goToBugButton" action="#{LayoutBean.goToBug}" value="#{general['horizontal.menu.goToBug']}" styleClass="menu-link"/>
										<h:inputText rendered="#{RightsBean.searchBugsRight}" value="#{LayoutBean.bugId}" styleClass="input30" />
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.searchBugsRight}" id="searchBugsButton" action="SEARCH_BUGS_OUTCOME" value="#{general['horizontal.menu.search']}" styleClass="menu-link"/>
									</td>
									<f:subview rendered="false">
										<td class="align-center">
											<h:commandLink rendered="#{RightsBean.enterNewBugRight}" id="myBugsButton" action="MY_BUGS_OUTCOME" value="#{general['horizontal.menu.myBugs']}" styleClass="menu-link"/>
										</td>
									</f:subview>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.editPreferencesRight}" id="preferencesButton" action="PREFERENCES_OUTCOME" value="#{general['horizontal.menu.preferences']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.defineProductsRight}" id="productsButton" action="PRODUCTS_OUTCOME" value="#{general['horizontal.menu.products']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.defineComponentsRight}" id="componentsButton" action="COMPONENTS_OUTCOME" value="#{general['horizontal.menu.components']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.defineVersionsRight}" id="versionsButton" action="VERSIONS_OUTCOME" value="#{general['horizontal.menu.versions']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.defineAramarokOptionsRight}" id="sysOptionsButton" action="SYS_OPTIONS_OUTCOME" value="#{general['horizontal.menu.sysOptions']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink rendered="#{RightsBean.editOtherUsersRight}" id="usersButton" action="USERS_OUTCOME" value="#{general['horizontal.menu.users']}" styleClass="menu-link"/>
									</td>
									<td class="align-center">
										<h:commandLink id="logoutButton" action="#{LogoutBean.logout}" value="#{general['horizontal.menu.logout']}"  styleClass="menu-link"/>
									</td>
								</tr>
							</table>
						</ui:insert>        	 		
					</td>
				</tr>
				<tr> 
					<td class="td-body">
						<table class="main-table">
							<tr>
								<td class="main-content">
									<table border="0" align="center" height="100%">								
										<tr height="100%" style="vertical-align:top">         				
											<td height="100%" style="vertical-align:top">
												<ui:insert name="body">        	 		
						         				</ui:insert>
						         			</td>
										</tr>         				
				       				</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr> 
					<td class="td-footer">
						<ui:insert name="footer">
							<h:outputText value="#{general['footer.message']}" escape="false"/>
						</ui:insert>        	 		
					</td>
				</tr>
			</table>
		</td>
		</tr>
		</table>
		</h:form>
	</f:view>
	</body>
</html>