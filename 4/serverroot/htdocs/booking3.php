<?php
	require_once('database.inc.php');
	
	session_start();
	$db = $_SESSION['db'];
	$userId = $_SESSION['userId'];
	$performanceId = $_SESSION['performanceId'] = $_GET['performanceId'];
e
	$db->openConnection();
	$performance = $db->getPerformance($performanceId);
	$db->closeConnection();
?>

<html>
<head><title>Booking 3</title><head>
<body><h1>Booking 3</h1>
	<p>Current user: <?php print $userId ?></p>
	<p>Data for selected performance:</p>
	<p>
		<ul>
			<li>Movie: <?php print $performance['movieName'] ?></li>
			<li>Date: <?php print $performance['date'] ?></li>
			<li>Theater: <?php print $performance['theaterName'] ?></li>
			<li>Free seats: <?php print $perforamnce['availableSeats'] ?></li>
		</ul>
	</p>
	<p>
		<form action="booking4.php?performanceId=<?php print $performanceId ?>" method="POST">
			<input type="submit" value="Book ticket">
		</form>
	</p>
</body>
</html>
