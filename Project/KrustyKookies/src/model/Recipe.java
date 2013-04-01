package model;

import java.util.ArrayList;

public class Recipe {
	public ArrayList<Ingredient> ingredients;
	public String name;
	
	public Recipe(String name, ArrayList<Ingredient> ingredients) {
		this.name = name;
		this.ingredients = ingredients;
	}
}
