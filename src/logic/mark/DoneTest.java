package logic.mark;

import static org.junit.Assert.*;
import org.junit.Test;

import common.TaskObject;
import logic.*;

import java.util.ArrayList;

public class DoneTest {
	
	private final TaskObject taskOne = new TaskObject("Find calculator", "floating", "undone", 200);
	private final TaskObject taskTwo = new TaskObject("Find money", "floating", "undone", 3);
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject> ();
	private ArrayList<TaskObject> lastOutput = new ArrayList<TaskObject> ();

	@Test
	public void testDone() {
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		ArrayList<String> actualOutput = new ArrayList<String> ();
		TaskObject taskTest = new TaskObject("1");
		taskList.add(taskOne);
		taskList.add(taskTwo);
		lastOutput.add(taskOne);
		Done doneTest = new Done(taskTest, taskList, lastOutput);
		actualOutput = doneTest.run();
		expectedOutput.add("Task: Find calculator marked as completed");
		assertEquals(expectedOutput, actualOutput);
	}

}
