package model;

import java.sql.Date;

public class Pallet {

	public long id;
	public Date creationDateAndTime;
	public boolean isBlocked;
	public long orderId;
	public String recipeName;
	
	public Pallet(long id, Date creationDateAndTime, boolean isBlocked, long orderId, String recipeName) {
		this.id = id;
		this.creationDateAndTime = creationDateAndTime;
		this.isBlocked = isBlocked;
		this.orderId = orderId;
		this.recipeName = recipeName;
	}
	
}
