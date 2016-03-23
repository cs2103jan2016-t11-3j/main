package logic.timeOutput;

import common.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TimeOutputTest {
	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();
	private ArrayList<String> actualTimeOutput = new ArrayList<String>();
	private static ArrayList<String> expectedTimeOutput = new ArrayList<String>();

	/**********************************************************************************/
	/**
	 * Tests for setTimeOutputForGui
	 */
	@Test
	public void testA() {
		TaskObject taskOne = new TaskObject("deadline with time and date", "deadline", "incomplete", 1);
		taskOne.setStartDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(taskOne);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("by 16:00 on 2016-03-15");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	public void testB() {
		TaskObject taskTwo = new TaskObject("event with different dates", "event", "incomplete", 2);
		taskTwo.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		taskTwo.setStartDateTime(LocalDateTime.of(2016, 3, 12, 15, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(taskTwo);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 2016-03-12, from 15:00 to 16:00 on 2016-03-15");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	public void testC() {
		TaskObject taskThree = new TaskObject("event in a single day", "event", "incomplete", 3);
		taskThree.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		taskThree.setStartDateTime(LocalDateTime.of(2016, 3, 15, 15, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(taskThree);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 2016-03-15, from 15:00 to 16:00");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	public void testD() {
		TaskObject taskFour = new TaskObject("deadline without time", "deadline", "incomplete", 4);
		taskFour.setStartDateTime(LocalDateTime.of(LocalDate.of(2016,  3,  15), LocalTime.MAX));
		testArray.add(taskFour);
		TimeOutput.setTimeOutputForGui(testArray);
		for(int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("by 2016-03-15");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	// Start date, no start time, end date, no end time
	public void testE() {
		TaskObject taskFive = new TaskObject("event without time", "event", "incomplete", 5);
		taskFive.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.MAX));
		taskFive.setEndDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 16), LocalTime.MAX));
		testArray.add(taskFive);
		TimeOutput.setTimeOutputForGui(testArray);
		for(int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("from 2016-03-15 to 2016-03-16");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	// Start date, start time, end date, no end time
	public void testF() {
		TaskObject taskFive = new TaskObject("event without time", "event", "incomplete", 5);
		taskFive.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.of(13, 00)));
		taskFive.setEndDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 16), LocalTime.MAX));
		testArray.add(taskFive);
		TimeOutput.setTimeOutputForGui(testArray);
		for(int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 2016-03-15, from 13:00 to 2016-03-16");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	// Start date, no start time, end date, end time
	public void testG() {
		TaskObject taskFive = new TaskObject("event without time", "event", "incomplete", 5);
		taskFive.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.MAX));
		taskFive.setEndDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 16), LocalTime.of(14, 00)));
		testArray.add(taskFive);
		TimeOutput.setTimeOutputForGui(testArray);
		for(int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("from 2016-03-15 to 14:00 on 2016-03-16");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	/*******************************************************************************/
	/**
	 * Tests for setEventTimeOutput(LocalDateTime , LocalDateTime )
	 * Possible paths for this function mostly tested in preceding tests
	 */
	
	@Test
	// Regular event with start date, time, end date, time
	public void testH() {
		LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2016, 11, 16), LocalTime.of(9, 30));
		LocalDateTime endDateTime= LocalDateTime.of(LocalDate.of(2016, 11, 19), LocalTime.of(3, 20));
		
		String actualOutput = TimeOutput.setEventTimeOutput(startDateTime, endDateTime);
		String expectedOutput = "on 2016-11-16, from 09:30 to 03:20 on 2016-11-19";
		
		assertEquals(expectedOutput, actualOutput);
	}
}
