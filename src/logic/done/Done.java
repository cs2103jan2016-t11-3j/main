package logic.done;
import logic.*;

import java.util.ArrayList;

public class Done {

	private final String MESSAGE_DONE = "Task: %1s marked as completed";
	private final String MESSAGE_ERROR = "Error marking task as complete";
	
	private TaskObject instructionTask; // Task containing instruction
	private String taskName = "";
	private String statusBeforeChange = "";
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String> ();
	private int taskIdToMark = -1; // The intended task ID user wants to mark
	
	public Done() {
		
	}
	
	// Constructor for normal done
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
	
	private void obtainTaskId() {
		int lineNumber = Integer.parseInt(instructionTask.getTitle());
		lineNumber--;
		if(lineNumber >= 0 && lineNumber < lastOutputTaskList.size()) {
			taskIdToMark = lastOutputTaskList.get(lineNumber).getTaskId();
		} else {
			createErrorOutput();
		}
	}
	
	private boolean changeStatus() {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskId() == taskIdToMark) {
				taskName = taskList.get(i).getTitle();
				statusBeforeChange = taskList.get(i).getStatus();
				taskList.get(i).setStatus("completed");
				return true;
			}
		}
		return false;
	}
	
	private void createOutput() {
		String text = String.format(MESSAGE_DONE, taskName);
		output.add(text);
	}
	
	private void createErrorOutput() {
		output.add(MESSAGE_ERROR);
	}
	//Getter
	public int getTaskIdToMark() {
		return taskIdToMark;
	}
	
	public String getStatusToChange() {
		return statusBeforeChange;
	}
}
