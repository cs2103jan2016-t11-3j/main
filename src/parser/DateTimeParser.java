package parser;

import common.AtfLogger;
import common.TaskObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	
	private LocalDate startDate = LocalDate.MAX;
	private LocalDate endDate = LocalDate.MAX;
	private LocalTime startTime = LocalTime.MAX;
	private LocalTime endTime = LocalTime.MAX;
	private LocalDate untilDate = LocalDate.MAX;
	private LocalTime untilTime = LocalTime.MAX;
	
	private LocalDateTime startDateTime = LocalDateTime.MAX;
	private LocalDateTime endDateTime = LocalDateTime.MAX;
	private LocalDateTime untilDateTime = LocalDateTime.MAX;
	
	private TaskObject TO = new TaskObject();
	List<String> dtlist = new ArrayList<String>();
	TaskType tasktype;
	
	private static Logger logger = AtfLogger.getLogger();
	
	public TaskObject parse(String input, boolean isForAdd) throws Exception {
		parseDateTime(input, isForAdd);
		setLocalDateTime(isForAdd, tasktype);
		return TO;
	}
	
	/**
	 * method will take in string containing date and time, then splitting it into the date and time
	 * separately for parsing through dateparser and timeparser
	 * 
	 * @param input      
	 * 			user input in string format
	 * @param isForAdd   
	 * 			boolean to indicate if command is for the add parser
	 * @throws Exception 
	 */
	public void parseDateTime(String input, boolean isForAdd) throws Exception {
		if (isForAdd) {
			parseDateTimeForAdd(input);
		} else {
			parseDateTimeOthers(input);
		}
	}

	/**
	 * method will parse date time string according to its task type
	 * 
	 * @param input   
	 * 			user's input in a string format
	 * @throws Exception 
	 */
	public void parseDateTimeOthers(String input) throws Exception {
		tasktype = getTaskType(input);
		//separate stuff for different task types
		switch(tasktype) {
		case event :
			String temp1 = "", temp2 = "";
			Pattern split = Pattern.compile(" to ");
			Matcher matcher = split.matcher(input);
			if (matcher.find()) {
				temp1 = getTrimmedString(input, 0, matcher.start());
				temp2 = getTrimmedString(input, matcher.end(), input.length());
			}
			
			separateDateTime(temp1, "start");
			separateDateTime(temp2, "end");
			break;
		case recurring :
			TO.setIsRecurring(true);
			recur(input);
			break;
		case deadline :
		default :
			if(input.contains("to")) {
				endDateTime = startDateTime;
			}
			separateDateTime(input, "start");
			break;
		}
	}
	
	
	/**
	 * method will parse date time string according to its task type
	 * 
	 * @param input   
	 * 			user's input in a string format
	 * @throws Exception 
	 */
	private void parseDateTimeForAdd(String input) throws Exception {
		tasktype = getTaskType(input);
		//separate stuff for different task types
		switch(tasktype) {
		case event:
			String temp1 = "", temp2 = "";
			Pattern split = Pattern.compile(" to ");
			Matcher matcher = split.matcher(input);
			if (matcher.find()) {
				temp1 = getTrimmedString(input, 0, matcher.start());
				temp2 = getTrimmedString(input, matcher.end(), input.length());
			}
			
			separateDateTime(temp1, "start");
			separateDateTime(temp2, "end");
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
	}
	
	/**
	 * Method will split string into the startdatetime, interval and limiter components of a recurring task identifier
	 * Limiter refers to an "until" or "for" restriction to specify the expiry of the task
	 * 
	 * @param input
	 * 				string input for recurring tasks with interval, startdatetime and a limiter
	 * @throws Exception
	 */
	public void recur(String input) throws Exception {
		String intervalString = "";
		String forstring = "";
		
		input = extractUntilLimiter(input);
		
		forstring = extractForLimiter(input);
		input = input.replaceFirst(forstring, "");
		
		intervalString = extractInterval(input, forstring);
		
		if (startDate == LocalDate.MAX) { // checks for startdatetime without date
			getStartDateFromInterval(intervalString);
		}
	}

	/**
	 * method searches for the regular expression representing end date for
	 * recurring tasks that starts with "until"
	 * 
	 * @param input
	 * 				string for recurring tasks input. not null.
	 * @return
	 * 				remaining string without until-string. not null.
	 * @throws Exception
	 */
	public String extractUntilLimiter(String input) throws Exception {
		String untilstring;
		Pattern until = Pattern.compile(Constants.REGEX_RECURRING_UNTIL);
		Matcher untilMatcher = until.matcher(input);
		if (untilMatcher.find()) {
			untilstring = getTrimmedString(input, untilMatcher.start(), input.length());
			separateDateTime(untilstring, "until"); //only run this if its until
			input = input.replaceFirst(untilstring, "");
			untilDateTime = LocalDateTime.of(untilDate, untilTime); //only run this if its until
		}
		return input;
	}

	/**
	 * method searches for the regular expression representing end date for 
	 * recurring tasks that starts with "for"
	 * 
	 * @param input
	 * 				 string for recurring tasks input. not null.
	 * @param forstring
	 * 				for-string, contains "for" keyword and regular expression for number of intervals. 
	 * 				null value here returns unchanged input
	 * @return
	 * 				remaining string without for-string, not null
	 */
	public String extractForLimiter(String input) {
		String forstring = "";
		Pattern multiplier = Pattern.compile(Constants.REGEX_RECURRING_FOR);
		Matcher forMatcher = multiplier.matcher(input);
		if (forMatcher.find()) {
			forstring = getTrimmedString(input, forMatcher.start(), input.length());
		}
		return forstring;
	}
	
	/**
	 * method extracts interval string from input. parses start date time string to process start date.
	 * calls parseFor to process forstring's count and interval. 
	 * 
	 * @param input
	 * 				non-null string containing interval and startdatetime
	 * @param forstring
	 * 				"for" limiter. null if original input has no "for" limit specified
	 * @return
	 * 				interval string containing number and frequency of task. not null
	 * @throws Exception
	 */
	public String extractInterval(String input, 
			String forstring) throws Exception {
		String intervalString = "";
		Pattern interval = Pattern.compile(Constants.REGEX_RECURRING_INTERVAL2);
		Matcher intervalMatcher = interval.matcher(input);
		if (intervalMatcher.find()) {
			intervalString = getTrimmedString(input, intervalMatcher.start(), intervalMatcher.end());
			input = input.replaceFirst(intervalString, "").trim();
			parseInterval(intervalString);
			
			if (!input.isEmpty()) {
				parseDateTime(input,true);
			}
			
			if (!forstring.isEmpty()) {
				parseFor(forstring);	
			}
		}
		return intervalString;
	}

	
	/**
	 * method calculates count for the recurring tasks to be repeated
	 * 
	 * @param input
	 * 				contains number of repetition (integer, after parsing) and length of each occurrence (string)
	 * @throws Exception
	 */
	private void parseFor(String input) throws Exception {//for x weeks
		int numberOf = 0, multiplier = 0, count = 0;
		String forFreq = "";
		String[] temp = input.split(" ");
		
		if (hasNumber(temp[1])) {
			numberOf = Integer.parseInt(temp[1]);
			forFreq = temp[2];	
		} else {
			throw new Exception("Invalid count");
		}
		
		if (forFreq.matches("times")) {
			multiplier = 1;
		} else {
			multiplier = getMultiplier(forFreq, TO.getInterval().getFrequency());	
		}
		
		count = multiplier * numberOf * TO.getInterval().getTimeInterval();
		TO.getInterval().setCount(count);
	}
	
	/**
	 * method returns multiplier for calculating the task's interval count
	 * for example, every day for 1 month will return 30 as there are 30 days in a month on average
	 * 
	 * @param forInput
	 * 				date frequency such as weeks or months. not null
	 * @param intervalInput
	 * 				date frequency such as weeks or months, not null
	 * @return
	 * 				multiplier (integer) based on the relationship between intervalinput and forinput
	 * @throws Exception
	 */
	private int getMultiplier(String forInput, String intervalInput) throws Exception {
		forInput = getFormattedFrequency(forInput);
		if (forInput.matches("DAILY") && intervalInput.matches("DAILY")) {
			return 1;
		} else if (forInput.matches("WEEKLY") && intervalInput.matches("DAILY")) {
			return 7;
		} else if (forInput.matches("MONTHLY") && intervalInput.matches("DAILY")) {
			return 31;
		} else if (forInput.matches("YEARLY") && intervalInput.matches("DAILY")) {
			return 365;
		} else if (forInput.matches("WEEKLY") && intervalInput.matches("WEEKLY")) {
			return 1;
		} else if (forInput.matches("MONTHLY") && intervalInput.matches("WEEKLY")) {
			return 4;
		} else if (forInput.matches("MONTHLY") && intervalInput.matches("MONTHLY")) {
			return 1;
		} else if (forInput.matches("YEARLY") && intervalInput.matches("MONTHLY")) {
			return 12;
		} else if (forInput.matches("YEARLY") && intervalInput.matches("YEARLY")) {
			return 1;
		} else {
			throw new Exception("Invalid date input, recurrence larger than interval"); //improve engrish here 
		}
		
	}
	
	/**
	 * method will use the frequency from interval to obtain start date
	 * if user does not input the start date
	 * 
	 * @param input  
	 * 			frequency from the user's input
	 * 			e.g. every 2 tuesday
	 * @throws Exception 
	 */
	public void getStartDateFromInterval(String input) throws Exception {
		String _freq = "";
		int _interval = 1;
		if (!input.matches(Constants.REGEX_RECURRING_INTERVAL_EVERYDAY)) {
			input = input.replaceFirst("every","").trim();
			if (input.contains(" ") && hasNumber(input)) { // starts with a number?
				String[] interval = input.split(" ");
				_interval = Integer.parseInt(interval[0]);
				_freq = interval[1];
			} else if (input.contains(" ")) {
				String[] interval = input.split(" ");
				_freq = interval[0];
			} else {
				_freq = input;
			}
		} else {
			_freq = input;
		}
		
		_freq = cleanString(_freq);
		DateParser DP = new DateParser();
		if (_freq.matches(Constants.REGEX_DAYS_TEXT) 
				|| _freq.matches(Constants.REGEX_RECURRING_INTERVAL_EVERYDAY)) {
			DP.parseDate(_freq);
		}

		startDate = DP.getDateObject();
	
		if (tasktype.equals(TaskType.event)) {
			endDate = DP.getDateObject();	
		}	
	}
	
	private boolean hasNumber(String input) {
		String[] interval = input.split(" ");
		if (interval[0].matches("[\\d]+")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * method breaks down string into the interval and frequency to be stored in
	 * task object's interval object.
	 * 
	 * @param input      
	 * 			interval and frequency from user's input 
	 * 			e.g. every 5 weeks
	 * @throws Exception
	 */
	public void parseInterval(String input) throws Exception {
		input = input.replaceFirst("every","").trim();
		String _freq = "";
		int _interval = 1;
		if (input.contains("and") || input.contains("&") || input.contains(",")) {
			setDaysInWeek(input);
			_freq = "week";
		} else if (input.contains(" ")) { //assuming interval then frequency 
			String[] interval = input.split(" ");
			_interval = Integer.parseInt(interval[0]);
			_freq = interval[1];
		} else {
			_freq = input;
		}
		setInterval(_interval, _freq);
	}
	
	/**
	 * method sets byDay array in interval object according to the days that are present
	 * 
	 * @param input
	 * 				string containing days which the task falls on. not null 
	 * 
	 */
	public void setDaysInWeek(String input) { //monday and tuesday ??
		//wanna read using comma?
		input = input.toLowerCase();
		if (input.contains("mon") || input.contains("monday")) {
			TO.getInterval().setByDay(1);
		}
		if (input.contains("tue") || input.contains("tues") 
				|| input.contains("tuesday")) {
			TO.getInterval().setByDay(2);
		}
		if (input.contains("wed") || input.contains("wednesday")) {
			TO.getInterval().setByDay(3);
		} 
		if (input.contains("thur") || input.contains("thurs") 
				|| input.contains("thursday")) {
			TO.getInterval().setByDay(4);
		} 
		if (input.contains("fri") || input.contains("friday")) {
			TO.getInterval().setByDay(5);
		}
		if (input.contains("sat") || input.contains("saturday")) {
			TO.getInterval().setByDay(6);
		}
		if (input.contains("sun") || input.contains("sunday")) {
			TO.getInterval().setByDay(7);
		}
	}
	
	private void setInterval(int interval, String frequency) throws Exception {
		frequency = getFormattedFrequency(frequency);
		TO.getInterval().setTimeInterval(interval);
		TO.getInterval().setFrequency(frequency);
		TO.getInterval().setUntil(untilDateTime);
	}

	public String getFormattedFrequency(String frequency) {
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
		return frequency;
	}
	
	/**
	 * method will take in string and identify regular expressions for time
	 * and date. 
	 * 
	 * Creates dateparser object and timeparser object
	 * to parse date and time respectively.
	 * 
	 * @param input		
	 * 			user's input containing date and time
	 * 			e.g. 7pm 9 june, tmr 9am
	 * @param isStart   
	 * 			type of time/date the user's input will be stored, either as start, end or until
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
			startTime = TP.getTimeObject();
			startDate = DP.getDateObject();
		} else if (type.matches("until")) {
			untilTime = TP.getTimeObject();
			untilDate = DP.getDateObject();
		} else {
			endTime = TP.getTimeObject();
			endDate = DP.getDateObject();
		}
	}

	
	public void processParallel(DateParser DP, TimeParser TP, String _date,
			String _time) throws Exception {
		_time = cleanString(_time);
		_date = cleanString(_date);
		DP.parseDate(_date);
		TP.processTime(_time);
	}
	
	/**
	 * method will set appropriate localdatetime for the task object
	 * if the task is an event without end date, start date will be used
	 * 
	 * @param isForAdd  
	 * 				boolean show if the task is for an add command
	 * @param task      
	 * 				task type (deadline, event, floating)
	 */
	public void setLocalDateTime(boolean isForAdd, TaskType task) {
		if (isForAdd) {
			if (startDate.equals(LocalDate.MAX) && task.toString().equals("deadline")) {
				startDate = LocalDate.now();
			}
			if (task.toString() == "event" && endDate.equals(LocalDate.MAX)) { 
				if(startDate.equals(LocalDate.MAX)) {
					startDate = LocalDate.now();
				}
				//endD = startD; //for special case of lazy ppl not typing end date
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
	
	
	
	/**
	 * method checks string to identify the task type
	 * 
	 * @param input    
	 * 			user's input in string format
	 * 			e.g. by tmr 6pm (deadline)
	 * @return         
	 * 			appropriate task type for the input 
	 */
	public TaskType getTaskType(String input) {
		if (input.matches(Constants.REGEX_DEADLINE_IDENTIFIER)) {
			logger.log(Level.INFO, "Deadline recognised");
			return TaskType.deadline;
		} else if (input.matches(Constants.REGEX_EVENT_IDENTIFIER)) {
			logger.log(Level.INFO, "Event recognised");
			return TaskType.event;
		} else if (input.matches(Constants.REGEX_POINT_TASK_IDENTIFIER)) {
			logger.log(Level.INFO, "Event recognised");
			return TaskType.deadline;
		} else if (input.matches(Constants.REGEX_RECURRING_TASK_IDENTIFIER)) {
			logger.log(Level.INFO, "Recurring recognised");
			return TaskType.recurring;
		} else {
			logger.log(Level.INFO, "Floating recognised");
			return TaskType.floating;
		}
	}
	
	//nid to take note of "7 days from now" kind of query, dont remove from, or recognise now
	private String cleanString(String input) {
		input = input.replaceAll("[,]+", "").trim();
		if (input.contains("today") || input.contains("tomorrow")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_2, "").trim(); //trim specially
		} else if (input.contains("tonight")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_6, "").trim(); //trim specially
		} else if (input.contains("saturday") || input.contains("sat")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_3, "").trim();
		} else if (input.contains("mon") || input.contains("monday")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_4, "").trim();
		} else if (input.contains("everyday")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_5,"").trim();
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
		startDate = LocalDate.MAX;
		endDate = LocalDate.MAX;
		startTime = LocalTime.MAX;
		endTime = LocalTime.MAX;
		untilDate = LocalDate.MAX;
		untilTime = LocalTime.MAX;
		startDateTime = LocalDateTime.MAX;
		endDateTime = LocalDateTime.MAX;
		untilDateTime = LocalDateTime.MAX;
	}
	
}
