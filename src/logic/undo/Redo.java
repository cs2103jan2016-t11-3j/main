package logic.undo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

public class Redo extends UndoRedo {
	
	private static final String MESSAGE_REDO = "%1$s redone.";
	private static final String MESSAGE_REDO_ERROR = "Nothing to redo!";
	private static final String MESSAGE_REDO_LIST_EMPTY = "Redo list is empty.";

	public Redo(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		if (redoList.isEmpty()) {
			output.add(MESSAGE_REDO_LIST_EMPTY);
		} else {
			try {
				CommandObject commandObj = redoList.pop();
				
				Logic secondaryLogic = new Logic(taskList, undoList, redoList);
				secondaryLogic.parseCommandObject(commandObj, false, true);
				
				output.add(String.format(MESSAGE_REDO, getRedoneCommandType(commandObj)));
			} catch (NoSuchElementException e) {
				output.add(MESSAGE_REDO_ERROR);
			}
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
			case INDEX_COMPLETE :
			case INDEX_INCOMPLETE :
			case INDEX_OVERDUE :
				return "Status change";
			default :
				return "";
		}
	}
}
