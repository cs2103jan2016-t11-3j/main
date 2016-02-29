package logic;

import parser.*;
import logic.add.*;
import logic.delete.*;
import logic.display.*;
import logic.edit.*;
import logic.search.*;
import logic.undo.*;
import logic.save.*;

import java.util.ArrayList;
import java.util.Stack;

// Parent class for Undo

public class Logic {

	public static final int INDEX_ADD = 1;
	public static final int INDEX_SEARCH_DISPLAY = 2;
	public static final int INDEX_EDIT = 3;
	public static final int INDEX_DELETE = 4;
	public static final int INDEX_UNDO = 5;
	public static final int INDEX_SAVE = 6;
	public static final int INDEX_EXIT = 7;
	public static final int INDEX_HELP = 8;
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command";

	private ArrayList<TaskObject> taskList;
	private Stack<CommandObject> undoList;

	// This will get repeatedly updated by UI for each input
	private String userInput;
	// Output is to be returned to UI after each command
	private ArrayList<String> output;
	// Keeps track of the last output task list returned from display/search;
	// for editing purposes
	private ArrayList<TaskObject> lastOutputTaskList;

	public Logic() {

	}

	public Logic(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
	}

	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public Stack<CommandObject> getUndoList() {
		return undoList;
	}

	public ArrayList<String> getOutput() {
		return output;
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
	void run(String userInput) {
		setUserInput(userInput);
		CommandObject commandObj = callParser();	
		parseCommandObject(commandObj, false);
	}

	// Calling Parser to parse the user input
	protected CommandObject callParser() {
		Parser parser = new Parser(userInput);
		return parser.run();
	}

	public void parseCommandObject(CommandObject commandObj, boolean isUndoAction) {
		int command = commandObj.getCommandType();
		TaskObject taskObj = commandObj.getTaskObject();	

		switch (command) {
		case INDEX_ADD:
			addFunction(command, taskObj);
			if (!isUndoAction) {
				addToUndoList(command, taskObj);
			}
			break;
		case INDEX_SEARCH_DISPLAY:
			checkDisplayOrSearch(taskObj);
			break;
		case INDEX_EDIT:
			Edit editOriginal = editFunction(taskObj);
			if (!isUndoAction) {
				addToUndoList(editOriginal);
			}
			break;
		case INDEX_DELETE:
			TaskObject removedTask = new TaskObject();
			if (taskObj.getTitle().equals("")) {
				// Quick-delete function for item recently added in the last
				// command
				if (undoList.peek().getCommandType() == INDEX_DELETE) {
					removedTask = undoList.peek().getTaskObject();
				}
				deleteFunction(); // overloaded function
			} else {
				removedTask = deleteFunction(taskObj);
				// Needs editing as the TaskObject added to undoList is the
				// wrong TaskObject
			}
			if (!isUndoAction) {
				if (removedTask.getTitle().equals("")) {
					addToUndoList(command, removedTask);
				}
			}
			break;
		case INDEX_UNDO:
			undoFunction();
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
		default:
			printInvalidCommandMessage();
			break;
		}
	}

	private void addFunction(int command, TaskObject taskObj) {
		Add add = new Add(taskObj, taskList);
		setOutput(add.run());
		addToUndoList(command, taskObj);
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

	private Edit editFunction(TaskObject taskObj) {
		Edit edit = new Edit(taskObj, lastOutputTaskList, taskList);
		setOutput(edit.run());
		return edit;
	}

	private TaskObject deleteFunction(TaskObject taskObj) {
		TaskObject removedTask = new TaskObject();
		Delete delete = new Delete(taskObj, taskList, lastOutputTaskList);
		setOutput(delete.run());
		removedTask = delete.getRemovedTask();
		return removedTask;
	}

	private void deleteFunction() {
		Delete delete = new Delete(taskList, undoList);
		setOutput(delete.run());
	}

	private void undoFunction() {
		Undo undo = new Undo();
		undo.run();
	}

	private void saveFunction(TaskObject taskObj) {
		Save save = new Save(taskObj, taskList);
		setOutput(save.run());
	}

	private void exitFunction() {
		Exit exit = new Exit();
		exit.run();
	}

	// The following 2 methods stores the reverse of the user input in the
	// stack.
	// Add <-> delete
	private void addToUndoList(int command, TaskObject taskObj) {
		if (command == INDEX_ADD) {
			undoList.push(new CommandObject(INDEX_DELETE, taskObj));
		} else if (command == INDEX_DELETE) {
			undoList.push(new CommandObject(INDEX_ADD, taskObj));
		}
	}

	// Edit <-> edit
	// Saves the item number to be edited and the original title
	private void addToUndoList(Edit editOriginal) {
		String originalTitle = "" + editOriginal.getEditItemNumber() + " " + editOriginal.getOriginalTitle();
		CommandObject newCommandObj = new CommandObject(INDEX_EDIT, new TaskObject(originalTitle));
		undoList.push(newCommandObj);
	}

	private void helpFunction() {

	}

	private void printInvalidCommandMessage() {
		output.add(MESSAGE_INVALID_COMMAND);
	}

}
