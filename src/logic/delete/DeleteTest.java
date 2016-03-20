package logic.delete;

import logic.*;
import logic.constants.Index;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

import org.junit.Test;

import common.CommandObject;
import common.TaskObject;

public class DeleteTest {

	private final ArrayList<TaskObject> testArray = new ArrayList<TaskObject> ();
	private final ArrayList<TaskObject> lastOutputList = new ArrayList<TaskObject> ();
	private TaskObject taskOne = new TaskObject("Hello", 200);
	private TaskObject taskTwo = new TaskObject("Nonsense", 178);
	private TaskObject taskThree = new TaskObject("Dinner tonight", 20160226, 1900, "deadline", "incomplete", 24);
	private CommandObject delete = new CommandObject(Index.INDEX_DELETE, new TaskObject(), 1);
	private CommandObject deleteFail = new CommandObject(Index.INDEX_DELETE, new TaskObject(), 2);
	private TaskObject deleteQuick = new TaskObject("");
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
		expectedOutput.add("Task deleted from AdultTaskFinder: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Delete an inapplicable task
	public void testFail() {
		testArray.add(taskOne);		
		Delete deleteFirst = new Delete(deleteFail, testArray, lastOutputList);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Error deleting task from TaskFinder. Requested index does not exist");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	/* For all QUICK Delete **************************************************/
	@Test
	//Failed Quick delete test(One try)
	public void testQuickFail() {
		testArray.add(taskOne);
		testUndoList.push(new CommandObject(Index.INDEX_ADD, deleteQuick));
		CommandObject cmd = new CommandObject(Index.INDEX_DELETE, new TaskObject(), -1);
		Delete deleteLast = new Delete(cmd, testArray, testUndoList);
		ArrayList<String> actualOutput = deleteLast.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Quick delete unavailable");
		
		assertEquals(expectedOutput, actualOutput);
	}
	@Test
	//Successful Quick delete test(One try)
	public void testQuickSuccess() {
		testArray.add(taskOne);
		testUndoList.push(new CommandObject(Index.INDEX_DELETE, deleteQuick));
		CommandObject cmd = new CommandObject(Index.INDEX_DELETE, new TaskObject(), -1);
		Delete deleteLast = new Delete(cmd, testArray, testUndoList);
		ArrayList<String> actualOutput = deleteLast.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted from AdultTaskFinder: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}
	@Test
	// Multiple quick deletes (Two tries, first success, second fail)
	public void testMultipleQuick() {
		testArray.add(taskOne);
		testArray.add(taskTwo);
		testArray.add(taskThree);
		testUndoList.push(new CommandObject(Index.INDEX_DELETE, deleteQuick));
		CommandObject cmd = new CommandObject(Index.INDEX_DELETE, new TaskObject(), -1);
		Delete deleteOne = new Delete(cmd, testArray, testUndoList);
		ArrayList<ArrayList<String> > actualOutput = new ArrayList<ArrayList<String> > ();
		actualOutput.add(deleteOne.run());
		testUndoList.push(new CommandObject(Index.INDEX_ADD, new TaskObject()));
		CommandObject cmdx = new CommandObject(Index.INDEX_DELETE, new TaskObject(), -1);
		Delete deleteTwo = new Delete(cmdx, testArray, testUndoList);
		// Dummy add command to simulate the effect of deleting a task 
		actualOutput.add(deleteTwo.run());
		
		ArrayList<ArrayList<String> > expectedOutput = new ArrayList<ArrayList<String> > ();
		ArrayList<String> firstExpectedOutput = new ArrayList<String> ();
		firstExpectedOutput.add("Task deleted from AdultTaskFinder: Dinner tonight");
		expectedOutput.add(firstExpectedOutput);
		ArrayList<String> secondExpectedOutput = new ArrayList<String> ();
		secondExpectedOutput.add("Quick delete unavailable");
		expectedOutput.add(secondExpectedOutput);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	// Delete all
	public void testDeleteAll() {
		testArray.add(taskOne);
		testArray.add(taskTwo);
		testArray.add(taskThree);
		CommandObject cmd = new CommandObject(Index.INDEX_DELETE, new TaskObject(), 0);
		Delete deleteAll = new Delete(cmd, testArray, testUndoList);
		ArrayList<String> actualOutput = new ArrayList<String> ();
		actualOutput = deleteAll.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All tasks deleted from AdultTaskFinder");
		
		assertEquals(expectedOutput, actualOutput);
	}
}
