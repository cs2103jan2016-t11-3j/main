package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.TaskObject;

public class SearchParser extends CommandParser {
	private TaskObject TO = new TaskObject();
	private int index=-1;
	
	public TaskObject process(String input) throws Exception {
		input = removeSearchKeyword(input);
		if (isSearchByCategory(input)) {
			setCategory(input);
		} else if (isSearchForRecurringDates(input)) {
			setIndex(input);
		} else {
			
			//read directly with matcher
			Pattern dateTimePattern = Pattern.compile(Constants.REGEX_SEARCH);
			Matcher matcher = dateTimePattern.matcher(input);
			
			String identifier = null;
			
			if (matcher.find()) {
				identifier = getTrimmedString(input ,matcher.start(), input.length());
				input = getTrimmedString(input, 0, matcher.start());
			}
			
			if (identifier != null) {
				DateTimeParser dtp = new DateTimeParser();
				TO = dtp.parse(identifier, false);
				setDateTime(dtp);
	        }
			
			_task = input;

			setTaskObject();
		}
		return TO;
	}
	
	private boolean isSearchForRecurringDates(String input) {
		if (input.matches("\\d+")) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setIndex(String input) {
		index = Integer.parseInt(input);
	}
	
	private boolean isSearchByCategory(String input) {
		if (input.matches("(floating|deadline|event)")) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setCategory(String input) {
		if (input.matches("floating")) {
			TO.setCategory("floating");
		} else if (input.matches("deadline")) {
			TO.setCategory("deadline");
		} else if (input.matches("event")) {
			TO.setCategory("event");	
		}
	}
	
	private void setDateTime(DateTimeParser dtp) {
		_startDate = dtp.getStartDate();
		_startTime = dtp.getStartTime();
		_startDateTime = dtp.getStartDateTime();
		
		_endDate = dtp.getEndDate();
		_endTime = dtp.getEndTime();
		_endDateTime = dtp.getEndDateTime();
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
	
	public boolean isDateTime(String input) {
		
		if (input.matches(Constants.REGEX_EDIT_DATE_TIME_IDENTIFIER)) {
			return true;
		} else {
			return false;
		}
	} 
	
	public String removeSearchKeyword(String input) {
		return input.replaceFirst("search ", "").trim();
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
		return index;
	}
	
}
