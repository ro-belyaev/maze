<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href='./bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css'>
<script type="text/javascript">
	<%@ include file='./jquery-1.9.1.min.js' %>
</script>
<script type="text/javascript">
	<%@ include file='./bootstrap/js/bootstrap.js' %>
</script>
<script type="text/javascript">
	<%@ include file='./game.js' %>
</script>
<title>Insert title here</title>
</head>
<body>
    <div id="actions" class="well">
        <button id="go" type="button" class="btn"><i class="icon-road"></i></button>
        <button id="shoot" type="button" class="btn"><i class="icon-screenshot"></i></button>

        <div class="btn-group">
            <button id="left" type="button" class="btn"><i class="icon-arrow-left"></i></button>
            <button id="right" type="button" class="btn"><i class="icon-arrow-right"></i></button>
            <button id="up" type="button" class="btn"><i class="icon-arrow-up"></i></button>
            <button id="down" type="button" class="btn"><i class="icon-arrow-down"></i></button>
        </div>


        <button id="drop" type="button" class="btn"><i class="icon-minus"></i></button>
        <button id="pick-up" type="button" class="btn"><i class="icon-plus"></i></button>
        <button id="predict" type="button" class="btn"><i class="icon-question-sign"></i></button>
        <button id="exit" type="button" class="btn"><i class="icon-off"></i></button>

        <button id="draw" type="button" class="btn"><i class="icon-pencil"></i></button>
        <button id="generate" type="button" class="btn"><i class="icon-wrench"></i></button>
        <button id="stop" type="button" class="btn"><i class="icon-ban-circle"></i></button>
    </div>
    <select id="user-id">
        <option>0</option>
        <option>1</option>
        <option>2</option>
    </select>
</body>
</html>