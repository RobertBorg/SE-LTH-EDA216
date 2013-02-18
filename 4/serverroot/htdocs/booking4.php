<?php
	require_once('database.inc.php');
	
	session_start();
	$db = $_SESSION['db'];
	$userId = $_SESSION['userId'];
	$performanceId = $_SESSION['bookPerformanceId'] = $_POST['performanceId'];

	$db->openConnection();
	$booking = $db->tryMakeBooking($performanceId);
	$db->closeConnection();
?>

<html>
<head><title>Booking 4</title><head>
<body>
	<h1>Booking 4</h1>
	<p>
		<?php 
		if($booking == null) {
			print "unable to book ticket";
		} else {
			print "one ticked booked. Booking id: $booking";
		}
		?>
	</p>
</body>
</html>
