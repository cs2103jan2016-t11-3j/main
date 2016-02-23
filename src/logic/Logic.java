package logic;

import java.util.ArrayList;
import java.util.Stack;

public class Logic {
	
	private ArrayList<TaskObject> taskList;
	private Stack<CommandObject> undoList;

	private CommandObject commandObj;
	private TaskObject taskObj;
 	private ArrayList<String> output; // output is to be returned to UI after each command
	
	public Logic(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
	}
	
	public ArrayList<String> getOutput() {
		return output;
	}
	
	public void setOutput(ArrayList<String> newOutput) {
		this.output = newOutput;
	}
	
	// Calling Parser
	// ...
	// ...
	
	public void parseCommandObject() {
		int command = commandObj.getCommand();
		TaskObject taskObj = commandObj.getTaskObject();

		switch (command) {
			case 1 :
				addFunction();
				break;
			case 2 :
				
			case 3 :
				
			default:
		}
	}
	
	private void addFunction() {
		Add add = new Add(taskObj, taskList);
		setOutput(add.run());
	}
	
	private void displayFunction() {
		Display display = new Display(taskList);
		setOutput(display.run());
	}
	
	private void deleteFunction() {
		Delete delete = new Delete(taskObj, taskList);
		setOutput(delete.run());
	}
	
	private void searchFunction() {
		Search search = new Search(taskObj, taskList);
		setOutput(search.run());
	}
	
	private void exitFunction() {
		Exit exit = new Exit();
		setOutput(exit.run());
	}
	
	
}
