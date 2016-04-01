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
	
	public boolean equals(LocalDateTimePair newDateTimePair) {
		return (this.startDateTime.equals(newDateTimePair.getStartDateTime()) && this.endDateTime.equals(newDateTimePair.getEndDateTime()));
		
	}
	
	public boolean isEmpty() {
		return (this.startDateTime.equals(LocalDateTime.MAX) && this.endDateTime.equals(LocalDateTime.MAX));
	}
	
	public String print() {
		return "Start date/time = " + startDateTime.toString() + ", End date/time = " + endDateTime.toString();
	}
	
}
