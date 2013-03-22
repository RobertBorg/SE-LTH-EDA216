package view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import se.datadosen.component.RiverLayout;

public class KrustyView extends JFrame {

	private static final long serialVersionUID = -7322546617726420651L;
	private JTextField inputText;
	private JButton searchButton;
	private JTextArea searchOutput, productionOutput;
	private String[] searchDescription = { "Search for pallet", "Block pallets", "Search quantity" };
	private JComboBox<String> searchCombo = new JComboBox<String>();
	
	
	public KrustyView() {
		JFrame content = new JFrame();
		content.setLayout(new RiverLayout());
		this.inputText = new JTextField("", 20);
		content.add("left", inputText);
		for(String s : searchDescription) {
			searchCombo.addItem(s);
		}
		content.add("tab", searchCombo);
		this.searchButton = new JButton("Search");
		content.add("tab", searchButton);

		GridLayout outputLayout = new GridLayout(0, 2);
		outputLayout.setHgap(10);
		JPanel outputPanel = new JPanel(outputLayout);
		content.add("p hfill vfill", outputPanel);
		
		searchOutput = new JTextArea();
		searchOutput.setEditable(false);
		TitledBorder searchTitle;
		searchTitle = BorderFactory.createTitledBorder("Search results");
		searchTitle.setTitleJustification(TitledBorder.RIGHT);
		searchOutput.setBorder(searchTitle);
		searchOutput.setLineWrap(true);
		outputPanel.add(new JScrollPane(searchOutput));
		productionOutput = new JTextArea();
		productionOutput.setEditable(false);
		TitledBorder productionTitle;
		productionTitle = BorderFactory.createTitledBorder("Production status");
		productionTitle.setTitleJustification(TitledBorder.RIGHT);
		productionOutput.setBorder(productionTitle);
		productionOutput.setLineWrap(true);
		outputPanel.add(new JScrollPane(productionOutput));
		
		content.setSize(800, 600);
		content.setTitle("Krusty Kookies");
		content.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content.setVisible(true);
		content.setResizable(false);
	}
	
	public void updateSearchBox(String result) {
		searchOutput.setText(result);
	}
	
	public void insertToProductionBox(String toInsert) {
		productionOutput.insert(toInsert + '\n', productionOutput.getLineCount() - 1);
	}
	
	public String getSearchText() {
		return inputText.getText();
	}
	
	public void addSearchListener(ActionListener actionListener) {
        searchButton.addActionListener(actionListener);
    }
	
	public void addComboBoxActionListener(ActionListener actionListener) {
		searchCombo.addActionListener(actionListener);	  
		//http://www.java2s.com/Code/Java/Swing-JFC/Usingdropdownlists.htm
	}
}
