package logic.constants;

import common.TaskObject;

public class Strings {
	//common Constants
	public static final String CATEGORY_EVENT = "event";
	public static final String CATEGORY_DEADLINE = "deadline";

	// CommandFacade Constants
	public static final String CATEGORY_FLOATING = "floating";
	public static final String MESSAGE_INVALID_COMMAND = "Invalid command";
	
	// Logic Constants
	public static final String MESSAGE_ALERT_EVENT = "Events today:";
	public static final String MESSAGE_ALERT_DEADLINE = "Deadlines today:";
	public static final String MESSAGE_INFORMATION_EVENT = "Task: %1s; Time: %2s";
	public static final String MESSAGE_INFORMATION_DEADLINE = "Task: %1s; Due: %2s";
	
	// Recurring/Interval Constants
	public static final String FREQ_HOURLY = "HOURLY";
	public static final String FREQ_DAILY = "DAILY";
	public static final String FREQ_WEEKLY = "WEEKLY";
	public static final String FREQ_MONTHLY = "MONTHLY";
	public static final String FREQ_YEARLY = "YEARLY";
	public static final String MESSAGE_INVALID_RECURRENCE = "No valid end of recurrence";
	public static final String MESSAGE_INVALID_FREQUENCY = "Invalid frequency";
	
	//Add Constants
	public static final String MESSAGE_ADD = "Task added: ";
	public static final String MESSAGE_FAIL = "Failed to add task. ";
	public static final String MESSAGE_CLASH = "Task: %1s clashes with %2s";
	public static final String MESSAGE_INVALID_TIME = "Reason: Invalid time input.";
	public static final String MESSAGE_NULL_POINTER = "Reason: No object available to access.";

	//Delete Constants
	public static final String MESSAGE_DELETE = "Task deleted from AdultTaskFinder: %1s";
	public static final String MESSAGE_DELETE_ERROR = "Error deleting task from TaskFinder. ";
	public static final String MESSAGE_QUICK_DELETE_UNAVAILABLE_ERROR = "Quick delete unavailable.";
	public static final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Requested index does not exist";
	public static final String MESSAGE_DELETED_ALL = "All tasks deleted from AdultTaskFinder.";
	public static final String MESSAGE_RECURRENCE_DELETE = "Most recent occurrence deleted.";
	public static final String MESSAGE_RECURRENCE_DELETE_ALL = "All occurrences deleted.";
	public static final String MESSAGE_RECURRENCE_DELETE_ERROR = "Error deleting most recent occurrence.";
	public static final String MESSAGE_ONLY_ONE_OCCURRENCE_REMAINING = "Only one occurrence remaining. ";
	
	//Display Constants
	public static final String MESSAGE_EMPTY_LIST = "Task list is empty.";
	public static final String MESSAGE_SEARCH_RESULTS = "Search results:";
	public static final String DISPLAY_RESULT_DEADLINE = "%1$s. %2$s, %3$s, %4$shrs, %5$s";
	public static final String DISPLAY_RESULT_EVENT = "%1$s. %2$s, %3$s-%4$s, %5$shrs-%6$shrs, %7$s";
	public static final String DISPLAY_RESULT_FLOATING = "%1$s. %2$s, %3$s. TaskId: %4$s";

	//Edit Constants
	public static final String MESSAGE_TITLE_EDIT = "Title edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_DATE_ADD = "Added date '%1s'. ";
	public static final String MESSAGE_DATE_EDIT = "Date edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_START_DATE_ADD = "Added start date '%1s'. ";
	public static final String MESSAGE_START_DATE_EDIT = "Start date edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_TIME_FOR_ALL_OCCURRENCES_EDIT = "All times edited to '%1$s'. ";
	public static final String MESSAGE_START_TIME_FOR_ALL_OCCURRENCES_EDIT = "All start times edited to '%1$s'. ";
	public static final String MESSAGE_TIME_ADD = "Added time '%1s'. ";
	public static final String MESSAGE_TIME_EDIT = "Time edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_START_TIME_ADD = "Added start time '%1s'. ";
	public static final String MESSAGE_START_TIME_EDIT = "Start time edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_END_TIME_FOR_ALL_OCCURRENCES_EDIT = "All end times edited to '%1$s'. ";
	public static final String MESSAGE_END_DATE_ADD = "Added end date '%1s'. ";
	public static final String MESSAGE_END_DATE_EDIT = "End date edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_END_TIME_ADD = "Added end time '%1s'. ";
	public static final String MESSAGE_END_TIME_EDIT = "End time edited from '%1$s' to '%2$s'. ";
	public static final String MESSAGE_INTERVAL_EDIT = "Interval edited. ";
	public static final String MESSAGE_NO_EDIT = "No edits have been made.";

	//Mark Constants
	public static final String MESSAGE_DONE = "Task: '%1s' marked as completed";
	public static final String MESSAGE_MARK_ERROR = "Error marking task as complete";
	public static final String MESSAGE_INCOMPLETE = "Task: '%1s' is marked as incomplete";
	public static final String MESSAGE_OVERDUE = "Task: '%1s' is marked as overdue";
	
	//Save Constants
	public static final String MESSAGE_SAVE_TO = "Tasks have been, and will continue to be saved to %1s";
	public static final String MESSAGE_SAVE_AS = "Tasks have been saved to %1s";
	public static final String MESSAGE_SAVE_INVALID = "Save command is invalid";
	public static final String MESSAGE_SAVE_ERROR = "Error saving file to %1s";
	
	//Search Constants
	public static final String MESSAGE_NO_RESULTS_FOUND = "No results found for the specified parameters.";
	public static final String MESSAGE_TIMINGS_FOUND = "Timings for %1s:";
	public static final String MESSAGE_TIMINGS_NOT_FOUND = "Task not found";
	public static final String MESSAGE_RECURRING_TASK = "Information for recurring task: ";
	
	//TimeOutput Constants
	public static final String MESSAGE_DATE_TIME_CONVERSION_ERROR = "Error converting DateTime to GUI Display";
	public static final String MESSAGE_NULL_POINTER_EXCEPTION = "Not enough arguments within target object";
	public static final String DISPLAY_TIME_EVENT_1 = "on %1s, from %2s to %3s";
	public static final String DISPLAY_TIME_EVENT_2 = "from %1s to %2s";
	public static final String DISPLAY_TIME_DEADLINE = "by %1s";

	//Redo Constants
	public static final String MESSAGE_REDO = "%1$s redone.";
	public static final String MESSAGE_REDO_ERROR = "Nothing to redo!";
	public static final String MESSAGE_REDO_LIST_EMPTY = "Redo list is empty.";
	
	//Undo Constants
	public static final String MESSAGE_UNDO = "%1$s undone.";
	public static final String MESSAGE_UNDO_ERROR = "Nothing to undo!";
	public static final String MESSAGE_UNDO_LIST_EMPTY = "Undo list is empty.";
	
	// FOR PACKAGE-WIDE DEBUGGING PURPOSES
	public static void printTaskObjectFields(TaskObject taskObj) {
		System.out.println("title = " + taskObj.getTitle());
		System.out.println("start date time = " + taskObj.getStartDateTime());
		System.out.println("start end time = " + taskObj.getEndDateTime());
		System.out.println("category = " + taskObj.getCategory());
		System.out.println("status = " + taskObj.getStatus());
		System.out.println("task id = " + taskObj.getTaskId());
		System.out.println("isRecurring = " + taskObj.getIsRecurring());
	}

}

