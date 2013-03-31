package model;

import java.sql.Date;

public class Model {

	/**
	 * // In addition to these methods we need a way to simulate the production
	 * process. You may change the parameters and output variables to something
	 * more suitable if you wish to do so. These methods are only called if
	 * every required field is filled in and the dates are correct (follow the
	 * format and beforeDate is actually before toDate). The "input" Strings are
	 * not validated in any way (except for that they exist), so you can leave
	 * it to me (or do it by yourself) when you have defined the correct format.
	 */
	public Model() {

	}

	/**
	 * The assignment doesn't specify how we search, so do what suits you (Do
	 * you want to include dates in this search?)
	 * 
	 * @param input
	 * @return a string containing a nice description of the result (preferably
	 *         formatted in a nice way)
	 */
	public String searchForPallet(String input) {
		return input.trim() + " found!";
	}

	/**
	 * Block certain pallet (containing a specific product produced during a
	 * specific time)
	 * 
	 * @param input
	 *            define it yourself...
	 * @param fromDate
	 * @param toDate
	 * @return true if pallet was blocked, false otherwise
	 */
	public boolean blockPallet(String input, Date fromDate, Date toDate) {
		return false;
	}

	/**
	 * Must be able to check how many pallets of a product have been produced
	 * during a specific time
	 * 
	 * @param input
	 *            The product to check for
	 * @param fromDate
	 * @param toDate
	 * @return a string containing a nice description of the result (preferably
	 *         formatted in a nice way)
	 */
	public String checkQuantity(String input, Date fromDate, Date toDate) {
		return "Quantity for: " + input.trim() + ", " + fromDate + " - " + toDate + " is 0";
	}

	/**
	 * Update storage quantities, used when materials run out.
	 * 
	 * @param material
	 */
	public void updateQuantity(String material) {

	}
}
