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

	public Overdue(CommandObject commandObj, ArrayList<TaskObject> taskList,
			ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
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
			createErrorOutput(MESSAGE_MARK_OVERDUE_ERROR);
		}
		return output;
	}

	@Override
	protected boolean changeStatus() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskIdToMark) {
				taskName = taskList.get(i).getTitle();
				statusBeforeChange = taskList.get(i).getStatus();
				taskList.get(i).setStatus(STATUS_OVERDUE);

				logger.log(Level.INFO, "Status changed to \'overdue\'");
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

	/**
	 * Checks and marks all overdue tasks in the task list, called by Logic when a Logic object is first
	 * constructed.
	 * 
	 * @param taskList
	 *            stores all tasks
	 */
	public static void markAllOverdueTasks(ArrayList<TaskObject> taskList) {
		boolean isOverdue = false;
		for (int i = 0; i < taskList.size(); i++) {
			// only for non-recurring tasks, recurring tasks have their own
			// dedicated methods
			if (!taskList.get(i).getIsRecurring()) {
				if (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) {
					if (!taskList.get(i).getStatus().equals(STATUS_COMPLETED)) {
						isOverdue = performCheckOverdue(taskList.get(i));
						if (isOverdue) {
							taskList.get(i).setStatus(STATUS_OVERDUE);
							logger.log(Level.INFO, "set status of non-recurring deadline to overdue");
						}
					}
				}
				if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
					if (!taskList.get(i).getStatus().equals(STATUS_COMPLETED)) {
						isOverdue = performCheckOverdue(taskList.get(i));
						if (isOverdue) {
							taskList.get(i).setStatus(STATUS_OVERDUE);
							logger.log(Level.INFO, "set status of non-recurring event to overdue");
						}
					}
				}
			}
		}
	}

	private static boolean performCheckOverdue(TaskObject task) {
		LocalDateTime startDateTime = task.getStartDateTime();
		assert (!startDateTime.isEqual(LocalDateTime.MAX));

		if (startDateTime.isBefore(LocalDateTime.now())) {
			return true;
		}
		return false;
	}
}
