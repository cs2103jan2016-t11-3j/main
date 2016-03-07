package logic.add;
import logic.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;

public class AddTest {
	
	// static declaration for continuous adding to the testArray
	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject> ();

	@Test
	// Add floating task
	public void testOne() {
		ArrayList<String> actualOutput = new ArrayList<String> ();
		TaskObject taskOne = new TaskObject("Dinner tonight", 1);
		taskOne.setTitle("Dinner tonight"); // This line due to error in TaskObject
		taskOne.setCategory("floating");
		Add addFirst = new Add(taskOne, -1, testArray);
		actualOutput = addFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: Dinner tonight");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add deadline
	public void testTwo() {
		ArrayList<String> actualOutput = new ArrayList<String> ();
		TaskObject taskTwo= new TaskObject("Assignment 1", 2);
		taskTwo.setTitle("Assignment 1"); // This line due to error in TaskObject
		taskTwo.setCategory("deadline");
		taskTwo.setEndDate(20160229);
		taskTwo.setEndTime(1500);
		Add addSecond = new Add(taskTwo, -1, testArray);
		actualOutput = addSecond.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: Assignment 1");
		
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add non-clashing event
	public void testThree() {
		ArrayList<String> actualOutput = new ArrayList<String> ();
		TaskObject taskThree = new TaskObject("Hitachi D&D", 3);
		taskThree.setTitle("Hitachi D&D"); // This line due to error in TaskObject
		taskThree.setCategory("event");
		taskThree.setStartDate(20160226);
		taskThree.setStartTime(1700);
		taskThree.setEndDate(20160226);
		taskThree.setEndTime(2200);
		Add addThird = new Add(taskThree, -1, testArray);
		actualOutput = addThird.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: Hitachi D&D");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add clashing event on day
	public void testFour() {
		ArrayList<String> actualOutput = new ArrayList<String> ();
		
		TaskObject taskFour = new TaskObject("Dinner at home", 4);
		taskFour.setTitle("Dinner at home"); // This line due to error in TaskObject
		taskFour.setCategory("event");
		taskFour.setStartDate(20160226);
		taskFour.setStartTime(1800);
		taskFour.setEndDate(20160226);
		taskFour.setEndTime(2100);
		Add addFourth = new Add(taskFour, -1, testArray);
		actualOutput = addFourth.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: Dinner at home");
		expectedOutput.add("Task: Dinner at home clashes with Hitachi D&D");
		expectedOutput.add("Task: Dinner at home clashes with Phuket trip");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add clashing event across days
	public void testFive() {
		ArrayList<String> actualOutput = new ArrayList<String> ();
		
		TaskObject taskFive = new TaskObject("Phuket trip", 5);
		taskFive.setTitle("Phuket trip"); // This line due to error in TaskObject
		taskFive.setCategory("event");
		taskFive.setStartDate(20160224);
		taskFive.setStartTime(1200);
		taskFive.setEndDate(20160227);
		taskFive.setEndTime(1600);
		Add addFifth = new Add(taskFive, -1, testArray);
		actualOutput = addFifth.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: Phuket trip");
		expectedOutput.add("Task: Phuket trip clashes with Hitachi D&D");
		// Strange thing is that test 5 runs ahead of test 4, hence expected output changes
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add overdue deadline
	public void testSix() {
	ArrayList<String> actualOutput = new ArrayList<String> ();
		
		TaskObject taskSix = new TaskObject("CE2", 5);
		taskSix.setTitle("CE2"); // This line due to error in TaskObject
		taskSix.setCategory("deadline");
		taskSix.setEndDate(20160227);
		taskSix.setEndTime(1600);
		Add addSixth = new Add(taskSix, -1, testArray);
		actualOutput = addSixth.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: CE2");
		
		assertEquals(expectedOutput, actualOutput);
		
		String taskStatus = addSixth.getTask().getStatus();
		assertEquals("overdue", taskStatus);
	}
}
