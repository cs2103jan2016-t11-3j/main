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

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Stack;

// Parent class for Undo

/* On startup, should load taskList automatically from storage, and mark all events which are
 * overdue. Consider displaying all overdue tasks or alert
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
	public static final int INDEX_UNDONE = 12;

	private static final String MESSAGE_INVALID_COMMAND = "Invalid command";

	// Maintained throughout the entire running operation of the program
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	private Stack<CommandObject> undoList = new Stack<CommandObject>();
	private Stack<CommandObject> redoList = new Stack<CommandObject>();
	private int taskId = 1;

	// This variable will get repeatedly updated by UI for each input
	private String userInput;
	// Output is to be returned to UI after each command
	private ArrayList<String> output;
	// Keeps track of the last output task list returned from display/search;
	// for editing purposes
	private ArrayList<TaskObject> lastOutputTaskList;

	public Logic() {
		taskList = new ArrayList<TaskObject>();
		undoList = new Stack<CommandObject>();
		redoList = new Stack<CommandObject>();
	}

	public Logic(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList, Stack<CommandObject> redoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.redoList = redoList;
	}

	// Getters and setters
	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}
	
	public Stack<CommandObject> getUndoList() {
		return undoList;
	}
	
	public Stack<CommandObject> getRedoList() {
		return redoList;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public void setTaskList(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	public void setUndoList(Stack<CommandObject> undoList) {
		this.undoList = undoList;
	}
	
	public void setRedoList(Stack<CommandObject> redoList) {
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

	// Takes in a String argument from UI component
	public void run(String userInput) {
		setUserInput(userInput);
		CommandObject commandObj = callParser();
		parseCommandObject(commandObj, false);
	}

	// Calling Parser to parse the user input
	private CommandObject callParser() {
		Parser parser = new Parser(userInput, taskId);
		taskId++;
		return parser.run();
	}

	public void parseCommandObject(CommandObject commandObj, boolean isUndoAction) {
		int command = commandObj.getCommandType();
		TaskObject taskObj = commandObj.getTaskObject();
		
		// Clears the redo stack if it is a new command
		if (!redoList.empty() && isListOperation(command) && !isUndoAction) {
			clearRedoList();
		}

		// FOR TESTING
		// printTaskObjectFields(taskObj);
		// System.out.println();

		switch (command) {
			case INDEX_ADD:
				addFunction(command, taskObj);
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
					// Quick-delete function for item recently added in the last
					// command
					if (undoList.peek().getCommandType() == INDEX_DELETE) {
						removedTask = undoList.peek().getTaskObject(); 	// NEED TO POP SOMEWHERE
					}
					deleteFunction(); // overloaded function
				} else {
					removedTask = deleteFunction(commandObj);
					// Needs editing as the TaskObject added to undoList is the
					// wrong TaskObject
				}
				
				CommandObject newCommandObj = new CommandObject(command, removedTask, -1);
				if (isUndoAction) {
					addToList(newCommandObj, redoList);
				} else {
					addToList(newCommandObj, undoList);
				}
				break;
			case INDEX_UNDO: case INDEX_REDO:
				undoRedoFunction(command);
				break;
			case INDEX_SAVE:
				saveFunction(taskObj);
				break;
			case INDEX_EXIT:
				exitFunction();
				break;
			case INDEX_HELP:
				helpFunction();
				break;
			case INDEX_DONE:
				doneFunction(taskObj);
				break;
			case INDEX_OVERDUE:
				overdueFunction(taskObj);
				break;
			case INDEX_UNDONE:
				undoneFunction(taskObj);
				break;
			default:
				printInvalidCommandMessage();
				break;
		}
	}

	private void addFunction(int command, TaskObject taskObj) {
		Add add = new Add(taskObj, taskList);
		setOutput(add.run());
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
		return edit;
	}

	private TaskObject deleteFunction(CommandObject commandObj) {
		TaskObject removedTask = new TaskObject();
		Delete delete = new Delete(commandObj, taskList, lastOutputTaskList);
		setOutput(delete.run());
		removedTask = delete.getRemovedTask();
		return removedTask;
	}

	private void deleteFunction() {
		Delete delete = new Delete(taskList, undoList);
		setOutput(delete.run());
	}

	private void undoRedoFunction(int command) {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		setOutput(undoRedo.run(command));
		
		// Update the task and undo lists
		setTaskList(undoRedo.getTaskList());
		setUndoList(undoRedo.getUndoList());
		setRedoList(undoRedo.getRedoList());
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
		if (done.getTaskIdToMark() != -1) { // If successfully marked as done
			String pastStatus = done.getStatusToChange();
			int commandIndex = getCommandIndex(pastStatus);
			if (commandIndex != 0) {
				//addToUndoList(commandIndex, new TaskObject(pastStatus, done.getTaskIdToMark()));
			}
		}
	}

	private void overdueFunction(TaskObject taskObj) {
		Overdue overdue = new Overdue(taskObj, taskList, lastOutputTaskList);
		setOutput(overdue.run());
		if (overdue.getTaskIdToMark() != -1) {
			String pastStatus = overdue.getStatusToChange();
			int commandIndex = getCommandIndex(pastStatus);
			if(commandIndex != 0) {
				//addToUndoList(commandIndex, new TaskObject(pastStatus, overdue.getTaskIdToMark()));
			}
		}
	}
	
	private void undoneFunction(TaskObject taskObj) {
		Undone undone = new Undone(taskObj, taskList, lastOutputTaskList);
		setOutput(undone.run());
		if (undone.getTaskIdToMark() != -1) {
			String pastStatus = undone.getStatusToChange();
			int commandIndex = getCommandIndex(pastStatus);
			if(commandIndex != 0) {
				//addToUndoList(commandIndex, new TaskObject(pastStatus, undone.getTaskIdToMark()));
			}
		}
	}

	private int getCommandIndex(String pastStatus) {
		if (pastStatus.equals("overdue")) {
			return INDEX_OVERDUE;
		} else {
			if (pastStatus.equals("completed")) {
				return INDEX_DONE;
			} else {
				if (pastStatus.equals("undone")) {
					return INDEX_UNDONE;
				}
			}
		}
		return 0;
	}

	/*
	 * The following methods stores the reverse of the user input in the stack.
	 */

	// Add <-> delete
	private void addToList(CommandObject commandObj, Stack<CommandObject> list) {
		if (commandObj.getCommandType() == INDEX_ADD) {
			// For the corresponding delete object, the TaskObject is null
			// the index number of the CommandObject is the index of the item that was just added
			list.push(new CommandObject(INDEX_DELETE, new TaskObject(), taskList.size()));
		} else if (commandObj.getCommandType() == INDEX_DELETE) {
			// For the corresponding add object, the title of the TaskObject
			// should be the name of the task that is just deleted
			list.push(new CommandObject(INDEX_ADD, commandObj.getTaskObject()));
		} else if (commandObj.getCommandType() == INDEX_DONE) {
			list.push(new CommandObject(INDEX_UNDO, commandObj.getTaskObject()));
		}
	}

	// Edit <-> edit
	// Saves the item number to be edited and the original title
	private void addToList(Edit editOriginal, Stack<CommandObject> list) {
		String originalTitle = editOriginal.getOriginalTitle();
		CommandObject newCommandObj = new CommandObject(INDEX_EDIT, new TaskObject(originalTitle), editOriginal.getEditItemNumber());
		list.push(newCommandObj);
	}

	private void helpFunction() {

	}
	
	// Returns true if the command is one which involves editing of the task lists
	private boolean isListOperation(int command) {
		return command == INDEX_ADD || command == INDEX_EDIT || command == INDEX_DELETE ||
				command == INDEX_DONE || command == INDEX_OVERDUE || command == INDEX_UNDONE;
	}
	
	private void clearRedoList() {
		while (!redoList.empty()) {
			redoList.pop();
		}
	}

	private void printInvalidCommandMessage() {
		output.add(MESSAGE_INVALID_COMMAND);
	}

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

    public void load() throws NoSuchFileException, IOException {
        FileStorage storage = FileStorage.getInstance();
        taskList = storage.load();
        // TODO Auto-generated method stub
        
    }

}
