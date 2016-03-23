package parser;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import common.TaskObject;
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
	
	private LocalDate startDate = LocalDate.MAX;
	private LocalDate endDate = LocalDate.MAX;
	private LocalTime startTime = LocalTime.MAX;
	private LocalTime endTime = LocalTime.MAX;
	private LocalDate untilDate = LocalDate.MAX;
	private LocalTime untilTime = LocalTime.MAX;
	
	private LocalDateTime startDateTime = LocalDateTime.MAX;
	private LocalDateTime endDateTime = LocalDateTime.MAX;
	private LocalDateTime untilDateTime = LocalDateTime.MAX;
	
	TaskObject TO = new TaskObject();
	List<String> dtlist = new ArrayList<String>();
	TaskType tasktype;
	
	private static Logger logger = Logger.getLogger("DateTimeParser");
	
	public TaskObject parse(String input, boolean isForAdd) throws Exception {
		parseDateTime(input, isForAdd);
		return TO;
	}
	
	/**
	 * method will take in string containing date and time, then splitting it into the date and time
	 * separately for parsing through dateparser and timeparser
	 * 
	 * @param input      user input in string format
	 * @param isForAdd   boolean to indicate if command is for the add parser
	 * @throws Exception 
	 */
	public void parseDateTime(String input, boolean isForAdd) throws Exception {
		if (isForAdd) {
			parseDateTimeForAdd(input);
		} else {
			tasktype = getTaskType(input);
			//separate stuff for different task types
			switch(tasktype) {
			case event:
				for (String temp : input.split("to")) {
					dtlist.add(temp);
				}
				separateDateTime(dtlist.get(0), "start");
				separateDateTime(dtlist.get(1), "end");
				break;
			case deadline:
			default:
				if(input.contains("to")) {
					endDateTime = startDateTime;
				}
				separateDateTime(input, "start");
				break;
			}
			setLocalDateTime(isForAdd, tasktype);
		}
	}
	
	
	/**
	 * method will parse date time string according to its task type
	 * 
	 * @param input   user's input in a string format
	 * @throws Exception 
	 */
	private void parseDateTimeForAdd(String input) throws Exception {
		tasktype = getTaskType(input);
		//separate stuff for different task types
		switch(tasktype) {
		case event:
			for (String temp : input.split("to")) {
				dtlist.add(temp);
			}
			separateDateTime(dtlist.get(0), "start");
			separateDateTime(dtlist.get(1), "end");
			break;
		case deadline:
			separateDateTime(input, "start");
			break;
		case recurring:
			TO.setIsRecurring(true);
			recur(input);
			break;
		default:
			break;
		}
		setLocalDateTime(true, tasktype);
	}
	
	/**
	 * Method will split string into the startdatetime, interval and until components of a recurring task identifier
	 * 
	 * @param input
	 * @throws Exception
	 */
	public void recur(String input) throws Exception {
		Pattern until = Pattern.compile(Constants.REGEX_RECURRING_UNTIL);
		Pattern interval = Pattern.compile(Constants.REGEX_RECURRING_INTERVAL);
		String intervalString=null;
		
		Matcher untilMatcher = until.matcher(input);
		if(untilMatcher.find()) {
			String untilstring = getTrimmedString(input, untilMatcher.start(), input.length());
			separateDateTime(untilstring, "until");
			input = input.replaceFirst(untilstring, "");
			untilDateTime = LocalDateTime.of(untilDate, untilTime);
		}
		
		Matcher intervalMatcher = interval.matcher(input);
		if(intervalMatcher.find()) {
			intervalString = getTrimmedString(input, intervalMatcher.start(), intervalMatcher.end());
			input = input.replaceFirst(intervalString, "").trim();
			parseInterval(intervalString);
			if (!input.isEmpty()) {
				parseDateTime(input,true);
			}
		}
		
		if (startDate == LocalDate.MAX || startDate.equals(LocalDate.now())) {
			getStartDateFromInterval(intervalString);
		}
	}
	
	/**
	 * method will use the frequency from interval to obtain start date
	 * if user does not input the start date
	 * 
	 * @param input  frequency from the user's input
	 * 			e.g. every 2 tuesday
	 * @throws Exception 
	 */
	public void getStartDateFromInterval(String input) throws Exception {
		input = input.replaceFirst("every","").trim();
		String _freq = "";
		int _interval = 1;
		if (input.contains(" ")) {
			String[] interval = input.split(" ");
			_interval = Integer.parseInt(interval[0]);
			_freq = interval[1];
		} else {
			_freq = input;
		}
		DateParser DP = new DateParser();
		DP.processDate(_freq);
		startDate = DP.getDateObject();
		if (tasktype.equals(TaskType.event)) {
			endDate = DP.getDateObject();	
		}
		
		setLocalDateTime(false, TaskType.event);
	}
	
	/**
	 * method breaks down string into the interval and frequency to be stored in
	 * task object's interval object.
	 * 
	 * @param input      interval and frequency from user's input 
	 * 			e.g. every 5 weeks
	 * @throws Exception
	 */
	public void parseInterval(String input) throws Exception {
		input = input.replaceFirst("every","").trim();
		String _freq;
		int _interval = 1;
		if (input.contains(" ")) {
			String[] interval = input.split(" ");
			_interval = Integer.parseInt(interval[0]);
			_freq = interval[1];
		} else {
			_freq = input;
		}
		setInterval(_interval, _freq);
	}
	
	/**
	 * method will take in string and identify regular expressions for time
	 * and date. 
	 * 
	 * Creates dateparser object and timeparser object
	 * to parse date and time respectively.
	 * 
	 * @param input		user's input containing date and time
	 * 			e.g. 7pm 9 june, tmr 9am
	 * @param isStart   type of time/date the user's input will be stored, either as start, end or until
	 * @throws Exception 
	 */
	public void separateDateTime(String input, String type) throws Exception {
		input = input.replaceFirst("until", "").trim();
		input = input.replaceFirst("from", "").trim();
		
		Pattern time = Pattern.compile(Constants.REGEX_TIME_FORMAT);
		Matcher timeMatcher = time.matcher(input);
		DateParser DP = new DateParser();
		TimeParser TP = new TimeParser();
		
		String _date = "", _time = "";
		
		if (timeMatcher.find()) {
			//logger.log(Level.INFO, "Time format found");
			_time = getTrimmedString(input, timeMatcher.start(), timeMatcher.end());
			_date = input.replaceAll(_time, "").trim();
			if (_date.matches("(start|end)")) {
				type = _date;
				_date = ""; 
			}
		} else {
			//logger.log(Level.INFO, "Time format NOT found");
			_date = input;
		}
	
		processParallel(DP, TP, _date, _time);
		setDateTime(type, DP, TP);
	}


	public void setDateTime(String type, DateParser DP, TimeParser TP) {
		if (type.matches("start")) {
			_startTime = TP.getTime();
			_startDate = DP.getStartDate();
			startT = TP.getTimeString();
			startD = DP.getDateString();
			startTime = TP.getTimeObject();
			startDate = DP.getDateObject();
		} else if (type.matches("until")) {
			_startDate = DP.getStartDate();
			untilTime = TP.getTimeObject();
			untilDate = DP.getDateObject();
		} else {
			_endTime = TP.getTime();
			_endDate = DP.getStartDate();
			endT = TP.getTimeString();
			endD = DP.getDateString();
			endTime = TP.getTimeObject();
			endDate = DP.getDateObject();
		}
	}

	
	public void processParallel(DateParser DP, TimeParser TP, String _date,
			String _time) throws Exception {
		_time = cleanString(_time);
		_date = cleanString(_date);
		DP.processDate(_date);
		TP.processTime(_time);
	}
	
	/**
	 * method will set appropriate localdatetime for the task object
	 * if the task is an event without end date, start date will be used
	 * 
	 * @param isForAdd  boolean show if the task is for an add command
	 * @param task      task type (deadline, event, floating or recurrent)
	 */
	public void setLocalDateTime(boolean isForAdd, TaskType task) {
		if (isForAdd) {
			if (task.toString() == "event" && endDate.equals(LocalDate.MAX)) { 
				if(startDate.equals(LocalDate.MAX)) {
					startDate = LocalDate.now();
				}
				endD = startD; //for special case of lazy ppl not typing end date
				endDate = startDate;
			}
			if (task.toString() == "event") {
				endDateTime = LocalDateTime.of(endDate, endTime);
			}
		}
		//setting the startdatetime and enddatetime
		endDateTime = LocalDateTime.of(endDate, endTime);
		startDateTime = LocalDateTime.of(startDate, startTime);
		TO.setStartDateTime(startDateTime);
		TO.setEndDateTime(endDateTime);
	}
	
	private void setInterval(int interval, String frequency) throws Exception {
		//read in freq
		if (frequency.matches(Constants.REGEX_DAYS_TEXT)
				|| frequency.matches("(week|wk)(s)?")) {
			frequency = "WEEKLY";
		} else if (frequency.matches("(year|yr)(s)?")) {
			frequency = "YEARLY";
		} else if (frequency.matches("(hour|hr)(s)?")) {
			frequency = "HOURLY";
		} else if (frequency.matches(Constants.REGEX_MONTHS_TEXT) 
				|| frequency.matches("(month|mth)(s)?")) {
			frequency = "MONTHLY";
		} else if (frequency.matches("(day)(s)?")) {
			frequency = "DAILY";
		}
		
		TO.getInterval().setTimeInterval(interval);
		TO.getInterval().setFrequency(frequency);
		TO.getInterval().setUntil(untilDateTime);
	}
	
	/**
	 * method checks string to identify the task type
	 * 
	 * @param input    user's input in string format
	 * 			e.g. by tmr 6pm (deadline)
	 * @return         appropriate task type for the input 
	 */
	public TaskType getTaskType(String input) {
		if (input.matches(Constants.REGEX_DEADLINE_IDENTIFIER)) {
			//logger.log(Level.INFO, "Deadline recognised");
			return TaskType.deadline;
		} else if (input.matches(Constants.REGEX_EVENT_IDENTIFIER)) {
			//logger.log(Level.INFO, "Event recognised");
			return TaskType.event;
		} else if (input.matches(Constants.REGEX_POINT_TASK_IDENTIFIER)) {
			//logger.log(Level.INFO, "Event recognised");
			return TaskType.deadline;
		} else if (input.matches(Constants.REGEX_RECURRING_TASK_IDENTIFIER)) {
			//logger.log(Level.INFO, "Recurring recognised");
			return TaskType.recurring;
		} else {
			//logger.log(Level.INFO, "Floating recognised");
			return TaskType.floating;
		}
	}
	
	//nid to take note of "7 days from now" kind of query, dont remove from, or recognise now
	private String cleanString(String input) {
		if (input.contains("today") || input.contains("tomorrow")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_2, "").trim();
		} else {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER, "").trim();	
		}
	}
	
	//extract string and trims out whitespace
	private String getTrimmedString(String input, int startIndex, int endIndex) {
		return input.substring(startIndex, endIndex).trim();
	}
	
	
	//getters!!!!
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
	
	public void reset() {
		startDateTime = LocalDateTime.MAX;
		endDateTime = LocalDateTime.MAX;
	}
	
}
