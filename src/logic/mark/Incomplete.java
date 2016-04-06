//@@author A0124052X

package logic.mark;

import java.util.ArrayList;
import java.util.logging.Level;

import common.CommandObject;
import common.TaskObject;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * Child class of Mark, main purpose is to support Logic in the marking of a task as incomplete. Obtains the
 * desired task ID through the index parsed from user input, before proceeding to mark that particular task
 * with the status "incomplete".
 * 
 * @author ChongYan
 *
 */
public class Incomplete extends Mark {

	public Incomplete(CommandObject commandObj, ArrayList<TaskObject> taskList,
			ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
		this.markTaskObj = commandObj.getTaskObject();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	/**
	 * The main processor called by CommandFacade to mark a task as "incomplete". Obtains the desired task ID,
	 * changes the status internally before saving it to the external storage.
	 * 
	 * @return ArrayList<String> containing output showed to the user
	 */
	public ArrayList<String> run() {
		obtainTaskId();
		assert (taskIdToMark != -1);

		boolean isChanged = false;
		isChanged = changeStatus();
		if (isChanged) {
			saveToFile();
			createOutput();
			logger.log(Level.INFO, "marked task as incomplete");
		} else {
			createErrorOutput(MESSAGE_MARK_INCOMPLETE_ERROR);
		}
		return output;
	}
	
//@@author A0124636H

	@Override
	protected boolean changeStatus() {
		for (int i = 0; i < taskList.size(); i++) {
			TaskObject task = taskList.get(i);
			if (task.getTaskId() == taskIdToMark) {

				originalTask.setTaskObject(task);
				originalTimings.addAll(task.getTaskDateTimes());

				taskName = task.getTitle();
				statusBeforeChange = task.getStatus();

				try {
					task.setTaskObject(markTaskObj); // if markTaskObj is not null, this is an undo function
					statusBeforeChange = STATUS_COMPLETED;

					if (markTaskObj.getIsRecurring() && markTaskObj.getTaskDateTimes().size() > 1) {
						deleteSplitTaskFromTaskList(); // deletes the split task that had been created upon
														// mark as done
					}
					logger.log(Level.INFO, "Undo-incomplete processed");
				} catch (NullPointerException e) {
					task.setStatus(STATUS_INCOMPLETE);
					logger.log(Level.INFO, "Status changed to \'incomplete\'");
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
