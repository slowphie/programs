<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Passenger Details</title>
	<link rel="stylesheet" href="css/style.css">
	<link href='https://fonts.googleapis.com/css?family=Raleway' rel='stylesheet'>
</head>
<body>

	<?php
	//http://localhost/volare_airlines/passenger_details.php
	
	require("connect.php");

	$book_yes = "";
	$plane_yes = "";
	$faq_yes = "";

	// define variables and set to empty values
	$first_nameErr = $last_nameErr =$dobErr = $classErr = "";
	$first_name = $last_name = $dob =$class = "";

	if ($_SERVER["REQUEST_METHOD"] == "POST") {

	  if (empty($_POST["first_name"])) {
	    $first_nameErr = "* Required Field";
	  } else {
	    $first_name = test_input($_POST["first_name"]);
	    // check if name only contains letters and whitespace
	    if (!preg_match("/^[a-zA-Z]*$/",$first_name)) {
	      $first_nameErr = "* Invalid entry"; 
	    }
	  }

	  if (empty($_POST["last_name"])) {
	    $last_nameErr = "* Required Field";
	  } else {
	    $last_name = test_input($_POST["last_name"]);
	    // check if URL address syntax is valid
	    if (!preg_match("/^[a-zA-Z]*$/",$last_name)) {
	      $last_nameErr = "* Invalid entry"; 
	    }    
	  }


	  if (empty($_POST["dob"])) {
	    $dobErr = "* Required Field";
	  } else {
	    $dob = test_input($_POST["dob"]);
		}

	  if (empty($_POST["class"])) {
	    $classErr = "* Required Field";
	  } else {
	    $class = test_input($_POST["class"]);
	    }

	   
		if((isset($_POST["book_yes"])) and ($_SERVER["REQUEST_METHOD"] == "POST")){
			eraseFunc($con, 1);
		}
		if((isset($_POST["plane_yes"])) and ($_SERVER["REQUEST_METHOD"] == "POST")){
			eraseFunc($con, 2);
		}
		if((isset($_POST["faq_yes"])) and ($_SERVER["REQUEST_METHOD"] == "POST")){
			eraseFunc($con, 3);
		}   
		if((isset($_POST["home_yes"])) and ($_SERVER["REQUEST_METHOD"] == "POST")){
			eraseFunc($con, 4);
		}

		if(empty($first_nameErr) and empty($last_nameErr) and empty($phoneErr) and empty($dobErr) and empty($classErr)){

			$sql = "INSERT INTO `passenger` (`id_passenger`, `last_name`, `first_name`, `date_of_birth`, `class`) VALUES(NULL, '$last_name', '$first_name','$dob', '$class')";
			$result = mysqli_query($con, $sql);
			
			if($result && mysqli_affected_rows($con)>0){
			
				$last_id = $con->insert_id;

				//calculating age
				$date = new DateTime($dob);
				$flightdate = new DateTime("2017-11-25");
				$diff = $flightdate->diff($date)->format("%a");
				if ($diff>5840){
					$adultchild = 1;
					$pricefetch = mysqli_query($con,"SELECT `adult_price` FROM `class_price` WHERE `class_id`='$class'");
					while($row = mysqli_fetch_array($pricefetch)){
						$price=($row['adult_price']);
					}
					$sql2 = "UPDATE `billing_details` SET `price`='$price' WHERE `id`='$last_id'";
					$result2 = mysqli_query($con, $sql2);

				}else{
					$adultchild = 2;
					$pricefetch = mysqli_query($con,"SELECT `child_price` FROM `class_price` WHERE `class_id`='$class'");
					while($row = mysqli_fetch_array($pricefetch)){
						$price=($row['child_price']);
					}
					$sql2 = "UPDATE `billing_details` SET `price`='$price' WHERE `id`='$last_id'";
					$result2 = mysqli_query($con, $sql2);
				}
				
				$sql3 = "UPDATE `passenger` SET `adult_child`='$adultchild'WHERE `id_passenger`='$last_id'";
				$result3 = mysqli_query($con, $sql3);	
				
				if($result2 && $result3){
					header("Location: seat_booking.php");	
				}			
			} else {
				echo "Error - Database was not updated";
			}

			}

	}


	function test_input($data) {
	  $data = trim($data);
	  $data = stripslashes($data);
	  $data = htmlspecialchars($data);
	  return $data;
	}


	function eraseFunc($con, $page){
		$select_id = "SELECT MAX(`id`) AS updated_last FROM `billing_details`";
		$resultone = mysqli_query($con, $select_id);
		$get_array = mysqli_fetch_array($resultone);
		$billing_id = $get_array["updated_last"];

		//select the phone number as it will be unique
		$get_phone = "SELECT `phone` AS select_phone FROM `billing_details`  WHERE `id`='$billing_id'";
		$resulttwo = mysqli_query($con, $get_phone);
		$select_array = mysqli_fetch_array($resulttwo);
		$phone_number = $select_array["select_phone"];

		$delete_billing = "DELETE FROM `billing_details` WHERE `phone`='$phone_number'";
		$resulttwo = mysqli_query($con, $delete_billing);

		$billing_delete_increment = mysqli_query($con, "ALTER TABLE billing_details DROP COLUMN id");
		$billing_add_new_increment = mysqli_query($con, "ALTER TABLE billing_details ADD id INT NOT NULL AUTO_INCREMENT FIRST, ADD PRIMARY KEY (id)");

		if($resulttwo){
			$select_id = "SELECT MAX(`id`) AS last_id FROM `billing_details`";
			$resultthree = mysqli_query($con, $select_id);
			$array = mysqli_fetch_array($resultthree);
			$last_billing = $array["last_id"];


			$get_seats = mysqli_query($con, "SELECT `seat_number` FROM `passenger` WHERE `id_passenger`>'$last_billing'");

			while($row = mysqli_fetch_array($get_seats)){
				$seat_no = $row['seat_number'];
				$book = mysqli_query($con, "UPDATE `seating` SET `available`=1 WHERE `seat`='$seat_no'");
			}


			$delete_passenger = "DELETE FROM `passenger` WHERE `id_passenger`>'$last_billing'";
			$result = mysqli_query($con, $delete_passenger);


			$passenger_delete_increment = mysqli_query($con, "ALTER TABLE passenger DROP COLUMN id_passenger");
			$passenger_add_new_increment = mysqli_query($con, "ALTER TABLE passenger ADD id_passenger INT NOT NULL AUTO_INCREMENT FIRST, ADD PRIMARY KEY (id_passenger)");	

			if($result){
				if($page==1){
					header("Location: billing_details.php");
				}
				if($page==2){
					header("Location: planes.php");
				}
				if($page==3){
					header("Location: faq.php");;
				}
				if($page==4){
					header("Location: home_page.php");;
				}
				
			}else{
				echo "Error - Database was not updated";
			}
		}else{
			echo "Error - Database was not updated";
		}

	}


	?>

  <main>
    <div class=content>
    	<div class="headingbar">
			<div id="logo">
				<a href="#home"><img src="./css/images/logo.jpg"/></a>
			</div>
			<ul>
		      	<li><a href="#book_a_flight">Book a Flight</a></li>
				<li><a href="#planes_destinations">Our Planes and Destinations</a></li>
				<li><a href="#faq">FAQ</a></li>
				<li id="title">Volare Airlines</li>
			</ul>
		</div>

	  <!-- id here matches href in the activating link -->
	  <div id="book_a_flight" class="pop-up">

	    <!-- The pop-up block -->
	    <div class="popBox">

	      <!-- If the content becomes larger than the pop-up this div will scroll the content -->
	      <div class="popScroll">
	        <h3>Book a Flight</h3>
	        <p>Are you sure you want to exit Passenger Details? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="passenger_details.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
				<input type="submit" class="button_yes" name="book_yes" value="Yes">  
			</form>
			<a href="" class="button_cancel">Cancel</a>
	      </div>
	      <a href="" class="close"><span>Back to page</span></a>
	    </div>
	    <a href="" class="lightbox">Back to page</a>
	  </div>

	  <div id="planes_destinations" class="pop-up">
	    <div class="popBox">
	      <div class="popScroll">
	        <h3>Our Planes and Destinations</h3>
	        <p>Are you sure you want to exit Passenger Details? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="passenger_details.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
				<input type="submit" class="button_yes" name="plane_yes" value="Yes">  
			</form>
			<a href="" class="button_cancel">Cancel</a>
	      </div>
	      <a href="" class="close"><span>Back to page</span></a>
	    </div>
	    <a href="" class="lightbox">Back to page</a>
	  </div>

	  <div id="faq" class="pop-up">
	    <div class="popBox">
	      <div class="popScroll">
	      	<h3>FAQ</h3>
	        <p>Are you sure you want to exit Passenger Details? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="seat_booking.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
				<input type="submit" class="button_yes" name="faq_yes" value="Yes">  
			</form>
			<a href="" class="button_cancel">Cancel</a>
	      </div>
	      <a href="" class="close"><span>Back to page</span></a>
	    </div>
	    <a href="" class="lightbox">Back to page</a>
	  </div>


	  <div id="home" class="pop-up">
	    <div class="popBox">
	      <div class="popScroll">
	      	<h3>Volare Airlines</h3>
	        <p>Are you sure you want to exit Passenger Details? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="seat_booking.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
				<input type="submit" class="button_yes" name="home_yes" value="Yes">  
			</form>
			<a href="" class="button_cancel">Cancel</a>
	      </div>
	      <a href="" class="close"><span>Back to page</span></a>
	    </div>
	    <a href="" class="lightbox">Back to page</a>
	  </div>

	</div>
	</main>
  	<div id="main">
		<div class="titleheading">
			<h2><img class="multipagebar" src="./css/images/passenger_multipagebar.jpg" alt=""/></h2>
		</div>
	</div>
	<div class="text">Passenger Details</div>
	<div class="parallax"></div>
	<div id="main">
		<p><span class="error"></span></p>

		<div class="container">
			<h2>Enter your details below</h2>
			<form method="post" action="passenger_details.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">  

			  First name: <input type="text" name="first_name"><br>
			  <span class="error"><?php echo $first_nameErr;?></span>
			  <br><br>

			  Surname: <input type="text" name="last_name"><br>
			  <span class="error"><?php echo $last_nameErr;?></span>
			  <br><br>

			  Date of Birth: 
			  <br><input type="date" name="dob"><br>
			  <span class="error"><?php echo $dobErr;?></span>
			  <br><br>

			  <?php $class_table = mysqli_query($con, "SELECT * FROM class_price"); ?>
			  Class: 
			  <br><select name="class">
			  	<option value="">Choose your class</option>
			  	<?php
			  	while($row = mysqli_fetch_array($class_table))
					  	{
						echo "<option value='" .$row['class_id']. "'>" .$row['name']. "</option>";
						}
			  	?>
			  </select>
			  <span class="error"><?php echo $classErr;?></span>
			  <br><br>

			  <input type="submit" name="submit" value="Submit"> 

			</form>

		<div class="class_info">
			<?php
				$class_table = mysqli_query($con, "SELECT * FROM class_price");

				//below causing options not to display in selection 
				echo "<table>
				<tr>
				<th>Class</th>
				<th>Adult Price</th>
				<th>Child Price (under 16yr)</th>
				</tr>";

				while($row = mysqli_fetch_array($class_table)){
					echo "<tr>";
					echo "<td>". $row['name'] . "</td>";
					echo "<td>","$". $row['adult_price'] . "</td>";
					echo "<td>","$". $row['child_price'] . "</td>";
					echo "</tr>";
					  }
				echo "</table>";
			?>
		</div>
		</div>
	</div>

</body>
</html>