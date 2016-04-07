//@@author A0125003A
package parser;

import common.TaskObject;
import java.time.LocalDateTime;

/**
 * This class is an abstract class that is the parent class for
 * AddParser, EditParser and SearchParser. Contains task, StartDateTime
 * and EndDateTime as well as some basic methods that all 3 child will
 * need to call. 
 * 
 * @author sylvesterchin
 *
 */
public abstract class CommandParser {
	
	protected String _task = "";
	protected LocalDateTime _startDateTime = LocalDateTime.MAX;
	protected LocalDateTime _endDateTime = LocalDateTime.MAX;
	
	/**
	 * This method processes the input string and returns a task object with attributes
	 * filled.
	 * 
	 * @param input
	 * 				user's input. non-null string. 
	 * @return
	 * 				post processing task object with non-null attribute.
	 * @throws Exception
	 */
	public abstract TaskObject process(String input) throws Exception;
	
	/**
	 * This method returns the index for editing. Only used by EditParser.
	 * 
	 * @return
	 * 				integer that represents the index number of task to be edited.
	 */
	public abstract int getIndex();
	
	protected String getTrimmedString(String input, int startIndex, int endIndex) {
		return input.substring(startIndex, endIndex).trim();
	}
	
}
