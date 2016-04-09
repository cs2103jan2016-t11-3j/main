package logic.exceptions;

import static logic.constants.Strings.*;

@SuppressWarnings("serial")
public class EditException extends Exception {

	public EditException() {
		
	}
	
	public EditException(String message) {
		super(String.format(MESSAGE_EDIT_EXCEPTION, message));
	}

	public String getEditExceptionMessage() {
		return super.getMessage();
	}
}
