package logic.exceptions;

import static logic.constants.Strings.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("serial")
public class EditException extends Exception {

	public EditException() {
		
	}
	
	public EditException(String message) {
		super(String.format(MESSAGE_EDIT_EXCEPTION, message));
	}
	
	public EditException(LocalTime localTime) {
		super(MESSAGE_EDIT_END_TIME_WITHOUT_START_TIME_EXCEPTION);
	}
	
	public EditException(LocalDateTime localDateTime) {
		super(MESSAGE_INVALID_NEW_START_TIMING_EXCEPTION);
	}

	public String getEditExceptionMessage() {
		return super.getMessage();
	}
}
