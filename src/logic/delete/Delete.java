package logic.delete;

import logic.*;
import storage.*;

import java.util.Stack;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

// Needs delete function for last added code
public class Delete {

	// Deletes by searching for the unique taskID
	private static String MESSAGE_DELETE = "Task deleted from TaskFinder: %1s";
	private static String MESSAGE_ERROR = "Error deleting task from TaskFinder";

	// This command object contains the index number of the line to be deleted
	private CommandObject commandObj;
	
	// This is the task which is actually removed from TaskFinder
	private TaskObject removedTask = new TaskObject();

	// Attributes that should be passed in when the delete object is first
	// constructed
	private ArrayList<TaskObject> taskList;
	private ArrayList<TaskObject> lastOutputTaskList;
	private ArrayList<String> output = new ArrayList<String>();
	private Stack<CommandObject> undoList = new Stack<CommandObject>();

	// Internal checkers to ensure that deletion has occurred
	private boolean hasDeletedInternal = false;
	private boolean hasDeletedExternal = false;

	// Actual task ID of the task requested to be deleted
	private int taskIdToDelete = -1;

	// Actual name of the task which is to be deleted
	private String taskName = "";

	// Constructors
	public Delete() {

	}
	
	public Delete(ArrayList<TaskObject> taskList, Stack<CommandObject> undoList) {
		this.taskList = taskList;
		this.undoList = undoList;
		this.commandObj = new CommandObject(Logic.INDEX_DELETE, new TaskObject(), -1);
	}

	public Delete(CommandObject commandObj, ArrayList<TaskObject> taskList, ArrayList<TaskObject> lastOutputTaskList) {
		this.commandObj = commandObj;
		this.taskList = taskList;
		this.lastOutputTaskList = lastOutputTaskList;
	}
	
	public ArrayList<String> run() {
		assert(!taskList.isEmpty());
		if(commandObj.getIndex() == -1) {
			runQuickDelete();
		} else {
			runNormalDelete();
		}
		return output;
	}
	
	private void runQuickDelete() {
		if(undoList.empty()) {
			createErrorOutput();
		}
		if(undoList.peek().getCommandType() == Logic.INDEX_DELETE) {
			// delete the last item in taskList as it shows that the item had just been added
			int index = taskList.size() - 1;
			taskName = taskList.get(index).getTitle();
			taskList.remove(index);
			if(taskList.size() == index) {
				// To check if taskList has shrunk by 1
				hasDeletedInternal = true;
			}
			hasDeletedExternal = deleteExternal();
			if(hasDeletedInternal && hasDeletedExternal) {
				createOutput();
			} else {
				createErrorOutput();
			}
		} else {
			createErrorOutput();
		}
	}

	private void runNormalDelete() {
		hasDeletedInternal = deleteInternal();
		if (hasDeletedInternal) {
			hasDeletedExternal = deleteExternal();
			if (hasDeletedExternal) {
				createOutput();
			}
		} else {
			createErrorOutput();
		}
	}

	private boolean deleteInternal() {
		obtainTaskId();
		if (taskIdToDelete != -1) {
			for (int i = 0; i < taskList.size(); i++) {
				if (taskList.get(i).getTaskId() == taskIdToDelete) {
					taskName = taskList.get(i).getTitle();
					removedTask = taskList.get(i);
					taskList.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	private void obtainTaskId() {
		int index = commandObj.getIndex();
		if (index > 0 && index <= taskList.size()) { 
			index--;
			taskIdToDelete = lastOutputTaskList.get(index).getTaskId();
		}
	}

	private boolean deleteExternal() {
		FileStorage storage = FileStorage.getInstance();
		try {
            storage.save(taskList);
        } catch (NoSuchFileException e) {
         // TODO Auto-generated catch block
            //Ask user to specify new location or use default location
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return true;
	}

	private void createOutput() {
		String text = String.format(MESSAGE_DELETE, taskName);
		output.add(text);
	}

	private void createErrorOutput() {
		output.add(MESSAGE_ERROR);
	}

	// GETTERS AND SETTERS
	public CommandObject getCommandObject() {
		return commandObj;
	}
	
	public TaskObject getRemovedTask() {
		return removedTask;
	}

	public ArrayList<String> getOutput() {
		return output;
	}

	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}

	public void setTaskList(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public void setCommandObject(CommandObject commandObj) {
		this.commandObj = commandObj;
	}

}
