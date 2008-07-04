<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
			<ui:define name="title">
				#{general['home.title']}
			</ui:define>
			
			<ui:define name="body">
				<table width="100%" height="100%" align="center" style="vertical-align:middle;">
					<tr height="100%" align="center" style="vertical-align:middle;">
						<td width="100%" height="100%" align="center" style="vertical-align:middle;">
							<h:outputText value="" />
						</td>
					</tr>
				</table>
			</ui:define>
		</ui:composition>
	</body>
</html>