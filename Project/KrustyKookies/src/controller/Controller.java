package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.KrustyView;
import model.Model;

public class Controller {
	
	private KrustyView view;
	private Model model;
	
	public Controller(Model model, KrustyView view) {
		this.view = view;
		this.model = model;
		
		view.addSearchListener(new SearchListener());		
	}
	
	class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String searchText = view.getSearchText();
			int selectedAction = view.getSelectedAction();
			switch(selectedAction) {
			case KrustyView.SEARCH_FOR_PALLET :
				String result = model.searchForPallet(searchText);
				view.updateSearchBox(result);
				break;
			case KrustyView.BLOCK_PALLET :
				String blockResult = model.blockPallet(searchText) ? searchText + " was blocked succesfully" : "Blocking failed";
				view.updateSearchBox(blockResult);
				break;
			case KrustyView.SEARCH_QUANTITY :
				String quantityResult = model.checkQuantity(searchText);
				view.updateSearchBox(quantityResult);
				break;
			}
		}
		
	}
}
