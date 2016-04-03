package parser;

import common.CommandObject;
import common.TaskObject;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class will be the only class from the parser component which interacts
 * with the logic. Logic will initialise a parser object with the command and 
 * unique taskID and call run().
 * 
 * Parser will call the relevant method to process the command which the user has input. 
 * 
 * @author sylvesterchin
 *
 */
public class Parser {
	
	public CommandObject CO = new CommandObject();
	public TaskObject TO = new TaskObject();
    //command object. setType, setIndex, setTask, setDate, setTime, setPath
	
	private String _command;
	private int _taskId;	// ADDED TASKID VARIABLE 
	
	public Parser() {
	}
	
	/**
	 * This method constructs the parser object that will take in the user input and a internally 
	 * system generated taskID
	 * 
	 * @param command  
	 * 				user's input into the system of String type
	 * @param taskId   
	 * 				system generated ID
	 */
	public Parser(String command, int taskId) {
		_command = command;
		_taskId = taskId;	// ADDED INITIALISATION FOR TASKID
	}
	
	/**
	 * This method runs the methods to process the command and returns command object
	 * to Logic
	 * 
	 * @return CO 
	 * 				command object that contains task description and task object
	 * @throws Exception 
	 */
	public CommandObject run() throws Exception {
		allocate(_command);
		return CO;
	}
	
	/**
	 * This method reads string and trigger the relevant method to process string's information
	 * 
	 * @param command  
	 * 				user's input to the program, not null
	 * @throws Exception 
	 */
	public void allocate(String command) throws Exception {
		assert(!command.isEmpty()); //ensure command is a proper string
		
		command = command.trim();
		
		if (isMatch(Constants.REGEX_PARSER_EXIT, command)) {
			CO.setCommandType(Constants.EXIT_INDEX);
		} else if (isMatch(Constants.REGEX_PARSER_HELP, command)) {
			parseHelp(command);
		} else if (isMatch(Constants.REGEX_PARSER_UNDO, command)) {
			CO.setCommandType(Constants.UNDO_INDEX);
		} else if (isMatch(Constants.REGEX_PARSER_REDO, command)) {
			CO.setCommandType(Constants.REDO_INDEX);
		} else if (isMatch(Constants.REGEX_PARSER_EDIT, command)) {
			parseEdit(command);
		} else if (isMatch(Constants.REGEX_PARSER_SAVE, command)) {
			parseSave(command);
		} else if (isMatch(Constants.REGEX_PARSER_DELETE, command)) {
			parseDelete(command);
		} else if (isMatch(Constants.REGEX_PARSER_ADD, command)) {
			parseAdd(command);
		} else if (isMatch(Constants.REGEX_PARSER_DONE, command)) {
			parseDone(command);
		} else if (isMatch(Constants.REGEX_PARSER_NOTDONE, command)) {
			parseNotDone(command);
		} else if (isMatch(Constants.REGEX_PARSER_SEARCH, command)) {
			parseSearch(command);
		} else {
			parseSearch(command);
		}
  	}
	
	private boolean isMatch(String input, String command) {
		Pattern dateTimePattern = Pattern.compile(input);
		Matcher matcher = dateTimePattern.matcher(command);
		return matcher.find();
	}
	
	
	/**
	 * This method returns help index to CommandObject when the
	 *  
	 * @param command
	 * 				string input that represents a help command
	 */
	public void parseHelp(String command) {
		CO.setCommandType(Constants.HELP_INDEX);
		command = command.replaceFirst("(?i)(help )", "");
		TO.setTitle(command);
		CO.setTaskObject(TO);
	}
	
	
	/**
	 * This method sets command type and index of the task to be marked as done
	 * 
	 * @param  command
	 * 				string input that represent a done command
	 */
	public void parseDone(String command) {
		int temp = command.indexOf(" ");
		if (temp != -1) {
			CO.setCommandType(Constants.DONE_INDEX);
			command = command.substring(temp + 1);
			//taskObject.setTitle(command);  --> can remove this after logic passes the tests
			temp = Integer.parseInt(command);
			CO.setIndex(temp);	
		} else {
			CO.setCommandType(Constants.SEARCH_INDEX);
			TO.setStatus("completed");
			CO.setTaskObject(TO);
		}
	}
	
	/**
	 * This method sets command type and index of task to be marked as incomplete
	 * 
	 * @param command
	 * 				string input that represents a notdone command
	 */
	public void parseNotDone(String command) {
		int temp = command.indexOf(" ");
		if (temp != -1) {
			CO.setCommandType(Constants.NOTDONE_INDEX);
			command = command.substring(temp + 1);
			//taskObject.setTitle(command);  --> can remove this after logic passes the tests
			temp = Integer.parseInt(command);
			CO.setIndex(temp);	
		} else {
			CO.setCommandType(Constants.SEARCH_INDEX);
			TO.setStatus("incomplete");
			CO.setTaskObject(TO);
		}
	}
	
	
	/**
	 * This method sets command type, index of task to edit and parts of the task to edit
	 * 
	 * @param command   
	 * 				user's input for the system, such as "edit 5 6pm start" 
	 * @throws Exception 
	 */
	public void parseEdit(String command) throws Exception {
		CO.setCommandType(Constants.EDIT_INDEX);
		
		if (command.contains("edit all")) {
			command = command.replaceFirst("edit all", "").trim();
			TO.setIsRecurring(true);
			TO.setIsEditAll(true);
		} else {
			command = command.replaceFirst(Constants.REGEX_PARSER_EDIT, "").trim();
		}
		
		CommandParser EP = new EditParser();
		TO = EP.process(command);
		CO.setTaskObject(TO);
		CO.setIndex(EP.getIndex());
		EP.reset();
	}
	
	/**
	 * This method sets command type and creates task object with details keyed in by user
	 * 
	 * @param command   
	 * 				string input that represents an add command
	 * @throws Exception 
	 */
	public void parseAdd(String command) throws Exception {
		CO.setCommandType(Constants.ADD_INDEX);
		CommandParser AP = new AddParser();
		command = command.replaceFirst(Constants.REGEX_PARSER_ADD, "").trim();
		TO = AP.process(command);
		TO.setTaskId(_taskId);
		setCategory();
		CO.setTaskObject(TO);
		AP.reset();
	}
	
	/**
	 * This method sets command type and creates task object with details entered by user 
	 * for search purpose
	 * 
	 * @param command   
	 * 				string input that represents a search command
	 * @throws Exception 
	 */
	public void parseSearch(String command) throws Exception {
		CO.setCommandType(Constants.SEARCH_INDEX);
		CommandParser SP = new SearchParser();

		// if there is no search keyword, set TaskObject values to null/-1
		if (command.indexOf(" ") == -1 && isMatch(Constants.REGEX_PARSER_SEARCH,command)) {
			TO.setStartTime(-1);
			TO.setEndTime(-1);
			TO.setStartDate(-1);
			TO.setEndDate(-1);
		} else {
			command = command.substring(command.indexOf(" ")+1);
			TO = SP.process(command);
			CO.setIndex(SP.getIndex());
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
		}  else {
			TO.setCategory("event"); //edited mistake here
		}
	}
	
	public boolean isFloating() {
		if (TO.getEndDateTime().equals(LocalDateTime.MAX) 
				&& TO.getStartDateTime().equals(LocalDateTime.MAX)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isDeadline() {
		if (TO.getEndDateTime().equals(LocalDateTime.MAX)) {
			return true;
		} else {
			return false;
		}
	}

 	
 	/**
 	 * This method sets command type for delete commands 
 	 * 
 	 * @param command 
 	 * 				user's input as a string for deleting
 	 * @throws Exception 
 	 */
 	public void parseDelete(String command) throws Exception {
 		CO.setCommandType(Constants.DELETE_INDEX);
 		int index;
 		index = extractDeleteIndex(command);
 		CO.setIndex(index);
 		if (index > 0 && command.contains("all")) {
 			TO.setIsEditAll(true);
 			TO.setTitle("all");
 			CO.setTaskObject(TO);
 		}
 	}
 	
 	
 	/**
 	 * this method returns the number that is after the delete command as an integer
 	 * 
 	 * @param command
 	 * 				string input that represents a delete command
 	 * @throws Exception 
 	 */
 	public int extractDeleteIndex(String command) throws Exception {		
 		String newString;
 		if (command.indexOf(" ") == -1) {	// if it is a delete command with no specified index
 			return -1; //quick delete
 		} else if (command.replaceFirst("delete","").trim().matches("(?i)(all)")) { //delete all
 			TO.setIsEditAll(true); 
 			return 0;
 		} else { //delete with index
	 		int index = command.indexOf(" ") + 1;
	 		newString = command.substring(index).replaceAll("[a-zA-Z]+", "").trim();
 		}
 		//implementation for delete particular recurring/all recurring
 		//delete 2 this ?
 		//delete 2 all ?
 		
	 	return Integer.parseInt(newString);
 	}

 	/**
 	 * This method sets command type for command object and returns file path
 	 * 
 	 * @param command 
 	 * 				string input that represents a save command 
 	 * @throws Exception 
 	 */
 	public void parseSave(String command) throws Exception {
 		CO.setCommandType(Constants.SAVE_INDEX);
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		if (command.length() > index) {
 			newString = command.substring(index);
 	 		TO.setTitle(newString);
 	 		CO.setTaskObject(TO);	
 		} else {
 			throw new Exception("Filepath missing");
 		}
 		
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
 	
 	public LocalDateTime getStartDateTime() {
 		return TO.getStartDateTime();
 	}
 	
 	public LocalDateTime getEndDateTime() {
 		return TO.getEndDateTime();
 	}
 	
 	public String getStatus() {
 		return TO.getStatus();
 	}
 	
 	public String getCategory() {
 		return TO.getCategory();
 	}
 	
 	public void resetTaskObj() {
 		TO.resetAttributes();
 	}
 	
 	public int getCommandTask() {
 		return CO.getCommandType();
 	}
}
