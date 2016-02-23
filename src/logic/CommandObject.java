
public class CommandObject {
	
	private int command;
	private TaskObject taskObj;
	
	public CommandObject(int command, TaskObject taskObj) {
		this.command = command;
		this.taskObj = taskObj;
	}
	
	public int getCommand() {
		return command;
	}
	
	public TaskObject getTaskObject() {
		return taskObj;
	}
	
	public void setCommand(int newCommand) {
		this.command = newCommand;
	}
	
	public void setTask(TaskObject newTask) {
		this.taskObj = newTask;
	}

}
