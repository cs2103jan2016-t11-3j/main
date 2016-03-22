package logic.delete;

import storage.*;

import common.CommandObject;
import common.LocalDateTimePair;
import common.TaskObject;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.*;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * Creates a "Delete" object to facilitate the deletion of a task from task list
 * internally, before updating the file at its default location. <br>
 * 
 * There are five ways which Delete can be run: <br>
 * 1) Quick delete <br>
 * Input command: 'delete'
 * Pre-condition that user has to add a task in his last command. Quick delete does not require 
 * the user to input an index for deletion, it automatically deletes the last added task. 
 * This will remove the entire task and all its occurrences, even if it is a recurring task. <br>
 * 
 * 2) Delete all <br>
 * Input command: 'delete all'
 * This clears all lists - task list, undo list and redo list. 
 * 
 * 3) Normal delete <br>
 * Input command: 'delete [index]'
 * Pre-condition that user has to use a search/display function first. With that
 * task list which was displayed, the user proceeds to decide which item in the
 * list he wishes to delete. <br>
 * 
 * 4) Normal delete on recurring task <br>
 * Input command: 'delete [index]'
 * If the task to be deleted is a recurring task, the upcoming occurrence will be deleted and be
 * replaced by the second occurrence.
 *  
 * 5) Delete all on recurring task
 * Input command: 'delete [index] all'
 * The recurring task and all related occurrences will be deleted.
 *
 * @author ChongYan, RuiBin
 *
 */

public class Delete {

	private static final Logger LOGGER = Logger.getLogger(Delete.class.getName());

	// This command object contains the index number of the line to be deleted
	private CommandObject commandObj;

	// This is the task which is actually removed from TaskFinder
	private TaskObject removedTask = new TaskObject();
	// Stores the position of the task to be removed in the taskList 
	private int removedTaskIndex = -1;
	// Actual name of the task which is to be deleted
	private String removedTaskName = "";
	// Actual task ID of the task requested to be deleted
	private int taskIdToBeDeleted = -1;
	// Check if the task to be deleted is a recurring task
	private boolean isRecurringTask = false;

	// Attributes that should be passed in when the delete object is first
	// constructed
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();
	private Deque<CommandObject> undoList = new ArrayDeque<CommandObject>();
	private Deque<CommandObject> redoList = new ArrayDeque<CommandObject>();

	// Internal checkers to ensure that deletion has occurred
	private boolean hasDeletedInternal = false;
	private boolean hasDeletedExternal = false;

	
	// Constructors
	public Delete() {

	}

	/**
	 * Default constructor for Quick Delete. <br>
	 * There will be an additional CommandObject initialised, with an index of
	 * -1.
	 * 
	 * @param taskList
	 *            Existing list of tasks in Adult TaskFinder
	 * @param undoList
	 *            Current stack of CommandObjects with the purpose of undoing
	 *            previous actions
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, Deque<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.commandObj = commandObj;
	}
	
	// Constructor for test cases
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.commandObj = commandObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	/**
	 * Default constructor for Normal Delete.
	 * 
	 * @param commandObj
	 *            Contains the index to delete from the last output task list
	 * @param taskList
	 *            Existing list of tasks in Adult TaskFinder
	 * @param lastOutputTaskList
	 *            List of tasks outputted in the last command (e.g. Search,
	 *            Display)
	 * @param undoList
	 *            Deque containing the list of undo tasks
	 * @param redoList
	 *            Deque containing the list of redo tasks
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList,
			Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		this.commandObj = commandObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
		this.undoList = undoList;
		this.redoList = redoList;
	}

	/**
	 * Called by logic to find and delete an object in the task list.
	 * Automatically decides whether to use quick delete or normal delete based
	 * on the index of the CommandObject in the Delete object.
	 * 
	 * @return output: ArrayList<String> - Contains all the output that the user
	 *         will see
	 */
	public ArrayList<String> run() {
		assert (!taskList.isEmpty());

		try {
			if (commandObj.getIndex() == -1) {
				runQuickDelete();
			} else {
				if (commandObj.getIndex() == 0) {
					runDeleteAll();
				} else {
					setDeleteInformation();

					if (isRecurringTask) {
						if (commandObj.getTaskObject().getTitle().equals("all")) {
							runNormalDelete();
						} else {
							runRecurrenceDelete();
						}
					} else {
						runNormalDelete();
					}
				}
			}
		} catch (NullPointerException e) {
			output.add(MESSAGE_DELETE_ERROR + MESSAGE_NULL_POINTER);
		} catch (IndexOutOfBoundsException e) {
			output.add(MESSAGE_DELETE_ERROR + MESSAGE_INDEX_OUT_OF_BOUNDS);
		}
		return output;
	}

	/**
	 * Main method driving the quick delete function. Checks if the top of the
	 * undoList contains a CommandObject with a delete command, and proceeds to
	 * remove the task if it is the case.
	 */
	private void runQuickDelete() {
		if (undoList.isEmpty()) {
			createErrorOutput();
		} else if (undoList.peek().getCommandType() == INDEX_DELETE) {
			assert (!taskList.isEmpty());
			
			setDeleteInformationForQuickDelete();
			hasDeletedInternal = deleteInternal(taskList.size() - 1);
			hasDeletedExternal = deleteExternal();
			
			if (hasDeletedInternal && hasDeletedExternal) {
				createOutput();
				LOGGER.log(Level.INFO, "Quick delete executed");
			} else {
				createErrorOutput();
			}
		} else {
			createQuickDeleteUnavailableErrorOutput();
		}
	}
	
	// Clears everything - task list, undo list, redo list and the storage file
	private void runDeleteAll() {
		taskList.clear();
		undoList.clear();
		redoList.clear();
		deleteExternal();
		LOGGER.log(Level.INFO, "Delete all executed");
		
		createDeletedAllOutput();
	}
	
	// Delete is handled differently if it is a recurring task
	private void runNormalDelete() throws NullPointerException {
		assert (!taskList.isEmpty());
		hasDeletedInternal = deleteInternal();
		if (hasDeletedInternal) {
			hasDeletedExternal = deleteExternal();
			if (hasDeletedExternal) {
				createOutput();
				LOGGER.log(Level.INFO, "Normal delete executed");
			}
		} else {
			createErrorOutput();
		}
	}
	
	// Gets the array list of LocalDateTimePair from the task and removes the upcoming occurrence
	private void runRecurrenceDelete() {
		ArrayList<LocalDateTimePair> taskDateTimes = removedTask.getTaskDateTimes();
		assert (!taskDateTimes.isEmpty());
		
		taskDateTimes.remove(0);
		removedTask.setTaskDateTimes(taskDateTimes);
		
		// might need additional handling for deletion of indefinite recurring tasks
	}
	
	// ----------------------- PROCESSING DELETE -----------------------
	
	private void setDeleteInformationForQuickDelete() {
		removedTask = taskList.get(taskList.size() - 1);
		removedTaskName = removedTask.getTitle();
	}
	
	private void setDeleteInformation() {
		setTaskIdToBeDeleted();
		setRemovedTask();
		setTaskName();
		setIsRecurringTask();
	}
	
	private void setTaskIdToBeDeleted() {
		int index = commandObj.getIndex();
		assert (index > 0 && index <= lastOutputTaskList.size());

		taskIdToBeDeleted = lastOutputTaskList.get(index - 1).getTaskId();
	}
	
	private void setRemovedTask() {
		assert (taskIdToBeDeleted > 0);
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskIdToBeDeleted) {
				removedTask = taskList.get(i);
				removedTaskIndex = i;
			}
		}
	}
	
	private void setTaskName() {
		removedTaskName = removedTask.getTitle();
	}
	
	private void setIsRecurringTask() {
		isRecurringTask = removedTask.getIsRecurring();
	}
/*
	private boolean removeTask(int index) {
		assert (index > 0 && index <= taskList.size());
		try {
			setTaskName(taskList.get(index).getTitle());
			setRemovedTask(taskList.get(index));
			taskList.remove(index);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}
*/
	private boolean deleteInternal() {
		try {
			taskList.remove(removedTaskIndex);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	// For quick delete
	private boolean deleteInternal(int index) {
		try {
			taskList.remove(index);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}



	private boolean deleteExternal() {
		FileStorage storage = FileStorage.getInstance();
		try {
			storage.save(taskList);
			LOGGER.log(Level.INFO, "Storage file replaced");
		} catch (NoSuchFileException e) {
			// TODO Auto-generated catch block
			// Ask user to specify new location or use default location
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// ----------------------- CREATING OUTPUT -----------------------

	private void createOutput() {
		String text = String.format(MESSAGE_DELETE, removedTaskName);
		output.add(text);
	}

	private void createErrorOutput() {
		removedTask = null;
		output.add(MESSAGE_DELETE_ERROR);
	}

	private void createQuickDeleteUnavailableErrorOutput() {
		removedTask = null;
		output.add(MESSAGE_QUICK_DELETE_UNAVAILABLE_ERROR);
	}

	private void createDeletedAllOutput() {
		output.add(MESSAGE_DELETED_ALL);
	}

	// ----------------------- GETTERS AND SETTERS -----------------------
	
	public CommandObject getCommandObject() {
		return commandObj;
	}

	public TaskObject getRemovedTask() {
		return removedTask;
	}

	public ArrayList<String> getOutput() {
		return output;
	}

	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}

	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public Deque<CommandObject> getUndoList() {
		return undoList;
	}

	public Deque<CommandObject> getRedoList() {
		return redoList;
	}

	public void setTaskList(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public void setCommandObject(CommandObject commandObj) {
		this.commandObj = commandObj;
	}

	public void setTaskName(String removedTaskName) {
		this.removedTaskName = removedTaskName;
	}

	public void setRemovedTask(TaskObject removedTask) {
		this.removedTask = removedTask;
	}
}