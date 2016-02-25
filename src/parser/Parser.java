package parser;

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

//command object. setType, setIndex, setTask, setDate, setTime, setPath


	private static commandObject parseInput(String command) {
		CommandObject commandObject = new CommandObject();
		allocateCommandType(command);
		return commandObject;
	}

	private static void allocateCommandType(String command) {
		if(command.startsWith(EXIT_COMMAND_1) || command.startsWith(EXIT_COMMAND_2)) {
		//	commandObject.setType(EXIT_INDEX);
		} else if(command.startsWith(HELP_COMMAND)) {
			//commandObject.setType(HELP_INDEX);
		} else if(command.startsWith(UNDO_COMMAND)) {
			//commandObject.setType(UNDO_INDEX)
		} else if(command.startsWith(EDIT_COMMAND)) {
			//commandObject.setType(EDIT_INDEX);
			EditProcessor EP = new EditProcessor();
			EP.processEdit(command);
		} else if(command.startsWith(SAVE_COMMAND)) {
			//commandObject.setType(SAVE_INDEX);
			setCommandObjectToSave(command);
		} else if(command.startsWith(DELETE_COMMAND)) {
			//commandObject.setType(DELETE_INDEX);
			setCommandObjectToDelete(command);
		} else if(command.startsWith(ADD_COMMAND)) {
			//commandObject.setType(ADD_INDEX);
			AddProcessor AP = new AddProcessor();
			AP.addCommand(command);
			//add these 5 main attributes
			commandObject.setTask(AP.getTask());
			commandObject.setStartTime(AP.getStartTime());
			commandObject.setEndTime(AP.getEndTime());
			commandObject.setStartDate(AP.getStartDate());
			commandObject.setEndDate(AP.getEndDate());
		} else if(isSearch(command)) {
			//commandObject.setType(SEARCH_INDEX);
			SearchProcessor SP = new SearchProcessor();
			SP.p
			setCommandObjectToSearch(command);
		}
 	}
	
	/**
	 * method checks if the search keyword is present
	 */
 	private static boolean isSearch(String command) {
 		if(command.startsWith(SEARCH_COMMAND_1) || command.startsWith(SEARCH_COMMAND_2) 
 			|| command.startsWith(SEARCH_COMMAND_3) || command.startsWith(SEARCH_COMMAND_4) 
 			|| command.startsWith(SEARCH_COMMAND_5) ) {
 			return true;
 		} else {
 			return false;
 		}
 	}
 	
 	private static void setCommandObjectToDelete(String command) {
 		int index;
 		index = extractDeleteIndex(command);
 		commandObject.setIndex(index);
 	}
 	
 	/**
 	 * this method returns the number that is after the delete command as an integer
 	 */
 	private static int extractDeleteIndex(String command) {		
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		newString = command.substring(index);
 		return Integer.parseInt(newString);
 	}

 	private static void setCommandObjectToSave(String command) {
 		String newString;
 		int index = command.indexOf(" ") + 1;
 		newString = command.substring(index);
 		commandObject.setPath(newString);
 	}

 	
}
