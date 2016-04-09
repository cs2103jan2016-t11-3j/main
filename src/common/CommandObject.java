//@@author A0124636H

package common;

public class CommandObject {
	
	private int commandType;
	private TaskObject taskObj;
	private int index = -1;
	private int lastSearchedIndex = -1;
	
	public CommandObject() {
	}
	
	public CommandObject(int commandType, TaskObject taskObj) {
		this.commandType = commandType;
		this.taskObj = taskObj;
	}
	
	public CommandObject(int commandType, TaskObject taskObj, int index) {
		this.commandType = commandType;
		this.taskObj = taskObj;
		this.index = index;
	}
	
	public CommandObject(int commandType, TaskObject taskObj, int index, int lastSearchedIndex) {
		this.commandType = commandType;
		this.taskObj = taskObj;
		this.index = index;
		this.lastSearchedIndex = lastSearchedIndex;
	}
	
	public int getCommandType() {
		return commandType;
	}
	
	public TaskObject getTaskObject() {
		return taskObj;
	}
	
	public int getIndex() {
		return index;
	}

	public int getLastSearchedIndex() {
		return lastSearchedIndex;
	}

	public void setCommandType(int newCommandType) {
		this.commandType = newCommandType;
	}
	
	public void setTaskObject(TaskObject newTaskObject) {
		this.taskObj = newTaskObject;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setLastSearchedIndex(int lastSearchedIndex) {
		this.lastSearchedIndex = lastSearchedIndex;
	}

}
