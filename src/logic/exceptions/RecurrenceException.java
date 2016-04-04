package logic.exceptions;

import common.TaskObject;
import common.Interval;
import static logic.constants.Strings.*;

@SuppressWarnings("serial")
public class RecurrenceException extends Exception{

	public RecurrenceException() {
		
	}
	
	public RecurrenceException(TaskObject task) {
		super(String.format(MESSAGE_RECURRENCE_EXCEPTION, task.getTitle()));
	}
	
	public RecurrenceException(String errorMessage) {
		super(String.format(MESSAGE_RECURRENCE_EXCEPTION, errorMessage));
	}
	
	public RecurrenceException(Interval interval) {
		super(String.format(MESSAGE_RECURRENCE_EXCEPTION, MESSAGE_RECURRENCE_EXCEPTION_INVALID_INTERVAL));
	}
	
	public String getRecurrenceExceptionMessage() {
		return super.getMessage();
	}
}
