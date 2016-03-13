package parser;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

public class DateTimeParser {
	
	private int _startTime = -1;
	private int _endTime = -1;
	private int _startDate = -1;
	private int _endDate = -1;
	
	private String startT;
	private String endT;
	private String startD;
	private String endD;
	
	private LocalDate startDate = null;
	private LocalDate endDate = null;
	private LocalTime startTime = null;
	private LocalTime endTime = null;
	
	private LocalDateTime startDateTime = null;
	private LocalDateTime endDateTime = null;
	
	
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
			setLocalDateTime(isForAdd, tasktype);
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
		setLocalDateTime(false, tasktype);
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
		
		DateParser DP = new DateParser();
		TimeParser TP = new TimeParser();
		
		String _date = "", _time = "";
		
		if (timeMatcher.find()) {
			_time = getTrimmedString(input, timeMatcher.start(), timeMatcher.end());
			_date = input.replaceAll(_time, "").trim();
		} else {
			_date = input;
		}
		
		_time = cleanString(_time);
		_date = cleanString(_date);
		
		DP.processDate(_date, false);
		TP.processTime(_time);
		
		if (isStart) {
			_startTime = TP.getTime();
			_startDate = DP.getStartDate();
			startT = TP.getTimeString();
			startD = DP.getDateString();
		} else {
			_endTime = TP.getTime();
			_endDate = DP.getStartDate();
			endT = TP.getTimeString();
			endD = DP.getDateString();
		}
	}
	
	/**
	 * 
	 */
	public void setLocalDateTime(boolean isForAdd, TaskType task) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		
		if (isForAdd) {
			if (task.toString() == "event" && _endDate == -1) { 
				endD = startD; //for special case of lazy ppl not typing end date
			}
			startDate = LocalDate.parse(startD, dateFormatter);
			startTime = LocalTime.parse(startT, timeFormatter);
			if(task.toString() == "event") {
				endDate = LocalDate.parse(endD, dateFormatter);
				endTime = LocalTime.parse(endT, timeFormatter);
				endDateTime = LocalDateTime.of(endDate, endTime);
			}
		} else {
			setOtherLocalDateTime(timeFormatter, dateFormatter); //for searches and edits
			}
		startDateTime = LocalDateTime.of(startDate, startTime);
	}
	
	public void setOtherLocalDateTime(DateTimeFormatter timeF, DateTimeFormatter dateF) {
		if (_startDate == -1 && _startTime != -1) { // set weird date
			startDate = LocalDate.MAX;
			startTime = LocalTime.parse(startT, timeF);
		} else if (_startDate != -1 && _startTime == -1) { //set weird time
			startTime = LocalTime.MAX;
			startDate = LocalDate.parse(startD, dateF);
		} else {
			startDate = LocalDate.parse(startD, dateF);
			startTime = LocalTime.parse(startT, timeF);
		}
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
