package logic.undoredo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

public class Redo extends UndoRedo {
	
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
	
	private String getRedoneCommandType(CommandObject commandObj) {
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
