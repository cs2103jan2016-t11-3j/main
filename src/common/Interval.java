package common;

import java.time.LocalDateTime;

public class Interval {
	private String freq;
	private int interval;
	private int count;
	private LocalDateTime until;
	private String byDay;
	
	public Interval() {
	}
	
	public Interval(String freq, int interval, int count, LocalDateTime until, String byDay) {
		this.freq = freq;
		this.interval = interval;
		this.count = count;
		this.until = until;
		this.byDay = byDay;
	}
	
	public Interval(String freq, int interval, int count, String byDay) {
		this.freq = freq;
		this.interval = interval;
		this.count = count;
		this.byDay = byDay;
	}
	
	public Interval(String freq, int interval, LocalDateTime until, String byDay) {
		this.freq = freq;
		this.interval = interval;
		this.until = until;
		this.byDay = byDay;
	}
	
	public String getFrequency() {
		return freq;
	}
	
	public int getTimeInterval() {
		return interval;
	}
	
	public int getCount() {
		return count;
	}
	
	public LocalDateTime getUntil() {
		return until;
	}
	
	public String getByDay() {
		return byDay;
	}
	
	public void setFrequency(String freq) {
		this.freq = freq;
	}
	
	public void setTimeInterval(int interval) {
		this.interval = interval;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public void setUntil(LocalDateTime until) {
		this.until = until;
	}
	
	public void setByDay(String byDay) {
		this.byDay = byDay;
	}
}
