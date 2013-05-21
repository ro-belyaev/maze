<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	<%@ include file='./jquery-1.9.1.min.js' %>
</script>
<script type="text/javascript">
	<%@ include file='./registration.js' %>
</script>
</head>
<body>
    <form>
        <fieldset>
        <legend>Registration</legend>
        <input type="text" id="name" placeholder="Enter name">
        <input type="text" id="pass" placeholder="Enter password">
        <%--<button class="submit" id="reg">Register</button>--%>
        <input type="button" id="reg" value="REG">
        </fieldset>
    </form>
</body>
</html>