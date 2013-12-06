<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<html>
<head>
<style type="text/css">
.font {color:red;}
.accordion {width:50%;}
</style>
	<title>Home</title>
</head>
<body>
<head>
<meta charset="utf-8">
<title>accordion demo</title>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
</head>
<body>
<div id="accordion" class="accordion">
<h3 class="font">Kill</h3>
<div>
<form action="/players/kill" method="post">
Victim: <input type="text" name="victim"><br>
<input type="submit" value="Submit">
</form> 
</div>
<h3>Vote</h3>
<div id="images">
<form action="/players/vote" method="post" id="form_id">
<select id="voted" name="voted" size="4"></select><br>
<input type="submit" value="Submit">
</form> 
</div>
<h3>Scent</h3>
<div>
<form action="/players/kill" method="post">
<select id="victim" name="victim" size="4"></select><br>
<input type="submit" value="Attack">
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
</div>
</div>
<script>
$( "#accordion" ).accordion();
</script>  
</body>
</html>
