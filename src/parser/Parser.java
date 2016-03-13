package parser;
import common.CommandObject;
import common.TaskObject;


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
	private static final String DONE_COMMAND_3 = "complete";
	private static final String DONE_COMMAND_4 = "completed";
	private static final int DONE_INDEX = 10;
	
	private static final String NOTDONE_COMMAND_1 = "undone";
	private static final String NOTDONE_COMMAND_3 = "incomplete";
	private static final int NOTDONE_INDEX = 11;
	
	public CommandObject CO = new CommandObject();
	public TaskObject TO = new TaskObject();
//command object. setType, setIndex, setTask, setDate, setTime, setPath
	
	private String _command;
	private int _taskId;	// ADDED TASKID VARIABLE 
	
	public Parser() {
	}
	
	/**
	 * Constructs the parser object that will take in the user input and a internally 
	 * system generated taskID
	 * 
	 * @param command  user's input into the system of String type
	 * @param taskId   system generated ID
	 */
	public Parser(String command, int taskId) {
		_command = command;
		_taskId = taskId;	// ADDED INITIALISATION FOR TASKID
	}
	
	/**
	 * Runs the methods to process the command and returns command object
	 * to Logic
	 * 
	 * @return CO command object that contains task description and task object
	 */
	public CommandObject run() {
		allocate(_command);
		return CO;
	}
	
	/**
	 * method reads string and trigger the relevant method to process string's information
	 * 
	 * @param command  is the user's input to the program
	 */
	public void allocate(String command) {
		if (command.startsWith(EXIT_COMMAND_1) || command.startsWith(EXIT_COMMAND_2)) {
			CO.setCommandType(EXIT_INDEX);
		} else if (command.startsWith(HELP_COMMAND)) {
			parseHelp(command);
		} else if (command.startsWith(UNDO_COMMAND)) {
			CO.setCommandType(UNDO_INDEX);
		} else if (command.startsWith(REDO_COMMAND)) {
			CO.setCommandType(REDO_INDEX);
		} else if (command.startsWith(EDIT_COMMAND_1) || command.startsWith(EDIT_COMMAND_2)) {
			parseEdit(command);
		} else if (command.startsWith(SAVE_COMMAND)) {
			parseSave(command);
		} else if (command.startsWith(DELETE_COMMAND)) {
			parseDelete(command);
		} else if (command.startsWith(ADD_COMMAND)) {
			parseAdd(command);
		} else if (command.startsWith(DONE_COMMAND_1) || command.startsWith(DONE_COMMAND_2)
				|| command.startsWith(DONE_COMMAND_3) || command.startsWith(DONE_COMMAND_4)) {
			parseDone(command);
		} else if (command.startsWith(NOTDONE_COMMAND_1) || command.startsWith(NOTDONE_COMMAND_3)) {
			parseNotDone(command);
		} else if (isSearch(command)) {
			parseSearch(command);
		} else {
			parseSearch(command);
		}
  	}
	
	public void parseHelp(String command) {
		CO.setCommandType(HELP_INDEX);
		command = command.replaceFirst("(?i)(help )", "");
		TO.setTitle(command);
		CO.setTaskObject(TO);
	}
	
	
	/**
	 * method sets command type and index of the task to be marked as done
	 * 
	 * @param  command   user's input
	 */
	public void parseDone(String command) {
		CO.setCommandType(DONE_INDEX);
		int temp = command.indexOf(" ");
		command = command.substring(temp + 1);
		//taskObject.setTitle(command);  --> can remove this after logic passes the tests
		temp = Integer.parseInt(command);
		CO.setIndex(temp);
	}
	
	public void parseNotDone(String command) {
		CO.setCommandType(NOTDONE_INDEX);
		int temp = command.indexOf(" ");
		command = command.substring(temp + 1);
		//taskObject.setTitle(command);  --> can remove this after logic passes the tests
		temp = Integer.parseInt(command);
		CO.setIndex(temp);
	}
	
	/**
	 * method sets command type, index of task to edit and parts of the task to edit
	 * 
	 * @param command   user's input
	 */
	public void parseEdit(String command) {
		CO.setCommandType(EDIT_INDEX);
		CommandParser EP = new EditParser();
		TO = EP.process(command);
		CO.setTaskObject(TO);
		CO.setIndex(EP.getIndex());
		EP.reset();
	}
	
	/**
	 * method sets command type and creates task object with details keyed in by user
	 * 
	 * @param command   user's input as a string
	 */
	public void parseAdd(String command) {
		CO.setCommandType(ADD_INDEX);
		CommandParser AP = new AddParser();
		TO = AP.process(command);
		//add these 5 main attributes
		TO.setTaskId(_taskId);
		CO.setTaskObject(TO);
		setCategory();
		AP.reset();
	}
	
	/**
	 * method sets command type and creates task object with details entered by user 
	 * for search purpose
	 * 
	 * @param command   user's input as a string
	 */
	public void parseSearch(String command) {
		CO.setCommandType(SEARCH_INDEX);
		CommandParser SP = new SearchParser();
		
		// if there is no search keyword, set TaskObject values to null/-1
		if (command.indexOf(" ") == -1 && isSearch(command)) {
			TO.setStartTime(-1);
			TO.setEndTime(-1);
			TO.setStartDate(-1);
			TO.setEndDate(-1);
		} else {
			command = command.substring(command.indexOf(" ")+1);
			TO = SP.process(command);
			
		}
		CO.setTaskObject(TO);
	}
	
	
	public void setCategory() {
		if (isFloating()) {
			TO.setCategory("floating");
		} else if (isDeadline()) {
			TO.setCategory("deadline");
			TO.setEndTime(TO.getStartTime());
			TO.setEndDate(TO.getStartDate());
		} else {
			TO.setCategory("event"); //edited mistake here
		}
	}
	
	public boolean isFloating() {
		if (TO.getStartDate() == -1 && TO.getEndDate() == -1
				&& TO.getStartTime() == -1 && TO.getEndTime() ==-1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDeadline() {
		if (TO.getEndDate() == -1
				&& TO.getEndTime() == -1) {
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
 	
 	/**
 	 * method sets command type for delete commands 
 	 * 
 	 * @param command user's input as a string
 	 */
 	public void parseDelete(String command) {
 		CO.setCommandType(DELETE_INDEX);
 		int index;
 		index = extractDeleteIndex(command);
 		CO.setIndex(index);
 	}
 	
 	/**
 	 * this method returns the number that is after the delete command as an integer
 	 */
 	public int extractDeleteIndex(String command) {		
 		String newString;
 		if (command.indexOf(" ") == -1) {	// if it is a delete command with no specified index
 			return -1;
 		} else if (command.contains("all")) {
 			return 0;
 		} else {
	 		int index = command.indexOf(" ") + 1;
	 		newString = command.substring(index);
 		}
	 	return Integer.parseInt(newString);
 	}

 	/**
 	 * method sets command type for command object and returns file path
 	 * 
 	 * @param command user's input as a string 
 	 */
 	public void parseSave(String command) {
 		CO.setCommandType(SAVE_INDEX);
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		newString = command.substring(index);
 		TO.setTitle(newString);
 		CO.setTaskObject(TO);
 	}
 	
 	//all the getters for testing purposes
 	
 	public int getCommandType() {
 		return CO.getCommandType();
 	}

 	public String getTask() {
 		return TO.getTitle();
 	}
 	
 	public int getStartDate() {
 		return TO.getStartDate();
 	}
 	
 	public int getStartTime() {
 		return TO.getStartTime();
 	}
 	
 	public int getEndDate() {
 		return TO.getEndDate();
 	}
 	
 	public int getEndTime() {
 		return TO.getEndTime();
 	}
 	
 	public String getStatus() {
 		return TO.getStatus();
 	}
 	
 	public void resetTaskObj() {
 		TO.resetAttributes();
 	}
 	
 	public int getCommandTask() {
 		return CO.getCommandType();
 	}
}
