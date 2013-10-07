<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<head>
<meta charset="utf-8">
<title>accordion demo</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
</head>
<body>
<div id="accordion">
<h3>Kill</h3>
<div>
<form action="/werewolf/players/kill" method="post">
Victim: <input type="text" name="victim"><br>
<input type="submit" value="Submit">
</form> 
</div>
<h3>Vote</h3>
<div>
<form action="/werewolf/players/vote" method="post">
Vote for: <input type="text" name="voted"><br>
<input type="submit" value="Submit">
</form> 
</div>
<h3>Section 3</h3>
<div>
<p>Nam enim risus, molestie et, porta ac, aliquam ac, risus.
Quisque lobortis.Phasellus pellentesque purus in massa.</p>
<ul>
<li>List item one</li>
<li>List item two</li>
<li>List item three</li>
</ul>
</div>
</div>
<script>
$( "#accordion" ).accordion();
</script>  
</body>
</html>
