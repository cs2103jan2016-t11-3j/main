//@@author A0124636H

package logic.search;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.TaskObject;
import common.CommandObject;
import common.LocalDateTimePair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static logic.constants.Index.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SearchTest {

	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject>();
	private ArrayList<LocalDateTimePair> testTimings = new ArrayList<LocalDateTimePair>();
	private ArrayList<LocalDateTimePair> testTimingsTwo = new ArrayList<LocalDateTimePair>();

	TaskObject one = new TaskObject("Buy new washing machine",
			LocalDateTime.of(LocalDate.parse("2016-06-04"), LocalTime.parse("15:30")), "deadline",
			"incomplete", 1);
	TaskObject two = new TaskObject("Army",
			LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("12:00")),
			LocalDateTime.of(LocalDate.parse("2013-11-29"), LocalTime.parse("12:00")), "event", "complete",
			2);
	TaskObject three = new TaskObject("Army reservist",
			LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.parse("12:00")),
			LocalDateTime.of(LocalDate.parse("2016-11-08"), LocalTime.parse("18:30")), "event", "incomplete",
			3);
	TaskObject four = new TaskObject("Go to the army shop", "floating", "incomplete", 4);
	TaskObject five = new TaskObject("SSS1207 CA2",
			LocalDateTime.of(LocalDate.parse("2016-03-31"), LocalTime.parse("16:00")), "deadline", "complete",
			5);
	TaskObject six = new TaskObject("CS2106 Assignment 2",
			LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.parse("18:00")), "deadline", "complete",
			6);
	TaskObject seven = new TaskObject("Dinner and Dance",
			LocalDateTime.of(LocalDate.parse("2016-04-02"), LocalTime.parse("19:00")),
			LocalDateTime.of(LocalDate.parse("2016-04-02"), LocalTime.parse("22:00")), "event", "complete",
			7);

	LocalDateTime startOne = LocalDateTime.of(LocalDate.parse("2017-03-25"), LocalTime.parse("16:00"));
	LocalDateTime endOne = LocalDateTime.of(LocalDate.parse("2017-03-25"), LocalTime.parse("18:00"));
	LocalDateTime startTwo = LocalDateTime.of(LocalDate.parse("2017-04-01"), LocalTime.parse("16:00"));
	LocalDateTime endTwo = LocalDateTime.of(LocalDate.parse("2017-04-01"), LocalTime.parse("18:00"));
	LocalDateTime startThree = LocalDateTime.of(LocalDate.parse("2017-04-08"), LocalTime.parse("16:00"));
	LocalDateTime endThree = LocalDateTime.of(LocalDate.parse("2017-04-08"), LocalTime.parse("18:00"));
	LocalDateTime startFour = LocalDateTime.of(LocalDate.parse("2017-04-15"), LocalTime.parse("16:00"));
	LocalDateTime endFour = LocalDateTime.of(LocalDate.parse("2017-04-15"), LocalTime.parse("18:00"));
	LocalDateTimePair pairOne = new LocalDateTimePair(startOne, endOne);
	LocalDateTimePair pairTwo = new LocalDateTimePair(startTwo, endTwo);
	LocalDateTimePair pairThree = new LocalDateTimePair(startThree, endThree);
	LocalDateTimePair pairFour = new LocalDateTimePair(startFour, endFour);
	TaskObject eight = new TaskObject("CS2103 lecture", startOne, endFour, "event", "incomplete", 13, true,
			testTimings);
	
	LocalDateTime startFive = LocalDateTime.of(LocalDate.parse("2016-05-01"), LocalTime.MAX);
	LocalDateTime startSix = LocalDateTime.of(LocalDate.parse("2016-06-01"), LocalTime.MAX);
	LocalDateTime startSeven = LocalDateTime.of(LocalDate.parse("2016-07-01"), LocalTime.MAX);
	LocalDateTimePair pairFive = new LocalDateTimePair(startFive, LocalDateTime.MAX);
	LocalDateTimePair pairSix = new LocalDateTimePair(startSix, LocalDateTime.MAX);
	LocalDateTimePair pairSeven = new LocalDateTimePair(startSeven, LocalDateTime.MAX);
	TaskObject nine = new TaskObject("Take IPPT", startFive, startSeven, "deadline", "incomplete", 14, true,
			testTimingsTwo);
	
	
	@Test
	public void populate() {

		// Adding test values
		testList.add(one);
		testList.add(two);
		testList.add(three);
		testList.add(four);
		testList.add(five);
		testList.add(six);
		testList.add(seven);

		testTimings.add(pairOne);
		testTimings.add(pairTwo);
		testTimings.add(pairThree);
		testTimings.add(pairFour);
		testList.add(eight);
		
		testTimingsTwo.add(pairFive);
		testTimingsTwo.add(pairSix);
		testTimingsTwo.add(pairSeven);
		testList.add(nine);
	}

	@Test // Search by title, where search keyword is 1 word
	public void testAA() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(two.getTitle());
		expectedMatchedTasksTitles.add(three.getTitle());
		expectedMatchedTasksTitles.add(four.getTitle());

		// Getting actual output
		TaskObject test = new TaskObject("army");
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();

		assertEquals(3, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(1), actualMatchedTasks.get(1).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(2), actualMatchedTasks.get(2).getTitle());
	}

	@Test // Search by title, where search keyword is >1 word
	public void testAB() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(seven.getTitle());

		// Getting actual output
		TaskObject test = new TaskObject("dinner dance");
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();

		assertEquals(1, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
	}

	@Test // Search by date
	public void testB() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(two.getTitle());

		// Getting actual output
		TaskObject test = new TaskObject(LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.MAX));
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();

		assertEquals(1, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
	}

	@Test // Search by time without search by date (should not work)
	public void testC() {
		// Getting actual output
		TaskObject test = new TaskObject(LocalDateTime.of(LocalDate.MAX, LocalTime.parse("12:00")));
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();

		// Output should be the SearchException message
		assertEquals("Search by time requires a search by date as well.", testSearch.getOutput().get(0));
	}

	@Test // Search by time, but now with date (should work)
	public void testD() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(three.getTitle());

		// Getting actual output
		TaskObject test = new TaskObject(
				LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.parse("12:00")));
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();

		assertEquals(1, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
	}

	@Test // Search by category
	public void testE() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(one.getTitle());
		expectedMatchedTasksTitles.add(five.getTitle());
		expectedMatchedTasksTitles.add(six.getTitle());

		// Getting actual output
		TaskObject test = new TaskObject("", "deadline", "", -1);
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();

		assertEquals(4, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(1), actualMatchedTasks.get(1).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(2), actualMatchedTasks.get(2).getTitle());
	}

	@Test // Search by index for recurring event
	public void testFA() {
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, new TaskObject(), 8), testList,
				testList);
		testSearch.run();

		assertEquals(5, testSearch.getTaskDateTimeOutput().size());
		assertEquals("Displaying recurrence timings for task 8.", testSearch.getOutput().get(0));
		assertEquals("Timings for CS2103 lecture:", testSearch.getTaskDateTimeOutput().get(0));
		assertEquals("1. from 16:00 to 18:00 \non 25/03/17", testSearch.getTaskDateTimeOutput().get(1));
		assertEquals("2. from 16:00 to 18:00 \non 01/04/17", testSearch.getTaskDateTimeOutput().get(2));
		assertEquals("3. from 16:00 to 18:00 \non 08/04/17", testSearch.getTaskDateTimeOutput().get(3));
		assertEquals("4. from 16:00 to 18:00 \non 15/04/17", testSearch.getTaskDateTimeOutput().get(4));
	}
	
	@Test // Search by index for recurring deadline
	public void testFB() {
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, new TaskObject(), 9), testList,
				testList);
		testSearch.run();

		assertEquals(4, testSearch.getTaskDateTimeOutput().size());
		assertEquals("Displaying recurrence timings for task 9.", testSearch.getOutput().get(0));
		assertEquals("Timings for Take IPPT:", testSearch.getTaskDateTimeOutput().get(0));
		assertEquals("1. by 01/05/16", testSearch.getTaskDateTimeOutput().get(1));
		assertEquals("2. by 01/06/16", testSearch.getTaskDateTimeOutput().get(2));
		assertEquals("3. by 01/07/16", testSearch.getTaskDateTimeOutput().get(3));
	}
	
	@Test // Search by index for normal event
	public void testFC() {
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, new TaskObject(), 2), testList,
				testList);
		testSearch.run();
		
		assertEquals(2, testSearch.getTaskDateTimeOutput().size());
		assertEquals("Task 2 has no recurrence timings to be displayed.", testSearch.getOutput().get(0));
		
	}
	
	@Test // Search by index for normal deadline
	public void testFD() {
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, new TaskObject(), 5), testList,
				testList);
		testSearch.run();
		
		assertEquals(2, testSearch.getTaskDateTimeOutput().size());
		assertEquals("Task 5 has no recurrence timings to be displayed.", testSearch.getOutput().get(0));
		
	}


	@Test // Test for out of bounds index
	public void testFE() {
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, new TaskObject(), 20),
				testList, testList);
		testSearch.run();

		assertEquals("No such task index found.", testSearch.getOutput().get(0));
	}
	
	@Test // Search by status
	public void testG() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(two.getTitle());
		expectedMatchedTasksTitles.add(five.getTitle());
		expectedMatchedTasksTitles.add(six.getTitle());
		expectedMatchedTasksTitles.add(seven.getTitle());

		// Getting actual output
		TaskObject test = new TaskObject("", "", "complete", -1);
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();

		assertEquals(4, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(1), actualMatchedTasks.get(1).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(2), actualMatchedTasks.get(2).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(3), actualMatchedTasks.get(3).getTitle());
	}
	
	@Test // Search with no results found
	public void testH() {
		TaskObject test = new TaskObject("randomest", "", "", -1);
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();

		assertEquals(0, testSearch.getMatchedTasks().size());
		assertEquals("No results found for the specified parameters.", testSearch.getOutput().get(0));
	}
}
