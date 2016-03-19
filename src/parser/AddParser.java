package parser;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import common.TaskObject;


//TODO -> set task type

public class AddParser extends CommandParser {
	
	public ArrayList<String> list = new ArrayList<String>();
	public ArrayList<Integer> dateList = new ArrayList<Integer>();
	
	private TimeParser TP = new TimeParser();
	private DateParser DP = new DateParser();
	
	public TaskObject TO = new TaskObject();
	
	/**
	 * this method will take in the command and allocate the correct information
	 * to task/date/time
	 * 
	 * @param input    string input from user
	 * @throws Exception 
	 */
	public TaskObject process(String input) throws Exception {
		setTask(input);
		setTaskObject();
		return TO;
	}
	
	/**
	 * method will split string into task and date-time. Calls datetimeparser for
	 * date-time string and sets task in AddParser class
	 * 
	 * @param input
	 * @throws Exception
	 */
	public void setTask(String input) throws Exception {
		Pattern dateTimePattern = Pattern.compile(Constants.REGEX_DATE_TIME_IDENTIFIER);
		Matcher matcher = dateTimePattern.matcher(input);
		
		String identifier = null;
		
		if (matcher.find()) {
			identifier = getTrimmedString(input ,matcher.start(), matcher.end());
			input = getTrimmedString(input, 0, matcher.start());
		}
		
		if (identifier != null) {
			DateTimeParser dtp = new DateTimeParser();
			dtp.parseDateTime(identifier, true);
			setDateTime(dtp);
        }
		
		_task = input;
	}
	
	
	private void setDateTime(DateTimeParser dtp) {
		_startDate = dtp.getStartDate();
		_endDate = dtp.getEndDate();
		_startTime = dtp.getStartTime();
		_endTime = dtp.getEndTime();
		_startDateTime = dtp.getStartDateTime();
		_endDateTime = dtp.getEndDateTime();
	}
	
	
	private void setTaskObject() {
		TO.setTitle(_task);
		TO.setStartDateTime(_startDateTime);
		TO.setEndDateTime(_endDateTime);
		TO.setStartTime(_startTime);
		TO.setEndTime(_endTime);
		TO.setEndDate(_endDate);
		TO.setStartDate(_startDate);
		TO.setStatus("incomplete");
	}
 	
 	
 	//Getters 
 	
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
 	
 	public void reset() {
 		_task = null;
 		_startDate = -1;
 		_endDate = -1;
 		_startTime = -1;
 		_endTime = -1;
 	}
 	
 	public void clearDP() {
 		DP.clearList();
 		DP.resetDate();
 	}
 	
 	public void clearTP() {
 		TP.clearList();
 		TP.resetTime();
 	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

}
