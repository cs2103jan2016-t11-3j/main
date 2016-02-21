package logic.help;

import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class HelpFunction {
	
	// Messages used within the function
	private static final String MESSAGE_NO_TOPIC = "Sorry, the topic you requested is not available";
	
	// Number of topics in user manual
	private static final int MAX_TOPIC_NUMBER = 13;
	
	// Attributes contained in the helpFunction class
	private ArrayList<HelpTopic> manual = new ArrayList(); // Entire manual will be stored inside
	private ArrayList<String> display = new ArrayList();
	private String searchKey = "";
	
	// Constructors
	public HelpFunction() {
		importManual(manual);
	}
	
	public HelpFunction(String line) {
		searchKey = line;
		importManual(manual);
	}
	
	// Getters and Setters;
	public ArrayList<HelpTopic> getManual() {
		return manual;
	}
	
	public ArrayList<String> getDisplay() {
		return display;
	}
	
	public String getSearchKey() {
		return searchKey;
	}
	
	public void setSearchKey(String line) {
		searchKey = line;
	}
	
	// Class methods
	public void importManual(ArrayList<HelpTopic> manual) {
		for(int i = 1; i <= MAX_TOPIC_NUMBER; i++) {
			manual.add(new HelpTopic(i));
		}
	}
}
