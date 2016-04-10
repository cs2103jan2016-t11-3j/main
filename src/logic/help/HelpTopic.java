package logic.help;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpTopic {

	public static String HELP_RESOURCES_PATH = "/src/logic/help/";
	
	private int topicNumber = 0;
	private String topicName = "";
	private ArrayList<String> details = new ArrayList<String> ();
	
	/**
	 * Constructs the HelpTopic Object of the specified topic number. Initializes
	 * the private attributes of topicNumber, topicName and the details. These
	 * attributes can be read using the getter functions.
	 * @param topic The topic number of the HelpTopic to be constructed.
	 */
	public HelpTopic(int topic) {
		String num;
		topicNumber = topic;
		num = Integer.toString(topicNumber);
		String topicResource = getResourcePath(num);
		details = readResource(topicResource);
	}
	
	private String getResourcePath(String num) {
		String helpResourcePath = num + ".txt";
		return helpResourcePath;
	}

	/**
	 * Reads the help content from the specified topicFile resource.
	 * @param topicResource Name of the help resource to read from.
	 * @return content The contents of the helpTopic read.
	 */
	private ArrayList<String> readResource(String topicResource) {
	    ArrayList<String> content = new ArrayList<String> ();
	    InputStream is = this.getClass().getResourceAsStream(topicResource);
	    if (is == null) {
	        return content;
	    }
	    String text = "";
		try { 
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			topicName = reader.readLine();
			text = reader.readLine();
			while (text != null) {
				content.add(text);
				text = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
        return content;
	}
	
	// Getters and setters
	public int getTopicNumber() {
		return topicNumber;
	}
	
	public String getTopicName() {
		return topicName;
	}
	
	public ArrayList<String> getDetails() {
		return details;
	}

}
