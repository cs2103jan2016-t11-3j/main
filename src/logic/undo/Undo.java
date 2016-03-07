package logic.undo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.EmptyStackException;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

public class Undo extends UndoRedo {

	private static final int INDEX_ADD = 1;
	private static final int INDEX_EDIT = 3;
	private static final int INDEX_DELETE = 4;
	private static final String MESSAGE_UNDO = "%1$s undone.";
	private static final String MESSAGE_UNDO_ERROR = "Nothing to undo!";

	public Undo(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		try {
			CommandObject commandObj = undoList.pop();
			//redoList.push(commandObj);
			
			Logic secondaryLogic = new Logic(taskList, undoList, redoList);
			secondaryLogic.parseCommandObject(commandObj, true, false);
			
			output.add(String.format(MESSAGE_UNDO, getUndoneCommandType(commandObj)));
		} catch (EmptyStackException e) {
			System.out.println(MESSAGE_UNDO_ERROR);
		}
		
		return output;
	}
	
	protected String getUndoneCommandType(CommandObject commandObj) {
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