package logic;

import java.util.Date;


public class TaskObject {

	private String title;
	private int startDate;
	private int endDate;
	private int startTime;
	private int endTime;
	private String category; // deadline, event, or floating
	private String status;
	private int taskId;
	
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
	
	public TaskObject(String title, int taskId){
		this.title = "title";
		this.startDate = 0;
		this.endDate = 0;
		this.startTime = 0;
		this.endTime = 0;
		this.category = "default";
		this.status = "default";
		this.taskId = taskId;
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

}
