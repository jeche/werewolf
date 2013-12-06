<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<html>
<head>
<style type="text/css">
.font {color:red;}
.accordion {width:50%;}
body {background-image:url("resources/wolfs-cry.jpg");
background-repeat:no-repeat;}
</style>
	<title>Home</title>

<meta charset="utf-8">
<title>accordion demo</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<style type="text/css">
.font {color:red;}
.accordion {width:50%;}
body {background-image:url("resources/wolfs-cry.jpg");
background-repeat:no-repeat;
background-size:100%}
.ui-widget-content {background-color: rgba(0,0,0,0.6);color: #ffffff;}
.ui-accordion-content {background-color: rgba(0,0,0,0.6);
color: #ffffff;}
</style>
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
}

.ui-accordion-content { 
background-color: rgba(0, 0, 0, 0.6);
color: white;}

.ui-widget-content {
background: rgba(0,0,0,0.6);
}

.ui-state-active {
background-color: black;
color: white;
}

.ui-state-default {
background: black;
color:white;
font-size:1.5em;
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
</style>
</head>
<body>
<div id="accordion" class="accordion">
<h3 class="font">Kill</h3>
<div>
<form action="/players/kill" method="post">
Victim: <input class="input" type="text" name="victim"><br>
<input class="button" type="submit" value="Submit">
</form> 
</div>
<h3>Vote</h3>
<div id="images">
<form action="/players/vote" method="post" id="form_id">
<select class="input" id="voted" name="voted" size="4"></select><br>
<input class="button" type="submit" value="Submit">
</form> 
</div>
<h3>Scent</h3>
<div>
<form action="/players/kill" method="post">
<select class="input" id="victim" name="victim" size="4"></select><br>
<input class="button" type="submit" value="Attack">
</form> 
<script>
$.getJSON("/players/scent", function(data) {
	var $victim =  $("#victim");
	$victim.empty();
	$.each(data, function(id, Player) {
		if(Player.score == 0){
		$victim.append($('<option value="'+Player.id+'">' + Player.id + '</option>'));}
		else if(Player.score == 2 ){
			$victim.append($('<option value="'+Player.id+'">' + Player.id + ' smells gross. </option>'));
		}
		else{
			$victim.append($('<option value="'+Player.id+'">' + Player.id + ' fresh blood. </option>'));
		}

	});
});

</script>

<script>
$.getJSON("/players/all", function(data) {
	var $voted =  $("#voted");
	$voted.empty();
	$.each(data, function(id, Player) {

		$voted.append($('<option value="'+Player.id+'">' + Player.id + '</option>'));

	});
});

</script>
<<<<<<< HEAD
=======

</div>
>>>>>>> 5680f370601af9d09f2fb196f39990c6330c265a
</div>
</div>
<script>
$( "#accordion" ).accordion();
</script>  
</body>
</html>
