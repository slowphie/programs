<?php 

	$servername = "localhost";
	$username = "13081";
	$password = "luckycart27";
	$database = "13081_volare_airlines";


	//http://fhsdte01.feildinghigh.school.nz/websites/13dte/13081/voters.php

	//Create connection
	$con = new mysqli($servername, $username, $password, $database);
	//check connection
	if (mysqli_connect_error())
		{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
		}
?>