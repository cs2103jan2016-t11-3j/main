package logic.add;

import logic.*;
import common.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import parser.Parser;

import com.sun.javafx.tk.Toolkit.Task;

import common.TaskObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AddTest {

	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();

	/*********************************************************************************/
	
	@Test
	// Add floating task. No partitions
	public void testZ() throws Exception {
		Parser parser = new Parser("add dinner by tmr 7pm", 1);
		ArrayList<String> actualOutput = new ArrayList<String>();
		//TaskObject task = new TaskObject("Dinner tonight", 1);
		TaskObject temp = parser.run().getTaskObject();
		Add add = new Add(temp, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: dinner");

		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Add floating task. No partitions
	public void testA() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject task = new TaskObject("Dinner tonight", 1);
		task.setCategory("floating");
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Dinner tonight");

		assertEquals(expectedOutput, actualOutput);
	}

	/*********************************************************************************/
	@Test
	// Add deadline not yet due
	public void testB() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject task = new TaskObject("Assignment 1", 2);
		task.setStatus("incomplete");
		task.setCategory("deadline");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 29, 15, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: Assignment 1");

		assertEquals(expectedOutput, actualOutput);

		assertEquals("incomplete", task.getStatus());
	}

	@Test
	// Add overdue deadline
	public void testC() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("CE2", 6);
		task.setCategory("deadline");
		task.setStartDateTime(LocalDateTime.of(2016, 02, 27, 16, 00));
		Add add = new Add(task, 3, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CE2");

		assertEquals(expectedOutput, actualOutput);

		String taskStatus = add.getTask().getStatus();
		assertEquals("overdue", taskStatus);
	}

	/*********************************************************************************/

	@Test
	// Add completely new event
	public void testD() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		TaskObject task = new TaskObject("event 1", 1);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 25, 15, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 26, 12, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 1");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing event: StartDateTime and EndDateTime identical
	public void testE() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 2", 2);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 25, 15, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 26, 12, 00));
		Add add = new Add(task, 1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 2");
		expectedOutput.add("Task: event 2 clashes with event 1");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing event: new task EndDateTime == current task StartDateTime
	public void testF() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 3", 3);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 25, 14, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 25, 15, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 3");
		expectedOutput.add("Task: event 3 clashes with event 2");
		expectedOutput.add("Task: event 3 clashes with event 1");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing event: new task StartDateTime == current task EndDateTime
	public void testG() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 4", 4);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 26, 12, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 26, 13, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 4");
		expectedOutput.add("Task: event 4 clashes with event 2");
		expectedOutput.add("Task: event 4 clashes with event 1");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add completely new event
	public void testH() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 5", 5);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 03, 31, 21, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 01, 20, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 5");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing event: current task StartDateTime between new task start and
	// end
	public void testI() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 6", 6);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 03, 31, 18, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 03, 31, 23, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 6");
		expectedOutput.add("Task: event 6 clashes with event 5");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing event: current task EndDateTime between new task start and
	// end
	public void testJ() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 7", 7);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 01, 17, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 01, 21, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 7");
		expectedOutput.add("Task: event 7 clashes with event 5");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing event: New task starts and ends within timings of current
	// task
	public void testK() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 8", 8);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 04, 01, 01, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 04, 01, 06, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 8");
		expectedOutput.add("Task: event 8 clashes with event 5");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add completely new event
	public void testL() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 9", 9);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 12, 00, 30));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 12, 00, 31));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 9");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing events: Current event occurs during new event
	public void testM() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 10", 10);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 11, 21, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 12, 12, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 10");
		expectedOutput.add("Task: event 10 clashes with event 9");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing events: New event starts the same time as current event
	public void testN() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 11", 11);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 11, 21, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 11, 22, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 11");
		expectedOutput.add("Task: event 11 clashes with event 10");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add clashing events: New event ends the same time as current event
	public void testO() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 12", 12);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 12, 10, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 12, 12, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 12");
		expectedOutput.add("Task: event 12 clashes with event 10");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add non-clashing event: new event ends right before current event
	public void testP() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 13", 13);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 11, 20, 30));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 11, 20, 59));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 13");

		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Add non-clashing event: new event starts right after current event
	public void testQ() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 14", 14);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 12, 12, 01));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 12, 12, 40));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 14");

		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Tests exception handling: start time after end time
	public void testR() {
		ArrayList<String> actualOutput = new ArrayList<String>();

		TaskObject task = new TaskObject("event 15", 15);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 05, 18, 12, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 05, 18, 11, 00));
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Failed to add task. Reason: Invalid time input.");

		assertEquals(expectedOutput, actualOutput);
	}
	/*********************************************************************************/

	@Test
	// Adds recurrent task with preset "until"
	public void testS() throws Exception {
		ArrayList<String> actualOutput = new ArrayList<String>();
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.of(2016, 9, 24, 12, 00), "");

		TaskObject task = new TaskObject("event 16", 16);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 06, 18, 12, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 06, 18, 14, 00));
		task.setInterval(interval);
		task.setIsRecurring(true);
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 16");
		assertEquals(expectedOutput, actualOutput);
		
		ArrayList<LocalDateTimePair> expectedDateTimes = new ArrayList<LocalDateTimePair> ();
		
		LocalDateTime testStart = LocalDateTime.of(2016, 06, 18, 12, 00);
		LocalDateTime testEnd = LocalDateTime.of(2016, 06, 18, 14, 00);
		
		while(testStart.isBefore(LocalDateTime.of(2016, 9, 24, 12, 00))) {
			LocalDateTimePair pair = new LocalDateTimePair(testStart, testEnd);
			expectedDateTimes.add(pair);
			testStart = testStart.plusWeeks(2);
			testEnd = testEnd.plusWeeks(2);
		}
		
		assertEquals(expectedDateTimes.size(), task.getTaskDateTimes().size());
		
		for (int i = 0; i < expectedDateTimes.size(); i++) {
			assertEquals(expectedDateTimes.get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getStartDateTime());
			assertEquals(expectedDateTimes.get(i).getEndDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
		}
	}
	
	@Test
	// Adds recurrent task with preset "count"
	public void testT() throws Exception {
		ArrayList<String> actualOutput = new ArrayList<String>();
		Interval interval = new Interval("DAILY", 6, 5, "");

		TaskObject task = new TaskObject("event 17", 17);
		task.setCategory("event");
		task.setStartDateTime(LocalDateTime.of(2016, 06, 26, 12, 00));
		task.setEndDateTime(LocalDateTime.of(2016, 06, 26, 14, 00));
		task.setInterval(interval);
		task.setIsRecurring(true);
		Add add = new Add(task, -1, testArray);
		actualOutput = add.run();

		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: event 17");
		expectedOutput.add("Task: event 17 clashes with event 16");
		assertEquals(expectedOutput, actualOutput);
		
		assertTrue(add.getIsClash());
		
		ArrayList<LocalDateTimePair> expectedDateTimes = new ArrayList<LocalDateTimePair> ();
		
		LocalDateTime testStart = LocalDateTime.of(2016, 06, 26, 12, 00);
		LocalDateTime testEnd = LocalDateTime.of(2016, 06, 26, 14, 00);
		
		for(int i = 0; i < interval.getCount(); i++) {
			LocalDateTimePair pair = new LocalDateTimePair(testStart, testEnd);
			expectedDateTimes.add(pair);
			testStart = testStart.plusDays(6);
			testEnd = testEnd.plusDays(6);
		}
		
		assertEquals(expectedDateTimes.size(), task.getTaskDateTimes().size());
		
		for (int i = 0; i < expectedDateTimes.size(); i++) {
			//System.out.println(task.getTaskDateTimes().get(i).getStartDateTime().toString());
			//System.out.println(task.getTaskDateTimes().get(i).getEndDateTime().toString());
			assertEquals(expectedDateTimes.get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getStartDateTime());
			assertEquals(expectedDateTimes.get(i).getEndDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
		}
	}
	/*********************************************************************************/
	
	@Test // Test for addition of a single occurrence to the recurrence details of a recurring task
	public void testU() {
		ArrayList<String> actualOutput = new ArrayList<String>();
		
		// Creating a sample recurring task
		ArrayList<LocalDateTimePair> originalRecurrenceDetails = new ArrayList<LocalDateTimePair>();
		LocalDateTime startOne= LocalDateTime.of(LocalDate.parse("2016-01-01"), LocalTime.parse("00:00"));
		LocalDateTime endOne = LocalDateTime.of(LocalDate.parse("2016-01-01"), LocalTime.parse("23:59"));
		LocalDateTime startTwo = LocalDateTime.of(LocalDate.parse("2016-02-01"), LocalTime.parse("00:00"));
		LocalDateTime endTwo = LocalDateTime.of(LocalDate.parse("2016-02-01"), LocalTime.parse("23:59"));
		LocalDateTime startThree = LocalDateTime.of(LocalDate.parse("2016-03-01"), LocalTime.parse("00:00"));
		LocalDateTime endThree = LocalDateTime.of(LocalDate.parse("2016-03-01"), LocalTime.parse("23:59"));
		originalRecurrenceDetails.add(new LocalDateTimePair(startTwo, endTwo));
		originalRecurrenceDetails.add(new LocalDateTimePair(startThree, endThree));
		
		TaskObject recurrenceTask = new TaskObject("1st day of every month", startOne, endThree, "event", "incomplete",
				18, true, originalRecurrenceDetails);
		testArray.add(recurrenceTask); // this is the 20th item in the list
		
		// Creating the sample task object
		ArrayList<LocalDateTimePair> occurrenceToBeAdded = new ArrayList<LocalDateTimePair>();
		occurrenceToBeAdded.add(new LocalDateTimePair(startOne, endOne));
		TaskObject testTaskObj = new TaskObject(occurrenceToBeAdded);
		Add add = new Add(testTaskObj, 20, testArray);
		actualOutput = add.run();
		
		ArrayList<LocalDateTimePair> editedRecurrenceDetails = testArray.get(testArray.size()-1).getTaskDateTimes();
		assertEquals(editedRecurrenceDetails.get(0).getStartDateTime(), startOne);
		assertEquals(editedRecurrenceDetails.get(0).getEndDateTime(), endOne);
		
	}
	
	

}

