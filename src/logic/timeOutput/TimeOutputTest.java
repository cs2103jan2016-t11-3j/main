package logic.timeOutput;

import common.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TimeOutputTest {
	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();
	private ArrayList<String> actualTimeOutput = new ArrayList<String>();
	private static ArrayList<String> expectedTimeOutput = new ArrayList<String>();
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");

	/**********************************************************************************/
	/**
	 * Tests for setTimeOutputForGui
	 */
	@Test
	public void testA() {
		TaskObject task = new TaskObject("deadline with time and date", "deadline", "incomplete", 1);
		task.setStartDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("by 16:00 on 15/03/16");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	public void testB() {
		TaskObject task = new TaskObject("event with different dates", "event", "incomplete", 2);
		task.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		task.setStartDateTime(LocalDateTime.of(2016, 3, 12, 15, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 12/03/16, from 15:00 to 16:00 on 15/03/16");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	public void testC() {
		TaskObject task = new TaskObject("event in a single day", "event", "incomplete", 3);
		task.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		task.setStartDateTime(LocalDateTime.of(2016, 3, 15, 15, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 15/03/16, from 15:00 to 16:00");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	public void testD() {
		TaskObject task = new TaskObject("deadline without time", "deadline", "incomplete", 4);
		task.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.MAX));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("by 15/03/16");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Start date, no start time, end date, no end time
	public void testE() {
		TaskObject task = new TaskObject("event without time", "event", "incomplete", 5);
		task.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.MAX));
		task.setEndDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 16), LocalTime.MAX));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("from 15/03/16 to 16/03/16");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Start date, start time, end date, no end time
	public void testF() {
		TaskObject task = new TaskObject("event without time", "event", "incomplete", 6);
		task.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.of(13, 00)));
		task.setEndDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 16), LocalTime.MAX));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 15/03/16, from 13:00 to 16/03/16");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Start date, no start time, end date, end time
	public void testG() {
		TaskObject task = new TaskObject("event without time", "event", "incomplete", 7);
		task.setStartDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 15), LocalTime.MAX));
		task.setEndDateTime(LocalDateTime.of(LocalDate.of(2016, 3, 16), LocalTime.of(14, 00)));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("from 15/03/16 to 14:00 on 16/03/16");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	/*******************************************************************************/
	/**
	 * Tests for setEventTimeOutput(LocalDateTime , LocalDateTime ) Possible
	 * paths for this function mostly tested in preceding tests
	 */

	@Test
	// Regular event with start date, time, end date, time
	public void testH() {
		LocalDateTime startDateTime = LocalDateTime.of(LocalDate.of(2016, 11, 16), LocalTime.of(9, 30));
		LocalDateTime endDateTime = LocalDateTime.of(LocalDate.of(2016, 11, 19), LocalTime.of(3, 20));

		String actualOutput = TimeOutput.setEventTimeOutput(startDateTime, endDateTime);
		String expectedOutput = "on 16/11/16, from 09:30 to 03:20 on 19/11/16";

		assertEquals(expectedOutput, actualOutput);
	}

	/*********************************************************************************/
	/**
	 * Tests for setEventTimeOutput for events and deadlines due in the current
	 * week. Tests are written in a way to make it independent of when the tests
	 * are run
	 */
	@Test
	// Start date and time, end date and time on the same day
	public void testI() {
		TaskObject task = new TaskObject("event this week", "event", "incomplete", 9);
		task.setStartDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 00)));
		task.setEndDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 00)));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}

		String dayOfWeek = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
		String dateString = LocalDate.now().format(formatter);

		expectedTimeOutput.add("on " + dayOfWeek + " " + dateString + ", from 12:00 to 14:00");

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Start date and time, end date and time on different days of current week
	public void testJ() {
		TaskObject task = new TaskObject("event from thurs to fri", "event", "incomplete", 10);

		LocalDate thisThursday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
		LocalDate thisFriday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
		LocalDate thisSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		if (thisThursday.isAfter(thisSunday)) {
			thisThursday = thisThursday.minusWeeks(1);
		}

		if (thisFriday.isAfter(thisSunday)) {
			thisFriday = thisFriday.minusWeeks(1);
		}

		task.setStartDateTime(LocalDateTime.of(thisThursday, LocalTime.of(00, 00)));
		task.setEndDateTime(LocalDateTime.of(thisFriday, LocalTime.of(23, 59)));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);

		String startDateString = thisThursday.format(formatter);
		String endDateString = thisFriday.format(formatter);

		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}

		expectedTimeOutput.add("on Thursday " + startDateString + ", from 00:00 to 23:59 on Friday " + endDateString);

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Deadline within this week
	public void testK() {
		TaskObject task = new TaskObject("deadline with time and date", "deadline", "incomplete", 11);

		LocalDate thisTuesday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
		LocalDate thisSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		if (thisTuesday.isAfter(thisSunday)) {
			thisTuesday = thisTuesday.minusWeeks(1);
		}

		task.setStartDateTime(LocalDateTime.of(thisTuesday, LocalTime.of(15, 00)));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);

		String dateString = thisTuesday.format(formatter);

		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}

		expectedTimeOutput.add("by 15:00 on Tuesday " + dateString);

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Deadline within this week, without time
	public void testL() {
		TaskObject task = new TaskObject("deadline with date", "deadline", "incomplete", 12);

		LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
		LocalDate thisSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		if (thisMonday.isAfter(thisSunday)) {
			thisMonday = thisMonday.minusWeeks(1);
		}

		task.setStartDateTime(LocalDateTime.of(thisMonday, LocalTime.MAX));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);

		String dateString = thisMonday.format(formatter);

		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}

		expectedTimeOutput.add("by Monday " + dateString);

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

	@Test
	// Event with start date in current week and end date in next week
	public void testM() {
		TaskObject task = new TaskObject("event from sunday to monday", "event", "incomplete", 13);

		LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		LocalDate thisSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		task.setStartDateTime(LocalDateTime.of(thisSunday, LocalTime.of(00, 00)));
		task.setEndDateTime(LocalDateTime.of(nextMonday, LocalTime.of(23, 59)));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);

		String startDateString = thisSunday.format(formatter);
		String endDateString = nextMonday.format(formatter);

		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}

		expectedTimeOutput.add("on Sunday " + startDateString + ", from 00:00 to 23:59 on next Monday " + endDateString);

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	// Event with start date and end date in next week
	public void testN() {
		TaskObject task = new TaskObject("event from sunday to monday", "event", "incomplete", 13);

		LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		LocalDate nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
		
		if(nextSunday.isBefore(nextMonday)) {
			nextSunday = nextSunday.plusWeeks(1);
		}

		task.setStartDateTime(LocalDateTime.of(nextMonday, LocalTime.of(00, 00)));
		task.setEndDateTime(LocalDateTime.of(nextSunday, LocalTime.of(23, 59)));
		testArray.add(task);
		TimeOutput.setTimeOutputForGui(testArray);

		String startDateString = nextMonday.format(formatter);
		String endDateString = nextSunday.format(formatter);

		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}

		expectedTimeOutput.add("on " + startDateString + ", from 00:00 to 23:59 on " + endDateString);

		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
}
