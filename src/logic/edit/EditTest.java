package logic.edit;
import static org.junit.Assert.*;
import org.junit.Test;

import common.CommandObject;
import common.TaskObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

// Can consider test cases for situations where a time is added, i.e. from MAX to a specified time - how to display that?

public class EditTest {
	
	private static ArrayList<TaskObject> testList = new ArrayList<TaskObject> ();
	private static ArrayList<String> actualOutput = new ArrayList<String>();
	private static ArrayList<String> correctOutput = new ArrayList<String>();
	
	private CommandObject testCommandObject;
	private TaskObject testTaskObject;

	@Test // Populate the task list
	public void test() {
	
		testList.add(new TaskObject("Study hard for finals", LocalDateTime.of(LocalDate.parse("2016-05-25"), LocalTime.parse("09:00")),
				"deadline", "incomplete", 1));
		testList.add(new TaskObject("Find internship in Germany", LocalDateTime.of(LocalDate.parse("2016-12-31"), LocalTime.parse("23:59")),
				"deadline", "incomplete", 2));
		testList.add(new TaskObject("Travel Eastern Europe", "floating", "incomplete", 3));
		testList.add(new TaskObject("CS2106 Assignment 2", LocalDateTime.of(LocalDate.parse("2016-12-01"), LocalTime.parse("18:00")),
				"deadline", "incomplete", 4));
		testList.add(new TaskObject("CS2103 v0.2", LocalDateTime.of(LocalDate.parse("2017-03-24"), LocalTime.parse("19:00")), "deadline", "incomplete", 5));
		testList.add(new TaskObject("SSS1207 CA2", LocalDateTime.of(LocalDate.parse("2016-03-31"), LocalTime.parse("23:59")), "deadline", "incomplete", 6));
		testList.add(new TaskObject("Spring break", LocalDateTime.of(LocalDate.parse("2001-01-11"), LocalTime.parse("00:00")),
				LocalDateTime.of(LocalDate.parse("2016-05-06"), LocalTime.parse("23:59")), "event", "incomplete", 7));
		testList.add(new TaskObject("Overseas paradise", LocalDateTime.of(LocalDate.parse("2012-01-31"), LocalTime.parse("22:24")), 
				LocalDateTime.of(LocalDate.parse("2013-11-29"), LocalTime.parse("17:00")), "event", "complete", 8));
		testList.add(new TaskObject("Hiking trip", LocalDateTime.of(LocalDate.parse("2014-07-11"), LocalTime.parse("16:00")), 
				LocalDateTime.of(LocalDate.parse("2016-07-15"), LocalTime.parse("17:00")), "event", "incomplete", 9));
	}
	
	
	@Test // Test edit for title + start date + start time
	public void testA() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.parse("2016-07-11"), LocalTime.parse("10:00"));
		testTaskObject = new TaskObject("Reservist", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 9);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Hiking trip' to 'Reservist'. Start date edited from '2014-07-11' to '2016-07-11'. Start time edited from '16:00' to '10:00'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}

	@Test // Test edit for start date + start time
	public void testB() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.parse("2016-03-24"), LocalTime.parse("11:00"));
		testTaskObject = new TaskObject("", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 5);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Date edited from '2017-03-24' to '2016-03-24'. Time edited from '19:00' to '11:00'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for start date + start time, but with same old and new start date, so only start time should be edited
	public void testC() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.parse("2016-12-31"), LocalTime.parse("18:00"));
		testTaskObject = new TaskObject("", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 2);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Time edited from '23:59' to '18:00'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	
	@Test // Test edit for start date + start time, but with same old and new start time, so only start date should be edited
	public void testD() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.parse("2016-04-25"), LocalTime.parse("09:00"));
		testTaskObject = new TaskObject("", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 1);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Date edited from '2016-05-25' to '2016-04-25'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for start date
	public void testE() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.parse("2016-04-01"), LocalTime.MAX);
		testTaskObject = new TaskObject("", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 4);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Date edited from '2016-12-01' to '2016-04-01'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for start time
	public void testF() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.MAX, LocalTime.parse("16:00"));
		testTaskObject = new TaskObject("", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 6);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Time edited from '23:59' to '16:00'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for title + start time
	public void testG() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.MAX, LocalTime.parse("10:00"));
		testTaskObject = new TaskObject("Army", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 8);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Overseas paradise' to 'Army'. Start time edited from '22:24' to '10:00'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for title
	public void testH() {
		testTaskObject = new TaskObject("Travel Eastern Europe and Iceland", "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 3);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Travel Eastern Europe' to 'Travel Eastern Europe and Iceland'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}
	
	@Test // Test edit for title + start date
	public void testI() {
		LocalDateTime testStartDateTime = LocalDateTime.of(LocalDate.parse("2016-01-11"), LocalTime.MAX);
		testTaskObject = new TaskObject("AY2016/17 Sem 2", testStartDateTime, "", "", -1);
		testCommandObject = new CommandObject(3, testTaskObject, 7);
		
		Edit testEdit = new Edit(testCommandObject, testList, testList);
		actualOutput = testEdit.run();
		correctOutput.add("Title edited from 'Spring break' to 'AY2016/17 Sem 2'. Start date edited from '2001-01-11' to '2016-01-11'.");
		
		assertEquals(actualOutput, correctOutput);
		correctOutput.clear();
	}

}
