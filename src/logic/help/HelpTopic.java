package logic.help;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpTopic {

	// File Path
	public static String FILE_PATH = "/src/logic/help/";
	
	// Attributes of the HelpTopic class
	private int number = 0;
	private String name = "";
	private ArrayList<String> details = new ArrayList<String> ();
	
	// Constructors
	public HelpTopic() {
		
	}
	
	public HelpTopic(int _number) {
		String num;
		number = _number;
		num = Integer.toString(number);
		String topicFile = constructFile(num);
		readFile(topicFile);
	}
	
	public String constructFile(String num) {
		String helpFilePath = num + ".txt";
		return helpFilePath;
	}
	// Helper function for constructor
	public void readFile(String topicFile) {
	    InputStream is = this.getClass().getResourceAsStream(topicFile);
	    if (is == null) {
	        return;
	    }
	    String text = "";
		try { 
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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
