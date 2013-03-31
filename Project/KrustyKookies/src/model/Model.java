package model;

public class Model {

	public Model() {
		
	}
	
	/**
	 * The assignment doesn't specify how we search, so do what suits you
	 * 
	 * @param input
	 * @return a string containing a nice description of the result (preferably
	 *         formatted in a nice way)
	 */
	public String searchForPallet(String input) {
		return "Search result";
	}
	
	/**
	 * Block certain pallet (containing a specific product produced during a
	 * specific time)
	 * 
	 * @param input
	 *            define it yourself... We might have to add another input field
	 *            to the GUI
	 * @return true if pallet was blocked, false otherwise
	 */
	public boolean blockPallet(String input) {
		return false;
	}
	
	
	/**
	 * Must be able to check how many pallets of a product have been produced
	 * during a specific time
	 * 
	 * @param input
	 *            The product to check for and a time period
	 * @return a string containing a nice description of the result (preferably
	 *         formatted in a nice way)
	 */
	public String checkQuantity(String input) {
		return "Quantity result";
	}
	
	/**
	 * Update storage quantities, used when materials run out.
	 * 
	 * @param material
	 */
	public void updateQuantity(String material) {
		
	}
	
	// In addition to these methods we need a way to simulate the production
	// process. You may change the parameters and output variables to something
	// more suitable if you wish to do so.
}
