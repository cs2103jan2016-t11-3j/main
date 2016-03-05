package logic.display;
import logic.TaskObject;

import java.util.ArrayList;

/* Sample output formats are listed as follows.
 * (a) Example output for deadline: 
 *	   1. CS2103 v0.1, 11/03/2016, 1600hrs, pending
 * (b) Example output for event:
 *     2. Pulau Ubin Camp, 15/06/2009-17/06/2009, 0800hrs-1300hrs, completed
 * (c) Example output for floating task:
 * 	   3. Study more, pending
 */

public class Display {
	
	private static final String MESSAGE_EMPTY_LIST = "Task list is empty.";
	private static final String MESSAGE_SEARCH_RESULTS = "Search results:";
	private static final String DISPLAY_RESULT_DEADLINE = "%1$s. %2$s, %3$s, %4$shrs, %5$s";
	private static final String DISPLAY_RESULT_EVENT = "%1$s. %2$s, %3$s-%4$s, %5$shrs-%6$shrs, %7$s";
	private static final String DISPLAY_RESULT_FLOATING = "%1$s. %2$s, %3$s";
	
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> outputTaskList = new ArrayList<TaskObject>();
	private ArrayList<String> output = new ArrayList<String>();
	
	public Display() {
		
	}

	public Display(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	public ArrayList<TaskObject> getLastOutputTaskList() {
		return outputTaskList;
	}
	
	// For search function
	public ArrayList<String> runSpecificList(ArrayList<TaskObject> newTaskList) {
		this.taskList = newTaskList;
		return this.display();
	}
	
	public ArrayList<String> run() {
		return this.display();
	}
	
	private ArrayList<String> display() {
		if (taskList.isEmpty()) {
			outputEmptyMessage();
		} else {
			output.add(MESSAGE_SEARCH_RESULTS);
			for (int i = 0; i < taskList.size(); i++) {
				TaskObject task = taskList.get(i);
				outputTaskList.add(task);
				
				String taskCategory = task.getCategory();
				String taskTitle = task.getTitle();
				int taskStartDate = task.getStartDate();
				int taskEndDate = task.getEndDate();
				int taskStartTime = task.getStartTime();
				int taskEndTime = task.getEndTime();
				String taskStatus = task.getStatus();
				
				/* Output format for the tasks differs according to the category.
				 * This switch statement calls the relevant method and passes in the relevant arguments.
				 */
				switch (taskCategory) {
					case "deadline":
						String taskEndDateInOutputFormat = parseDate(taskEndDate);
						outputDeadlineTask(i+1, taskTitle, taskEndDateInOutputFormat, taskEndTime, taskStatus);
						break;
					case "event":
						String taskStartDateInOutputFormat = parseDate(taskStartDate);
						taskEndDateInOutputFormat = parseDate(taskEndDate);
						outputEventTask(i+1, taskTitle, taskStartDateInOutputFormat, taskEndDateInOutputFormat, taskStartTime, taskEndTime, taskStatus);
						break;
					case "floating": case "null": // REMOVE NULL AFTER TESTING
						outputFloatingTask(i+1, taskTitle, taskStatus);
						break;
					default:
						break;
				}
				
			}
		}
		
		return output;
	}
	
	private void outputEmptyMessage() {
		output.add(MESSAGE_EMPTY_LIST);
	}

	// Returns the date in DD/MM/YYYY format
	private String parseDate(int date) {
		String dateInString = "" + date;
		String day = dateInString.substring(6, 8);
		String month = dateInString.substring(4, 6);
		String year = dateInString.substring(0, 4);
		
		return day + "/" + month + "/" + year;
	}
	
	private void outputDeadlineTask(int num, String taskTitle, String taskEndDate, int taskEndTime, String taskStatus) {
		output.add(String.format(DISPLAY_RESULT_DEADLINE, num, taskTitle, taskEndDate, taskEndTime, taskStatus));
	}
	
	private void outputEventTask(int num, String taskTitle, String taskStartDate, String taskEndDate, 
								 int taskStartTime, int taskEndTime, String taskStatus) {
		output.add(String.format(DISPLAY_RESULT_EVENT, num, taskTitle, taskStartDate, taskEndDate, taskStartTime, taskEndTime, taskStatus));
	}
	
	private void outputFloatingTask(int num, String taskTitle, String taskStatus) {
		output.add(String.format(DISPLAY_RESULT_FLOATING, num, taskTitle, taskStatus));
	}
}
