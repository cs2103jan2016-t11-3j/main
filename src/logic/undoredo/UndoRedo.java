package logic.undoredo;

import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.*;

import common.CommandObject;
import common.TaskObject;

import static logic.constants.Index.*;

/**
 * The UndoRedo class is a parent class of the Undo and Redo classes.
 * The run method reads in the command type and then calls the corresponding child class.
 * The list of undo/redo operations are:
 * 1. Add <-> Delete
 * 2. Edit <-> Edit
 * 3. Incomplete <-> Done <-> Overdue
 * 
 * @param taskList ArrayList containing all tasks
 * @param undoList Deque containing all undo tasks
 * @param redoList Deque containing all redo tasks
 * @author Ruibin
 */

public class UndoRedo {

	protected static final Logger LOGGER = Logger.getLogger(UndoRedo.class.getName());
	
	private Undo undo;
	private Redo redo;
	
	protected ArrayList<TaskObject> taskList;
	protected Deque<CommandObject> undoList;
	protected Deque<CommandObject> redoList;
	protected ArrayList<String> output = new ArrayList<String>();

	public UndoRedo(ArrayList<TaskObject> taskList, Deque<CommandObject> undoList, Deque<CommandObject> redoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.redoList = redoList;
	}
	
	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}
	
	public Deque<CommandObject> getUndoList() {
		return undoList;
	}
	
	public Deque<CommandObject> getRedoList() {
		return redoList;
	}
	
	public ArrayList<String> run(int commandType) {
		assert (commandType == INDEX_UNDO || commandType == INDEX_REDO);

		if (commandType == INDEX_UNDO) {
			undo = new Undo(taskList, undoList, redoList);
			output = undo.run();
		} else if (commandType == INDEX_REDO) {
			redo = new Redo(taskList, undoList, redoList);
			output = redo.run();
		}
		
		return output;
	}
	
	public Undo getUndo() {
		return undo;
	}
	
	public Redo getRedo() {
		return redo;
	}
}
