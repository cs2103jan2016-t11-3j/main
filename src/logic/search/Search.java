package logic.search;
import logic.display.Display;
import logic.TaskObject;

import java.util.ArrayList;
import java.util.Arrays;

/* Currently search function only supports for searching of the task name.
 * Can consider implementing the search function for a date/time in the future, 
 * e.g. 'search 24 feb' will return the list of deadlines due on 24th Feb.
 */
public class Search extends Display {
	
	private static final String MESSAGE_NO_TITLE_RESULTS_FOUND = "Keyword \'%1$s\' not found.";
	private static final String MESSAGE_NO_DATE_RESULTS_FOUND = "No task found for the specified date.";
	
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> matchedTasks = new ArrayList<TaskObject>();
	private ArrayList<String> output = new ArrayList<String>();
	
	String searchKeyword;
	int searchDate;
	
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
			String taskName = taskList.get(i).getTitle().toLowerCase();
			if (Arrays.asList(taskName.split(" ")).contains(searchKeyword.toLowerCase())) {
				matchedTasks.add(taskList.get(i));
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
