package logic.edit;

import storage.FileStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

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

	private static final String MESSAGE_TITLE_DATE_TIME_EDIT = "Title edited from '%1$s' to '%2$s', date edited from '%3$s' to '%4$s', time edited from '%5$s' to '%6$s'.";
	private static final String MESSAGE_DATE_TIME_EDIT = "Date edited from '%1$s' to '%2$s', time edited from '%3$s' to '%4$s'.";
	private static final String MESSAGE_DATE_EDIT = "Date edited from '%1$s' to '%2$s'.";
	private static final String MESSAGE_TIME_EDIT = "Time edited from '%1$s' to '%2$s'.";
	private static final String MESSAGE_TITLE_TIME_EDIT = "Title edited from '%1$s' to '%2$s', time edited from '%3$s' to '%4$s'.";
	private static final String MESSAGE_TITLE_EDIT = "Title edited from '%1$s' to '%2$s'.";
	private static final String MESSAGE_TITLE_DATE_EDIT = "Title edited from '%1$s' to '%2$s', date edited from '%3$s' to '%4$s'.";
	private static final Logger LOGGER = Logger.getLogger(Edit.class.getName());
	
	private CommandObject commandObj;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<TaskObject> taskList;

	private ArrayList<String> output = new ArrayList<String>();
	private int editItemNumber;
	private String originalTitle;
	private int originalDate;
	private int originalTime;
	private String editTitle;
	private int editDate;
	private int editTime;
	
	boolean isEditDate = false;
	boolean isEditTitle = false;
	boolean isEditTime = false;
	
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
		//checkEditInformation();
		int editTaskId = getTaskIdOfTaskToBeEdited();
		editTask(editTaskId);
		saveExternal();
		
		setOutput();
		return output;
	}
	
	// Retrieves values from the data objects and sets the relevant edit information	
	private void setEditInformation() {
		try {
			editItemNumber = commandObj.getIndex();
			editTitle = commandObj.getTaskObject().getTitle();
			if (!editTitle.equals("")) {
				isEditTitle = true;
			}
			editDate = commandObj.getTaskObject().getStartDate();
			if (editDate != -1) {
				isEditDate = true;
			}
			editTime = commandObj.getTaskObject().getStartTime();
			if (editTime != -1) {
				isEditTime = true;
			}
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "Error setting edit information");
		}
	}

	private int getTaskIdOfTaskToBeEdited() {
		assert (editItemNumber > 0 && editItemNumber <= lastOutputTaskList.size());
		LOGGER.log(Level.INFO, "Obtained task ID to be edited");
		
		return lastOutputTaskList.get(editItemNumber-1).getTaskId();
	}

	/**
	 * Core method of Edit. Reads in the task ID to be edited and edits the respective information
	 * based on the boolean checks. The data is only edited if it is different from the current.
	 * @param editTaskId
	 */
	private void editTask(int editTaskId) {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskId() == editTaskId) { // if this is the task to be edited
				if (isEditTitle) {
					originalTitle = task.getTitle();
					
					if (!originalTitle.equals(editTitle)) {
						task.setTitle(editTitle);
						LOGGER.log(Level.INFO, "Title edited");
					} else {
						isEditTitle = false;
					}
				} 
				if (isEditDate) {
					originalDate = task.getStartDate();
					
					if (originalDate != editDate) {
						task.setStartDate(editDate);
						LOGGER.log(Level.INFO, "Date edited");
					} else {
						isEditDate = false;
					} 
				}
				if (isEditTime) {
					originalTime = task.getStartTime();
				
					if (originalTime != editTime) {
						task.setStartTime(editTime);
						LOGGER.log(Level.INFO, "Time edited");
					} else {
						isEditTime = false;
					}
				}
			}
		}
	}
	
	// Saves the updated file to Storage
	private void saveExternal() {
		try {
			FileStorage storage = FileStorage.getInstance();
			storage.save(taskList);
			LOGGER.log(Level.INFO, "Storage file updated");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/*
	 * 7 combinations:
	 * TITLE	DATE	TIME
	 * 	 -		DATE	TIME
	 * 	 -		DATE	 -
	 *   -   	 -		TIME
	 * TITLE	 -		TIME
	 * TITLE	 -		 -
	 * TITLE	DATE	 -
	 */
	private void setOutput() {
		if (isEditTitle && isEditDate && isEditTime) {
			outputTitleDateTimeEditedMessage();
		} else if (!isEditTitle) {
			if (isEditDate && isEditTime) {
				outputDateTimeEditedMessage();
			} else {
				if (!isEditTime) {	// no time, only date
					outputDateEditedMessage();
				} else { // // no date, only time
					outputTimeEditedMessage();
				}
			}
		} else if (!isEditDate) {
			if (isEditTitle && isEditTime) {
				outputTitleTimeEditedMessage();
			} else if (!isEditTime) {
				outputTitleEditedMessage();
			}
		} else if (!isEditTime) {
			outputTitleDateEditedMessage();
		}
	
	}
	
	// FOR DEBUGGING
	private void checkEditInformation() {
		System.out.println("isEditTitle = " + isEditTitle);
		System.out.println("isEditDate = " + isEditDate);
		System.out.println("isEditTime = " + isEditTime);
	}
	
	// ------------------------- OUTPUT MESSAGES -------------------------
	
	private void outputTitleDateTimeEditedMessage() {
		output.add(String.format(MESSAGE_TITLE_DATE_TIME_EDIT, originalTitle, editTitle, 
				originalDate, editDate, originalTime, editTime));
	}

	private void outputDateTimeEditedMessage() {
		output.add(String.format(MESSAGE_DATE_TIME_EDIT, originalDate, editDate, originalTime, editTime));
	}
	
	private void outputDateEditedMessage() {
		output.add(String.format(MESSAGE_DATE_EDIT, originalDate, editDate));
	}

	private void outputTimeEditedMessage() {
		output.add(String.format(MESSAGE_TIME_EDIT, originalTime, editTime));
	}
	
	private void outputTitleTimeEditedMessage() {
		output.add(String.format(MESSAGE_TITLE_TIME_EDIT, originalTitle, editTitle, originalTime, editTime));
	}
	
	private void outputTitleEditedMessage() {
		output.add(String.format(MESSAGE_TITLE_EDIT, originalTitle, editTitle));
	}

	private void outputTitleDateEditedMessage() {
		output.add(String.format(MESSAGE_TITLE_DATE_EDIT, originalTitle, editTitle, originalDate, editDate));
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
