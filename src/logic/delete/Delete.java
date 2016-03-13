package logic.delete;

import storage.*;

import common.CommandObject;
import common.TaskObject;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Creates a "Delete" object to facilitate the deletion of a task from task list
 * internally, before updating the file at its default location. <br>
 * There are two ways which Delete can be run: <br>
 * 1) Normal delete <br>
 * Pre-condition that user has to use a search/display function first. With that
 * task list which was displayed, the user proceeds to decide which item in the
 * list he wishes to delete. <br>
 * 2) Quick delete <br>
 * Pre-condition that user has to add a task in his last command. Quick delete
 * does not require the user to input an index for deletion, it automatically
 * deletes the last added task.
 * 
 * @author ChongYan
 *
 */

// Needs delete function for last added code
public class Delete {

	// Deletes by searching for the unique taskID

	private final String MESSAGE_DELETE = "Task deleted from AdultTaskFinder: %1s";
	private final String MESSAGE_ERROR = "Error deleting task from TaskFinder. ";
	private final String MESSAGE_QUICK_DELETE_UNAVAILABLE_ERROR = "Quick delete unavailable";
	private final String MESSAGE_NULL_POINTER = "Attempted to access a non-existent task. ";
	private final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Requested index does not exist";
	private final String MESSAGE_DELETED_ALL = "All tasks deleted from AdultTaskFinder";

	private final int INDEX_DELETE = 4;
	// This command object contains the index number of the line to be deleted
	private CommandObject commandObj;

	// This is the task which is actually removed from TaskFinder
	private TaskObject removedTask = new TaskObject();

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

	// Actual task ID of the task requested to be deleted
	private int taskIdToDelete = -1;

	// Actual name of the task which is to be deleted
	private String taskName = "";

	// Constructors
	public Delete() {

	}

	/**
	 * Default constructor for Quick Delete. <br>
	 * There will be an additional CommandObject initialised, with an index of
	 * -1.
	 * 
	 * @param taskList
	 *            - Existing list of tasks in Adult TaskFinder
	 * @param undoList
	 *            - Current stack of CommandObjects with the purpose of undoing
	 *            previous actions
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, Deque<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.commandObj = commandObj;
	}

	/**
	 * Default constructor for Normal Delete.
	 * 
	 * @param commandObj
	 *            - Contains the index to delete from the last output task list
	 * @param taskList
	 *            - Existing list of tasks in Adult TaskFinder
	 * @param lastOutputTaskList
	 *            - List of tasks outputted in the last command (e.g. Search, Display)
	 * @param undoList
	 * 			  - Deque containing the list of undo tasks
	 * @param redoList
	 * 			  - Deque containing the list of redo tasks
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
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
					runNormalDelete();
				}
			}
		} catch (NullPointerException e) {
			output.add(MESSAGE_ERROR + MESSAGE_NULL_POINTER);
		} catch (IndexOutOfBoundsException e) {
			output.add(MESSAGE_ERROR + MESSAGE_INDEX_OUT_OF_BOUNDS);
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
			hasDeletedInternal = removeTask(taskList.size() - 1);
			hasDeletedExternal = deleteExternal();
			if (hasDeletedInternal && hasDeletedExternal) {
				createOutput();
			} else {
				createErrorOutput();
			}
		} else {
			createQuickDeleteUnavailableErrorOutput();
		}
	}

	private void runNormalDelete() throws NullPointerException {
		assert (!taskList.isEmpty());
		hasDeletedInternal = deleteInternal();
		if (hasDeletedInternal) {
			hasDeletedExternal = deleteExternal();
			if (hasDeletedExternal) {
				createOutput();
			}
		} else {
			createErrorOutput();
		}
	}
	
	// Clears everything - task list, undo list, redo list and the storage file
	private void runDeleteAll() {
		taskList.clear();
		undoList.clear();
		redoList.clear();
		deleteExternal();

		createDeletedAllOutput();
	}

	private boolean removeTask(int index) {
		assert (index > 0 && index < taskList.size());
		try {
			setTaskName(taskList.get(index).getTitle());
			setRemovedTask(taskList.get(index));
			taskList.remove(index);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	
	private boolean deleteInternal() throws NullPointerException {
		obtainTaskId();
		if (taskIdToDelete != -1) {
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTaskId() == taskIdToDelete) {
					return removeTask(i);
				}
			}
		}
		return false;
	}

	private void obtainTaskId() {
		int index = commandObj.getIndex();
		assert (index > 0 && index <= lastOutputTaskList.size());
		
		taskIdToDelete = lastOutputTaskList.get(index-1).getTaskId();
	}

	private boolean deleteExternal() {
		FileStorage storage = FileStorage.getInstance();
		try {
			storage.save(taskList);
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

	private void createOutput() {
		String text = String.format(MESSAGE_DELETE, taskName);
		output.add(text);
	}

	private void createErrorOutput() {
		removedTask = null;
		output.add(MESSAGE_ERROR);
	}

	private void createQuickDeleteUnavailableErrorOutput() {
		removedTask = null;
		output.add(MESSAGE_QUICK_DELETE_UNAVAILABLE_ERROR);
	}
	
	private void createDeletedAllOutput() {
		output.add(MESSAGE_DELETED_ALL);
	}

	// GETTERS AND SETTERS
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

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setRemovedTask(TaskObject removedTask) {
		this.removedTask = removedTask;
	}
}
