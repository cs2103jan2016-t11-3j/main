package logic.edit;

import storage.FileStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	
	private static final Logger LOGGER = Logger.getLogger(Edit.class.getName());
	
	private CommandObject commandObj;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<TaskObject> taskList;

	private ArrayList<String> output = new ArrayList<String>();
	private String originalTitle;
	private LocalDate originalDate;
	private LocalTime originalTime;
	private String editTitle;
	private LocalDate editDate;
	private LocalTime editTime;
	private int editItemNumber;
	
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
		checkEditInformation();
		int editTaskId = getTaskIdOfTaskToBeEdited();
		editTask(editTaskId);
		saveExternal();
		
		setOutput();
		return output;
	}
	
	// Retrieves values from the data objects and sets the relevant edit information	
	private void setEditInformation() {
		assert (editItemNumber > 0);
		
		try {
			editItemNumber = commandObj.getIndex();
			editTitle = commandObj.getTaskObject().getTitle();
			if (!editTitle.equals("")) {
				isEditTitle = true;
			}
			editDate = commandObj.getTaskObject().getStartDateTime().toLocalDate();
			if (editDate.compareTo(LocalDate.MAX) < 0) {
				isEditDate = true;
			}
			editTime = commandObj.getTaskObject().getStartDateTime().toLocalTime();
			if (editTime.compareTo(LocalTime.MAX) < 0) {
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
					editTitle(task);
				} 
				
				if (isEditDate && isEditTime) {
					editDateAndTime(task);
				} else {
					if (!isEditTime) {
						editDate(task);
					}
					if (!isEditDate) {
						editTime(task);
					}
				}

			}
		}
	}

	private void editTitle(TaskObject task) {
		originalTitle = task.getTitle();
		
		if (!originalTitle.equals(editTitle)) {
			task.setTitle(editTitle);
			LOGGER.log(Level.INFO, "Title edited");
		} else {
			isEditTitle = false;
		}
	}
	
	private void editDateAndTime(TaskObject task) {
		originalDate = task.getStartDateTime().toLocalDate();
		originalTime = task.getStartDateTime().toLocalTime();
		
		if (originalDate.compareTo(editDate) != 0 && originalTime.compareTo(editTime) != 0) {		
			task.setStartDateTime(LocalDateTime.of(editDate, editTime));
			LOGGER.log(Level.INFO, "Date and time edited");
		} else if (originalTime.compareTo(editTime) == 0) {
			isEditTime = false;
			editDate(task);
		} else if (originalDate.compareTo(editDate) == 0) {
			isEditDate = false;
			editTime(task);
		} 
	}
	
	private void editDate(TaskObject task) {
		originalDate = task.getStartDateTime().toLocalDate();
		
		if (originalDate.compareTo(editDate) != 0) {		
			originalTime = task.getStartDateTime().toLocalTime();
			task.setStartDateTime(LocalDateTime.of(editDate, originalTime));
			LOGGER.log(Level.INFO, "Date edited");
		} else {
			isEditDate = false;
		} 
	}
	
	private void editTime(TaskObject task) {
		originalTime = task.getStartDateTime().toLocalTime();
		
		if (originalTime.compareTo(editTime) != 0) {
			originalDate = task.getStartDateTime().toLocalDate();
			task.setStartDateTime(LocalDateTime.of(originalDate, editTime));
			LOGGER.log(Level.INFO, "Time edited");
		} else {
			isEditTime = false;
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
	
	public LocalDate getOriginalDate() {
		return originalDate;
	}
	
	public boolean getIsEditDate() {
		return isEditDate;
	}
	
	public boolean getIsEditTitle() {
		return isEditTitle;
	}

}
