package logic.undo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

public class Undo extends UndoRedo {

	private static final String MESSAGE_UNDO = "%1$s undone.";
	private static final String MESSAGE_UNDO_ERROR = "Nothing to undo!";
	private static final String MESSAGE_UNDO_LIST_EMPTY = "Undo list is empty.";
	
	public Undo(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		if (undoList.isEmpty()) {
			output.add(MESSAGE_UNDO_LIST_EMPTY);
		} else {
			try {
				CommandObject commandObj = undoList.pop();
				
				Logic secondaryLogic = new Logic(taskList, undoList, redoList);
				secondaryLogic.parseCommandObject(commandObj, true, false);
				
				output.add(String.format(MESSAGE_UNDO, getUndoneCommandType(commandObj)));
			} catch (NoSuchElementException e) {
				output.add(MESSAGE_UNDO_ERROR);
			}
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
			case INDEX_DONE :
			case INDEX_OVERDUE :
			case INDEX_INCOMPLETE :
				return "Status change";
			default :
				return "";
		}
	}
	
}