package logic;

import common.*;
import logic.add.Add;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Recurring {

	public final static String FREQ_HOURLY = "HOURLY";
	public final static String FREQ_DAILY = "DAILY";
	public final static String FREQ_WEEKLY = "WEEKLY";
	public final static String FREQ_MONTHLY = "MONTHLY";
	public final static String FREQ_YEARLY = "YEARLY";

	private static final String CATEGORY_EVENT = "event";
	
	private static final String MESSAGE_INVALID_RECURRENCE = "No valid end of recurrence";
	/*
	 * Insert following methods in Logic() constructor:
	 * Recurring.checkRecurringDeadlines(taskList);
	 * Recurring.checkRecurringEvents(taskList);
	 */

	public static void updateRecurringEvents(ArrayList<TaskObject> taskList) throws Exception {
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
		} else {
			return false;
		}
	}
	
	private static boolean checkIfStillRecurring(TaskObject task) throws Exception{
		int count = task.getInterval().getCount();
		LocalDateTime until = task.getInterval().getUntil();
		
		if (count != -1) {
			if (count == 0) {
				return false;
			} else {
				task.getInterval().setCount(count - 1);
				return true;
			}
		} else {
			if (!until.equals(LocalDateTime.MAX)) {
				if (until.isBefore(LocalDateTime.now())) {
					return false;
				} else {
					return true;
				}
			} else {
				Exception e = new Exception(MESSAGE_INVALID_RECURRENCE);
				throw e;
			}
		}		
	}

	private static void setNextEventTime(TaskObject task) {
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
	}
	
	private static void markEventAsDone(TaskObject task) {
		task.setStatus("done");
	}
}