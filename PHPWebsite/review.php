<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Receipt</title>
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
	$cancel_yes = "";
	
	$sql = mysqli_query($con, "SELECT MAX(`id`) AS last_updated FROM `billing_details`");
	$row = mysqli_fetch_array($sql);
	$billing_id = $row["last_updated"];

	$sql = mysqli_query($con, "SELECT MAX(`id_passenger`) AS passenger FROM `passenger`");
	$row = mysqli_fetch_array($sql);
	$passenger_id = $row["passenger"];

	$sql = mysqli_query($con, "SELECT `given_name` AS name FROM `billing_details` WHERE `id`='$billing_id'");
	$row = mysqli_fetch_array($sql);
	$billing_name = $row["name"];	
	$sql = mysqli_query($con, "SELECT `surname` AS lastname FROM `billing_details` WHERE `id`='$billing_id'");
	$row = mysqli_fetch_array($sql);
	$billing_lastname = $row["lastname"];
	$sql = mysqli_query($con, "SELECT `phone` AS phone FROM `billing_details` WHERE `id`='$billing_id'");
	$row = mysqli_fetch_array($sql);
	$billing_phone = $row["phone"];
	$sql = mysqli_query($con, "SELECT `email` AS email FROM `billing_details` WHERE `id`='$billing_id'");
	$row = mysqli_fetch_array($sql);
	$billing_email = $row["email"];

	$sql = mysqli_query($con, "SELECT COUNT(`phone`) AS number_billing FROM `billing_details` WHERE `phone`='$billing_phone'");
	$row = mysqli_fetch_array($sql);
	$number = $row["number_billing"];

	$min_id = ($billing_id - $number);
	$passengers_to_enter = ($billing_id-$passenger_id);

	$passenger_info = mysqli_query($con, "SELECT * FROM `passenger` WHERE `id_passenger`>'$min_id'");


	$sql = mysqli_query($con, "SELECT SUM(`price`) AS total_price FROM billing_details WHERE `phone`='$billing_phone'");
	$row = mysqli_fetch_array($sql);
	$total = $row["total_price"];



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
	if((isset($_POST["cancel_yes"])) and ($_SERVER["REQUEST_METHOD"] == "POST")){
		eraseFunc($con, 4);
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
	        <p>Are you sure you want to exit? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="review.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
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
	        <p>Are you sure you want to exit? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="review.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
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
	        <p>Are you sure you want to exitg? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="review.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
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
	        <p>Are you sure you want to exit? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="seat_booking.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
				<input type="submit" class="button_yes" name="home_yes" value="Yes">  
			</form>
			<a href="" class="button_cancel">Cancel</a>
	      </div>
	      <a href="" class="close"><span>Back to page</span></a>
	    </div>
	    <a href="" class="lightbox">Back to page</a>
	  </div>

	  <div id="cancel" class="pop-up">
	    <div class="popBox">
	      <div class="popScroll">
	        <p>Are you sure you want to cancel your booking? You will lose all details previously entered.</p><br><br>
	        <form method="post" action="review.php"<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
				<input type="submit" class="button_yes" name="cancel_yes" value="Yes">  
			</form>
			<a href="" class="button_cancel">No</a>
	      </div>
	      <a href="" class="close"><span>Back to page</span></a>
	    </div>
	    <a href="" class="lightbox">Back to page</a>
	  </div>

	</div>
	</main>
	<div id="main">
		<div class="titleheading">
			<h2><img class="multipagebar" src="./css/images/review_multipagebar.jpg" alt=""/></h2>
		</div>
	</div>
	<div class="text">Review</div>
	<div class="parallax"></div>
	<div id="main">
		<p><span class="error"></span></p>
		<div class="container">
			<h2>Current order status</h2>
			<h3>Billing Details:</h3>
			<p><?php 	
			echo ("Name: $billing_name $billing_lastname");
			echo "<br>";				
			echo ("Phone: $billing_phone");
			echo "<br>";
			echo ("Email: $billing_email");
			echo "<br><br>";	

			echo "<h3>Passenger Details:</h3>";

		  	while($row1 = mysqli_fetch_array($passenger_info)){
					echo "Name: " .$row1['first_name']." ".$row1["last_name"]."<br>";
					echo "Date of Birth: " .$row1["date_of_birth"]."<br>";
					echo "Seat: " .$row1["seat_number"]."<br>";
					echo "<br><br>";
					}
			echo ("Current total price: $$total");
			echo "<br><br>";
			
			?>
			</p>
			<?php
				if ($billing_id>$passenger_id){
					echo "Passengers still to enter under these billing details: " .$passengers_to_enter. "<br><br><br>";
					echo "<a href='passenger_details.php' class='button'>Add another passenger</a>";
					echo "      ";
					echo "<a href='#cancel' class='button'>Cancel Booking</a>";
				}
				else{
					echo "<a href='ticket.php' class='button'>Complete booking</a>";
					echo "      ";
					echo "<a href='#cancel' class='button'>Cancel Booking</a>";
				}


			?>
			<br>
			<br>
		</div>
	</div>



</body>
</html>
