package logic.delete;

import logic.*;
import storage.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class DeleteTest {

	private final ArrayList<TaskObject> testArray = new ArrayList<TaskObject> ();
	private TaskObject taskOne = new TaskObject("Hello", 200);
	
	@Test
	// Delete an applicable task
	public void testSuccess() {
		testArray.add(taskOne);
		Delete deleteFirst = new Delete(taskOne, testArray);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task deleted from TaskFinder: Hello");
		
		assertEquals(expectedOutput, actualOutput);
	}

	@Test
	// Delete an inapplicable task
	public void testFail() {
		testArray.add(taskOne);
		
		TaskObject taskTwo = new TaskObject("LOL", 1);
		
		Delete deleteFirst = new Delete(taskTwo, testArray);
		ArrayList<String> actualOutput = deleteFirst.run();
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Error deleting task: LOL from TaskFinder");
		
		assertEquals(expectedOutput, actualOutput);
	}
}
