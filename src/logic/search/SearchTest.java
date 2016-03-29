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
	
	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	private ArrayList<LocalDateTimePair> testTimings = new ArrayList<LocalDateTimePair>();
	
	TaskObject one = new TaskObject("Buy new washing machine", LocalDateTime.of(LocalDate.parse("2016-06-04"), LocalTime.parse("15:30")),
			"deadline", "incomplete", 1);
	TaskObject two = new TaskObject("Army", LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("12:00")),
			LocalDateTime.of(LocalDate.parse("2013-11-29"), LocalTime.parse("12:00")), "event", "completed", 2);
	TaskObject three = new TaskObject("Army reservist", LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.parse("12:00")),
			LocalDateTime.of(LocalDate.parse("2016-11-08"), LocalTime.parse("18:30")), "event", "incomplete", 3);
	TaskObject four = new TaskObject("Go to the army shop", "floating", "incomplete", 4);
	TaskObject five = new TaskObject("SSS1207 CA2", LocalDateTime.of(LocalDate.parse("2016-03-31"), LocalTime.parse("16:00")), "deadline", "incomplete", 5);
	TaskObject six = new TaskObject("CS2106 Assignment 2", LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.parse("18:00")), "deadline", "incomplete", 6);
	TaskObject seven = new TaskObject("Dinner and Dance", LocalDateTime.of(LocalDate.parse("2016-04-02"), LocalTime.parse("19:00")), 
			LocalDateTime.of(LocalDate.parse("2016-04-02"), LocalTime.parse("22:00")), "event", "incomplete", 7);
	
	LocalDateTime startOne = LocalDateTime.of(LocalDate.parse("2016-03-25"), LocalTime.parse("16:00"));
	LocalDateTime endOne = LocalDateTime.of(LocalDate.parse("2016-03-25"), LocalTime.parse("18:00"));
	LocalDateTime startTwo = LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.parse("16:00"));
	LocalDateTime endTwo = LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.parse("18:00"));
	LocalDateTime startThree = LocalDateTime.of(LocalDate.parse("2016-04-08"), LocalTime.parse("16:00"));
	LocalDateTime endThree = LocalDateTime.of(LocalDate.parse("2016-04-08"), LocalTime.parse("18:00"));
	LocalDateTime startFour = LocalDateTime.of(LocalDate.parse("2016-04-15"), LocalTime.parse("16:00"));
	LocalDateTime endFour = LocalDateTime.of(LocalDate.parse("2016-04-15"), LocalTime.parse("18:00"));
	LocalDateTimePair pairOne = new LocalDateTimePair(startOne, endOne);
	LocalDateTimePair pairTwo = new LocalDateTimePair(startTwo, endTwo);		
	LocalDateTimePair pairThree = new LocalDateTimePair(startThree, endThree);		
	LocalDateTimePair pairFour = new LocalDateTimePair(startFour, endFour);
	TaskObject eight = new TaskObject("CS2103 lecture", startOne, endFour, "event", "incomplete", 13, true, testTimings);
	
	
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
	}
	
	@Test // Search by title
	public void testA() {
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
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();

		// Getting actual output
		TaskObject test = new TaskObject(LocalDateTime.of(LocalDate.MAX, LocalTime.parse("12:00")));
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();
		
		assertEquals(0, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles, actualMatchedTasks);
	}
	
	@Test // Search by time, but now with date (should work)
	public void testD() {
		// Adding expected output
		ArrayList<TaskObject> actualMatchedTasks = new ArrayList<TaskObject>();
		ArrayList<String> expectedMatchedTasksTitles = new ArrayList<String>();
		expectedMatchedTasksTitles.add(three.getTitle());
		
		// Getting actual output
		TaskObject test = new TaskObject(LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.parse("12:00")));
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
		TaskObject test = new TaskObject("deadline", "");
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();		
		
		assertEquals(3, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(1), actualMatchedTasks.get(1).getTitle());
		assertEquals(expectedMatchedTasksTitles.get(2), actualMatchedTasks.get(2).getTitle());
	}
	
	@Test // Search by index
	public void testF() {
		
	}
}
