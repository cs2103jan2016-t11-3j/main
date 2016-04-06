//@@author A0125003A
package parser;

import common.AtfLogger;
import common.TaskObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import parser.Constants.TaskType;


/**
 * This class is the facade class that hides the DateParser and TimeParser from the 
 * CommandParsers. Its main functionality is to:
 * a) split string into date and time to be parsed into DateParser and TimeParser respectively
 * b) recognise recurring tasks and process the interval and terminal date-time
 * c) set LocalDateTime variables in TaskObject to be returned to CommandParser
 * 
 * Does nothing if no recognizable tasktype is parsed in.
 * 
 * @author sylvesterchin
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
	
	/**
	 * This method is the only method that CommandParser will access. It parses 
	 * date and time as well as sets LocalDateTime.
	 * 
	 * @param input
	 * 				user's input consisting only date-time components. not null.
	 * @param isForAdd
	 * 				boolean to signify if called by AddParser
	 * @return
	 * 				task object with LocalDateTime and Interval object filled
	 * @throws Exception
	 */
	public TaskObject parse(String input, boolean isForAdd) throws Exception {
		parseDateTime(input, isForAdd);
		setLocalDateTime(isForAdd, tasktype);
		return TO;
	}
	
	// ================================
	// First Level of Abstraction
	// ================================
	
	/**
	 * This method will take in string containing date and time, then splitting it into the date and time
	 * separately for parsing through DateParser and TimeParser.
	 * 
	 * @param input      
	 * 			user input in string format
	 * @param isForAdd   
	 * 			boolean to indicate if command is for the add parser
	 * @throws Exception 
	 */
	private void parseDateTime(String input, boolean isForAdd) throws Exception {
		if (isForAdd) {
			parseDateTimeForAdd(input);
		} else {
			parseDateTimeOthers(input);
		}
	}
	
	/**
	 * This method will set appropriate localdatetime for the task object
	 * if the task is an event without end date, start date will be used
	 * 
	 * @param isForAdd  
	 * 				boolean show if the task is for an add command
	 * @param task      
	 * 				task type (deadline, event, floating)
	 */
	private void setLocalDateTime(boolean isForAdd, TaskType task) {
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
	
	// ================================
	// Second Level of Abstraction
	// ================================
	
	
	/**
	 * This method will parse date time string according to its task type
	 * 
	 * @param input   
	 * 			user's input in a string format
	 * @throws Exception 
	 */
	private void parseDateTimeOthers(String input) throws Exception {
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
	 * This method will parse date time string according to its task type
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
	
	// ================================
	// Third Level of Abstraction
	// ================================
	
	/**
	 * This method will split string into the StartDateTime, interval and limiter components of a recurring task identifier
	 * Limiter refers to an "until" or "for" restriction to specify the expiry of the task
	 * 
	 * @param input
	 * 				string input for recurring tasks with interval, StartDateTime and a limiter
	 * @throws Exception
	 */
	private void recur(String input) throws Exception {
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
	 * This method will take in string and identify regular expressions for time
	 * and date. 
	 * 
	 * Creates DateParser object and TimeParser object
	 * to parse date and time respectively.
	 * 
	 * @param input		
	 * 			user's input containing date and time
	 * 			e.g. 7pm 9 june, tmr 9am
	 * @param isStart   
	 * 			type of time/date the user's input will be stored, either as start, end or until
	 * @throws Exception 
	 */
	private void separateDateTime(String input, String type) throws Exception {
		input = input.replaceFirst("until", "").trim();
		input = input.replaceFirst("from", "").trim();
		
		Pattern time = Pattern.compile(Constants.REGEX_TIME_FORMAT);
		Matcher timeMatcher = time.matcher(input);
		DateParser DP = new DateParser();
		TimeParser TP = new TimeParser();
		
		String _date = "", _time = "";
		
		if (timeMatcher.find()) {
			logger.log(Level.INFO, "Time format found");
			_time = getTrimmedString(input, timeMatcher.start(), timeMatcher.end());
			_date = input.replaceAll(_time, "").trim();
			if (_date.matches("(start|end)")) {
				type = _date;
				_date = ""; 
			}
		} else {
			logger.log(Level.INFO, "Time format NOT found");
			_date = input;
		}
	
		processParallel(DP, TP, _date, _time);
		setDateTime(type, DP, TP);
	}
	
	/**
	 * This method checks string to identify the task type
	 * 
	 * @param input    
	 * 			user's input in string format
	 * 			e.g. by tmr 6pm (deadline)
	 * @return         
	 * 			appropriate task type for the input 
	 */
	private TaskType getTaskType(String input) {
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
	
	
	// ================================
	// Fourth Level of Abstraction
	// ================================
	
	
	/**
	 * This method searches for the regular expression representing end date for
	 * recurring tasks that starts with "until".
	 * 
	 * @param input
	 * 				string for recurring tasks input. not null.
	 * @return
	 * 				remaining string without until-string. not null.
	 * @throws Exception
	 */
	private String extractUntilLimiter(String input) throws Exception {
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
	 * This method searches for the regular expression representing end date for 
	 * recurring tasks that starts with "for".
	 * 
	 * @param input
	 * 				 string for recurring tasks input. not null.
	 * @param forstring
	 * 				for-string, contains "for" keyword and regular expression for number of intervals. 
	 * 				null value here returns unchanged input
	 * @return
	 * 				remaining string without for-string, not null
	 */
	private String extractForLimiter(String input) {
		String forstring = "";
		Pattern multiplier = Pattern.compile(Constants.REGEX_RECURRING_FOR);
		Matcher forMatcher = multiplier.matcher(input);
		if (forMatcher.find()) {
			forstring = getTrimmedString(input, forMatcher.start(), input.length());
		}
		return forstring;
	}
	
	/**
	 * This method extracts interval string from input. parses start date time string to process start date.
	 * calls parseFor to process forstring's count and interval. 
	 * 
	 * @param input
	 * 				non-null string containing interval and StartDateTime
	 * @param forstring
	 * 				"for" limiter. null if original input has no "for" limit specified
	 * @return
	 * 				interval string containing number and frequency of task. not null
	 * @throws Exception
	 */
	private String extractInterval(String input, 
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
	 * This method will use the frequency from interval to obtain start date
	 * if user does not input the start date.
	 * 
	 * @param input  
	 * 			frequency from the user's input
	 * 			e.g. every 2 tuesday
	 * @throws Exception 
	 */
	private void getStartDateFromInterval(String input) throws Exception {
		String _freq = "";
		
		_freq = readFirstDayFromInterval(input);
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
	
	//extract string and trims out whitespace
	private String getTrimmedString(String input, int startIndex, int endIndex) {
		return input.substring(startIndex, endIndex).trim();
	}
	
	
	private void setDateTime(String type, DateParser DP, TimeParser TP) {
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
	
	private void processParallel(DateParser DP, TimeParser TP, String _date,
			String _time) throws Exception {
		_time = cleanString(_time);
		_date = cleanString(_date);
		DP.parseDate(_date);
		TP.processTime(_time);
	}
	
	// ================================
	// Fifth Level of Abstraction
	// ================================
	
	/**
	 * This method breaks down string into the interval and frequency to be stored in
	 * task object's interval object.
	 * 
	 * @param input      
	 * 			interval and frequency from user's input 
	 * 			e.g. every 5 weeks
	 * @throws Exception
	 */
	private void parseInterval(String input) throws Exception {
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
	 * This method calculates count for the recurring tasks to be repeated
	 * 
	 * @param input
	 * 				contains number of repetition (integer, after parsing) 
	 * 				and length of each occurrence (string)
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
		
		count = multiplier * numberOf / TO.getInterval().getTimeInterval();
		TO.getInterval().setCount(count);
	}
	
	/**
	 * This method will look for the first day in the interval string that corresponds
	 * to a day of the week.
	 * 
	 * @param input 
	 * 				interval string. non null.
	 * @return
	 * 				returns day-of=the-week or "everyday" to be parsed by DateParser
	 */
	private String readFirstDayFromInterval(String input) {
		String _freq;
		//int _interval;
		if (!input.matches(Constants.REGEX_RECURRING_INTERVAL_EVERYDAY)) {
			input = input.replaceFirst("every","").trim();
			if (input.contains(" ") && hasNumber(input)) { // starts with a number?
				String[] interval = input.split(" ");
				//_interval = Integer.parseInt(interval[0]);
				_freq = interval[1];
			} else if (input.contains(" ")) {
				//String[] interval = input.split(" ");
				_freq = getNextNearestDayInInterval();
				//_freq = interval[0];
			} else {
				_freq = input;
			}
		} else {
			_freq = input;
		}
		return _freq;
	}
	
	/**
	 * This method removes keywords for identifying task. It takes into account for 
	 * special exceptions such as today, monday and words containing identifier keywords.
	 * 
	 * @param input
	 * 				any general string. non-null.
	 * @return
	 */
	private String cleanString(String input) {
		input = input.replaceAll("[,]+", "").trim();
		if (input.contains("today") || input.contains("tomorrow")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_2, "").trim(); //trim specially
		} else if (input.contains("tonight")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_6, "").trim(); //trim specially
		} else if (input.contains("saturday") || input.contains("sat")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_3, "").trim();
		} else if (input.contains("mon") || input.contains("monday")) {
			input = input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_4, "").trim();
			if (input.startsWith("on")) {
				return input.replaceFirst("on", "").trim();
			}
			return input;
		} else if (input.contains("everyday")) {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER_5,"").trim();
		} else {
			return input.replaceAll(Constants.REGEX_TASK_IDENTIFIER, "").trim();	
		}
	}
	
	// ================================
	// Sixth Level of Abstraction
	// ================================
	
	/**
	 * This method sets byDay array in interval object according to the days that are present
	 * 
	 * @param input
	 * 				string containing days which the task falls on. not null 
	 * 
	 */
	private void setDaysInWeek(String input) { //monday and tuesday ??
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
	
	/**
	 * This method checks if the string starts with a number.
	 * 
	 * @param input
	 * 				date input with interval such as a day of the week, "weeks" or "months" 
	 * 				with integer in front as a multiplier
	 * @return
	 */
	private boolean hasNumber(String input) {
		String[] interval = input.split(" ");
		if (interval[0].matches("[\\d]+")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method returns multiplier for calculating the task's interval count
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
			return 31; //or 30?
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
	
	private String getNextNearestDayInInterval() {
		int start = 0;
		int now = LocalDate.now().getDayOfWeek().getValue();
		
		for (int i = now; i < TO.getInterval().getByDayArray().length ; i++) {
			if (TO.getInterval().getByDayArray()[i] == 1) {
				start = i;
				break;
			}
		}
		
		if (start == 7 && TO.getInterval().getByDayArray()[7] == 0) {
			//get from behind the now
			for (int i = 1; i < now ; i++) {
				if (TO.getInterval().getByDayArray()[i] == 1) {
					start = i;
					break;
				}
			}
		}
		
		return getDayInWeek(start);
	}
	

	// ================================
	// Seventh Level of Abstraction
	// ================================
	
	private String getDayInWeek(int index) {
		if (index == 1) {
			return "monday";
		} else if (index == 2) {
			return "tuesday";
		} else if (index == 3) {
			return "wednesday";
		} else if (index == 4) {
			return "thursday";
		} else if (index == 5) {
			return "friday";
		} else if (index == 6) {
			return "saturday";
		} else if (index == 7) {
			return "sunday";
		} else {
			return "";
		}
	}
	
	private String getFormattedFrequency(String frequency) {
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
