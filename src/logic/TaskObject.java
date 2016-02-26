package logic;

public class TaskObject {

	private String title;
	private int startDate;
	private int endDate;
	private int startTime;
	private int endTime;
	private String category; // deadline, event, or floating
	private String status;
	private int taskId;
	
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
	
	// Constructor for search keyword, delete, save and for edit functions
	public TaskObject(String title){
		this.title = title;
		this.startDate = -1;
		this.endDate = -1;
		this.startTime = -1;
		this.endTime = -1;
		this.category = null;
		this.status = null;
		this.taskId = -1;
	}
	
	public TaskObject(String title, int taskId){
		this.title = title;
		this.startDate = 0;
		this.endDate = 0;
		this.startTime = 0;
		this.endTime = 0;
		this.category = "";
		this.status = "";
		this.taskId = taskId;
	}
	
	// empty constructor
	public TaskObject() {
		
	}
	
	public String getTitle() {
		return title;
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
		return category;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTitle(String newTitle) {
		this.title = newTitle;
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
	
	// Checks if title, dates and times are invalid values
	public boolean isSearchKeywordPresent() {
		if (title == null && startDate == -1 && endDate == -1 && startTime == -1 && endTime == -1)
			return false;
		return true;
	}

}
