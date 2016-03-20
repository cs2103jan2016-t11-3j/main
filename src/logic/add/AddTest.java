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
// Updated to include all time inputs as LocalDateTime
public class AddTest {

	// static declaration for continuous adding to the testArray
	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();

	/*********************************************************************************/
	@Test
	// Add floating task
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
	// Add deadline
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
}

