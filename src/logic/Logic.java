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
import logic.timeOutput.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
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
		TimeOutput.setTimeOutputForGui(taskList);
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
		CommandFacade commandFacade = new CommandFacade(taskList, undoList, redoList, lastOutputTaskList, commandObj, isUndoAction, isRedoAction);
		commandFacade.process();
		updateLists(commandFacade);
	}
	
	// Retrieves the updated lists from the CommandFacade class and updates the corresponding lists in Logic
	private void updateLists(CommandFacade commandFacade) {
		setTaskList(commandFacade.getTaskList());
		setUndoList(commandFacade.getUndoList());
		setRedoList(commandFacade.getRedoList());
		setLastOutputTaskList(commandFacade.getLastOutputTaskList());
		setOutput(commandFacade.getOutput());
	}
		
	// ------------------------- GETTERS AND SETTERS -------------------------
	
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

	public void setLastOutputTaskList(ArrayList<TaskObject> newLastOutputTaskList) {
		this.lastOutputTaskList = newLastOutputTaskList;
	}
	
	public void setOutput(ArrayList<String> newOutput) {
		this.output = newOutput;
	}

	public void setUserInput(String newUserInput) {
		this.userInput = newUserInput;
	}
		
	// Checks for overdue tasks at the start when the program is first run
	public void checkOverdue() {
		Overdue.markAllOverdueTasks(taskList);
	}

}
