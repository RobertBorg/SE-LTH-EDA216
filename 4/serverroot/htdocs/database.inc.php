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
		$sql = "select userId from Users where userId = '$userId'";
		$result = $this->executeQuery($sql);
		$ret = $result->numRows() == 1; 
		$result->free();
		return $ret; 
	}

	/** 
	 * Get the names of movies that are currently showing. Queries
	 * the Performances database table.
	 *
	 * @return The array of movie names.
	 */
	public function getMovieNames() {
		$sql = "select distinct movieName from Performances"; 
		$result = $this->executeQuery($sql);
		$ret = array();
		while ($row = $result->fetchRow()) {
			$ret[] = $row[0];
		}
		$result->free();
		return $ret;
	}
	
	/*
	 * *** Add functions ***
	 */
}
?>
