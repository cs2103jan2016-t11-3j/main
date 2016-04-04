package logic;

import common.*;
import logic.add.Add;
import logic.timeOutput.TimeOutput;
import logic.exceptions.RecurrenceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Recurring {

	private static Logger logger = AtfLogger.getLogger();
	
	/**
	 * Method called by logic when AdultTaskFinder is launched. Searches for all
	 * recurring events and passes it to updateEvent to determine if the
	 * recurring event has to be updated, and updates the event if necessary
	 * 
	 * @param taskList
	 *            List of tasks stored by AdultTaskFinder
	 */
	public static void updateRecurringEvents(ArrayList<TaskObject> taskList) throws RecurrenceException {
		logger.log(Level.INFO, "about to update all recurring events");
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getIsRecurring()) {
				if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
					updateEvent(taskList.get(i), taskList, STATUS_OVERDUE);
					logger.log(Level.INFO, "about to update recurring event:" + taskList.get(i).getTitle());
				}
			}
		}
	}

	/**
	 * Method called by updateRecurringEvents and a Done object to update an event
	 * to the desired status of either
	 */
	public static void updateEvent(TaskObject task, ArrayList<TaskObject> taskList, String status)
			throws RecurrenceException {
		if (status.equals(STATUS_OVERDUE)) {
			updateEventToOverdue(task, taskList, status);
		} else {
			if (status.equals(STATUS_COMPLETED)) {
				updateEventToCompleted(task, taskList, status);
			} else {
				logger.log(Level.WARNING, "unable to update status of event");
				RecurrenceException e = new RecurrenceException(MESSAGE_RECURRENCE_EXCEPTION_INVALID_STATUS);
				throw e;
			}
		}

		logger.log(Level.INFO, "updated status of event " + task.getTitle() + " to " + status);
	}

	private static void updateEventToOverdue(TaskObject task, ArrayList<TaskObject> taskList, String status) {
		LocalDateTime eventEndTime = task.getEndDateTime();
		LocalDateTime eventStartTime = task.getStartDateTime();
		String taskName = task.getTitle();

		assert (!eventEndTime.equals(LocalDateTime.MAX));
		while (LocalDateTime.now().isAfter(eventEndTime) && task.getTaskDateTimes().size() > 1) {
			splitTaskFromRecurringEvent(taskName, eventStartTime, eventEndTime, taskList, status);
			renewEvent(task);
			System.out.println(eventStartTime.toString());
			System.out.println(eventEndTime.toString());
			eventEndTime = task.getEndDateTime();
			eventStartTime = task.getStartDateTime();
			logger.log(Level.INFO,
					"Modified recurring event to next set of timings, and split current overdue event");
		}

		if (task.getTaskDateTimes().size() <= 1) {
			if (LocalDateTime.now().isAfter(eventEndTime)) {
				task.setStatus(status);
				logger.log(Level.INFO, "Recurring event has come to an end");
			}
		}
	}

	private static void updateEventToCompleted(TaskObject task, ArrayList<TaskObject> taskList,
			String status) {
		LocalDateTimePair eventTimePair = task.getTaskDateTimes().get(0);
		String taskName = task.getTitle();

		if (task.getTaskDateTimes().size() == 1) {
			task.setStatus(status);
			logger.log(Level.INFO, "Recurring event has come to an end");
		} else {
			splitTaskFromRecurringEvent(taskName, eventTimePair.getStartDateTime(),
					eventTimePair.getEndDateTime(), taskList, status);
			renewEvent(task);
			logger.log(Level.INFO,
					"Modified recurring event to next set of timings, and split current completed event");
		}
	}

	private static void splitTaskFromRecurringEvent(String taskName, LocalDateTime startDateTime,
			LocalDateTime endDateTime, ArrayList<TaskObject> taskList, String status) {
		int taskId = generateTaskId(taskList);

		assert (taskId < 0);
		assert (startDateTime.isBefore(endDateTime));

		TaskObject splitEvent = createSplitEventTaskObject(taskName, startDateTime, endDateTime, status,
				taskId);
		Add add = new Add(splitEvent, -1, taskList);
		add.run();

		logger.log(Level.INFO, "Added the split recurring event to task list");
	}

	private static TaskObject createSplitEventTaskObject(String taskName, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String status, int taskId) {
		TaskObject splitEvent = new TaskObject(taskName, startDateTime, endDateTime, CATEGORY_EVENT, status,
				taskId);
		splitEvent.setIsRecurring(false);
		splitEvent.addToTaskDateTimes(new LocalDateTimePair(startDateTime, endDateTime));
		TimeOutput.setEventTimeOutput(splitEvent);

		logger.log(Level.INFO, "Created a split event task object to be added");
		return splitEvent;
	}

	public static void renewEvent(TaskObject task) throws IndexOutOfBoundsException {
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
		logger.log(Level.INFO, "Set to next recurring date time: " + newStartDateTime.toString()
				+ newEndDateTime.toString());

		boolean isInfiniteRecurrence = checkIfInfiniteRecurrence(task.getInterval());
		if (isInfiniteRecurrence) {
			updateInfiniteRecurrence(task);
		}
	}

	public static void setAllRecurringEventTimes(TaskObject task) {
		assert task.getCategory().equals(CATEGORY_EVENT);
		logger.log(Level.INFO, "About to set all recurring times for event: " + task.getTitle());

		Interval interval = task.getInterval();
		LocalDateTimePair eventDateTime = task.getTaskDateTimes().get(0);

		// in case there is an existing list and the interval is changed
		task.removeAllDateTimes();

		if (!interval.getUntil().equals(LocalDateTime.MAX)) {
			setTimingsBasedOnUntil(task, eventDateTime, interval);
		} else {
			if (interval.getCount() != -1) {
				setTimingsBasedOnCounts(task, eventDateTime, interval, interval.getCount());
			} else {
				setTimingsBasedOnCounts(task, eventDateTime, interval, 10);
			}
		}

		// In case of funny recurrences where no timings get added
		if (task.getTaskDateTimes().isEmpty()) {
			task.addToTaskDateTimes(eventDateTime);
		}
	}

	/*******************************************************************************/
	/**
	 * Methods used for recurring deadlines
	 * 
	 * @param task
	 */

	public static void setAllRecurringDeadlineTimes(TaskObject task) {
		assert task.getCategory().equals(CATEGORY_DEADLINE);

		Interval interval = task.getInterval();
		LocalDateTimePair deadlineDateTime = task.getTaskDateTimes().get(0);

		task.removeAllDateTimes();

		if (!interval.getUntil().isEqual(LocalDateTime.MAX)) {
			setTimingsBasedOnUntil(task, deadlineDateTime, interval);
		} else {
			if (interval.getCount() != -1) {
				setTimingsBasedOnCounts(task, deadlineDateTime, interval, interval.getCount());
			} else {
				setTimingsBasedOnCounts(task, deadlineDateTime, interval, 10);
			}
		}

		// In case of funny recurrences where no timings get added
		if (task.getTaskDateTimes().isEmpty()) {
			task.addToTaskDateTimes(deadlineDateTime);
		}
	}

	// Used in logic upon construction of logic object
	public static void updateRecurringDeadlines(ArrayList<TaskObject> taskList) throws RecurrenceException {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getIsRecurring()) {
				if (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) {
					updateDeadline(taskList.get(i), taskList, STATUS_OVERDUE);
					logger.log(Level.INFO,
							"about to update recurring deadline:" + taskList.get(i).getTitle());
				}
			}
		}
	}

	/**
	 * Method used when starting the program, and when a recurring task is
	 * marked <br>
	 * If the current time has passed the said recurrent time, this function
	 * updates the deadline by creating a new task with this recurrent time and
	 * adds it to the task list, with the status set as overdue/complete. This
	 * is due to the premise that marking a recurring deadline as complete
	 * automatically creates a new task with that deadline and marking it as
	 * complete
	 * 
	 * @param task
	 */
	public static void updateDeadline(TaskObject task, ArrayList<TaskObject> taskList, String status)
			throws RecurrenceException {
		if (status.equals(STATUS_OVERDUE)) {
			updateDeadlineToOverdue(task, taskList, status);
		} else {
			if (status.equals(STATUS_COMPLETED)) {
				updateDeadlineToCompleted(task, taskList, status);
			} else {
				logger.log(Level.WARNING, "unable to update status of deadline");
				RecurrenceException e = new RecurrenceException(MESSAGE_RECURRENCE_EXCEPTION_INVALID_STATUS);
				throw e;
			}
		}
		logger.log(Level.INFO, "updated status of deadline " + task.getTitle() + " to " + status);
	}

	private static void updateDeadlineToOverdue(TaskObject task, ArrayList<TaskObject> taskList,
			String status) {
		LocalDateTime deadlineDateTime = task.getTaskDateTimes().get(0).getStartDateTime();
		String taskName = task.getTitle();

		// Continually splits tasks until recurring task is no longer overdue
		// number of recurring times must also be more than 1
		while (LocalDateTime.now().isAfter(deadlineDateTime) && task.getTaskDateTimes().size() > 1) {
			splitTaskFromRecurringDeadline(deadlineDateTime, taskName, taskList, status);
			renewDeadline(task);
			deadlineDateTime = task.getStartDateTime();
			logger.log(Level.INFO, "modified recurring deadline to the next deadline");
		}

		// Special case for only 1 timing left
		if (task.getTaskDateTimes().size() == 1) {
			if (LocalDateTime.now().isAfter(deadlineDateTime)) {
				task.setStatus(status);
				logger.log(Level.INFO, "recurring deadline has come to an end");
			}
		}
	}

	private static void updateDeadlineToCompleted(TaskObject task, ArrayList<TaskObject> taskList,
			String status) {
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

		logger.log(Level.INFO, "set the next deadline for recurring task");

		boolean isInfiniteRecurrence = checkIfInfiniteRecurrence(task.getInterval());
		if (isInfiniteRecurrence) {
			updateInfiniteRecurrence(task);
		}
	}

	private static void splitTaskFromRecurringDeadline(LocalDateTime deadline, String title,
			ArrayList<TaskObject> taskList, String status) {
		int taskId = generateTaskId(taskList);
		TaskObject splitDeadline = createSplitDeadlineTaskObject(deadline, title, taskId, status);
		Add add = new Add(splitDeadline, -1, taskList);
		add.run();
		// adds the split deadline into the taskList
	}

	// returns a negative number as taskID to prevent clashing with normal IDs
	private static int generateTaskId(ArrayList<TaskObject> taskList) {
		int id = -2; // -1 denotes error so set to -2 to prevent any unforeseen
						// errors
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() <= id) {
				id = taskList.get(i).getTaskId() - 1; // so that no 2 split
														// tasks have the same
														// ID
			}
		}
		return id;
	}

	private static TaskObject createSplitDeadlineTaskObject(LocalDateTime deadline, String title, int taskId,
			String status) {
		TaskObject splitDeadline = new TaskObject(title, deadline, CATEGORY_DEADLINE, status, taskId);
		splitDeadline.setIsRecurring(false);
		splitDeadline.addToTaskDateTimes(new LocalDateTimePair(deadline));
		TimeOutput.setDeadlineTimeOutput(splitDeadline);
		return splitDeadline;
	}

	/************************************************************************************/
	/**
	 * Common methods shared by both deadline and events
	 * 
	 * @param task
	 */
	private static void updateInfiniteRecurrence(TaskObject task) {
		int index = task.getTaskDateTimes().size() - 1;
		LocalDateTimePair lastTimingInList = task.getTaskDateTimes().get(index);

		lastTimingInList = setNextTimePair(task.getInterval(), lastTimingInList);
		task.addToTaskDateTimes(lastTimingInList);
		logger.log(Level.INFO, "Inserted a new timing for infinite recurrence");
	}

	private static void setTimingsBasedOnUntil(TaskObject task, LocalDateTimePair timePair,
			Interval interval) {
		while (!timePair.getStartDateTime().isAfter(interval.getUntil())) {
			// not after == before and equal
			task.addToTaskDateTimes(timePair);
			timePair = setNextTimePair(interval, timePair);
		}
		logger.log(Level.INFO, "Added recurring times till specified end date");
	}

	private static int retrieveMultiplier(int[] byDayArray) {
		int countMultiplier = 0;
		for (int i = 1; i <= 7; i++) {
			if (byDayArray[i] == 1) {
				countMultiplier++;
			}
		}
		return countMultiplier;
	}

	private static void setTimingsBasedOnCounts(TaskObject task, LocalDateTimePair timePair,
			Interval interval, int count) {
		int[] byDayArray = interval.getByDayArray();

		if (byDayArray[0] == 0) {
			for (int i = 0; i < count; i++) {
				task.addToTaskDateTimes(timePair);
				timePair = setNextTimePair(interval, timePair);
			}
		} else {
			if (count != -1) {
				// Updates the number of counts to reflect effects of byDay
				int countMultiplier = retrieveMultiplier(byDayArray);
				count = count * countMultiplier;
				task.setInterval(interval);
			}
			for (int i = 0; i < count; i++) {
				task.addToTaskDateTimes(timePair);
				timePair = setNextTimePair(interval, timePair);
			}
		}
		logger.log(Level.INFO, "Added recurring times for specified number of counts");
	}

	public static LocalDateTimePair setNextTimePair(Interval interval, LocalDateTimePair timePair) {
		LocalDateTime startDateTime = timePair.getStartDateTime();
		LocalDateTime endDateTime = timePair.getEndDateTime();
		LocalDateTimePair nextTimePair = new LocalDateTimePair();

		int[] byDayArray = interval.getByDayArray();

		if (byDayArray[0] == 0) {
			nextTimePair = obtainNextTime(interval, startDateTime, endDateTime);
		} else {
			// implementation with byDay
			nextTimePair = obtainNextTimeByDay(interval, startDateTime, endDateTime);
		}

		return nextTimePair;
	}

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

	private static LocalDateTimePair obtainNextTimeByDay(Interval interval, LocalDateTime startDateTime,
			LocalDateTime endDateTime) {

		Duration duration = Duration.ZERO;
		if (!endDateTime.equals(LocalDateTime.MAX)) {
			duration = Duration.between(startDateTime, endDateTime);
		}

		ArrayList<LocalDateTime> comparisonList = generateComparisonList(interval, startDateTime);
		assert (comparisonList.size() > 0);

		startDateTime = generateNextStartDateTime(interval, startDateTime, comparisonList);
		if (!endDateTime.equals(LocalDateTime.MAX)) {
			endDateTime = startDateTime.plus(duration);
		}

		return new LocalDateTimePair(startDateTime, endDateTime);
	}

	private static ArrayList<LocalDateTime> generateComparisonList(Interval interval,
			LocalDateTime startDateTime) {
		ArrayList<LocalDateTime> comparisonList = new ArrayList<LocalDateTime>();
		int[] byDayArray = interval.getByDayArray();
		for (int i = 1; i <= 7; i++) {
			if (byDayArray[i] == 1) {
				DayOfWeek dayOfWeek = DayOfWeek.of(i);
				LocalDateTime dateTimeForComparing = startDateTime.with(TemporalAdjusters.next(dayOfWeek));
				comparisonList.add(dateTimeForComparing);
			}
		}
		return comparisonList;
	}

	private static LocalDateTime generateNextStartDateTime(Interval interval, LocalDateTime startDateTime,
			ArrayList<LocalDateTime> comparisonList) {
		Collections.sort(comparisonList);
		// Takes the first timing as it is the earliest
		LocalDateTime newStartDateTime = comparisonList.get(0);

		// If in the same week, no need to consider the task interval
		boolean isInTheSameWeek = checkIfInTheSameWeek(startDateTime, newStartDateTime);
		if (!isInTheSameWeek) {
			newStartDateTime = modifyStartDateTime(interval, newStartDateTime);
		}
		return newStartDateTime;
	}

	private static boolean checkIfInTheSameWeek(LocalDateTime startDateTime, LocalDateTime newStartDateTime) {
		LocalDate startDate = startDateTime.toLocalDate();
		LocalDate newStartDate = newStartDateTime.toLocalDate();
		startDate = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		newStartDate = newStartDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		// Checks if their upcoming Sundays are equal
		if (newStartDate.isEqual(startDate)) {
			return true;
		} else {
			return false;
		}
	}

	private static LocalDateTime modifyStartDateTime(Interval interval, LocalDateTime newStartDateTime) {
		String frequency = interval.getFrequency();
		int timeInterval = interval.getTimeInterval();

		// To negate the effects of obtaining the date in the next week earlier
		newStartDateTime = newStartDateTime.minusWeeks(1);

		switch (frequency) {
		case FREQ_HOURLY:
			newStartDateTime = newStartDateTime.plusHours(timeInterval);
			break;

		case FREQ_DAILY:
			newStartDateTime = newStartDateTime.plusDays(timeInterval);
			break;

		case FREQ_WEEKLY:
			newStartDateTime = newStartDateTime.plusWeeks(timeInterval);
			break;

		case FREQ_MONTHLY:
			newStartDateTime = newStartDateTime.plusMonths(timeInterval);
			break;

		case FREQ_YEARLY:
			newStartDateTime = newStartDateTime.plusYears(timeInterval);
			break;
		}

		return newStartDateTime;
	}

	private static boolean checkIfInfiniteRecurrence(Interval interval) {
		if (interval.getCount() == -1 && interval.getUntil().isEqual(LocalDateTime.MAX)) {
			return true;
		} else {
			return false;
		}
	}

	// ----------------------------- GETTERS -----------------------------

}