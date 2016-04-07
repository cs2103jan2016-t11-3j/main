//@@author A0124636H

package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import common.AtfLogger;
import common.TaskObject;
import storage.FileStorage;

public class Exit {

	static Logger logger = AtfLogger.getLogger();
	
	private ArrayList<TaskObject> taskList;

	public Exit(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public void run() {
		saveToExternalFile();
		System.exit(0);
	}

	public void saveToExternalFile() {
		FileStorage storage = FileStorage.getInstance();
		try {
			storage.save(taskList);
			logger.info("saved tasks before exiting");
		} catch (IOException e) {
			logger.warning("did not save tasks properly");
		}
	}
}
