//@@author A0124636H

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
		expectedOutput.add("Recurring task added: event. ");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getTaskList().size());
		assertEquals(2, logic.getLastOutputTaskList().size());
		assertEquals(6, logic.getTaskList().get(1).getTaskDateTimes().size());
	}
	
//@@author A0124052X
	
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
		logic.run("add run every day by 2359hrs until 30 April");
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
	
	@Test // test search for title
	public void testAI() {
		logic.run("add CS2103 lecture every friday from 4pm to 6pm until 1 May");
		logic.run("add assignment 1 by 31/4 4pm");
		logic.run("add IE2100 lecture every sunday from 1pm to 2pm");
		
		assertEquals(4, logic.getTaskList().size());
		
		logic.run("search lecture");
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'lecture\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getLastOutputTaskList().size());
		
		logic.run("search junk");
		expectedOutput.clear();
		expectedOutput.add("No results found for the specified parameters.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getLastOutputTaskList().size());
	}
	
	@Test // search by category
	public void testAJ() {
		logic.run("search event");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'event\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getLastOutputTaskList().size());
		
		logic.run("search deadline");
		expectedOutput.clear();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'deadline\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getLastOutputTaskList().size());
	}
	
	@Test // marking a task as done
	public void testAK() {
		logic.run("done 1");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task: \'run\' marked as completed");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(5, logic.getLastOutputTaskList().size());
	}
	
	@Test // search by status and display
	public void testAL() {
		logic.run("view completed");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'completed\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		
		logic.run("display");
		expectedOutput.clear();
		expectedOutput.add("Displaying all tasks.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(4, logic.getLastOutputTaskList().size());
	}
	
	@Test // delete all occurrences
	public void testAM() {
		logic.run("delete 2 all");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All occurrences deleted.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(3, logic.getLastOutputTaskList().size());
		assertEquals(4, logic.getTaskList().size());
	}
	
	@Test // delete done
	public void testAN() {
		logic.run("delete done");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All completed tasks deleted.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(3, logic.getLastOutputTaskList().size());
		assertEquals(3, logic.getTaskList().size());
	}
	
	@Test // search time
	public void testAO() {
		logic.run("search 10/4");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'2016-04-10\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(3, logic.getTaskList().size());
	}
	
	@Test // edit title
	public void testAP() {
		logic.run("display");
		logic.run("edit 3 IE3100 Lecture");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Title edited from \'IE2100 lecture\' to \'IE3100 Lecture\'.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(3, logic.getLastOutputTaskList().size());
	}
	
	@Test // edit date and time
	public void testAQ() {
		logic.run("edit 2 by 1900hrs 25/4");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Date edited from \'2016-04-30\' to \'2016-04-25\'. \nTime edited from \'16:00\' to \'19:00\'.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(3, logic.getLastOutputTaskList().size());
	}
}
