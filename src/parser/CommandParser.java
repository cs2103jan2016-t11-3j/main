//@@author A0125003A
package parser;

import common.TaskObject;
import java.time.LocalDateTime;

/**
 * This class is an abstract class that is the parent class for
 * AddParser, EditParser and SearchParser. Contains task, startdatetime
 * and enddatetime as well as some basic methods that all 3 child will
 * need to call. 
 * 
 * @author sylvesterchin
 *
 */
public abstract class CommandParser {
	
	protected String _task = "";
	protected LocalDateTime _startDateTime = LocalDateTime.MAX;
	protected LocalDateTime _endDateTime = LocalDateTime.MAX;
	
	public abstract TaskObject process(String input) throws Exception;
	
	public abstract void reset();
	
	public abstract int getIndex(); //only for EDIT
	
	protected String getTrimmedString(String input, int startIndex, int endIndex) {
		return input.substring(startIndex, endIndex).trim();
	}
	
}
