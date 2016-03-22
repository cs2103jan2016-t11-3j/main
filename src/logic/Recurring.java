package logic;

import common.*;
import logic.add.Add;

import java.util.ArrayList;
import java.time.LocalDateTime;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Recurring {

	/*
	 * Insert following methods in Logic() constructor:
	 * Recurring.checkRecurringDeadlines(taskList);
	 * Recurring.checkRecurringEvents(taskList);
	 */
	
	/* For logic ************************************************************/
	public static void updateRecurringEvents(ArrayList<TaskObject> taskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getIsRecurring()) {
				updateEvent(taskList.get(i));
			}
		}
	}
	
	public static void updateEvent(TaskObject task) {
		LocalDateTime eventEndTime = task.getEndDateTime();
		if (eventEndTime.isAfter(LocalDateTime.now())) {
			renewEvent(task);
		}
	}
	
	public static void renewEvent(TaskObject task) {
		LocalDateTime newStartDateTime;
		LocalDateTime newEndDateTime;
		LocalDateTimePair nextEvent;
		
		task.removeFromTaskDateTimes(0);
		
		nextEvent = task.getTaskDateTimes().get(0);
		newStartDateTime = nextEvent.getStartDateTime();
		newEndDateTime = nextEvent.getEndDateTime();
		
		task.setStartDateTime(newStartDateTime);
		task.setEndDateTime(newEndDateTime);
	}

	/* For add ********************************************************************/

	public static void setAllRecurringEventTimes(TaskObject task) {
		Interval interval = task.getInterval();
		LocalDateTimePair eventDateTime = task.getTaskDateTimes().get(0);

		if (!interval.getUntil().equals(LocalDateTime.MAX)) {
			while (eventDateTime.getStartDateTime().isBefore(interval.getUntil())) {
				eventDateTime = setNextEventTime(interval, eventDateTime);
				task.addToTaskDateTimes(eventDateTime);
			}
		} else {
			if (interval.getCount() != -1) {
				for (int i = 0; i < interval.getCount(); i++) {
					eventDateTime = setNextEventTime(interval, eventDateTime);
					task.addToTaskDateTimes(eventDateTime);
				}
			}
		}
	}

	public static LocalDateTimePair setNextEventTime(Interval interval, LocalDateTimePair eventDateTime) {
		LocalDateTime startDateTime = eventDateTime.getStartDateTime();
		LocalDateTime endDateTime = eventDateTime.getEndDateTime();
		LocalDateTimePair nextEvent = new LocalDateTimePair();

		if (interval.getByDay().equals("")) {
			nextEvent = obtainNextEventTime(interval, startDateTime, endDateTime);
		} else {
			// implementation with byDay
		}

		return nextEvent;
	}

	public static LocalDateTimePair obtainNextEventTime(Interval interval, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		String frequency = interval.getFrequency();
		int timeInterval = interval.getTimeInterval();
		
		switch (frequency) {
		case FREQ_HOURLY:
			startDateTime = startDateTime.plusHours(timeInterval);
			endDateTime = endDateTime.plusHours(timeInterval);
			break;

		case FREQ_DAILY:
			startDateTime = startDateTime.plusDays(timeInterval);
			endDateTime = endDateTime.plusDays(timeInterval);
			break;

		case FREQ_WEEKLY:
			startDateTime = startDateTime.plusWeeks(timeInterval);
			endDateTime = endDateTime.plusWeeks(timeInterval);
			break;

		case FREQ_MONTHLY:
			startDateTime = startDateTime.plusMonths(timeInterval);
			endDateTime = endDateTime.plusMonths(timeInterval);
			break;

		case FREQ_YEARLY:
			startDateTime = startDateTime.plusYears(timeInterval);
			endDateTime = endDateTime.plusYears(timeInterval);
			break;
		}
		
		return new LocalDateTimePair(startDateTime, endDateTime);
	}
}



/*
 * UNUSED METHODS FROM PRIOR IMPLEMENTATION OF RECURRING
 * 	public static void updateRecurringEvents(ArrayList<TaskObject> taskList) throws Exception {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getIsRecurring()) {
				if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
					boolean hasEnded = checkIfEventOver(taskList.get(i));
					if (hasEnded) {
						boolean isStillRecurring = checkIfStillRecurring(taskList.get(i));
						if (isStillRecurring) {
							setNextEventTime(taskList.get(i));
						} else {
							markEventAsDone(taskList.get(i));
						}
					}
				}
			}
		}
	}

	private static boolean checkIfEventOver(TaskObject task) {
		LocalDateTime eventEndDateTime = task.getEndDateTime();
		if (eventEndDateTime.isBefore(LocalDateTime.now())) {
			return true;
		}

		return false;
	}

	private static boolean checkIfStillRecurring(TaskObject task) throws Exception {
		int count = task.getInterval().getCount();
		LocalDateTime until = task.getInterval().getUntil();

		if (count != -1) {
			if (count == 0) {
				return false;
			}

			task.getInterval().setCount(count - 1);
			return true;
		} else {
			if (!until.equals(LocalDateTime.MAX)) {
				if (until.isBefore(LocalDateTime.now())) {
					return false;
				}
				return true;
			} else {
				Exception e = new Exception(MESSAGE_INVALID_RECURRENCE);
				throw e;
			}
		}
	}

	public static void setNextEventTime(TaskObject task) {
		String frequency = task.getInterval().getFrequency();
		int timeInterval = task.getInterval().getTimeInterval();
		if (task.getInterval().getByDay().equals("")) {
			switch (frequency) {
			case FREQ_HOURLY:
				task.setStartDateTime(task.getStartDateTime().plusHours(timeInterval));
				task.setEndDateTime(task.getEndDateTime().plusHours(timeInterval));
				break;

			case FREQ_DAILY:
				task.setStartDateTime(task.getStartDateTime().plusDays(timeInterval));
				task.setEndDateTime(task.getEndDateTime().plusDays(timeInterval));
				break;

			case FREQ_WEEKLY:
				task.setStartDateTime(task.getStartDateTime().plusWeeks(timeInterval));
				task.setEndDateTime(task.getEndDateTime().plusWeeks(timeInterval));
				break;

			case FREQ_MONTHLY:
				task.setStartDateTime(task.getStartDateTime().plusMonths(timeInterval));
				task.setEndDateTime(task.getEndDateTime().plusMonths(timeInterval));
				break;

			case FREQ_YEARLY:
				task.setStartDateTime(task.getStartDateTime().plusYears(timeInterval));
				task.setEndDateTime(task.getEndDateTime().plusYears(timeInterval));
				break;
			}
		} else {
			// implementation for by day
		}
	
	private static void markEventAsDone(TaskObject task) {
		task.setStatus("done");
	}

	}
	*/
