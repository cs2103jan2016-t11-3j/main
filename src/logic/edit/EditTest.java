package logic.edit;
import static org.junit.Assert.*;
import org.junit.Test;

import common.CommandObject;
import common.TaskObject;

import java.util.ArrayList;


public class EditTest {
	
	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	private static ArrayList<String> actualOutput = new ArrayList<String>();
	private static ArrayList<String> correctOutput = new ArrayList<String>();
	
	private CommandObject testCommandObject;
	private TaskObject testTaskObject;

	@Test // Populate the task list
	public void test() {
	
		testList.add(new TaskObject("Study hard for finals", "floating", "incomplete", 1));
		testList.add(new TaskObject("Find internship in Germany", "floating", "incomplete", 2));
		testList.add(new TaskObject("Travel Eastern Europe", "floating", "incomplete", 3));
		testList.add(new TaskObject("CS2106 Assignment 2", 20161201, 1800, "deadline", "incomplete", 4));
		testList.add(new TaskObject("CS2103 v0.2", 20170324, 1900, "deadline", "incomplete", 5));
		testList.add(new TaskObject("SSS1207 CA2", 20160331, 2359, "deadline", "incomplete", 6));
		testList.add(new TaskObject("Spring break", 20010111, 20160506, 0000, 2359, "event", "incomplete", 7));
		testList.add(new TaskObject("Overseas paradise", 20120131, 20131129, 2224, 1700, "event", "complete", 8));
		testList.add(new TaskObject("Hiking trip", 20140711, 20160715, 1600, 1700, "event", "incomplete", 9));
	}
	
	@Test // Test edit for title + date + time
	public void testA() {
		testTaskObject = new TaskObject("Reservist", 20160711, 20160715, 1000, 1700, "event", "incomplete", 9);
		testCommandObject = new CommandObject(3, testTaskObject, 9);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Hiking trip' to 'Reservist', date edited from '20140711' to '20160711', time edited from '1600' to '1000'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for date + time
	public void testB() {
		testTaskObject = new TaskObject("", 20160324, 1100, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 5);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Date edited from '20170324' to '20160324', time edited from '1900' to '1100'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for date
	public void testC() {
		testTaskObject = new TaskObject("", 20160401, -1, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 4);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Date edited from '20161201' to '20160401'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for time
	public void testD() {
		testTaskObject = new TaskObject("", -1, 1600, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 6);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Time edited from '2359' to '1600'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for title + time
	public void testE() {
		testTaskObject = new TaskObject("Army", -1, 1000, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 8);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Overseas paradise' to 'Army', time edited from '2224' to '1000'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for title
	public void testF() {
		testTaskObject = new TaskObject("Travel Eastern Europe and Iceland");
		testCommandObject = new CommandObject(3, testTaskObject, 3);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Travel Eastern Europe' to 'Travel Eastern Europe and Iceland'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for title + date
	public void testG() {
		testTaskObject = new TaskObject("AY2016/17 Sem 2", 20160111, -1, -1, -1, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 7);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Spring break' to 'AY2016/17 Sem 2', date edited from '20010111' to '20160111'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}

}
