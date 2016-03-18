package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.TaskObject;

public class SearchParser extends CommandParser {
	private TaskObject TO = new TaskObject();
	
	public TaskObject process(String input) throws Exception {
		input = removeSearchKeyword(input);
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
			dtp.parseDateTime(identifier, false);
			setDateTime(dtp);
        }
		
		_task = input;

		setTaskObject();
		return TO;
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
		return input.replaceFirst("search ", "");
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
		// TODO Auto-generated method stub
		return 0;
	}
	
}
