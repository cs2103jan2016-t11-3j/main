package logic;

import common.*;
import logic.add.Add;
import logic.timeOutput.TimeOutput;

import java.util.ArrayList;
import java.time.LocalDateTime;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Recurring {

	// EVENTS
	/* For logic ************************************************************/
	public static void updateRecurringEvents(ArrayList<TaskObject> taskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getIsRecurring()) {
				if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
					updateEvent(taskList.get(i));
				}
			}
		}
	}

	public static void updateEvent(TaskObject task) {
		LocalDateTime eventEndTime = task.getEndDateTime();

		while (LocalDateTime.now().isAfter(eventEndTime) && task.getTaskDateTimes().size() > 1) {
			renewEvent(task);
			eventEndTime = task.getEndDateTime();
		}

		if (task.getTaskDateTimes().size() <= 1) {
			if (LocalDateTime.now().isAfter(eventEndTime)) {
				markAsDone(task);
			}
		}
	}

	public static void renewEvent(TaskObject task) {
		LocalDateTime newStartDateTime;
		LocalDateTime newEndDateTime;
		LocalDateTimePair nextEvent;

		assert (task.getTaskDateTimes().size() > 1);

		task.removeFromTaskDateTimes(0);

		nextEvent = task.getTaskDateTimes().get(0);
		newStartDateTime = nextEvent.getStartDateTime();
		newEndDateTime = nextEvent.getEndDateTime();

		task.setStartDateTime(newStartDateTime);
		task.setEndDateTime(newEndDateTime);
	}

	private static void markAsDone(TaskObject task) {
		task.setStatus("completed");
	}

	/*
	 * For add and edit
	 ***************************************************************/

	public static void setAllRecurringEventTimes(TaskObject task) {
		assert task.getCategory().equals(CATEGORY_EVENT);

		Interval interval = task.getInterval();
		LocalDateTimePair eventDateTime = task.getTaskDateTimes().get(0);

		// in case there is an existing list and the interval is changed
		task.removeAllDateTimes();

		if (!interval.getUntil().equals(LocalDateTime.MAX)) {
			while (!eventDateTime.getStartDateTime().isAfter(interval.getUntil())) {
				// not after == before and equal
				task.addToTaskDateTimes(eventDateTime);
				eventDateTime = setNextEventTime(interval, eventDateTime);
			}
		} else {
			if (interval.getCount() != -1) {
				for (int i = 0; i < interval.getCount(); i++) {
					task.addToTaskDateTimes(eventDateTime);
					eventDateTime = setNextEventTime(interval, eventDateTime);
				}
			}
		}
	}

	public static LocalDateTimePair setNextEventTime(Interval interval, LocalDateTimePair eventDateTime) {
		LocalDateTime startDateTime = eventDateTime.getStartDateTime();
		LocalDateTime endDateTime = eventDateTime.getEndDateTime();
		LocalDateTimePair nextEvent = new LocalDateTimePair();

		if (interval.getByDay().equals("")) {
			nextEvent = obtainNextTime(interval, startDateTime, endDateTime);
		} else {
			// implementation with byDay
		}

		return nextEvent;
	}

	// Common method shared by both recurring events and deadlines
	public static LocalDateTimePair obtainNextTime(Interval interval, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {
		String frequency = interval.getFrequency();
		int timeInterval = interval.getTimeInterval();

		switch (frequency) {
		case FREQ_HOURLY:
			startDateTime = startDateTime.plusHours(timeInterval);
			if (!endDateTime.isEqual(LocalDateTime.MAX)) {
				// if it is a deadline, endDateTime remains unadjusted
				endDateTime = endDateTime.plusHours(timeInterval);
			}
			break;

		case FREQ_DAILY:
			startDateTime = startDateTime.plusDays(timeInterval);
			if (!endDateTime.isEqual(LocalDateTime.MAX)) {
				endDateTime = endDateTime.plusDays(timeInterval);
			}
			break;

		case FREQ_WEEKLY:
			startDateTime = startDateTime.plusWeeks(timeInterval);
			if (!endDateTime.isEqual(LocalDateTime.MAX)) {
				endDateTime = endDateTime.plusWeeks(timeInterval);
			}
			break;

		case FREQ_MONTHLY:
			startDateTime = startDateTime.plusMonths(timeInterval);
			if (!endDateTime.isEqual(LocalDateTime.MAX)) {
				endDateTime = endDateTime.plusMonths(timeInterval);
			}
			break;

		case FREQ_YEARLY:
			startDateTime = startDateTime.plusYears(timeInterval);
			if (!endDateTime.isEqual(LocalDateTime.MAX)) {
				endDateTime = endDateTime.plusYears(timeInterval);
			}
			break;
		}

		return new LocalDateTimePair(startDateTime, endDateTime);
	}

	// DEADLINES
	/*******************************************************************************/
	/**
	 * For adding recurring deadlines, called by add and edit
	 * 
	 * @param task
	 */

	public static void setAllRecurringDeadlineTimes(TaskObject task) {
		assert task.getCategory().equals(CATEGORY_DEADLINE);

		Interval interval = task.getInterval();
		LocalDateTimePair deadlineDateTime = task.getTaskDateTimes().get(0);

		task.removeAllDateTimes();

		if (!interval.getUntil().isEqual(LocalDateTime.MAX)) {
			while (!deadlineDateTime.getStartDateTime().isAfter(interval.getUntil())) {
				// not after == before and equal
				task.addToTaskDateTimes(deadlineDateTime);
				deadlineDateTime = setNextDeadlineTime(interval, deadlineDateTime);
			}
		} else {
			if (interval.getCount() != -1) {
				for (int i = 0; i < interval.getCount(); i++) {
					task.addToTaskDateTimes(deadlineDateTime);
					deadlineDateTime = setNextDeadlineTime(interval, deadlineDateTime);
				}
			}
		}
	}

	public static LocalDateTimePair setNextDeadlineTime(Interval interval, LocalDateTimePair deadlineDateTime) {
		LocalDateTimePair newPair = new LocalDateTimePair();

		if (interval.getByDay().equals("")) {
			newPair = obtainNextTime(interval, deadlineDateTime.getStartDateTime(), LocalDateTime.MAX);
		} else {
			// implementation for byDay
		}

		return newPair;
	}

	// Used in logic upon construction of logic object
	public static void updateRecurringDeadlines(ArrayList<TaskObject> taskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getIsRecurring()) {
				if (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) {
					updateDeadline(taskList.get(i), taskList, "overdue");
				}
			}
		}
	}

	/**
	 * Method used when starting the program, and when a recurring task is marked
	 * <br>
	 * If the current time has passed the said recurrent time, this function
	 * updates the deadline by creating a new task with this recurrent time and
	 * adds it to the task list, with the status set as overdue/complete. This is due 
	 * to the premise that marking a recurring deadline as complete automatically
	 * creates a new task with that deadline and marking it as complete
	 * 
	 * @param task
	 */
	public static void updateDeadline(TaskObject task, ArrayList<TaskObject> taskList, String status) {
		if (status.equals("overdue")) {
			updateDeadlineToOverdue(task, taskList, status);
		} else {
			if (status.equals("completed")) {
				updateDeadlineToCompleted(task, taskList, status);
			}
		}
	}
	
	private static void updateDeadlineToOverdue(TaskObject task, ArrayList<TaskObject> taskList, String status) {
		LocalDateTime deadlineDateTime = task.getTaskDateTimes().get(0).getStartDateTime();
		String taskName = task.getTitle();

		// Continually splits tasks until recurring task is no longer overdue
		// number of recurring times must also be more than 1
		while (LocalDateTime.now().isAfter(deadlineDateTime) && task.getTaskDateTimes().size() > 1) {
			splitTaskFromRecurringDeadline(deadlineDateTime, taskName, taskList, status);
			renewDeadline(task);
			deadlineDateTime = task.getStartDateTime();
		}

		// Special case for only 1 timing left
		if (task.getTaskDateTimes().size() == 1) {
			if (LocalDateTime.now().isAfter(deadlineDateTime)) {
				task.setStatus(status);
			}
		}
	}
	
	private static void updateDeadlineToCompleted(TaskObject task, ArrayList<TaskObject> taskList, String status) {
		LocalDateTime deadlineDateTime = task.getTaskDateTimes().get(0).getStartDateTime();
		String taskName = task.getTitle();
		
		if (task.getTaskDateTimes().size() == 1) {
			task.setStatus(status);
		} else {
			splitTaskFromRecurringDeadline(deadlineDateTime, taskName, taskList, status);
			renewDeadline(task);
		}
	}

	private static void renewDeadline(TaskObject task) {
		LocalDateTime newStartDateTime;
		LocalDateTimePair nextDeadline;

		task.removeFromTaskDateTimes(0);
		nextDeadline = task.getTaskDateTimes().get(0);
		newStartDateTime = nextDeadline.getStartDateTime();
		task.setStartDateTime(newStartDateTime);
	}

	private static void splitTaskFromRecurringDeadline(LocalDateTime deadline, String title,
			ArrayList<TaskObject> taskList, String status) {
		int taskId = generateTaskId(taskList);
		TaskObject splitDeadline = createOverdueDeadlineTaskObject(deadline, title, taskId, status);
		Add add = new Add(splitDeadline, -1, taskList);
		add.run();
		// adds the split deadline into the taskList
	}

	// returns a negative number as taskID to prevent clashing with normal IDs
	private static int generateTaskId(ArrayList<TaskObject> taskList) {
		int id = -1;
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() < 0) {
				id--; // so that no 2 split tasks have the same ID
			}
		}
		return id;
	}

	private static TaskObject createOverdueDeadlineTaskObject(LocalDateTime deadline, String title, int taskId, String status) {
		TaskObject splitDeadline = new TaskObject(title, deadline, CATEGORY_EVENT, status, taskId);
		splitDeadline.setIsRecurring(false);
		splitDeadline.addToTaskDateTimes(new LocalDateTimePair(deadline));
		TimeOutput.setDeadlineTimeOutput(splitDeadline);
		return splitDeadline;
	}
}