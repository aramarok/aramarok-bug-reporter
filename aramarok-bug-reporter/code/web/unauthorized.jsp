<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
			<ui:define name="title">
				#{general['unauthorized.title']}
			</ui:define>
			
			<ui:define name="body">
				
				<h:outputText value="#{general['unauthorized.message1']}"/>
				<h:outputText value="#{general['unauthorized.message2']}"/>
				
			</ui:define>
		</ui:composition>
	</body>
</html>