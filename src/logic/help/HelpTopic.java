package logic.help;

import java.util.ArrayList;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

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
		File topicFile = constructFile(num);
		readFile(topicFile);
	}
	
	public File constructFile(String num) {
		File fakeFile = new File("");
		String path = fakeFile.getAbsolutePath(); // To obtain working directory
		File file = new File(path + FILE_PATH + num + ".txt");
		return file;
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
