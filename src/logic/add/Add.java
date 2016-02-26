package logic.add;

import logic.*;
import storage.*;

import java.io.IOException;
import java.util.ArrayList;

public class Add {

	/*
	 * List of functions needed: - simple add to taskList - check if clash for
	 * events - return display message - write to storage file: create a new
	 * storage object -
	 */
	private final String MESSAGE_ADD = "Task added: ";
	private final String MESSAGE_FAIL = "Failed to add task";
	private final String MESSAGE_CLASH = "Task: %1s clashes with %2s";

	private TaskObject task;
	private boolean addedInternal = false;
	private boolean addedExternal = false;
	private boolean isClash = false;
	private ArrayList<TaskObject> taskList;
	private ArrayList<String> output = new ArrayList<String>();
	private ArrayList<String> clashedTasks = new ArrayList<String> ();

	public Add() {

	}

	public Add(TaskObject task, ArrayList<TaskObject> taskList) {
		this.task = task;
		this.taskList = taskList;
	}

	public ArrayList<String> run() {

		String taskType = task.getCategory();
		if (taskType.equals("event")) {
			isClash = checkIfClash();
			// check for clash only necessary if task is an event
		}
		addTask();
		createOutput();
		return output;
	}

	private boolean checkIfClash() {
		boolean hasClashes = false;
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getCategory().equals("event")) {
				// only check with events
				if (checkTimeClash(taskList.get(i))) {
					clashedTasks.add(taskList.get(i).getTitle());
					hasClashes = true;
				}
			}
		}
		return hasClashes;
	}

	private boolean checkTimeClash(TaskObject current) {
		int currentStartDate = current.getStartDate();
		int currentStartTime = current.getStartTime();
		int currentEndDate = current.getEndDate();
		int currentEndTime = current.getEndTime();
		int newStartDate = task.getStartDate();
		int newStartTime = task.getStartTime();
		int newEndDate = task.getEndDate();
		int newEndTime = task.getEndTime();

		// Checking with the start time of the new event, if new task's
		// start is in between existing task's start and end time, flag as clash
		if ((newStartDate > currentStartDate)
				|| ((newStartDate == currentStartDate) && (newStartTime >= currentStartTime))) {
			if ((newStartDate < currentEndDate)
					|| ((newStartDate == currentEndDate) && (newStartTime <= currentEndTime))) {
				return true;
			}
		}
		// Checking with the end time of the new event, if new task's
		// end is in between existing task's start and end time, flag as clash
		if ((newEndDate > currentStartDate) || ((newEndDate == currentStartDate) && (newEndTime >= currentStartTime))) {
			if ((newEndDate < currentEndDate) || ((newEndDate == currentEndDate) && (newEndTime <= currentEndTime))) {
				return true;
			}
		}
		// Checking with the start time of the current event in the taskList, if
		// the current task's start time is in between start and end time of
		// new task, flag as clash
		if ((currentStartDate > newStartDate)
				|| ((currentStartDate == newStartDate) && (currentStartTime >= newStartTime))) {
			if ((currentStartDate < newEndDate)
					|| ((currentStartDate == newStartDate) && (currentStartDate <= newEndTime))) {
				return true;
			}
		}
		// Checking with the end time of the current event in the taskList, if
		// the current task's start time is in between start and end time of
		// new task, flag as clash
		if ((currentEndDate > newStartDate) || ((currentEndDate == newStartDate) && (currentEndDate >= newStartTime))) {
			if ((currentEndDate < newEndDate) || ((currentEndDate == newEndDate) && (currentEndTime <= newEndTime))) {
				return true;
			}
		}

		return false;
	}

	private void addTask() {
		addInternal();
		addExternal();
	}

	private void addInternal() {
		int originalSize = taskList.size();
		int newSize = originalSize + 1;
		taskList.add(task);
		if (taskList.size() == newSize)
			addedInternal = true;
	}

	private void addExternal() {
		FileStorage storage = FileStorage.getInstance();
		int success = storage.save(taskList);
		if (success == 0) {
			addedExternal = true;
		}
	}

	private void createOutput() {
		if (addedInternal && addedExternal) {
			String title = task.getTitle();
			String text = MESSAGE_ADD.concat(title);
			output.add(text);
			if (isClash) {
				for(int i = 0; i < clashedTasks.size(); i++) {
					String clashMessage = createClashOutput(i);
					output.add(clashMessage);
				}
			}
		} else {
			output.add(MESSAGE_FAIL);
		}
	}

	private String createClashOutput(int i) {
		String text = "";
		text = String.format(MESSAGE_CLASH, task.getTitle(), clashedTasks.get(i));
		return text;
	}

	// GETTERS, SETTERS
	public ArrayList<String> getOutput() {
		return output;
	}

	public ArrayList<TaskObject> getTaskList() {
		return taskList;
	}

	public TaskObject getTask() {
		return task;
	}

	public void setOutput(ArrayList<String> output) {
		this.output = output;
	}

	public void setTaskList(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public void setTask(TaskObject task) {
		this.task = task;
	}
}
