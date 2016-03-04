package logic.undo;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import logic.CommandObject;
import logic.Logic;
import logic.TaskObject;

public class Redo extends UndoRedo {
	
	private static final String MESSAGE_REDO = "%1$s redone.";
	private static final String MESSAGE_REDO_ERROR = "Nothing to redo!";

	public Redo(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList, Stack<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		try {
			CommandObject commandObj = redoList.pop();
			undoList.push(commandObj);
			
			Logic secondaryLogic = new Logic(taskList, undoList, redoList);
			secondaryLogic.parseCommandObject(commandObj, true);
			
			output.add(String.format(MESSAGE_REDO, super.getUndoneCommandType(commandObj)));
		} catch (EmptyStackException e) {
			System.out.println(MESSAGE_REDO_ERROR);
		}
		
		return output;
	}
}
