//@@author A0124052X

package logic.exceptions;

import static logic.constants.Strings.*;
import common.TaskObject;

@SuppressWarnings("serial")
public class MarkException extends Exception {

	public MarkException () {
		
	}
	
	public MarkException (TaskObject task) {
		super(String.format(MESSAGE_MARK_EXCEPTION, String.format(MESSAGE_MARK_EXCEPTION_SAME_STATUS, task.getStatus())));
	}
	
	public String getMarkExceptionMessage() {
		return super.getMessage();
	}
}
