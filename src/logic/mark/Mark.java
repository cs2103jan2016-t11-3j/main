package logic.mark;

import logic.*;
import storage.FileStorage;
import storage.IStorage;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.logging.*;

import common.AtfLogger;
import common.CommandObject;
import common.LocalDateTimePair;
import common.TaskObject;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * An abstract class. Inherited by Done, Incomplete and Overdue. This set of classes 
 * serve to facilitate switching the status of a selected task between three set statuses - 
 * "done", "incomplete" and "overdue".
 * <br> Precondition: This command should be preceded by a command such as "display" or 
 * "search", which will generate a list of tasks for the user to manipulate. 
 * Class methods will not work if the last output task list is invalid.
 * <br> Contains the abstract run() method which will be overridden by each subclasses's 
 * implementation.
 * @author ChongYan
 *
 */
public abstract class Mark {

	protected static Logger logger = AtfLogger.getLogger();

	/**
	 * @param TaskObject instructionTask - This is the TaskObject which contains information 
	 * on which task's status to be modified.
	 * @param TaskObject markedTask - This is a copy of the TaskObject which is modified by
	 * this specific Mark object. Will be added to the undo/redo list whichever is 
	 * applicable.
	 * @param String taskName - This contains the name of the task which was modified
	 * @param String statusBeforeChange - This contains the status of the task before it
	 * was toggled by Mark.run()
	 * @param taskIdToMark - Contains the task ID of the task whose status will be toggled
	 */
	
	protected TaskObject originalTask = new TaskObject(); // original task info; for undo purposes
	protected ArrayList<LocalDateTimePair> originalTimings = new ArrayList<LocalDateTimePair>();
	
	protected TaskObject markedTask;
	protected String taskName = "";
	protected String statusBeforeChange = "";
	protected ArrayList<TaskObject> taskList;
	protected ArrayList<TaskObject> lastOutputTaskList;
	protected ArrayList<String> output = new ArrayList<String>();
	protected int taskIdToMark = -1; // The intended task ID user wants to mark
	
	protected TaskObject markTaskObj = new TaskObject();
	protected int index = -1;

	public Mark() {

	}
	
	/**
	 * Generic constructor for all subclasses of Mark to be used.
	 * @param taskObj - Contains information on the task to be changed, not the task to be changed
	 * @param taskList - Contains all existing tasks in Adult TaskFinder
	 * @param lastOutputTaskList - Contains the list of tasks which was last outputted
	 */
	public Mark(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.index = commandObj.getIndex();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	public abstract ArrayList<String> run();

	// May need to change if parser changes the way this command object is constructed
	protected void obtainTaskId() {
		index--;
		if (index >= 0 && index < lastOutputTaskList.size()) {
			taskIdToMark = lastOutputTaskList.get(index).getTaskId();
		} else {
			createErrorOutput(MESSAGE_MARK_DONE_ERROR);
		}
	}
	
	protected void saveToFile() {
		IStorage storage = FileStorage.getInstance();
		try {
			storage.save(taskList);
		} catch (NoSuchFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract boolean changeStatus();

	protected void createErrorOutput(String errorMessage) {
		output.add(errorMessage);
	}
	
	protected void deleteSplitTaskFromTaskList() {
		int smallestTaskId = 0;
		int indexOfTaskToDelete = -1;
		
		for (int i = 0; i < taskList.size(); i++) {
			int taskId = taskList.get(i).getTaskId();
			if (taskId < 0 && taskId < smallestTaskId) {
				smallestTaskId = taskId;
				indexOfTaskToDelete = i;
			}
		}
		
		taskList.remove(indexOfTaskToDelete);
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
	
	public TaskObject getOriginalTask() {
		return originalTask;
	}
	
	public int getMarkIndex() {
		return index;
	}
	
	public void setMarkIndex(int index) {
		this.index = index;
	}
}
