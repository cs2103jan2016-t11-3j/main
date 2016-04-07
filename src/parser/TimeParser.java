//@@author A0125003A
package parser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * This class's job is to identify the correct time from a user's string input.
 * It recognises time in 2 main formats, hh:mm and hh:mm AM/PM. 
 * 
 * @author sylvesterchin
 *
 */
public class TimeParser {
	private int time = -1;
	private String timeString = "";
	private LocalTime timeObject = LocalTime.MAX;
		
	
	/**
	 * This method takes in the user's input from add/edit/search parser, checks if empty
	 * and processes it into 
	 * 
	 *@param input  
	 *				time input from user. null input results in nothing being done
	 *				e.g. 21:59hr, 7.13pm
	 * @throws Exception 
	 */
	public void processTime(String input) throws Exception {
		if (!input.isEmpty()) {
			furtherProcessTime(input);
			createLocalTimeObject();
		}
	}
	
	// ================================
	// First Level of Abstraction
	// ================================
	
	/**
	 * This method will check process the time input by recognizing am/pm/hr
	 * 
	 * @param input   
	 * 				time input from user, non-null string
	 */
	private void furtherProcessTime(String input) {
		if (isAM(input)) {
			setTime(input, false);
		} else if (isPM(input)) {
			setTime(input, true);
		} else if (isHr(input)) {
			setTime(input, false);
		}
	}
	
	/**
	 * This method will convert time from integer format in HHmm to string format in HH:mm 
	 * and create LocalTime object.
	 * 
	 * @throws Exception 
	 */
	private void createLocalTimeObject() throws Exception {
		String minute, hour;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		
		if (time < 1000 && time > 99) {
			minute = "0" + Integer.toString(time);
		} else if (time < 1000 && time > 9) {
			minute = "00" + Integer.toString(time);
		} else if (time < 1000 && time < 100) {
			minute = "000" + Integer.toString(time);
		} else {
			minute = Integer.toString(time);
		}
		
		if ((minute.length() == 4 && time < 2400) || minute.matches("0000")) {
			hour = minute.substring(0, 2);
			minute = minute.substring(2);
			timeString = hour + ":" + minute;
			timeObject = LocalTime.parse(timeString, timeFormatter);	
		} else {
			throw new Exception("Invalid Time");
		}
	}
	
	// ================================
	// Second Level of Abstraction
	// ================================
	
	/**
	 * This method cleans the string and converts to the integer form. 
	 * It will be manipulated into HHMM format.
	 * 
	 * @param input
	 * 				time input in string format. non null. 
	 * @param isPM
	 * 				boolean, true if the time input has "PM" in it
	 */
	private void setTime(String input, boolean isPM) {
		if (input.matches(Constants.REGEX_TIME_HHMM)) {
			input = input.replaceAll("[:!-/a-zA-Z]+", "").trim();
			time = Integer.parseInt(input);
		} else {
			input = input.replaceAll("[:!-/a-zA-Z]+", "");
			input = input.replaceAll(" ", "");
			int _time = 0;
			_time = Integer.parseInt(input);
			time = timeConverter(isPM, _time);	
		}
	}
	
	private boolean isPM(String temp) {
		return temp.contains("pm");
	}

	private boolean isAM(String temp) {
		return temp.contains("am");
	}
	
	private boolean isHr(String temp) {
		return temp.contains("hr") || temp.contains("hour");
	}
	
	// ================================
	// Third Level of Abstraction
	// ================================
	
	/**
	 * This method will convert input time into a number that is within the range of 
	 * 0-2400 if the input is a valid time. 
	 * 
	 * @param isPM
	 * 				boolean to show if the original input had a "pm" marker.
	 * @param _time
	 * 				time input by user. positive, non-null. 
	 * @return
	 */
	private int timeConverter(boolean isPM, int _time) {
		if (_time < 100) { //converts time to 4 digit format
			_time = _time * 100;
		}
		
		if (isPM) { //converts timing to correct value
			_time = _time + 1200;
			if (_time > 2359 && _time < 2460 ) {
				_time = _time - 1200;
			}
		} else if (!isPM && _time > 1159 && _time < 1260) { //only for 12.xxam cases
			_time = _time - 1200;
		} else if (!isPM && _time > 1259) {
			_time = _time + 9999; // anything more than 12.59am will be rendered invalid
		}
		return _time;
	}

	
	
	/**
	 * Getter method for test in JUnit environment.
	 * 
	 * @return LocalTime variable
	 */
	public LocalTime getTimeObject() {
		return timeObject;
	}
	
	
}
