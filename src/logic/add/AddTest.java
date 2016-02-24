package logic.add;
import logic.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;

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
		Add addFirst = new Add(taskOne, testArray);
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
		Add addSecond = new Add(taskTwo, testArray);
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
		Add addThird = new Add(taskThree, testArray);
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
		Add addFourth = new Add(taskFour, testArray);
		actualOutput = addFourth.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task: Dinner at home clashes with Hitachi D&D");
		
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
		Add addFifth = new Add(taskFive, testArray);
		actualOutput = addFifth.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task: Phuket trip clashes with Hitachi D&D");
		
		assertEquals(expectedOutput, actualOutput);
	}
}
