package main;

import controller.Controller;
import controller.ProductionController;
import view.KrustyView;
import model.Model;

public class Main {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Model model = new Model();
		model.openConnection("db53", "nmn966ez"); // this is no a error!
		KrustyView view = new KrustyView();
		Controller controller = new Controller(model, view);
		ProductionController production =new ProductionController(5, model, view);
	}
}
