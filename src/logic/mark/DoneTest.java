//@@author A0124052X

package logic.mark;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import common.*;
import logic.*;
import logic.timeOutput.TimeOutput;

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
	// Recurrent deadline with multiple timings left
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
	// Recurrent deadline with 1 timing left
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
	
	@Test
	// Infinite recurring deadline
	public void testD() throws Exception {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		CommandObject test = new CommandObject(INDEX_COMPLETE, new TaskObject(), 3);
		TaskObject task = new TaskObject("deadline 1", LocalDateTime.of(2016, 4, 1, 23, 59), CATEGORY_DEADLINE, "incomplete", 2);
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.MAX, "");
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
		
		// Infinite recurring task should always have incomplete status
		System.out.println(task.getStatus());
		assertTrue(task.getStatus().equals("incomplete"));
		
		expectedOutput.add("Task: 'deadline 1' marked as completed");
		assertEquals(expectedOutput, actualOutput);
		
		// Increase in number of tasks by 1
		System.out.println(taskList.size());
		assertTrue(taskList.size() == 4);
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() < 0) {
				System.out.println(taskList.get(i).getTitle());
				System.out.println(taskList.get(i).getStatus());
				System.out.println(taskList.get(i).getStartDateTime().toString());
			}
		}
		
		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setDeadlineTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime());
			System.out.println(line);
		}
	}
	
	@Test
	// Recurring event with multiple timings
	public void testE() throws Exception {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		CommandObject test = new CommandObject(INDEX_COMPLETE, new TaskObject(), 3);
		TaskObject task = new TaskObject("event 1", LocalDateTime.of(2016, 4, 13, 23, 59), LocalDateTime.of(2016, 4, 14, 01, 59), CATEGORY_EVENT, "incomplete", 2);
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.of(2016, 6, 13, 23, 59), "");
		LocalDateTimePair newPair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(newPair);
		task.setIsRecurring(true);
		task.setInterval(interval);
		Recurring.setAllRecurringEventTimes(task);
		int originalTimingCount = task.getTaskDateTimes().size();
		
		// sets up taskList and lastOutputTaskList
		taskList.add(taskOne);
		taskList.add(taskTwo);
		taskList.add(task);
		lastOutput.add(taskOne);
		lastOutput.add(taskTwo);
		lastOutput.add(task);
		
		Done done = new Done(test, taskList, lastOutput);
		actualOutput = done.run();
		int newTimingCount = task.getTaskDateTimes().size();
		
		// Number of timings in the task should decrease by 1
		assertTrue (newTimingCount + 1 == originalTimingCount);
		
		// Number of tasks should increase by 1
		assertTrue (taskList.size() == 4);
		
		// Recurring task with >1 timings should always have incomplete status
		System.out.println(task.getStatus());
		assertTrue(task.getStatus().equals("incomplete"));
		
		expectedOutput.add("Task: 'event 1' marked as completed");
		assertEquals(expectedOutput, actualOutput);
		
		// No change in number of tasks
		System.out.println(taskList.size());
		assertTrue(taskList.size() == 3);
		
		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() < 0) {
				System.out.println(taskList.get(i).getTimeOutputString());
				System.out.println(taskList.get(i).getStatus());
				System.out.println(taskList.get(i).getTaskId());
			}
		}
	}
	
	@Test
	// Recurring event with 1 timing left
	public void testF() throws Exception {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		CommandObject test = new CommandObject(INDEX_COMPLETE, new TaskObject(), 3);
		TaskObject task = new TaskObject("event 1", LocalDateTime.of(2016, 4, 13, 23, 59), LocalDateTime.of(2016, 4, 14, 01, 59), CATEGORY_EVENT, "incomplete", 2);
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.of(2016, 4, 13, 23, 59), "");
		LocalDateTimePair newPair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(newPair);
		task.setIsRecurring(true);
		task.setInterval(interval);
		Recurring.setAllRecurringEventTimes(task);
		int originalTimingCount = task.getTaskDateTimes().size();
		
		// sets up taskList and lastOutputTaskList
		taskList.add(taskOne);
		taskList.add(taskTwo);
		taskList.add(task);
		lastOutput.add(taskOne);
		lastOutput.add(taskTwo);
		lastOutput.add(task);
		
		Done done = new Done(test, taskList, lastOutput);
		actualOutput = done.run();
		int newTimingCount = task.getTaskDateTimes().size();
		
		// Number of timings in the task should not change
		assertTrue (newTimingCount == originalTimingCount);
		
		// Recurring event with 1 timing left should be set to completed
		System.out.println(task.getStatus());
		assertTrue(task.getStatus().equals("completed"));
		
		expectedOutput.add("Task: 'event 1' marked as completed");
		assertEquals(expectedOutput, actualOutput);
		
		// No change in number of tasks
		System.out.println(taskList.size());
		assertTrue(taskList.size() == 3);
		
		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}
	
	@Test
	// Infinite recurring event 
	public void testG() throws Exception {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		CommandObject test = new CommandObject(INDEX_COMPLETE, new TaskObject(), 3);
		TaskObject task = new TaskObject("event 1", LocalDateTime.of(2016, 4, 13, 23, 59), LocalDateTime.of(2016, 4, 14, 01, 59), CATEGORY_EVENT, "incomplete", 2);
		Interval interval = new Interval("WEEKLY", 2, LocalDateTime.MAX, "");
		LocalDateTimePair newPair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(newPair);
		task.setIsRecurring(true);
		task.setInterval(interval);
		Recurring.setAllRecurringEventTimes(task);
		int originalTimingCount = task.getTaskDateTimes().size();
		
		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
		
		// sets up taskList and lastOutputTaskList
		taskList.add(taskOne);
		taskList.add(taskTwo);
		taskList.add(task);
		lastOutput.add(taskOne);
		lastOutput.add(taskTwo);
		lastOutput.add(task);
		
		Done done = new Done(test, taskList, lastOutput);
		actualOutput = done.run();
		int newTimingCount = task.getTaskDateTimes().size();
		
		// Number of timings in the task should be 10
		assertTrue (newTimingCount == originalTimingCount);
		assertTrue (newTimingCount == 10);
		
		// Infinitely recurring event cannot be completed
		System.out.println(task.getStatus());
		assertTrue(task.getStatus().equals("incomplete"));
		
		expectedOutput.add("Task: 'event 1' marked as completed");
		assertEquals(expectedOutput, actualOutput);
		
		// No change in number of tasks
		System.out.println(taskList.size());
		assertTrue(taskList.size() == 3);
		
		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}
}
