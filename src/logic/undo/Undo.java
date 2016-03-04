package logic.undo;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import logic.CommandObject;
import logic.Logic;
import logic.TaskObject;

public class Undo extends UndoRedo {

	private static final String MESSAGE_UNDO = "%1$s undone.";
	private static final String MESSAGE_UNDO_ERROR = "Nothing to undo!";

	public Undo(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList, Stack<CommandObject> redoList) {
		super(taskList, undoList, redoList);
	}
	
	public ArrayList<String> run() {
		try {
			CommandObject commandObj = undoList.pop();
			redoList.push(commandObj);
			
			Logic secondaryLogic = new Logic(taskList, undoList, redoList);
			secondaryLogic.parseCommandObject(commandObj, true);
			
			output.add(String.format(MESSAGE_UNDO, super.getUndoneCommandType(commandObj)));
		} catch (EmptyStackException e) {
			System.out.println(MESSAGE_UNDO_ERROR);
		}
		
		return output;
	}
	
}