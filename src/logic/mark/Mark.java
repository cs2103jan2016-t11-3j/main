package logic.mark;

import logic.*;

import java.util.ArrayList;

public abstract class Mark {

	private final String MESSAGE_ERROR = "Error marking task as complete";

	protected TaskObject instructionTask; // Task containing instruction
	protected TaskObject markedTask;
	protected String taskName = "";
	protected String statusBeforeChange = "";
	protected ArrayList<TaskObject> taskList;
	protected ArrayList<TaskObject> lastOutputTaskList;
	protected ArrayList<String> output = new ArrayList<String>();
	protected int taskIdToMark = -1; // The intended task ID user wants to mark

	public Mark() {

	}

	// Constructor for normal done
	public Mark(TaskObject taskObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		instructionTask = taskObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	public abstract ArrayList<String> run();

	protected void obtainTaskId() {
		int lineNumber = Integer.parseInt(instructionTask.getTitle());
		lineNumber--;
		if (lineNumber >= 0 && lineNumber < lastOutputTaskList.size()) {
			taskIdToMark = lastOutputTaskList.get(lineNumber).getTaskId();
		} else {
			createErrorOutput();
		}
	}

	protected boolean changeStatus() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskIdToMark) {
				taskName = taskList.get(i).getTitle();
				statusBeforeChange = taskList.get(i).getStatus();
				markedTask = taskList.get(i);
				taskList.get(i).setStatus("completed");
				return true;
			}
		}
		return false;
	}

	protected void createErrorOutput() {
		output.add(MESSAGE_ERROR);
	}

	// Getter
	public int getTaskIdToMark() {
		return taskIdToMark;
	}

	public String getStatusToChange() {
		return statusBeforeChange;
	}

	public TaskObject getMarkedTask() {
		return markedTask;
	}
}
