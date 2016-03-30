package logic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class LogicTest {
	
	private Logic logic = new Logic();
	
	@Test // Test delete all - also helps to delete any previous tasks
	public void testA() {
		logic.run("delete all");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("All tasks deleted.");
		
		assertEquals(logic.getOutput(), expectedOutput);
		assertEquals(true, logic.getTaskList().isEmpty());
		assertEquals(true, logic.getUndoList().isEmpty());
		assertEquals(true, logic.getRedoList().isEmpty());
		assertEquals(true, logic.getLastOutputTaskList().isEmpty());
	}
	
	@Test // Test add
	public void testB() {
		logic.run("add CS2103 v0.5");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CS2103 v0.5");
		
		assertEquals(logic.getOutput(), expectedOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getUndoList().size());
		assertEquals(0, logic.getRedoList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
	}
	
	@Test // Test edit
	public void testC() {
		logic.run("edit 1 by 11/4");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Added date '2016-04-11' to task 'CS2103 v0.5'.");
		
		assertEquals(logic.getOutput(), expectedOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(2, logic.getUndoList().size());
		assertEquals(0, logic.getRedoList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
	}
	
	@Test // Test undo
	public void testD() {
		logic.run("undo");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Edit undone.");
		
		assertEquals(logic.getOutput(), expectedOutput);
	}
	
	
}
