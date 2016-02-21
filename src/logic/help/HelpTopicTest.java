package logic.help;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelpTopicTest {

	@Test
	public void testHelpTopicConstructor() {
		String name = "1.	Adding a new event/floating task: ";
		HelpTopic test = new HelpTopic(1);
		assertEquals(name, test.getTopicName());
	}

}
