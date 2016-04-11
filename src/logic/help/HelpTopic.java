//@@author A0080510X

package logic.help;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is a representation of a help topic. 
 * The contents of the topic and name of the topic can be obtained from the class methods.
 * 
 * @author Hang
 *
 */
public class HelpTopic {

	public static String HELP_RESOURCES_PATH = "/src/logic/help/";
	
	private int topicNumber = 0;
	private String topicName = "";
	private ArrayList<String> topicContents = new ArrayList<String> ();
	
	/**
	 * Constructs the HelpTopic Object of the specified topic number. Initializes
	 * the private attributes of topicNumber, topicName and the details. These
	 * attributes can be read using the getter functions.
	 * @param topicNumber The topic number of the HelpTopic to be constructed.
	 */
	public HelpTopic(int topicNumber) {
		setTopicNumber(topicNumber);
		String topicResource = getResourcePath(Integer.toString(topicNumber));
		ArrayList<String> details = readResource(topicResource);
		topicName = setTopicName(details);
		topicContents = setTopicContents(details);
	}

    private void setTopicNumber(int topic) {
		topicNumber = topic;
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
        String textLine = "";
        try { 
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            textLine = reader.readLine();
            while (textLine != null) {
                content.add(textLine);
                textLine = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return content;
    }
    
    private String setTopicName(ArrayList<String> details) {
        return details.get(0); 
    }
	
    private ArrayList<String> setTopicContents(ArrayList<String> details) {
        details.remove(0);
        return details;
    }
    
	// Getters and setters
	public int getTopicNumber() {
		return topicNumber;
	}
	
	public String getTopicName() {
		return topicName;
	}
	
	public ArrayList<String> getDetails() {
		return topicContents;
	}

}
