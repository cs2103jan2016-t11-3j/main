package common;

import java.time.LocalDateTime;

public class LocalDateTimePair {
	
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	
	// Constructor for events
	public LocalDateTimePair(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
	}
	
	// Constructors for deadlines
	public LocalDateTimePair(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
		this.endDateTime = LocalDateTime.MAX; // System defined value
	}
	
	// Empty constructor
	public LocalDateTimePair() {
		this.startDateTime = LocalDateTime.MAX;
		this.endDateTime = LocalDateTime.MAX;
		
	}
	
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}
	
	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public void print() {
		System.out.println("Start date/time = " + startDateTime.toString() + ", End date/time = " + endDateTime.toString());
	}
	
}
