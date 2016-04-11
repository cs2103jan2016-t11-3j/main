//@@ author A0124052X
package logic.mark;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.CommandObject;
import common.TaskObject;

import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OverdueTest {

	private final TaskObject taskOne = new TaskObject("Find calculator", "floating", "undone", 200);
	private final TaskObject taskTwo = new TaskObject("Find money", "floating", "undone", 3);
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject> ();
	private ArrayList<TaskObject> lastOutput = new ArrayList<TaskObject> ();
	
	@Test
	public void testA() {
		taskList.add(taskOne);
		taskList.add(taskTwo);
		lastOutput.add(taskOne);
		TaskObject test = new TaskObject("1");
		CommandObject commandObj = new CommandObject(-1, test, 1);
		Overdue overdue = new Overdue(commandObj, taskList, lastOutput);
		ArrayList<String> actualOutput = overdue.run();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		
		expectedOutput.add("Task: \'Find calculator\' is marked as overdue");
		assertEquals(expectedOutput, actualOutput);
	}

}
