package logic.delete;

import storage.*;

import java.io.IOException;
import java.util.ArrayList;

import logic.*;

public class Delete {

	// Deletes by searching for the unique taskID
	private static String MESSAGE_DELETE = "Task deleted from TaskFinder: %1s";
	private static String MESSAGE_ERROR = "Error deleting task %1s from TaskFinder";

	// This is the delete task that should only contain the displayed line
	// number to delete
	private TaskObject task;

	// Attributes that should be passed in when the delete object is first
	// constructed
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();

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

	public Delete(TaskObject task, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.task = task;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	public ArrayList<String> run() {
		hasDeletedInternal = deleteInternal();
		if (hasDeletedInternal) {
			hasDeletedExternal = deleteExternal();
			if (hasDeletedExternal) {
				createOutput();
			}
		} else {
			createErrorOutput();
		}
		return output;
	}

	private boolean deleteInternal() {
		obtainTaskId();
		if (taskIdToDelete != -1) {
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTaskId() == taskIdToDelete) {
					taskName = taskList.get(i).getTitle();
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
		if (num > 0 && num <= lastOutputTaskList.size()) {
			num--;
			taskIdToDelete = lastOutputTaskList.get(num).getTaskId();
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
		String text = String.format(MESSAGE_ERROR, task.getTitle());
		output.add(text);
	}

	// GETTERS AND SETTERS
	public TaskObject getTask() {
		return task;
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
