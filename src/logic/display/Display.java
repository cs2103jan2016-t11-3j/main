//@@author A0124636H

package logic.display;

import java.util.ArrayList;
import java.util.logging.*;

import common.AtfLogger;
import common.TaskObject;
import logic.timeOutput.TimeOutput;

import static logic.constants.Strings.*;


/**
 *  The Display class is a parent of the Search class. <br>
 *  This class is called when the user inputs a search command with no search keyword, i.e. the entire task
 *  list is to be displayed. <br>
 *  There are 2 run methods: <br>
 *  (a) run() - This is called when the entire task list is to be displayed.
 *  (b) runSpecificList() - This is called by the child Search class, where an ArrayList<TaskObject> will be
 *	passed into the method and only the tasks in this list will be displayed.
 *  
 * @author ChongYan, RuiBin
 *
 */

public class Display {

	protected static Logger logger = AtfLogger.getLogger();
	
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> outputTaskList = new ArrayList<TaskObject>();
	private ArrayList<String> output = new ArrayList<String>();
	protected int lastSearchedIndex = -1;
	
	public Display() {
		
	}

	/**
	 * Default constructor for a Display object.
	 * @param taskList	existing ArrayList of TaskObjects which comprises the entire task list
	 */
	public Display(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	/**
	 * Default run method to display all tasks in the task list.
	 * @return output	Task list formatted for display in the GUI
	 */
	public ArrayList<String> run() {
		output.add(MESSAGE_DISPLAYING_ALL_TASKS);
		return this.display();
	}

	/**
	 * Run method called by the child Search class. Only the tasks in the ArrayList passed to this method
	 * will be displayed.
	 * @param newTaskList	Contains a filtered list of tasks
	 * @return output		Task list formatted for display in the GUI
	 */
	public ArrayList<String> runSpecificList(ArrayList<TaskObject> newTaskList) {
		this.taskList = newTaskList;
		return this.display();
	}
	
	/**
	 * Extracts task information from each TaskObject and puts them into an ArrayList<String> for display in
	 * the GUI. <br>
	 * If the task list is empty, a default message for an empty task list will be printed instead. <br>
	 * @return output	Task list formatted for display in the GUI
	 */
	private ArrayList<String> display() {
		if (taskList.isEmpty()) {
			logger.log(Level.INFO, "Task list is empty");
			output.add(MESSAGE_EMPTY_LIST);
		} else {
			logger.log(Level.INFO, "Displaying tasks");
			outputTaskList.addAll(taskList);
			TimeOutput.setTimeOutputForGui(taskList);
		}
			
		return output;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public ArrayList<TaskObject> getLastOutputTaskList() {
		return outputTaskList;
	}
	
	public int getLastSearchedIndex() {
		return lastSearchedIndex;
	}
	
}
