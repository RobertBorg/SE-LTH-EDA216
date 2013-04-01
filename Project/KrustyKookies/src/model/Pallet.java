package model;

import java.sql.Date;

public class Pallet {

	public int id;
	public Date creationDateAndTime;
	public boolean isBlocked;
	
	public Pallet(int id, Date creationDateAndTime, boolean isBlocked) {
		this.id = id;
		this.creationDateAndTime = creationDateAndTime;
		this.isBlocked = isBlocked;
	}
	
}
