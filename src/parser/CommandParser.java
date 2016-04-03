package parser;

import common.TaskObject;
import java.time.LocalDateTime;

/**
 * This class is an abstract class to hold certain
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
