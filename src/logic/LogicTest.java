package logic;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogicTest {
	
	private static Logic logic = new Logic();
	
	@Test // Test delete all - also helps to delete any previous tasks
	public void testAA() {
		logic.run("delete all");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("All tasks deleted.");
		
		assertEquals(logic.getOutput(), expectedOutput);
		assertEquals(true, logic.getTaskList().isEmpty());
		assertEquals(true, logic.getUndoList().isEmpty());
		assertEquals(true, logic.getRedoList().isEmpty());
		assertEquals(true, logic.getLastOutputTaskList().isEmpty());
	}
	
	@Test // Test add non-recurring
	public void testAB() {
		logic.run("add CS2103 v0.5");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CS2103 v0.5. ");
		
		assertEquals(logic.getOutput(), expectedOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getUndoList().size());
		assertEquals(0, logic.getRedoList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
	}
	
	@Test // Test edit
	public void testAC() {
		logic.run("edit 1 by 11/4");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Added date '2016-04-11' to task 'CS2103 v0.5'.");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getTaskList().size());
		assertEquals(2, logic.getUndoList().size());
		assertEquals(0, logic.getRedoList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
	}
	
	@Test // Test undo
	public void testAD() {
		logic.run("undo");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Edit undone.");
		
		assertEquals(logic.getOutput(), expectedOutput);
	}
	
	@Test // Test add recurring
	public void testAE() {
		logic.run("add event every friday from 1500hrs to 1800hrs for 6 weeks");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Recurring Task added: event. ");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getTaskList().size());
		assertEquals(2, logic.getLastOutputTaskList().size());
		assertEquals(6, logic.getTaskList().get(1).getTaskDateTimes().size());
	}
	
	@Test // Test quick delete
	public void testAF() {
		logic.run("delete");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted: event");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
	}
	
	@Test // Test normal deletion
	public void testAG() {
		logic.run("delete 1");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted: CS2103 v0.5");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(0, logic.getTaskList().size());
		assertEquals(0, logic.getLastOutputTaskList().size());
	}
	
	// At this point, taskList is empty
	
	@Test // Test delete 3rd recurring time
	public void testAH() {
		logic.run("add run every day by 7pm until 30 April");
		logic.run("view 1");
		
		int originalNumberOfOccurrences = logic.getTaskList().get(0).getTaskDateTimes().size();
		
		logic.run("delete 3");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Occurrence 3 deleted.");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(originalNumberOfOccurrences - 1, logic.getTaskList().get(0).getTaskDateTimes().size());
	}
	
}
