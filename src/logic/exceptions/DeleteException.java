package logic.exceptions;

import static logic.constants.Strings.*;

@SuppressWarnings("serial")
public class DeleteException extends Exception {

	public DeleteException() {
		
	}
	
	public DeleteException(int index) {
		super(MESSAGE_NO_INDEX_SPECIFIED_EXCEPTION);
	}
	
	public String getDeleteExceptionMessage() {
		return super.getMessage();
	}
	
}
