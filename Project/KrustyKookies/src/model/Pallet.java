package model;

import java.sql.Date;

public class Pallet {

	public int id;
	public Date creationDateAndTime;
	
	public Pallet(int id, Date creationDateAndTime) {
		this.id = id;
		this.creationDateAndTime = creationDateAndTime;
	}
	
}
