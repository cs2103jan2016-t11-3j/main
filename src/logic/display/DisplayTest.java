package logic.display;
import logic.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import common.TaskObject;

public class DisplayTest {

	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	
	@Test
	public void testDisplayDeadline() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		
		// Adding test values
		testList.add(new TaskObject("CS2103 v0.1", 20160311, 1800, "deadline", "pending", 1));
		testList.add(new TaskObject("Buy new washing machine", 20160313, 1530, "deadline", "pending", 1));
		testList.add(new TaskObject("Book into camp", 20160711, 2359, "deadline", "pending", 1));
		testList.add(new TaskObject("Fly off into the sunset", 20161231, 2300, "deadline", "pending", 1));
		testList.add(new TaskObject("ORD", 20131129, 1200, "deadline", "completed", 1));

		// Adding expected output
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("1. CS2103 v0.1, 11/03/2016, 1800hrs, pending");
		expectedOutput.add("2. Buy new washing machine, 13/03/2016, 1530hrs, pending");
		expectedOutput.add("3. Book into camp, 11/07/2016, 2359hrs, pending");
		expectedOutput.add("4. Fly off into the sunset, 31/12/2016, 2300hrs, pending");
		expectedOutput.add("5. ORD, 29/11/2013, 1200hrs, completed");
		
		Display display = new Display(testList);
		actualOutput = display.run();
		
		assertEquals(expectedOutput, actualOutput);
		
	}
	
	@Test
	public void testDisplayVariousTasks() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		
		// Adding test values
		testList.clear();
		testList.add(new TaskObject("Buy new washing machine", 20160313, 1530, "deadline", "pending", 1));
		testList.add(new TaskObject("Army", 20120131, 20131129, 1200, 1200, "event", "completed", 2));
		testList.add(new TaskObject("Internship", 20160509, 20161108, 1200, 1830, "event", "pending", 3));
		testList.add(new TaskObject("Buck up", "floating", "pending", 4));
		
		// Adding expected output
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("1. Buy new washing machine, 13/03/2016, 1530hrs, pending");
		expectedOutput.add("2. Army, 31/01/2012-29/11/2013, 1200hrs-1200hrs, completed");
		expectedOutput.add("3. Internship, 09/05/2016-08/11/2016, 1200hrs-1830hrs, pending");
		expectedOutput.add("4. Buck up, pending");
		
		Display display = new Display(testList);
		actualOutput = display.run();
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	
}
