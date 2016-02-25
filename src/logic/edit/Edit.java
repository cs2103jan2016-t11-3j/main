package logic.edit;
import logic.TaskObject;

import java.util.ArrayList;


public class Edit {
	
	private static final String MESSAGE_EDIT = "Message title edited from '%1$s' to '%2$s'.";
	
	private TaskObject taskObj;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<TaskObject> taskList;
	
	private ArrayList<String> output = new ArrayList<String>();
	private int editItemNumber;
	private String editTitle;
	private String originalTitle;
	
	public Edit(TaskObject taskObj, ArrayList<TaskObject> lastOutputTaskList, ArrayList<TaskObject> taskList) {
		this.taskObj = taskObj;
		this.lastOutputTaskList = lastOutputTaskList;
		this.taskList = taskList;
	}
	
	public ArrayList<String> run() {
		parseTaskObjectTitle();
		int returnedTaskId = getTaskIdOfTaskToBeEdited();
		editTask(returnedTaskId);
		
		outputConfirmationMessage();
		return output;
	}
	
	/* Splits the title in TaskObject into its relevant components.
	 * e.g. for command 'edit 2 dinner with mom',
	 * '2' is mapped to editItemNumber, and
	 * 'dinner with mom' is mapped to editTitle.
	 */
	private void parseTaskObjectTitle() {
		String title = taskObj.getTitle();
		String[] splitTitle = title.split(" ", 2);
		editItemNumber = Integer.parseInt(splitTitle[0]);
		editTitle = splitTitle[1];
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
