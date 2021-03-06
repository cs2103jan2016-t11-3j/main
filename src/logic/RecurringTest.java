//@@author A0124052X

package logic;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import common.*;
import logic.timeoutput.TimeOutput;

import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecurringTest {

	public ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();

	// EXCEPTIONS THROWN FOR INTERVAL CLASS
	@Test
	// Interval containing counts > 0
	public void testA() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 1);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, 5, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 04, 14, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 04, 14, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());
		
		for (int i = 0; i < testArray.size(); i++) {
			if (testArray.get(i).getTaskId() < 0) {
				System.out.println(testArray.get(i).getTimeOutputString());
				System.out.println(testArray.get(i).getStatus());
				System.out.println(testArray.get(i).getTaskId());
			}
		}
	}

	@Test
	// Interval containing until but not yet end of recurrence
	public void testB() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 18, 16, 00), "event", "incomplete", 2);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 04, 14, 14, 00), "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(),
					task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}

		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 04, 14, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 04, 15, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());
	}

	@Test
	// Interval containing counts == 0
	public void testC() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 3);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, 0, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 17, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 17, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());

		assertEquals("overdue", testArray.get(0).getStatus());
	}

	@Test
	// Interval containing until which is reached
	public void testD() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 4);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 03, 16, 14, 00), "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 17, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 17, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());

		assertEquals("overdue", testArray.get(0).getStatus());
	}

	@Test
	// Recurring task with different interval
	public void testE() throws Exception {
		TaskObject task = new TaskObject("Go to gym", LocalDateTime.of(2016, 03, 17, 15, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 5);
		task.setIsRecurring(true);
		Interval interval = new Interval("DAILY", 14, 10, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 04, 14, 15, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 04, 14, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());
	}

	/************************************************************************************/
	@Test
	// Recurring deadline which is not overdue, with counts
	public void testF() throws Exception {
		TaskObject task = new TaskObject("buy eggs", LocalDateTime.of(2016, 10, 13, 15, 00), "deadline", "incomplete",
				6);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, 5, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);

		testArray.add(task);
		Recurring.updateRecurringDeadlines(testArray);

		ArrayList<LocalDateTimePair> expectedDeadlineList = new ArrayList<LocalDateTimePair>();
		for (int i = 0; i < interval.getCount(); i++) {
			LocalDateTime dateTime = task.getStartDateTime().plusWeeks(i);
			expectedDeadlineList.add(new LocalDateTimePair(dateTime));
		}

		// Compares across all timings
		for (int i = 0; i < interval.getCount(); i++) {
			LocalDateTime expectedDateTime = expectedDeadlineList.get(i).getStartDateTime();
			LocalDateTime actualDateTime = task.getTaskDateTimes().get(i).getStartDateTime();
			assertEquals(expectedDateTime, actualDateTime);
		}
	}

	@Test
	// Recurring deadline which is not overdue, with until
	public void testG() throws Exception {
		TaskObject task = new TaskObject("buy tissue", LocalDateTime.of(2016, 7, 13, 15, 00), "deadline", "incomplete",
				7);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 9, 14, 15, 00), "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);

		testArray.add(task);
		Recurring.updateRecurringDeadlines(testArray);

		ArrayList<LocalDateTimePair> expectedDeadlineList = new ArrayList<LocalDateTimePair>();
		LocalDateTime dateTime = task.getStartDateTime();
		while (dateTime.isBefore(interval.getUntil())) {
			expectedDeadlineList.add(new LocalDateTimePair(dateTime));
			dateTime = dateTime.plusWeeks(1);
		}

		// Compares across all timings
		for (int i = 0; i < expectedDeadlineList.size(); i++) {
			LocalDateTime expectedDateTime = expectedDeadlineList.get(i).getStartDateTime();
			LocalDateTime actualDateTime = task.getTaskDateTimes().get(i).getStartDateTime();
			assertEquals(expectedDateTime, actualDateTime);
		}
	}

	@Test
	// Recurring deadline which is overdue, with until
	public void testH() throws Exception {
		TaskObject task = new TaskObject("buy lunch", LocalDateTime.of(2016, 3, 14, 15, 00), "deadline", "incomplete",
				8);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 5, 16, 14, 59), "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);

		testArray.add(task);
		int currentNumberOfTasks = testArray.size();
		int currentNumberOfRecurrences = task.getTaskDateTimes().size();
		Recurring.updateRecurringDeadlines(testArray);
		int newNumberOfTasks = testArray.size();
		int newNumberOfRecurrences = task.getTaskDateTimes().size();

		// Sum of tasks and timings is always the same
		assertTrue(currentNumberOfTasks + currentNumberOfRecurrences == newNumberOfTasks + newNumberOfRecurrences);
		// Checks that the updated deadline is after the current time
		assertTrue(task.getStartDateTime().isAfter(LocalDateTime.now()));

		for (int i = 0; i < testArray.size(); i++) {
			if (testArray.get(i).getTaskId() < 0) {
				// Split recurrences have a negative task ID
				System.out.println(testArray.get(i).getStartDateTime().toString());
				System.out.println(testArray.get(i).getStatus());
			}
		}

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			System.out.println(task.getTaskDateTimes().get(i).getStartDateTime().toString());
		}
	}

	@Test
	// Recurring deadline which is ALL overdue, with counts
	public void testI() throws Exception {
		TaskObject task = new TaskObject("buy lunch", LocalDateTime.of(2016, 3, 16, 15, 00), "deadline", "incomplete",
				9);
		task.setIsRecurring(true);
		Interval interval = new Interval("DAILY", 1, 5, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);

		testArray.add(task);
		int currentNumberOfTasks = testArray.size();
		int currentNumberOfRecurrences = task.getTaskDateTimes().size();
		Recurring.updateRecurringDeadlines(testArray);
		int newNumberOfTasks = testArray.size();
		int newNumberOfRecurrences = task.getTaskDateTimes().size();

		// Sum of tasks and timings is always the same
		assertTrue(currentNumberOfTasks + currentNumberOfRecurrences == newNumberOfTasks + newNumberOfRecurrences);
		// Checks that splitting only occurs 4 times
		assertTrue(newNumberOfTasks - currentNumberOfTasks == 4);

		for (int i = 0; i < testArray.size(); i++) {
			if (testArray.get(i).getTaskId() < 0) {
				// Split recurrences have a negative task ID
				System.out.println(testArray.get(i).getStartDateTime().toString());
				System.out.println(testArray.get(i).getStatus());
			}
		}

	}

	@Test
	// Until == startDateTime of another recurrence
	public void testJ() throws Exception {
		TaskObject task = new TaskObject("cs lecture", LocalDateTime.of(2016, 04, 01, 16, 00),
				LocalDateTime.of(2016, 04, 01, 18, 00), "event", "incomplete", 10);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 04, 22, 16, 00), "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(),
					task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}

		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 04, 15, 16, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 04, 15, 18, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());
	}

	@Test
	// Addition of infinite event
	public void testK() throws Exception {
		TaskObject task = new TaskObject("cs lecture", LocalDateTime.of(2016, 04, 01, 16, 00),
				LocalDateTime.of(2016, 04, 01, 18, 00), "event", "incomplete", 11);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.MAX, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		assertTrue(task.getTaskDateTimes().size() == 10);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(),
					task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}

	@Test
	// Addition of infinite deadline
	public void testL() throws Exception {
		TaskObject task = new TaskObject("cs lecture", LocalDateTime.of(2016, 04, 01, 16, 00), "deadline", "incomplete",
				12);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.MAX, "");
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);

		testArray.add(task);
		Recurring.updateRecurringDeadlines(testArray);

		assertTrue(task.getTaskDateTimes().size() == 10);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setDeadlineTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime());
			System.out.println(line);
		}
	}

	/*****************************************************************************/
	/**
	 * Tests for the implementation of byDay
	 */

	@Test
	// event, byDay = Monday, Thursday, Sunday. Interval = 1 week
	public void testM() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 05, 15, 14, 00),
				LocalDateTime.of(2016, 05, 15, 16, 00), "event", "incomplete", 13);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 06, 19, 14, 00), "");
		interval.setByDay(1);
		interval.setByDay(4);
		interval.setByDay(7);
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);

		assertTrue(task.getTaskDateTimes().size() == 16);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(),
					task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}

	@Test
	// event, byDay = Tuesday, Saturday. Interval = 3 weeks
	public void testN() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 05, 17, 14, 00),
				LocalDateTime.of(2016, 05, 17, 16, 00), "event", "incomplete", 14);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 3, LocalDateTime.of(2016, 07, 19, 14, 00), "");
		interval.setByDay(2);
		interval.setByDay(6);
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);
		
		assertTrue(task.getTaskDateTimes().size() == 7);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(),
					task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}
	
	@Test
	// event, byDay = Wednesday, Saturday. Interval = 2 weeks
	public void testO() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 05, 19, 14, 00),
				LocalDateTime.of(2016, 05, 19, 16, 00), "event", "incomplete", 15);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 2, 10, "");
		interval.setByDay(3);
		interval.setByDay(6);
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);
		
		// 10 * 2 == 20
		assertTrue (task.getTaskDateTimes().size() == 20);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(),
					task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}
	
	@Test
	// deadline, byDay = Monday, Friday. Interval = 2 weeks
	public void testP() throws Exception {
		TaskObject task = new TaskObject("deadline 40", LocalDateTime.of(2016, 8, 19, 14, 00),
				"deadline", "incomplete", 16);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 2, 15, "");
		interval.setByDay(1);
		interval.setByDay(5);
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);
		
		// 15 * 2 == 30
		assertTrue (task.getTaskDateTimes().size() == 30);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setDeadlineTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime());
			System.out.println(line);
		}
	}
	
	@Test
	// deadline, byDay = Thursday, Friday, infinite recurrence 2 weeks
	public void testQ() throws Exception{
		TaskObject task = new TaskObject("deadline infinite", LocalDateTime.of(2016, 8, 21, 14, 00),
				"deadline", "incomplete", 17);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 2, -1, "");
		interval.setByDay(4);
		interval.setByDay(5);
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringDeadlineTimes(task);
		
		// 10 * 2 = 20
		assertTrue (task.getTaskDateTimes().size() == 20);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setDeadlineTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime());
			System.out.println(line);
		}
	}
	
	@Test
	// event, byDay = Wednesday, Friday, infinite recurrence 1 week
	public void testR() throws Exception{
		TaskObject task = new TaskObject("play soccer", LocalDateTime.of(2016, 05, 19, 14, 00),
				LocalDateTime.of(2016, 05, 19, 16, 00), "event", "incomplete", 18);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, -1, "");
		interval.setByDay(3);
		interval.setByDay(5);
		task.setInterval(interval);

		LocalDateTimePair pair = new LocalDateTimePair(task.getStartDateTime(), task.getEndDateTime());
		task.addToTaskDateTimes(pair);
		Recurring.setAllRecurringEventTimes(task);
		
		// 10 * 2 = 20
		assertTrue (task.getTaskDateTimes().size() == 20);

		for (int i = 0; i < task.getTaskDateTimes().size(); i++) {
			String line = TimeOutput.setEventTimeOutput(task.getTaskDateTimes().get(i).getStartDateTime(), task.getTaskDateTimes().get(i).getEndDateTime());
			System.out.println(line);
		}
	}
}
