//@@author A0080510X

package logic.help;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class tests if the helpTopic correctly constructs and returns the required 
 * help content.
 * @author Hang
 *
 */
public class HelpTopicTest {

	@Test
	public void testHelpTopicConstructorStart() {
		String topicName = "Add a floating task";
		String firstLine = "Enter “add”, followed by the title of the task.";
		HelpTopic test = new HelpTopic(1);
		assertEquals(topicName, test.getTopicName());
		assertEquals(firstLine, test.getDetails().get(0));
	}
	
	@Test
    public void testHelpTopicConstructorEnd() {
	    String topicName = "Exiting the programme";
	    String firstLine = "Enter the exit command. The data will be automatically"
                + " saved to the default file location before exiting.";
	    HelpTopic test = new HelpTopic(28);
	    assertEquals(topicName, test.getTopicName());
	    assertEquals(firstLine, test.getDetails().get(0));
	}

}
