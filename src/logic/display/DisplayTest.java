//@@author A0124636H

package logic.display;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Test;

import common.TaskObject;

public class DisplayTest {

	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject>();

	@Test // Empty task list
	public void testA() {
		Display displayTest = new Display(testList);
		displayTest.run();

		assertEquals("Task list is empty.", displayTest.getOutput().get(0));
	}

	@Test // Displaying all tasks
	public void testB() {
		testList.add(new TaskObject("Be great in life", "floating", "incomplete", 1));
		testList.add(new TaskObject("Clear Year 2 IPPT",
				LocalDateTime.of(LocalDate.parse("2016-08-17"), LocalTime.MAX), "deadline", "incomplete", 2));
		testList.add(new TaskObject("ATAP internship",
				LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.MAX),
				LocalDateTime.of(LocalDate.parse("2016-10-21"), LocalTime.MAX), "event", "incomplete", 3));

		Display displayTest = new Display(testList);
		displayTest.run();

		assertEquals("Displaying all tasks.", displayTest.getOutput().get(0));
		assertEquals(3, displayTest.getLastOutputTaskList().size());
	}
	
	@Test // Run a specific list
	public void testC() {
		Display displayTest = new Display();
		displayTest.runSpecificList(testList);
		
		assertEquals("Displaying all tasks.", displayTest.getOutput().get(0));
		assertEquals(3, displayTest.getLastOutputTaskList().size());
		assertEquals(-1, displayTest.getLastSearchedIndex());
	}

}
