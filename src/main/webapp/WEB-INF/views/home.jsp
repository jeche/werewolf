<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<style>
#accordion {
width:100%;
}

#center {
margin-left: auto;
margin-right: auto;
width: 50%;
background-color: rgba(0, 0, 0, 0.6);
}

.button {
	text-decoration: none;
	background-color:rgba(0, 0, 0, 0.6);
	padding: 5px;
	color:white;
	border:none;
	margin: 20px;
	border:solid thin transparent;
}

.button:hover {
	background-color:rgba(0, 0, 0, .1);
	border-color: white;
} 

.input {
    background-color:rgba(0,0,0,0.4);
    border: 0;
    color: white;
    width: 300px;
    margin: 5px;
}

.label {
	display: inline-block;
    float: left;
    clear: left;
    width: 100px;
	color: white;
	margin-left:20px;
	margin-top:15px;
	margin-right:15px;
}

#specialLabel {
font-size:1.5em;
color:white;
margin:20px;
padding-top:20px;
}

.alignBox {
	display: inline-block;
}
</style>
	<title>Registration</title>

</head>
<body>
<div id="center">
<h3 id="specialLabel">Add a User</h3>
<form action="/addUser" method="post">
<label class="label">First name:</label> <input class="input alignBox" type="text" name="firstName"><br>
<label class="label">Last name:</label> <input class="input alignBox" type="text" name="lastName"><br>
<label class="label">Password:</label> <input class="input alignBox" type="password" name="hashedPassword"><br>
<label class="label">User name:</label> <input class="input alignBox" type="text" name="userName"><br>
<label class="label">ID:</label> <input class="input alignBox" type="text" name="id"><br>
<label class="label">IMG:</label> <input class="input alignBox" type="text" name="img"><br>
<input class="button" type="submit" value="Submit">
</form> 
</body>
</div>
</html>
