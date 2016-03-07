package logic.undo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.EmptyStackException;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

public class Redo extends UndoRedo {
	
	private static final int INDEX_ADD = 1;
	private static final int INDEX_EDIT = 3;
	private static final int INDEX_DELETE = 4;
	private static final String MESSAGE_REDO = "%1$s redone.";
	private static final String MESSAGE_REDO_ERROR = "Nothing to redo!";

	public Redo(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		try {
			CommandObject commandObj = redoList.pop();
			//undoList.push(commandObj);
			
			Logic secondaryLogic = new Logic(taskList, undoList, redoList);
			secondaryLogic.parseCommandObject(commandObj, false, true);
			
			output.add(String.format(MESSAGE_REDO, getRedoneCommandType(commandObj)));
		} catch (EmptyStackException e) {
			System.out.println(MESSAGE_REDO_ERROR);
		}
		
		return output;
	}
	
	protected String getRedoneCommandType(CommandObject commandObj) {
		switch (commandObj.getCommandType()) {
			case INDEX_ADD :
				return "Add";
			case INDEX_DELETE :
				return "Delete";
			case INDEX_EDIT :
				return "Edit";
			default :
				return "";
		}
	}
}
