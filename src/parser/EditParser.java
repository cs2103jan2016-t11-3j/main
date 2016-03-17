package parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.TaskObject;

public class EditParser extends CommandParser {
	
	private ArrayList<String> list = new ArrayList<String>();

	private TaskObject TO = new TaskObject();

	public DateTimeParser dtp = new DateTimeParser();
	private int _index = -1;
	
	/**
	 * this method will take in the string from the parser 
	 * and break down its component, determining if it is a task, time or date edit
	 */
	public TaskObject process(String input) {
		convertToArray(input); //change this to extract index
		input = cleanString(input);
		
		Pattern dateTimePattern = Pattern.compile(Constants.REGEX_SEARCH);
		Matcher matcher = dateTimePattern.matcher(input);
		
		String identifier = null;
		
		if (matcher.find()) {
			identifier = getTrimmedString(input ,matcher.start(), input.length());
			input = getTrimmedString(input, 0, matcher.start());
		}
		
		if (identifier != null) {
			dtp.parseDateTime(identifier, false);
			setDateTime();
        }
		
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
		TO.setStartDateTime(_startDateTime);
		TO.setEndDateTime(_endDateTime);
	}
	
	/**
	 * this method will convert instruction into string array list
	 * and remove the "edit" and number
	 */
	public void convertToArray(String input) {
		for (String temp : input.split(" ")) {
			list.add(temp);
		}
		
		//remove "edit" and "number"
		list.remove(0);
		String index = list.get(0);
		_index = Integer.parseInt(index);
		list.remove(0); 	// REMOVED THIS BECAUSE EDIT FUNCTION NEEDS THE INDEX NUMBER
	}
	
	/**
	 * this method will re-form the command that the user input
	 * without "edit" and the index number
	 */
	public String cleanString(String input) {
		input = input.replaceFirst(Constants.REGEX_EDIT, "").trim();
		return input;
	}
	
	public boolean isDateTime(String input) {
		if (input.matches(Constants.REGEX_EDIT_DATE_TIME_IDENTIFIER)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setDateTime() {
		_startDateTime = dtp.getStartDateTime();
		_endDateTime = dtp.getEndDateTime();
		_startTime = dtp.getStartTime();
		_startDate = dtp.getStartDate();
		_endTime = dtp.getEndTime();
		_endDate = dtp.getEndDate();
	}
	
	//this method sets the date for the object by using the date processor 
	//class to performing the processing
	public void setDate(String input) {
		if (input.contains("start")) {
			setStartDate(dtp.getStartDate());
		} else if (input.contains("end")) {
			setEndDate(dtp.getStartDate());
		} else {
			setStartDate(dtp.getStartDate());
			setEndDate(dtp.getEndDate());
		}
	}
	
	//this method sets the time for the object by using the time processor 
	//class to performing the processing
	public void setTime(String input) {
		if (input.contains("start")) {
			setStartTime(dtp.getStartTime());
		} else if (input.contains("end")) {
			setEndTime(dtp.getStartTime());
		} else {
			setStartTime(dtp.getStartTime());
			setEndTime(dtp.getEndTime());
		}
	}
	
	public String getTask() {
		return _task;
	}

	public void setTask(String task) {
		_task = task;
	}

	public int getStartDate() {
		return _startDate;
	}

	public void setStartDate(int startDate) {
		_startDate = startDate;
	}

	public int getEndDate() {
		return _endDate;
	}

	public void setEndDate(int endDate) {
		_endDate = endDate;
	}

	public int getStartTime() {
		return _startTime;
	}

	public void setStartTime(int startTime) {
		_startTime = startTime;
	}

	public int getEndTime() {
		return _endTime;
	}

	public void setEndTime(int endTime) {
		_endTime = endTime;
	}
	
	public LocalDateTime getStartDateTime() {
		return _startDateTime;
	}
	
	public LocalDateTime getEndDateTime() {
		return _endDateTime;
	}
	
	public int getIndex() {
		return _index;
	}
 	
	public void resetList() {
		list.clear();
	}
	
 	public void reset() {
 		setTask("");
 		setStartDate(-1);
 		setEndDate(-1);
 		setStartTime(-1);
 		setEndTime(-1);
 	}
 	
 	/*
 	 * if (isDateTime(clean_string)) {
		dtp.parseDateTime(clean_string, false);
		setDateTime();
		setDate(input);
		setTime(input);
	} else {
		setTask(clean_string);
	} 
 	 */
}
