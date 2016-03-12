package logic;
import common.*;
import logic.add.Add;
import logic.delete.Delete;
import logic.display.Display;
import logic.edit.Edit;
import logic.help.Help;
import logic.mark.Done;
import logic.mark.Incomplete;
import logic.mark.Mark;
import logic.mark.Overdue;
import logic.save.Save;
import logic.search.Search;
import logic.undo.UndoRedo;

import java.util.ArrayList;
import java.util.Deque;

public class CommandFacade {
	
	public static final int INDEX_ADD = 1;
	public static final int INDEX_SEARCH_DISPLAY = 2;
	public static final int INDEX_EDIT = 3;
	public static final int INDEX_DELETE = 4;
	public static final int INDEX_UNDO = 5;
	public static final int INDEX_REDO = 6;
	public static final int INDEX_SAVE = 7;
	public static final int INDEX_EXIT = 8;
	public static final int INDEX_HELP = 9;
	// A set of indicators for task status modifiers
	public static final int INDEX_DONE = 10;
	public static final int INDEX_OVERDUE = 11;
	public static final int INDEX_INCOMPLETE = 12;
	public static final String CATEGORY_EVENT = "event";
	public static final String CATEGORY_DEADLINE = "deadline";
	public static final String CATEGORY_FLOATING = "floating";
	
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command";	
	
	private ArrayList<TaskObject> taskList;
	private Deque<CommandObject> undoList;
	private Deque<CommandObject> redoList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output;
	
	private CommandObject commandObj;
	private int commandType;
	private TaskObject taskObj;
	private int index;

	boolean isUndoAction;
	boolean isRedoAction;
	
	/**
	 * Constructor called by Logic which passes all arguments that might be used; this class acts as a facade to process the command types
	 * @param taskList The default taskList storing all the tasks
	 * @param undoList The deque of CommandObjects which stores all undo actions
	 * @param redoList The deque of CommandObjects which stores all redo actions
	 * @param lastOutputTaskList The ArrayList which keeps track of what is currently being displayed to the user
	 * @param commandObj The CommandObject returned by the Parser class which returns the processed information
	 * @param isUndoAction Tracks if this call is an undo action
	 * @param isRedoAction Tracks if this call is an undo action
	 */
	public CommandFacade(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList,
			ArrayList<TaskObject> lastOutputTaskList, CommandObject commandObj, boolean isUndoAction, boolean isRedoAction) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.redoList = redoList;
		this.lastOutputTaskList = lastOutputTaskList;
		this.commandObj = commandObj;
		this.isUndoAction = isUndoAction;
		this.isRedoAction = isRedoAction;
		setCommandObjectValues();
	}
	
	
	/**
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
	public void run() {
			
		// FOR TESTING
		//System.out.println("isUndoAction = " + isUndoAction + ", undo size = "
		//+ undoList.size() + ", redo size = " + redoList.size());
		//System.out.println("commandObj command type = " + commandObj.getCommandType());
		//System.out.println("commandObj index = " + commandObj.getIndex());
		//if (taskObj != null) printTaskObjectFields(taskObj);
		// System.out.println();
	
		// Clears the redo stack if it is a new command
		if (!redoList.isEmpty() && isListOperation(commandType) && !isUndoAction && !isRedoAction) {
			clearRedoList();
		}
	
		switch (commandType) {
			case INDEX_ADD:
				addFunction();
				break;
			case INDEX_SEARCH_DISPLAY:
				checkDisplayOrSearch();
				break;
			case INDEX_EDIT:
				editFunction();
				break;
			case INDEX_DELETE:
				deleteFunction();
				break;
			case INDEX_UNDO:
			case INDEX_REDO:
				undoRedoFunction();
				break;
			case INDEX_SAVE:
				saveFunction();
				break;
			case INDEX_EXIT:
				exitFunction();
				break;
			case INDEX_HELP:
				helpFunction();
				break;
			case INDEX_DONE:
				doneFunction();
				break;
			case INDEX_OVERDUE:
				overdueFunction();
				break;
			case INDEX_INCOMPLETE:
				incompleteFunction();
				break;
			default:
				printInvalidCommandMessage();
				break;
			}
		}
	
	
		/* 
		 * Calls Add function, which adds the task to the task list and writes it to storage
		 * It then adds the relevant CommandObject to the undo list or the redo list
		 */
		private void addFunction() {
			Add add = new Add(taskObj, index, taskList);
			setOutput(add.run());
			setLastOutputTaskList(taskList);
			
			if (isUndoAction) {
				addToList(commandObj, redoList);
			} else {
				addToList(commandObj, undoList);
			}
		}
	
		
		/*
		 * This method checks for the presence of a search keyword in TaskObject. If
		 * there is a keyword, search function will be called. If there is no
		 * keyword, display function will be called.
		 */
		private void checkDisplayOrSearch() {
			if (taskObj.isSearchKeywordPresent())
				searchFunction();
			else
				displayFunction();
		}
		
		// Calls Search function which outputs only the tasks that match the search keyword
		private void searchFunction() {
			Search search = new Search(taskObj, taskList);
			setOutput(search.run());
			setLastOutputTaskList(search.getLastOutputTaskList());
		}
		
		// Calls Display function which outputs the entire task list
		private void displayFunction() {
			Display display = new Display(taskList);
			setOutput(display.run());
			setLastOutputTaskList(display.getLastOutputTaskList());
		}
		
		
		/*
		 * Calls Edit function which edits the task title, date, or both
		 * It then adds the relevant CommandObject to the undo list or the redo list
		 */
		private void editFunction() {
			Edit edit = new Edit(commandObj, lastOutputTaskList, taskList);
			setOutput(edit.run());
			setLastOutputTaskList(taskList);
			
			if (isUndoAction) {
				addToList(edit, redoList);
			} else {
				addToList(edit, undoList);
			}
		}
		
		
		/*
		 *  The method checks if there is a task specified in the Delete command.
		 *  If there is no task specified, quick delete is run, i.e. deletes the most recently added task.
		 *  If there is a task specified, normal delete is run to remove the specified task.
		 */
		private void deleteFunction() {
			TaskObject removedTask = new TaskObject();
			if (index == -1) {	// no task specified
				removedTask = quickDelete(); 
			} else {
				removedTask = normalDelete();
			}
			
			processUndoForDelete(removedTask);
		}
		
		private TaskObject quickDelete() {
			Delete delete = new Delete(taskList, undoList);
			setOutput(delete.run());
			setLastOutputTaskList(taskList);
			
			return delete.getRemovedTask();
		}
		
		private TaskObject normalDelete() {
			Delete delete = new Delete(commandObj, taskList, lastOutputTaskList);
			setOutput(delete.run());
			setLastOutputTaskList(taskList);
			
			return delete.getRemovedTask();
		}
		
		// Checks that removedTask is not null, then adds the corresponding CommandObject to the undo list or the redo list
		private void processUndoForDelete(TaskObject removedTask) {
			assert removedTask != null;

			CommandObject newCommandObj = new CommandObject(commandType, removedTask, index);	// MIGHT HAVE ERRORS
			if (isUndoAction) {
				addToList(newCommandObj, redoList);
			} else {
				addToList(newCommandObj, undoList);
			}
		}
		
		
		/*
		 * Calls the UndoRedo class, which is a parent of the Undo and Redo classes.
		 * The class reads in the command type and then calls the relevant child class.
		 */
		private void undoRedoFunction() {
			UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
			setOutput(undoRedo.run(commandType));
		
			// Update the lists
			setTaskList(undoRedo.getTaskList());
			setUndoList(undoRedo.getUndoList());
			setRedoList(undoRedo.getRedoList());
			setLastOutputTaskList(taskList);
		}
		
		
		// Calls the Save function, which saves the task list to an appropriate storage place.
		private void saveFunction() {
			Save save = new Save(taskObj, taskList);
			setOutput(save.run());
		}
		
		
		// Calls the Exit function, which exits the program
		private void exitFunction() {
			Exit exit = new Exit();
			exit.run();
		}

		
		// Calls the Help function, which displays the user guide depending on the search keyword entered (if any)
		private void helpFunction() {
			String helpSearchKey = taskObj.getTitle();
			Help help = new Help(helpSearchKey);
			setOutput(help.run());
		}

		
		// Calls the Done function, which marks a specified task as done
		private void doneFunction() {
			Done done = new Done(commandObj, taskList, lastOutputTaskList);
			setOutput(done.run());
			setLastOutputTaskList(taskList);
			
			if (done.getTaskIdToMark() != -1) { // If successfully marked as done
				if (isUndoAction) {
					addToList(done, redoList);
				} else {
					addToList(done, undoList);
				}
			}
		}
		
		// Calls the Overdue function, which marks a specified task as overdue
		private void overdueFunction() {
			Overdue overdue = new Overdue(commandObj, taskList, lastOutputTaskList);
			setOutput(overdue.run());
			setLastOutputTaskList(taskList);
			
			if (overdue.getTaskIdToMark() != -1) {
				if (isUndoAction) {
					addToList(overdue, redoList);
				} else {
					addToList(overdue, undoList);
				}
			}
		}
		
		// Calls the Incomplete function, which marks a specified task as overdue
		private void incompleteFunction() {
			Incomplete incomplete = new Incomplete(commandObj, taskList, lastOutputTaskList);
			setOutput(incomplete.run());
			setLastOutputTaskList(taskList);
			
			if (incomplete.getTaskIdToMark() != -1) {
				if (isUndoAction) {
					addToList(incomplete, redoList);
				} else {
					addToList(incomplete, undoList);
				}
			}
		}
		
		
		// ------------------------- METHODS TO POPULATE UNDO/REDO LIST -------------------------
		 	
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
			CommandObject newCommandObj = new CommandObject();
			
			if (commandType == INDEX_ADD) {
				if (index == -1) {
					// if task was previously added to the end of the list
					newCommandObj = new CommandObject(INDEX_DELETE, new TaskObject(), taskList.size());
				} else {
					// if task was previously added to a pre-determined location in the list
					newCommandObj = new CommandObject(INDEX_DELETE, new TaskObject(), index);
				}
			} else if (commandType == INDEX_DELETE) {
				newCommandObj = new CommandObject(INDEX_ADD, commandObj.getTaskObject(), index);
			} 
			
			list.push(newCommandObj);
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
		
		/**
		 * Constructs a CommandObject for either "done", "incomplete" or "overdue" 
		 * for the purpose of pushing it into the undoList
		 * @param mark Mark object which performed the modification to the task list
		 * @param list Either an undoList or redoList
		 */
		private void addToList(Mark mark, Deque<CommandObject> list) {
			CommandObject newCommandObj = new CommandObject();
			
			String pastStatus = mark.getStatusToChange();
			int commandIndex = getCommandIndex(pastStatus);
			if (commandIndex != 0) {
				newCommandObj.setCommandType(commandIndex);
				newCommandObj.setTaskObject(mark.getMarkedTask());
			}
			
			list.push(newCommandObj);
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
	
	public ArrayList<TaskObject> getLastOutputTaskList() {
		return lastOutputTaskList;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public CommandObject getCommandObject() {
		return commandObj;
	}
	
	public void setTaskList(ArrayList<TaskObject> newTaskList) {
		this.taskList = newTaskList;
	}
	
	public void setUndoList(Deque<CommandObject> newUndoList) {
		this.undoList = newUndoList;
	}
	
	public void setRedoList(Deque<CommandObject> newRedoList) {
		this.redoList = newRedoList;
	}
	
	public void setLastOutputTaskList(ArrayList<TaskObject> newLastOutputTaskList) {
		this.lastOutputTaskList = newLastOutputTaskList;
	}
	
	public void setOutput(ArrayList<String> newOutput) {
		this.output = newOutput;
	}
	
	public void setCommandObject(CommandObject newCommandObj) {
		this.commandObj = newCommandObj;
	}
	
	public void setCommandObjectValues() {
		setCommandType();
		setTaskObject();
		setIndex();
	}
	
	private void setCommandType() {
		this.commandType = commandObj.getCommandType();
	}
	
	private void setTaskObject() {
		this.taskObj = commandObj.getTaskObject();
	}
	
	private void setIndex() {
		this.index = commandObj.getIndex();
	}


}
