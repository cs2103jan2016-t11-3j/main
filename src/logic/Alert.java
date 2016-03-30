package logic;

import static logic.constants.Strings.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import common.TaskObject;

public class Alert {
	
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");
	static DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("dd/MM");
	
	public static ArrayList<String> createAlertOutput(ArrayList<TaskObject> taskList) {
		ArrayList<String> alertOutput = new ArrayList<String> ();
		
		String taskInformation = "";
		boolean hasEvent = false;
		boolean hasDeadline = false;
		alertOutput.add(MESSAGE_ALERT_EVENT);
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
				if (taskList.get(i).getStartDateTime().toLocalDate().isEqual(LocalDate.now())) {
					String time = createEventAlertTime(taskList.get(i));
					taskInformation = String.format(MESSAGE_INFORMATION_EVENT, taskList.get(i).getTitle(), time);
					alertOutput.add(taskInformation);
					hasEvent = true;
				}
			}
		}

		if (!hasEvent) {
			alertOutput.remove(alertOutput.size() - 1);
		}

		alertOutput.add(MESSAGE_ALERT_DEADLINE);
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) {
				if (taskList.get(i).getStartDateTime().toLocalDate().isEqual(LocalDate.now())) {
					String time = createDeadlineAlertTime(taskList.get(i).getStartDateTime());
					taskInformation = String.format(MESSAGE_INFORMATION_DEADLINE, taskList.get(i).getTitle(), time);
					alertOutput.add(taskInformation);
					hasDeadline = true;
				}
			}
		}

		if (!hasDeadline) {
			alertOutput.remove(alertOutput.size() - 1);
		}
		
		return alertOutput;
	}

	private static String createDeadlineAlertTime(LocalDateTime deadline) {
		String endTime;
		if (deadline.toLocalTime().equals(LocalTime.MAX)) {
			endTime = MESSAGE_BY_TODAY;
		} else {
			endTime = deadline.toLocalTime().toString();
		}
		return endTime;
	}

	private static String createEventAlertTime(TaskObject task) {
		String timeString;
		String startTime = "";
		String endDate = "";
		String endTime = "";

		// without start time
		if (task.getStartDateTime().toLocalTime().equals(LocalTime.MAX)) {
			startTime = "";
		} else {
			// with start time
			startTime = task.getStartDateTime().toLocalTime().toString() + " ";
		}

		// if start date == end date
		if (task.getEndDateTime().toLocalDate().equals(task.getStartDateTime().toLocalDate())) {
			// with end time
			if (!task.getEndDateTime().toLocalTime().equals(LocalTime.MAX)) {
				endTime = "to " + task.getEndDateTime().toLocalTime().toString() + " ";
			} else {
				// without end time
				if (startTime.equals("")) {
					startTime = "not specified ";
				} else {
					startTime = "from " + startTime + "today ";
				}
			}
		} else {
			endDate = task.getEndDateTime().toLocalDate().format(shortFormatter) + " ";
			// with end time
			if (!task.getEndDateTime().toLocalTime().equals(LocalTime.MAX)) {
				endTime = "to " + task.getEndDateTime().toLocalTime().toString() + " ";
				endDate = "on " + endDate;
			} else {
				endDate = "to " + endDate;
			}
		}
		timeString = startTime + endTime + endDate;
		return timeString;
	}

}
