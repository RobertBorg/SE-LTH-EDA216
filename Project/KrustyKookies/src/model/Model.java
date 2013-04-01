package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class Model {

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

	/**
	 * The assignment doesn't specify how we search, so do what suits you (Do
	 * you want to include dates in this search?)
	 * 
	 * @param palletId
	 * @return a string containing a nice description of the result (preferably
	 *         formatted in a nice way)
	 */
	public Pallet searchForPallet(String palletId) {
		return new Pallet(dummyId, new Date(Calendar.getInstance().getTimeInMillis()));
	}
	
	public ArrayList<Pallet> searchForPallet(Date fromDate, Date toDate) {
		ArrayList<Pallet> pallets = new ArrayList<Pallet>();
		int id = 123;
		pallets.add(new Pallet(id, new Date(Calendar.getInstance().getTimeInMillis())));
		pallets.add(new Pallet(++id, new Date(Calendar.getInstance().getTimeInMillis())));
		pallets.add(new Pallet(++id, new Date(Calendar.getInstance().getTimeInMillis())));
		pallets.add(new Pallet(++id, new Date(Calendar.getInstance().getTimeInMillis())));
		return pallets;
	}
	
	public ArrayList<Pallet> searchForPallet(String recipeName, Date fromDate, Date toDate) {
		ArrayList<Pallet> pallets = new ArrayList<Pallet>();
		int id = 123;
		pallets.add(new Pallet(id, new Date(Calendar.getInstance().getTimeInMillis())));
		pallets.add(new Pallet(++id, new Date(Calendar.getInstance().getTimeInMillis())));
		pallets.add(new Pallet(++id, new Date(Calendar.getInstance().getTimeInMillis())));
		pallets.add(new Pallet(++id, new Date(Calendar.getInstance().getTimeInMillis())));
		return pallets;
	}
	
	public Customer getCustomerForPallet(Pallet pallet) {
		return new Customer("Finkakor AB", "Helsingborg");
	}

	/**
	 * Block certain pallets (containing a specific product produced during a
	 * specific time)
	 * 
	 * @param recipeName
	 * @param fromDate
	 * @param toDate
	 * @return true if pallet was blocked, false otherwise
	 */
	public boolean blockPallets(String recipeName, Date fromDate, Date toDate) {
		return false;
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
		return 0;
	}

	/**
	 * Update storage quantities, used when materials run out.
	 * 
	 * @param material
	 * @param quantity
	 */
	public void updateQuantity(RawMaterial material, int quantity) {
		
	}
	
	private int dummyId = 0;
	
	/**
	 * Returns the next order that production should start producing.
	 * 
	 * @return An ArrayList of ProductionOrders, null if there are no orders to
	 *         produce
	 */
	public Order getNextOrder() {
		//Just some dummy data
		if(dummyId % 2 != 0) {
			ArrayList<ProductionOrder> productionOrders = new ArrayList<ProductionOrder>();
			RawMaterial sugar = new RawMaterial("Sugar");
			RawMaterial potato = new RawMaterial("Potato");
			RawMaterial carrot = new RawMaterial("Carrot");
			Ingredient sugarIngredient = new Ingredient(100, sugar);
			Ingredient potatoIngredient = new Ingredient(5, potato);
			Ingredient carrotIngredient = new Ingredient(100, carrot);
			ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
			ingredients.add(sugarIngredient);
			ingredients.add(potatoIngredient);
			Recipe sweetPotatoes = new Recipe("Sweet potatoes", ingredients);
			ArrayList<Ingredient> carrotIngredients = new ArrayList<Ingredient>();
			carrotIngredients.add(carrotIngredient);
			Recipe carrotRecipe = new Recipe("Carrots", carrotIngredients);
			productionOrders.add(new ProductionOrder(sweetPotatoes));
			productionOrders.add(new ProductionOrder(carrotRecipe));
			Order order = new Order(dummyId, new Date(Calendar.getInstance().getTimeInMillis()), productionOrders);
			dummyId++;
			return order;
		} else if(dummyId == 2) {
			ArrayList<ProductionOrder> productionOrders = new ArrayList<ProductionOrder>();
			Order order = new Order(dummyId, new Date(Calendar.getInstance().getTimeInMillis()), productionOrders);
			dummyId++;
			return order;
		} else 
			dummyId++;
			return null;
	}
	
	/**
	 * Checks if there is enough RawMaterials in storage for the specified
	 * Ingredient.
	 * 
	 * @param ingredient
	 * @return true if there is enough, false otherwise
	 */
	public boolean isEnoughRawMaterials(Ingredient ingredient) {
		return dummyId % 2 != 0;
	}
	
	/**
	 * When a ProductionOrder has been produced this method is called.
	 * 
	 * @param order Not sure if needed, but I included it anyway
	 * @param productionOrder
	 */
	public void createPallet(Order order, ProductionOrder productionOrder) {
		
	}
}
