package logic.help;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelpTopicTest {

	@Test
	public void testHelpTopicConstructor() {
		String name = "Add a floating task";
		HelpTopic test = new HelpTopic(1);
		assertEquals(name, test.getTopicName());
	}

}
