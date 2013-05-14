package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sound.midi.Receiver;

public class Model {

	private Connection conn;
	/**
	 * You may change the parameters and output variables to something
	 * more suitable if you wish to do so. These methods are only called if
	 * every required field is filled in and the dates are correct (follow the
	 * format and beforeDate is actually before toDate). The "input" Strings are
	 * not validated in any way (except for that they exist), so you can leave
	 * it to me (or do it by yourself) when you have defined the correct format.
	 */
	public Model() {
		
	}
	
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
	
	public void reinit() {
		String sql = 
			"SET foreign_key_checks = 0; " +
			"DROP TABLE IF EXISTS RawMaterials; " + 
			"DROP TABLE IF EXISTS Recipes; " +
			"DROP TABLE IF EXISTS Pallets; " +
			"DROP TABLE IF EXISTS Orders; " +
			"DROP TABLE IF EXISTS Customers; " +
			"DROP TABLE IF EXISTS Ingredients; " +
			"DROP TABLE IF EXISTS Shipments; " +
			"-- Create the tables. " +
			"create table RawMaterials ( "+
			"name char(40) PRIMARY KEY "+
			"); "+
			
			"create table Recipes ( "+
			"name char(100) PRIMARY KEY "+
			"); "+
			
			"create table Ingredients ( "+
			"quantity int NOT NULL, "+
			"rawMaterialName char(40) NOT NULL, "+
			"recipeName char(100) NOT NULL, "+
			"FOREIGN KEY(rawMaterialName) "+
			"  REFERENCES RawMaterials(name), "+
			"FOREIGN KEY(recipeName) "+
			"  REFERENCES Recipes(name), "+
			"CONSTRAINT uniqueNames UNIQUE (rawMaterialName,recipeName) "+
			"); "+
			
			"create table Pallets ( "+
			"id int PRIMARY KEY AUTO_INCREMENT, "+
			"orderId int, "+
			"creationDateAndTime datetime, "+
			"recipeName char(100) NOT NULL, "+
			"shipmentId int, "+
			"isBlocked boolean, "+
			"FOREIGN KEY (orderId) "+
			"  REFERENCES Orders(id), "+
			"FOREIGN KEY (recipeName) "+
			"  REFERENCES Recipes(name), "+
			"FOREIGN KEY (shipmentId) "+
			"  REFERENCES Shipments(id) "+
			"); "+
			
			"create table Orders ( "+
			"id int PRIMARY KEY AUTO_INCREMENT, "+
			"requestedDeliveryDate date NOT NULL, "+
			"customerName char(100) NOT NULL, "+
			"FOREIGN KEY (customerName) "+
			"  REFERENCES Customers(name) "+
			"); "+
			
			"create table Customers ( "+
			"name char(100) PRIMARY KEY UNIQUE, "+
			"address char(100) NOT NULL "+
			"); "+
			
			"create table Shipments ( "+
			"id int PRIMARY KEY AUTO_INCREMENT "+
			"); "+
			
			"SET foreign_key_checks = 1; ";

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stmt.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(stmt != null) stmt.close();
			} catch(SQLException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * The assignment doesn't specify how we search, so do what suits you (Do
	 * you want to include dates in this search?)
	 * 
	 * @param palletId
	 * @return a string containing a nice description of the result (preferably
	 *         formatted in a nice way). Returns null if nothing found.
	 */
	public Pallet searchForPallet(String palletId) {
		String sql = 
				"Select * from Pallets where id = ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, palletId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pallet rtn = null;;
		try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					rtn = extractPallet(rs);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		return rtn;
	
	}

	private Pallet extractPallet(ResultSet rs) throws SQLException {
		Pallet rtn;
		Date date = rs.getDate("creationDateAndTime");
		String recipeName = rs.getString("recipeName");
		long shipmentId = rs.getLong("shipmentId");
		boolean isBlocked = rs.getBoolean("isBLocked");
		rtn = new Pallet(rs.getLong("id"), date, isBlocked,rs.getLong("orderId"),recipeName);
		return rtn;
	}
	
	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return A list containing the found pallets. Returns an empty list if no pallets were found.
	 */
	public ArrayList<Pallet> searchForPallet(Date fromDate, Date toDate) {
		ArrayList<Pallet> pallets = new ArrayList<Pallet>();

		String sql = 
				"Select * from Pallets where creationDateAndTime BETWEEN ? and ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setDate(1, fromDate);
			ps.setDate(2, toDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					pallets.add(extractPallet(rs));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		return pallets;
	}
	
	/**
	 * 
	 * @param recipeName
	 * @param fromDate
	 * @param toDate
	 * @return A list containing the found pallets. Returns an empty list if no pallets were found.
	 */
	public ArrayList<Pallet> searchForPallet(String recipeName, Date fromDate, Date toDate) {
		ArrayList<Pallet> pallets = new ArrayList<Pallet>();

		String sql = 
				"Select * from Pallets where recipeName LIKE ? and creationDateAndTime BETWEEN ? and ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, recipeName);
			ps.setDate(2, fromDate);
			ps.setDate(3, toDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					pallets.add(extractPallet(rs));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		return pallets;
	}
	
	public Customer getCustomerForPallet(Pallet pallet) {
		String sql = 
				"Select name, address "+
				"from Orders,Customers "+
				"where Orders.customerName = name and Orders.id = ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setLong(1, pallet.orderId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Customer rtn = null;
		try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					rtn = new Customer(rs.getString("name"),rs.getString("address"));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		return rtn;
	}
	
	public Recipe getRecipeForPallet(Pallet pallet) {
		String sql = 
				"Select quantity, rawMaterialName, recipeName "+
				"from Ingredients "+
				"where Ingredients.recipeName like ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, pallet.recipeName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Recipe rtn = null;
		try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					if (rtn == null) {
						rtn = new Recipe(rs.getString("recipeName"), new ArrayList<Ingredient>());
					}
					rtn.ingredients.add(new Ingredient(rs.getInt("quantity"),new RawMaterial(rs.getString("rawMaterialName"))));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		return rtn;
	}

	/**
	 * Block certain pallets (containing a specific product produced during a
	 * specific time)
	 * 
	 * @param recipeName
	 * @param fromDate
	 * @param toDate
	 * @return an int representing the number of blocked pallets in the date range
	 */
	public int blockPallets(String recipeName, Date fromDate, Date toDate) {
		String sql = 
				"update Pallets "+
				"SET isBlocked = true "+
				"where recipeName LIKE ? and creationDateAndTime BETWEEN ? and ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, recipeName);
			ps.setDate(2, fromDate);
			ps.setDate(3, toDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rtn = 0;
		try {
			rtn = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rtn;
	}

	/**
	 * Must be able to check how many pallets of a product have been produced
	 * during a specific time
	 * 
	 * @param recipeName
	 *            The product to check for
	 * @param fromDate
	 * @param toDate
	 * @return The amount of pallets produced
	 */
	public int checkQuantity(String recipeName, Date fromDate, Date toDate) {
		String sql = 
				"Select count(*) "+
				"from Pallets "+
				"where recipeName like ? and creationDateAndTime between ? and ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, recipeName);
			ps.setDate(2, fromDate);
			ps.setDate(3, toDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rtn = 0;
		try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					rtn = rs.getInt(1);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		return rtn;
	}

	/**
	 * Update storage quantities, used when materials run out.
	 * 
	 * @param material
	 * @param quantity
	 */
	public void updateQuantity(RawMaterial material, int quantity) {
		
	}
	
	/**
	 * Returns the next order that production should start producing.
	 * 
	 * @return An ArrayList of ProductionOrders, null if there are no orders to
	 *         produce
	 */
	public Pallet getNextPalletToProduce() {
		String sql = 
		"select Pallets.*"+
		"from Pallets, Orders "+
		"where Pallets.orderId = Orders.id and Pallets.creationDateAndTime is NULL "+
		"ORDER BY requestedDeliveryDate ASC "+
		"LIMIT 1 ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Pallet rtn = null;
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rtn = extractPallet(rs);
				
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return rtn;
	}
	
	/**
	 * Checks if there is enough RawMaterials in storage for the specified
	 * Ingredient.
	 * 
	 * @param ingredient
	 * @return true if there is enough, false otherwise
	 */
	public boolean isEnoughRawMaterials(Ingredient ingredient) {
		return true;
	}
	
	/**
	 * When a ProductionOrder has been produced this method is called.
	 * 
	 * @param order Not sure if needed, but I included it anyway
	 * @param productionOrder
	 */
	public void createPallet(Pallet p) {
		String sql = 
				"update Pallets "+
				"SET creationDateAndTime = ? "+
				"where id = ? ";

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
			ps.setLong(2, p.id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			int rs = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Pallet> getPalletsWithBlockStatus(boolean status) {
		ArrayList<Pallet> toReturn = new ArrayList<Pallet>();
		return toReturn;
	}
}
