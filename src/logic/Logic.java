package logic;

import parser.*;
import storage.FileStorage;
import logic.add.*;
import logic.delete.*;
import logic.display.*;
import logic.edit.*;
import logic.mark.*;
import logic.search.*;
import logic.undo.*;
import logic.save.*;
import logic.help.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import common.CommandObject;
import common.TaskObject;

// Parent class for Undo

/* On startup, should load taskList automatically from storage, and mark all events which are
 * overdue. Consider displaying all overdue tasks or alert
 */

/**
 * Main driver for Adult TaskFinder. Upon initialisation of the object, retrieves all
 * existing tasks from an external file source and places them into an ArrayList of 
 * TaskObjects. The main Logic object initialised in the GUI will exist until exit command is
 * inputted. <br>
 * Alternatively, secondary Logic objects may be initialised when Undo or Redo commands are
 * given. In this case, secondary Logic objects will only carry out the specific task 
 * required before it "dies".
 * @param taskList - Initialised as an empty list of TaskObjects,
 * will maintain all TaskObjects existing in Adult TaskFinder internally throughout the 
 * runtime of the program.
 * @param undoList - Stack of CommandObjects stored for undoing. Every
 * time a command is executed, the reverse of that command will be pushed into undoList in 
 * the form of a CommandObject.
 * @param redoList - Stack of CommandObjects stored for redoing. Every 
 * time a command is popped from the undoList for undoing, the reverse of that command 
 * will be pushed into the redoList as an CommandObject. Clears itself whenever the user
 * inputs a command which is not "undo".
 * @author ChongYan
 *
 */
public class Logic {

	public static final int INDEX_ADD = 1;
	public static final int INDEX_SEARCH_DISPLAY = 2;
	public static final int INDEX_EDIT = 3;
	public static final int INDEX_DELETE = 4;
	public static final int INDEX_UNDO = 5;
	public static final int INDEX_REDO = 6;
	public static final int INDEX_SAVE = 7;
	public static final int INDEX_EXIT = 8;
	public static final int INDEX_HELP = 9;
	// A set of indicators for task status modifiers;
	public static final int INDEX_DONE = 10;
	public static final int INDEX_OVERDUE = 11;
	public static final int INDEX_INCOMPLETE = 12;

	private static final String MESSAGE_INVALID_COMMAND = "Invalid command";

	// Maintained throughout the entire running operation of the program
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	private Deque<CommandObject> undoList = new ArrayDeque<CommandObject>();
	private Deque<CommandObject> redoList = new ArrayDeque<CommandObject>();
	private int taskId = 1;

	// This variable will get repeatedly updated by UI for each input
	private String userInput;
	// Output is to be returned to UI after each command
	private ArrayList<String> output = new ArrayList<String>();
	// Keeps track of the list that is constantly displayed in UI
	private ArrayList<TaskObject> lastOutputTaskList = new ArrayList<TaskObject>();

	/**
	 * Constructor called by UI. Loads all existing tasks and checks each task to see
	 * whether any of them are overdue, and updates their corresponding statuses.
	 */
	public Logic() {
		taskList = new ArrayList<TaskObject>();
		undoList = new ArrayDeque<CommandObject>();
		redoList = new ArrayDeque<CommandObject>();
		loadTaskList();
		checkOverdue();
	}
	
	/**
	 * Constructor called by Undo/Redo. This is a secondary logic class which only performs
	 * one operation before being deactivated.
	 * @param taskList The default taskList storing all the tasks
	 * @param undoList The stack of CommandObjects which stores all undo actions
	 * @param redoList The stack of CommandObjects which stores all redo actions
	 */
	public Logic(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.redoList = redoList;
		this.lastOutputTaskList = taskList;
	}

	// Loads all existing tasks into the program from Storage
	private void loadTaskList() {
		try {
			FileStorage storage = FileStorage.getInstance();
			taskList = storage.load();
			setLastOutputTaskList(taskList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Takes in a String argument from UI component
	public void run(String userInput) {
		setUserInput(userInput);
		CommandObject commandObj = callParser();
		parseCommandObject(commandObj, false, false);
	}

	/**
	 * Calls Parser to parse the user input
	 * @return CommandObject containing information on the task to be manipulated, as well
	 * as the command to execute
	 */
	private CommandObject callParser() {
		Parser parser = new Parser(userInput, taskId);
		taskId++;
		return parser.run();
	}

	/**
	 * Parses the CommandObject and determines which command to execute.
	 * Responsible for manipulating the undoList and determining whether the redoList
	 * should be cleared. <br>
	 * The redoList will be cleared as long as the command given is not an undo or
	 * redo. <br>
	 * A "reverse" CommandObject will be created and pushed into the undoList if the 
	 * current CommandObject is an action which manipulates the existing task list. 
	 * @param commandObj CommandObject which contains information on the actions to 
	 * execute within the lower levels of the program
	 * @param isUndoAction a boolean value stating if this CommandObject is a result of
	 * an undo action
	 * @param isRedoAction a boolean value stating if this CommandObject is a result of 
	 * a redo action
	 */
	public void parseCommandObject(CommandObject commandObj, boolean isUndoAction, boolean isRedoAction) {
		int command = commandObj.getCommandType();
		TaskObject taskObj = commandObj.getTaskObject();
		int index = commandObj.getIndex();

		// FOR TESTING
		//System.out.println("CommandObject command = " + command + ", index = " + index);
		//System.out.println("isUndoAction = " + isUndoAction + ", undo size = "
		//+ undoList.size() + ", redo size = " + redoList.size());
		//printTaskObjectFields(taskObj);
		// System.out.println();

		// Clears the redo stack if it is a new command
		if (!redoList.isEmpty() && isListOperation(command) && !isUndoAction && !isRedoAction) {
			clearRedoList();
		}

		switch (command) {
		case INDEX_ADD:
			addFunction(taskObj, index);
			if (isUndoAction) {
				addToList(commandObj, redoList);
			} else {
				addToList(commandObj, undoList);
			}
			break;
		case INDEX_SEARCH_DISPLAY:
			checkDisplayOrSearch(taskObj);
			break;
		case INDEX_EDIT:
			Edit editOriginal = editFunction(commandObj);
			if (isUndoAction) {
				addToList(editOriginal, redoList);
			} else {
				addToList(editOriginal, undoList);
			}
			break;
		case INDEX_DELETE:
			TaskObject removedTask = new TaskObject();
			if (commandObj.getIndex() == -1) {
				// Quick-delete function for item recently added
				removedTask = deleteFunction(); // overloaded function
			} else {
				removedTask = deleteFunction(commandObj);
			}
			
			processUndoForDelete(command, removedTask, commandObj, isUndoAction);
			break;
		case INDEX_UNDO:
		case INDEX_REDO:
			undoRedoFunction(command);
			break;
		case INDEX_SAVE:
			saveFunction(taskObj);
			break;
		case INDEX_EXIT:
			exitFunction();
			break;
		case INDEX_HELP:
			helpFunction(taskObj);
			break;
		case INDEX_DONE:
			doneFunction(taskObj);
			break;
		case INDEX_OVERDUE:
			overdueFunction(taskObj);
			break;
		case INDEX_INCOMPLETE:
			incompleteFunction(taskObj);
			break;
		default:
			printInvalidCommandMessage();
			break;
		}
	}

	private void addFunction(TaskObject taskObj, int index) {
		Add add = new Add(taskObj, index, taskList);
		setOutput(add.run());
		setLastOutputTaskList(taskList);
	}

	/*
	 * This method checks for the presence of a search keyword in TaskObject. If
	 * there is a keyword, search function will be called. If there is no
	 * keyword, display function will be called.
	 */
	private void checkDisplayOrSearch(TaskObject taskObj) {
		if (taskObj.isSearchKeywordPresent())
			searchFunction(taskObj);
		else
			displayFunction();
	}

	// Outputs the entire task list
	private void displayFunction() {
		Display display = new Display(taskList);
		setOutput(display.run());
		setLastOutputTaskList(display.getLastOutputTaskList());
	}

	// Outputs only the tasks that match the search keyword
	private void searchFunction(TaskObject taskObj) {
		Search search = new Search(taskObj, taskList);
		setOutput(search.run());
		setLastOutputTaskList(search.getLastOutputTaskList());
	}

	private Edit editFunction(CommandObject commandObj) {
		Edit edit = new Edit(commandObj, lastOutputTaskList, taskList);
		setOutput(edit.run());
		setLastOutputTaskList(taskList);
		return edit;
	}

	private TaskObject deleteFunction(CommandObject commandObj) {
		Delete delete = new Delete(commandObj, taskList, lastOutputTaskList);
		setOutput(delete.run());
		setLastOutputTaskList(taskList);
		
		return delete.getRemovedTask();
	}

	private TaskObject deleteFunction() {
		Delete delete = new Delete(taskList, undoList);
		setOutput(delete.run());
		setLastOutputTaskList(taskList);
		
		return delete.getRemovedTask();
	}
	
	private void processUndoForDelete(int command, TaskObject removedTask, CommandObject commandObj, boolean isUndoAction) {
		if (removedTask != null) {
			CommandObject newCommandObj;
			if (commandObj.getIndex() == -1) {
				newCommandObj = new CommandObject(command, removedTask, -1);
			} else {
				newCommandObj = new CommandObject(command, removedTask, commandObj.getIndex());
			}
			
			if (isUndoAction) {
				addToList(newCommandObj, redoList);
			} else {
				addToList(newCommandObj, undoList);
			}
		}
	}

	private void undoRedoFunction(int command) {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		setOutput(undoRedo.run(command));

		// Update the task and undo lists
		setTaskList(undoRedo.getTaskList());
		setUndoList(undoRedo.getUndoList());
		setRedoList(undoRedo.getRedoList());
		setLastOutputTaskList(taskList);
	}

	private void saveFunction(TaskObject taskObj) {
		Save save = new Save(taskObj, taskList);
		setOutput(save.run());
	}

	private void exitFunction() {
		Exit exit = new Exit();
		exit.run();
	}

	private void doneFunction(TaskObject taskObj) {
		Done done = new Done(taskObj, taskList, lastOutputTaskList);
		setOutput(done.run());
		setLastOutputTaskList(taskList);
		if (done.getTaskIdToMark() != -1) { // If successfully marked as done
			CommandObject undoCommand = constructStatusCommandObject(done);
			// addToUndoList(commandIndex, new TaskObject(pastStatus,
			// done.getTaskIdToMark()));
		}
	}

	private void overdueFunction(TaskObject taskObj) {
		Overdue overdue = new Overdue(taskObj, taskList, lastOutputTaskList);
		setOutput(overdue.run());
		setLastOutputTaskList(taskList);
		if (overdue.getTaskIdToMark() != -1) {
			CommandObject undoCommand = constructStatusCommandObject(overdue);
		}
		// addToUndoList(commandIndex, new TaskObject(pastStatus,
		// overdue.getTaskIdToMark()));
	}

	private void incompleteFunction(TaskObject taskObj) {
		Incomplete incomplete = new Incomplete(taskObj, taskList, lastOutputTaskList);
		setOutput(incomplete.run());
		setLastOutputTaskList(taskList);
		if (incomplete.getTaskIdToMark() != -1) {
			CommandObject undoCommand = constructStatusCommandObject(incomplete);
			// addToUndoList(commandIndex, new TaskObject(pastStatus,
			// undone.getTaskIdToMark()));
		}
	}

	/**
	 * Constructs a CommandObject for either "done", "incomplete" or "overdue" 
	 * for the purpose of pushing it into the undoList
	 * @param statusChanger Mark object which performed the modification to the task list
	 * @return CommandObject which reverses the operation performed previously
	 */
	private CommandObject constructStatusCommandObject(Mark statusChanger) {
		CommandObject returnedCommand = new CommandObject();
		String pastStatus = statusChanger.getStatusToChange();
		int commandIndex = getCommandIndex(pastStatus);
		if (commandIndex != 0) {
			returnedCommand.setCommandType(commandIndex);
			returnedCommand.setTaskObject(statusChanger.getMarkedTask());
		}
		return returnedCommand;
	}

	private int getCommandIndex(String pastStatus) {
		if (pastStatus.equals("overdue")) {
			return INDEX_OVERDUE;
		} else {
			if (pastStatus.equals("completed")) {
				return INDEX_DONE;
			} else {
				if (pastStatus.equals("incomplete")) {
					return INDEX_INCOMPLETE;
				}
			}
		}
		return 0;
	}

	 //The following methods stores the reverse of the user input in the stack.
	 	
	/**
	 * Method for adding a CommandObject containing add or delete to either the undoList or redoList,
	 * which is previously determined by the caller. <br>
	 * For command "add", a "delete" CommandObject will be pushed into the list. The index
	 * of the previously added TaskObject will also be added into the list to facilitate
	 * future deletion. <br>
	 * For command "delete", an "add" CommandObject will be pushed into the list, together 
	 * with a copy of the task which was just deleted
	 * @param commandObj The CommandObject to be added to the stated list.
	 * @param list Either a undoList or a redoList
	 */
	private void addToList(CommandObject commandObj, Deque<CommandObject> list) {
		if (commandObj.getCommandType() == INDEX_ADD) {
			if (commandObj.getIndex() == -1) {
				// if task was previously added to the end of the list
				list.push(new CommandObject(INDEX_DELETE, new TaskObject(), taskList.size()));
			} else {
				// if task was previously added to a pre-determined location in the list
				list.push(new CommandObject(INDEX_DELETE, new TaskObject(), commandObj.getIndex()));
			}
		} else if (commandObj.getCommandType() == INDEX_DELETE) {
			list.push(new CommandObject(INDEX_ADD, commandObj.getTaskObject(), commandObj.getIndex()));
		} 
	}

	/**
	 * Method for adding a CommandObject containing edit to either the undoList or redoList,
	 * predetermined by the caller of this method. <br>
	 * 
	 * @param editOriginal Contains an Edit object which stores information on retrieving
	 * the original TaskObject prior to the edit.
	 * @param list Either an undoList or redoList
	 */
	private void addToList(Edit editOriginal, Deque<CommandObject> list) {
		CommandObject newCommandObj = new CommandObject();
		if (editOriginal.getIsEditTitle()) {
			String originalTitle = editOriginal.getOriginalTitle();
			newCommandObj = new CommandObject(INDEX_EDIT, new TaskObject(originalTitle),
					editOriginal.getEditItemNumber());
		} else if (editOriginal.getIsEditDate()) {
			int originalDate = editOriginal.getOriginalDate();
			newCommandObj = new CommandObject(INDEX_EDIT, new TaskObject(originalDate),
					editOriginal.getEditItemNumber());
		}
		list.push(newCommandObj);
	}

	private void helpFunction(TaskObject taskObj) {
		String helpSearchKey = "";
		Help help = new Help(helpSearchKey);
		setOutput(help.run());
	}

	/**
	 * Determines if the command involves editing of a task
	 * @param command Integer containing the command index of this command
	 * @return a boolean value indicating whether the command involves editing
	 */
	private boolean isListOperation(int command) {
		return command == INDEX_ADD || command == INDEX_EDIT || command == INDEX_DELETE || command == INDEX_DONE
				|| command == INDEX_OVERDUE || command == INDEX_INCOMPLETE;
	}

	private void clearRedoList() {
		while (!redoList.isEmpty()) {
			redoList.pop();
		}
	}

	private void printInvalidCommandMessage() {
		output.clear();
		output.add(MESSAGE_INVALID_COMMAND);
	}
	
	// Getters and setters
	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public Deque<CommandObject> getUndoList() {
		return undoList;
	}

	public Deque<CommandObject> getRedoList() {
		return redoList;
	}

	public ArrayList<String> getOutput() {
		return output;
	}
	
	public ArrayList<TaskObject> getLastOutputTaskList() {
		return lastOutputTaskList;
	}

	public void setTaskList(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public void setUndoList(Deque<CommandObject> undoList) {
		this.undoList = undoList;
	}

	public void setRedoList(Deque<CommandObject> redoList) {
		this.redoList = redoList;
	}

	public void setUserInput(String newUserInput) {
		this.userInput = newUserInput;
	}

	public void setOutput(ArrayList<String> newOutput) {
		this.output = newOutput;
	}

	public void setLastOutputTaskList(ArrayList<TaskObject> newLastOutputTaskList) {
		this.lastOutputTaskList = newLastOutputTaskList;
	}

	// For testing
	private void printTaskObjectFields(TaskObject taskObj) {
		System.out.println("title = " + taskObj.getTitle());
		System.out.println("start date = " + taskObj.getStartDate());
		System.out.println("end date = " + taskObj.getEndDate());
		System.out.println("start time = " + taskObj.getStartTime());
		System.out.println("end time = " + taskObj.getEndTime());
		System.out.println("category = " + taskObj.getCategory());
		System.out.println("status = " + taskObj.getStatus());
		System.out.println("task id = " + taskObj.getTaskId());
	}
	
	public void checkOverdue() {
		Overdue.markAllOverdueTasks(taskList);
	}

	/**
	 * Combines and converts date and time strings stored in TaskObjects 
	 * into LocalDateTime format for easier comparison and greater usability.
	 * @param date series of numbers representing the date in the format
	 * YYYYMMDD
	 * @param time series of numbers representing the time in the 24hr format
	 * HHMM
	 * @return LocalDateTime object containing all the above information
	 */
	public static LocalDateTime obtainDateTime(int date, int time) {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int dayOfMonth = date % 100;
		int hour = time / 100;
		int min = time % 100;
		LocalDateTime formattedTime = LocalDateTime.of(year, month, dayOfMonth, hour, min);
		return formattedTime;
	}

}
