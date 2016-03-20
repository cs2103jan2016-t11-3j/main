package logic.search;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import common.TaskObject;

import java.util.ArrayList;

public class SearchTest {
	
	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	private static ArrayList<String> actualOutput = new ArrayList<String>();

	@Test
	public void testA() {
		
		// Adding test values
		testList.add(new TaskObject("Buy new washing machine", 20160313, 1530, "deadline", "incomplete", 1));
		testList.add(new TaskObject("Army", 20120131, 20131129, 1200, 1200, "event", "completed", 2));
		testList.add(new TaskObject("Army reservist", 20160509, 20161108, 1200, 1830, "event", "incomplete", 3));
		testList.add(new TaskObject("I love army", "floating", "incomplete", 4));
		
		// Adding expected output
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Search results:");
		expectedOutput.add("1. Army, 31/01/2012-29/11/2013, 1200hrs-1200hrs, completed");
		expectedOutput.add("2. Army reservist, 09/05/2016-08/11/2016, 1200hrs-1830hrs, incomplete");
		expectedOutput.add("3. I love army, incomplete");
		
		TaskObject testKeyword = new TaskObject("army");
		Search testSearch = new Search(testKeyword, testList);
		actualOutput = testSearch.run();
		
		assertEquals(expectedOutput, actualOutput);
	}
	

}
