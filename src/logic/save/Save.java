package logic.save;

import logic.*;
import storage.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import common.TaskObject;

/**
 * Creates a Save object to perform two main types of operations. <br>
 * 1) Save to - Permanently changes the default location for the file to be saved. Also saves
 * the file there for the first time when this command is executed. <br>
 * 2) Save as - Saves a copy of the file at the specified location. Default location
 * will not be changed.
 * @param int saveCommand - Default value 0, which will not be recognised by the program.
 * Takes the value of 1 for "Save to" and value of 2 for "Save as".
 * @param boolean isSaved - Value determines whether file was saved successfully to the location
 * @author ChongYan
 *
 */
public class Save {

	private static final String MESSAGE_SAVE_TO = "Tasks have been, and will continue to be saved to %1s";
	private static final String MESSAGE_SAVE_AS = "Tasks have been saved to %1s";
	private static final String MESSAGE_SAVE_INVALID = "Save command is invalid";
	private static final String MESSAGE_SAVE_ERROR = "Error saving file to %1s";

	private boolean isSaved = false;
	private int saveCommand = 0;
	private String newFilePath = "";
	private ArrayList<String> output = new ArrayList<String>();
	private ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();

	public Save() {

	}

	/**
	 * Constructor for Save object.
	 * @param taskObj - Contains the details of execution in the title attribute. 
	 * Precondition: Title must be of the format "to -file directory-" or "as -file directory-"
	 * or else the constructor will not modify the value of saveCommand, which will lead
	 * to the abortion of the operation.
	 * @param taskList
	 */
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
		this.taskList = taskList;
	}

	/**
	 * Main method of the Save class. Makes use of saveCommand to determine which
	 * save operation to execute. Does not do anything and returns an error output
	 * if saveCommand == 0, and if the file location is invalid.
	 * @return
	 */
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
		try {
            storage.save(taskList);
        } catch (NoSuchFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		isSaved = true;
	}

	private void saveAs() {
		FileStorage storage = FileStorage.getInstance();
		storage.createCopy(newFilePath, "filecopy.txt");
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
