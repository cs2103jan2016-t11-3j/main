//@@author A0124052X

package logic.exceptions;
import common.TaskObject;
import static logic.constants.Strings.*;

public class AddException extends Exception{
	
	private boolean isRecurring;
	private String category;
	
	public AddException() {
		
	}
	
	public AddException(TaskObject task) {
		super(MESSAGE_ADD_EXCEPTION);
		isRecurring = task.getIsRecurring();
		category = task.getCategory();
	}
	
	public boolean getIsRecurring() {
		return isRecurring;
	}
	
	public String getCategory() {
		return category;
	}

}
