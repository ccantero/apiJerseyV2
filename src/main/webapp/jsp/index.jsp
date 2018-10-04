<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>JSP Page</title>
<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
<style type="text/css">
.contEvento {
	float: left;
	clear: both;
}

form{
	width: 500px;
}
label,input,button {
	float: left;
	margin-bottom: 10px;
}

label,button {
	clear: left;
}
</style>
</head>
<body>
	<%--         <h1>${it.hello} ${it.world}</h1> --%>
	<%@ page contentType="text/html; charset=iso-8859-1" language="java"  %>
	<%
 		String hostName=request.getServerName();
	%>
	
	<h2>JSP URI, URL, Context</h2>

Request Context Path: <%= request.getContextPath() %> <br>
Request URI:          <%= request.getRequestURI() %> <br>
Request URL:          <%= request.getRequestURL() %> <br>
Request ServletPath:  <%= request.getServletPath() %> <br>
Request ServerName:  <%= request.getServerName() %> <br>
Request LocalPort:  <%= request.getLocalPort() %> <br>

Host Name of server <%=hostName%>
		
	<h1>Administrador de eventos</h1>
	<div class="contEvento">
		<h3>Telegram</h3>
		<a href="http://localhost:8080/telegram" target="_blank">http://localhost:8080/telegram</a>
	</div>
	<div class="contEvento">
		<h3>Ver usuarios</h3>
		<a href="http://localhost:8080/users" target="_blank">http://localhost:8080/users</a>
	</div>
	<div class="contEvento">
		<h3>Ver eventos disponibles</h3>
		<a href="http://localhost:8080/events" target="_blank">http://localhost:8080/events</a><br>
		<br> <br>
	</div>
	<div class="contEvento">
		<h3>Buscar evento</h3>
		<form method="post" action="http://localhost:8080/events/buscarEvento">
			<label>C�digo del evento</label> 
			<input type="number" name="eventId" onkeypress="return event.charCode >= 48 && event.charCode <= 57" required/>
			<button type="submit">Buscar</button>
		</form>
		<form method="post" action="http://localhost:8080/events/buscarEvento">
 			<label>Nombre del evento</label> <input type="text"	name="eventNombre" required/>
			<button type="submit">Buscar</button>
		</form>
	</div>



</body>
</html>
