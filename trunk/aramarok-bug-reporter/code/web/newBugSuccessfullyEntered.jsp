<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
		
			<ui:define name="title">
				#{general['newBug.title']}
			</ui:define>
			
			<ui:define name="body">
			
				<br />
				<br />
				
				<h:outputText value="#{general['newBugSuccessfullyEntered.text1']}" rendered="true" />
				<br />
				<h:outputText value="#{general['newBugSuccessfullyEntered.text2']}" rendered="true" />
				<h:commandLink value="#{NewBugSuccessfullyEnteredBean.newBugEnteredId}" action="#{NewBugSuccessfullyEnteredBean.viewBug}" rendered="true" />
				
				<br />
				<br />
												
			</ui:define>
		</ui:composition>
	</body>
</html>