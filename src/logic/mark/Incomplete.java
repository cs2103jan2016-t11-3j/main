package logic.mark;

import java.util.ArrayList;
import java.util.logging.Level;

import common.CommandObject;
import common.TaskObject;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Incomplete extends Mark {

	public Incomplete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
		this.markTaskObj = commandObj.getTaskObject();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	public ArrayList<String> run() {
		obtainTaskId();
		boolean isChanged = false;
		isChanged = changeStatus();
		if (isChanged) {
			saveToFile();
			createOutput();
		} else {
			createErrorOutput();
		}
		return output;
	}

	@Override
	protected boolean changeStatus() {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskId() == taskIdToMark) {
				originalTask.setTaskObject(task);
				originalTimings.addAll(task.getTaskDateTimes());
				
				if (!markTaskObj.isNull()) { // this is an undo function
					task.setTaskObject(markTaskObj);
					LOGGER.log(Level.INFO, "Undo-incomplete processed");
				} else {
					taskName = task.getTitle();
					statusBeforeChange = task.getStatus();
					task.setStatus("incomplete");
	
					LOGGER.log(Level.INFO, "Status changed to \'incomplete\'");
				}
				
				
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
