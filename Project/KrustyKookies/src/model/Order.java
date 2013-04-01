package model;

import java.sql.Date;
import java.util.ArrayList;

public class Order {
	public int id;
	public Date requestedDeliveryDate;
	public ArrayList<ProductionOrder> productionOrders;
	
	public Order(int id, Date requestedDeliveryDate, ArrayList<ProductionOrder> productionOrders) {
		this.id = id;
		this.requestedDeliveryDate = requestedDeliveryDate;
		this.productionOrders = productionOrders;
	}
}
