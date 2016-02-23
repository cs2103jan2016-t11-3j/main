package logic.help;

import java.util.ArrayList;

public class Help {

	// Messages used within the function
	public static final String MESSAGE_NO_TOPIC = "Sorry, the topic you requested is not available";

	// Number of topics in user manual
	private static final int MAX_TOPIC_NUMBER = 13;

	// Attributes contained in the helpFunction class
	private ArrayList<HelpTopic> manual = new ArrayList<HelpTopic>();
	// Entire manual will be stored in this once a new object is called
	private ArrayList<String> display = new ArrayList<String>();
	private String searchKey = "";

	// Constructors
	public Help() {
		importManual();
	}

	public Help(String line) {
		searchKey = line;
		importManual();
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

	// Imports manual from text files using objects called HelpTopics
	public void importManual() {
		for (int i = 1; i <= MAX_TOPIC_NUMBER; i++) {
			manual.add(new HelpTopic(i));
		}
	}

	// If a searchKey is absent, method automatically moves everything from the
	// manual over to
	// the display arrayList. Else it will search for all the topics containing
	// the searchKey.
	// If searchKey does not match with any help topic, method returns a message
	// contained in
	// the arrayList
	public ArrayList<String> run() {
		if (searchKey == "") {
			for (int i = 0; i < MAX_TOPIC_NUMBER; i++) {
				createDisplay(i);
			}
			return display;
		} else {
			runSearch();
			if (display.isEmpty()) {
				display.add(MESSAGE_NO_TOPIC);
			}
			return display;
		}
	}

	public void createDisplay(int num) {
		int i = 0;
		String text = "";
		display.add(manual.get(num).getTopicName());
		while (i < manual.get(num).getDetails().size()) {
			text = manual.get(num).getDetails().get(i);
			display.add(text);
			i++;
		}
	}

	public void runSearch() {
		String name;
		for (int i = 0; i < MAX_TOPIC_NUMBER; i++) {
			name = manual.get(i).getTopicName();
			if (name.contains(searchKey)) {
				createDisplay(i);
			}
		}
	}
}
