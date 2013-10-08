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
<form action="/players/kill" method="post">
Victim: <input type="text" name="victim"><br>
<input type="submit" value="Submit">
</form> 
</div>
<h3>Vote</h3>
<div>
<form action="/players/vote" method="post">
Vote for: <input type="text" name="voted"><br>
<input type="submit" value="Submit">
</form> 
</div>
<h3>Scent</h3>
<div>
<button type="button" onclick="proceed();">do</button> 
<script>
function proceed () {
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', 'http://google.com');
    form.style.display = 'hidden';
    document.body.appendChild(form)
    form.submit();
}
$.getJSON("/players/alive",
		function(data){
		  console.log(data);
		});
</script>
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
