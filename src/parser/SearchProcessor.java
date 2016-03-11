package parser;

import java.util.ArrayList;

import common.TaskObject;

public class SearchProcessor extends CommandProcessor {
	
	private boolean isPM;
	
	private ArrayList<String> list = new ArrayList<String>();
	public TaskObject TO = new TaskObject();
	
	private DateTimeProcessor dtp = new DateTimeProcessor();
	
	public TaskObject process(String input) {
		input = removeSearchKeyword(input);
		convertToArray(input);
		if (isDateTime(input)) {
			//process and set time
			dtp.parseDateTime(input, false);
			setDateTime();
		}
		//set task
		_task = input;
		setTaskObject();
		return TO;
	}
	
	private void setDateTime() {
		_startDate = dtp.getStartDate();
		_startTime = dtp.getStartTime();
		_startDateTime = dtp.getStartDateTime();
	}
	
	private void setTaskObject() {
		TO.setTitle(_task);
		TO.setStartTime(_startTime);
		TO.setEndTime(_endTime);
		TO.setEndDate(_endDate);
		TO.setStartDate(_startDate);
	}
	
	public boolean isDateTime(String input) {
		if (input.matches(Constants.REGEX_EDIT_DATE_TIME_IDENTIFIER)) {
			return true;
		} else {
			return false;
		}
	} 
	
	public String removeSearchKeyword(String input) {
		return input.replaceFirst("search ", "");
	}
	
	public void convertToArray(String input) {
			for (String temp: input.split(" ")) {
				list.add(temp);
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
