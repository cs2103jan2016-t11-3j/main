package common;

import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.util.Optional;

public class TaskObject implements Comparable<TaskObject> {

	protected String title = "";
	protected LocalDateTime startDateTime; //newly added
	protected LocalDateTime endDateTime; //newly added
	protected String category; // deadline, event, or floating
	protected String status; //completed, overdue or incomplete
	protected int taskId;
	protected String timeOutputString = ""; // stores date time in the desired output for GUI
	
	// Constructor for event tasks
	public TaskObject(String title, LocalDateTime startDateTime, LocalDateTime endDateTime, String category, String status, int taskId) {
		this.title = title;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}
	
	// Constructor for deadline tasks
	public TaskObject(String title, LocalDateTime startDateTime, String category, String status, int taskId){
		this.title = title;
		this.startDateTime = startDateTime;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}
	
	// Constructor for floating tasks
	public TaskObject(String title, String category, String status, int taskId) {
		this.title = title;
		this.category = category;
		this.status = status;
		this.taskId = taskId;
	}
	
	// Constructor for search keyword, delete, save and for edit-title functions
	public TaskObject(String title){
		this.title = title;
		this.category = null;
		this.status = null;
		this.taskId = -1;
	}
	
	// Constructor for edit-date functions
	public TaskObject(LocalDateTime date) {
		this.title = "";
		this.startDateTime = date;
		this.category = null;
		this.status = null;
		this.taskId = -1;
	}
	
    public TaskObject(String title, int taskId){
        this.title = title;
        this.category = "";
        this.status = "";
        this.taskId = taskId;
    }
	
	// empty constructor
	public TaskObject() {
		
	}
	
	public String getTitle() {
		if (title == null)
			return "null";
		return title;
	}
	
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	
	public LocalDateTime getEndDateTime() {
		return endDateTime;
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
	
	// Checks if title, dates and times are invalid values
	public boolean isSearchKeywordPresent() {
		Optional<LocalDateTime> dateTime = Optional.ofNullable(startDateTime);
		if (title.equals("") && !dateTime.isPresent()) {
			return false;
		}
		return true;
	}
	
	//for testing purpose
	public void resetAttributes() {
		setTitle(null);
		setStartDateTime(null);
		setEndDateTime(null);
	}

    @Override
    public int compareTo(TaskObject task) {
        // TODO Auto-generated method stub
        return 0;
    }

}
