package logic.mark;

import java.util.ArrayList;

import common.CommandObject;
import common.TaskObject;

public class Incomplete extends Mark {

	private final String MESSAGE_INCOMPLETE = "Task: %1s is marked as incomplete";

	public Incomplete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	public ArrayList<String> run() {
		obtainTaskId();
		boolean isChanged = false;
		isChanged = changeStatus();
		if (isChanged) {
			createOutput();
		} else {
			createErrorOutput();
		}
		return output;
	}

	@Override
	protected boolean changeStatus() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskIdToMark) {
				taskName = taskList.get(i).getTitle();
				statusBeforeChange = taskList.get(i).getStatus();
				taskList.get(i).setStatus("incomplete");
				return true;
			}
		}
		return false;
	}

	private void createOutput() {
		String text = String.format(MESSAGE_INCOMPLETE, taskName);
		output.add(text);
	}

}
