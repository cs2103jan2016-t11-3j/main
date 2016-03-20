package logic.edit;

import storage.FileStorage;

import java.io.IOException;
import java.util.ArrayList;

import common.CommandObject;
import common.TaskObject;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * Creates an Edit object which will edit the title of a selected task to the
 * desired title. <br>
 * Precondition that Edit must be preceded by a command such as "search" or
 * "display", or else the function will not work. This is because "search" and
 * "display" creates a last output task list which will be utilised by the Edit
 * object. <br>
 * Current capability of Edit is only limited to editing the title.
 * Implementation should be extended to changing any part of the TaskObject.
 * 
 * @param CommandObject
 *            commandObj - Contains information regarding what to change. The
 *            TaskObject contained within this attribute contains the desired
 *            title of a particular object, while the index contained within
 *            this attribute contains the relative position of the task in the
 *            last output task list.
 * @author ChongYan, RuiBin
 *
 */

public class Edit {

	private CommandObject commandObj;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<TaskObject> taskList;

	private ArrayList<String> output = new ArrayList<String>();
	private int editItemNumber;
	private int originalDate;
	private int editDate;
	private String originalTitle;
	private String editTitle;
	
	boolean isEditDate = false;
	boolean isEditTitle = false;

	public Edit(CommandObject commandObj, ArrayList<TaskObject> lastOutputTaskList, ArrayList<TaskObject> taskList) {
		this.commandObj = commandObj;
		this.lastOutputTaskList = lastOutputTaskList;
		this.taskList = taskList;
	}
	
	/**
	 * Main method of Edit. Finds the target task and edits its title before
	 * saving it to the external file location.
	 * @return output
	 */
	public ArrayList<String> run() {
		setEditInformation();
		int returnedTaskId = getTaskIdOfTaskToBeEdited();
		editTask(returnedTaskId);
		saveExternal();
		
		if (isEditDate && !isEditTitle) {
			outputDateEditedConfirmationMessage();
		} else if (isEditTitle && !isEditDate) {
			outputTitleEditedConfirmationMessage();
		}

		return output;
	}

	private void saveExternal() {
		try {
			FileStorage storage = FileStorage.getInstance();
			storage.save(taskList);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void setEditInformation() {
		editItemNumber = commandObj.getIndex();
		editDate = commandObj.getTaskObject().getStartDate();
		if (editDate != -1) {
			isEditDate = true;
		}
		editTitle = commandObj.getTaskObject().getTitle();
		if (!editTitle.equals("")) {
			isEditTitle = true;
		}
	}

	private int getTaskIdOfTaskToBeEdited() {
		assert (editItemNumber > 0 && editItemNumber <= lastOutputTaskList.size());
		
		return lastOutputTaskList.get(editItemNumber-1).getTaskId();
	}

	// Edits the title of the task based on the task ID passed
	private void editTask(int editTaskId) {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskId() == editTaskId) { // if this is the task to be edited
				if (editDate != -1) {	// if date is to be edited
					originalDate = task.getStartDate();
					task.setStartDate(editDate);
					task.setEndDate(editDate);
				} else {	// if title is to be edited
					originalTitle = task.getTitle();
					task.setTitle(editTitle);
				}
			}
		}
	}
	
	private void outputDateEditedConfirmationMessage() {
		output.add(String.format(MESSAGE_DATE_EDIT, originalDate, editDate));
	}

	private void outputTitleEditedConfirmationMessage() {
		output.add(String.format(MESSAGE_TITLE_EDIT, originalTitle, editTitle));
	}

	// ------------------------- GETTERS -------------------------
	
	public int getEditItemNumber() {
		return editItemNumber;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}
	
	public int getOriginalDate() {
		return originalDate;
	}
	
	public boolean getIsEditDate() {
		return isEditDate;
	}
	
	public boolean getIsEditTitle() {
		return isEditTitle;
	}

}
