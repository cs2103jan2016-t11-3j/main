//@@author A0125003A
package parser;

import common.AtfLogger;
import common.CommandObject;
import common.TaskObject;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class takes in the user's input and processes it, identifying the command type
 * and details of the command to exeucute.
 *  
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
	private static Logger logger = AtfLogger.getLogger();
	private String _command;
	private int _taskId;
	
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
		_taskId = taskId;
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
			logger.log(Level.INFO, "Exit command called.");
			CO.setCommandType(Constants.INDEX_EXIT);
		} else if (isMatch(Constants.REGEX_PARSER_HELP, command)) {
			parseHelp(command);
		} else if (command.equals(Constants.REGEX_PARSER_UNDO)) {
			logger.log(Level.INFO, "Undo command called.");
			CO.setCommandType(Constants.INDEX_UNDO);
		} else if (isMatch(Constants.REGEX_PARSER_REDO, command)) {
			logger.log(Level.INFO, "Redo command called.");
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
		} else if (isMatch(Constants.REGEX_PARSER_LOAD, command)) {
			parseLoad(command);
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
	 * This method returns help index to CommandObject and a search term if present.
	 *  
	 * @param command
	 * 				string input that represents a help command.
	 */
	private void parseHelp(String command) {
		logger.log(Level.INFO, "Help command called.");
		CO.setCommandType(Constants.INDEX_HELP);
		command = command.replaceFirst("(?i)(help )", "");
		TO.setTitle(command);
		CO.setTaskObject(TO);
	}
	
	/**
	 * This method sets command type and index of the task to be marked as done.
	 * 
	 * @param  command
	 * 				string input that represent a done command.
	 */
	private void parseDone(String command) {
		int temp = command.indexOf(" ");
		if (temp != -1) {
			logger.log(Level.INFO, "Done command called.");
			CO.setCommandType(Constants.INDEX_DONE);
			command = command.substring(temp + 1);
			temp = Integer.parseInt(command);
			CO.setIndex(temp);	
		} else {
			logger.log(Level.INFO, "Search command called.");
			CO.setCommandType(Constants.INDEX_SEARCH);
			TO.setStatus("completed");
			CO.setTaskObject(TO);
		}
	}
	
	/**
	 * This method sets command type and index of task to be marked as incomplete.
	 * 
	 * @param command
	 * 				string input that represents a not-done command.
	 */
	private void parseNotDone(String command) {
		logger.log(Level.INFO, "Not-done command called.");
		int temp = command.indexOf(" ");
		if (temp != -1) {
			CO.setCommandType(Constants.INDEX_NOTDONE);
			command = command.substring(temp + 1);
			temp = Integer.parseInt(command);
			CO.setIndex(temp);	
		} else {
			CO.setCommandType(Constants.INDEX_SEARCH);
			TO.setStatus("incomplete");
			CO.setTaskObject(TO);
		}
	}
	
	/**
	 * This method sets command type, index of task to edit and attributes to edit.
	 * 
	 * @param command   
	 * 				user's input for the system, such as "edit 5 6pm start".
	 * @throws Exception 
	 */
	private void parseEdit(String command) throws Exception {
		logger.log(Level.INFO, "Edit command called.");
		CO.setCommandType(Constants.INDEX_EDIT);
		boolean isEditAllRecurring = false;
		
		if (command.toLowerCase().startsWith("edit all") 
				|| command.toLowerCase().startsWith("update all")) {
			System.out.println(command);
			command = command.replaceFirst("(?i)((edit|update) all)", "").trim();
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
	 * This method sets command type and creates task object with details keyed in by user.
	 * 
	 * @param command   
	 * 				string input that represents an add command.
	 * @throws Exception 
	 */
	private void parseAdd(String command) throws Exception {
		logger.log(Level.INFO, "Add command called.");
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
	 * for search purpose.
	 * 
	 * @param command   
	 * 				string input that represents a search command.
	 * @throws Exception 
	 */
	private void parseSearch(String command) throws Exception {
		logger.log(Level.INFO, "Search command called.");
		CO.setCommandType(Constants.INDEX_SEARCH);
		CommandParser SP = new SearchParser();

		// if there is no search keyword, set TaskObject values to null/-1
		if (command.indexOf(" ") == -1 && isMatch(Constants.REGEX_PARSER_SEARCH,command)) {
			TO.setStartDateTime(LocalDateTime.MAX);
			TO.setEndDateTime(LocalDateTime.MAX);
		} else {
			command = command.replaceFirst(Constants.REGEX_PARSER_SEARCH, "").trim();
			TO = SP.process(command);
			CO.setIndex(SP.getIndex());
		}
		CO.setTaskObject(TO);
	}
	
	/**
 	 * This method sets command type for delete commands and keyword "all" if included.
 	 * 
 	 * @param command 
 	 * 				user's input as a string for deleting.
 	 * @throws Exception 
 	 */
	private void parseDelete(String command) throws Exception {
		logger.log(Level.INFO, "Delete command called.");
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
 	 * This method sets command type for command object and returns file path.
 	 * 
 	 * @param command 
 	 * 				string input that represents a save command.
 	 * @throws Exception 
 	 */
	private void parseSave(String command) throws Exception {
		logger.log(Level.INFO, "Save command called.");
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
	
	/**
	 * This method sets command type for command object and specifies type of load, 
	 * (1) backup or (2) specific file.
	 * 
	 * @param command
	 * 					user input with load command. non-null.
	 * @throws Exception
	 */
	private void parseLoad(String command) throws Exception {
		logger.log(Level.INFO, "Load command called.");
		CO.setCommandType(Constants.INDEX_LOAD);
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		
 		if (command.length() > index) {
 			if (command.contains("backup")) {
 				newString = "backup";
 			} else {
 				newString = command.substring(index);
 			}
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
			TO.setCategory(Constants.TaskType.floating.toString());
		} else if (isDeadline()) {
			TO.setCategory(Constants.TaskType.deadline.toString());
		}  else {
			TO.setCategory(Constants.TaskType.event.toString());
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
 	 * This method returns the number that is after the delete command as an integer.
 	 * 
 	 * @param command
 	 * 				string input that represents a delete command.
 	 * @throws Exception 
 	 */
	private int extractDeleteIndex(String command) throws Exception {		
 		String newString;
 		if (command.indexOf(" ") == -1) {
 			//quick delete
 			return -1; 
 		} else if (command.replaceFirst(Constants.REGEX_PARSER_DELETE, "").trim().matches("(?i)(all)")) {
 			TO.setIsEditAll(true); 
 			return 0;
 		} else if (command.replaceFirst(Constants.REGEX_PARSER_DELETE, "").trim().matches("(?i)(done)")) {
 			return 0;
 		} else {
	 		int index = command.indexOf(" ") + 1;
	 		newString = command.substring(index).replaceAll("[a-zA-Z]+", "").trim();
 		} 		
	 	return Integer.parseInt(newString);
 	}
 	
 	//All the getters for testing purposes.
 	
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
