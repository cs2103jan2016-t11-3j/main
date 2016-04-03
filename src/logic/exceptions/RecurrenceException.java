package logic.exceptions;

import common.TaskObject;
import static logic.constants.Strings.*;

public class RecurrenceException extends Exception{

	public RecurrenceException() {
		
	}
	
	public RecurrenceException(TaskObject task) {
		super(String.format(MESSAGE_RECURRENCE_EXCEPTION, task.getTitle()));
	}
	
	public RecurrenceException(String errorMessage) {
		super(String.format(MESSAGE_RECURRENCE_EXCEPTION, errorMessage));
	}
	
	public String getRecurrenceExceptionMessage() {
		return super.getMessage();
	}
}
