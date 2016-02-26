package logic.delete;

import logic.*;
import storage.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class DeleteTest {

	private final ArrayList<TaskObject> testArray = new ArrayList<TaskObject> ();
	private final ArrayList<TaskObject> lastOutputList = new ArrayList<TaskObject> ();
	private TaskObject taskOne = new TaskObject("Hello", 200);
	private TaskObject taskTwo = new TaskObject("Nonsense", 178);
	private TaskObject taskThree = new TaskObject("Dinner tonight", 20160226, 1900, "deadline", "incomplete", 24);
	private TaskObject delete = new TaskObject("1");
	
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
		
		TaskObject taskTwo = new TaskObject("3");
		
		Delete deleteFirst = new Delete(taskTwo, testArray, lastOutputList);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Error deleting task 3 from TaskFinder");
		
		assertEquals(expectedOutput, actualOutput);
	}
}
