package logic.search;
import logic.display.Display;
import logic.timeOutput.TimeOutput;

import common.TaskObject;
import common.CommandObject;
import java.util.ArrayList;
import java.util.logging.Level;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

import com.sun.media.jfxmedia.logging.Logger;


/**
 * Creates a Search object which facilitates the finding of tasks matching the search strings.
 * Search is a subclass of Display
 * <br> Search can be implemented in two ways
 * <br> 1) Search within the title of each task. If the task contains the search strings,
 * this task will be added to an ArrayList of matched tasks.
 * <br> 2) Search within the start/end dates of each task. If the task contains either the start
 * or the end time, it will be added to the same ArrayList of matched tasks. Only applies
 * for deadlines and events.
 * @author ChongYan, RuiBin
 *
 */

public class Search extends Display {
	
	private static final String MESSAGE_NO_RESULTS_FOUND = "No results found for the specified parameters.";
	
	/**
	 * @param matchedTasks - a list maintained by the search object which contains
	 * all the relevant tasks to the search strings
	 */
	private CommandObject commandObj;
	private int searchIndex;
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> matchedTasks = new ArrayList<TaskObject>();
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();
	
	String searchTitle;
	int searchDate;
	int searchTime;
	boolean isSearchTitle = false;
	boolean isSearchDate = false;
	boolean isSearchTime = false;
	boolean isSearchIndex = false;
	
	/**
	 * Constructor for a Search object
	 * @param taskObj - Contains the search strings which the user keyed in
	 * @param taskList - Contains all tasks in Adult TaskFinder
	 */

	public Search(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.commandObj = commandObj;
		this.taskObj = commandObj.getTaskObject();
		this.searchIndex = commandObj.getIndex();
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
	 * First searches within the titles of each task, if no results obtained,
	 * it will search within the start/end dates of each task.
	 */
	public ArrayList<String> run() {
		setSearchInformation();
		processSearch();
		setOutput();
		
		return output;
	}
	
	// Retrieves values from the data objects and sets the relevant search information
	private void setSearchInformation() {
		try {
			searchTitle = taskObj.getTitle();
			if (!searchTitle.equals("")) {
				isSearchTitle = true;
			}
			searchDate = taskObj.getStartDate();
			if (searchDate != -1) {
				isSearchDate = true;
			}
			searchTime = taskObj.getStartTime();
			if (searchTime != -1) {
				isSearchTime = true;
			}
			searchIndex = commandObj.getIndex();
			if (searchIndex != -1) {
				isSearchIndex = true;
			}
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "Error setting search information");
		}
		
		//printSearchInformation();
	}
	
	// FOR DEBUG
	private void printSearchInformation() {
		System.out.println("search title = " + searchTitle);
		System.out.println("search date = " + searchDate);
		System.out.println("search time = " + searchTime);
		System.out.println("isSearchTitle = " + isSearchTitle);
		System.out.println("isSearchDate = " + isSearchDate);
		System.out.println("isSearchTime = " + isSearchTime);
	}

	private void processSearch() {
		matchedTasks = taskList;
		
		if (isSearchTitle) {
			matchedTasks = searchByTitle(matchedTasks);
		}
		if (isSearchDate) {
			System.out.println("matchedTasks size = " + matchedTasks.size());
			matchedTasks = searchByDate(matchedTasks);
		}
		if (isSearchTime) {
			matchedTasks = searchByTime(matchedTasks);
		}
		if (isSearchIndex) {
			searchByIndex(searchIndex);
		}
		
	}	
	
	// Finds all tasks where the title contains a semblance of the search keyword
	private ArrayList<TaskObject> searchByTitle(ArrayList<TaskObject> list) {
		ArrayList<TaskObject> match = new ArrayList<TaskObject>();

		for (int i = 0; i < list.size(); i++) {
			// Gets the title of one task and splits it up into the individual words
			String taskName = list.get(i).getTitle().toLowerCase();
			String[] splitTaskName = taskName.split(" ");
			boolean isMatch = false;
			
			// Checks if any of the individual words contain the search keyword
			// If yes, add the task to the arraylist and stop scanning the other words for this task
			int j = 0;
			while (j < splitTaskName.length && !isMatch) {
				String str = splitTaskName[j];
				if (str.trim().contains(searchTitle.toLowerCase())) {
					match.add(list.get(i));
					isMatch = true;
				}
				j++;
			}
		}
		
		return match;
	}
	
	// Finds all tasks that have the same start or end date as the search date
	private ArrayList<TaskObject> searchByDate(ArrayList<TaskObject> list) {
		ArrayList<TaskObject> match = new ArrayList<TaskObject>();
		
		for (int i = 0; i < list.size(); i++) {
			int taskStartDate = list.get(i).getStartDate();
			int taskEndDate = list.get(i).getEndDate();
			if (searchDate == taskStartDate || searchDate == taskEndDate) {
				match.add(list.get(i));
			}
		}

		return match;
	}
	
	// Finds all tasks that have the same start start or end time as the search time
	private ArrayList<TaskObject> searchByTime(ArrayList<TaskObject> list) {
		ArrayList<TaskObject> match = new ArrayList<TaskObject>();
		
		for (int i = 0; i < list.size(); i++) {
			int taskStartTime = list.get(i).getStartTime();
			int taskEndTime= list.get(i).getEndTime();
			if (searchTime == taskStartTime || searchDate == taskEndTime) {
				match.add(list.get(i));
			}
		}
		
		return match;
	}
	
	private void searchByIndex(int index) {
		index--;
		boolean isFound = false;
		if (index >= 0 && index < lastOutputTaskList.size()) {
			int taskIdToSearch = lastOutputTaskList.get(index).getTaskId();
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTaskId() == taskIdToSearch) {
					TaskObject foundTask = taskList.get(i);
					setOutput(foundTask);
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
	
	private void setOutput() {
		if (matchedTasks.isEmpty()) {
			output.add(String.format(MESSAGE_NO_RESULTS_FOUND));
		} else {
			output.addAll(super.runSpecificList(matchedTasks));
		}
	}
	
	private void setOutput(TaskObject foundTask) {
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
