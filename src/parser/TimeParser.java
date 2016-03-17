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
	 * this method takes in the user's input from add/edit/search processor
	 * 
	 *@param input  time input from user
	 */
	public void processTime(String input) {
		if (!input.isEmpty()) {
			furtherProcessTime(input);
			convertToString();
		}
	}
	
	
	
	/**
	 * this method will check process the time input by recognizing am/pm and if the time is 
	 * in order (8-5pm is recognized as 8am to 5pm)
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
		input = input.replaceAll("[:!-/a-zA-Z]+", "");
		input = input.replaceAll(" ", "");
		int temp = 0;
		temp = Integer.parseInt(input);
		if (temp < 100) {
			temp = temp * 100;
		}
		
		if (isPM) {
			temp = temp + 1200;
		}
		
		if (temp == 2400) {
			temp = 0000;
		}
		
		if (temp > 2359) {
			return;
		}
		time = temp;
	}
	
	private void convertToString() {
		String minute, hour;
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		if (time < 1000) {
			minute = "0" + Integer.toString(time);
		} else {
			minute = Integer.toString(time);
		}
		
		hour = minute.substring(0, 2);
		minute = minute.substring(2);
		
		timeString = hour + ":" + minute;
		timeObject = LocalTime.parse(timeString, timeFormatter);
	}
	
	/**
	 * this method checks if the two time are in running order, meaning 8-1pm would
	 * mean 8am to 1pm instead of 8pm to 1pm
	 * 
	 * @param temp      start time
	 * @param tempNext  end time
	 */
	private static boolean checkIfCrossover(String temp, String tempNext) {
		int first, second;
		temp = temp.replaceAll("[:!-/a-zA-Z]+", "");
		tempNext = tempNext.replaceAll("[:!-/a-zA-Z]+", "");
		first = Integer.parseInt(temp);
		second = Integer.parseInt(tempNext);
		
		if (first < 100) {
			first = first * 100;
		}
		
		if (second < 100) {
			second = second * 100;
		}
		
		if (first > second) {
			return true;
		} else {
			return false;
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
