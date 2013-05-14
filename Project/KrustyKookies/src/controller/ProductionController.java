package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import model.Ingredient;
import model.Model;
import model.Order;
import model.Pallet;
import model.ProductionOrder;
import model.Recipe;
import view.KrustyView;

public class ProductionController {

	public static final int PRODUCTION = 0;
	public static final int FREEZING = 1;
	public static final int PACKAGING_IN_BAGS = 2;
	public static final int PACKAGING_IN_CARTONS = 3;
	public static final int LOADING_ON_PALLETS = 4;
	
	private Timer timer;
	private int currentStageInProduction = PRODUCTION;
	private KrustyView view;
	private Model model;
	private ProductionOrder currentProductionOrder;
	private Pallet currentPallet;
	private ArrayList<ProductionOrder> orders;
	private SimpleDateFormat sdf;
	private int currentProductionOrderNumber = Integer.MAX_VALUE;
	
	public ProductionController(int runTime, Model model, KrustyView view) {
		this.view = view;
		this.model = model;
		
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timer = new Timer();
		timer.schedule(new ProductionTask(), 0, (runTime / 5) * 1000 );

	}
	
	class ProductionTask extends TimerTask {

		@Override
		public void run() {
			Calendar cal = Calendar.getInstance();
			if(currentPallet == null) {
				currentPallet = model.getNextPalletToProduce();
				if(currentPallet == null) {
					view.insertToProductionBox(sdf.format(cal.getTime()) + " - No orders to produce");
					return;
				}
			}
			String message = sdf.format(cal.getTime()) + " - Order number " + Long.toString(currentPallet.orderId) + ", " ;
			switch (currentStageInProduction) {
			case PRODUCTION :
				//updateRawMaterialQuantities(currentPallet.recipeName);
				message += currentPallet.recipeName + " in production";
				currentStageInProduction++;
				break;
			case FREEZING :
				message += currentPallet.recipeName + " in freezing";
				currentStageInProduction++;
				break;
			case PACKAGING_IN_BAGS :
				message += currentPallet.recipeName + " in packaging in bags";
				currentStageInProduction++;
				break;
			case PACKAGING_IN_CARTONS :
				message += currentPallet.recipeName + " in packaging in cartons";
				currentStageInProduction++;
				break;
			case LOADING_ON_PALLETS :
				message += currentPallet.recipeName + " in loading on pallets";
				currentStageInProduction = PRODUCTION;
				model.createPallet(currentPallet);
				currentPallet = null;
				break;
			}
			view.insertToProductionBox(message);			
		}
		
		private void updateRawMaterialQuantities(Pallet pallet) {
				if(!model.isEnoughRawMaterials(pallet)) {
					int newQuantity = 100000;
//					model.updateQuantity(pallet.rawMaterial, newQuantity);
//					Calendar cal = Calendar.getInstance();
//					view.insertToProductionBox(sdf.format(cal.getTime()) + " - " + i.rawMaterial.name + " quantity restored to " + newQuantity + " units");
				}
		}
	}
	
}
