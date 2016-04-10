//@@author A0124052X

package logic.delete;

import static logic.constants.Index.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Deque;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.CommandObject;
import common.LocalDateTimePair;
import common.TaskObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DeleteTest {
	
	private ArrayList<LocalDateTimePair> testTimingsOne = new ArrayList<LocalDateTimePair>();
	private ArrayList<LocalDateTimePair> testTimingsTwo = new ArrayList<LocalDateTimePair>();

	private final ArrayList<TaskObject> testArray = new ArrayList<TaskObject> ();
	private final ArrayList<TaskObject> lastOutputList = new ArrayList<TaskObject> ();
	private TaskObject taskOne = new TaskObject("Hello", 200);
	private TaskObject taskTwo = new TaskObject("Nonsense", 178);
	private TaskObject taskThree = new TaskObject("Dinner tonight", LocalDateTime.of(2016, 02, 26, 19, 00), "deadline", "incomplete", 24);
	private CommandObject delete = new CommandObject(INDEX_DELETE, new TaskObject(), 1);
	private CommandObject deleteFail = new CommandObject(INDEX_DELETE, new TaskObject(), 2);
	private TaskObject deleteQuick = new TaskObject();
	private Deque<CommandObject> testUndoList = new ArrayDeque<CommandObject> ();

	/* For all NORMAL Delete **************************************************/
	@Test
	// Delete an applicable task
	public void testSuccess() {
		testArray.add(taskOne);
		testArray.add(taskTwo);
		testArray.add(taskThree);
		
		lastOutputList.add(taskOne);
		lastOutputList.add(taskThree);
		Delete deleteFirst = new Delete(delete, testArray, lastOutputList);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task deleted: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Delete an inapplicable task
	public void testFail() {
		testArray.add(taskOne);		
		Delete deleteFirst = new Delete(deleteFail, testArray, lastOutputList);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Error deleting task. Requested index does not exist");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	/* For all QUICK Delete **************************************************
	@Test
	//Failed Quick delete test(One try)
	public void testQuickFail() {
		testArray.add(taskOne);
		testUndoList.push(new CommandObject(INDEX_ADD, deleteQuick));
		CommandObject cmd = new CommandObject(INDEX_DELETE, new TaskObject(), -1);
		Delete deleteLast = new Delete(cmd, testArray, testUndoList);
		ArrayList<String> actualOutput = deleteLast.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Quick delete unavailable.");
		
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	//Successful Quick delete test(One try)
	public void testQuickSuccess() {
		testArray.add(taskOne);
		testUndoList.push(new CommandObject(INDEX_DELETE, deleteQuick));
		CommandObject cmd = new CommandObject(INDEX_DELETE, new TaskObject(), -1);
		Delete deleteLast = new Delete(cmd, testArray, testUndoList);
		ArrayList<String> actualOutput = deleteLast.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}
	@Test
	// Multiple quick deletes (Two tries, first success, second fail)
	public void testMultipleQuick() {
		testArray.add(taskOne);
		testArray.add(taskTwo);
		testArray.add(taskThree);
		testUndoList.push(new CommandObject(INDEX_DELETE, deleteQuick));
		CommandObject cmd = new CommandObject(INDEX_DELETE, new TaskObject(), -1);
		Delete deleteOne = new Delete(cmd, testArray, testUndoList);
		ArrayList<ArrayList<String> > actualOutput = new ArrayList<ArrayList<String> > ();
		actualOutput.add(deleteOne.run());
		testUndoList.push(new CommandObject(INDEX_ADD, new TaskObject()));
		CommandObject cmdx = new CommandObject(INDEX_DELETE, new TaskObject(), -1);
		Delete deleteTwo = new Delete(cmdx, testArray, testUndoList);
		// Dummy add command to simulate the effect of deleting a task 
		actualOutput.add(deleteTwo.run());
		
		ArrayList<ArrayList<String> > expectedOutput = new ArrayList<ArrayList<String> > ();
		ArrayList<String> firstExpectedOutput = new ArrayList<String> ();
		firstExpectedOutput.add("Task deleted: Dinner tonight");
		expectedOutput.add(firstExpectedOutput);
		ArrayList<String> secondExpectedOutput = new ArrayList<String> ();
		secondExpectedOutput.add("Quick delete unavailable.");
		expectedOutput.add(secondExpectedOutput);
		
		assertEquals(expectedOutput, actualOutput);
	}*/

//@@author A0124636H
	
	@Test
	// Delete all
	public void testDeleteAll() {
		testArray.add(taskOne);
		testArray.add(taskTwo);
		testArray.add(taskThree);
		CommandObject cmd = new CommandObject(INDEX_DELETE, new TaskObject(), 0);
		Delete deleteAll = new Delete(cmd, testArray, testUndoList);
		ArrayList<String> actualOutput = new ArrayList<String> ();
		actualOutput = deleteAll.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All tasks deleted. Undo and redo lists are cleared.");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// --------------------------- DELETE FOR RECURRING TASK ---------------------------
	public void testDeleteRecurrence() {
		testArray.clear();
		
		// For recurrence task 1
		LocalDateTime startOne = LocalDateTime.of(LocalDate.parse("2016-03-25"), LocalTime.parse("16:00"));
		LocalDateTime endOne = LocalDateTime.of(LocalDate.parse("2016-03-25"), LocalTime.parse("18:00"));
		LocalDateTime startTwo = LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.parse("16:00"));
		LocalDateTime endTwo = LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.parse("18:00"));
		LocalDateTime startThree = LocalDateTime.of(LocalDate.parse("2016-04-08"), LocalTime.parse("16:00"));
		LocalDateTime endThree = LocalDateTime.of(LocalDate.parse("2016-04-08"), LocalTime.parse("18:00"));
		LocalDateTime startFour = LocalDateTime.of(LocalDate.parse("2016-04-15"), LocalTime.parse("16:00"));
		LocalDateTime endFour = LocalDateTime.of(LocalDate.parse("2016-04-15"), LocalTime.parse("18:00"));
		LocalDateTimePair pairOne = new LocalDateTimePair(startOne, endOne);
		LocalDateTimePair pairTwo = new LocalDateTimePair(startTwo, endTwo);		
		LocalDateTimePair pairThree = new LocalDateTimePair(startThree, endThree);		
		LocalDateTimePair pairFour = new LocalDateTimePair(startFour, endFour);		
		testTimingsOne.add(pairOne);
		testTimingsOne.add(pairTwo);
		testTimingsOne.add(pairThree);
		testTimingsOne.add(pairFour);
		
		TaskObject recurrenceTaskOne = new TaskObject("CS2103 lecture", startOne, endFour, "event", "incomplete", 
				1, true, testTimingsOne);
		testArray.add(recurrenceTaskOne);
		
		// For recurrence task 2
		startOne = LocalDateTime.of(LocalDate.parse("2016-09-03"), LocalTime.parse("22:00"));
		endOne = LocalDateTime.of(LocalDate.parse("2016-09-03"), LocalTime.parse("23:30"));
		startTwo = LocalDateTime.of(LocalDate.parse("2016-09-10"), LocalTime.parse("22:00"));
		endTwo = LocalDateTime.of(LocalDate.parse("2016-09-10"), LocalTime.parse("23:30"));
		startThree = LocalDateTime.of(LocalDate.parse("2016-09-17"), LocalTime.parse("22:00"));
		endThree = LocalDateTime.of(LocalDate.parse("2016-09-17"), LocalTime.parse("23:30"));
		pairOne = new LocalDateTimePair(startOne, endOne);
		pairTwo = new LocalDateTimePair(startTwo, endTwo);
		pairThree = new LocalDateTimePair(startThree, endThree);
		testTimingsTwo.add(pairOne);
		testTimingsTwo.add(pairTwo);
		testTimingsTwo.add(pairThree);
		
		TaskObject recurrenceTaskTwo = new TaskObject("Soccer match", startOne, endThree, "event", "incomplete",
				2, true, testTimingsTwo);
		testArray.add(recurrenceTaskTwo);
		
		CommandObject testCmdObj = new CommandObject(INDEX_DELETE, new TaskObject(), 1);
		Delete delete = new Delete(testCmdObj, testArray, testArray);
		delete.run();
		
		ArrayList<String> actualOutput = delete.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Most recent occurrence of task 'CS2103 lecture' deleted.");
		
		// check if it is the correct occurrence that has been removed
		LocalDateTimePair removedTaskOccurrenceDetails = delete.getRemovedTaskOccurrenceDetails();
		LocalDateTime start = removedTaskOccurrenceDetails.getStartDateTime();
		LocalDateTime end = removedTaskOccurrenceDetails.getEndDateTime();
		assertEquals(start.toString(), "2016-03-25T16:00");
		assertEquals(end.toString(), "2016-03-25T18:00");
		
		assertEquals(expectedOutput.get(0), actualOutput.get(1));
		assertEquals(3, delete.getRemovedTask().getTaskDateTimes().size()); // new size of arrlist after first occurrence is removed
	}
	
	@Test
	public void testRecurrenceDeleteTwo() {
		LocalDateTime startOne = LocalDateTime.of(LocalDate.parse("2016-03-25"), LocalTime.parse("16:00"));
		LocalDateTime endFour = LocalDateTime.of(LocalDate.parse("2016-04-15"), LocalTime.parse("18:00"));
		startOne = LocalDateTime.of(LocalDate.parse("2016-09-03"), LocalTime.parse("22:00"));
		LocalDateTime endThree = LocalDateTime.of(LocalDate.parse("2016-09-17"), LocalTime.parse("23:30"));
		
		TaskObject recurrenceTaskOne = new TaskObject("CS2103 lecture", startOne, endFour, "event", "incomplete", 
				1, true, testTimingsOne);
		TaskObject recurrenceTaskTwo = new TaskObject("Soccer match", startOne, endThree, "event", "incomplete",
				2, true, testTimingsTwo);
		testArray.add(recurrenceTaskOne);
		testArray.add(recurrenceTaskTwo);
		
		CommandObject testCmdObj = new CommandObject(INDEX_DELETE, new TaskObject("all"), 2);
		Delete delete = new Delete(testCmdObj, testArray, testArray);
		delete.run();
		
		ArrayList<String> actualOutput = delete.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Only one occurrence remaining. All occurrences of task 'Soccer match' deleted.");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(1, delete.getTaskList().size());
	}
	
}
