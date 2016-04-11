//@@author A0124052X

package logic.timeoutput;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

import java.util.ArrayList;
import java.util.Locale;

import common.TaskObject;

import static logic.constants.Strings.*;

public class TimeOutput {

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");
	static DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("dd/MM");

	/**
	 * Formats a time output for GUI to display, for the entire list of tasks. For events, the following are
	 * the permutations for display: <br>
	 * 
	 * 1. with event within a day, start and end date in the same week as the current week:
	 * "from 15:00 to 16:00 on Thursday 24/03" <br>
	 * 2. with event across days, start and end date in the same week as the current week:
	 * "from 15:00 on Thursday 24/03 to 16:00 on Friday 25/03" <br>
	 * 3. with event across days, start date in current week, end date in next week:
	 * "from 15:00 on Thursday 24/03 to 16:00 on next Friday 01/04" <br>
	 * 4. with events within a day, start and end date in different week from current week:
	 * "from 15:00 to 16:00 on 31/03/16" <br>
	 * 5. with events across days, start and end date in different week from current week:
	 * "from 15:00 on 31/03/16 to 16:00 on 01/04/16" <br>
	 * 
	 * * if start time does not exist, format will be "from -date- to -end time- on date" <br>
	 * * if both start, end time do not exist, format will be "from -date- to -end date-" <br>
	 * * if only end time does not exist, format will be "from -start time- on -start date- to -end date-"
	 * <br>
	 * 
	 * <br>
	 * For deadlines, the following are the permutations for display: <br>
	 * 1. deadlines without time in same week: "by Thursday 24/03" <br>
	 * 2. deadlines with time in same week: "by 15:00 on Thursday 24/03" <br>
	 * 3. deadlines without time in different week: "by 31/03/16" <br>
	 * 4. deadlines with time in different week: "by 15:00 on 31/03/16"
	 * 
	 * @param taskList
	 *            contains all the tasks for the user
	 */

	public static void setTimeOutputForGui(ArrayList<TaskObject> taskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
				setEventTimeOutput(taskList.get(i));
			} else if (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) {
				setDeadlineTimeOutput(taskList.get(i));
			} else {
				taskList.get(i).setTimeOutputString("");
				// No time displayed for floating tasks
			}
		}
	}
	
	/**
	 * Common method called by Delete and Edit to set the timeOutput for a task. Method basically contains
	 * control statements to determine if it is a deadline or an event, and calls the relevant helper
	 * functions to set the timeOutput
	 * 
	 * @param task
	 *            Task whose timeOutput is to be modified
	 */
	public static void setTaskTimeOutput(TaskObject task) {
		if (task.getCategory().equals(CATEGORY_DEADLINE)) {
			setDeadlineTimeOutput(task);
		} else if (task.getCategory().equals(CATEGORY_EVENT)) {
			setEventTimeOutput(task);
		}
	}

	// ==================================================================================
	// First Level of Abstraction
	// ==================================================================================
	/**
	 * Helper method to setTimeOutputForGui, but also called individually by methods in Search and Recurring
	 * to set the timeOutputString for a specific event without running through the entire task list. Format
	 * of timeOutputString identical to those specified in setTimeOutputForGui.
	 * 
	 * @param event
	 *            TaskObject of category "event" whose timeOutputString is to be modified
	 */
	public static void setEventTimeOutput(TaskObject event) {
		String line;
		String[] start;
		String[] end;

		try {
			start = createDateTimeArray(event.getStartDateTime(), false);
			if (checkIfInTheSameWeek(event.getStartDateTime().toLocalDate())) {
				end = createDateTimeArray(event.getEndDateTime(), true);
			} else {
				end = createDateTimeArray(event.getEndDateTime(), false);
			}
			line = formatEventTimeOutput(start, end);
			event.setTimeOutputString(line);
		} catch (DateTimeException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A variant of setEventTimeOutput, for Recurring tasks to display all occurrences in the desired
	 * timeOutput format. Only called from searchByIndex in Search.
	 * 
	 * @param startDateTime
	 *            LocalDateTime containing the start date and time of the event
	 * @param endDateTime
	 *            LocalDateTime containing the end date and time of the event
	 * @return String containing the timeOutput for this set of start and end timings
	 */
	public static String setEventTimeOutput(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		String line = "";
		String[] start;
		String[] end;

		try {
			start = createDateTimeArray(startDateTime, false);
			if (checkIfInTheSameWeek(startDateTime.toLocalDate())) {
				end = createDateTimeArray(endDateTime, true);
			} else {
				end = createDateTimeArray(endDateTime, false);
			}
			line = formatEventTimeOutput(start, end);
		} catch (DateTimeException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return line;
	}

	/**
	 * Helper method to setTimeOutputForGui, but also called individually by methods in Search and Recurring
	 * to set the timeOutputString for a specific deadline without running through the entire task list.
	 * Format of timeOutputString identical to those specified in setTimeOutputForGui.
	 * 
	 * @param deadline
	 *            TaskObject of category "deadline" whose timeOutput is to be set
	 */
	public static void setDeadlineTimeOutput(TaskObject deadline) {
		String line;
		try {
			String[] start = createDateTimeArray(deadline.getStartDateTime(), false);
			line = formatDeadlineTimeOutput(start);
			deadline.setTimeOutputString(line);
		} catch (DateTimeException e) {
			System.out.println(MESSAGE_DATE_TIME_CONVERSION_ERROR);
		} catch (NullPointerException e) {
			System.out.println(MESSAGE_NULL_POINTER_EXCEPTION);
		}
	}

	/**
	 * A variant of setDeadlineTimeOutput, for Recurring tasks to display all occurrences in the desired
	 * timeOutput format. Only called from searchByIndex in Search.
	 * 
	 * @param startDateTime
	 *            LocalDateTime containing the deadline of this task
	 * @return String containing the timeOutput information for this task
	 */
	public static String setDeadlineTimeOutput(LocalDateTime startDateTime) {
		String line = "";
		try {
			String[] start = createDateTimeArray(startDateTime, false);
			line = formatDeadlineTimeOutput(start);
		} catch (DateTimeException e) {
			System.out.println(MESSAGE_DATE_TIME_CONVERSION_ERROR);
		} catch (NullPointerException e) {
			System.out.println(MESSAGE_NULL_POINTER_EXCEPTION);
		}
		return line;
	}

	// ==================================================================================
	// Second Level of Abstraction
	// ==================================================================================
	private static String formatEventTimeOutput(String[] start, String[] end) throws NullPointerException {
		String formattedString = "";
		if (end[0].equals(start[0])) { // If start date == end date
			formattedString = String.format(DISPLAY_TIME_EVENT_3, start[1], end[1], end[0]);
			// End Date will not be printed
		} else if (!end[1].equals("") && !start[1].equals("")) {
			// if both start and end time exist
			formattedString = String.format(DISPLAY_TIME_EVENT_1, start[1], start[0], end[1], end[0]);
			// End Date will be printed
		} else if (end[1].equals("")) { // if end time does not exist
			if (start[1].equals("")) { // if start time does not exist
				formattedString = String.format(DISPLAY_TIME_EVENT_2, start[0], end[0]);
			} else { // if start time exists
				formattedString = String.format(DISPLAY_TIME_EVENT_4, start[1], start[0], end[0]);
			}
		} else if (start[1].equals("")) { // if start time does not exist and end time exists
			formattedString = String.format(DISPLAY_TIME_EVENT_5, start[0], end[1], end[0]);
		}
		return formattedString;
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

	// =================================================================================
	// Lower Levels of Abstraction
	// =================================================================================
	private static String[] createDateTimeArray(LocalDateTime dateTime, boolean isEndDate)
			throws DateTimeException {
		String[] timeArray = new String[2];

		timeArray[0] = processRelativeDate(dateTime.toLocalDate(), isEndDate);

		if (!dateTime.toLocalTime().equals(LocalTime.MAX)) {
			timeArray[1] = dateTime.toLocalTime().toString();
		} else {
			timeArray[1] = "";
		}
		return timeArray;
	}

	private static String processRelativeDate(LocalDate date, boolean isEndDate) {
		String dateString = "";
		String dayOfWeek = "";
		boolean isInTheSameWeek = checkIfInTheSameWeek(date);
		// event in this week: e.g Thursday 24/03, applies to both start end
		if (isInTheSameWeek) {
			dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			dateString = dayOfWeek + " " + date.format(shortFormatter);
		} else if (isEndDate) { // only applies for ending date of events
			boolean isInTheNextWeek = checkIfInTheNextWeek(date);
			if (isInTheNextWeek) {
				// event end date in next week: e.g. next Monday 28/03
				dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
				dateString = "next " + dayOfWeek + " " + date.format(shortFormatter);
			} else {
				// event end date not in next week: e.g. 19/04
				dateString = date.format(shortFormatter);
			}
		} else {
			// event start date, deadline date: e.g. 19/04/16
			dateString = date.format(formatter);
		}
		return dateString;
	}

	private static boolean checkIfInTheSameWeek(LocalDate date) {
		DayOfWeek sunday = DayOfWeek.SUNDAY;
		LocalDate thisSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(sunday));
		LocalDate lastSunday = thisSunday.minusWeeks(1);

		if (!date.isAfter(thisSunday) && date.isAfter(lastSunday)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean checkIfInTheNextWeek(LocalDate date) {
		DayOfWeek sunday = DayOfWeek.SUNDAY;
		LocalDate thisSunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(sunday));
		LocalDate nextSunday = thisSunday.plusWeeks(1);

		if (!date.isAfter(nextSunday) && date.isAfter(thisSunday)) {
			return true;
		} else {
			return false;
		}
	}
}