<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Seat Booking</title>
	<link rel="stylesheet" href="css/style.css">
	<link href='https://fonts.googleapis.com/css?family=Raleway' rel='stylesheet'>

</head>
<body>
	<?php
	//http://localhost/volare_airlines/seat_booking.php
	
	require("connect.php");
	$seatErr = "";
	$seat = "";

	$book_yes = "";
	$plane_yes = "";
	$faq_yes = "";

	$sql = mysqli_query($con, "SELECT MAX(`id_passenger`) AS last_updated FROM `passenger`");
	$row = mysqli_fetch_array($sql);
	$passenger_id = $row["last_updated"];


	//Selecting the appropriate available seats

	$sql = mysqli_query($con, "SELECT `class` AS class FROM passenger WHERE `id_passenger`='$passenger_id'");
	$fetchclass = mysqli_fetch_array($sql);
	$class = $fetchclass["class"];


	$sql = mysqli_query($con, "SELECT `adult_child` AS age FROM passenger WHERE `id_passenger`='$passenger_id'");
	$fetchclass = mysqli_fetch_array($sql);
	$adultchild = $fetchclass["age"];

	if ($adultchild==1){
		$seat_table = mysqli_query($con, "SELECT * FROM seating WHERE (class='$class' AND available=1)"); 

	}elseif ($adultchild==2){
		$seat_table = mysqli_query($con, "SELECT * FROM `seating` WHERE (`class`='$class' AND `available`=1 AND `restricted`=0)"); 

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

	if ((empty($_POST["yes_button"])) and ($_SERVER["REQUEST_METHOD"] == "POST")) {		

	  if (empty($_POST["seat"])) {
	    $seatErr = "* Required Field";
	  } else {
	    $seat = test_input($_POST["seat"]);
	    }	   

		if(empty($seatErr)){
			$update = "UPDATE `passenger` SET `seat_number`='$seat' WHERE `id_passenger`='$passenger_id'";
			$result = mysqli_query($con, $update);

			if($result && mysqli_affected_rows($con)>0){
				$book = mysqli_query($con, "UPDATE `seating` SET `available`=0 WHERE `seat`='$seat'");
			if($result){
				header("Location: review.php");
			}else{
			echo "Error - Database was not updated";
			}

			}else{
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
	        <p>Are you sure you want to exit Seat Booking? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="seat_booking.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
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
	        <p>Are you sure you want to exit Seat Booking? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="seat_booking.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
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
	        <p>Are you sure you want to exit Seat Booking? You will lose all details previously entered.</p><br><br>
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
	        <p>Are you sure you want to exit Seat Booking? You will lose all details previously entered.</p><br><br>
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
			<h2><img class="multipagebar" src="./css/images/seat_multipagebar.jpg" alt=""/></h2>
		</div>
	</div>
	<div class="text">Seat Booking</div>
	<div class="parallax"></div>
	<div id="main">
		<p><span class="error"></span></p>
		<div class="container">
			<div>
			<h2>Choose your seat</h2>
			<form method="post" action="seat_booking.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
			  Seat: 
			  <br><select name="seat">
			  	<option value="">Choose your seat</option>
			  	<?php
			  	while($row = mysqli_fetch_array($seat_table))
					  	{
						echo "<option value='" .$row['seat']. "'>" .$row['seat']. "</option>";
						}
			  	?>
			  </select>
			  <span class="error"><?php echo $seatErr;?></span>
			  <br>

			  <input type="submit" name="submit" value="Submit">  
			</form>
			</div>
			<div>
			<img class="seating" src="./css/images/seating_plan.jpg" alt=""/>
			</div>
		</div>
	</div>



</body>
</html>
