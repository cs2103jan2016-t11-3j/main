package logic.search;
import logic.display.Display;

import common.TaskObject;
import java.util.ArrayList;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;


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
	
	/**
	 * @param matchedTasks - a list maintained by the search object which contains
	 * all the relevant tasks to the search strings
	 */
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> matchedTasks = new ArrayList<TaskObject>();
	private ArrayList<String> output = new ArrayList<String>();
	
	String searchKeyword;
	int searchDate;
	
	/**
	 * Constructor for a Search object
	 * @param taskObj - Contains the search strings which the user keyed in
	 * @param taskList - Contains all tasks in Adult TaskFinder
	 */
	public Search(TaskObject taskObj, ArrayList<TaskObject> taskList) {
		this.taskObj = taskObj;
		this.taskList = taskList;
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
		if (taskObj.getStartDate() == -1) { // it is a title search
			searchKeyword = getSearchKeyword();
			searchByTitle(searchKeyword);
			outputSearchResults(searchKeyword);
		} else {
			searchDate = getSearchDate();
			searchByDate(searchDate);
			outputSearchResults(searchDate);
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
		for (int i = 0; i < taskList.size(); i++) {
			int taskStartDate = taskList.get(i).getStartDate();
			int taskEndDate = taskList.get(i).getStartDate();
			if (searchDate == taskStartDate || searchDate == taskEndDate) {
				matchedTasks.add(taskList.get(i));
			}
		}
	}
		
	// Finds all tasks where the title contains a semblance of the search keyword
	private void searchByTitle(String searchKeyword) {
		for (int i = 0; i < taskList.size(); i++) {
			// Gets the title of one task and splits it up into the individual words
			String taskName = taskList.get(i).getTitle().toLowerCase();
			String[] splitTaskName = taskName.split(" ");
			boolean isMatch = false;
			
			// Checks if any of the individual words contain the search keyword
			// If yes, add the task to the arraylist and stop scanning the other words for this task
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

}
