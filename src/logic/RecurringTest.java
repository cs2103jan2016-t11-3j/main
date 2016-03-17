package logic;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

import common.*;

import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecurringTest {

	public ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();

	@Test
	// Interval containing counts > 0
	public void testA() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 1);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, 5, "");
		task.setInterval(interval);
		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 24, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 24, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());

		assertEquals(4, testArray.get(0).getInterval().getCount());
	}

	@Test
	// Interval containing until but not yet end of recurrence
	public void testB() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 1);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 04, 25, 14, 00), "");
		task.setInterval(interval);
		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 24, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 24, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());
	}

	@Test
	// Interval containing counts == 0
	public void testC() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 1);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, 0, "");
		task.setInterval(interval);
		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 17, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 17, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());

		assertEquals(0, testArray.get(0).getInterval().getCount());

		assertEquals("done", testArray.get(0).getStatus());
	}

	@Test
	// Interval containing until which is reached
	public void testD() throws Exception {
		TaskObject task = new TaskObject("IE2130 Lecture", LocalDateTime.of(2016, 03, 17, 14, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 1);
		task.setIsRecurring(true);
		Interval interval = new Interval("WEEKLY", 1, LocalDateTime.of(2016, 03, 16, 14, 00), "");
		task.setInterval(interval);
		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 17, 14, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 17, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());

		assertEquals("done", testArray.get(0).getStatus());
	}

	@Test
	// Recurring task with different interval
	public void testE() throws Exception {
		TaskObject task = new TaskObject("Go to gym", LocalDateTime.of(2016, 03, 17, 15, 00),
				LocalDateTime.of(2016, 03, 17, 16, 00), "event", "incomplete", 1);
		task.setIsRecurring(true);
		Interval interval = new Interval("DAILY", 3, 10, "");
		task.setInterval(interval);
		testArray.add(task);
		Recurring.updateRecurringEvents(testArray);

		LocalDateTime nextStartDateTime = LocalDateTime.of(2016, 03, 20, 15, 00);
		LocalDateTime nextEndDateTime = LocalDateTime.of(2016, 03, 20, 16, 00);

		assertEquals(nextStartDateTime, testArray.get(0).getStartDateTime());
		assertEquals(nextEndDateTime, testArray.get(0).getEndDateTime());

		assertEquals(9, testArray.get(0).getInterval().getCount());
	}
}
