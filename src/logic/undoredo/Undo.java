package logic.undoredo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Undo extends UndoRedo {

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
	
	private String getUndoneCommandType(CommandObject commandObj) {
		switch (commandObj.getCommandType()) {
			case INDEX_ADD :
				return "Delete";
			case INDEX_DELETE :
				return "Add";
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