//@@author A0124052X

package logic.help;

import java.util.ArrayList;

/**
 * Creates a Help object to display the user guide. There are two ways which the
 * manual can be displayed: <br>
 * 1) Entire user guide - The user does not key in any search keys. Entire
 * manual will be transferred to the output ArrayList for displaying <br>
 * 2) Parts related to the topic of interest - Based on the search keys entered
 * by the user, the program searches for topics related and displays all
 * sections related to it. If the topic of interest does not exist, an error
 * message will be returned to the user will be returned through the same output
 * channel.
 * 
 * @author ChongYan
 *
 */
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
	private void importManual() {
		for (int i = 1; i <= MAX_TOPIC_NUMBER; i++) {
			manual.add(new HelpTopic(i));
		}
	}

	/**
	 * Main method in Help which copies all information related to the topic
	 * requested, or all information if no topics were requested, to the output
	 * ArrayList
	 * 
	 * @return output: ArrayList<String>
	 */
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

	private void createDisplay(int num) {
		int i = 0;
		String text = "";
		display.add(manual.get(num).getTopicName());
		while (i < manual.get(num).getDetails().size()) {
			text = manual.get(num).getDetails().get(i);
			display.add(text);
			i++;
		}
	}

	private void runSearch() {
		String name;
		for (int i = 0; i < MAX_TOPIC_NUMBER; i++) {
			name = manual.get(i).getTopicName();
			if (name.toUpperCase().contains(searchKey.toUpperCase())) {
				createDisplay(i);
			}
		}
	}
}
