package logic.edit;

import storage.FileStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.*;

import common.CommandObject;
import common.Interval;
import common.LocalDateTimePair;
import common.TaskObject;
import logic.Recurring;
import logic.exceptions.RecurrenceException;
import logic.timeOutput.TimeOutput;

import static logic.constants.Strings.*;

/**
 * Creates an Edit object which will edit the information of a selected task/occurrence. <br>
 * 
 * All the edit information is first set based on the information in the CommandObject that is passed in the
 * constructor. This is done by having boolean checks for each attribute. The attributes that can be edited
 * are: <br>
 * 1. Title <br>
 * 2. Start date <br>
 * 3. Start time <br>
 * 4. End date <br>
 * 5. End time <br>
 * 6. Interval <br>
 * 
 * If it is a single occurrence being edited instead of a task, there will only be 4 attributes that can be
 * edited: <br>
 * 1. Start date <br>
 * 2. Start time <br>
 * 3. End date <br>
 * 4. End time <br>
 * 
 * If the task to be edited is a recurring task, there are 2 ways that the edit can be processed: <br>
 * (a) Edit only the upcoming occurrence - this will be the default option, where only the date/time data for
 * the first occurrence in the ArrayList<LocalDateTimePair> will be modified. <br>
 * (b) Edit all occurrences - this will be called if the user input contains the 'all' keyword, i.e. 'edit all
 * <index> ...'. This will edit the date/time details for all occurrences in the ArrayList. <br>
 * 
 * @param commandObj
 *            Contains all the information regarding what to change. The TaskObject contained within contains
 *            all the new edit information, while the index in this CommandObject contains the relative
 *            position of the task in the lastOutputTaskList.
 * @param lastOutputTaskList
 *            Contains the task list that is currently being displayed to the user.
 * @param taskList
 *            Contains the entire list of all tasks.
 * @param lastSearchedIndex
 *            Contains the last searched index - it is only filled if it is an edit of a n-th occurrence.
 * 
 * @author ChongYan, RuiBin
 *
 */

public class Edit {

	private static final Logger LOGGER = Logger.getLogger(Edit.class.getName());

	private CommandObject commandObj;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<TaskObject> taskList;
	private int lastSearchedIndex;
	private ArrayList<String> tempOutput = new ArrayList<String>();
	private ArrayList<String> output = new ArrayList<String>();

	private TaskObject originalTask = new TaskObject(); // original task info; for undo purposes
	private TaskObject editTask; // task to be edited
	private int editTaskIndex;
	private int editOccurrenceIndex = -1;
	private int editTaskId;
	private String originalTitle = "";
	private LocalDateTime originalStartDateTime = LocalDateTime.MAX;
	private LocalDate originalStartDate = LocalDate.MAX;
	private LocalTime originalStartTime = LocalTime.MAX;
	private LocalDateTime originalEndDateTime = LocalDateTime.MAX;
	private LocalDate originalEndDate = LocalDate.MAX;
	private LocalTime originalEndTime = LocalTime.MAX;
	private Interval originalInterval = new Interval();
	private String editTitle;
	private LocalDate editStartDate;
	private LocalTime editStartTime;
	private LocalDate editEndDate;
	private LocalTime editEndTime;
	private Interval editInterval;

	// For normal tasks
	boolean isEditTitle = false;
	boolean isEditStartDate = false;
	boolean isEditStartTime = false;
	boolean isEditEndDate = false;
	boolean isEditEndTime = false;
	boolean isEditInterval = false;
	// For recurring tasks
	boolean isEditAll = false;
	boolean isRecurringTask = false;
	boolean isEditStartDateForAllOccurrences = false;
	boolean isEditStartTimeForAllOccurrences = false;
	boolean isEditEndDateForAllOccurrences = false;
	boolean isEditEndTimeForAllOccurrences = false;
	// For editing of an occurrence
	boolean isEditSingleOccurrence = false;
	boolean isEditStartDateOccurrence = false;
	boolean isEditStartTimeOccurrence = false;
	boolean isEditEndDateOccurrence = false;
	boolean isEditEndTimeOccurrence = false;

	public Edit(CommandObject commandObj, ArrayList<TaskObject> lastOutputTaskList,
			ArrayList<TaskObject> taskList, int lastSearchedIndex) {
		this.commandObj = commandObj;
		this.lastOutputTaskList = lastOutputTaskList;
		this.taskList = taskList;
		this.lastSearchedIndex = lastSearchedIndex;
	}

	/**
	 * Main method of Edit. <br>
	 * Sets the information to be edited, finds the task to be edited and edits all the information before
	 * saving the edited task to the external file location.
	 * 
	 * @return output
	 */
	public ArrayList<String> run() {
		setEditInformation();
		setTaskToBeEdited();
		processEdit();
		updateCategory();
		saveExternal();
		updateGuiDisplay();

		setOutput();
		return output;
	}

	/**
	 * Sets all the edit information by retrieving the relevant information from the CommandObject and
	 * TaskObject.
	 */
	private void setEditInformation() {
		try {
			editTaskIndex = commandObj.getIndex();

			editTitle = commandObj.getTaskObject().getTitle();
			if (!editTitle.equals("")) {
				isEditTitle = true;
			}
			editStartDate = commandObj.getTaskObject().getStartDateTime().toLocalDate();
			if (!editStartDate.isEqual(LocalDate.MAX)) {
				isEditStartDate = true;
			}
			editStartTime = commandObj.getTaskObject().getStartDateTime().toLocalTime();
			if (!editStartTime.equals(LocalTime.MAX)) {
				isEditStartTime = true;
			}
			editEndDate = commandObj.getTaskObject().getEndDateTime().toLocalDate();
			if (!editEndDate.isEqual(LocalDate.MAX)) {
				isEditEndDate = true;
			}
			editEndTime = commandObj.getTaskObject().getEndDateTime().toLocalTime();
			if (!editEndTime.equals(LocalTime.MAX)) {
				isEditEndTime = true;
			}
			editInterval = commandObj.getTaskObject().getInterval();
			if (!editInterval.isNull()) {
				isEditInterval = true;
			}
			if (lastSearchedIndex != -1) { // it is an occurrence, and not a task, that is to be edited
				isEditSingleOccurrence = true;
				editTaskIndex = lastSearchedIndex;
				editOccurrenceIndex = commandObj.getIndex();
			}
			isEditAll = commandObj.getTaskObject().getIsEditAll(); // if all occurrences are to be edited

			checkEditInformation(); // for debugging
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "Error setting edit information");
		}
	}

	/**
	 * Finds the task to be edited based on the edit task index value, and sets the originalTask to be a
	 * replicate of this task for undo purposes.
	 */
	private void setTaskToBeEdited() {
		assert (editTaskIndex > 0 && editTaskIndex <= lastOutputTaskList.size());

		editTaskId = lastOutputTaskList.get(editTaskIndex - 1).getTaskId();
		LOGGER.log(Level.INFO, "Obtained task ID to be edited");

		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (taskList.get(i).getTaskId() == editTaskId) {
				editTask = task;
				originalTask.setTaskObject(task);
				isRecurringTask = task.getIsRecurring();
				compareOldAndNewCategory(task);
			}
		}
	}

	/**
	 * Compares the categories of the task to be edited and the edit data. If the edit data category is not
	 * empty, then this edit is an undo function. If it is an undo function, the date and times should be
	 * edited depending if the previous event (i.e. editTask) is a floating task or a deadline. This special
	 * check has to be implemented because by default, the boolean checks would be set to false if the edit
	 * value is MAX, which is the case if the previous event is a floating/deadline.
	 * 
	 * @param task
	 *            current task
	 */
	private void compareOldAndNewCategory(TaskObject task) {
		String currentTaskCategory = task.getCategory();
		String editTaskCategory = commandObj.getTaskObject().getCategory();

		// if this edit is an undo
		if (!editTaskCategory.equals("")) {
			if (currentTaskCategory.equals(CATEGORY_DEADLINE) && 
					editTaskCategory.equals(CATEGORY_FLOATING)) {
				isEditStartDate = true;
				isEditStartTime = true;
			} else if (currentTaskCategory.equals(CATEGORY_EVENT) && 
					editTaskCategory.equals(CATEGORY_FLOATING)) {
				isEditStartDate = true;
				isEditStartTime = true;
				isEditEndDate = true;
				isEditEndTime = true;
			} else if (currentTaskCategory.equals(CATEGORY_EVENT) &&
					editTaskCategory.equals(CATEGORY_DEADLINE)) {
				isEditEndDate = true;
				isEditEndTime = true;
			}
		}

	}

	/**
	 * Processes the edit depending if it is an edit of a single occurrence or of a task.
	 */
	private void processEdit() {
		if (isEditSingleOccurrence) {
			updateBooleanChecksForEditOccurrence(); // so that the correct output will be printed
			editRecurrenceTiming();
		} else {
			editTask();
		}
	}

	private void updateBooleanChecksForEditOccurrence() {
		if (isEditStartDate) {
			isEditStartDate = false;
			isEditStartDateOccurrence = true;
		}
		if (isEditStartTime) {
			isEditStartTime = false;
			isEditStartTimeOccurrence = true;
		}
		if (isEditEndDate) {
			isEditEndDate = false;
			isEditEndDateOccurrence = true;
		}
		if (isEditEndTime) {
			isEditEndTime = false;
			isEditEndTimeOccurrence = true;
		}
	}

	// Edits a recurrence timing
	private void editRecurrenceTiming() {
		TaskObject task = lastOutputTaskList.get(editTaskIndex - 1);
		originalTask.setTaskObject(task);

		LocalDateTimePair timing = new LocalDateTimePair();
		try {
			timing = task.getTaskDateTimes().get(editOccurrenceIndex - 1);

			originalStartDate = timing.getStartDateTime().toLocalDate();
			originalStartTime = timing.getStartDateTime().toLocalTime();
			originalEndDate = timing.getEndDateTime().toLocalDate();
			originalEndTime = timing.getEndDateTime().toLocalTime();

			if (isEditStartDateOccurrence && isEditStartTimeOccurrence) {
				timing.setStartDateTime(LocalDateTime.of(editStartDate, editStartTime));
			} else {
				if (isEditStartDateOccurrence) {
					timing.setStartDateTime(LocalDateTime.of(editStartDate, originalStartTime));
				}
				if (isEditStartTimeOccurrence) {
					timing.setStartDateTime(LocalDateTime.of(originalStartDate, editStartTime));
				}
			}
			if (isEditEndDateOccurrence && isEditEndTimeOccurrence) {
				timing.setEndDateTime(LocalDateTime.of(editEndDate, editEndTime));
			} else {
				if (isEditEndDateOccurrence) {
					timing.setEndDateTime(LocalDateTime.of(editEndDate, originalEndTime));
				}
				if (isEditEndTimeOccurrence) {
					timing.setEndDateTime(LocalDateTime.of(originalEndDate, editEndTime));
				}
			}

			task.updateStartAndEndDateTimes();
			editTask = task;
		} catch (IndexOutOfBoundsException e) {
			tempOutput.add(MESSAGE_NO_SUCH_OCCURRENCE_EXISTS);
			isEditStartDateOccurrence = false;
			isEditStartTimeOccurrence = false;
			isEditEndDateOccurrence = false;
			isEditEndTimeOccurrence = false;
		}
	}

	// Edits a task
	private void editTask() {
		if (isEditTitle) {
			editTitle(editTask);
		}
		if (isEditStartDate && isEditStartTime) {
			if (isRecurringTask && isEditAll) {
				editStartDateAndTimeForAllOccurrences(editTask);
			} else {
				editStartDateAndTime(editTask);

			}
		} else {
			if (isEditStartDate) {
				if (isRecurringTask && isEditAll) {
					editStartDateForAllOccurrences(editTask);
				} else {
					editStartDate(editTask);
				}
			}
			if (isEditStartTime) {
				if (isRecurringTask && isEditAll) {
					editStartTimeForAllOccurrences(editTask);
				} else {
					editStartTime(editTask);
				}
			}
		}
		if (isEditEndDate && isEditEndTime) {
			if (isRecurringTask && isEditAll) {
				editEndDateAndTimeForAllOccurrences(editTask);
			} else {
				editEndDateAndTime(editTask);
			}
		} else {
			if (isEditEndDate) {
				if (isRecurringTask && isEditAll) {
					editEndDateForAllOccurrences(editTask);
				} else {
					editEndDate(editTask);
				}
			}
			if (isEditEndTime) {
				if (isRecurringTask && isEditAll) {
					editEndTimeForAllOccurrences(editTask);
				} else {
					editEndTime(editTask);
				}
			}
		}
		if (isEditInterval) {
			editInterval(editTask);
		}

	}

	// Edits the title of the task
	private void editTitle(TaskObject task) {
		originalTitle = task.getTitle();

		if (!originalTitle.equals(editTitle)) {
			task.setTitle(editTitle.trim());
			LOGGER.log(Level.INFO, "Title edited");
		} else {
			isEditTitle = false;
		}
	}

	// Edits the start date and time for all recurring occurrences
	private void editStartDateAndTimeForAllOccurrences(TaskObject task) {
		originalStartDateTime = task.getStartDateTime();
		originalStartDate = originalStartDateTime.toLocalDate();
		originalStartTime = originalStartDateTime.toLocalTime();
		ArrayList<LocalDateTimePair> taskDateTimes = task.getTaskDateTimes();

		try {
			// edit the ArrayList
			for (int i = 0; i < taskDateTimes.size(); i++) {
				LocalDateTimePair taskDateTime = taskDateTimes.get(i);
				LocalDateTime taskNewStartDateTime = LocalDateTime.of(editStartDate, editStartTime);
				taskDateTime.setStartDateTime(taskNewStartDateTime);
			}

			// then edit the TaskObject
			task.setStartDateTime(LocalDateTime.of(editStartDate, editStartTime));

			LOGGER.log(Level.INFO, "Start dates and times edited for all occurrences of recurring task");
			isEditStartDateForAllOccurrences = true;
			isEditStartTimeForAllOccurrences = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// If recurring task, edits the TaskObject and the first occurrence in the arraylist.
	// If not, edit the TaskObject directly.
	private void editStartDateAndTime(TaskObject task) {
		LocalDateTimePair taskDateTimeFirst = new LocalDateTimePair();

		if (isRecurringTask) {
			taskDateTimeFirst = task.getTaskDateTimes().get(0); // gets the timings of the first occurrence
			originalStartDateTime = taskDateTimeFirst.getStartDateTime();
		} else {
			originalStartDateTime = task.getStartDateTime();
		}
		originalStartDate = originalStartDateTime.toLocalDate();
		originalStartTime = originalStartDateTime.toLocalTime();

		if (!originalStartDate.isEqual(editStartDate) && !originalStartTime.equals(editStartTime)) {
			task.setStartDateTime(LocalDateTime.of(editStartDate, editStartTime));

			if (isRecurringTask) {
				taskDateTimeFirst.setStartDateTime(LocalDateTime.of(editStartDate, editStartTime));
				LOGGER.log(Level.INFO, "Start date and time edited for first occurrence of recurring task");
			} else {
				LOGGER.log(Level.INFO, "Start date and time edited");
			}
		} else if (originalStartTime.equals(editStartTime)) { // if old time == new time, only edit date
			isEditStartTime = false;
			editStartDate(task);
		} else if (originalStartDate.isEqual(editStartDate)) { // if old date == new date, only edit time
			isEditStartDate = false;
			editStartTime(task);
		}

	}

	// Edits the start date for all recurring occurrences
	private void editStartDateForAllOccurrences(TaskObject task) {
		originalStartDateTime = task.getStartDateTime();
		originalStartDate = originalStartDateTime.toLocalDate();
		originalStartTime = originalStartDateTime.toLocalTime();
		ArrayList<LocalDateTimePair> taskDateTimes = task.getTaskDateTimes();

		try {
			// edit the ArrayList
			for (int i = 0; i < taskDateTimes.size(); i++) {
				LocalDateTimePair taskDateTime = taskDateTimes.get(i);
				LocalTime taskOriginalStartTime = taskDateTime.getStartDateTime().toLocalTime();

				// Sets the start time to be the new time
				LocalDateTime taskNewStartDateTime = LocalDateTime.of(editStartDate, taskOriginalStartTime);
				taskDateTime.setStartDateTime(taskNewStartDateTime);
			}

			// then edit the TaskObject
			task.setStartDateTime(LocalDateTime.of(editStartDate, originalStartTime));

			LOGGER.log(Level.INFO, "Start dates edited for all occurrences of recurring task");
			isEditStartDateForAllOccurrences = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// If recurring task, edits the TaskObject and the first occurrence in the arraylist.
	// If not, edit the TaskObject directly.
	private void editStartDate(TaskObject task) {
		LocalDateTimePair taskDateTimeFirst = new LocalDateTimePair();

		if (isRecurringTask) {
			taskDateTimeFirst = task.getTaskDateTimes().get(0); // gets the timings of the first occurrence
			originalStartDateTime = taskDateTimeFirst.getStartDateTime();
		} else {
			originalStartDateTime = task.getStartDateTime();
		}
		originalStartDate = originalStartDateTime.toLocalDate();
		originalStartTime = originalStartDateTime.toLocalTime();

		if (!originalStartDate.isEqual(editStartDate)) {
			task.setStartDateTime(LocalDateTime.of(editStartDate, originalStartTime));

			if (isRecurringTask) {
				taskDateTimeFirst.setStartDateTime(LocalDateTime.of(editStartDate, originalStartTime));
				LOGGER.log(Level.INFO, "Start date edited for first occurrence of recurring task");
			} else {
				LOGGER.log(Level.INFO, "Start date edited");
			}
		} else {
			isEditStartDate = false;
		}
	}

	// Edits the start time for all recurring occurrences
	private void editStartTimeForAllOccurrences(TaskObject task) {
		originalStartDateTime = task.getStartDateTime();
		originalStartDate = originalStartDateTime.toLocalDate();
		originalStartTime = originalStartDateTime.toLocalTime();
		ArrayList<LocalDateTimePair> taskDateTimes = task.getTaskDateTimes();

		try {
			// edit the ArrayList
			for (int i = 0; i < taskDateTimes.size(); i++) {
				LocalDateTimePair taskDateTime = taskDateTimes.get(i);
				LocalDate taskOriginalStartDate = taskDateTime.getStartDateTime().toLocalDate();

				// If the original start date is null, i.e. it is a floating task which is being edited to
				// another category, then the date will be default to today.
				if (taskOriginalStartDate.equals(LocalDate.MAX)) {
					taskOriginalStartDate = LocalDate.now();
				}

				// Sets the start time to be the new time
				LocalDateTime taskNewStartDateTime = LocalDateTime.of(taskOriginalStartDate, editStartTime);
				taskDateTime.setStartDateTime(taskNewStartDateTime);
			}

			// then edit the TaskObject
			task.setStartDateTime(LocalDateTime.of(originalStartDate, editStartTime));

			LOGGER.log(Level.INFO, "Start times edited for all occurrences of recurring task");
			isEditStartTimeForAllOccurrences = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// If recurring task, edits the TaskObject and the first occurrence in the arraylist.
	// If not, edit the TaskObject directly.
	private void editStartTime(TaskObject task) {
		LocalDateTimePair taskDateTimeFirst = new LocalDateTimePair();

		if (isRecurringTask) {
			taskDateTimeFirst = task.getTaskDateTimes().get(0); // gets the timings of the first occurrence
			originalStartDateTime = taskDateTimeFirst.getStartDateTime();
		} else {
			originalStartDateTime = task.getStartDateTime();
		}
		originalStartDate = originalStartDateTime.toLocalDate();
		originalStartTime = originalStartDateTime.toLocalTime();

		// If the original start date is null, i.e. it is a floating task which is being edited to another 
		// category, then the date will be default to today.
		if (originalStartDate.equals(LocalDate.MAX)) {
			originalStartDate = LocalDate.now();
		}

		if (!originalStartTime.equals(editStartTime)) {
			task.setStartDateTime(LocalDateTime.of(originalStartDate, editStartTime));

			if (isRecurringTask) {
				taskDateTimeFirst.setStartDateTime(LocalDateTime.of(originalStartDate, editStartTime));
				LOGGER.log(Level.INFO, "Start time edited for first occurrence of recurring task");
			} else {
				LOGGER.log(Level.INFO, "Start time edited");
			}
		} else {
			isEditStartTime = false;
		}
	}

	// Edits the end date and time for all recurring occurrences
	private void editEndDateAndTimeForAllOccurrences(TaskObject task) {
		originalEndDateTime = task.getEndDateTime();
		originalEndDate = originalEndDateTime.toLocalDate();
		originalEndTime = originalEndDateTime.toLocalTime();
		ArrayList<LocalDateTimePair> taskDateTimes = task.getTaskDateTimes();

		try {
			// edit the ArrayList
			for (int i = 0; i < taskDateTimes.size(); i++) {
				LocalDateTimePair taskDateTime = taskDateTimes.get(i);
				LocalDateTime taskNewEndDateTime = LocalDateTime.of(editEndDate, editEndTime);
				taskDateTime.setEndDateTime(taskNewEndDateTime);
			}

			// then edit the TaskObject
			task.setEndDateTime(LocalDateTime.of(editEndDate, editEndTime));

			LOGGER.log(Level.INFO, "End dates and times edited for all occurrences of recurring task");
			isEditEndDateForAllOccurrences = true;
			isEditEndTimeForAllOccurrences = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// If recurring task, edits the TaskObject and the first occurrence in the arraylist.
	// If not, edit the TaskObject directly.
	private void editEndDateAndTime(TaskObject task) {
		LocalDateTimePair taskDateTimeFirst = new LocalDateTimePair();

		if (isRecurringTask) {
			taskDateTimeFirst = task.getTaskDateTimes().get(0); // gets the timings of the first occurrence
			originalEndDateTime = taskDateTimeFirst.getEndDateTime();
		} else {
			originalEndDateTime = task.getEndDateTime();
		}
		originalEndDate = originalEndDateTime.toLocalDate();
		originalEndTime = originalEndDateTime.toLocalTime();

		if (!originalEndDate.isEqual(editEndDate) && !originalEndTime.equals(editEndTime)) {
			task.setEndDateTime(LocalDateTime.of(editEndDate, editEndTime));

			if (isRecurringTask) {
				taskDateTimeFirst.setEndDateTime(LocalDateTime.of(editEndDate, editEndTime));
				LOGGER.log(Level.INFO, "End date and time edited for first occurrence of recurring task");
			} else {
				LOGGER.log(Level.INFO, "End date and time edited");
			}
		} else if (originalEndTime.equals(editEndTime)) { // if old time == new time, only edit date
			isEditEndTime = false;
			editEndDate(task);
		} else if (originalEndDate.isEqual(editEndDate)) { // if old date == new date, only edit time
			isEditEndDate = false;
			editEndTime(task);
		}
	}

	// Edits the end date for all recurring occurrences
	private void editEndDateForAllOccurrences(TaskObject task) {
		originalEndDateTime = task.getEndDateTime();
		originalEndDate = originalEndDateTime.toLocalDate();
		originalEndTime = originalEndDateTime.toLocalTime();
		ArrayList<LocalDateTimePair> taskDateTimes = task.getTaskDateTimes();

		try {
			// edit the ArrayList
			for (int i = 0; i < taskDateTimes.size(); i++) {
				LocalDateTimePair taskDateTime = taskDateTimes.get(i);
				LocalTime taskOriginalEndTime = taskDateTime.getEndDateTime().toLocalTime();

				// Sets the start time to be the new time
				LocalDateTime taskNewEndDateTime = LocalDateTime.of(editEndDate, taskOriginalEndTime);
				taskDateTime.setEndDateTime(taskNewEndDateTime);
			}

			// then edit the TaskObject
			task.setEndDateTime(LocalDateTime.of(editEndDate, originalEndTime));

			LOGGER.log(Level.INFO, "End dates edited for all occurrences of recurring task");
			isEditEndDateForAllOccurrences = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// If recurring task, edits the TaskObject and the first occurrence in the arraylist.
	// If not, edit the TaskObject directly.
	private void editEndDate(TaskObject task) {
		LocalDateTimePair taskDateTimeFirst = new LocalDateTimePair();

		if (isRecurringTask) {
			taskDateTimeFirst = task.getTaskDateTimes().get(0); // gets the timings of the first occurrence.
			originalEndDateTime = taskDateTimeFirst.getEndDateTime();
		} else {
			originalEndDateTime = task.getEndDateTime();
		}
		originalEndDate = originalEndDateTime.toLocalDate();
		originalEndTime = originalEndDateTime.toLocalTime();

		if (!originalEndDate.isEqual(editEndDate)) {
			task.setEndDateTime(LocalDateTime.of(editEndDate, originalEndTime));

			if (isRecurringTask) {
				taskDateTimeFirst.setEndDateTime(LocalDateTime.of(editEndDate, originalEndTime));
				LOGGER.log(Level.INFO, "End date edited for first occurrence of recurring task");
			} else {
				LOGGER.log(Level.INFO, "End date edited");
			}
		} else {
			isEditEndDate = false;
		}
	}

	// Edits the end time for all recurring occurrences
	private void editEndTimeForAllOccurrences(TaskObject task) {
		originalEndDateTime = task.getEndDateTime();
		originalEndDate = originalEndDateTime.toLocalDate();
		originalEndTime = originalEndDateTime.toLocalTime();
		ArrayList<LocalDateTimePair> taskDateTimes = task.getTaskDateTimes();

		try {
			// edit the ArrayList
			for (int i = 0; i < taskDateTimes.size(); i++) {
				LocalDateTimePair taskDateTime = taskDateTimes.get(i);
				LocalDate taskOriginalEndDate = taskDateTime.getEndDateTime().toLocalDate();

				// If the original end date is null, i.e. it is a floating task which is being edited to
				// another category, then the date will be default to today.
				if (taskOriginalEndDate.equals(LocalDate.MAX)) {
					taskOriginalEndDate = LocalDate.now();
				}

				// Sets the end time to be the new time
				LocalDateTime taskNewEndDateTime = LocalDateTime.of(taskOriginalEndDate, editEndTime);
				taskDateTime.setEndDateTime(taskNewEndDateTime);
			}

			// then edit the TaskObject
			task.setEndDateTime(LocalDateTime.of(originalEndDate, editEndTime));

			LOGGER.log(Level.INFO, "End times edited for all occurrences of recurring task");
			isEditEndTimeForAllOccurrences = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// If recurring task, edits the TaskObject and the first occurrence in the arraylist.
	// If not, edit the TaskObject directly.
	private void editEndTime(TaskObject task) {
		LocalDateTimePair taskDateTimeFirst = new LocalDateTimePair();

		if (isRecurringTask) {
			taskDateTimeFirst = task.getTaskDateTimes().get(0); // gets the timings of the first occurrence
			originalEndDateTime = taskDateTimeFirst.getEndDateTime();
		} else {
			originalEndDateTime = task.getEndDateTime();
		}
		originalEndDate = originalEndDateTime.toLocalDate();
		originalEndTime = originalEndDateTime.toLocalTime();

		// If the original end date is null, i.e. it is a floating task which is being edited to another
		// category, then the date will be default to today.
		if (originalEndDate.equals(LocalDate.MAX)) {
			originalEndDate = LocalDate.now();
		}

		if (!originalEndTime.equals(editEndTime)) {
			task.setEndDateTime(LocalDateTime.of(originalEndDate, editEndTime));

			if (isRecurringTask) {
				taskDateTimeFirst.setEndDateTime(LocalDateTime.of(originalEndDate, editEndTime));
				LOGGER.log(Level.INFO, "End time edited for first occurrence of recurring task");
			} else {
				LOGGER.log(Level.INFO, "End time edited");
			}
		} else {
			isEditEndTime = false;
		}
	}

	private void editInterval(TaskObject task) {
		try {
			originalInterval = task.getInterval();
			
			if (!originalInterval.equals(editInterval)) {
				task.setInterval(editInterval);
				// To update the list of recurrence timings
				Recurring.setAllRecurringEventTimes(task);
				LOGGER.log(Level.INFO, "Interval edited");
			} else {
				isEditInterval = false;
			}
		} catch (RecurrenceException e) {
			String exceptionMessage = e.getRecurrenceExceptionMessage();
			output.add(exceptionMessage);
		}
	}

	// Updates the category of the task in case it has been modified
	private void updateCategory() {
		LocalDateTime newStartDateTime = editTask.getStartDateTime();
		LocalDateTime newEndDateTime = editTask.getEndDateTime();

		if (newStartDateTime.equals(LocalDateTime.MAX) && newEndDateTime.equals(LocalDateTime.MAX)) {
			editTask.setCategory(CATEGORY_FLOATING);
		} else {
			if (newEndDateTime.equals(LocalDateTime.MAX)) {
				editTask.setCategory(CATEGORY_DEADLINE);
			} else {
				editTask.setCategory(CATEGORY_EVENT);
			}
		}
	}

	// Saves the updated file to Storage
	private void saveExternal() {
		try {
			FileStorage storage = FileStorage.getInstance();
			storage.save(taskList);
			LOGGER.log(Level.INFO, "Storage file updated");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void updateGuiDisplay() {
		TimeOutput.setTaskTimeOutput(editTask);
	}

	// FOR DEBUGGING
	private void checkEditInformation() {

		System.out.println("isEditTitle = " + isEditTitle);
		System.out.println("isEditStartDate = " + isEditStartDate);
		System.out.println("isEditStartTime = " + isEditStartTime);
		System.out.println("isEditStartTimeForAllOccurrences = " + isEditStartTimeForAllOccurrences);
		System.out.println("isEditEndDate = " + isEditEndDate);
		System.out.println("isEditEndTime = " + isEditEndTime);
		System.out.println("isEditEndTimeForAllOccurrences = " + isEditEndTimeForAllOccurrences);
		System.out.println("isEditInterval = " + isEditInterval);
		System.out.println("isRecurringTask = " + isRecurringTask);
		System.out.println("isEditAll = " + isEditAll);
	}

	// ------------------------- OUTPUT MESSAGES -------------------------

	/*
	 * The Deadline check is because output for deadlines is slightly different, as they should not have
	 * start/end dates/times. If the original date/time is equal to MAX value, then there was no previous
	 * date/time so the output should be 'added' instead of 'edited'.
	 */
	private void setOutput() {
		// checkEditInformation();

		if (!isEditSingleOccurrence) {
			if (isEditTitle) {
				outputTitleEditedMessage();
			}
			if (isEditStartDate) {
				if (isEditStartDateForAllOccurrences) {
					if (editTask.getCategory().equals(CATEGORY_DEADLINE)) {
						outputDateEditedForAllOccurrencesMessage();
					} else {
						outputStartDateEditedForAllOccurrencesMessage();
					}
				} else {
					if (editTask.getCategory().equals(CATEGORY_DEADLINE)) {
						if (originalStartDate.equals(LocalDate.MAX)) {
							outputDateAddedMessage();
						} else {
							outputDateEditedMessage();
						}
					} else {
						if (originalStartDate.equals(LocalDate.MAX)) {
							outputStartDateAddedMessage();
						} else {
							outputStartDateEditedMessage();
						}
					}
				}
			}
			if (isEditStartTime) {
				if (isEditStartTimeForAllOccurrences) {
					if (editTask.getCategory().equals(CATEGORY_DEADLINE)) {
						outputTimeEditedForAllOccurrencesMessage();
					} else {
						outputStartTimeEditedForAllOccurrencesMessage();
					}
				} else {
					if (editTask.getCategory().equals(CATEGORY_DEADLINE)) {
						if (originalStartTime.equals(LocalTime.MAX)) {
							outputTimeAddedMessage();
						} else {
							outputTimeEditedMessage();
						}
					} else {
						if (originalStartTime.equals(LocalTime.MAX)) {
							outputStartTimeAddedMessage();
						} else {
							outputStartTimeEditedMessage();
						}
					}
				}
			}
			if (isEditEndDate) {
				if (isEditEndDateForAllOccurrences) {
					outputEndDateEditedForAllOccurencesMessage();
				} else {
					if (originalEndDate.equals(LocalDate.MAX)) {
						outputEndDateAddedMessage();
					} else {
						outputEndDateEditedMessage();
					}
				}
			}
			if (isEditEndTime) {
				if (isEditEndTimeForAllOccurrences) {
					outputEndTimeEditedForAllOccurrencesMessage();
				} else {
					if (originalEndTime.equals(LocalTime.MAX)) {
						outputEndTimeAddedMessage();
					} else {
						outputEndTimeEditedMessage();
					}
				}
			}
			if (isEditInterval) {
				outputIntervalEditedMessage();
			}
		} else { // Output for editing of a single occurrence
			if (isEditStartDateOccurrence) {
				outputStartDateOccurrenceEditedMessage();
			}
			if (isEditStartTimeOccurrence) {
				outputStartTimeOccurrenceEditedMessage();
			}
			if (isEditEndDateOccurrence) {
				outputEndDateOccurrenceEditedMessage();
			}
			if (isEditEndTimeOccurrence) {
				outputEndTimeOccurrenceEditedMessage();
			}
		}

		concatenateOutput();
	}

	private void outputTitleEditedMessage() {
		tempOutput.add(String.format(MESSAGE_TITLE_EDIT, originalTitle, editTitle));
	}

	private void outputDateAddedMessage() {
		tempOutput.add(String.format(MESSAGE_DATE_ADD, editStartDate, editTask.getTitle()));
	}

	private void outputDateEditedMessage() {
		tempOutput.add(String.format(MESSAGE_DATE_EDIT, originalStartDate, editStartDate));
	}

	private void outputDateEditedForAllOccurrencesMessage() {
		tempOutput.add(String.format(MESSAGE_DATE_FOR_ALL_OCCURRENCES_EDIT, editStartDate));
	}

	private void outputStartDateAddedMessage() {
		tempOutput.add(String.format(MESSAGE_START_DATE_ADD, editStartDate, editTask.getTitle()));
	}

	private void outputStartDateEditedMessage() {
		tempOutput.add(String.format(MESSAGE_START_DATE_EDIT, originalStartDate, editStartDate));
	}

	private void outputStartDateEditedForAllOccurrencesMessage() {
		tempOutput.add(String.format(MESSAGE_START_DATE_FOR_ALL_OCCURRENCES_EDIT, editStartDate));
	}

	private void outputTimeAddedMessage() {
		tempOutput.add(String.format(MESSAGE_TIME_ADD, editStartTime, editTask.getTitle()));
	}

	private void outputTimeEditedMessage() {
		tempOutput.add(String.format(MESSAGE_TIME_EDIT, originalStartTime, editStartTime));
	}

	private void outputTimeEditedForAllOccurrencesMessage() {
		tempOutput.add(String.format(MESSAGE_TIME_FOR_ALL_OCCURRENCES_EDIT, editStartTime));
	}

	private void outputStartTimeAddedMessage() {
		tempOutput.add(String.format(MESSAGE_START_TIME_ADD, editStartTime, editTask.getTitle()));
	}

	private void outputStartTimeEditedMessage() {
		tempOutput.add(String.format(MESSAGE_START_TIME_EDIT, originalStartTime, editStartTime));
	}

	private void outputStartTimeEditedForAllOccurrencesMessage() {
		tempOutput.add(String.format(MESSAGE_START_TIME_FOR_ALL_OCCURRENCES_EDIT, editStartTime));
	}

	private void outputEndDateAddedMessage() {
		tempOutput.add(String.format(MESSAGE_END_DATE_ADD, editEndDate, editTask.getTitle()));
	}

	private void outputEndDateEditedMessage() {
		tempOutput.add(String.format(MESSAGE_END_DATE_EDIT, originalEndDate, editEndDate));
	}

	private void outputEndDateEditedForAllOccurencesMessage() {
		tempOutput.add(String.format(MESSAGE_END_DATE_FOR_ALL_OCCURRENCES_EDIT, editEndDate));
	}

	private void outputEndTimeAddedMessage() {
		tempOutput.add(String.format(MESSAGE_END_TIME_ADD, editEndTime, editTask.getTitle()));
	}

	private void outputEndTimeEditedMessage() {
		tempOutput.add(String.format(MESSAGE_END_TIME_EDIT, originalEndTime, editEndTime));
	}

	private void outputEndTimeEditedForAllOccurrencesMessage() {
		tempOutput.add(String.format(MESSAGE_END_TIME_FOR_ALL_OCCURRENCES_EDIT, editEndTime));
	}

	private void outputIntervalEditedMessage() {
		tempOutput.add(MESSAGE_INTERVAL_EDIT);
	}

	private void outputStartDateOccurrenceEditedMessage() {
		tempOutput.add(String.format(MESSAGE_START_DATE_FOR_OCCURRENCE_EDITED, editTaskIndex,
				originalStartDate, editStartDate));
	}

	private void outputStartTimeOccurrenceEditedMessage() {
		tempOutput.add(String.format(MESSAGE_START_TIME_FOR_OCCURRENCE_EDITED, editTaskIndex,
				originalStartTime, editStartTime));
	}

	private void outputEndDateOccurrenceEditedMessage() {
		tempOutput.add(String.format(MESSAGE_END_DATE_FOR_OCCURRENCE_EDITED, editTaskIndex, originalEndDate,
				editEndDate));
	}

	private void outputEndTimeOccurrenceEditedMessage() {
		tempOutput.add(String.format(MESSAGE_END_TIME_FOR_OCCURRENCE_EDITED, editTaskIndex, originalEndTime,
				editEndTime));
	}

	// Combines all the output strings into 1 string
	private void concatenateOutput() {
		if (tempOutput.isEmpty()) {
			output.add(MESSAGE_NO_EDIT);
		} else {
			String concatOutput = "";
			for (int i = 0; i < tempOutput.size(); i++) {
				concatOutput = concatOutput.concat(tempOutput.get(i));
			}

			output.add(concatOutput.trim());
		}
	}

	// ------------------------- GETTERS -------------------------

	public TaskObject getOriginalTask() {
		return originalTask;
	}

	public TaskObject getEditTask() {
		return editTask;
	}

	public int getEditTaskIndex() {
		return editTaskIndex;
	}

	public int getEditOccurrenceIndex() {
		return editOccurrenceIndex;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public LocalDateTime getOriginalStartDateTime() {
		return originalStartDateTime;
	}

	public LocalDateTime getOriginalEndDateTime() {
		return originalEndDateTime;
	}

	public Interval getOriginalInterval() {
		return originalInterval;
	}

	public boolean getIsEditTitle() {
		return isEditTitle;
	}

	public boolean getIsEditStartDateAndTime() {
		return isEditStartDate || isEditStartTime;
	}

	public boolean getIsEditStartDate() {
		return isEditStartDate;
	}

	public boolean getIsEditStartTime() {
		return isEditStartTime;
	}

	public boolean getIsEditEndDateAndTime() {
		return isEditEndDate || isEditEndTime;
	}

	public boolean getIsEditEndDate() {
		return isEditEndDate;
	}

	public boolean getIsEditEndTime() {
		return isEditEndTime;
	}

	public boolean getIsEditAll() {
		return isEditAll;
	}

	public boolean getIsEditSingleOccurrence() {
		return isEditSingleOccurrence;
	}
}
