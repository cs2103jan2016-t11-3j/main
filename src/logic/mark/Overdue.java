package logic.mark;

import java.util.ArrayList;
import java.util.logging.Level;
import java.time.LocalDateTime;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Overdue extends Mark {
	
	public Overdue(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}
	
	public ArrayList<String> run() {
		obtainTaskId();
		boolean isChanged = false;
		isChanged = changeStatus();
		if(isChanged) {
			saveToFile();
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

				LOGGER.log(Level.INFO, "Status changed to \'overdue\'");
				return true;
			}
		}
		return false;
	}
	
	private void createOutput() {
		String text = String.format(MESSAGE_OVERDUE, taskName);
		output.add(text);
	}
	
	/*******************************************************************************/
	
	public static void markAllOverdueTasks(ArrayList<TaskObject> taskList) {
		boolean isOverdue = false;
		for(int i = 0; i < taskList.size(); i++) {
			if(taskList.get(i).getCategory().equals("deadline")) {
				isOverdue = performCheckOverdue(taskList.get(i));
				if(isOverdue) {
					taskList.get(i).setStatus("overdue");
				}
			}
		}
	}
	
	private static boolean performCheckOverdue(TaskObject task) {
		LocalDateTime deadline = task.getStartDateTime();
		if(deadline.isBefore(LocalDateTime.now())) {
			return true;
		}
		return false;
	}
}
