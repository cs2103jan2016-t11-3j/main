package logic.delete;

import storage.*;
import common.AtfLogger;
import common.CommandObject;
import common.LocalDateTimePair;
import common.TaskObject;
import logic.timeOutput.TimeOutput;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.*;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * Creates a "Delete" object to facilitate the deletion of a task from task list
 * internally, before updating the file at its default location. <br>
 * 
 * There are five ways which Delete can be run: <br>
 * 1) Quick delete <br>
 * Input command: 'delete'
 * Pre-condition that user has to add a task in his last command. Quick delete does not require 
 * the user to input an index for deletion, it automatically deletes the last added task. 
 * This will remove the entire task and all its occurrences, even if it is a recurring task. <br>
 * 
 * 2) Delete all <br>
 * Input command: 'delete all'
 * This clears all lists - task list, undo list and redo list. 
 * 
 * 3) Normal delete <br>
 * Input command: 'delete [index]'
 * Pre-condition that user has to use a search/display function first. With that
 * task list which was displayed, the user proceeds to decide which item in the
 * list he wishes to delete. <br>
 * 
 * 4) Normal delete on recurring task <br>
 * Input command: 'delete [index]'
 * If the task to be deleted is a recurring task, the upcoming occurrence will be deleted and be
 * replaced by the second occurrence.
 *  
 * 5) Delete all on recurring task
 * Input command: 'delete [index] all'
 * The recurring task and all related occurrences will be deleted.
 *
 * @author ChongYan, RuiBin
 *
 */

public class Delete {

	private static Logger logger = AtfLogger.getLogger();

	// This command object contains the index number of the line to be deleted
	private CommandObject commandObj;

	private TaskObject removedTask = new TaskObject();	// Task that is removed
	private int removedTaskIndex = -1;	// Stores the position of the task to be removed in the taskList 
	private int removedOccurrenceIndex = 1; // Stores the index of the timings to be removed (Only for recurrence and single occurrence delete)
	private ArrayList<LocalDateTimePair> originalRecurrenceTimings = new ArrayList<LocalDateTimePair>();	// stores the original timings
	private LocalDateTimePair removedTaskOccurrenceDetails = new LocalDateTimePair(); 	// Stores the details of the removed occurrence of the task
	// Actual name of the task which is to be deleted
	private String removedTaskName = "";
	// Actual task ID of the task requested to be deleted
	private int taskIdToBeDeleted = -1;
	// Check if the task to be deleted is a recurring task
	private boolean isRecurringTask = false;

	// Attributes that should be passed in when the delete object is first constructed
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> tempOutput = new ArrayList<String>();
	private ArrayList<String> output = new ArrayList<String>();
	private Deque<CommandObject> undoList = new ArrayDeque<CommandObject>();
	private Deque<CommandObject> redoList = new ArrayDeque<CommandObject>();
	private int lastSearchedIndex;

	// Internal checkers
	private boolean isDeleteSingleOccurrence = false;
	private boolean isDeleteAll = false;
	private boolean hasDeletedInternal = false;
	private boolean hasDeletedExternal = false;

	
	// Constructors
	public Delete() {

	}

	/**
	 * Default constructor for Quick Delete. <br>
	 * There will be an additional CommandObject initialised, with an index of
	 * -1.
	 * 
	 * @param taskList
	 *            Existing list of tasks in Adult TaskFinder
	 * @param undoList
	 *            Current stack of CommandObjects with the purpose of undoing
	 *            previous actions
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, Deque<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.commandObj = commandObj;
	}
	
	// Constructor for test cases
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.commandObj = commandObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	/**
	 * Default constructor for Normal Delete.
	 * 
	 * @param commandObj
	 *            Contains the index to delete from the last output task list
	 * @param taskList
	 *            Existing list of tasks in Adult TaskFinder
	 * @param lastOutputTaskList
	 *            List of tasks outputted in the last command (e.g. Search,
	 *            Display)
	 * @param undoList
	 *            Deque containing the list of undo tasks
	 * @param redoList
	 *            Deque containing the list of redo tasks
	 */
	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList,
			Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		this.commandObj = commandObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
		this.undoList = undoList;
		this.redoList = redoList;
		this.lastSearchedIndex = commandObj.getLastSearchedIndex();
	}

	/**
	 * Called by logic to find and delete an object in the task list.
	 * Automatically decides whether to use quick delete or normal delete based
	 * on the index of the CommandObject in the Delete object.
	 * 
	 * @return output: ArrayList<String> - Contains all the output that the user
	 *         will see
	 */
	public ArrayList<String> run() {
		assert (!taskList.isEmpty());

		try {
			if (commandObj.getIndex() == -1) {
				runQuickDelete();
			} else if (commandObj.getIndex() == 0) {
				if (commandObj.getTaskObject().getStatus().equals("completed")) {
					runDeleteCompletedTasks();
				} else {
					runDeleteAll();
				}
			} else {
				checkIfDeleteSingleOccurrence();	
				
				if (isDeleteSingleOccurrence) {
					setDeleteInformationForSingleOccurrenceDelete();
					processDeleteForSingleOccurrence(); // deletes a single occurrence of recurring task
				} else {
					setDeleteInformationForNormalDelete();
					
					if (isRecurringTask) {
						if (commandObj.getTaskObject().getIsEditAll()) {	
							runNormalDelete();	// deletes entire task
						} else {
							processDeleteForSingleOccurrence();
						}
					} else {
						runNormalDelete();
					}
				}
			}
		} catch (NullPointerException e) {
			tempOutput.add(MESSAGE_DELETE_ERROR + MESSAGE_NULL_POINTER);
		} catch (IndexOutOfBoundsException e) {
			tempOutput.add(MESSAGE_DELETE_ERROR + MESSAGE_INDEX_OUT_OF_BOUNDS);
		}
		
		concatenateOutput();
		return output;
	}

	/**
	 * Main method driving the quick delete function. Checks if the top of the
	 * undoList contains a CommandObject with a delete command, and proceeds to
	 * remove the task if it is the case.
	 */
	private void runQuickDelete() throws NullPointerException, IndexOutOfBoundsException {
		if (undoList.isEmpty()) {
			createErrorOutput();
		} else if (undoList.peek().getCommandType() == INDEX_DELETE) {
			assert (!taskList.isEmpty());
			
			setDeleteInformationForQuickDelete();
			hasDeletedInternal = deleteInternal();
			hasDeletedExternal = deleteExternal();
			
			if (hasDeletedInternal && hasDeletedExternal) {
				createOutput();
				logger.log(Level.INFO, "Quick delete executed");
			} else {
				createErrorOutput();
			}
		} else {
			createQuickDeleteUnavailableErrorOutput();
		}
	}
	
	// Deletes all completed tasks from the task list
	private void runDeleteCompletedTasks() {
		for (int i = 0; i < taskList.size(); i++) {
			System.out.println(taskList.get(i).getTitle() + " " + taskList.get(i).getStatus());
			if (taskList.get(i).getStatus().equals(STATUS_COMPLETED)) {
				System.out.println("deleted: " + taskList.get(i).getTitle());
				taskList.remove(i);
				i--; // since all remaining index of taskList will be reduced by 1
			}
		}
		deleteExternal();
		createCompletedTasksDeletedOutput();
	}
	
	// Clears everything - task list, undo list, redo list and the storage file
	private void runDeleteAll() {
		taskList.clear();
		undoList.clear();
		redoList.clear();
		deleteExternal();
		logger.log(Level.INFO, "Delete all executed");
		
		isDeleteAll = true;
		createDeletedAllOutput();
	}
	
	private void processDeleteForSingleOccurrence() {
		if (removedTask.getTaskDateTimes().size() > 1){
			runSingleOccurrenceDelete();
		} else { // if there is only 1 occurrence left, delete the entire task
			createOnlyOneOccurrenceRemainingOutput();
			runNormalDelete();
		}
	}
	
	// Delete is handled differently if it is a recurring task
	private void runNormalDelete() throws NullPointerException, IndexOutOfBoundsException {
		assert (!taskList.isEmpty());
		
		hasDeletedInternal = deleteInternal();
		
		if (hasDeletedInternal) {
			hasDeletedExternal = deleteExternal();
			if (hasDeletedExternal) {
				createOutput();
				logger.log(Level.INFO, "Normal delete executed");
			}
		} else {
			createErrorOutput();
		}
	}
	
	// Gets the array list of LocalDateTimePair from the task and removes the specified occurrence
	private void runSingleOccurrenceDelete() throws NullPointerException {
		try {
			ArrayList<LocalDateTimePair> taskDateTimes = removedTask.getTaskDateTimes();
			originalRecurrenceTimings.addAll(taskDateTimes);
			assert (taskDateTimes.size() > 1);

			removedTaskOccurrenceDetails = taskDateTimes.remove(removedOccurrenceIndex - 1);
			removedTask.addToDeletedTaskDateTimes(removedTaskOccurrenceDetails);
			removedTask.setTaskDateTimes(taskDateTimes);
			removedTask.updateStartAndEndDateTimes();
			
			if (deleteExternal()) {
				TimeOutput.setTaskTimeOutput(removedTask); // to update the recurrence date in GUI
				createSingleOccurrenceOutput();
				logger.log(Level.INFO, "Single occurrence delete executed");
			}
		} catch (IndexOutOfBoundsException e) {
			createSingleOccurrenceMissingErrorOutput();
		}
		
		// might need additional handling for deletion of indefinite recurring tasks
	}
	
	// ----------------------- PROCESSING DELETE -----------------------
	
	private void checkIfDeleteSingleOccurrence() {
		if (lastSearchedIndex != -1) {
			isDeleteSingleOccurrence = true;
			removedOccurrenceIndex = commandObj.getIndex();
		}
	}
	
	private void setDeleteInformationForQuickDelete() {
		removedTaskIndex = taskList.size() - 1;
		removedTask = taskList.get(removedTaskIndex);
		removedTaskName = removedTask.getTitle();
	}
	
	private void setDeleteInformationForSingleOccurrenceDelete() {
		removedTaskIndex = lastSearchedIndex - 1;
		removedTask = lastOutputTaskList.get(removedTaskIndex);
		removedTaskName = removedTask.getTitle();
	}
	
	private void setDeleteInformationForNormalDelete() {
		setTaskIdToBeDeleted();
		setRemovedTask();
		setRemovedTaskName();
		setIsRecurringTask();
		
		logger.log(Level.INFO, "Set delete information for normal delete");
	}
	
	private void setTaskIdToBeDeleted() {
		int index = commandObj.getIndex();
		assert (index > 0 && index <= lastOutputTaskList.size());

		taskIdToBeDeleted = lastOutputTaskList.get(index - 1).getTaskId();
	}
	
	private void setRemovedTask() {
		assert (taskIdToBeDeleted > 0);
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskIdToBeDeleted) {
				removedTask = taskList.get(i);
				removedTaskIndex = i;
				lastSearchedIndex = i+1;
			}
		}
	}
	
	private void setRemovedTaskName() {
		removedTaskName = removedTask.getTitle();
	}
	
	private void setIsRecurringTask() {
		isRecurringTask = removedTask.getIsRecurring();
	}

	private boolean deleteInternal() {
		try {
			taskList.remove(removedTaskIndex);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	private boolean deleteExternal() {
		FileStorage storage = FileStorage.getInstance();
		try {
			storage.save(taskList);
			logger.log(Level.INFO, "Storage file replaced");
		} catch (NoSuchFileException e) {
			// TODO Auto-generated catch block
			// Ask user to specify new location or use default location
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// ----------------------- CREATING OUTPUT -----------------------
	
	private void createOutput() {
		if (isRecurringTask) {
			tempOutput.add(String.format(MESSAGE_ALL_OCCURRENCES_DELETE));
		} else {
			tempOutput.add(String.format(MESSAGE_DELETE, removedTaskName));
		}
	}

	private void createErrorOutput() {
		removedTask = null;
		tempOutput.add(MESSAGE_DELETE_ERROR);
	}

	private void createQuickDeleteUnavailableErrorOutput() {
		removedTask = null;
		tempOutput.add(MESSAGE_QUICK_DELETE_UNAVAILABLE_ERROR);
	}
	
	private void createCompletedTasksDeletedOutput() {
		tempOutput.add(MESSAGE_COMPLETED_TASKS_DELETE);
	}

	private void createDeletedAllOutput() {
		tempOutput.add(MESSAGE_DELETED_ALL);
	}
	
	private void createSingleOccurrenceOutput() {
		if (removedOccurrenceIndex == 1 && isRecurringTask) {
			tempOutput.add(String.format(MESSAGE_MOST_RECENT_OCCURRENCE_DELETE));
		} else {
			tempOutput.add(String.format(MESSAGE_SINGLE_OCCURRENCE_DELETE, removedOccurrenceIndex));
		}
	}
	
	private void createSingleOccurrenceMissingErrorOutput() {
		removedTask = null;
		tempOutput.add(MESSAGE_SINGLE_OCCURENCE_MISSING_ERROR);
		
	}
	
	private void createOnlyOneOccurrenceRemainingOutput() {
		tempOutput.add(MESSAGE_ONLY_ONE_OCCURRENCE_REMAINING);
	}
	
	private void concatenateOutput() {
		assert (!tempOutput.isEmpty());

		String concatOutput = "";
		for (int i = 0; i < tempOutput.size(); i++) {
			concatOutput = concatOutput.concat(tempOutput.get(i));
		}
		
		output.add(concatOutput.trim());
	}

	// ----------------------- GETTERS AND SETTERS -----------------------
	
	public CommandObject getCommandObject() {
		return commandObj;
	}

	public TaskObject getRemovedTask() {
		return removedTask;
	}
	
	public int getRemovedOccurrenceIndex() {
		return removedOccurrenceIndex;
	}

	public ArrayList<String> getOutput() {
		return output;
	}
	
	public ArrayList<LocalDateTimePair> getOriginalRecurrenceTimings() {
		return originalRecurrenceTimings;
	}
	
	public LocalDateTimePair getRemovedTaskOccurrenceDetails() {
		return removedTaskOccurrenceDetails;
	}

	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public Deque<CommandObject> getUndoList() {
		return undoList;
	}

	public Deque<CommandObject> getRedoList() {
		return redoList;
	}
	
	public boolean getIsDeleteAll() {
		return isDeleteAll;
	}
	
	public boolean getIsDeleteSingleOccurrence() {
		return isDeleteSingleOccurrence;
	}

	public void setTaskList(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public void setCommandObject(CommandObject commandObj) {
		this.commandObj = commandObj;
	}

	public void setTaskName(String removedTaskName) {
		this.removedTaskName = removedTaskName;
	}

	public void setRemovedTask(TaskObject removedTask) {
		this.removedTask = removedTask;
	}
	
	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}
	
	
}