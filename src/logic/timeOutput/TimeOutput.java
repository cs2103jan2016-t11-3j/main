package logic.timeOutput;

import logic.*;
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
import common.Interval;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class TimeOutput {

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");

	/**
	 * Formats a time output for GUI to display, for the entire list of tasks.
	 * For events, the following are the permutations for display: <br>
	 * 
	 * 1. with event within a day, start and end date in the same week as the
	 * current week: "on Thursday 24/03/16, from 15:00 to 16:00" <br>
	 * 2. with event across days, start and end date in the same week as the
	 * current week:
	 * "on Thursday 24/03/16, from 15:00 to 16:00 on Friday 25/03/16" <br>
	 * 3. with event across days, start date in current week, end date in next
	 * week: "on Thursday 24/03/16, from 15:00 to 16:00 on next Friday 01/04/16"
	 * <br>
	 * 4. with events within a day, start and end date in different week from
	 * current week: "on 31/03/16, from 15:00 to 16:00" <br>
	 * 5. with events across days, start and end date in different week from
	 * current week: "on 31/03/16, from 15:00 to 16:00 on 01/04/2016" <br>
	 * 
	 * * if start time does not exist, format will be
	 * "from -date- to -end time- on date" <br>
	 * * if both start, end time do not exist, format will be
	 * "from -date- to -end date-" <br>
	 * * if only end time does not exist, format will be "on -start date-, from
	 * -start time- to -end date-" <br>
	 * 
	 * <br>
	 * For deadlines, the following are the permutations for display: <br>
	 * 1. deadlines without time in same week: "by Thursday 24/03/16" <br>
	 * 2. deadlines with time in same week: "by 15:00 on Thursday 24/03/16" <br>
	 * 
	 * @param taskList
	 *            contains all the tasks for the user
	 */

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

	private static String formatEventTimeOutput(String[] start, String[] end) throws NullPointerException {
		String formattedString = "";
		if (end[0].equals(start[0])) { // If start date == end date
			formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], end[1]);
			// End Date will not be printed
		} else {
			if (!end[1].equals("") && !start[1].equals("")) { 
				// if both start and end time exist
				String endDateTime = end[1].concat(" on ").concat(end[0]);
				formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], endDateTime);
				// End Date will be printed
			} else {
				if (end[1].equals("")) { // if end time does not exist
					if (start[1].equals("")) { // if start time does not exist
						formattedString = String.format(DISPLAY_TIME_EVENT_2, start[0], end[0]);
					} else { // if start time exists
						formattedString = String.format(DISPLAY_TIME_EVENT_1, start[0], start[1], end[0]);
					}
				} else { // if end time exists
					if (start[1].equals("")) { // if start time does not exist
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
			String[] start = createDateTimeArray(deadline.getStartDateTime(), false);
			line = formatDeadlineTimeOutput(start);
			deadline.setTimeOutputString(line);
		} catch (DateTimeException e) {
			System.out.println(MESSAGE_DATE_TIME_CONVERSION_ERROR);
		} catch (NullPointerException e) {
			System.out.println(MESSAGE_NULL_POINTER_EXCEPTION);
		}
	}
	
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

	private static String[] createDateTimeArray(LocalDateTime time, boolean isEndDate) throws DateTimeException {
		String[] timeArray = new String[2];

		timeArray[0] = processRelativeDate(time.toLocalDate(), isEndDate);

		if (!time.toLocalTime().equals(LocalTime.MAX)) {
			timeArray[1] = time.toLocalTime().toString();
		} else {
			timeArray[1] = "";
		}
		return timeArray;
	}

	private static String processRelativeDate(LocalDate date, boolean isEndDate) {
		String dateString = "";
		String dayOfWeek = "";
		boolean isInTheSameWeek = checkIfInTheSameWeek(date);
		// event in this week: e.g Thursday 24/03/16, applies to both start end
		if (isInTheSameWeek) {
			dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			dateString = dayOfWeek + " " + date.format(formatter);
		} else {
			// only applies for ending date of events
			if (isEndDate) {
				boolean isInTheNextWeek = checkIfInTheNextWeek(date);
				if (isInTheNextWeek) {
					// event end date in next week: e.g. next Monday 28/03/16
					dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
					dateString = "next " + dayOfWeek + " " + date.format(formatter);
				} else {
					// event end date not in next week: e.g. 19/04/16
					dateString = date.format(formatter);
				}
			} else {
				// event start date, deadline date: e.g. 19/04/16
				dateString = date.format(formatter);
			}
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
	
	public static void setTaskTimeOutput(TaskObject task) {
		if (task.getCategory().equals(CATEGORY_DEADLINE)) {
			setDeadlineTimeOutput(task);
		} else {
			if (task.getCategory().equals(CATEGORY_EVENT)) {
				setEventTimeOutput(task);
			}
		}
	}
}