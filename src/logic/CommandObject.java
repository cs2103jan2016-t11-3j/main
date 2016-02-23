

public class CommandObject {
	
	private int command;
	private TaskObject taskObj;
	private int taskId;
	
	public CommandObject(int command, TaskObject taskObj, int taskId) {
		this.command = command;
		this.taskObj = taskObj;
		this.taskId = taskId;
	}
	
	public int getCommand() {
		return command;
	}
	
	public TaskObject getTask() {
		return taskObj;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setCommand(int newCommand) {
		this.command = newCommand;
	}
	
	public void setTask(TaskObject newTask) {
		this.taskObj = newTask;
	}
	
	public void setTaskId(int newTaskId) {
		this.taskId = newTaskId;
	}
}
