package common;

import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.util.ArrayList;

public class TaskObject implements Comparable<TaskObject> {

	protected String title = "";
	protected LocalDateTime startDateTime = LocalDateTime.MAX; //newly added
	protected LocalDateTime endDateTime = LocalDateTime.MAX; //newly added
	protected String category = ""; // deadline, event, or floating
	protected String status = ""; //completed, overdue or incomplete
	protected int taskId;
	protected String timeOutputString = ""; // stores date time in the desired output for GUI
	protected boolean isRecurring;
	protected Interval interval = new Interval();
	protected ArrayList<LocalDateTimePair> taskDateTimes = new ArrayList<LocalDateTimePair> (); // stores all dates and times related to a task
	
	// Checks to facilitate undo processes
	protected boolean isEditAll = false;
	protected boolean isContainingOnlyTaskDateTimes = false;
	
	// Not used in Logic anymore
	protected int startDate;
	protected int endDate;
	protected int startTime;
	protected int endTime;
	
	// Constructor for recurring event tasks
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String status,
			int taskId, boolean isRecurring, ArrayList<LocalDateTimePair> taskDateTimes) {
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
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String status, int taskId) {
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
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, Interval interval) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.interval = interval;
	}

	// Constructor for dummyTask in searchByIndex
	public TaskObject (LocalDateTime startDateTime, LocalDateTime endDateTime, Interval interval) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.interval = interval;
	}
	
	// Constructor for AddTest
	public TaskObject (String title, int taskId) {
		this.title = title;
		this.taskId = taskId;
	}
	
	// Constructor for deleting all for recurring tasks
	public TaskObject(String title){
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
	public TaskObject(ArrayList<LocalDateTimePair> taskDateTimes) {
		this.taskDateTimes = taskDateTimes;
		this.isContainingOnlyTaskDateTimes = true;
	}
	
	// Constructor for undoing an addition of a recurring task
	public TaskObject(boolean bool) {
		this.isEditAll = bool;
	}
	
	// empty constructor
	public TaskObject() {
		
	}
	
	/*
	// Constructor for event tasks
	public TaskObject(String title, int startDate, int endDate, int startTime, int endTime, String category, String status, int taskId) {
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}
	
	// Constructor for deadline tasks
	public TaskObject(String title, int endDate, int endTime, String category, String status, int taskId){
		this.title = title;
		this.startDate = endDate;
		this.endDate = endDate;
		this.startTime = endTime;
		this.endTime = endTime;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}
	
	// Constructor for floating tasks
	public TaskObject(String title, String category, String status, int taskId) {
		this.title = title;
		this.startDate = -1;
		this.endDate = -1;
		this.startTime = -1;
		this.endTime = -1;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}*/
	

	public String getTitle() {
		if (title == null)
			return "";
		return title;
	}
	
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}
	
	public int getStartDate() {
		return startDate;
	}
	
	public int getEndDate() {
		return endDate;
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public String getCategory() {
		if (category == null) 
			return "null";
		return category;
	}
	
	public String getStatus() {
		if (status == null)
			return "null";
		return status;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public String getTimeOutputString() {
		return timeOutputString;
	}
	
	public boolean getIsContainingOnlyTaskDateTimes() {
		return isContainingOnlyTaskDateTimes;
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
	
	
	public void setStartDate(int newStartDate) {
		this.startDate = newStartDate;
	}
	
	public void setEndDate(int newEndDate) {
		this.endDate = newEndDate;
	}
	
	public void setStartTime(int newStartTime) {
		this.startTime = newStartTime;
	}
	
	public void setEndTime(int newEndTime) {
		this.endTime = newEndTime;
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
	
	public Interval getInterval() {
		return interval;
	}
	
	public void setInterval(Interval interval) {
		this.interval = interval;
	}
	
	public boolean getIsRecurring() {
		return isRecurring;
	}
	
	public void setIsRecurring(boolean isRecurring) {
		this.isRecurring = isRecurring;
	}
	
	public boolean getIsEditAll() {
		return isEditAll;
	}
	
	public void setIsEditAll(boolean bool) {
		isEditAll = bool;
	}
	public void addToTaskDateTimes(LocalDateTimePair pair) {
		this.taskDateTimes.add(pair);
	}
	
	public void removeFromTaskDateTimes(int index) {
		this.taskDateTimes.remove(index);
	}
	
	public void removeAllDateTimes() {
		this.taskDateTimes.clear();
	}
	
	public ArrayList<LocalDateTimePair> getTaskDateTimes() {
		return taskDateTimes;
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
	
	// Checks if title, dates and times are invalid values
	public boolean isSearchKeywordPresent() {
		if (title.equals("") && startDate == -1 && endDate == -1 && startTime == -1 && endTime == -1)
			return false;
		return true;
	}
	
	//for testing purpose
	public void resetAttributes() {
		setTitle(null);
		setStartDate(-1);
		setEndDate(-1);
		setStartTime(-1);
		setEndTime(-1);
		setStartDateTime(LocalDateTime.MAX);
		setEndDateTime(LocalDateTime.MAX);
	}

    @Override
    public int compareTo(TaskObject task) {
        // TODO Auto-generated method stub
        return 0;
    }

}



/*
// Constructor for event tasks
public TaskObject(String title, int startDate, int endDate, int startTime, int endTime, String category, String status, int taskId) {
	this.title = title;
	this.startDate = startDate;
	this.endDate = endDate;
	this.startTime = startTime;
	this.endTime = endTime;
	this.category = category;
	this.status = status;
	this.taskId = taskId;
}

// Constructor for deadline tasks
public TaskObject(String title, int endDate, int endTime, String category, String status, int taskId){
	this.title = title;
	this.startDate = endDate;
	this.endDate = endDate;
	this.startTime = endTime;
	this.endTime = endTime;
	this.category = category;
	this.status = status;
	this.taskId = taskId;
}

// Constructor for floating tasks
public TaskObject(String title, String category, String status, int taskId) {
	this.title = title;
	this.startDate = -1;
	this.endDate = -1;
	this.startTime = -1;
	this.endTime = -1;
	this.category = category;
	this.status = status;
	this.taskId = taskId;
}

// Constructor for search keyword, delete, save and for edit-title functions
public TaskObject(String title){
	this.title = title;
	this.startDate = -1;
	this.endDate = -1;
	this.startTime = -1;
	this.endTime = -1;
	this.category = "";
	this.status = "";
	this.taskId = -1;
}*/


