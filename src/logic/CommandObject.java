package logic;

public class CommandObject {
	
	private int commandType;
	private TaskObject taskObj;
	private int index;
	
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
	
	public int getCommandType() {
		return commandType;
	}
	
	public TaskObject getTaskObject() {
		return taskObj;
	}
	
	public void setCommandType(int newCommandType) {
		this.commandType = newCommandType;
	}
	
	public void setTaskObject(TaskObject newTaskObject) {
		this.taskObj = newTaskObject;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
