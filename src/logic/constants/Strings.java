//@@author A0130622X

package logic.constants;

import common.TaskObject;

public class Strings {

	// common Constants
	public static final String CATEGORY_EVENT = "event";
	public static final String CATEGORY_DEADLINE = "deadline";
	public static final String CATEGORY_FLOATING = "floating";
	public static final String MESSAGE_INVALID_COMMAND = "Invalid command";
	public static final String STATUS_COMPLETED = "completed";
	public static final String STATUS_INCOMPLETE = "incomplete";
	public static final String STATUS_OVERDUE = "overdue";

	// Logic Constants
	public static final String MESSAGE_ALERT_EVENT = "Events today:";
	public static final String MESSAGE_ALERT_DEADLINE = "Deadlines today:";
	public static final String MESSAGE_INFORMATION_EVENT = "Task: %1s\nTime: %2s\n";
	public static final String MESSAGE_INFORMATION_DEADLINE = "Task: %1s\nDue: %2s\n";
	public static final String MESSAGE_BY_TODAY = "by end of today";
	public static final String MESSAGE_WELCOME_TASKS_OVERDUE_TODAY = "Tasks overdue and due today";
	public static final String MESSAGE_WELCOME_EMPTY = "No incomplete tasks. Add a task!";
	public static final String MESSAGE_WELCOME_TASKS_INCOMPLETE = "No tasks overdue and due today. \nDisplaying all incomplete tasks.";
	public static final String MESSAGE_FAILED_PROCESSING = "Failed to process input";

	// Recurring/Interval Constants
	public static final String FREQ_HOURLY = "HOURLY";
	public static final String FREQ_DAILY = "DAILY";
	public static final String FREQ_WEEKLY = "WEEKLY";
	public static final String FREQ_MONTHLY = "MONTHLY";
	public static final String FREQ_YEARLY = "YEARLY";
	public static final String MESSAGE_INVALID_RECURRENCE = "No valid end of recurrence";
	public static final String MESSAGE_INVALID_FREQUENCY = "Invalid frequency";

	// Add Constants
	public static final String MESSAGE_ADD_NON_RECURRING = "Task added: ";
	public static final String MESSAGE_ADD_RECURRING = "Recurring task added: ";
	public static final String MESSAGE_FAIL = "Failed to add task. ";
	public static final String MESSAGE_CLASH = "\nTask: %1s clashes with ";
	public static final String MESSAGE_INVALID_TIME = "Reason: Invalid time input.";
	public static final String MESSAGE_NULL_POINTER = "Reason: No object available to access.";
	public static final String MESSAGE_ADD_OVERDUE = "Task added is overdue.";
	public static final String MESSAGE_REQUEST_SAVE_LOCATION = "Error saving to file. Please enter \"save to\" followed by a valid file directory";

	// Delete Constants
	public static final String MESSAGE_DELETE = "Task deleted: %1s";
	public static final String MESSAGE_DELETE_ERROR = "Error deleting task. ";
	public static final String MESSAGE_DELETED_ALL = "All tasks deleted.";
	public static final String MESSAGE_COMPLETED_TASKS_DELETE = "All completed tasks deleted.";
	public static final String MESSAGE_SINGLE_OCCURRENCE_DELETE = "Occurrence %1s deleted.";
	public static final String MESSAGE_MOST_RECENT_OCCURRENCE_DELETE = "Most recent occurrence of task '%1s' deleted.";
	public static final String MESSAGE_ALL_OCCURRENCES_DELETE = "All occurrences of task '%1s' deleted.";
	public static final String MESSAGE_ONLY_ONE_OCCURRENCE_REMAINING = "Only one occurrence remaining. ";
	public static final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Requested index does not exist";
	public static final String MESSAGE_SINGLE_OCCURENCE_MISSING_ERROR = "Occurrence does not exist.";
	public static final String MESSAGE_FILE_NOT_FOUND = "File to save information to is not found.";
	public static final String MESSAGE_IO_EXCEPTION = "Unable to write tasks to storage file";

	// Display Constants
	public static final String MESSAGE_EMPTY_LIST = "Task list is empty.";
	public static final String MESSAGE_DISPLAYING_ALL_TASKS = "Displaying all tasks.";

	// Edit Constants
	public static final String MESSAGE_TITLE_EDIT = "Title edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_DATE_ADD = "Added date '%1s' to task '%2s'. ";
	public static final String MESSAGE_DATE_EDIT = "Date edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_DATE_FOR_OCCURRENCE_ADD = "Added date '%1s' to occurrence %2s. ";
	public static final String MESSAGE_DATE_FOR_OCCURRENCE_EDIT = "Date of occurrence %1s edited from '%2s' to %3s'. ";
	public static final String MESSAGE_DATE_FOR_ALL_OCCURRENCES_ADD = "Added date '%1s' to all occurrences. ";
	public static final String MESSAGE_DATE_FOR_ALL_OCCURRENCES_EDIT = "All dates edited to '%1s'. ";
	public static final String MESSAGE_TIME_ADD = "Added time '%1s' to task '%2s'. ";
	public static final String MESSAGE_TIME_EDIT = "Time edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_TIME_FOR_OCCURRENCE_ADD = "Added time '%1s' to occurrence %2s. ";
	public static final String MESSAGE_TIME_FOR_OCCURRENCE_EDIT = "TIme of occurrence %1s edited from '%2s' to %3s'. ";
	public static final String MESSAGE_TIME_FOR_ALL_OCCURRENCES_ADD = "Added time '%1s' to all occurrences. ";
	public static final String MESSAGE_TIME_FOR_ALL_OCCURRENCES_EDIT = "All times edited to '%1s'. ";
	public static final String MESSAGE_START_DATE_ADD = "Added start date '%1s' to task '%2s'. ";
	public static final String MESSAGE_START_DATE_EDIT = "Start date edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_START_DATE_FOR_OCCURRENCE_ADD = "Added start date '%1s' to occurrence %2s. ";
	public static final String MESSAGE_START_DATE_FOR_OCCURRENCE_EDIT = "Start date of occurrence %1s edited from '%2s' to '%3s'. ";
	public static final String MESSAGE_START_DATE_FOR_ALL_OCCURRENCES_ADD = "Added start date '%1s' to all occurrences. ";
	public static final String MESSAGE_START_DATE_FOR_ALL_OCCURRENCES_EDIT = "All start dates edited to '%1s'. ";
	public static final String MESSAGE_START_TIME_ADD = "Added start time '%1s' to task '%2s'. ";
	public static final String MESSAGE_START_TIME_EDIT = "Start time edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_START_TIME_FOR_OCCURRENCE_ADD = "Added start time '%1s' to occurrence %2s. ";
	public static final String MESSAGE_START_TIME_FOR_OCCURRENCE_EDIT = "Start time of occurrence %1s edited from '%2s' to '%3s'. ";
	public static final String MESSAGE_START_TIME_FOR_ALL_OCCURRENCES_ADD = "Added start time '%1s' to all occurrences. ";
	public static final String MESSAGE_START_TIME_FOR_ALL_OCCURRENCES_EDIT = "All start times edited to '%1s'. ";
	public static final String MESSAGE_END_DATE_ADD = "Added end date '%1s' to task '%2s'. ";
	public static final String MESSAGE_END_DATE_EDIT = "End date edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_END_DATE_FOR_OCCURRENCE_ADD = "Added end date '%1s' to occurrence %2s. ";
	public static final String MESSAGE_END_DATE_FOR_OCCURRENCE_EDIT = "End date of occurrence %1s edited from '%2s' to '%3s'. ";
	public static final String MESSAGE_END_DATE_FOR_ALL_OCCURRENCES_ADD = "Added end date '%1s' to all occurrences. ";
	public static final String MESSAGE_END_DATE_FOR_ALL_OCCURRENCES_EDIT = "All end dates edited to '%1s'. ";
	public static final String MESSAGE_END_TIME_ADD = "Added end time '%1s' to task '%2s'. ";
	public static final String MESSAGE_END_TIME_EDIT = "End time edited from '%1s' to '%2s'. ";
	public static final String MESSAGE_END_TIME_FOR_OCCURRENCE_ADD = "Added end time '%1s' to occurrence %2s. ";
	public static final String MESSAGE_END_TIME_FOR_OCCURRENCE_EDIT = "End time of occurrence %1s edited from '%2s' to '%3s'. ";
	public static final String MESSAGE_END_TIME_FOR_ALL_OCCURRENCES_ADD = "Added end time '%1s' to all occurrences. ";
	public static final String MESSAGE_END_TIME_FOR_ALL_OCCURRENCES_EDIT = "All end times edited to '%1s'. ";
	public static final String MESSAGE_INTERVAL_EDIT = "Interval edited. ";
	public static final String MESSAGE_NO_EDIT = "No edits have been made.";
	public static final String MESSAGE_NO_SUCH_OCCURRENCE_EXISTS = "No such occurrence exists. ";
	public static final String MESSAGE_SETTING_EDIT_INFO_ERROR = "Error setting edit information. ";

	// Load Constants
	public static final String KEYWORD_FROM = "from ";
	public static final String KEYWORD_BACKUP = "backup";
	public static final String MESSAGE_LOAD_SUCCESS = "Loaded file from: %1s";

	// Mark Constants
	public static final String MESSAGE_DONE = "Task: '%1s' marked as completed";
	public static final String MESSAGE_MARK_DONE_ERROR = "Error marking task as complete";
	public static final String MESSAGE_MARK_INCOMPLETE_ERROR = "Error marking task as incomplete";
	public static final String MESSAGE_MARK_OVERDUE_ERROR = "Error marking task as overdue";
	public static final String MESSAGE_INCOMPLETE = "Task: '%1s' is marked as incomplete";
	public static final String MESSAGE_OVERDUE = "Task: '%1s' is marked as overdue";

	// Save Constants
	public static final String MESSAGE_SAVE_TO = "Tasks have been, and will continue to be saved to %1s";
	public static final String MESSAGE_SAVE_AS = "Tasks have been saved to %1s";
	public static final String MESSAGE_SAVE_INVALID = "Save command is invalid";
	public static final String MESSAGE_SAVE_ERROR = "Error saving file to %1s";

	// Search Constants
	public static final String MESSAGE_SETTING_SEARCH_INFORMATION_ERROR = "Error setting search information.";
	public static final String MESSAGE_NO_RESULTS_FOUND = "No results found for the specified parameters.";
	public static final String MESSAGE_SEARCH_PARAMETERS = "Displaying tasks for the search parameters:\n%1s";
	public static final String MESSAGE_RECURRENCE_TIMINGS_DISPLAY = "Displaying recurrence timings for task %1s.";
	public static final String MESSAGE_NO_RECURRENCE_TIMING_DISPLAY = "Task %1s has no recurrence timings to be displayed.";
	public static final String MESSAGE_TIMINGS_FOUND = "Timings for %1s:";
	public static final String MESSAGE_TASK_INDEX_NOT_FOUND_ERROR = "No such task index found.";

	// TimeOutput Constants
	public static final String MESSAGE_DATE_TIME_CONVERSION_ERROR = "Error converting DateTime to GUI Display";
	public static final String MESSAGE_NULL_POINTER_EXCEPTION = "Not enough arguments within target object";
	public static final String DISPLAY_TIME_EVENT_1 = "from %1s on %2s \nto %3s on %4s";
	public static final String DISPLAY_TIME_EVENT_2 = "from %1s to %2s";
	public static final String DISPLAY_TIME_EVENT_3 = "from %1s to %2s \non %3s";
	public static final String DISPLAY_TIME_EVENT_4 = "from %1s on %2s \nto %3s";
	public static final String DISPLAY_TIME_EVENT_5 = "from %1s \nto %2s on %3s";
	public static final String DISPLAY_TIME_DEADLINE = "by %1s";

	// Redo Constants
	public static final String MESSAGE_REDO = "%1s redone.";
	public static final String MESSAGE_REDO_ERROR = "Nothing to redo!";
	public static final String MESSAGE_REDO_LIST_EMPTY = "Redo list is empty.";

	// Undo Constants
	public static final String MESSAGE_UNDO = "%1s undone.";
	public static final String MESSAGE_UNDO_ERROR = "Nothing to undo!";
	public static final String MESSAGE_UNDO_LIST_EMPTY = "Undo list is empty.";

	// Add Exception constants
	public static final String MESSAGE_ADD_EXCEPTION = "Error adding task to task list";
	public static final String MESSAGE_ADD_FLOATING_RECURRING = "Task with no time cannot be set as a recurring task";

	// Delete Exception constants
	public static final String MESSAGE_NO_INDEX_SPECIFIED_EXCEPTION = "Please specify a task index to delete.";
	
	// Edit Exception constants
	public static final String MESSAGE_EDIT_EXCEPTION = "Error editing %1s. ";
	
	// Load Exception constants
	public static final String MESSAGE_LOAD_EXCEPTION_IO = "Error reading information from external file storage";
	public static final String MESSAGE_LOAD_EXCEPTION_FNF = "Saved storage file not found";
	public static final String MESSAGE_LOAD_EXCEPTION_JSON = "Error interpreting information from external file storage";
	public static final String MESSAGE_LOAD_EXCEPTION_IFP = "Invalid file path used";
	public static final String MESSAGE_LOAD_BACKUP = "Please key in \"load backup\" to load the backup file";

	// Mark Exception constants
	public static final String MESSAGE_MARK_EXCEPTION = "Error marking task: %1s.";
	public static final String MESSAGE_MARK_EXCEPTION_SAME_STATUS = "Task already has status: %1s";

	// Recurring Exception constants
	public static final String MESSAGE_RECURRENCE_EXCEPTION = "Error handling recurring task: %1s";
	public static final String MESSAGE_RECURRENCE_EXCEPTION_INVALID_STATUS = "Invalid status to change to";
	public static final String MESSAGE_RECURRENCE_EXCEPTION_INVALID_INTERVAL = "Interval is invalid";
	public static final String MESSAGE_RECURRENCE_EXCEPTION_INVALID_UNTIL = "End of Recurrence is invalid";
	public static final String MESSAGE_RECURRENCE_EXCEPTION_CORRUPTED = "Corrupted task: %1s removed";

	// Search Exception constants
	public static final String MESSAGE_SEARCH_BY_DATE_REQUIRED_EXCEPTION = "Search by time requires a search by date as well.";
	
	// FOR PACKAGE-WIDE DEBUGGING PURPOSES
	public static void printTaskObjectFields(TaskObject taskObj) {
		System.out.println("title = " + taskObj.getTitle());
		System.out.println("start date time = " + taskObj.getStartDateTime());
		System.out.println("start end time = " + taskObj.getEndDateTime());
		System.out.println("category = " + taskObj.getCategory());
		System.out.println("status = " + taskObj.getStatus());
		System.out.println("task id = " + taskObj.getTaskId());
		System.out.println("isRecurring = " + taskObj.getIsRecurring());
		System.out.println("taskDateTimes size = " + taskObj.getTaskDateTimes().size());
	}

}
