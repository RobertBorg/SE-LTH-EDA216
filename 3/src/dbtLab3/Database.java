package dbtLab3;

import java.util.ArrayList;
import java.sql.*;

import datatypes.SingleObjectHolder;

/**
 * Database is a class that specifies the interface to the movie database. Uses
 * JDBC and the MySQL Connector/J driver.
 */
public class Database {
	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * An SQL statement object.
	 */
	private Statement stmt;

	/**
	 * Create the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Open a connection to the database, using the specified user name and
	 * password.
	 * 
	 * @param userName
	 *            The user name.
	 * @param password
	 *            The user's password.
	 * @return true if the connection succeeded, false if the supplied user name
	 *         and password were not recognized. Returns false also if the JDBC
	 *         driver isn't found.
	 */
	public boolean openConnection(String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://puccini.cs.lth.se/" + userName, userName,
					password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Close the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
		}
		conn = null;
	}

	/**
	 * Check if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public ArrayList<SingleObjectHolder<String>> getMovies() {
		ArrayList<SingleObjectHolder<String>> toReturn = new ArrayList<SingleObjectHolder<String>>();
		String sql = "select Movies.id, Movies.name " +
		"from Movies";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				toReturn.add(new SingleObjectHolder<String>(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
		return toReturn;
	}
	
	public ArrayList<SingleObjectHolder<String>> getPerformanceDates(String movieName) {
		ArrayList<SingleObjectHolder<String>> toReturn = new ArrayList<SingleObjectHolder<String>>();
		String sql = "select Performances.id, Performances.theDate " +
		"from Performances, Movies " +
		"where Movies.id = Performances.id and Movies.name = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String date = rs.getString("theDate");
				toReturn.add(new SingleObjectHolder<String>(id, date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
		return toReturn;
	}
	
	public ArrayList<SingleObjectHolder<String>> getMovieData(String movieName, String date) {
		ArrayList<SingleObjectHolder<String>> toReturn = new ArrayList<SingleObjectHolder<String>>();
		String sql = "select Performances.id, Movies.name, Performances.theDate, Theaters.name, Theaters.numberOfSeats " +
		"from Performances, Movies, Theaters " +
		"where Movies.id = Performances.id and Movies.name = ? and Theaters.id = Performances.id and Performances.theDate = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, movieName);
			ps.setString(2, date);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("Performances.id");
				String otherDate = rs.getString("Performances.theDate");
				String otherMovieName = rs.getString("Movies.name");
				String otherTheater = rs.getString("Theaters.name");
				String otherFreeSeats = Integer.toString(getNumberOfAvailableSeats(otherDate, otherMovieName));
				toReturn.add(new SingleObjectHolder<String>(id, otherMovieName, otherDate, otherTheater, otherFreeSeats));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
		return toReturn;
	}
	
	public boolean login(String inUsername){
		String sql = "select username " +
		"from Users " +
		"where username LIKE ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, inUsername);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String username = rs.getString("username");
				if( username.equalsIgnoreCase(inUsername)){
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}
	
	public String tryMakeReservation(String date, String movieName, String username) {
		Savepoint savepoint = null;
		try {
			savepoint = conn.setSavepoint();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(savepoint == null){
			return null;
		}
		
		if(getNumberOfAvailableSeats(date, movieName) > 0){
			
			int rNum =  makeReservation(date, movieName, username);
			if(rNum >= 0){
				return Integer.toString(rNum);
			} else {
				try {
					conn.rollback(savepoint);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			try {
				conn.rollback(savepoint);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private int getNumberOfAvailableSeats(String date, String movieName) {
		String sql = "select " +
				"(Select numberofseats " +
				"from theaters, performances,movies " +
				"where theaters.id = performances.theaterId and performances.movieId = movies.id and performances.thedate = ? and movies.name like ?)" +
				"-" +
				"(Select count(reservationNumber) as numReserved " +
				"from Movies,Performances,Reservations " +
				"where Reservations.performanceId = Performances.id and Performances.movieId = Movies.id and Performances.thedate = ? and Movies.name LIKE ?)";
		
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, movieName);
			ps.setString(3, date);
			ps.setString(4, movieName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
		return -1;
	}
	
	private int makeReservation(String date, String movieName, String username) {
		String sql = "insert into Reservations(performanceId, userId) " +
		"select Performances.id as performanceId, Users.id as userId " +
		"from Performances, Users, Movies " +
		"where Performances.theDate = ? and Performances.movieId = Movies.id and Movies.name = ? and Users.username LIKE ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, movieName);
			ps.setString(3, username);
			int n = ps.executeUpdate();
			if(n == 1){
				ps.close();
				ps = conn.prepareStatement("Select LAST_INSERT_ID() from Reservations");
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) ps.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
		return -1;
	}
}
