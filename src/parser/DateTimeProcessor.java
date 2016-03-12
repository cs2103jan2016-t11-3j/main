package parser;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import parser.Constants.TaskType;

/*
 * so far 
 * 1. from DT to DT
 * 2. by/before/at/on DT
 * 3. DT -> ddmmyyy, ddmonthyyyy, hhmm(hrs), hhmm(am/pm)
 * 
 * 1. (tmr/tomorrow) (get today date and + 1)
 * 
 * 2. (next/previous){x} (mon/tues/wed/thur/fri/sat/sun)(week)(year)(month) 
 * 		--> count number of next n prev 
 * 		--> figure out if its day, month or year
 * 		--> get current date/time
 * 		--> add the increment
 * 
 * 3. [\\d](mins)(hours)(days)(month)(year) (later/after/before/from now)
 * 		--> identify later/fromnow n etc, 
 * 		--> identify (mins or hour) (days, month or year)
 * 		--> convert \\d to integer
 * 		--> get current time or date
 * 		--> add the number of *unit* to current -> return 
 * 
 * 
 */

public class DateTimeProcessor {
	
	private int _startTime = -1;
	private int _endTime = -1;
	private int _startDate = -1;
	private int _endDate = -1;
	
	String startDT;
	String endDT;
	
	LocalDateTime startDateTime;
	LocalDateTime endDateTime;
	
	
	List<String> dtlist = new ArrayList<String>();
	
	public void parseDateTime(String input, boolean isForAdd) {
		if (isForAdd) {
			parseDateTimeForAdd(input);
		} else {
			TaskType tasktype = getTaskType(input);
			//separate stuff for different task types
			switch(tasktype) {
			case event:
				//find the "to" word n split
				for (String temp : input.split("to")) {
					dtlist.add(temp);
				}
				separateDateTime(dtlist.get(0), true);
				separateDateTime(dtlist.get(1), false);
				break;
			case deadline:
			default:
				separateDateTime(input, true);
				break;
			}
			setLocalDateTime();
		}
	}
	
	private void parseDateTimeForAdd(String input) {
		TaskType tasktype = getTaskType(input);
		//separate stuff for different task types
		switch(tasktype) {
		case event:
			//find the "to" word n split
			for (String temp : input.split("to")) {
				dtlist.add(temp);
			}
			separateDateTime(dtlist.get(0), true);
			separateDateTime(dtlist.get(1), false);
			break;
		case deadline:
			separateDateTime(input, true);
			break;
		default:
			break;
		}
		setLocalDateTime();
	}
	
	/**
	 * 
	 */
	public void setLocalDateTime() {
		if (_startTime == -1 || _endTime == -1 || _startDate == -1 || _endDate ==-1) {
			setUniqueLocalDateTime();
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
			setDateTimeString();
			startDateTime = LocalDateTime.parse(startDT, formatter);
			if(_endTime != -1 || _endDate != -1) {
				endDateTime = LocalDateTime.parse(endDT, formatter);
			}
		}
	}
	
	public void setUniqueLocalDateTime() {
		//create unique datetime formatters
	}
	
	/**
	 * this method will convert date and time into a string in the same format as the formatter
	 * 
	 */
	public void setDateTimeString() {
		String startTimeString, endTimeString;
		if (_startTime < 1000) {
			startTimeString = "0" + Integer.toString(_startTime);
		} else {
			startTimeString = Integer.toString(_startTime);
		}
		
		if (_endTime < 1000) {
			endTimeString = "0" + Integer.toString(_endTime);
		} else {
			endTimeString = Integer.toString(_endTime);
		}
		
		startDT = Integer.toString(_startDate) + " " + startTimeString;
		endDT = Integer.toString(_endDate) + " " + endTimeString;
	}
	
	public TaskType getTaskType(String input) {
		if (input.matches(Constants.REGEX_DEADLINE_IDENTIFIER)) {
			return TaskType.deadline;
		} else if (input.matches(Constants.REGEX_EVENT_IDENTIFIER)) {
			return TaskType.event;
		} else if (input.matches(Constants.REGEX_POINT_TASK_IDENTIFIER)) {
			return TaskType.deadline;
		} else {
			return TaskType.floating;
		}
	}
	
	/**
	 * method will take in string and identify regular expressions for time
	 * and date. It will create date processor object and tie processor object
	 * to parse date and time
	 * 
	 * @param input
	 * @param isStart
	 */
	public void separateDateTime(String input, boolean isStart) {
		//Pattern date = Pattern.compile(Constants.REGEX_DATE_FORMAT);
		Pattern time = Pattern.compile(Constants.REGEX_TIME_FORMAT);
		//Pattern relativedate = Pattern.compile(Constants.REGEX_RELATIVE_DATE_ALL);
		
		//Matcher dateMatcher = date.matcher(input);
		Matcher timeMatcher = time.matcher(input);
		//Matcher rdateMatcher = relativedate.matcher(input);
		
		DateProcessor DP = new DateProcessor();
		TimeProcessor TP = new TimeProcessor();
		
		String _date = "", _time = "";
		
		if (timeMatcher.find()) {
			_time = getTrimmedString(input, timeMatcher.start(), timeMatcher.end());
			_date = input.replaceAll(_time, "").trim();
		} else {
			//_date = getTrimmedString(input, dateMatcher.start(), dateMatcher.end());
			//_time = input.replaceAll(_date, "").trim();
			_date = input;
		}
		
		_time = cleanString(_time);
		_date = cleanString(_date);
		
		DP.processDate(_date, false);
		TP.processTime(_time);
		
		if (isStart) {
			_startTime = TP.getStartTime();
			_startDate = DP.getStartDate();
		} else {
			_endTime = TP.getStartTime();
			_endDate = DP.getStartDate();
		}
	}
	
	//nid to take note of "7 days from now" kind of query, dont remove from, or recognise now
	private String cleanString(String input) {
		return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER, "").trim();
	}
	
	private String getTrimmedString(String input, int startIndex, int endIndex) {
		return input.substring(startIndex, endIndex).trim();
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
	
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}
	
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}
}
