package logic.search;
import logic.display.Display;
import logic.timeOutput.TimeOutput;

import common.TaskObject;
import common.CommandObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;


/**
 * Creates a Search object which facilitates the finding of tasks matching the search strings.
 * Search is a subclass of Display.
 * <br> Search can be implemented in 4 ways:
 * <br> 1) Search by title - searches for tasks where the title contains the search keyword
 * <br> 2) Search by date - searches for tasks where the date matches the search date (for deadlines)
 * or if the date falls between the start and end date (for events)
 * <br> 3) Search by time - searches for tasks where the time matches the search time (for deadlines)
 * or if the time falls between the start and end dates AND times (for events); i.e. for an event 
 * "overseas camp 5jan-9jan 12pm-8pm", a search of '7jan 4pm' will return this event but a search
 * of '1jan 4pm' will not.
 * <br> 4) Search by index - searches for a specific index and returns all tasks that are linked
 * to this index if it is a recurring task 
 * It stores all search results in an arraylist and calls the superclass Display, where the search
 * results would be displayed.
 * 
 * @author ChongYan, RuiBin
 *
 */

public class Search extends Display {
	
	/**
	 * @param matchedTasks - a list maintained by the search object which contains
	 * all the relevant tasks to the search strings
	 */
	private CommandObject commandObj;
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> matchedTasks = new ArrayList<TaskObject>();
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();
	
	private String searchTitle;
	private LocalDate searchDate;
	private LocalTime searchTime;
	private int searchIndex;
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
		printSearchInformation();
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
			searchDate = taskObj.getStartDateTime().toLocalDate();
			if (searchDate.compareTo(LocalDate.MAX) != 0) {
				isSearchDate = true;
			}
			searchTime = taskObj.getStartDateTime().toLocalTime();
			if (searchTime.compareTo(LocalTime.MAX) != 0) {
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
			matchedTasks = searchByDate(matchedTasks);
		}
		if (isSearchTime) {
			matchedTasks = searchByTime(matchedTasks);
		}
		if (isSearchIndex) {
			searchByIndex();
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
	
	// Finds all tasks that have the same start/end date as the search date, or if the search date
	// falls between the start and end dates (only for events)
	private ArrayList<TaskObject> searchByDate(ArrayList<TaskObject> list) {
		ArrayList<TaskObject> match = new ArrayList<TaskObject>();
		
		for (int i = 0; i < list.size(); i++) {
			LocalDate taskStartDate = list.get(i).getStartDateTime().toLocalDate();
			LocalDate taskEndDate = list.get(i).getEndDateTime().toLocalDate();
			
			if (list.get(i).getCategory().equals(CATEGORY_EVENT)) {
				if (searchDate.isAfter(taskStartDate) && searchDate.isBefore(taskEndDate)) {
					// if the search date is within the start and end dates of this event
					match.add(list.get(i));
				}
			} else {	// deadline
				if (searchDate.isEqual(taskStartDate) || searchDate.isEqual(taskEndDate)) {
					match.add(list.get(i));
				}
			}
		}

		return match;
	}
	
	// Finds all tasks that have the same start/end time as the search time, or if the search time
	// falls between the start and end times AND dates (only for events)
	private ArrayList<TaskObject> searchByTime(ArrayList<TaskObject> list) {
		ArrayList<TaskObject> match = new ArrayList<TaskObject>();
		
		for (int i = 0; i < list.size(); i++) {
			LocalTime taskStartTime = list.get(i).getStartDateTime().toLocalTime();
			LocalTime taskEndTime = list.get(i).getEndDateTime().toLocalTime();

			if (list.get(i).getCategory().equals(CATEGORY_EVENT)) {
				LocalDate taskStartDate = list.get(i).getStartDateTime().toLocalDate();
				LocalDate taskEndDate = list.get(i).getEndDateTime().toLocalDate();
				
				// if it is an event, it checks if there had been a search date that is within the start and end dates
				// if so, it then checks if the search time is within the start and end times
				if (searchDate.isAfter(taskStartDate) && searchDate.isBefore(taskEndDate) &&
						searchTime.isAfter(taskStartTime) && searchTime.isBefore(taskEndTime)) {
					match.add(list.get(i));
				}
			} else {
				if (searchTime.compareTo(taskStartTime) == 0 || searchTime.compareTo(taskEndTime) == 0) {
					match.add(list.get(i));
				}
			}
		}
		
		return match;
	}
	
	/**
	 * Retrieves the task contained in the last output task list via an index, and proceeds
	 * to output all the timings associated with the task
	 */
	private void searchByIndex() {
		assert (searchIndex > 0 && searchIndex <= lastOutputTaskList.size());

		boolean isFound = false;
		try {
			int taskIdToSearch = lastOutputTaskList.get(searchIndex-1).getTaskId();
			isFound = findTaskWithIndex(taskIdToSearch);
		} catch (NullPointerException e) {
			throw new NullPointerException("invalid index");
		}
	}
	
	private boolean findTaskWithIndex(int taskIdToSearch) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskIdToSearch) {
				TaskObject foundTask = taskList.get(i);
				setOutput(foundTask);
				return true;
			}
		}
		return false;
	}
	
	private void setOutput() {
		if (matchedTasks.isEmpty()) {
			output.add(String.format(MESSAGE_NO_RESULTS_FOUND));
		} else {
			output.addAll(super.runSpecificList(matchedTasks));
		}
	}
	
	private void setOutput(TaskObject foundTask) {
		output.add(String.format(MESSAGE_TIMINGS_FOUND, foundTask.getTitle()));
		if (foundTask.getIsRecurring()) {
			try {
				for (int i = 0; i < foundTask.getTaskDateTimes().size(); i++) {
					LocalDateTime startDateTime = foundTask.getTaskDateTimes().get(i).getStartDateTime();
					LocalDateTime endDateTime = foundTask.getTaskDateTimes().get(i).getEndDateTime();
					String timeOutput = TimeOutput.setEventTimeOutput(startDateTime, endDateTime);
					output.add(timeOutput);
				}
			} catch (Exception e) {
				output.add(MESSAGE_INVALID_RECURRENCE);
			}
		} else {
			TimeOutput.setEventTimeOutput(foundTask);
			output.add(foundTask.getTimeOutputString());
		}
	}
	
}
