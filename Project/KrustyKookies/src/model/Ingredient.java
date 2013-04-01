package model;

public class Ingredient {
	public int quantity;
	public RawMaterial rawMaterial;
	
	public Ingredient(int quantity, RawMaterial rawMaterial) {
		this.quantity = quantity;
		this.rawMaterial = rawMaterial;
	}
}
