package logic.delete;

import logic.*;
import storage.*;

import java.util.Stack;

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
	private final String MESSAGE_DELETE = "Task deleted from TaskFinder: %1s";
	private final String MESSAGE_ERROR = "Error deleting task from TaskFinder";
	private final String MESSAGE_QUICK_DELETE_UNAVAILABLE_ERROR = "Quick delete unavailable";

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
	public Delete(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.commandObj = new CommandObject(Logic.INDEX_DELETE, new TaskObject(), -1);
	}

	/**
	 * Default constructor for Normal Delete.
	 * 
	 * @param commandObj
	 *            - Contains the index to delete from the last outputted task
	 *            list
	 * @param taskList
	 *            - Existing list of tasks in Adult TaskFinder
	 * @param lastOutputTaskList
	 *            - List of tasks outputted in the last command (e.g. Search,
	 *            Display)
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.commandObj = commandObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
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
		if (commandObj.getIndex() == -1) {
			runQuickDelete();
		} else {
			runNormalDelete();
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
		} else if (undoList.peek().getCommandType() == Logic.INDEX_DELETE) {
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

	private boolean removeTask(int index) {
		try {
			setTaskName(taskList.get(index).getTitle());
			setRemovedTask(taskList.get(index));
			taskList.remove(index);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void runNormalDelete() {
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

	private boolean deleteInternal() {
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
		if (index > 0 && index <= lastOutputTaskList.size()) {
			index--;
			taskIdToDelete = lastOutputTaskList.get(index).getTaskId();
		}
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

	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public void setOutput(ArrayList<String> output) {
		this.output = output;
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
