//@@author A0124636H

package common;

import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.util.ArrayList;

/**
 * The TaskObject class contains all pertinent information regarding a task. The main attributes are: <br>
 * 1. Title <br>
 * 2. Start date/time <br>
 * 3. End date/time <br>
 * 4. Category <br>
 * 5. Status <br>
 * 6. Task ID <br>
 * 7. Interval object (for recurring tasks) <br>
 * 8. ArrayList of multiple dates and times (for recurring tasks) <br>
 * 
 * @author RuiBin
 *
 */

public class TaskObject implements Comparable<TaskObject> {

	protected String title = "";
	protected LocalDateTime startDateTime = LocalDateTime.MAX;
	protected LocalDateTime endDateTime = LocalDateTime.MAX;
	protected String category = ""; // deadline, event, or floating
	protected String status = ""; // completed, overdue or incomplete
	protected int taskId = -1;
	protected String timeOutputString = ""; // stores date time in the desired output for GUI
	protected Interval interval = new Interval();
	// stores all occurrences of a task
	protected ArrayList<LocalDateTimePair> taskDateTimes = new ArrayList<LocalDateTimePair>();
	protected boolean isRecurring;
	// stores all occurrences that has been deleted (if any)
	protected ArrayList<LocalDateTimePair> deletedTaskDateTimes = new ArrayList<LocalDateTimePair>();

	// Checks to facilitate undo processes
	protected boolean isEditAll = false;
	protected boolean isContainingOnlyTaskDateTimes = false;

	// ------------------------------------ CONSTRUCTORS ------------------------------------

	// Constructor for recurring event tasks
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String category,
			String status, int taskId, boolean isRecurring, ArrayList<LocalDateTimePair> taskDateTimes) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
		this.isRecurring = isRecurring;
		this.taskDateTimes = taskDateTimes;
	}

	// Constructor for event tasks
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String category,
			String status, int taskId) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}

	// Constructor for recurring deadline tasks
	public TaskObject(String title, LocalDateTime startDateTime, String category, String status, int taskId,
			boolean isRecurring, ArrayList<LocalDateTimePair> taskDateTimes) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = LocalDateTime.MAX;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
		this.isRecurring = isRecurring;
		this.taskDateTimes = taskDateTimes;
	}

	// Constructor for deadline tasks
	public TaskObject(String title, LocalDateTime startDateTime, String category, String status, int taskId) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = LocalDateTime.MAX;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}

	// Constructor for floating tasks
	public TaskObject(String title, String category, String status, int taskId) {
		this.title = title;
		this.startDateTime = LocalDateTime.MAX;
		this.endDateTime = LocalDateTime.MAX;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}

	// Constructor for processing undo of edit tasks
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime,
			Interval interval) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.interval = interval;
	}

	// Constructor for dummyTask in searchByIndex
	public TaskObject(LocalDateTime startDateTime, LocalDateTime endDateTime, Interval interval) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.interval = interval;
	}

	// Constructor for AddTest
	public TaskObject(String title, int taskId) {
		this.title = title;
		this.taskId = taskId;
	}

	// Constructor for deleting all for recurring tasks
	public TaskObject(String title) {
		this.title = title;
	}

	// Constructor for SearchTest - search-by-date for deadline
	public TaskObject(LocalDateTime dateTime) {
		this.startDateTime = dateTime;
	}

	// Constructor for SearchTest - search-by-time for event
	public TaskObject(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}

	// Constructor for SearchTest - search-by-category
	public TaskObject(String category, String random) {
		this.category = category;
	}

	// Constructor for undoing the deletion of one occurrence of a recurring task
	public TaskObject(LocalDateTimePair dateTimePair) {
		this.taskDateTimes.add(dateTimePair);
		this.isContainingOnlyTaskDateTimes = true;
	}

	// Constructor for undoing an addition of a recurring task
	public TaskObject(boolean bool) {
		this.isEditAll = bool;
	}

	// Empty constructor
	public TaskObject() {

	}

	// ------------------------------------ USEFUL METHODS ------------------------------------

	public void addToTaskDateTimes(LocalDateTimePair pair) {
		this.taskDateTimes.add(pair);
	}
	
	public void addToTaskDateTimes(int index, LocalDateTimePair pair) {
		this.taskDateTimes.add(index, pair);
	}

	public void removeFromTaskDateTimes(int index) {
		this.taskDateTimes.remove(index);
	}

	public void removeAllDateTimes() {
		this.taskDateTimes.clear();
	}

	public void updateStartAndEndDateTimes() {
		this.startDateTime = taskDateTimes.get(0).getStartDateTime();
		this.endDateTime = taskDateTimes.get(0).getEndDateTime();
	}

	public void addToDeletedTaskDateTimes(LocalDateTimePair deletedOccurrence) {
		this.deletedTaskDateTimes.add(deletedOccurrence);
	}

	// Checks if title, dates and times are invalid values
	public boolean isSearchKeywordPresent() {
		return (!this.title.equals("") || !this.startDateTime.equals(LocalDateTime.MAX)
				|| !this.category.equals("") || !this.status.equals(""));
	}
	
	public boolean isInfiniteRecurrence() {
		return (this.interval.getCount() == -1 && this.interval.getUntil().equals(LocalDateTime.MAX));
	}

	public boolean isNull() {
		return (title.equals("") && startDateTime.equals(LocalDateTime.MAX)
				&& endDateTime.equals(LocalDateTime.MAX) && category.equals("") && status.equals("")
				&& taskId == -1 && timeOutputString.equals(""));
	}
	
	// for testing purpose
	public void resetAttributes() {
		setTitle("");
		setStartDateTime(LocalDateTime.MAX);
		setEndDateTime(LocalDateTime.MAX);
	}

	@Override
	public int compareTo(TaskObject task) {
		// TODO Auto-generated method stub
		return 0;
	}

	// ------------------------------------ GETTERS/SETTERS ------------------------------------

	public String getTitle() {
		return title;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public String getCategory() {
		return category;
	}

	public String getStatus() {
		return status;
	}

	public int getTaskId() {
		return taskId;
	}

	public String getTimeOutputString() {
		return timeOutputString;
	}

	public Interval getInterval() {
		return interval;
	}

	public boolean getIsRecurring() {
		return isRecurring;
	}

	public boolean getIsContainingOnlyTaskDateTimes() {
		return isContainingOnlyTaskDateTimes;
	}

	public boolean getIsEditAll() {
		return isEditAll;
	}

	public ArrayList<LocalDateTimePair> getTaskDateTimes() {
		return taskDateTimes;
	}

	public ArrayList<LocalDateTimePair> getDeletedTaskDateTimes() {
		return deletedTaskDateTimes;
	}

	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	public void setStartDateTime(LocalDateTime newStartDateTime) throws DateTimeException {
		this.startDateTime = newStartDateTime;
	}

	public void setEndDateTime(LocalDateTime newEndDateTime) throws DateTimeException {
		this.endDateTime = newEndDateTime;
	}

	public void setCategory(String newCategory) {
		this.category = newCategory;
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

	public void setTaskId(int newTaskId) {
		this.taskId = newTaskId;
	}

	public void setTimeOutputString(String newTimeOutput) {
		this.timeOutputString = newTimeOutput;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public void setIsRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}

	public void setIsEditAll(boolean isEditAll) {
		this.isEditAll = isEditAll;
	}

	public void setTaskDateTimes(ArrayList<LocalDateTimePair> newTaskDateTimes) {
		this.taskDateTimes = newTaskDateTimes;
	}

	// Essentially creates a copy of the task object that is passed into this method
	public void setTaskObject(TaskObject task) {
		this.title = task.getTitle();
		this.startDateTime = task.getStartDateTime();
		this.endDateTime = task.getEndDateTime();
		this.category = task.getCategory();
		this.status = task.getStatus();
		this.taskId = task.getTaskId();
		this.timeOutputString = task.getTimeOutputString();
		this.isRecurring = task.getIsRecurring();
		this.interval = task.getInterval();
		this.taskDateTimes = task.getTaskDateTimes();
		this.isEditAll = task.getIsEditAll();
		this.isContainingOnlyTaskDateTimes = task.getIsContainingOnlyTaskDateTimes();
		
	}

}