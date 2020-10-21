<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Billing Details</title>
	<link rel="stylesheet" href="css/style.css">
	<link href='https://fonts.googleapis.com/css?family=Raleway' rel='stylesheet'>

</head>
<body>
	<?php
	//http://localhost/volare_airlines/billing_details.php


	require("connect.php");

	$given_nameErr = $surnameErr = $phoneErr = $emailErr = $groupErr = "";
	$given_name = $surname = $phone = $email = $group = "" ;



	if ($_SERVER["REQUEST_METHOD"] == "POST") {

	  if (empty($_POST["given_name"])) {
	    $given_nameErr = "* Required Field";
	  } else {
	    $given_name = test_input($_POST["given_name"]);
	    // check if URL address syntax is valid
	    if (!preg_match("/^[a-zA-Z]*$/",$given_name)) {
	      $given_nameErr = "* Invalid entry"; 
	    }    
	  }

	  if (empty($_POST["surname"])) {
	    $surnameErr = "* Required Field";
	  } else {
	    $surname = test_input($_POST["surname"]);
	    // check if URL address syntax is valid
	    if (!preg_match("/^[a-zA-Z]*$/",$surname)) {
	      $surnameErr = "* Invalid entry"; 
	    }    
	  }

	  if (empty($_POST["phone"])) {
	    $phoneErr = "* Required Field";
	  } else {
	    $phone = test_input($_POST["phone"]);
	    // check if URL address syntax is valid
	    if (!preg_match("/^[0-9]*$/",$phone)) {
	      $phoneErr = "* Invalid entry"; 
	    }    

	  }

	  if (empty($_POST["email"])) {
	    $emailErr = "* Required Field";
	  } else {
	    $email = test_input($_POST["email"]);
	    // check if e-mail address is well-formed
	    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
	      $emailErr = "* Invalid email format"; 
	    }
	  }
	    
	  if (empty($_POST["group"])) {
	    $groupErr = "* Required Field";
	  } else {
	    $group = test_input($_POST["group"]);
	    // check if e-mail address is well-formed
	    if (!preg_match("/^[0-9]*$/",$group)) {
	      $groupErr = "* Invalid entry"; 
	    }
	  }



	if(empty($given_nameErr) and empty($surnameErr) and empty($phoneErr) and empty($emailErr) and empty($groupErr)){

		for ($i=1; $i<=$group; $i++){
			echo "Hello";
			$sql = "INSERT INTO `billing_details` (`id`, `given_name`, `surname`, `phone`, `email`) VALUES(NULL, '$given_name', '$surname',  '$phone', '$email')";

			$result = mysqli_query($con, $sql);
		}

		if($result){
			
			header("Location: passenger_details.php");
		} else {
			echo "Error - Database was not updated";
		}
	}	
	}

	function test_input($data) {
	  $data = htmlspecialchars($data);
	  return $data;
	}


	?>

	<div class="headingbar">
	<div id="logo">
		<a href="home_page.php"><img src="./css/images/logo.jpg"/></a>
	</div>
		<ul>
			<li><a href="billing_details.php">Book a Flight</a></li>
			<li><a href="planes.php">Our Planes and Destinations</a></li>
			<li><a href="faq.php">FAQ</a></li>
			<li id="title">Volare Airlines</li>
		</ul>
	</div>
	</div>
	<div id="main">
		<div class="titleheading">
			<h2><img class="multipagebar" src="./css/images/billing_multipagebar.jpg" alt=""/></h2>
		</div>
	</div>
	<div class="text">Billing Details</div>
	<div class="parallax"></div>
	<div id="main">
		<p><span class="error"></span></p>
		<div class="container">
			<h2>Enter your billing details below</h2>		
			<form method="post" action="billing_details.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>> 
		 
			  First name: <input type="text" name="given_name">
			  <span class="error"><?php echo $given_nameErr;?></span>
			  <br><br>

			  Surname: <input type="text" name="surname">
			  <span class="error"><?php echo $surnameErr;?></span>
			  <br><br>

			  Phone: <input type="text" name="phone">
			  <span class="error"><?php echo $phoneErr;?></span>
			  <br><br>

			  Email: <input type="text" name="email">
			  <span class="error"><?php echo $emailErr;?></span>
			  <br><br>

			  Number of passengers to be charged to these billing details:
			  <input type="number" name="group" min="1" max="20">
			  <span class="error"><?php echo $groupErr;?></span>
			  <br><br>

			  <input type="submit" name="submit" value="Submit">  

			</form>
		</div>
	</div>


</body>
</html>