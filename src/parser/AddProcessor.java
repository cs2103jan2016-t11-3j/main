package parser;

import java.util.ArrayList;


public class AddProcessor {
	
	public static ArrayList<String> list = new ArrayList<String>();
	public static ArrayList<Integer> dateList = new ArrayList<Integer>();
	
	private static String task;
	private static int startDate = -1;
	private static int endDate = -1;
	private static int startTime = -1;
	private static int endTime = -1;
	
	private static TimeProcessor TP = new TimeProcessor();
	private static DateProcessor DP = new DateProcessor();
	
	/**
	 * this method will take in the command and allocate the correct information
	 * to task/date/time
	 * 
	 * @param input    string input from user
	 */
	public void addCommand(String input) {
		//splits add command into array list
		convertToArray(input);
		//get the task stuff
		readTask();
	}

	public void convertToArray(String input) {
		for (String temp: input.split(" ")) {
 			list.add(temp);
 		}
	}
	
	/**
	 * this method extracts out the string from the array list for tasks
	 */
	public void readTask() {
 		int i;
 		String _task = null;
 		for (i = 1; i < list.size(); i++) {
 			if (isStartOfDate(list.get(i))) {
 				break;
 			}
 			
 			if (i == 1) {
 				_task = list.get(i);
 			} else {
 				_task = _task + " " + list.get(i);	
 			}
 		}
 		
 		task = _task;
 		if (i == list.size()){
 			return;
 		} else {
 			readDate(i+1);
 		}
	}
	
	// this method checks for first occurrence of the keyword indicating date input
 	public boolean isStartOfDate(String input) {
 		return input.matches("date:");
 	}
	
 	/**
 	 * this method will extract out the string from the arraylist for date
 	 */
 	public void readDate(int index) {
 		String _date = null;
 		int i = 0;
 		//forms the date string
 		for (i = index; i < list.size(); i++) {
 			if (isStartOfTime(list.get(i))) {
 				break;
 			}
 			
 			if (i == index) {
 				_date = list.get(i);
 			} else {
 				_date = _date + " " + list.get(i);	
 			}
 		}
 		DP.processDate(_date, false);
 		setDate();
 		index = i;
 		if (i == list.size()){
 			return;
 		} else {
 			readTime(index+1);
 		}
 		
 	}
 	
 	//checks for the "time:" keyword
 	public boolean isStartOfTime(String input) {
 		return input.matches("time:");
 	}
 	
 	/**
 	 * this method will extract the string from the array list for time
 	 */
 	public void readTime(int index) {
 		String _time = null;
 		int i;
 		for (i = index; i < list.size(); i++) { 			
 			if (i == index) {
 				_time = list.get(i);
 			} else {
 				_time = _time + " " + list.get(i);	
 			}
 		}
 		TP.processTime(_time);
 		setTime();
 	}
 	
 	public static void setDate() {
		startDate = DP.getStartDate();
		endDate = DP.getEndDate();
 	}
 	
 	public static void setTime() {
 		startTime = TP.getStartTime();
 		endTime = TP.getEndTime();
 	}
 	
 	public String getTask() {
 		return task;
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
 	
 	public void reset() {
 		task = null;
 		startDate = -1;
 		endDate = -1;
 		startTime = -1;
 		endTime = -1;
 	}
 	
 	public void clearDP() {
 		DP.clearList();
 		DP.resetDate();
 	}
 	
 	public void clearTP() {
 		TP.clearList();
 		TP.resetTime();
 	}
 	
}
