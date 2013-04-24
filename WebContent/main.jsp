<%@page import="ru.netcracker.belyaev.model.models.Board"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.*" %>
<%@ page import="java.lang.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<%
String task = request.getParameter("task");
if(task.equals("generation")) { 
	Board.getInstance().generateBoard(); %>
generation
<% } else if(task.equals("show")) {
	out.println(Board.getInstance().getNumOfPlayer());
} else {
	out.println("wrong command");
}
%>

</body>
</html>