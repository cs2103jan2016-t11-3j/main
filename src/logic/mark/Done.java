package logic.mark;

import java.util.ArrayList;

import logic.TaskObject;

public class Done extends Mark{

	private final String MESSAGE_DONE = "Task: %1s marked as completed";
	
	public Done(TaskObject taskObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		instructionTask = taskObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}
	
	public ArrayList<String> run() {
		obtainTaskId();
		boolean isChanged = false;
		isChanged = changeStatus();
		if(isChanged) {
			createOutput();
		} else {
			createErrorOutput();
		}
		return output;
	}
	
	private void createOutput() {
		String text = String.format(MESSAGE_DONE, taskName);
		output.add(text);
	}

}
