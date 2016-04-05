//@@author A0125003A
package parser;

import common.TaskObject;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a child class of the CommandParser. It takes in the string input from the user
 * and splits it into "task" and "date-time". Task is set in the AddParser object while the 
 * datetime is parsed into the DateTimeParser. Sets all newly created task as "incomplete"by 
 * default.
 * 
 * @author sylvesterchin
 *
 */

public class AddParser extends CommandParser {
	private TaskObject TO = new TaskObject();
	
	/**
	 * This method will take in the command and allocate the correct information
	 * to task/date/time
	 * 
	 * @param input    
	 * 				string input from user
	 * @throws Exception 
	 */
	public TaskObject process(String input) throws Exception {
		setTask(input);
		setTaskObject();
		return TO;
	}
	
	/**
	 * This method will split string into task and date-time. Calls DateTimeParser 
	 * for date-time string and sets task in AddParser class
	 * 
	 * @param input
	 * 				same input as process() method. not null.
	 * @throws Exception
	 */
	private void setTask(String input) throws Exception {
		Pattern dateTimePattern = Pattern.compile(Constants.REGEX_DATE_TIME_IDENTIFIER);
		Matcher matcher = dateTimePattern.matcher(input);
		
		String identifier = null;
		
		if (matcher.find()) {
			identifier = getTrimmedString(input ,matcher.start(), matcher.end());
			input = getTrimmedString(input, 0, matcher.start());
		}
		
		if (identifier != null) {
			DateTimeParser dtp = new DateTimeParser();
			TO = dtp.parse(identifier, true);
        }
		
		_task = input;
	}

	
	private void setTaskObject() {
		TO.setTitle(_task);
		TO.setStatus("incomplete");
	}
 	
 	
	
 	//------------GETTERS---------------
	//for testing purposes
 	public String getTask() {
 		return _task;
 	}
 	
 
 	 	
 	public void reset() {
 		_task = null;
 	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

}
