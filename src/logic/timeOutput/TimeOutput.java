package logic.timeOutput;

import logic.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import common.TaskObject;

public class TimeOutput {

	private static final String MESSAGE_DATE_TIME_CONVERSION_ERROR = "Error converting DateTime to GUI Display";
	private static final String MESSAGE_NULL_POINTER_EXCEPTION = "Not enough arguments within target object";

	private static final String DISPLAY_TIME_EVENT = "on %1s, from %2s to %3s";	
	private static final String DISPLAY_TIME_DEADLINE = "by %1s"; 
	
	public static void setTimeOutputForGui(ArrayList<TaskObject> taskList) {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getCategory().equals(Logic.CATEGORY_EVENT)) {
				setEventTimeOutput(taskList.get(i));
			} else {
				if(taskList.get(i).getCategory().equals(Logic.CATEGORY_DEADLINE)) {
					setDeadlineTimeOutput(taskList.get(i));
				} else {
					taskList.get(i).setTimeOutputString(""); // No time displayed for floating
				}
			}
		}
	}
	
	/**
	 * Formats the timing for GUI to display for events, takes in the LocalDateTime objects
	 * startDateTime and endDateTime and processes it to form an output for GUI
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
			System.out.println(MESSAGE_DATE_TIME_CONVERSION_ERROR);
		} catch (NullPointerException e) {
			System.out.println(MESSAGE_NULL_POINTER_EXCEPTION);
		}
	}
	
	private static String formatEventTimeOutput(String[] start, String[] end) throws NullPointerException{
		String formattedString = "";
		if(end[0].equals(start[0])) { // If start date == end date
			formattedString = String.format(DISPLAY_TIME_EVENT, start[0], start[1], end[1]);
			// End Date will not be printed 
		} else {
			String endDateTime = end[1].concat(" on ").concat(end[0]);
			formattedString = String.format(DISPLAY_TIME_EVENT,  start[0], start[1], endDateTime);
			// End Date will be printed
		}
		return formattedString;
	}
	
	/**
	 * Formats the timing for GUI to display for deadlines
	 * @param deadline
	 */
	private static void setDeadlineTimeOutput(TaskObject deadline) {
		String line;
		try {
			String[] end = createDateTimeArray(deadline.getEndDateTime());
			line = formatDeadlineTimeOutput(end);
			deadline.setTimeOutputString(line);
		} catch (DateTimeException e) {
			System.out.println(MESSAGE_DATE_TIME_CONVERSION_ERROR);
		} catch (NullPointerException e) {
			System.out.println(MESSAGE_NULL_POINTER_EXCEPTION);
		}
	}	
	
	private static String formatDeadlineTimeOutput(String[] end) throws NullPointerException {
		String formattedString = "";
		if(end[1].equals(null)) {
			formattedString = String.format(DISPLAY_TIME_DEADLINE, end[0]);
			// If time due is empty
		} else {
			String endDateTime = end[1].concat(" on ").concat(end[0]);
			formattedString = String.format(DISPLAY_TIME_DEADLINE, endDateTime);
		}
		return formattedString;
	}
	
	private static String[] createDateTimeArray(LocalDateTime time) throws DateTimeException { 
		String line = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String[] timeArray = line.split("T", 2);
		return timeArray;
	}
}
