//@@author A0124052X

package logic.mark;

import java.util.ArrayList;
import java.util.logging.*;

import common.CommandObject;
import common.LocalDateTimePair;
import common.TaskObject;
import logic.Recurring;
import logic.exceptions.MarkException;
import logic.exceptions.RecurrenceException;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * Creates a Done object, which is a Mark object. However, it specifically changes the status of the target
 * task to "done".
 * 
 * @author ChongYan
 *
 */
public class Done extends Mark {

	/**
	 * Constuctor for a Done object.
	 * 
	 * @param commandObj
	 *            - Contains information on the task to be changed, not the task to be changed
	 * @param taskList
	 *            - Contains all existing tasks in Adult TaskFinder
	 * @param lastOutputTaskList
	 *            - Contains the list of tasks which was last outputted
	 */
	public Done(CommandObject commandObj, ArrayList<TaskObject> taskList,
			ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
		this.markTaskObj = commandObj.getTaskObject();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;

		logger.log(Level.INFO, "constructed Done object");
	}

	/**
	 * Main method of the Done class, which facilitates the toggling of a task's status to "completed", before
	 * setting an output of ArrayList <String> describing the changes made to that specific task. <br>
	 * If the task marked as completed is a recurring task, it will be handled differently and this will be
	 * dealt with by the methods in Recurring.
	 * 
	 * @return output: ArrayList<String>
	 */
	public ArrayList<String> run() {
		obtainTaskId();
		assert (taskIdToMark != -1);
		// Impossible to have a task ID of -1

		boolean isChanged = false;
		isChanged = changeStatus();
		if (isChanged) {
			saveToFile();
			createOutput();
			logger.log(Level.INFO, "marked task as completed");
		} else {
			createErrorOutput(MESSAGE_MARK_DONE_ERROR);
		}
		return output;
	}

	private void createOutput() {
		String text = String.format(MESSAGE_DONE, taskName);
		output.add(text);
	}

	// @@author A0124636H

	protected boolean changeStatus() {
		try {
			for (int i = 0; i < taskList.size(); i++) {
				TaskObject task = taskList.get(i);
				if (task.getTaskId() == taskIdToMark) {
					originalTask.setTaskObject(task);
					originalTimings.addAll(task.getTaskDateTimes());

					taskName = task.getTitle();
					statusBeforeChange = task.getStatus();
					markedTask = task;
					checkCurrentStatus(STATUS_COMPLETED);
					setMostRecentlyMarkedTaskId(markedTask.getTaskId());

					if (task.getIsRecurring()) {
						changeStatusForRecurringTask(task);
						logger.log(Level.INFO, "marked a recurring task as done");
					} else {
						task.setStatus(STATUS_COMPLETED);
						logger.log(Level.INFO, "marked a non-recurring task as done");
					}

					originalTask.setTaskDateTimes(originalTimings);
					return true;
				}
			}
			logger.log(Level.WARNING, "failed to mark task as completed");
			return false;
		} catch (MarkException e) {
			logger.log(Level.WARNING, "task to be marked already has that status");
			createErrorOutput(e.getMarkExceptionMessage());
			return false;
		}
	}

	// @@author A0124052X

	// On top of this, consider implementation for event
	private void changeStatusForRecurringTask(TaskObject task) {
		if (task.getCategory().equals(CATEGORY_DEADLINE)) {
			logger.log(Level.INFO, "about to mark a recurring deadline as completed");
			changeStatusForRecurringDeadline(task);
		} else {
			if (task.getCategory().equals(CATEGORY_EVENT)) {
				logger.log(Level.INFO, "about to mark a recurring event as completed");
				changeStatusForRecurringEvent(task);
			}
		}
	}

	private void changeStatusForRecurringDeadline(TaskObject task) {
		try {
			Recurring.updateDeadline(task, taskList, STATUS_COMPLETED);
			// At the last recurrence, task will be set to be non-recurring
			if (task.getIsRecurring()) {
				int splitTaskId = findSplitTaskId();
				setMostRecentlyMarkedTaskId(splitTaskId);
			}
		} catch (RecurrenceException e) {
			String exceptionMessage = e.getRecurrenceExceptionMessage();
			createErrorOutput(exceptionMessage);
		}
	}

	private void changeStatusForRecurringEvent(TaskObject task) {
		try {
			Recurring.updateEvent(task, taskList, STATUS_COMPLETED);
			// At the last recurrence, task will be set to non-recurring
			if (task.getIsRecurring()) {
				int splitTaskId = findSplitTaskId();
				setMostRecentlyMarkedTaskId(splitTaskId);
			}
		} catch (RecurrenceException e) {
			String exceptionMessage = e.getRecurrenceExceptionMessage();
			createErrorOutput(exceptionMessage);
		}
	}

	private int findSplitTaskId() {
		int id = -1;
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() < id) {
				id = taskList.get(i).getTaskId();
			}
		}
		return id;
	}

}
