package parser;

import java.util.ArrayList;
import logic.TaskObject;


public class AddProcessor {
	
	public ArrayList<String> list = new ArrayList<String>();
	public ArrayList<Integer> dateList = new ArrayList<Integer>();
	
	
	private String task;
	private int startDate = -1;
	private int endDate = -1;
	private int startTime = -1;
	private int endTime = -1;
	
	private TimeProcessor TP = new TimeProcessor();
	private DateProcessor DP = new DateProcessor();
	public TaskObject TO = new TaskObject();
	
	/**
	 * this method will take in the command and allocate the correct information
	 * to task/date/time
	 * 
	 * @param input    string input from user
	 */
	public TaskObject addCommand(String input) {
		//splits add command into array list
		convertToArray(input);
		//get the task stuff
		readTask();
		setTaskObject();
		return TO;
	}
	
	private void setTaskObject() {
		TO.setTitle(task);
		TO.setStartTime(startTime);
		TO.setEndTime(endTime);
		TO.setEndDate(endDate);
		TO.setStartDate(startDate);
		TO.setStatus("undone");
	}

	private void convertToArray(String input) {
		for (String temp: input.split(" ")) {
 			list.add(temp);
 		}
	}
	
	/**
	 * this method extracts out the string from the array list for tasks
	 */
	private void readTask() {
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
 			readDate(i);
 		}
	}
	
	// this method checks for first occurrence of the keyword indicating date input
 	private boolean isStartOfDate(String input) {
 		return input.contains("date:");
 	}
	
 	/**
 	 * this method will extract out the string from the arraylist for date
 	 */
 	private void readDate(int index) {
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
 			readTime(index);
 		}
 		
 	}
 	
 	//checks for the "time:" keyword
 	private boolean isStartOfTime(String input) {
 		return input.contains("time:");
 	}
 	
 	/**
 	 * this method will extract the string from the array list for time
 	 */
 	private void readTime(int index) {
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
 	
 	private void setDate() {
		startDate = DP.getStartDate();
		endDate = DP.getEndDate();
 	}
 	
 	private void setTime() {
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
