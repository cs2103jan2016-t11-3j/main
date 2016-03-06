package parser;

import java.util.ArrayList;
import logic.TaskObject;


public class AddProcessor extends CommandProcessor{
	
	public ArrayList<String> list = new ArrayList<String>();
	public ArrayList<Integer> dateList = new ArrayList<Integer>();
	
	private TimeProcessor TP = new TimeProcessor();
	private DateProcessor DP = new DateProcessor();
	public TaskObject TO = new TaskObject();
	
	/**
	 * this method will take in the command and allocate the correct information
	 * to task/date/time
	 * 
	 * @param input    string input from user
	 */
	public TaskObject process(String input) {
		//splits add command into array list
		convertToArray(input);
		//get the task stuff
		readTask();
		setTaskObject();
		return TO;
	}
	
	private void setTaskObject() {
		TO.setTitle(_task);
		TO.setStartTime(_startTime);
		TO.setEndTime(_endTime);
		TO.setEndDate(_endDate);
		TO.setStartDate(_startDate);
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
 		String _newtask = null;
 		for (i = 1; i < list.size(); i++) {
 			if (isStartOfDate(list.get(i))) {
 				break;
 			}
 			
 			if (i == 1) {
 				_newtask = list.get(i);
 			} else {
 				_newtask = _newtask + " " + list.get(i);	
 			}
 		}
 		
 		_task = _newtask;
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
		_startDate = DP.getStartDate();
		_endDate = DP.getEndDate();
 	}
 	
 	private void setTime() {
 		_startTime = TP.getStartTime();
 		_endTime = TP.getEndTime();
 	}
 	
 	public String getTask() {
 		return _task;
 	}
 	
 	public int getStartDate() {
 		return _startDate;
 	}
 	
 	public int getEndDate() {
 		return _endDate;
 	}
 	
 	public int getStartTime() {
 		return _startTime;
 	}
 	
 	public int getEndTime() {
 		return _endTime;
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
 		_task = null;
 		_startDate = -1;
 		_endDate = -1;
 		_startTime = -1;
 		_endTime = -1;
 	}
 	
 	public void clearDP() {
 		DP.clearList();
 		DP.resetDate();
 	}
 	
 	public void clearTP() {
 		TP.clearList();
 		TP.resetTime();
 	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
 	
}
