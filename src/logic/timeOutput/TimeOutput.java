package logic.timeOutput;

import logic.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import common.TaskObject;
import common.Interval;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class TimeOutput {

	public static void setTimeOutputForGui(ArrayList<TaskObject> taskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
				setEventTimeOutput(taskList.get(i));
			} else {
				if (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) {
					setDeadlineTimeOutput(taskList.get(i));
				} else {
					taskList.get(i).setTimeOutputString("");
					// No time displayed for floating tasks
				}
			}
		}
	}

	/**
	 * Formats the timing for GUI to display for events, takes in the
	 * LocalDateTime objects startDateTime and endDateTime and processes it to
	 * form an output for GUI
	 * 
	 * @param event
	 */
	public static void setEventTimeOutput(TaskObject event) {
		String line;
		try {
			String[] start = createDateTimeArray(event.getStartDateTime());
			String[] end = createDateTimeArray(event.getEndDateTime());
			line = formatEventTimeOutput(start, end);
			event.setTimeOutputString(line);
		} catch (DateTimeException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private static String formatEventTimeOutput(String[] start, String[] end) throws NullPointerException {
		String formattedString = "";
		if (end[0].equals(start[0])) { // If start date == end date
			formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], end[1]);
			// End Date will not be printed
		} else {
			if (!end[1].equals("") && !start[1].equals("")) {
				String endDateTime = end[1].concat(" on ").concat(end[0]);
				formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], endDateTime);
				// End Date will be printed
			} else {
				if (end[1].equals("")) {
					if (start[1].equals("")) {
						formattedString = String.format(DISPLAY_TIME_EVENT_2, start[0], end[0]);
					} else {
						formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], end[0]);
					}
				} else {
					if (start[1].equals("")) {
						String endDateTime = end[1].concat(" on ").concat(end[0]);
						formattedString = String.format(DISPLAY_TIME_EVENT_2, start[0], endDateTime);
					}
				}
			}
		}
		return formattedString;
	}

	/**
	 * Formats the timing for GUI to display for deadlines
	 * 
	 * @param deadline
	 */
	public static void setDeadlineTimeOutput(TaskObject deadline) {
		String line;
		try {
			String[] start = createDateTimeArray(deadline.getStartDateTime());
			line = formatDeadlineTimeOutput(start);
			deadline.setTimeOutputString(line);
		} catch (DateTimeException e) {
			System.out.println(MESSAGE_DATE_TIME_CONVERSION_ERROR);
		} catch (NullPointerException e) {
			System.out.println(MESSAGE_NULL_POINTER_EXCEPTION);
		}
	}

	private static String formatDeadlineTimeOutput(String[] start) throws NullPointerException {
		String formattedString = "";
		if (start[1].equals("")) {
			formattedString = String.format(DISPLAY_TIME_DEADLINE, start[0]);
			// If time due is empty
		} else {
			String startDateTime = start[1].concat(" on ").concat(start[0]);
			formattedString = String.format(DISPLAY_TIME_DEADLINE, startDateTime);
		}
		return formattedString;
	}

	private static String[] createDateTimeArray(LocalDateTime time) throws DateTimeException {
		String[] timeArray = new String[2];
		if (!time.toLocalTime().equals(LocalTime.MAX)) {
			String line = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			timeArray = line.split("T", 2);
		} else {
			LocalDate date = time.toLocalDate();
			String line = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
			timeArray[0] = line;
			timeArray[1] = "";
		}
		return timeArray;
	}

	public static ArrayList<String> setRecurringEventTimeOutput(TaskObject foundTask) throws Exception {
		ArrayList<String> output = new ArrayList<String>();
		output.add(String.format(MESSAGE_TIMINGS_FOUND, foundTask.getTitle()));

		LocalDateTime startDateTime = foundTask.getStartDateTime();
		LocalDateTime endDateTime = foundTask.getEndDateTime();
		Interval interval = foundTask.getInterval();

		TaskObject dummyTask = new TaskObject(startDateTime, endDateTime, interval);
		if (!interval.getUntil().isEqual(LocalDateTime.MAX)) { // if there is no end date specified
			while (dummyTask.getStartDateTime().isBefore(dummyTask.getInterval().getUntil())) {
				TimeOutput.setEventTimeOutput(dummyTask);
				output.add(dummyTask.getTimeOutputString());
				Recurring.setNextEventTime(dummyTask);
			}
		} else {
			if (interval.getCount() != -1) {
				for (int i = 0; i <= interval.getCount(); i++) {
					TimeOutput.setEventTimeOutput(dummyTask);
					output.add(dummyTask.getTimeOutputString());
					Recurring.setNextEventTime(dummyTask);
				}
			} else {
				Exception e = new Exception(MESSAGE_INVALID_RECURRENCE);
				throw e;
			}
		}
		return output;
	}
}
