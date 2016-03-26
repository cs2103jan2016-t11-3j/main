package logic.mark;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import common.*;
import logic.*;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

import java.util.ArrayList;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DoneTest {
	
	private final TaskObject taskOne = new TaskObject("Find calculator", "floating", "undone", 200);
	private final TaskObject taskTwo = new TaskObject("Find money", "floating", "undone", 3);
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject> ();
	private ArrayList<TaskObject> lastOutput = new ArrayList<TaskObject> ();

	@Test
	public void testA() {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		TaskObject taskTest = new TaskObject("1");
		CommandObject test = new CommandObject(INDEX_COMPLETE, taskTest, 1);
		taskList.add(taskOne);
		taskList.add(taskTwo);
		lastOutput.add(taskOne);
		Done doneTest = new Done(test, taskList, lastOutput);
		actualOutput = doneTest.run();
		expectedOutput.add("Task: 'Find calculator' marked as completed");
		assertEquals(expectedOutput, actualOutput);
	}
	
	/**
	 * Following tested: <br>
	 * 1. Recurring deadline with >1 remaining timing <br>
	 * 2. Recurring deadline with 1 remaining timing <br>
	 */
	 
	@Test
	public void testB() throws Exception {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		CommandObject test = new CommandObject(INDEX_COMPLETE, new TaskObject(), 3);
		TaskObject task = new TaskObject("deadline 1", LocalDateTime.of(2016, 4, 1, 23, 59), CATEGORY_DEADLINE, "incomplete", 2);
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.of(2016, 5, 13, 23, 59), "");
		LocalDateTimePair newPair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(newPair);
		task.setIsRecurring(true);
		task.setInterval(interval);
		Recurring.setAllRecurringDeadlineTimes(task);
		
		// sets up taskList and lastOutputTaskList
		taskList.add(taskOne);
		taskList.add(taskTwo);
		taskList.add(task);
		lastOutput.add(taskOne);
		lastOutput.add(taskTwo);
		lastOutput.add(task);
		
		Done done = new Done(test, taskList, lastOutput);
		actualOutput = done.run();
		
		// Recurrent task should continue to have incomplete status
		System.out.println(task.getStatus());
		assertTrue(task.getStatus().equals("incomplete"));
		
		expectedOutput.add("Task: 'deadline 1' marked as completed");
		assertEquals(expectedOutput, actualOutput);
		
		// Number of tasks increase by 1
		System.out.println(taskList.size());
		assertTrue(taskList.size() == 4);
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() < 0) {
				System.out.println(taskList.get(i).getTitle());
				System.out.println(taskList.get(i).getStatus());
				System.out.println(taskList.get(i).getStartDateTime().toString());
			}
		}
	}
	
	@Test
	public void testC() throws Exception {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		CommandObject test = new CommandObject(INDEX_COMPLETE, new TaskObject(), 3);
		TaskObject task = new TaskObject("deadline 1", LocalDateTime.of(2016, 4, 1, 23, 59), CATEGORY_DEADLINE, "incomplete", 2);
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.of(2016, 4, 5, 23, 59), "");
		LocalDateTimePair newPair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(newPair);
		task.setIsRecurring(true);
		task.setInterval(interval);
		Recurring.setAllRecurringDeadlineTimes(task);
		
		// sets up taskList and lastOutputTaskList
		taskList.add(taskOne);
		taskList.add(taskTwo);
		taskList.add(task);
		lastOutput.add(taskOne);
		lastOutput.add(taskTwo);
		lastOutput.add(task);
		
		Done done = new Done(test, taskList, lastOutput);
		actualOutput = done.run();
		
		// Recurrent task with 1 timing left should have completed status
		System.out.println(task.getStatus());
		assertTrue(task.getStatus().equals("completed"));
		
		expectedOutput.add("Task: 'deadline 1' marked as completed");
		assertEquals(expectedOutput, actualOutput);
		
		// No increase in number of tasks
		System.out.println(taskList.size());
		assertTrue(taskList.size() == 3);
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() < 0) {
				System.out.println(taskList.get(i).getTitle());
				System.out.println(taskList.get(i).getStatus());
				System.out.println(taskList.get(i).getStartDateTime().toString());
			}
		}
	}
}
