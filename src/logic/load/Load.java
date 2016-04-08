//@@author A0124052X
package logic.load;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.JsonSyntaxException;

import common.AtfLogger;
import common.TaskObject;
import storage.FileStorage;
import static logic.constants.Strings.*;
import static logic.constants.Index.*;

public class Load {
	
	static Logger logger = AtfLogger.getLogger();

	private TaskObject task;
	private int loadCommand = -1;
	private String filePath = "";
	private ArrayList<String> output = new ArrayList<String>();
	private ArrayList<TaskObject> loadedTaskList = new ArrayList<TaskObject>();

	public Load(TaskObject task) {
		this.task = task;
		processLoadCommand();
	}

	private void processLoadCommand() {
		String command = task.getTitle();
		if (command.startsWith(KEYWORD_FROM)) {
			loadCommand = LOAD_FROM;
			obtainFilePath(command);
		} else {
			if (command.startsWith(KEYWORD_BACKUP)) {
				loadCommand = LOAD_BACKUP;
			} else {
				logger.warning("Load Command is invalid");
			}
		}
	}

	private void obtainFilePath(String command) {
		filePath = command.substring(STARTING_INDEX);
	}

	public ArrayList<String> run() {
		try {
			FileStorage storage = FileStorage.getInstance();
			loadFile(storage);
			logger.info("obtained task list from alternative storage");
		} catch (InvalidPathException e) {
			logger.warning("invalid file path provided");
			createErrorOutput(MESSAGE_LOAD_EXCEPTION_IFP);
		} catch (FileNotFoundException e) {
			logger.warning("file cannot be found");
			createErrorOutput(MESSAGE_LOAD_EXCEPTION_FNF);
		} catch (JsonSyntaxException e) {
			logger.warning("external file format cannot be read");
			createErrorOutput(MESSAGE_LOAD_EXCEPTION_JSON);
		} catch (IOException e) {
			logger.warning("general IO exception");
			createErrorOutput(MESSAGE_LOAD_EXCEPTION_IO);
		}
		return output;
	}

	private void loadFile(FileStorage storage)
			throws InvalidPathException, FileNotFoundException, JsonSyntaxException, IOException {
		if (loadCommand == LOAD_FROM) {
			loadedTaskList = storage.load(filePath);
			createOutput();
		} else {
			if (loadCommand == LOAD_BACKUP) {
				loadedTaskList = storage.loadBackup();
				createOutput();
			} else {
				IOException e = new IOException("Invalid command");
				throw e;
			}
		}
	}
	
	private void createOutput() {
		if (loadCommand == LOAD_FROM) {
			System.out.println(filePath);
			output.add(String.format(MESSAGE_LOAD_SUCCESS, "\n" + filePath));
		} else {
			if (loadCommand == LOAD_BACKUP) {
				output.add(String.format(MESSAGE_LOAD_SUCCESS, KEYWORD_BACKUP));
			}
		}
	}
	
	private void createErrorOutput(String message) {
		output.clear();
		output.add(message);
	}

	// =============================================================================

	public ArrayList<TaskObject> getLoadedTaskList() {
		return loadedTaskList;
	}

	public ArrayList<String> getOutput() {
		return output;
	}
}
