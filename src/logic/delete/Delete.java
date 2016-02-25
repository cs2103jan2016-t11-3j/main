package logic.delete;
import storage.*;

import java.io.IOException;
import java.util.ArrayList;

import logic.*;

public class Delete {

	// Deletes by searching for the unique taskID
	private static String MESSAGE_DELETE = "Task deleted from TaskFinder: %1s";
	private static String MESSAGE_ERROR = "Error deleting task: %1s from TaskFinder";
	
	private TaskObject task;
	private ArrayList<TaskObject> taskList;
	private ArrayList<String> output = new ArrayList<String>();
	private boolean deletedInternal = false;
	
	// Constructors
	public Delete() {
		
	}
	
	public Delete(TaskObject task, ArrayList<TaskObject> taskList) {
		this.task = task;
		this.taskList = taskList;
	}
	
	public ArrayList<String> run() {
		deletedInternal = deleteInternal();
		if(deletedInternal) {
			deleteExternal();
			createOutput();
		} else {
			createErrorOutput();
		}
		return output;
	}
	
	public boolean deleteInternal() {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskId() == task.getTaskId()) {
				taskList.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public void deleteExternal() {
		FileStorage storage = FileStorage.getInstance();
		int success = storage.save(taskList);
	}
	
	public void createOutput() {
		String text = String.format(MESSAGE_DELETE, task.getTitle());
		output.add(text);
	}
	
	public void createErrorOutput() {
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
