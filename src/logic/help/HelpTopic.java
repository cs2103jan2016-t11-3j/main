package logic.help;

import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class HelpTopic {

	// Attributes of the HelpTopic class
	private int number = 0;
	private String name = "";
	private ArrayList<String> details = new ArrayList<String> ();
	
	// Constructors
	public HelpTopic() {
		
	}
	
	public HelpTopic(int _number) {
		number = _number;
		File topicFile = new File(number + ".txt");
		readFile(topicFile);
	}
	
	// Helper function for constructor
	public void readFile(File topicFile) {
		String text = "";
		try {
			FileReader fileReader = new FileReader(topicFile);
			BufferedReader reader = new BufferedReader(fileReader);
			name = reader.readLine();
			text = reader.readLine();
			while (text != null) {
				details.add(text);
				text = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// Getters and setters
	public int getTopicNumber() {
		return number;
	}
	
	public String getTopicName() {
		return name;
	}
	
	public ArrayList<String> getDetails() {
		return details;
	}
	
	public void setTopicNumber(int _number) {
		number = _number;
	}
	
	public void setTopicName(String _name) {
		name = _name;
	}
}
