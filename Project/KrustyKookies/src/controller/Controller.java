package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import view.KrustyView;
import model.Model;
import model.Pallet;

public class Controller {

	private KrustyView view;
	private Model model;

	public Controller(Model model, KrustyView view) {
		this.view = view;
		this.model = model;
		view.addSearchListener(new SearchListener());
		view.addComboBoxActionListener(new ComboActionListener());
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
		if (checkInputBoxes()) {
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
				String searchText = view.getSearchText();
				int selectedAction = view.getSelectedAction();
				switch (selectedAction) {
				case KrustyView.SEARCH_FOR_PALLET:
					Pallet result = model.searchForPallet(searchText);
					view.updateSearchBox("Pallet with id: " + Integer.toString(result.id) + " found");
					break;
				case KrustyView.BLOCK_PALLET:
					boolean dateResult = validateDates();
					if (dateResult) {
						String blockResult = model.blockPallets(searchText,
								formatDate(view.getFromDate()),
								formatDate(view.getToDate())) ? searchText
								+ " was blocked succesfully"
								: "Blocking failed";
						view.updateSearchBox(blockResult);
					}
					break;
				case KrustyView.SEARCH_QUANTITY:
					boolean searchDateResult = validateDates();
					if (searchDateResult) {
						int quantityResult = model.checkQuantity(searchText,
								formatDate(view.getFromDate()),
								formatDate(view.getToDate()));
						view.updateSearchBox("Amount of pallets: " + Integer.toString(quantityResult));
					}
					break;
				}
			}
		}
	}

	class ComboActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			switch (view.getSelectedAction()) {
			case KrustyView.SEARCH_FOR_PALLET:
				view.SetDateEditable(false);
				break;
			case KrustyView.BLOCK_PALLET:
			case KrustyView.SEARCH_QUANTITY:
				view.SetDateEditable(true);
				break;
			}
		}
	}
}
