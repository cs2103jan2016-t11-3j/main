package parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.TaskObject;

public class EditParser extends CommandParser {
	
	private ArrayList<String> list = new ArrayList<String>();

	private TaskObject TO = new TaskObject();

	
	private int _index = -1;
	
	/**
	 * this method will take in the string from the parser 
	 * and break down its component, determining if it is a task, time or date edit
	 * @throws Exception 
	 */
	public TaskObject process(String input) throws Exception {
		convertToArray(input); //change this to extract index
		input = cleanString(input);
		
		Pattern dateTimePattern = Pattern.compile(Constants.REGEX_SEARCH2);
		Matcher matcher = dateTimePattern.matcher(input);
		
		String identifier = null;
		
		if (matcher.find()) {
			identifier = getTrimmedString(input ,matcher.start(), matcher.end());
			input = getTrimmedString(input, 0, matcher.start());
		}
		
		if (identifier != null) {
			DateTimeParser dtp = new DateTimeParser();
			dtp.parseDateTime(identifier, false);
			setDateTime(dtp);
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
	
	public void setDateTime(DateTimeParser dtp) {
		_startDateTime = dtp.getStartDateTime();
		_endDateTime = dtp.getEndDateTime();
		_startTime = dtp.getStartTime();
		_startDate = dtp.getStartDate();
		_endTime = dtp.getEndTime();
		_endDate = dtp.getEndDate();
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
 		_startDateTime = LocalDateTime.of(LocalDate.MAX, LocalTime.MAX);
 		_endDateTime = LocalDateTime.of(LocalDate.MAX, LocalTime.MAX);
 	}
}
