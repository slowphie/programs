<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Ticket</title>
  <link rel="stylesheet" href="css/style.css">
  <link href='https://fonts.googleapis.com/css?family=Raleway' rel='stylesheet'>

</head>
<body>
  <?php
    require("connect.php");

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

    $billing_info = mysqli_query($con, "SELECT * FROM `billing_details` WHERE `id`>'$min_id'");


    $passenger_info = mysqli_query($con, "SELECT * FROM passenger INNER JOIN billing_details ON passenger.id_passenger=billing_details.id WHERE id_passenger>'$min_id'");


    $sql = mysqli_query($con, "SELECT SUM(`price`) AS total_price FROM billing_details WHERE `phone`='$billing_phone'");
    $row = mysqli_fetch_array($sql);
    $total = $row["total_price"];



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
  <div id="main">
    <div class="titleheading">
      <h2><img class="multipagebar" src="./css/images/complete_multipagebar.jpg" alt=""/></h2>
    </div>
  </div>
  <div class="text">Order complete</div>
  <div class="parallax"></div>
  <div id="main">
    <p><span class="error"></span></p>
    <div class="container">
      <h2>Order complete</h2>
      Your order has been successfully completed.<br>
      
      <h3>Flight Details</h3>
      Date of Flight: 2017-11-25 <br>
      Flight number: VA087 <br>
      Gate: 13 <br>
      Boarding time: 15:40 <br>
      Departure time: 16:00<br>
      From: The Airport<br>
      To: Italy<br>
      Airplane model: Aereo 40<br><br>


      <h3>Billing Details</h3>

      <?php

      echo ("Name: $billing_name $billing_lastname <br>");     
      echo ("Phone: $billing_phone <br>");
      echo ("Email: $billing_email <br>");
      echo ("Total Price: $$total");



      echo "<br><br><h3>Passenger Details</h3>";
      while($row1 = mysqli_fetch_array($passenger_info)){
          echo "Name: " .$row1['first_name']." ".$row1["last_name"]."<br>";
          echo "Date of Birth: " .$row1["date_of_birth"]."<br>";
          echo "Seat: " .$row1["seat_number"]."<br>";
          echo "Ticket price: $" .$row1['price']."<br>";
          echo "<br><br>";
        }

      echo "Your printable tickets will be emailed to the following address: $billing_email <br><br>";

      ?>
      


      <h4>Thank you for choosing Volare Airlines. We hope you enjoy your flight.</h4><br><br>
      <a href='home_page.php' class='button'>Return to Home Page</a>
      <br>
  </div>


</body>
</html>