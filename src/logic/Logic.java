package logic;
import parser.Parser;
import logic.display.Display;

import java.util.ArrayList;
import java.util.Stack;

public class Logic {
	
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command";
	
	private ArrayList<TaskObject> taskList;
	private Stack<CommandObject> undoList;

	// These 3 values will get repeatedly updated by UI for each input
	private String userInput;
	private CommandObject commandObj;
	private TaskObject taskObj;
	
 	private ArrayList<String> output; // Output is to be returned to UI after each command
	
	public Logic(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public void setUserInput(String newUserInput) {
		this.userInput = newUserInput;
	}
	
	public void setOutput(ArrayList<String> newOutput) {
		this.output = newOutput;
	}
	
	
	// Calling Parser
	Parser parser = new Parser(userInput);
	commandObj = parser.run();
	
	public void parseCommandObject() {
		int command = commandObj.getCommandType();
		taskObj = commandObj.getTaskObject();

		switch (command) {
			case 1 :
				addFunction();
				break;
			case 2 :
				checkDisplayOrSearch();
				break;
			case 3 :
				editFunction();
				break;
			case 4 :
				deleteFunction();
				break;
			case 5 :
				undoFunction();
				break;
			case 6 :
				saveFunction();
				break;
			case 7 :
				exitFunction();
				break;
			case 8 :
				helpFunction();
				break;
			default:
				printInvalidCommandMessage();
				break;
		}
	}
	
	private void addFunction() {
		Add add = new Add(taskObj, taskList);
		setOutput(add.run());
	}	
	
	/* This method checks for the presence of a search keyword in TaskObject.
	 * If there is a keyword, search function will be called.
	 * If there is no keyword, display function will be called.
	 */
	private void checkDisplayOrSearch() {
		if (taskObj.isSearchKeywordPresent())
			searchFunction();
		else
			displayFunction();
	}
	
	// Outputs the entire task list
	private void displayFunction() {
		Display display = new Display(taskList);
		setOutput(display.run());
	}
	
	// Outputs only the tasks that match the search keyword
	private void searchFunction() {
		Search search = new Search(taskObj, taskList);
		setOutput(search.run());
	}
	
	private void editFunction() {
		
	}
	
	private void deleteFunction() {
		Delete delete = new Delete(taskObj, taskList);
		setOutput(delete.run());
	}
	
	private void undoFunction() {
		
	}
	
	private void saveFunction() {
		
	}
	
	private void exitFunction() {
		Exit exit = new Exit();
		exit.run();
	}
	
	private void helpFunction() {
		
	}
	
	private void printInvalidCommandMessage() {
		output.add(MESSAGE_INVALID_COMMAND);
	}
	
}
