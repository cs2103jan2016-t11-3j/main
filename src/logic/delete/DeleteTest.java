package logic.delete;

import logic.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Test;

public class DeleteTest {

	private final ArrayList<TaskObject> testArray = new ArrayList<TaskObject> ();
	private final ArrayList<TaskObject> lastOutputList = new ArrayList<TaskObject> ();
	private TaskObject taskOne = new TaskObject("Hello", 200);
	private TaskObject taskTwo = new TaskObject("Nonsense", 178);
	private TaskObject taskThree = new TaskObject("Dinner tonight", 20160226, 1900, "deadline", "incomplete", 24);
	private CommandObject delete = new CommandObject(Logic.INDEX_DELETE, new TaskObject(), 1);
	private CommandObject deleteFail = new CommandObject(Logic.INDEX_DELETE, new TaskObject(), 2);
	private TaskObject deleteQuick = new TaskObject("");
	private Stack<CommandObject> testUndoList = new Stack<CommandObject>();

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
		expectedOutput.add("Task deleted from TaskFinder: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Delete an inapplicable task
	public void testFail() {
		testArray.add(taskOne);		
		Delete deleteFirst = new Delete(deleteFail, testArray, lastOutputList);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Error deleting task from TaskFinder");
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	/* For all QUICK Delete **************************************************/
	@Test
	//Failed Quick delete test
	public void testQuickFail() {
		testArray.add(taskOne);
		testUndoList.push(new CommandObject(Logic.INDEX_ADD, deleteQuick));
		Delete deleteLast = new Delete(testArray, testUndoList);
		ArrayList<String> actualOutput = deleteLast.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Error deleting task from TaskFinder");
		
		assertEquals(expectedOutput, actualOutput);
	}
	@Test
	//Successful Quick delete test
	public void testQuickSuccess() {
		testArray.add(taskOne);
		testUndoList.push(new CommandObject(Logic.INDEX_DELETE, deleteQuick));
		Delete deleteLast = new Delete(testArray, testUndoList);
		ArrayList<String> actualOutput = deleteLast.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted from TaskFinder: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}
}
