package parser;
import logic.CommandObject;
import logic.TaskObject;

/*
parser takes in string


*/
public class Parser {

	private static final String ADD_COMMAND = "add";
	private static final int ADD_INDEX = 1;
	
	private static final String SEARCH_COMMAND_1 = "view";
	private static final String SEARCH_COMMAND_2 = "search";
	private static final String SEARCH_COMMAND_3 = "sort";
	private static final String SEARCH_COMMAND_4 = "find";
	private static final String SEARCH_COMMAND_5 = "filter";
	private static final String SEARCH_COMMAND_6 = "display";
	private static final int SEARCH_INDEX = 2;

	private static final String EDIT_COMMAND_1 = "edit";
	private static final String EDIT_COMMAND_2 = "update";
	private static final int EDIT_INDEX = 3;

	private static final String DELETE_COMMAND = "delete";
	private static final int DELETE_INDEX = 4;

	private static final String UNDO_COMMAND = "undo";
	private static final int UNDO_INDEX = 5;
	
	private static final String REDO_COMMAND = "redo";
	private static final int REDO_INDEX = 6;

	private static final String SAVE_COMMAND = "save";
	private static final int SAVE_INDEX = 7;

	private static final String EXIT_COMMAND_1 = "exit";
	private static final String EXIT_COMMAND_2 = "quit";
	private static final int EXIT_INDEX = 8;

	private static final String HELP_COMMAND = "help";
	private static final int HELP_INDEX = 9;	
	
	private static final String DONE_COMMAND_1 = "done";
	private static final String DONE_COMMAND_2 = "finish";
	private static final String DONE_COMMAND_3 = "completed";
	private static final int DONE_INDEX = 10;
	
	private CommandObject commandObject = new CommandObject();
	private TaskObject taskObject = new TaskObject();
//command object. setType, setIndex, setTask, setDate, setTime, setPath
	
	private String _command;
	private int _taskId;	// ADDED TASKID VARIABLE 
	
	public Parser() {
	}
	
	public Parser(String command, int taskId) {
		_command = command;
		_taskId = taskId;	// ADDED INITIALISATION FOR TASKID
	}
	public CommandObject run() {
		return parseInput(_command);
	}
	private CommandObject parseInput(String command) {
		allocateCommandType(command);
		return commandObject;
	}

	public void allocateCommandType(String command) {
		if (command.startsWith(EXIT_COMMAND_1) || command.startsWith(EXIT_COMMAND_2)) {
			commandObject.setCommandType(EXIT_INDEX);
		} else if (command.startsWith(HELP_COMMAND)) {
			commandObject.setCommandType(HELP_INDEX);
		} else if (command.startsWith(UNDO_COMMAND)) {
			commandObject.setCommandType(UNDO_INDEX);
		} else if (command.startsWith(REDO_COMMAND)) {
			commandObject.setCommandType(REDO_INDEX);
		} else if (command.startsWith(EDIT_COMMAND_1) || command.startsWith(EDIT_COMMAND_2)) {
			parseEdit(command);
		} else if (command.startsWith(SAVE_COMMAND)) {
			setCommandObjectToSave(command);
		} else if (command.startsWith(DELETE_COMMAND)) {
			setCommandObjectToDelete(command);
		} else if (command.startsWith(ADD_COMMAND)) {
			parseAdd(command);
		} else if (isSearch(command)) {
			parseSearch(command);
		} else if (command.startsWith(DONE_COMMAND_1) || command.startsWith(DONE_COMMAND_2)
				|| command.startsWith(DONE_COMMAND_3)) {
			parseDone(command);
		}
  	}
	
	public void parseDone(String command) {
		commandObject.setCommandType(DONE_INDEX);
		int temp = command.indexOf(" ");
		command = command.substring(temp);
		taskObject.setTitle("command");
		commandObject.setTaskObject(taskObject);
	}
	
	public void parseEdit(String command) {
		commandObject.setCommandType(EDIT_INDEX);
		EditProcessor EP = new EditProcessor();
		EP.processEdit(command);
		taskObject.setTitle(EP.getTask());
		taskObject.setStartTime(EP.getStartTime());
		taskObject.setEndTime(EP.getEndTime());
		taskObject.setStartDate(EP.getStartDate());
		taskObject.setEndDate(EP.getEndDate());
		commandObject.setTaskObject(taskObject);
		commandObject.setIndex(EP.getIndex());
		EP.resetAll();
	}
	
	public void parseAdd(String command) {
		commandObject.setCommandType(ADD_INDEX);
		AddProcessor AP = new AddProcessor();
		AP.addCommand(command);
		//add these 5 main attributes
		taskObject.setTitle(AP.getTask());
		taskObject.setStartTime(AP.getStartTime());
		taskObject.setEndTime(AP.getEndTime());
		taskObject.setStartDate(AP.getStartDate());
		taskObject.setEndDate(AP.getEndDate());
		taskObject.setTaskId(_taskId);	// ADDED
		commandObject.setTaskObject(taskObject);
		setCategory();
		AP.reset();
	}
	
	public void parseSearch(String command) {
		commandObject.setCommandType(SEARCH_INDEX);
		SearchProcessor SP = new SearchProcessor();
		
		// if there is no search keyword, set TaskObject values to null/-1
		if (command.indexOf(" ") == -1) {
			taskObject.setStartTime(-1);
			taskObject.setEndTime(-1);
			taskObject.setStartDate(-1);
			taskObject.setEndDate(-1);
		} else {
			command = command.substring(command.indexOf(" ")+1);
			SP.processSearchTerm(command);
			taskObject.setTitle(SP.getTask());
			taskObject.setStartTime(SP.getStartTime());
			taskObject.setEndTime(SP.getEndTime());
			taskObject.setStartDate(SP.getStartDate());
			taskObject.setEndDate(SP.getEndDate());
		}
		commandObject.setTaskObject(taskObject);
	}
	
	public void setCategory() {
		if (isFloating()) {
			taskObject.setCategory("floating");
		} else if (isDeadline()) {
			taskObject.setCategory("deadline");
		} else {
			taskObject.setCategory("deadline");
		}
	}
	
	public boolean isFloating() {
		if (taskObject.getStartDate() == -1 && taskObject.getEndDate() == -1
				&& taskObject.getStartTime() == -1 && taskObject.getEndTime() ==-1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDeadline() {
		if (taskObject.getStartDate() == taskObject.getEndDate()
				&& taskObject.getStartTime() == taskObject.getEndTime()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * method checks if the search keyword is present
	 */
 	public boolean isSearch(String command) {
 		if(command.startsWith(SEARCH_COMMAND_1) || command.startsWith(SEARCH_COMMAND_2) 
 			|| command.startsWith(SEARCH_COMMAND_3) || command.startsWith(SEARCH_COMMAND_4) 
 			|| command.startsWith(SEARCH_COMMAND_5) || command.startsWith(SEARCH_COMMAND_6)) {
 			return true;
 		} else {
 			return false;
 		}
 	}
 	
 	public void setCommandObjectToDelete(String command) {
 		commandObject.setCommandType(DELETE_INDEX);
 		int index;
 		index = extractDeleteIndex(command);
 		commandObject.setIndex(index);
 	}
 	
 	/**
 	 * this method returns the number that is after the delete command as an integer
 	 */
 	public int extractDeleteIndex(String command) {		
 		String newString;
 		if (command.indexOf(" ") == -1) {	// if it is a delete command with no specified index
 			return -1;
 		} else {
	 		int index = command.indexOf(" ") + 1;
	 		newString = command.substring(index);
 		}
	 	return Integer.parseInt(newString);
 	}

 	public void setCommandObjectToSave(String command) {
 		commandObject.setCommandType(SAVE_INDEX);
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		newString = command.substring(index);
 		taskObject.setTitle(newString);
 		commandObject.setTaskObject(taskObject);
 	}
 	
 	public int getCommandType() {
 		return commandObject.getCommandType();
 	}

 	public String getTask() {
 		return taskObject.getTitle();
 	}
 	
 	public int getStartDate() {
 		return taskObject.getStartDate();
 	}
 	
 	public int getStartTime() {
 		return taskObject.getStartTime();
 	}
 	
 	public int getEndDate() {
 		return taskObject.getEndDate();
 	}
 	
 	public int getEndTime() {
 		return taskObject.getEndTime();
 	}
 	
 	public void resetTaskObj() {
 		taskObject.resetAttributes();
 	}
 	
 	public int getCommandTask() {
 		return commandObject.getCommandType();
 	}
}
