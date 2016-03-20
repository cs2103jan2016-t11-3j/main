package logic.search;

import logic.display.Display;
import logic.timeOutput.TimeOutput;
import common.TaskObject;
import common.CommandObject;
import java.util.ArrayList;
import java.time.LocalDateTime;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * Creates a Search object which facilitates the finding of tasks matching the
 * search strings. Search is a subclass of Display <br>
 * Search can be implemented in two ways <br>
 * 1) Search within the title of each task. If the task contains the search
 * strings, this task will be added to an ArrayList of matched tasks. <br>
 * 2) Search within the start/end dates of each task. If the task contains
 * either the start or the end time, it will be added to the same ArrayList of
 * matched tasks. Only applies for deadlines and events.
 * 
 * @author ChongYan, RuiBin
 *
 */

public class Search extends Display {

	/**
	 * @param matchedTasks
	 *            - a list maintained by the search object which contains all
	 *            the relevant tasks to the search strings
	 */
	private CommandObject commandObj;
	private TaskObject taskObj;
	private int index;
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> matchedTasks = new ArrayList<TaskObject>();
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();

	String searchKeyword;
	int searchDate;

	/**
	 * Constructor for a Search object
	 * 
	 * @param taskObj
	 *            - Contains the search strings which the user keyed in
	 * @param taskList
	 *            - Contains all tasks in Adult TaskFinder
	 */
	public Search(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.commandObj = commandObj;
		this.taskObj = commandObj.getTaskObject();
		this.index = commandObj.getIndex();
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}

	public ArrayList<String> getOutput() {
		return output;
	}

	public ArrayList<TaskObject> getLastOutputTaskList() {
		return super.getLastOutputTaskList();
	}

	/**
	 * Overrides Display's run(). <br>
	 * First searches within the titles of each task, if no results obtained, it
	 * will search within the start/end dates of each task.
	 */
	public ArrayList<String> run() {
		if (commandObj.getIndex() == -1) {
			if (taskObj.getStartDate() == -1) { // it is a title search
				searchKeyword = getSearchKeyword();
				searchByTitle(searchKeyword);
				outputSearchResults(searchKeyword);
			} else {
				// Switch to LocalDateTime
				searchDate = getSearchDate();
				searchByDate(searchDate);
				outputSearchResults(searchDate);
			}
		} else {
			try {
				searchByIndex(index); // only shows details on one task
			} catch (NullPointerException e) {
				output.add(MESSAGE_NULL_POINTER_EXCEPTION);
			}
		}

		return output;
	}

	private String getSearchKeyword() {
		return taskObj.getTitle().trim();
	}

	private int getSearchDate() {
		return taskObj.getStartDate();
	}

	// Finds all tasks that have the same start or end date as the search date
	private void searchByDate(int searchDate) {
		// Need to convert to LocalDateTime
		for (int i = 0; i < taskList.size(); i++) {
			int taskStartDate = taskList.get(i).getStartDate();
			int taskEndDate = taskList.get(i).getStartDate();
			if (searchDate == taskStartDate || searchDate == taskEndDate) {
				matchedTasks.add(taskList.get(i));
			}
		}
	}

	// Finds all tasks where the title contains a semblance of the search
	// keyword
	private void searchByTitle(String searchKeyword) {
		for (int i = 0; i < taskList.size(); i++) {
			// Gets the title of one task and splits it up into the individual
			// words
			String taskName = taskList.get(i).getTitle().toLowerCase();
			String[] splitTaskName = taskName.split(" ");
			boolean isMatch = false;

			// Checks if any of the individual words contain the search keyword
			// If yes, add the task to the arraylist and stop scanning the other
			// words for this task
			int j = 0;
			while (j < splitTaskName.length && !isMatch) {
				String str = splitTaskName[j];
				if (str.trim().contains(searchKeyword.toLowerCase()) && !isMatch) {
					matchedTasks.add(taskList.get(i));
					isMatch = true;
				}
				j++;
			}

		}
	}

	private void searchByIndex(int index) {
		index--;
		boolean isFound = false;
		if (index >= 0 && index < lastOutputTaskList.size()) {
			int taskIdToSearch = lastOutputTaskList.get(index).getTaskId();
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTaskId() == taskIdToSearch) {
					TaskObject foundTask = taskList.get(i);
					outputSearchResults(foundTask);
					isFound = true;
				}
			}
		} else {
			NullPointerException e = new NullPointerException("invalid index");
			throw e;
		}

		if (!isFound) {
			outputSearchResults();
			// Message that cannot be found
		}
	}

	private void outputSearchResults(String searchKeyword) {
		if (matchedTasks.isEmpty()) {
			output.add(String.format(MESSAGE_NO_TITLE_RESULTS_FOUND, searchKeyword));
		} else {
			output.addAll(super.runSpecificList(matchedTasks));
		}
	}

	private void outputSearchResults(int searchDate) {
		if (matchedTasks.isEmpty()) {
			output.add(String.format(MESSAGE_NO_DATE_RESULTS_FOUND));
		} else {
			output.addAll(super.runSpecificList(matchedTasks));
		}
	}

	private void outputSearchResults(TaskObject foundTask) {
		if (foundTask.getIsRecurring()) {
			try {
				output = TimeOutput.setRecurringEventTimeOutput(foundTask);
			} catch (Exception e) {
				output.add(MESSAGE_INVALID_RECURRENCE);
			}
		} else {
			TimeOutput.setEventTimeOutput(foundTask);
			output.add(String.format(MESSAGE_TIMINGS_FOUND, foundTask.getTitle()));
			output.add(foundTask.getTimeOutputString());
		}
	}

	private void outputSearchResults() {
		output.add(String.format(MESSAGE_TIMINGS_NOT_FOUND));
	}

}
