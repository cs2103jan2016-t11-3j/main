//@@author A0124052X

package common;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A class for storing relevant information about a task's recurrence. 
 * Only accessed and used in the TaskObject if the task is recurring.
 * 
 * @author ChongYan
 *
 */
public class Interval implements Comparator<Interval> {

	public final String FREQ_HOURLY = "HOURLY";
	public final String FREQ_DAILY = "DAILY";
	public final String FREQ_WEEKLY = "WEEKLY";
	public final String FREQ_MONTHLY = "MONTHLY";
	public final String FREQ_YEARLY = "YEARLY";

	private final String MESSAGE_INVALID_FREQUENCY = "Invalid frequency";

	private String freq = "";
	private int interval = -1;
	private int count = -1;
	private LocalDateTime until = LocalDateTime.MAX;
	private String byDay = ""; // for multiple occurrences within the same
								// frequency -- TO BE DONE
	private int[] byDayArray = new int[8];

	public Interval() {
	}

	public Interval(String freq, int interval, int count, String byDay) throws Exception {
		setFrequency(freq);
		this.interval = interval;
		this.count = count;
		this.byDay = byDay;
		this.until = LocalDateTime.MAX;
		this.byDayArray[0] = 0;
		// Until cannot be a valid value if count exists
	}

	public Interval(String freq, int interval, LocalDateTime until, String byDay) throws Exception {
		setFrequency(freq);
		this.interval = interval;
		this.until = until;
		this.byDay = byDay;
		this.count = -1;
		this.byDayArray[0] = 0;
		// Count cannot be a valid value if until exists
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
	
	public int[] getByDayArray() {
		return byDayArray;
	}

	public void setFrequency(String freq) throws Exception {
		switch (freq) {

		case FREQ_HOURLY:
			this.freq = FREQ_HOURLY;
			break;

		case FREQ_DAILY:
			this.freq = FREQ_DAILY;
			break;

		case FREQ_WEEKLY:
			this.freq = FREQ_WEEKLY;
			break;

		case FREQ_MONTHLY:
			this.freq = FREQ_MONTHLY;
			break;

		case FREQ_YEARLY:
			this.freq = FREQ_YEARLY;
			break;

		default:
			Exception e = new Exception(MESSAGE_INVALID_FREQUENCY);
			throw e;
		}
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

	public void setByDay(int day) {
		if (day < 8) {
			byDayArray[0] = 1;
			byDayArray[day] = 1;
		}
	}

	@Override
	public int compare(Interval arg0, Interval arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Returns true if all the values are the default values
	public boolean isNull() {
		return (freq.equals("") && interval == -1 && count == -1 && until.equals(LocalDateTime.MAX));
	}
	
	// For debugging
	public void print() {
		System.out.println("freq = " + freq);
		System.out.println("interval = " + interval);
		System.out.println("count = " + count);
		System.out.println("until = " + until.toString());
		System.out.println("byDay = " + byDay);
		System.out.println("byDayArray = " + Arrays.toString(byDayArray));
	}
}
