package logic;

import java.util.ArrayList;
import java.util.Stack;

public class Logic {
	
	private CommandObject commandObj;
	private TaskObject taskObj;
	private ArrayList<TaskObject> taskList;
	private Stack<CommandObject> undoList;
 	
	public Logic(CommandObject commandObj, TaskObject taskObj, ArrayList<TaskObject> taskList) {
		this.commandObj = commandObj;
		this.taskObj = taskObj;
		this.taskList = taskList;
	}
	
	// ...
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
	
	public void addFunction() {
		Add add = new Add(taskObj, taskList);
		add.run();
	}
	
	public void displayFunction() {
		Display display = new Display(taskList);
		display.run();
	}
	
	public void deleteFunction() {
		Delete delete = new Delete(taskObj, taskList);
		delete.run();
	}
	
	public void searchFunction() {
		Search search = new Search(taskObj, taskList);
		search.run();
	}
	
	public void exitFunction() {
		Exit exit = new Exit();
		exit.run();
	}
	
}
