package parser;
import logic.CommandObject;
import logic.TaskObject;

import java.util.ArrayList;

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

	private static final String EDIT_COMMAND = "edit";
	private static final int EDIT_INDEX = 3;

	private static final String DELETE_COMMAND = "delete";
	private static final int DELETE_INDEX = 4;

	private static final String UNDO_COMMAND = "undo";
	private static final int UNDO_INDEX = 5;

	private static final String SAVE_COMMAND = "save";
	private static final int SAVE_INDEX = 6;

	private static final String EXIT_COMMAND_1 = "exit";
	private static final String EXIT_COMMAND_2 = "quit";
	private static final int EXIT_INDEX = 7;

	private static final String HELP_COMMAND = "help";
	private static final int HELP_INDEX = 8;	
	
	private static CommandObject commandObject = new CommandObject();
	private static TaskObject taskObject = new TaskObject();
//command object. setType, setIndex, setTask, setDate, setTime, setPath
	
	private static String _command;
	
	public Parser(String command) {
		_command = command;
	}
	public static CommandObject run() {
		return parseInput(_command);
	}
	private static CommandObject parseInput(String command) {
		allocateCommandType(command);
		return commandObject;
	}

	private static void allocateCommandType(String command) {
		if(command.startsWith(EXIT_COMMAND_1) || command.startsWith(EXIT_COMMAND_2)) {
			commandObject.setCommandType(EXIT_INDEX);
		} else if(command.startsWith(HELP_COMMAND)) {
			commandObject.setCommandType(HELP_INDEX);
		} else if(command.startsWith(UNDO_COMMAND)) {
			commandObject.setCommandType(UNDO_INDEX);
		} else if(command.startsWith(EDIT_COMMAND)) {
			commandObject.setCommandType(EDIT_INDEX);
			EditProcessor EP = new EditProcessor();
			EP.processEdit(command);
			taskObject.setTitle(EP.getTask());
			taskObject.setStartTime(EP.getStartTime());
			taskObject.setEndTime(EP.getEndTime());
			taskObject.setStartDate(EP.getStartDate());
			taskObject.setEndDate(EP.getEndDate());
			commandObject.setTaskObject(taskObject);
		} else if(command.startsWith(SAVE_COMMAND)) {
			commandObject.setCommandType(SAVE_INDEX);
			setCommandObjectToSave(command);
		} else if(command.startsWith(DELETE_COMMAND)) {
			commandObject.setCommandType(DELETE_INDEX);
			setCommandObjectToDelete(command);
		} else if(command.startsWith(ADD_COMMAND)) {
			commandObject.setCommandType(ADD_INDEX);
			AddProcessor AP = new AddProcessor();
			AP.addCommand(command);
			//add these 5 main attributes
			taskObject.setTitle(AP.getTask());
			taskObject.setStartTime(AP.getStartTime());
			taskObject.setEndTime(AP.getEndTime());
			taskObject.setStartDate(AP.getStartDate());
			taskObject.setEndDate(AP.getEndDate());
			commandObject.setTaskObject(taskObject);
		} else if(isSearch(command)) {
			commandObject.setCommandType(SEARCH_INDEX);
			SearchProcessor SP = new SearchProcessor();
			SP.processSearchTerm(command);
		}
 	}
	
	/**
	 * method checks if the search keyword is present
	 */
 	private static boolean isSearch(String command) {
 		if(command.startsWith(SEARCH_COMMAND_1) || command.startsWith(SEARCH_COMMAND_2) 
 			|| command.startsWith(SEARCH_COMMAND_3) || command.startsWith(SEARCH_COMMAND_4) 
 			|| command.startsWith(SEARCH_COMMAND_5) || command.startsWith(SEARCH_COMMAND_6)) {
 			return true;
 		} else {
 			return false;
 		}
 	}
 	
 	private static void setCommandObjectToDelete(String command) {
 		String index;
 		index = extractDeleteIndex(command);
 		taskObject.setTitle(index);
 		commandObject.setTaskObject(taskObject);
 	}
 	
 	/**
 	 * this method returns the number that is after the delete command as an integer
 	 */
 	private static String extractDeleteIndex(String command) {		
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		newString = command.substring(index);
 		return newString;
 	}

 	private static void setCommandObjectToSave(String command) {
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		newString = command.substring(index);
 		taskObject.setTitle(newString);
 		commandObject.setTaskObject(taskObject);
 	}

 	
}
