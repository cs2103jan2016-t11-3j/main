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

/**
 * This class serves to load tasks from another source file other than the existing, default source file.
 * There are two ways a load object could be run: <br>
 * 1. Loading from a user defined directory <br>
 * 2. Loading from a backup copy. This backup copy is saved internally within the program, and is saved in
 * parallel with the original copy that the user has access to.
 * 
 * @author ChongYan
 *
 */
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

	/**
	 * Main method within the Load class. Creates a FileStorage Object and accesses the storage component, and
	 * attempts to retrieve all tasks from a set location.
	 * 
	 * @return ArrayList<String> containing output to be displayed to the user. Output varies according to the
	 *         type of load command and also depends on whether it is successful.
	 */
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

	/**
	 * Loads a file using the FileStorage object. Method determines whether the command is to load from a
	 * user-defined location or from the backup, and proceeds to load the tasks.
	 * 
	 * @param storage
	 *            FileStorage object which is used to access all stored information
	 * @throws InvalidPathException
	 *             Thrown when the file path given is invalid
	 * @throws FileNotFoundException
	 *             Thrown when the file does not exist at the given location
	 * @throws JsonSyntaxException
	 *             Thrown when the file at the given location has an invalid syntax and cannot be read by the
	 *             library
	 * @throws IOException
	 *             General IO Exception in case of any unforeseen circumstances
	 */
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
