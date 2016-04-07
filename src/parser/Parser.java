//@@author A0125003A
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
	
	/**
	 * This is the general constructor for testing purposes.
	 */
	public Parser() {
	}
	
	/**
	 * This method constructs the parser object and assigns the command and a internally 
	 * system generated taskID.
	 * 
	 * @param command  
	 * 				user's input into the system of String type.
	 * @param taskId   
	 * 				system generated ID.
	 */
	public Parser(String command, int taskId) {
		_command = command;
		_taskId = taskId;	// ADDED INITIALISATION FOR TASKID
	}
	
	/**
	 * This method runs the methods to process the command and returns command object
	 * to Logic.
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
	 * using a set of if-else conditions to check for task-related keywords. If keywords are absent, 
	 * it will be processed as a search.
	 * 
	 * @param command  
	 * 				user's input to the program, not null.
	 * @throws Exception 
	 */
	public void allocate(String command) throws Exception {
		assert(!command.isEmpty()); //ensure command is a proper string
		
		command = command.trim();
		
		if (isMatch(Constants.REGEX_PARSER_EXIT, command)) {
			CO.setCommandType(Constants.INDEX_EXIT);
		} else if (isMatch(Constants.REGEX_PARSER_HELP, command)) {
			parseHelp(command);
		} else if (isMatch(Constants.REGEX_PARSER_UNDO, command)) {
			CO.setCommandType(Constants.INDEX_UNDO);
		} else if (isMatch(Constants.REGEX_PARSER_REDO, command)) {
			CO.setCommandType(Constants.INDEX_REDO);
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
	
	
	// ================================
	// First Level of Abstraction
	// ================================
	
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
	private void parseHelp(String command) {
		CO.setCommandType(Constants.INDEX_HELP);
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
	private void parseDone(String command) {
		int temp = command.indexOf(" ");
		if (temp != -1) {
			CO.setCommandType(Constants.INDEX_DONE);
			command = command.substring(temp + 1);
			//taskObject.setTitle(command);  --> can remove this after logic passes the tests
			temp = Integer.parseInt(command);
			CO.setIndex(temp);	
		} else {
			CO.setCommandType(Constants.INDEX_SEARCH);
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
	private void parseNotDone(String command) {
		int temp = command.indexOf(" ");
		if (temp != -1) {
			CO.setCommandType(Constants.INDEX_NOTDONE);
			command = command.substring(temp + 1);
			//taskObject.setTitle(command);  --> can remove this after logic passes the tests
			temp = Integer.parseInt(command);
			CO.setIndex(temp);	
		} else {
			CO.setCommandType(Constants.INDEX_SEARCH);
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
	private void parseEdit(String command) throws Exception {
		CO.setCommandType(Constants.INDEX_EDIT);
		boolean isEditAllRecurring = false;
		
		if (command.toLowerCase().startsWith("edit all")) {
			System.out.println(command);
			command = command.replaceFirst("(?i)(edit all)", "").trim();
			isEditAllRecurring = true;
		} else {
			command = command.replaceFirst(Constants.REGEX_PARSER_EDIT, "").trim();
		}
		
		CommandParser EP = new EditParser();
		TO = EP.process(command);
		
		if (isEditAllRecurring) {
			TO.setIsRecurring(true);
			TO.setIsEditAll(true);
		}
		
		CO.setTaskObject(TO);
		CO.setIndex(EP.getIndex());
	}
	
	/**
	 * This method sets command type and creates task object with details keyed in by user
	 * 
	 * @param command   
	 * 				string input that represents an add command
	 * @throws Exception 
	 */
	private void parseAdd(String command) throws Exception {
		CO.setCommandType(Constants.INDEX_ADD);
		CommandParser AP = new AddParser();
		command = command.replaceFirst(Constants.REGEX_PARSER_ADD, "").trim();
		TO = AP.process(command);
		TO.setTaskId(_taskId);
		setCategory();
		CO.setTaskObject(TO);
	}
	
	/**
	 * This method sets command type and creates task object with details entered by user 
	 * for search purpose
	 * 
	 * @param command   
	 * 				string input that represents a search command
	 * @throws Exception 
	 */
	private void parseSearch(String command) throws Exception {
		CO.setCommandType(Constants.INDEX_SEARCH);
		CommandParser SP = new SearchParser();

		// if there is no search keyword, set TaskObject values to null/-1
		if (command.indexOf(" ") == -1 && isMatch(Constants.REGEX_PARSER_SEARCH,command)) {
			TO.setStartDateTime(LocalDateTime.MAX);
			TO.setEndDateTime(LocalDateTime.MAX);
		} else {
			command = command.substring(command.indexOf(" ")+1);
			TO = SP.process(command);
			CO.setIndex(SP.getIndex());
		}
		CO.setTaskObject(TO);
	}
	
	/**
 	 * This method sets command type for delete commands 
 	 * 
 	 * @param command 
 	 * 				user's input as a string for deleting
 	 * @throws Exception 
 	 */
	private void parseDelete(String command) throws Exception {
 		CO.setCommandType(Constants.INDEX_DELETE);
 		int index;
 		index = extractDeleteIndex(command);
 		CO.setIndex(index);
 		if (index > 0 && command.contains("all")) {
 			TO.setIsEditAll(true);
 			TO.setTitle("all");
 		} else if (index == 0 && command.contains("done")) {
 			TO.setStatus("completed");
 		}
 		CO.setTaskObject(TO);	
 	}
	
	/**
 	 * This method sets command type for command object and returns file path
 	 * 
 	 * @param command 
 	 * 				string input that represents a save command 
 	 * @throws Exception 
 	 */
	private void parseSave(String command) throws Exception {
 		CO.setCommandType(Constants.INDEX_SAVE);
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
	
	// ================================
	// Second Level of Abstraction
	// ================================
	
	private void setCategory() {
		if (isFloating()) {
			TO.setCategory("floating");
		} else if (isDeadline()) {
			TO.setCategory("deadline");
		}  else {
			TO.setCategory("event"); //edited mistake here
		}
	}
	
	private boolean isFloating() {
		if (TO.getEndDateTime().equals(LocalDateTime.MAX) 
				&& TO.getStartDateTime().equals(LocalDateTime.MAX)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isDeadline() {
		if (TO.getEndDateTime().equals(LocalDateTime.MAX)) {
			return true;
		} else {
			return false;
		}
	}

 	
 	/**
 	 * This method returns the number that is after the delete command as an integer
 	 * 
 	 * @param command
 	 * 				string input that represents a delete command
 	 * @throws Exception 
 	 */
	private int extractDeleteIndex(String command) throws Exception {		
 		String newString;
 		if (command.indexOf(" ") == -1) {	// if it is a delete command with no specified index
 			return -1; //quick delete
 		} else if (command.replaceFirst("delete","").trim().matches("(?i)(all)")) { //delete all
 			TO.setIsEditAll(true); 
 			return 0;
 		} else if (command.replaceFirst("delete","").trim().matches("(?i)(done)")) { //delete done
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

 	
 	
 	//all the getters for testing purposes
 	
 	public int getCommandType() {
 		return CO.getCommandType();
 	}

 	public String getTask() {
 		return TO.getTitle();
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
