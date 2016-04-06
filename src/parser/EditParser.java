//@@author A0125003A
package parser;

import common.TaskObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is the child class of CommandParser. It's public method, process(), will
 * split the string into two components, "task" and "date-time" if either are present. 
 * EditParser will call DateTimeParser to process the DateTime string. EditParser will 
 * also identify the index for the task to be edited. 
 * 
 * @author sylvesterchin
 */
public class EditParser extends CommandParser {
	
	private ArrayList<String> list = new ArrayList<String>();

	private TaskObject TO = new TaskObject();

	
	private int _index = -1;
	
	/**
	 * This method will take in the string from the parser and break down its component, 
	 * determining if it is a task, time or date edit.
	 * 
	 * @param input
	 * 				user's input for editing task in the list. non-null. contains index number and
	 * 				attributes to edit
	 * 				
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
			TO = dtp.parse(identifier, false);
        }
		
		_task = input;
		setTaskObject();
		return TO;
	}
	
	private void setTaskObject() {
		TO.setTitle(_task);
	}
	
	/**
	 * This method will convert instruction into string array list
	 * and remove the "edit" and number
	 * 
	 * @param input
	 * 				string for edit command. non-null.
	 */
	public void convertToArray(String input) {
		for (String temp : input.split(" ")) {
			list.add(temp);
		}
		//remove "edit" and "number"
		//list.remove(0);
		String index = list.get(0);
		_index = Integer.parseInt(index);
		list.remove(0); 	// REMOVED THIS BECAUSE EDIT FUNCTION NEEDS THE INDEX NUMBER
	}
	
	/**
	 * This method will re-form the command that the user input
	 * without "edit" and the index number
	 * 
	 * @param input
	 * 				string input for editing. non null
	 * 				
	 */
	public String cleanString(String input) { //remove the number
		if (input.matches("[\\d]+")) {
			return "";
		}
		input = input.replaceFirst("[\\d]+ ", "").trim();
		return input;
	}

	
	public String getTask() {
		return _task;
	}

	public void setTask(String task) {
		_task = task;
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
}
