package logic;

import parser.*;
import storage.FileStorage;
import logic.mark.*;
import logic.timeOutput.*;
import logic.exceptions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonSyntaxException;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

import common.AtfLogger;
import common.CommandObject;
import common.TaskObject;

/**
 * Main driver for Adult TaskFinder. Upon initialisation of the object, retrieves all existing tasks from an
 * external file source and places them into an ArrayList of TaskObjects. The main Logic object initialised in
 * the GUI will exist until exit command is inputted. <br>
 * Alternatively, secondary Logic objects may be initialised when Undo or Redo commands are given. In this
 * case, secondary Logic objects will only carry out the specific task required before it "dies".
 * 
 * @param taskList
 *            - Initialised as an empty list of TaskObjects, will maintain all TaskObjects existing in Adult
 *            TaskFinder internally throughout the runtime of the program.
 * @param undoList
 *            - Stack of CommandObjects stored for undoing. Every time a command is executed, the reverse of
 *            that command will be pushed into undoList in the form of a CommandObject.
 * @param redoList
 *            - Stack of CommandObjects stored for redoing. Every time a command is popped from the undoList
 *            for undoing, the reverse of that command will be pushed into the redoList as an CommandObject.
 *            Clears itself whenever the user inputs a command which is not "undo".
 * @author ChongYan, RuiBin
 *
 */
public class Logic {

	static Logger logger = AtfLogger.getLogger();
	// Maintained throughout the entire running operation of the program
	protected ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	private Deque<CommandObject> undoList = new ArrayDeque<CommandObject>();
	private Deque<CommandObject> redoList = new ArrayDeque<CommandObject>();
	private int taskId; // For generation of a unique task ID for each task

	// This variable will get repeatedly updated by UI for each input
	private String userInput;
	// Output is to be returned to UI after each command
	private ArrayList<String> output = new ArrayList<String>();
	// Output containing the list of timings pertaining to a single task
	private ArrayList<String> taskDateTimeOutput = new ArrayList<String>();
	// Keeps track of the list that is constantly displayed in UI
	private ArrayList<TaskObject> lastOutputTaskList = new ArrayList<TaskObject>();
	// Stores the index of the last task searched
	private int lastSearchedIndex = -1;

	/**
	 * Constructor called by UI only upon starting up. Loads all existing tasks and checks each task to see
	 * whether any of them are overdue, and updates their corresponding statuses. Sets the next Task ID of a
	 * task which will be added to the list. Also calls method to set up the list of tasks which will be shown
	 * to users, as well as the relevant welcome message.
	 */
	public Logic() {
		taskList = new ArrayList<TaskObject>();
		undoList = new ArrayDeque<CommandObject>();
		redoList = new ArrayDeque<CommandObject>();
		try {
			loadTaskList();
			TimeOutput.setTimeOutputForGui(taskList);
			setStartingTaskId();
			checkOverdue();
			Recurring.updateRecurringEvents(taskList);
			Recurring.updateRecurringDeadlines(taskList);
			createFirstOutputTaskList();
		} catch (RecurrenceException e) {
			String exceptionMessage;
			if (e.getTaskId() != -1) {
				removeFromTaskList(e.getTaskId());
				exceptionMessage = String.format(MESSAGE_RECURRENCE_EXCEPTION_CORRUPTED, e.getTaskName());
			} else {
				exceptionMessage = e.getRecurrenceExceptionMessage();
			}
			output.add(exceptionMessage);
			logger.log(Level.WARNING, "unable to update recurrences");
		} catch (FileNotFoundException e) {
			output.add(MESSAGE_LOAD_EXCEPTION_FNF);
			logger.log(Level.WARNING,
					"unable to read information from external file storage, file not found");
		} catch (IOException e) {
			output.add(MESSAGE_LOAD_EXCEPTION_IO);
			logger.log(Level.WARNING,
					"unable to read information from external file storage, general IO exception");
		} catch (JsonSyntaxException e) {
			output.add(MESSAGE_LOAD_EXCEPTION_JSON);
			logger.log(Level.WARNING,
					"unable to read information from external file storage, Json syntax error");
		}
		logger.info("Start logic");
	}

	private void removeFromTaskList(int taskId) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskId() == taskId) {
				taskList.remove(i);
			}
		}
	}

	/**
	 * Method which is only called during startup of AdultTaskFinder. Tasks are added to a "first output" task
	 * list in the following order: <br>
	 * 1. Tasks overdue <br>
	 * 2. Tasks due today <br>
	 * If there are no tasks in the task list at this point, method will proceed to look for tasks which are
	 * incomplete and add them to the "first output" task list, regardless of whether it is due today, even if
	 * it is a floating task. <br>
	 * However, if there are no tasks in the list even at this point, then no tasks will be showed on startup.
	 * The message shown to the user varies based on the tasks which were added to the "first output" task
	 * list.
	 */
	private void createFirstOutputTaskList() {
		ArrayList<TaskObject> firstOutputTaskList = new ArrayList<TaskObject>();
		ArrayList<String> firstOutput = new ArrayList<String>();

		addOverdueTasksToFirstOutputTaskList(firstOutputTaskList);
		addTasksDueTodayToFirstOutputTaskList(firstOutputTaskList);
		if (firstOutputTaskList.isEmpty()) {
			addIncompleteTasksToFirstOutputTaskList(firstOutputTaskList);
		} else {
			firstOutput.add(MESSAGE_WELCOME_TASKS_OVERDUE_TODAY);
			logger.log(Level.INFO, "added overdue and tasks due today to show on startup");
		}

		if (firstOutputTaskList.isEmpty()) {
			firstOutput.add(MESSAGE_WELCOME_EMPTY);
			logger.log(Level.INFO, "no incomplete tasks to show on startup");
		} else {
			if (firstOutput.isEmpty()) {
				firstOutput.add(MESSAGE_WELCOME_TASKS_INCOMPLETE);
				logger.log(Level.INFO, "added incomplete tasks to show on startup");
			}
		}

		setLastOutputTaskList(firstOutputTaskList);
		setOutput(firstOutput);
	}

	private void addOverdueTasksToFirstOutputTaskList(ArrayList<TaskObject> firstOutputTaskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getStatus().equals(STATUS_OVERDUE)) {
				firstOutputTaskList.add(taskList.get(i));
			}
		}
	}

	private void addTasksDueTodayToFirstOutputTaskList(ArrayList<TaskObject> firstOutputTaskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (!taskList.get(i).getStatus().equals(STATUS_COMPLETED)) {
				if (taskList.get(i).getStartDateTime().toLocalDate().equals(LocalDate.now())) {
					if (checkNotDuplicate(taskList.get(i), firstOutputTaskList)) {
						firstOutputTaskList.add(taskList.get(i));
					}
				}
			}
		}
	}

	private void addIncompleteTasksToFirstOutputTaskList(ArrayList<TaskObject> firstOutputTaskList) {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getStatus().equals(STATUS_INCOMPLETE)) {
				firstOutputTaskList.add(taskList.get(i));
			}
		}
	}

	private boolean checkNotDuplicate(TaskObject task, ArrayList<TaskObject> firstOutputTaskList) {
		for (int i = 0; i < firstOutputTaskList.size(); i++) {
			if (task.getTaskId() == firstOutputTaskList.get(i).getTaskId()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Constructor called by Undo/Redo. This is a secondary logic class which only performs one operation
	 * before being deactivated.
	 * 
	 * @param taskList
	 *            The default taskList storing all the tasks
	 * @param undoList
	 *            The deque of CommandObjects which stores all undo actions
	 * @param redoList
	 *            The deque of CommandObjects which stores all redo actions
	 */
	public Logic(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList,
			Deque<CommandObject> redoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.redoList = redoList;
		this.lastOutputTaskList = taskList;
	}

	// sorts lastOutputTaskList by Date
	public void sortOutputByDate() {
		Comparator<TaskObject> dateComparator = new Comparator<TaskObject>() {
			@Override
			public int compare(final TaskObject o1, final TaskObject o2) {
				if (o1.getStatus() == o2.getStatus()) {
					if (o1.getStartDateTime() == o2.getStartDateTime()) {
						if (o1.getEndDateTime() == o2.getEndDateTime()) {
							return o1.getTitle().compareTo(o2.getTitle());
						}
						return o1.getEndDateTime().compareTo(o2.getEndDateTime());
					}
					return o1.getStartDateTime().compareTo(o2.getStartDateTime());
				}
				return o2.getStatus().compareTo(o1.getStatus());
			}
		};
		Collections.sort(lastOutputTaskList, dateComparator);
	}

	/**
	 * Main processing component of AdultTaskFinder. All user inputs will be passed through this command,
	 * where the internal logic of the software will process the command and react accordingly.
	 * 
	 * @param userInput
	 *            String input that is obtained from UI component. Cannot be empty.
	 */
	public void run(String userInput) {
		try {
			setUserInput(userInput);
			CommandObject commandObj = callParser();
			parseCommandObject(commandObj, false, false);
			TimeOutput.setTimeOutputForGui(taskList);
		} catch (Exception e) {
			output.clear();
			output.add(MESSAGE_FAILED_PROCESSING);
		}
	}

	/**
	 * Internal method which is called during the initialisation of Logic object. Purpose of this method is to
	 * call storage and retrieve all existing task information from the external file source, if available
	 * 
	 * @throws FileNotFoundException
	 *             Specific exception where file does not exist, will be caught and processed by Logic
	 *             constructor
	 * @throws JsonSyntaxException
	 *             Specific exception where the Json Library is unable to read the external file, will be
	 *             caught and processed by Logic constructor
	 * @throws IOException
	 *             General exception for failing to read a file, will be caught and processed by Logic
	 *             constructor
	 */
	private void loadTaskList() throws FileNotFoundException, JsonSyntaxException, IOException {
		FileStorage storage = FileStorage.getInstance();
		taskList = storage.load();
		setLastOutputTaskList(taskList);
	}

	// Sets the starting task ID value. This value should be larger than the
	// current largest task ID value in the task list so as to avoid overlap.
	private void setStartingTaskId() {
		int largestTaskId = 1;
		for (int i = 0; i < taskList.size(); i++) {
			int id = taskList.get(i).getTaskId();
			if (id > largestTaskId) {
				largestTaskId = id;
			}
		}

		this.taskId = largestTaskId + 1;
	}

	// Checks for overdue tasks at the start when the program is first run
	private void checkOverdue() {
		Overdue.markAllOverdueTasks(taskList);
	}

	/**
	 * Calls Parser to parse the user input.
	 * 
	 * @return CommandObject containing information on the task to be manipulated, as well as the command to
	 *         execute
	 */
	private CommandObject callParser() throws Exception {
		Parser parser = new Parser(userInput, taskId);
		taskId++;
		return parser.run();
	}

	/**
	 * Calls the CommandFacade class and passes all relevant arguments. CommandFacade class will be
	 * responsible for parsing the CommandObject and calling the appropriate function. All the lists (task
	 * list, undo list, redo list, last output task list, output) in Logic are subsequently updated with the
	 * values from the CommandFacade class.
	 * 
	 * @param commandObj
	 *            CommandObject obtained from parsing the user input, will be used in the CommandFacade object
	 *            to process changes to AdultTaskFinder
	 * @param isUndoAction
	 *            Boolean variable denoting if the method is called as a result of an undo command
	 * @param isRedoAction
	 *            Boolean variable denoting if the method is called as a result of a redo command
	 */
	public void parseCommandObject(CommandObject commandObj, boolean isUndoAction, boolean isRedoAction) {
		if (!(isUndoAction || isRedoAction)) {
			commandObj.setLastSearchedIndex(lastSearchedIndex);
		}
		CommandFacade commandFacade = new CommandFacade(taskList, undoList, redoList, lastOutputTaskList,
				commandObj, isUndoAction, isRedoAction);
		commandFacade.run();
		updateLists(commandFacade);

		System.out.println("Last searched index = " + lastSearchedIndex);
	}

	// Retrieves the updated lists from the CommandFacade class and updates the
	// corresponding lists in Logic
	private void updateLists(CommandFacade commandFacade) {
		setTaskList(commandFacade.getTaskList());
		setUndoList(commandFacade.getUndoList());
		setRedoList(commandFacade.getRedoList());
		setLastOutputTaskList(commandFacade.getLastOutputTaskList());
		setOutput(commandFacade.getOutput());
		setTaskDateTimeOutput(commandFacade.getTaskDateTimeOutput());
		setLastSearchedIndex(commandFacade.getLastSearchedIndex());
		/*
		 * if (commandFacade.getCommandType() == INDEX_SEARCH_DISPLAY) {
		 * setLastSearchedIndex(commandFacade.getLastSearchedIndex()); } else { setLastSearchedIndex(-1); }
		 */
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

	public ArrayList<String> getTaskDateTimeOutput() {
		return taskDateTimeOutput;
	}

	public int getLastSearchedIndex() {
		return lastSearchedIndex;
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

	public void setLastSearchedIndex(int lastSearchedIndex) {
		this.lastSearchedIndex = lastSearchedIndex;
	}

	public void setUserInput(String newUserInput) {
		this.userInput = newUserInput;
	}

	public void setTaskDateTimeOutput(ArrayList<String> taskDateTimeOutput) {
		this.taskDateTimeOutput = taskDateTimeOutput;
	}

}
