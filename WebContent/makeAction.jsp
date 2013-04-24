<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="ru.netcracker.belyaev.routing.Routing" %>
<%
String uid = request.getParameter("uid");
String action = request.getParameter("action");
String direction = null;
String treasureColor = null;
if(action.equals("go") || action.equals("shoot")) {
	direction = request.getParameter("direction");
} else if(action.equals("pick-up")) {
	treasureColor = request.getParameter("color");
}

Routing routing = new Routing();

if(action.equals("go")) {
	routing.go(uid, direction);
} else if(action.equals("shoot")) {
	routing.shoot(uid, direction);
} else if(action.equals("pick-up")) {
	routing.takeTreasure(uid, treasureColor);
} else if(action.equals("drop")) {
	routing.dropTreasure(uid);
} else if(action.equals("predict")) {
	routing.askPrediction(uid);
} else if(action.equals("exit")) {
	routing.exit(uid);
} else if(action.equals("draw")) {
	routing.drawBoard();
} else if(action.equals("generate")) {
	routing.generate();
} else if(action.equals("stop")) {
	routing.stop();
} else {
	System.out.println("!!! some wrong command from client !!!");
}

%>