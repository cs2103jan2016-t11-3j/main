package logic.undo;

import java.util.ArrayList;
import java.util.Stack;

import common.CommandObject;
import common.TaskObject;

public class UndoRedo {

	private static final int INDEX_ADD = 1;
	private static final int INDEX_EDIT = 3;
	private static final int INDEX_DELETE = 4;
	private static final int INDEX_UNDO = 5;
	private static final int INDEX_REDO = 6;
	
	protected ArrayList<TaskObject> taskList;
	protected Stack<CommandObject> undoList;
	protected Stack<CommandObject> redoList;
	protected ArrayList<String> output = new ArrayList<String>();

	public UndoRedo(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList, Stack<CommandObject> redoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.redoList = redoList;
	}
	
	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}
	
	public Stack<CommandObject> getUndoList() {
		return undoList;
	}
	
	public Stack<CommandObject> getRedoList() {
		return redoList;
	}
	
	public ArrayList<String> run(int command) {

		if (command == INDEX_UNDO) {
			Undo undo = new Undo(taskList, undoList, redoList);
			output = undo.run();
		} else if (command == INDEX_REDO) {
			Redo redo = new Redo(taskList, undoList, redoList);
			output = redo.run();
		}
		
		return output;
	}
	
}
