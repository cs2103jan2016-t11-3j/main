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
	
	private static final String MESSAGE_NO_RESULTS_FOUND = "Keyword \'%1$s\' not found.";
	private static final String MESSAGE_SEARCH_RESULTS = "Search results:";
	
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> matchedTasks = new ArrayList<TaskObject>();
	private ArrayList<String> output = new ArrayList<String>();

	public Search(TaskObject taskObj, ArrayList<TaskObject> taskList) {
		this.taskObj = taskObj;
		this.taskList = taskList;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public ArrayList<String> run() {
		String searchKeyword = getSearchKeyword();
		
		searchForMatches(searchKeyword);
		outputSearchResults(searchKeyword);
		
		return output;
	}

	// This needs to be improved 
	private String getSearchKeyword() {
		return taskObj.getTitle();
	}

	// Searches the task names based on the search keyword 
	private void searchForMatches(String searchKeyword) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTitle().toLowerCase();
			if (Arrays.asList(taskName.split(" ")).contains(searchKeyword.toLowerCase())) {
				matchedTasks.add(taskList.get(i));
			}
		}
	}
	
	private void outputSearchResults(String searchKeyword) {
		if (matchedTasks.isEmpty()) {
			output.add(String.format(MESSAGE_NO_RESULTS_FOUND, searchKeyword));
		} else {
			output.add(MESSAGE_SEARCH_RESULTS);
			output.addAll(super.runSpecificList(matchedTasks));
		}
	}
	
}
