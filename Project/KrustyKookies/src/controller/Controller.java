package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import view.KrustyView;
import model.Customer;
import model.Model;
import model.Pallet;
import model.Recipe;

public class Controller {

	private KrustyView view;
	private Model model;

	public Controller(Model model, KrustyView view) {
		this.view = view;
		this.model = model;
		view.addSearchListener(new SearchListener());
		view.addBlockedButtonListener(new BlockedListener());
		view.addNotBlockedButtonListener(new UnBlockedListener());
	}

	private boolean validateDates() {
		Date fromDate = formatDate(view.getFromDate());
		Date toDate = formatDate(view.getToDate());
		if (fromDate == null || toDate == null) {
			view.showErrorDialog("Invalid date, should have the format: YYYY-MM-DD and be within bounds.");
			return false;
		} else if (fromDate.getTime() > toDate.getTime()) {
			view.showErrorDialog("Impossible to reverse time!");
			return false;
		}
		return true;
	}

	private boolean validateInput() {
		if (checkInputBoxes() || view.getSelectedAction() == KrustyView.SEARCH_FOR_PALLET) {
			return true;
		} else {
			view.showErrorDialog("Please fill all input fields.");
			return false;
		}
	}

	private boolean checkInputBoxes() {
		String searchText = view.getSearchText().trim();
		if (view.getSelectedAction() == KrustyView.SEARCH_FOR_PALLET) {
			return searchText.length() != 0;
		} else {
			String fromDate = view.getFromDate().trim();
			String toDate = view.getToDate().trim();
			return !(searchText.length() == 0 || fromDate.length() == 0
					|| toDate.length() == 0);
		}
	}

	private Date formatDate(String date) {
		String[] dateSplit = date.split("-");
		if (dateSplit.length == 3) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.setLenient(false);
				cal.set(Calendar.YEAR, Integer.parseInt(dateSplit[0]));
				cal.set(Calendar.MONTH, Integer.parseInt(dateSplit[1]) - 1);
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[2]));
				Date dateRepresentation = new Date(cal.getTimeInMillis());
				return dateRepresentation;
			} catch (NumberFormatException e) {
				return null;
			} catch (ArrayIndexOutOfBoundsException e) {
				return null;
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}

	class SearchListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (validateInput()) {
				int selectedAction = view.getSelectedAction();
				String searchText = view.getSearchText();
				switch (selectedAction) {
				case KrustyView.SEARCH_FOR_PALLET:
					searchForPallets(searchText, view.getFromDate(), view.getToDate());
					break;
				case KrustyView.BLOCK_PALLET:
					if (validateDates()) {
						int numBlocked=  model.blockPallets(searchText,
								formatDate(view.getFromDate()),
								formatDate(view.getToDate()));
						view.updateSearchBox(String.format("Blocked %d pallets", numBlocked));
						
					}
					break;
				case KrustyView.SEARCH_QUANTITY:
					if (validateDates()) {
						int quantityResult = model.checkQuantity(searchText,
								formatDate(view.getFromDate()),
								formatDate(view.getToDate()));
						view.updateSearchBox(Integer.toString(quantityResult)
								+ " pallets of "
								+ searchText
								+ " has been produced during the time period between "
								+ view.getFromDate() + " and "
								+ view.getToDate());
					}
					break;
				}
			}
		}
	}
	
	private void searchForPallets(String searchText, String fromDate, String toDate) {
		//Search for pallet with id
		if(searchText.length() == 0 && fromDate.length() == 0 && toDate.length() == 0) {
			view.showErrorDialog("Please fill all input fields.");
		}else if(fromDate.length() == 0 && toDate.length() == 0) {	
			//Search for id
			Pallet result = null;
			try {
				int palletId = Integer.parseInt(searchText);
				result = model.searchForPallet(palletId + "");
				view.updateSearchBox(produceOutputForPallet(result, "found"));
			} catch (NumberFormatException e ) {
				//Search for recipe
				ArrayList<Pallet> pallets = model.searchForPallet(searchText, null, null);
				produceOutputForPallets(pallets, "found");
			}
			return;
			//Search for pallets produced during a specific time interval
		} else if(searchText.length() == 0 && fromDate.length() != 0 && toDate.length() != 0) {
			if(validateDates()) {
				ArrayList<Pallet> pallets = model.searchForPallet(formatDate(fromDate), formatDate(toDate));
				produceOutputForPallets(pallets, "found");
			}
			//Search for a specific product (recipe) produced during a specific time interval
		} else if(searchText.length() != 0 && fromDate.length() != 0 && toDate.length() != 0) {
			if(validateDates()) {
				ArrayList<Pallet> pallets = model.searchForPallet(searchText, formatDate(fromDate), formatDate(toDate));
				produceOutputForPallets(pallets, "found");
			}
		} else if(fromDate.length() != 0 || toDate.length() != 0) {
			validateDates();
		} else {
			view.showErrorDialog("Please make up your mind.");
		}
	}
	
	private void produceOutputForPallets(ArrayList<Pallet> pallets, String action) {
		if(!pallets.isEmpty()) {
			String resultString = "";
			for(Pallet p : pallets)
				resultString += produceOutputForPallet(p, action);
			view.updateSearchBox(resultString);
		} else {
			view.updateSearchBox("Nothing " + action);
		}
	}
	
	private String produceOutputForPallet(Pallet pallet, String action) {
		String toReturn;
		if(pallet != null) {
			toReturn = "Pallet with id: " + Long.toString(pallet.id) + " " + action + "\n";
			Customer customer = model.getCustomerForPallet(pallet);
			Recipe recipe = model.getRecipeForPallet(pallet);
			String recipeName = recipe != null ? recipe.name : "null";
			toReturn += "Product: " + recipeName + '\n';
			toReturn += "Blocked: " + pallet.isBlocked + '\n';
			String customerName = customer != null ? customer.name : "null";
			String customerAddress = customer != null ? customer.address : "null";
			toReturn += "Customer name: " + customerName + '\n';
			toReturn += "Address: " + customerAddress + "\n\n";
		} else {
			toReturn = "Nothing found";
		}
		return toReturn;
	}	
	
	class BlockedListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ArrayList<Pallet> pallets = model.getPalletsWithBlockStatus(true);
			produceOutputForPallets(pallets, "found");
		}
		
	}
	
	class UnBlockedListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Pallet> pallets = model.getPalletsWithBlockStatus(false);
			produceOutputForPallets(pallets, "found");
		}
		
	}
}
