package logic;

import java.util.ArrayList;
import java.util.Arrays;

/* Currently search function only supports for searching of the task name.
 * Can consider implementing the search function for a date/time in the future, 
 * e.g. 'search 24 feb' will return the list of deadlines due on 24th Feb.
 */
public class Search {
	
	private static final String MESSAGE_NO_RESULTS_FOUND = "Keyword \'%1$s\' not found.";
	private static final String MESSAGE_SEARCH_RESULTS = "Search results:";
	private static final String MESSAGE_RESULT = "%1$s. %2$s";
	
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private ArrayList<String> matchedLines = new ArrayList<String>();
	private ArrayList<String> output = new ArrayList<String>();

	public Search(TaskObject taskObj, ArrayList<TaskObject> taskList) {
		this.taskObj = taskObj;
		this.taskList = taskList;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	ArrayList<String> run() {
		String searchKeyword = getSearchKeyword();
		searchForMatches(searchKeyword);
		outputSearchResults(searchKeyword);
		
		return output;
	}

	private String getSearchKeyword() {
		return taskObj.getTitle();
	}

	// Searches the task names based on the search keyword 
	private void searchForMatches(String searchKeyword) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTitle();
			if (Arrays.asList(taskName.split(" ")).contains(searchKeyword)) {
				matchedLines.add(taskName);
			}
		}
	}
	
	private void outputSearchResults(String searchKeyword) {
		if (matchedLines.isEmpty()) {
			output.add(String.format(MESSAGE_NO_RESULTS_FOUND, searchKeyword));
		} else {
			output.add(MESSAGE_SEARCH_RESULTS);
			for (int i = 0; i < matchedLines.size(); i++) {
				output.add(String.format(MESSAGE_RESULT, i+1, matchedLines.get(i)));
			}
		}
	}
	
}
