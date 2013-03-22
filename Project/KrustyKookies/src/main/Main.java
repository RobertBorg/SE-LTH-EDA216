package main;

import controller.Controller;
import view.KrustyView;
import model.Model;

public class Main {
	public static void main(String[] args) {
		Model model = new Model();
		KrustyView view = new KrustyView();
		Controller controller = new Controller(model, view);
	}
}
