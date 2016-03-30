package parser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TimeParser {
	private final ArrayList<String> list = new ArrayList<String>();
	private int time = -1;
	private String timeString = "";
	private LocalTime timeObject = LocalTime.MAX;
		
	
	/**
	 * this method takes in the user's input from add/edit/search parser
	 * 
	 *@param input  time input from user
	 *			e.g. 21:59hr, 7.13pm
	 * @throws Exception 
	 */
	public void processTime(String input) throws Exception {
		if (!input.isEmpty()) {
			furtherProcessTime(input);
			convertToString();
		} else {
			
		}
	}
	
	
	
	/**
	 * this method will check process the time input by recognizing am/pm/hr
	 * 
	 * @param input   time input from user
	 */
	public void furtherProcessTime(String input) {
		if (isAM(input)) {
			setTime(input, false);
		} else if (isPM(input)) {
			setTime(input, true);
		} else if (isHr(input)) {
			setTime(input, false);
		}
	}

	public boolean isPM(String temp) {
		return temp.contains("pm");
	}

	public boolean isAM(String temp) {
		return temp.contains("am");
	}
	
	public boolean isHr(String temp) {
		return temp.contains("hr") || temp.contains("hour");
	}
	
	/**
	 * this method cleans the string and converts to the integer form. It will be manipulated into HHMM format
	 */
	public void setTime(String input, boolean isPM) {
		if (input.matches(Constants.REGEX_TIME_HHMM)) {
			input = input.replaceAll("[:!-/a-zA-Z]+", "").trim();
			time = Integer.parseInt(input);
		} else {
			input = input.replaceAll("[:!-/a-zA-Z]+", "");
			input = input.replaceAll(" ", "");
			int temp = 0;
			temp = Integer.parseInt(input);
			if (temp < 100) { //converts time to 4 digit format
				temp = temp * 100;
			}
			
			if (isPM) { //converts timing to correct value
				temp = temp + 1200;
			} else if (!isPM && temp > 1159) {
				temp = temp - 1200;
			}
			
			
			
			if (temp > 2359) {
				temp = temp - 1200; // how ah here
			}
			time = temp;	
		}
		
	}
	
	/**
	 * method will convert time from integer format in HHmm to string format in HH:mm 
	 * and create LocalTime object
	 * @throws Exception 
	 */
	private void convertToString() throws Exception {
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
	
	
	
	/**
	 * this method splits string into array list for easy manipulation.
	 * ideally the start and end date if any, will be split into two elements of the arraylist (events)
	 * if there is only one date, there will be no splitting
	 * 
	 * @param input  time input by user
	 */
	public void convertToArray(String input) {
		if (input.contains("-")) {
			for (String temp: input.split("-")) {
				temp = temp.replaceAll(" ", "");
				list.add(temp);
				}
		} else if (input.contains("to")) {
			for (String temp: input.split("to")) {
				temp = temp.replaceAll(" ", "");
	 			list.add(temp);
	 			}
		} else {
			list.add(input);
		}
	}
	
	
	public int getTime() {
		return time;
	}
	
	public String getTimeString() {
		return timeString;
	}
	
	public LocalTime getTimeObject() {
		return timeObject;
	}
	
	//method used to obtain the size of the list for testing 
	public int getListSize() {
		return list.size();
	}
	
	//method used to get the ith element in the list for testing
	public String getListElement(int i) {
		return list.get(i);
	}
	
	//method used to get the ith element in the list for testing
	public void clearList() {
		list.clear();
	}
	
	//method used to reset all dates for testing
	public void resetTime() {
		time = -1;
	}
}
