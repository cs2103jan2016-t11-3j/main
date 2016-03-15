package logic.add;

import logic.*;
import common.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import com.sun.javafx.tk.Toolkit.Task;

import common.TaskObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//Updated to include all time inputs as LocalDateTime
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
		taskTwo.setStartDateTime(LocalDateTime.of(2016, 02, 29, 15, 00));
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
		taskThree.setStartDateTime(LocalDateTime.of(2016, 02, 26, 17, 00));
		taskThree.setEndDateTime(LocalDateTime.of(2016, 02, 26, 22, 00));
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
		taskFive.setStartDateTime(LocalDateTime.of(2016, 02, 24, 12, 00));
		taskFive.setEndDateTime(LocalDateTime.of(2016, 02, 27, 16, 00));
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
		taskFour.setStartDateTime(LocalDateTime.of(2016, 02, 26, 18, 00));
		taskFour.setEndDateTime(LocalDateTime.of(2016, 02, 26, 21, 00));
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
		taskSix.setStartDateTime(LocalDateTime.of(2016, 02, 27, 16, 00));
		Add addSixth = new Add(taskSix, -1, testArray);
		actualOutput = addSixth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CE2");

		assertEquals(expectedOutput, actualOutput);

		String taskStatus = addSixth.getTask().getStatus();
		assertEquals("overdue", taskStatus);
	}

	/*
	@Test
	// Add failed due to invalid deadline time
	public void testG() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskSeven = new TaskObject("Buy pizza", 7);
		taskSeven.setCategory("deadline");
		taskSeven.setEndDateTime(LocalDateTime.of(2016, 02, 27, 25, 00));
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
		taskEight.setStartDateTime(LocalDateTime.of(2016, 13, 15, 17, 00)); // Invalid Date input
		taskEight.setEndDateTime(LocalDateTime.of(2016, 44, 44, 22, 00)); // Invalid Date input
		Add addEighth = new Add(taskEight, -1, testArray);
		actualOutput = addEighth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Failed to add task. Reason: Invalid time input.");

		assertEquals(expectedOutput, actualOutput);
	}
	*/
	
	@Test
	// Add non-clashing event 
	public void testI() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskNine = new TaskObject("Dinner in school", 9);
		taskNine.setCategory("event");
		taskNine.setStartDateTime(LocalDateTime.of(2016, 03, 26, 18, 00));
		taskNine.setEndDateTime(LocalDateTime.of(2016, 03, 26, 21, 00));
		Add addNinth = new Add(taskNine, -1, testArray);
		actualOutput = addNinth.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Dinner in school");

		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add recurring deadline
	public void testJ() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskTen = new TaskObject("Weekly assignment", 10);
		taskTen.setCategory("deadline");
		taskTen.setIsRecurring(true);
		LocalDateTime dateTime = LocalDateTime.of(2016, 03, 26, 18, 00);
		
		taskTen.setStartDateTime(dateTime);
		LocalDateTimePair pair = new LocalDateTimePair(dateTime);
		
		// Test if intervals add correctly
		Interval interval = new Interval();
		interval.setWeek(1);
		LocalDateTime duplicate = dateTime;
		duplicate = Add.addInterval(duplicate, interval);
		assertEquals(LocalDateTime.of(2016, 03, 26, 18, 00).plusWeeks(1), duplicate);
		
		taskTen.setInterval(interval);
		
		// Test if output is the same
		Add addTenth = new Add(taskTen, -1, testArray);
		actualOutput = addTenth.run();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Weekly assignment");
		assertEquals(expectedOutput, actualOutput);
		
		/* For test printing
		for(int i = 0; i < taskTen.getTaskDateTime().size(); i++) {
			System.out.println(i+1 + " " + taskTen.getTaskDateTime().get(i).getStartDateTime().toString());
		}
		*/
		
		// Test if taskDateTime ArrayLists are the same
		for(int i = 0; i < 10; i++) {
			pair = new LocalDateTimePair(dateTime);
			dateTime = dateTime.plusWeeks(1);
			assertEquals(pair.getStartDateTime(), taskTen.getTaskDateTime().get(i).getStartDateTime());
		}
	}
	
	@Test
	// Add recurring event
	public void testK() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskEleven = new TaskObject("Weekly dinner date", 11);
		taskEleven.setCategory("event");
		taskEleven.setIsRecurring(true);
		LocalDateTime sdt = LocalDateTime.of(2016, 03, 27, 18, 00);
		LocalDateTime edt = LocalDateTime.of(2016, 03, 27, 21, 00);
		
		taskEleven.setStartDateTime(sdt);
		taskEleven.setEndDateTime(edt);
		LocalDateTimePair pair = new LocalDateTimePair(sdt, edt);
		
		// Test if intervals add correctly
		Interval interval = new Interval();
		interval.setWeek(1);
		interval.setDay(1);
		
		taskEleven.setInterval(interval);
		
		// Test if output is the same
		Add addEleventh = new Add(taskEleven, -1, testArray);
		actualOutput = addEleventh.run();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Weekly dinner date");
		assertEquals(expectedOutput, actualOutput);
		
		/* For test printing
		for(int i = 0; i < taskTen.getTaskDateTime().size(); i++) {
			System.out.println(i+1 + " " + taskTen.getTaskDateTime().get(i).getStartDateTime().toString());
		}
		*/
		
		// Test if taskDateTime ArrayLists are the same
		for(int i = 0; i < 10; i++) {
			pair = new LocalDateTimePair(sdt, edt);
			sdt = sdt.plusWeeks(1).plusDays(1);
			edt = edt.plusWeeks(1).plusDays(1);
			assertEquals(pair.getStartDateTime(), taskEleven.getTaskDateTime().get(i).getStartDateTime());
		}
	}
	
	@Test
	// Add recurring event which will clash in the future
	public void testL() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject taskTwelve = new TaskObject("Weekly supper date", 12);
		taskTwelve.setCategory("event");
		taskTwelve.setIsRecurring(true);
		LocalDateTime sdt = LocalDateTime.of(2016, 03, 29, 20, 00);
		LocalDateTime edt = LocalDateTime.of(2016, 03, 29, 22, 00);
		
		taskTwelve.setStartDateTime(sdt);
		taskTwelve.setEndDateTime(edt);
		LocalDateTimePair pair = new LocalDateTimePair(sdt, edt);
		
		// Test if intervals add correctly
		Interval interval = new Interval();
		interval.setWeek(1);
		
		taskTwelve.setInterval(interval);
		
		// Test if output is the same
		Add addTwelfth = new Add(taskTwelve, -1, testArray);
		actualOutput = addTwelfth.run();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Weekly supper date");
		expectedOutput.add("Task: Weekly supper date clashes with Weekly dinner date");
		assertEquals(expectedOutput, actualOutput);
		
		/* For test printing
		for(int i = 0; i < taskTen.getTaskDateTime().size(); i++) {
			System.out.println(i+1 + " " + taskTen.getTaskDateTime().get(i).getStartDateTime().toString());
		}
		*/
		
		// Test if taskDateTime ArrayLists are the same
		for(int i = 0; i < 10; i++) {
			pair = new LocalDateTimePair(sdt, edt);
			sdt = sdt.plusWeeks(1);
			edt = edt.plusWeeks(1);
			assertEquals(pair.getStartDateTime(), taskTwelve.getTaskDateTime().get(i).getStartDateTime());
		}
	}
}
