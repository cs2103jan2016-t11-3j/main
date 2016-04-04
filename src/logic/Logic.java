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
import java.util.logging.Logger;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

import common.AtfLogger;
import common.CommandObject;
import common.TaskObject;

/**
 * Main driver for Adult TaskFinder. Upon initialisation of the object,
 * retrieves all existing tasks from an external file source and places them
 * into an ArrayList of TaskObjects. The main Logic object initialised in the
 * GUI will exist until exit command is inputted. <br>
 * Alternatively, secondary Logic objects may be initialised when Undo or Redo
 * commands are given. In this case, secondary Logic objects will only carry out
 * the specific task required before it "dies".
 * 
 * @param taskList
 *            - Initialised as an empty list of TaskObjects, will maintain all
 *            TaskObjects existing in Adult TaskFinder internally throughout the
 *            runtime of the program.
 * @param undoList
 *            - Stack of CommandObjects stored for undoing. Every time a command
 *            is executed, the reverse of that command will be pushed into
 *            undoList in the form of a CommandObject.
 * @param redoList
 *            - Stack of CommandObjects stored for redoing. Every time a command
 *            is popped from the undoList for undoing, the reverse of that
 *            command will be pushed into the redoList as an CommandObject.
 *            Clears itself whenever the user inputs a command which is not
 *            "undo".
 * @author ChongYan, RuiBin
 *
 */
public class Logic {

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
	 * Constructor called by UI. Loads all existing tasks and checks each task
	 * to see whether any of them are overdue, and updates their corresponding
	 * statuses.
	 */
	public Logic() {
		Logger logger = AtfLogger.getLogger();
		taskList = new ArrayList<TaskObject>();
		undoList = new ArrayDeque<CommandObject>();
		redoList = new ArrayDeque<CommandObject>();
		loadTaskList();
		TimeOutput.setTimeOutputForGui(taskList);
		setStartingTaskId();
		checkOverdue();
		try {
			Recurring.updateRecurringEvents(taskList);
			Recurring.updateRecurringDeadlines(taskList);
		} catch (RecurrenceException e) {
			String exceptionMessage = e.getRecurrenceExceptionMessage();
			output.add(exceptionMessage);
		}
		createFirstOutputTaskList();
		logger.info("Start logic");
	}

	// Creates the first task list containing overdue and due today
	public void createFirstOutputTaskList() {
		ArrayList<TaskObject> firstOutputTaskList = new ArrayList<TaskObject>();
		ArrayList<String> firstOutput = new ArrayList<String>();

		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getStatus().equals("overdue")) {
				firstOutputTaskList.add(taskList.get(i));
			}
		}

		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getStartDateTime().toLocalDate().equals(LocalDate.now())) {
				if (checkNotDuplicate(taskList.get(i), firstOutputTaskList)) {
					firstOutputTaskList.add(taskList.get(i));
				}
			}
		}

		if (firstOutputTaskList.isEmpty()) {
			firstOutput.add(MESSAGE_WELCOME_EMPTY);
		} else {
			firstOutput.add(MESSAGE_WELCOME_TASKS);
		}

		setLastOutputTaskList(firstOutputTaskList);
		setOutput(firstOutput);
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
	 * Constructor called by Undo/Redo. This is a secondary logic class which
	 * only performs one operation before being deactivated.
	 * 
	 * @param taskList
	 *            The default taskList storing all the tasks
	 * @param undoList
	 *            The deque of CommandObjects which stores all undo actions
	 * @param redoList
	 *            The deque of CommandObjects which stores all redo actions
	 */
	public Logic(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
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

	public void sortOutputByType() {
		Comparator<TaskObject> typeComparator = new Comparator<TaskObject>() {
			@Override
			public int compare(final TaskObject o1, final TaskObject o2) {
				return o1.getCategory().compareTo(o2.getCategory());
			}
		};
		Collections.sort(lastOutputTaskList, typeComparator);
	}

	// Takes in a String argument from UI component
	public void run(String userInput) {
		try {
			setUserInput(userInput);
			CommandObject commandObj = callParser();
			parseCommandObject(commandObj, false, false);
			TimeOutput.setTimeOutputForGui(taskList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Loads all existing tasks into the program from Storage
	private void loadTaskList() {
		try {
			FileStorage storage = FileStorage.getInstance();
			taskList = storage.load();
			// convertDateTime(taskList);
			setLastOutputTaskList(taskList);
		} catch (FileNotFoundException e) {
			// No file found in specified save location
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (DateTimeException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Retrieves all string/integer date time and sends it for conversion into
	 * LocalDateTime private void convertDateTime(ArrayList<TaskObject>
	 * taskList) throws DateTimeException { for(int i = 0; i < taskList.size();
	 * i++) { if (taskList.get(i).getCategory().equals(CATEGORY_EVENT)) {
	 * LocalDateTime startDateTime =
	 * obtainLocalDateTime(taskList.get(i).getStartDate(),
	 * taskList.get(i).getStartTime()); LocalDateTime endDateTime =
	 * obtainLocalDateTime(taskList.get(i).getEndDate(),
	 * taskList.get(i).getEndTime());
	 * taskList.get(i).setStartDateTime(startDateTime);
	 * taskList.get(i).setEndDateTime(endDateTime); } else { if
	 * (taskList.get(i).getCategory().equals(CATEGORY_DEADLINE)) { LocalDateTime
	 * deadlineTime = obtainLocalDateTime(taskList.get(i).getStartDate(),
	 * taskList.get(i).getStartTime());
	 * taskList.get(i).setStartDateTime(deadlineTime); } } } }
	 */

	// Converts into LocalDateTime
	public LocalDateTime obtainLocalDateTime(int date, int time) throws DateTimeException {
		int year = date / 10000;
		int month = (date % 10000) / 100;
		int day = date % 100;
		int hour = time / 100;
		int min = time % 100;
		return LocalDateTime.of(year, month, day, hour, min);
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
	public void checkOverdue() {
		Overdue.markAllOverdueTasks(taskList);
	}

	/**
	 * Calls Parser to parse the user input
	 * 
	 * @return CommandObject containing information on the task to be
	 *         manipulated, as well as the command to execute
	 */
	private CommandObject callParser() throws Exception {
		Parser parser = new Parser(userInput, taskId);
		taskId++;
		return parser.run();
	}

	/**
	 * Calls the CommandFacade class and passes all relevant arguments.
	 * CommandFacade class will be responsible for parsing the CommandObject and
	 * calling the appropriate function. All the lists (task list, undo list,
	 * redo list, last output task list, output) in Logic are subsequently
	 * updated with the values from the CommandFacade class.
	 */
	public void parseCommandObject(CommandObject commandObj, boolean isUndoAction, boolean isRedoAction) {
		if (!(isUndoAction || isRedoAction)) {
			commandObj.setLastSearchedIndex(lastSearchedIndex);
		}
		CommandFacade commandFacade = new CommandFacade(taskList, undoList, redoList, lastOutputTaskList, commandObj,
				isUndoAction, isRedoAction);
		commandFacade.run();
		updateLists(commandFacade);

		// System.out.println("Last searched index = " + lastSearchedIndex);
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

		if (commandFacade.getCommandType() == INDEX_SEARCH_DISPLAY) {
			setLastSearchedIndex(commandFacade.getLastSearchedIndex());
		} else {
			setLastSearchedIndex(-1);
		}
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
