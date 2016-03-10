package logic.add;

import logic.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import com.sun.javafx.tk.Toolkit.Task;

import common.TaskObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddTest {

	// static declaration for continuous adding to the testArray
	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();

	@Test
	// Add floating task
	public void testA() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject taskOne = new TaskObject("Dinner tonight", 1);
		taskOne.setCategory("floating");
		Add addFirst = new Add(taskOne, -1, testArray);
		actualOutput = addFirst.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Dinner tonight");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add deadline
	public void testB() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject taskTwo = new TaskObject("Assignment 1", 2);
		taskTwo.setCategory("deadline");
		taskTwo.setEndDate(20160229);
		taskTwo.setEndTime(1500);
		Add addSecond = new Add(taskTwo, -1, testArray);
		actualOutput = addSecond.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Assignment 1");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add non-clashing event
	public void testC() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject taskThree = new TaskObject("Hitachi D&D", 3);
		taskThree.setCategory("event");
		taskThree.setStartDate(20160226);
		taskThree.setStartTime(1700);
		taskThree.setEndDate(20160226);
		taskThree.setEndTime(2200);
		Add addThird = new Add(taskThree, -1, testArray);
		actualOutput = addThird.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Hitachi D&D");

		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add clashing event across days
	public void testD() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskFive = new TaskObject("Phuket trip", 5);
		taskFive.setCategory("event");
		taskFive.setStartDate(20160224);
		taskFive.setStartTime(1200);
		taskFive.setEndDate(20160227);
		taskFive.setEndTime(1600);
		Add addFifth = new Add(taskFive, -1, testArray);
		actualOutput = addFifth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Phuket trip");
		expectedOutput.add("Task: Phuket trip clashes with Hitachi D&D");

		assertEquals(expectedOutput, actualOutput);
	}
	@Test
	// Add clashing event on day
	public void testE() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskFour = new TaskObject("Dinner at home", 4);
		taskFour.setCategory("event");
		taskFour.setStartDate(20160226);
		taskFour.setStartTime(1800);
		taskFour.setEndDate(20160226);
		taskFour.setEndTime(2100);
		Add addFourth = new Add(taskFour, -1, testArray);
		actualOutput = addFourth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Dinner at home");
		expectedOutput.add("Task: Dinner at home clashes with Hitachi D&D");
		expectedOutput.add("Task: Dinner at home clashes with Phuket trip");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add overdue deadline
	public void testF() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskSix = new TaskObject("CE2", 6);
		taskSix.setCategory("deadline");
		taskSix.setEndDate(20160227);
		taskSix.setEndTime(1600);
		Add addSixth = new Add(taskSix, -1, testArray);
		actualOutput = addSixth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CE2");

		assertEquals(expectedOutput, actualOutput);

		String taskStatus = addSixth.getTask().getStatus();
		assertEquals("overdue", taskStatus);
	}

	@Test
	// Add failed due to invalid deadline time
	public void testG() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskSeven = new TaskObject("Buy pizza", 7);
		taskSeven.setCategory("deadline");
		taskSeven.setEndDate(20160227);
		taskSeven.setEndTime(2500); // Invalid time
		Add addSeventh = new Add(taskSeven, -1, testArray);
		actualOutput = addSeventh.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Failed to add task. Reason: Invalid time input.");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add failed due to invalid event time
	public void testH() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject taskEight = new TaskObject("Dinner with Ruibin", 8);
		taskEight.setCategory("event");
		taskEight.setStartDate(20161315); // Invalid Date input
		taskEight.setStartTime(1700);
		taskEight.setEndDate(20164444); // Invalid Date input
		taskEight.setEndTime(2200);
		Add addEighth = new Add(taskEight, -1, testArray);
		actualOutput = addEighth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Failed to add task. Reason: Invalid time input.");

		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add non-clashing event 
	public void testI() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskNine = new TaskObject("Dinner in school", 4);
		taskNine.setCategory("event");
		taskNine.setStartDate(20160326);
		taskNine.setStartTime(1800);
		taskNine.setEndDate(20160326);
		taskNine.setEndTime(2100);
		Add addNinth = new Add(taskNine, -1, testArray);
		actualOutput = addNinth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Dinner in school");

		assertEquals(expectedOutput, actualOutput);
	}
	
}
