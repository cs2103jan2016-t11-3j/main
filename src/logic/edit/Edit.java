package logic.edit;
import logic.CommandObject;
import logic.TaskObject;

import java.util.ArrayList;


public class Edit {
	
	private static final String MESSAGE_EDIT = "Message title edited from '%1$s' to '%2$s'.";
	
	private CommandObject commandObj;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<TaskObject> taskList;
	
	private ArrayList<String> output = new ArrayList<String>();
	private int editItemNumber;
	private String editTitle;
	private String originalTitle;
	
	public Edit(CommandObject commandObj, ArrayList<TaskObject> lastOutputTaskList, ArrayList<TaskObject> taskList) {
		this.commandObj = commandObj;
		this.lastOutputTaskList = lastOutputTaskList;
		this.taskList = taskList;
	}
	
	public int getEditItemNumber() {
		return editItemNumber;
	}
	
	public String getOriginalTitle() {
		return originalTitle;
	}
	
	public ArrayList<String> run() {
		setEditInformation();
		int returnedTaskId = getTaskIdOfTaskToBeEdited();
		editTask(returnedTaskId);
		
		outputConfirmationMessage();
		return output;
	}
	
	private void setEditInformation() {
		editItemNumber = commandObj.getIndex();
		editTitle = commandObj.getTaskObject().getTitle();
	}
	
	private int getTaskIdOfTaskToBeEdited() {
		return lastOutputTaskList.get(editItemNumber-1).getTaskId();
	}
	
	// Edits the title of the task based on the task ID passed 
	private void editTask(int editTaskId) {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskId() == editTaskId) {	// if this is the task to be edited
				originalTitle = task.getTitle();
				task.setTitle(editTitle);
			}
		}
	}
	
	private void outputConfirmationMessage() {
		output.add(String.format(MESSAGE_EDIT, originalTitle, editTitle));
	}

}
