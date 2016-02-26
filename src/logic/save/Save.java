package logic.save;

import logic.*;
import storage.*;
import java.util.ArrayList;

public class Save {

	private static final String MESSAGE_SAVE_TO = "Tasks have been, and will continue to be saved to %1s";
	private static final String MESSAGE_SAVE_AS = "Tasks have been saved to %1s";
	private static final String MESSAGE_SAVE_INVALID = "Save command is invalid";
	private static final String MESSAGE_SAVE_ERROR = "Error saving file to %1s";

	private boolean isSaved = false;
	private int saveCommand = 0;
	private String filePath = "";
	private String newFilePath = "";
	private ArrayList<String> output = new ArrayList<String>();
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();

	public Save() {

	}

	public Save(TaskObject taskObj, ArrayList<TaskObject> taskList) {
		String line = taskObj.getTitle();
		String[] cmd = new String[2];
		cmd = line.split(" ", 2);
		if (cmd[0].equals("to")) {
			saveCommand = 1;
		} else {
			if (cmd[0].equals("as")) {
				saveCommand = 2;
			}
		}
		newFilePath = cmd[1];
		filePath = FilePath.getPath();
		this.taskList = taskList;
	}

	public ArrayList<String> run() {
		if (saveCommand == 1) {
			saveTo();
		} else {
			if (saveCommand == 2) {
				saveAs();
			}
		}
		createOutput();
		return output;
	}

	private void saveTo() {
		FileStorage storage = FileStorage.getInstance();
		storage.changeSaveLocation(newFilePath);
		storage.save(taskList);
		isSaved = true;
	}

	private void saveAs() {
		FileStorage storage = FileStorage.getInstance();
		storage.changeSaveLocation(newFilePath);
		storage.save(taskList);
		storage.changeSaveLocation(filePath);
		isSaved = true;
	}

	private void createOutput() {
		String text = "";
		if (isSaved) {
			if (saveCommand == 1) {
				text = String.format(MESSAGE_SAVE_TO, newFilePath);
				output.add(text);
			} else {
				if (saveCommand == 2) {
					text = String.format(MESSAGE_SAVE_AS, newFilePath);
					output.add(text);
				}
			}
		} else {
			if (saveCommand == 0) {
				text = String.format(MESSAGE_SAVE_INVALID);
				output.add(text);
			} else {
				text = String.format(MESSAGE_SAVE_ERROR, newFilePath);
				output.add(text);
			}
		}
	}
}
