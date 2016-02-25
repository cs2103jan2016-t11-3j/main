package logic;

public class CommandObject {
	
	private int commandType;
	private TaskObject taskObj;
	
	public CommandObject() {
	}
	
	public CommandObject(int commandType, TaskObject taskObj) {
		this.commandType = commandType;
		this.taskObj = taskObj;
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

}
