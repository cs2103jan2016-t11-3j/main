package logic.delete;

import logic.*;
import storage.*;

import java.util.Stack;
import java.util.ArrayList;

// Needs delete function for last added code
public class Delete {

	// Deletes by searching for the unique taskID
	private static String MESSAGE_DELETE = "Task deleted from TaskFinder: %1s";
	private static String MESSAGE_ERROR = "Error deleting task from TaskFinder";

	// This is the delete task that should only contain the displayed line
	// number to delete, or nothing inside at all
	private TaskObject task = new TaskObject();
	
	// This is the task which is actually removed from TaskFinder
	private TaskObject removedTask = new TaskObject();

	// Attributes that should be passed in when the delete object is first
	// constructed
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();
	private Stack<CommandObject> undoList = new Stack<CommandObject>();

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
	
	public Delete(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
	}

	public Delete(TaskObject task, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.task = task;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}
	
	public ArrayList<String> run() {
		if(task.getTitle().equals("")) {
			runQuickDelete();
		} else {
			runNormalDelete();
		}
		return output;
	}
	
	private void runQuickDelete() {
		if(undoList.empty()) {
			createErrorOutput();
		}
		if(undoList.peek().getCommandType() == Logic.INDEX_DELETE) {
			// delete the last item in taskList as it shows that the item had just been added
			int index = taskList.size() - 1;
			taskName = taskList.get(index).getTitle();
			taskList.remove((index));
			if(taskList.size() == index) {
				// To check if taskList has shrunk by 1
				hasDeletedInternal = true;
			}
			hasDeletedExternal = deleteExternal();
			if(hasDeletedInternal && hasDeletedExternal) {
				createOutput();
			} else {
				createErrorOutput();
			}
		} else {
			createErrorOutput();
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
					taskName = taskList.get(i).getTitle();
					removedTask = taskList.get(i);
					taskList.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	private void obtainTaskId() {
		String line = task.getTitle();
		int num = Integer.parseInt(line);
		// CHANGED LASTOUTPUTTASKLIST TO TASKLIST
		// because 'undo' does not have a display command before it
		if (num > 0 && num <= taskList.size()) { 
			num--;
			taskIdToDelete = taskList.get(num).getTaskId();
		}
	}

	private boolean deleteExternal() {
		FileStorage storage = FileStorage.getInstance();
		int success = storage.save(taskList);
		if (success == 0) {
			return true;
		} else {
			return false;
		}
	}

	private void createOutput() {
		String text = String.format(MESSAGE_DELETE, taskName);
		output.add(text);
	}

	private void createErrorOutput() {
		output.add(MESSAGE_ERROR);
	}

	// GETTERS AND SETTERS
	public TaskObject getTask() {
		return task;
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

	public void setTask(TaskObject task) {
		this.task = task;
	}

}
