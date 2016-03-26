package logic.search;
import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.TaskObject;
import common.CommandObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static logic.constants.Index.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SearchTest {
	
	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	
	TaskObject one = new TaskObject("Buy new washing machine", LocalDateTime.of(LocalDate.parse("2016-06-04"), LocalTime.parse("15:30")),
			"deadline", "incomplete", 1);
	TaskObject two = new TaskObject("Army", LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("12:00")),
			LocalDateTime.of(LocalDate.parse("2013-11-29"), LocalTime.parse("12:00")), "event", "completed", 2);
	TaskObject three = new TaskObject("Army reservist", LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.parse("12:00")),
			LocalDateTime.of(LocalDate.parse("2016-11-08"), LocalTime.parse("18:30")), "event", "incomplete", 3);
	TaskObject four = new TaskObject("I love army", "floating", "incomplete", 4);

	@Test
	public void populate() {
		
		// Adding test values
		testList.add(one);
		testList.add(two);
		testList.add(three);
		testList.add(four);
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
		
		TaskObject test = new TaskObject(LocalDateTime.of(LocalDate.parse("2016-05-09"), LocalTime.parse("12:00")));
		Search testSearch = new Search(new CommandObject(INDEX_SEARCH_DISPLAY, test), testList, testList);
		testSearch.run();
		actualMatchedTasks = testSearch.getMatchedTasks();		
		
		assertEquals(1, actualMatchedTasks.size());
		assertEquals(expectedMatchedTasksTitles.get(0), actualMatchedTasks.get(0).getTitle());
	}
	
	@Test // Search by index
	public void testE() {
		
	}
}
