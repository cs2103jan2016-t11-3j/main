package logic.edit;
import logic.TaskObject;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;


public class EditTest {
	
	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	private static ArrayList<TaskObject> testLastOutputList = new ArrayList<TaskObject> ();
	
	
	@Test
	public void test() {
		TaskObject testTaskObject = new TaskObject("2 dinner with mom");
		
		// Adding test values
		testList.add(new TaskObject("Buy new washing machine", 20160313, 1530, "deadline", "pending", 1));
		testList.add(new TaskObject("Army", 20120131, 20131129, 1200, 1200, "event", "completed", 2));
		testList.add(new TaskObject("Internship in army", 20160509, 20161108, 1200, 1830, "event", "pending", 3));
		testList.add(new TaskObject("Buck up in army", "floating", "pending", 4));
		testLastOutputList.add(new TaskObject("Army", 20120131, 20131129, 1200, 1200, "event", "completed", 2));
		testLastOutputList.add(new TaskObject("Buck up in army", "floating", "pending", 4));
		
		Edit testEdit = new Edit(testTaskObject, testLastOutputList, testList);
		ArrayList<String> actualOutput = new ArrayList<String>();
		actualOutput = testEdit.run();
		
		ArrayList<String> correctOutput = new ArrayList<String>();
		correctOutput.add("Message title edited from 'Buck up in army' to 'dinner with mom'.");
		
		
		for (int i = 0; i < testList.size(); i++) {
			System.out.println(testList.get(i).getTitle());
		}
		
		assertEquals(actualOutput, correctOutput);
	}


}
