package logic.mark;

import java.util.ArrayList;

import common.TaskObject;

public class Overdue extends Mark {
	
private final String MESSAGE_OVERDUE = "Task: %1s is marked as overdue";
	
	public Overdue(TaskObject taskObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
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
	
	@Override
	protected boolean changeStatus() {
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getTaskId() == taskIdToMark) {
				taskName = taskList.get(i).getTitle();
				statusBeforeChange = taskList.get(i).getStatus();
				taskList.get(i).setStatus("overdue");
				return true;
			}
		}
		return false;
	}
	
	private void createOutput() {
		String text = String.format(MESSAGE_OVERDUE, taskName);
		output.add(text);
	}

}
