package logic.timeOutput;

import logic.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import common.TaskObject;

public class TimeOutput {

	public static final String CATEGORY_EVENT = "event";
	public static final String CATEGORY_DEADLINE = "deadline";
	public static final String CATEGORY_FLOATING = "floating";

	private static final String MESSAGE_DATE_TIME_CONVERSION_ERROR = "Error converting DateTime to GUI Display";
	private static final String MESSAGE_NULL_POINTER_EXCEPTION = "Not enough arguments within target object";

	private static final String DISPLAY_TIME_EVENT_1 = "on %1s, from %2s to %3s";
	private static final String DISPLAY_TIME_EVENT_2 = "from %1s to %2s";
	private static final String DISPLAY_TIME_DEADLINE = "by %1s";

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
	private static void setEventTimeOutput(TaskObject event) {
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
				if(end[1].equals("")) {
					if(start[1].equals("")) {
						formattedString = String.format(DISPLAY_TIME_EVENT_2, start[0], end[0]);
					} else {
						formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], end[0]);
					}
				} else {
					if(start[1].equals("")) {
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
	private static void setDeadlineTimeOutput(TaskObject deadline) {
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
}
