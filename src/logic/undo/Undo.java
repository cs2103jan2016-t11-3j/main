package logic.undo;
import logic.*;
import logic.CommandObject;

import java.util.ArrayList;
import java.util.Stack;

public class Undo extends Logic {
	
	private static final String MESSAGE_UNDO = "%1$s undone";
	
	private Stack<CommandObject> undoList;
	private ArrayList<String> output = new ArrayList<String>();

	public Undo() {
		this.undoList = super.getUndoList();
	}
	
	public ArrayList<String> run() {
		CommandObject commandObj = undoList.pop();
		
		super.parseCommandObject(commandObj, true);
		output.add(String.format(MESSAGE_UNDO, getUndoneCommandType(commandObj)));
		
		return output;
	}
	
	private String getUndoneCommandType(CommandObject commandObj) {
		switch (commandObj.getCommandType()) {
			case INDEX_ADD :
				return "delete";
			case INDEX_DELETE :
				return "add";
			case INDEX_EDIT :
				return "edit";
			default :
				return "";
		}
	}

}
