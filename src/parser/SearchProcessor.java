package parser;

import java.util.ArrayList;

import common.TaskObject;

public class SearchProcessor extends CommandProcessor {
	
	private boolean isPM;
	
	private ArrayList<String> list = new ArrayList<String>();
	public TaskObject TO = new TaskObject();
	
	public TaskObject process(String input) {
		input = removeSearchKeyword(input);
		convertToArray(input);
		if (isTime(input) && hasNumber()) {
			//process and set time
			convertToTime(input, isPM);
		} else if (isDate(input) && hasNumber()) {
			//process and set date
			convertToDate(input);
		}
		//set task
		_task = input;
		setTaskObject();
		return TO;
	}
	
	private void setTaskObject() {
		TO.setTitle(_task);
		TO.setStartTime(_startTime);
		TO.setEndTime(_endTime);
		TO.setEndDate(_endDate);
		TO.setStartDate(_startDate);
	}
	
	public boolean hasNumber() {
		for (String testing : list) {
			if (testing.matches(".*\\d.*")) {
				return true;
			}
		}
		return false;
	}
	
	public String removeSearchKeyword(String input) {
		return input.replaceFirst("search ", "");
	}
	
	/**
	 * this method checks for presence of time-keywords in the string 
	 * 
	 */
	private boolean isTime(String input) {
		input.toLowerCase();
		if (input.contains(TIME_AM_1) || input.contains(TIME_AM_2) || 
				input.contains(TIME_AM_3) || input.contains(TIME_AM_4)) {
			isPM = false;
			return true;
		} else if (input.contains(TIME_PM_1) || input.contains(TIME_PM_2)|| 
				input.contains(TIME_PM_3) || input.contains(TIME_PM_4)) {
			isPM = true;
			return true;
		}
		return false;
	}
	
	
	public boolean isDate(String input) {
		if (hasMonth(input) || isAlternativeDateFormat(input)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void convertToArray(String input) {
			for (String temp: input.split(" ")) {
				list.add(temp);
			}
	}
	
	/**
	 * this method converts a string into an integer that represents
	 * time in HHMM format
	 */
	public void convertToTime(String input, boolean isPM) {
		input = input.replaceAll("[!-/a-zA-Z]+", "");
		input = input.replaceAll(" ", "");
		if (!input.isEmpty()) { 
		int time = Integer.parseInt(input);
			if (time < 100) {
				time = time * 100;
			}
 		
			if (isPM) {
				time = time + 1200;
			}
 		
			if (time == 2400) {
				time = 0000;
			}
			_startTime = time;
			_endTime = _startTime;
		}
	}
	
	public void convertToDate(String input) {
		DateProcessor DP = new DateProcessor();
		DP.processDate(input, true);
		_startDate = DP.getSearchDate();
		_endDate = _startDate;
		DP.resetDate();
		DP.clearList();
	}
	
	public boolean hasMonth(String input) {
		if (input == MONTH_1_1 || input.contains(MONTH_1_2) || 
				input == MONTH_2_1 || input.contains(MONTH_2_2) || 
				input == MONTH_3_1 || input.contains(MONTH_3_2) ||
				input == MONTH_4_1 || input.contains(MONTH_3_2) ||
				input.contains(MONTH_5_1) || input == MONTH_6_1 ||
				input.contains(MONTH_6_2) || input == MONTH_7_1 ||
				input.contains(MONTH_7_2) || input == MONTH_8_1 ||
				input.contains(MONTH_8_2) || input == MONTH_9_1 ||
				input.contains(MONTH_9_2) || input == MONTH_10_1 ||
				input.contains(MONTH_10_2) || input == MONTH_11_1 ||
				input.contains(MONTH_11_2) || input == MONTH_12_1 ||
				input.contains(MONTH_12_2)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int setMonthInDataProcessor(String month) {
		month = month.toLowerCase();
		if (month == MONTH_1_1 || month.contains(MONTH_1_2)) {
			return VALUE_JAN;
		} else if (month == MONTH_2_1 || month.contains(MONTH_2_2)) {
			return VALUE_FEB;
		} else if (month == MONTH_3_1 || month.contains(MONTH_3_2)) {
			return VALUE_MAR;
		} else if (month == MONTH_4_1 || month.contains(MONTH_4_2)) {
			return VALUE_APR;
		} else if (month.contains(MONTH_5_1)) {
			return VALUE_MAY;
		} else if (month == MONTH_6_1 || month.contains(MONTH_6_2)) {
			return VALUE_JUN;
		} else if (month == MONTH_7_1 || month.contains(MONTH_7_2)) {
			return VALUE_JUL;
		} else if (month == MONTH_8_1 || month.contains(MONTH_8_2)) {
			return VALUE_AUG;
		} else if (month == MONTH_9_1 || month.contains(MONTH_9_2)) {
			return VALUE_SEPT;
		} else if (month == MONTH_10_1 || month.contains(MONTH_10_2)) {
			return VALUE_OCT;
		} else if (month == MONTH_11_1 || month.contains(MONTH_11_2)) {
			return VALUE_NOV;
		} else if (month == MONTH_12_1 || month.contains(MONTH_12_2)) {
			return VALUE_DEC;
		} else {
			return -1;
		}
	}
	
	private static boolean isAlternativeDateFormat(String input) {
		//look for slashes
		ArrayList<String> templist = new ArrayList<String>();
		if (input.contains("/")) {
			for (String temp : input.split("/")) {
				templist.add(temp);
			}
			if (templist.size() < 4) { //magic number
				return true;
			} else {
				return false;
			}
		}
		
		return false;
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
 	
 	public void resetAll() {
 		_task = null;
 		_startDate = -1;
 		_endDate = -1;
 		_startTime = -1;
 		_endTime = -1;
 	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
