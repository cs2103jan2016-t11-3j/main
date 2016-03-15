package logic;

import common.*;
import logic.add.Add;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Recurring {
	
	/*
	 * Insert following methods in Logic() constructor:
	 * Recurring.checkRecurringDeadlines(taskList);
	 * Recurring.checkRecurringEvents(taskList);
	 */

	public static void checkRecurringDeadlines(ArrayList<TaskObject> taskList) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getIsRecurring() && taskList.get(i).getCategory().equals("deadline")) {
				ArrayList<LocalDateTimePair> taskTimes = taskList.get(i).getTaskDateTime();
				boolean isOver = checkIfPastDeadline(taskTimes);
				if(isOver) {
					sortNextDeadlineTime(taskTimes, taskList.get(i).getInterval());
				}
			}
		}
	}
	
	public static boolean checkIfPastDeadline(ArrayList<LocalDateTimePair> taskTimes) {
		if(LocalDateTime.now().isBefore(taskTimes.get(1).getStartDateTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void sortNextDeadlineTime(ArrayList<LocalDateTimePair> taskTimes, Interval interval) {
		if(taskTimes.get(0).getEndDateTime().isEqual(LocalDateTime.MAX)) {
			// If there's no set date for end of recurrence
			LocalDateTime lastAddedTime = taskTimes.get(taskTimes.size() - 1).getStartDateTime();
			lastAddedTime = Add.addInterval(lastAddedTime, interval);
			taskTimes.add(new LocalDateTimePair(lastAddedTime));
		}
			taskTimes.remove(1);
	}
	
	public static void checkRecurringEvents(ArrayList<TaskObject> taskList) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getIsRecurring() && taskList.get(i).getCategory().equals("event")) {
				ArrayList<LocalDateTimePair> taskTimes = taskList.get(i).getTaskDateTime();
				boolean isOver = checkIfPastEvent(taskTimes);
				if(isOver) {
					sortNextEventTime(taskTimes, taskList.get(i).getInterval());
				}
			}
		}
	}
	
	public static boolean checkIfPastEvent(ArrayList<LocalDateTimePair> taskTimes) {
		if (LocalDateTime.now().isBefore(taskTimes.get(1).getEndDateTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void sortNextEventTime(ArrayList<LocalDateTimePair> taskTimes, Interval interval) {
		if(taskTimes.get(0).getEndDateTime().isEqual(LocalDateTime.MAX)) {
			LocalDateTime lastAddedStartTime = taskTimes.get(taskTimes.size() - 1).getStartDateTime();
			LocalDateTime lastAddedEndTime = taskTimes.get(taskTimes.size() - 1).getEndDateTime();
			lastAddedStartTime = Add.addInterval(lastAddedStartTime, interval);
			lastAddedEndTime = Add.addInterval(lastAddedEndTime, interval);
			taskTimes.add(new LocalDateTimePair(lastAddedStartTime, lastAddedEndTime));
		}
	}
}
