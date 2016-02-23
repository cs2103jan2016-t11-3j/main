package logic;

import java.util.Date;


public class TaskObject {

	private String title;
	private int date;
	private int time;								
	private String status;
	private int taskId;
	
	public TaskObject(String title, int date, int time, String status, int taskId) {
		this.title = title;
		this.date = date;
		this.time = time;
		this.status = status;
		this.taskId = taskId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getDate() {
		return date;
	}
	
	public int getTime() {
		return time;
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
	
	public void setDate(int newDate) {
		this.date = newDate;
	}
	
	public void setTime(int newTime) {
		this.time = newTime;
	}
	
	public void setStatus(String newStatus) {
		this.status = newStatus;
	}
	
	public void setTaskId(int newTaskId) {
		this.taskId = newTaskId;
	}

}