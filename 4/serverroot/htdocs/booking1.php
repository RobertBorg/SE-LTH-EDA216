<?php
	require_once('database.inc.php');
	
	session_start();
	$db = $_SESSION['db'];
	$userId = $_SESSION['userId'];
	$db->openConnection();
	
	$movies = $db->getMovies();
	$db->closeConnection();
?>

<html>
<head><title>Booking 1</title><head>
<body><h1>Booking 1</h1>
	<p>Current user: <?php print $userId ?></p>
	<p>
		Movies showing:
		<ul>
			<?php
				foreach ($movies as $movie) {
					print "<li><a href=\"booking2.php?movieId=$movie[id]\" >$movie[name]</a></li>";
				}
			?>
		</ul>
	</p>
</body>
</html>
