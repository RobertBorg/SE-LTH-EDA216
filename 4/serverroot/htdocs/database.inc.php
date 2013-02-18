<?php
/*
 * Class Database: interface to the movie database from PHP.
 *
 * You must:
 *
 * 1) Change the functions userExists and getMovieNames so the
 *    SQL queries are appropriate for your tables.
 * 2) Write more functions.
 *
 */
require_once("MDB2.php");
class Database {
	private $userName;
	private $password;
	private $database;
	private $conn;
	
	/**
	 * Constructs a database object for the specified user.
	 */
	public function __construct($userName, $password, $database) {
		$this->userName = $userName;
		$this->password = $password;
		$this->database = $database;
	}
	
	/** 
	 * Opens a connection to the database, using the earlier specified user
	 * name and password.
	 *
	 * @return true if the connection succeeded, false if the supplied
	 * user name and password were not recognized.
	 */
	public function openConnection() {
		$connectString = "mysqli://" . $this->userName . 
			":" . $this->password . "@puccini.cs.lth.se/" .
			$this->database;
		$this->conn = MDB2::connect($connectString);
		if (PEAR::isError($this->conn)) {
			$error = "Connection error: " . $this->conn->getMessage();
			print $error . "<p>";
			unset($this->conn);
			return false;
		}
		return true;
	}
	
	/**
	 * Closes the connection to the database.
	 */
	public function closeConnection() {
		if (isset($this->conn)) {
			$this->conn->disconnect();
			unset($this->conn);
		}
	}

	/**
	 * Checks if the connection to the database has been established.
	 *
	 * @return true if the connection has been established
	 */
	public function isConnected() {
		return isset($this->conn);
	}
	
	/**
	 * Execute a database query (or insert/update).
	 *
	 * @param The query string (SQL)
	 * @return The result set, or the number of affected rows on
	 * an insert/update
	 */
	private function executeQuery($query) {
		$result = $this->conn->query($query);
		if (PEAR::isError($result)) {
			$error = "*** Internal error: " . $result->getMessage() .
					"<p>" . $query;
			die($error);
		}
		return $result;
	}
	
	/**
	 * Check if a user with the specified user id exists in the database.
	 * Queries the Users database table.
	 *
	 * @param userId The user id 
	 * @return true if the user exists, false otherwise.
	 */
	public function userExists($userId) {
		$sql = "select username from Users where username = '$userId'";
		$result = $this->executeQuery($sql);
		$ret = $result->numRows() == 1; 
		$result->free();
		return $ret; 
	}
	
	/*
	 * Get the names of Movies that are currently showing. 
	 * Returns an array with the movieId as key and name as value
	 * Example: ret[123] = "King Kong"
	 */
	public function getMovies() {
		$sql = "select Movies.id, Movies.name " .
		"from Movies, Performances " .
		"where Performances.movieId = Movies.id ";
		$result = $this->executeQuery($sql);
		$ret = array();
		while ($row = $result->fetchRow()) {
			array_push($ret, array("id" => $row[0], "name" => $row[1] ) );
		}
		$result->free();
		return $ret;		
	}

	/*
	* Get the performance dates for the defined movie ID.
	* Returns an array with the performance ID as key and date as value.
	*/
	public function getPerformancesForMovie($movieId) {	
		$stmt = $conn->prepare("select Performances.id, Performances.theDate " .
		"from Performances, Movies " .
		"where Movies.id = Performances.id and Movies.id = ?");
		$result = $stmt->execute(array($movieId));
		$ret = array();
		while ($row = $result->fetchRow()) {
			array_push($ret, array("id" => $row[0], "date" => $row[1] ) );
		}
		$results->free();
		return $ret;
	}

	public function getMovieNameForMovie($movieId) {
		$stmt = $conn->prepare("select Movies.name " +
		"from Movies " +
		"where Movies.id = ?");
		$result = $stmt->execute(array($movieId));
		$movieName = "";
		while ($row = $result->fetchRow()) {
			$movieName = $row[0];
		}
		$results->free();
		return $movieName;
	}

	public function getMovieData($performanceId) {
		$stmt = $conn->prepare("select Performances.id, Movies.name, Performances.theDate, Theaters.name, Theaters.numberOfSeats " +
		"from Performances, Movies, Theaters " +
		"where Movies.id = Performances.id and Theaters.id = Performances.theaterId and Performances.id = ?");
		$result = $stmt->execute(array($performanceId));
		$ret = array();
		while ($row = $result->fetchRow()) {
			array_push($ret, array("id" => $row[0], "movieName" => $row[1], "date" => $row[2], "theaterName" => $row[3], "numberOfSeats" => $row[4] ) );
		}
		$results->free();
		return $ret;
	}
}
?>
