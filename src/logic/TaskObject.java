import java.util.Date;


public class TaskObject {

	private String title;
	private Date date;
	private int time;									// TIME INT???
	private String status;
	
	public TaskObject(String title, Date date, int time, String status) {
		this.title = title;
		this.date = date;
		this.time = time;
		this.status = status;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getTime() {
		return time;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	public void setDate(Date newDate) {
		this.date = newDate;
	}
	
	public void setTime(int newTime) {
		this.time = newTime;
	}
	
	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

}
