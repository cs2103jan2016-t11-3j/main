package parser;

import common.TaskObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a child class of CommandParser. It takes in a string and checks if
 * it is searching for a category, recurring dates, by status or by the standard attributes (
 * task, date and time). Creates a Task Object to be returned to the Parser object. 
 * 
 * @author sylvesterchin
 *
 */
public class SearchParser extends CommandParser {
	private TaskObject TO = new TaskObject();
	private int index=-1;
	
	/**
	 * This method checks if the search command is looking for category, recurring dates, status,
	 * task, date or time and calls the appropriate function.
	 * 
	 * @param input
	 * 				search command, non-null 
	 * 				
	 */
	public TaskObject process(String input) throws Exception {
		input = removeSearchKeyword(input);
		if (isSearchByCategory(input)) {
			setCategory(input);
		} else if (isSearchForRecurringDates(input)) {
			setIndex(input);
		} else if (isSearchByStatus(input)) {
			setStatus(input);
		} else {
			searchTaskDateTime(input);
		}
		return TO;
	}

	/**
	 * This method will look for date-time pattern and call the DateTimeParser if needed.
	 * Also, it sets the task description if that is the user's input.
	 * 
	 * @param input
	 * 				string input for search command, non-null
	 * @throws Exception
	 */
	private void searchTaskDateTime(String input) throws Exception {
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
		}
		
		_task = input;

		setTaskObject();
	}
	
	
	private boolean isSearchByStatus(String input) {
		return input.matches("(?i)(done|completed)");
	}
	
	private void setStatus(String input){
		TO.setStatus("completed");
	}
	
	private boolean isSearchForRecurringDates(String input) {
		return input.matches("[\\d]+");
	}
	
	private void setIndex(String input) {
		index = Integer.parseInt(input);
	}
	
	private boolean isSearchByCategory(String input) {
		return input.matches("(floating|deadline|event)");
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
	
	private void setTaskObject() {
		TO.setTitle(_task);
	}
	
	public String removeSearchKeyword(String input) {
		return input.replaceFirst("search ", "").trim();
	}
	
	public String getTask() {
		return _task;
	}
	
	
 	
 	public void resetAll() {
 		_task = null;
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
