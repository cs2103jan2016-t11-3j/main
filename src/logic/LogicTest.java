//@@author A0124636H

package logic;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogicTest {
	
	private static Logic logic = new Logic();
	
	@Test // Test delete all - also helps to delete any previous tasks
	public void testAA() {
		logic.run("delete all");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("All tasks deleted. Undo and redo lists are cleared.");
		
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
		logic.run("edit 1 by 15/4");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Added date '2016-04-15' to task 'CS2103 v0.5'.");
		
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
		assertEquals(6, logic.getTaskList().get(0).getTaskDateTimes().size());
	}
	
//@@author A0124052X
	
	@Test // Invalid delete
	public void testAF() {
		logic.run("delete");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Please specify a task index to delete.");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getTaskList().size());
		assertEquals(2, logic.getLastOutputTaskList().size());
	}
	
	@Test // Test normal deletion
	public void testAG() {
		logic.run("delete 2");
		
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted: CS2103 v0.5");
		
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getLastOutputTaskList().size());
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
		assertEquals(2, logic.getTaskList().size());
		assertEquals(2, logic.getLastOutputTaskList().size());
		assertEquals(originalNumberOfOccurrences - 1, logic.getTaskList().get(0).getTaskDateTimes().size());
	}
	
	@Test // test search for title
	public void testAI() {
		logic.run("add CS2103 lecture every friday from 4pm to 6pm until 1 May");
		logic.run("add assignment 1 by 30/4 4pm");
		logic.run("add IE2100 lecture every saturday from 1pm to 2pm");
		
		assertEquals(5, logic.getTaskList().size());
		
		for (int i = 0; i < logic.getTaskList().size(); i++) {
			System.out.println(logic.getTaskList().get(i).getTitle());
		}
		
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
		assertEquals(3, logic.getLastOutputTaskList().size());
		
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
		assertEquals(6, logic.getLastOutputTaskList().size());
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
		assertEquals(5, logic.getLastOutputTaskList().size());
	}
	
	@Test // delete all occurrences
	public void testAM() {
		logic.run("delete 2 all");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All occurrences of task \'event\' deleted.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(4, logic.getLastOutputTaskList().size());
		assertEquals(5, logic.getTaskList().size());
	}
	
	@Test // delete done
	public void testAN() {
		logic.run("delete done");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All completed tasks deleted.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(4, logic.getLastOutputTaskList().size());
		assertEquals(4, logic.getTaskList().size());
	}
	
	@Test // search date
	public void testAO() {
		logic.run("search 30/4");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'2016-04-30\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(4, logic.getTaskList().size());
	}
	
	@Test // edit title
	public void testAP() {
		logic.run("display");
		logic.run("edit 3 IE3100 Lecture");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Title edited from \'IE2100 lecture\' to \'IE3100 Lecture\'.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(4, logic.getLastOutputTaskList().size());
	}
	
	@Test // edit date and time
	public void testAQ() {
		logic.run("edit 4 by 1900hrs 25/4");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Date edited from \'2016-04-30\' to \'2016-04-25\'. \nTime edited from \'16:00\' to \'19:00\'.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(4, logic.getLastOutputTaskList().size());
	}
	
	@Test // search date and time
	public void testAR() {
		logic.run("search 25/4 1900hrs");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Displaying tasks for the search parameters:\n\'2016-04-25\', \'19:00\'");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
	}
	
	@Test // load backup
	public void testAS() {
		logic.run("load backup");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Loaded file from: backup");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(4, logic.getLastOutputTaskList().size());
		assertEquals(4, logic.getTaskList().size());
	}

	@Test // test edit time end
	public void testAT() {
		logic.run("delete all");
		logic.run("add new event from 30/5 4pm to 10pm");
		logic.run("edit 1 11pm end");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("End time edited from \'22:00\' to \'23:00\'.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(1, logic.getTaskList().size());
	}
	
	@Test // test edit time start
	public void testAU() {
		logic.run("edit 1 6pm start");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Start time edited from \'16:00\' to \'18:00\'.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(1, logic.getTaskList().size());
	}
	
	@Test // test failed help
	public void testAV() {
		logic.run("help blah blah");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Sorry, the topic you requested is not available");
		assertEquals(expectedOutput, logic.getOutput());
	}
	
	@Test // test edit interval
	public void testAW() {
		logic.run("delete all");
		logic.run("add event every saturday from 4pm to 6pm for 6 weeks");
		logic.run("edit 1 every sunday from 3pm to 5pm");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Start date edited from \'2016-04-16\' to \'2016-04-17\'. \nStart time edited from \'16:00\' to \'15:00\'. \nEnd date edited from \'2016-04-16\' to \'2016-04-17\'. \nEnd time edited from \'18:00\' to \'17:00\'. \nInterval edited.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(1, logic.getTaskList().size());
	}
	
	@Test // delete all occurrences
	public void testAX() {
		logic.run("delete all 1");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("All occurrences of task \'event\' deleted.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(0, logic.getLastOutputTaskList().size());
		assertEquals(0, logic.getTaskList().size());
	}
	
	@Test // add overdue deadline
	public void testAY() {
		logic.run("add deadline by today 0000hrs");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: deadline. Task added is overdue.");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(1, logic.getTaskList().size());
	}
	
	@Test // add clashing event
	public void testAZ() {
		logic.run("delete all");
		logic.run("add event 1 from today 0000hrs to 0040hrs");
		logic.run("add event 2 from today 0000hrs to 0040hrs");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task added: event 2. Task added is overdue.\nTask: event 2 clashes with event 1. ");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(2, logic.getLastOutputTaskList().size());
		assertEquals(2, logic.getTaskList().size());
	}
	
	@Test // test incomplete
	public void testBA() {
		logic.run("delete all");
		logic.run("add deadline by 6 june");
		logic.run("done 1");
		logic.run("incomplete 1");
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Task: \'deadline\' is marked as incomplete");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(1, logic.getLastOutputTaskList().size());
		assertEquals(1, logic.getTaskList().size());
	}
	
	@Test // test save as and load from
	public void testBB() {
		Path path = Paths.get(".\\atf_files");
		String directory = path.toString();
		logic.run("save as " + directory);
		
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Tasks have been saved to " + directory);
		assertEquals(expectedOutput, logic.getOutput());
		
		logic.run("load from" + directory + "\\filecopy.txt.txt");
		expectedOutput.clear();
		expectedOutput.add("Loaded file from:\n" + directory + "\\filecopy.txt.txt");
	}
	
	@Test // failed load
	public void testZZ() {
		logic.run("load from blahblahblah");
		ArrayList<String> expectedOutput = new ArrayList<String> ();
		expectedOutput.add("Invalid file path used");
		assertEquals(expectedOutput, logic.getOutput());
		assertEquals(0, logic.getLastOutputTaskList().size());
		assertEquals(0, logic.getTaskList().size());
	}
}
