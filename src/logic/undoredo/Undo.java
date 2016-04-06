//@@author A0124636H

package logic.undoredo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.logging.*;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;

/**
 * The Undo class is a child class of the UndoRedo class.
 * The first CommandObject in the undo list ArrayDeque is "popped" and a secondary Logic class is created to 
 * process the operation.
 * 
 * @param taskList ArrayList containing all tasks
 * @param undoList Deque containing all undo tasks
 * @param redoList Deque containing all redo tasks
 * @author RuiBin
 */

public class Undo extends UndoRedo {

	private Logic secondaryLogic;
	
	public Undo(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		if (undoList.isEmpty()) {
			output.add(MESSAGE_UNDO_LIST_EMPTY);
		} else {
			try {
				CommandObject commandObj = undoList.pop();
				
				secondaryLogic = new Logic(taskList, undoList, redoList);
				secondaryLogic.parseCommandObject(commandObj, true, false);
				logger.log(Level.INFO, "Undo CommandObject processed in secondary Logic class");
				
				output.add(String.format(MESSAGE_UNDO, getUndoneCommandType(commandObj)));
			} catch (NoSuchElementException e) {
				logger.log(Level.WARNING, "Undo error");
				output.add(MESSAGE_UNDO_ERROR);
			}
		}
		
		return output;
	}
	
	private String getUndoneCommandType(CommandObject commandObj) {
		int commandType = commandObj.getCommandType();
		assert (commandType == INDEX_ADD || commandType == INDEX_DELETE || commandType == INDEX_EDIT ||
				commandType == INDEX_COMPLETE || commandType == INDEX_INCOMPLETE);
		
		switch (commandType) {
			case INDEX_ADD :
				return "Delete";
			case INDEX_DELETE :
				return "Add";
			case INDEX_EDIT :
				return "Edit";
			case INDEX_COMPLETE :
			case INDEX_INCOMPLETE :
				return "Status change";
			default :
				return "";
		}
	}
	
	public Logic getLogic() {
		return secondaryLogic;
	}
}