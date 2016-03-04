package logic.undo;
import logic.*;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Undo {

	public static final int INDEX_ADD = 1;
	public static final int INDEX_EDIT = 3;
	public static final int INDEX_DELETE = 4;
	private static final String MESSAGE_UNDO = "%1$s undone.";
	private static final String MESSAGE_UNDO_ERROR = "Nothing to undo!";
	
	private ArrayList<TaskObject> taskList;
	private Stack<CommandObject> undoList;
	private ArrayList<String> output = new ArrayList<String>();

	public Undo(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
	}
	
	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}
	
	public Stack<CommandObject> getUndoList() {
		return undoList;
	}
	
	public ArrayList<String> run() {
		try {
			CommandObject commandObj = undoList.pop();
		
			Logic secondaryLogic = new Logic(taskList, undoList);
			secondaryLogic.parseCommandObject(commandObj, true);
			// The primary logic class will then call getters to get the updated lists
			
			output.add(String.format(MESSAGE_UNDO, getUndoneCommandType(commandObj)));
		} catch (EmptyStackException e) {
			System.out.println(MESSAGE_UNDO_ERROR);
		}
		
		return output;
	}
	
	private String getUndoneCommandType(CommandObject commandObj) {
		switch (commandObj.getCommandType()) {
			case INDEX_ADD :
				return "Delete";
			case INDEX_DELETE :
				return "Add";
			case INDEX_EDIT :
				return "Edit";
			default :
				return "";
		}
	}

}
