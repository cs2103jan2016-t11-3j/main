//@@author A0130622X

package logic.sort;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import common.AtfLogger;
import common.TaskObject;
import storage.FileStorage;

public class Sort {
	
	private static Logger logger = AtfLogger.getLogger();

	private ArrayList<TaskObject> taskList;
	
	public Sort(ArrayList<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	public ArrayList<TaskObject> run() {
		Comparator<TaskObject> dateComparator = new Comparator<TaskObject>() {
			@Override
			public int compare(final TaskObject o1, final TaskObject o2) {
				if (!o1.getStatus().equals(o2.getStatus())) {
					return o2.getStatus().compareTo(o1.getStatus());
				} else if (!o1.getStartDateTime().equals(o2.getStartDateTime())) {
					return o1.getStartDateTime().compareTo(o2.getStartDateTime());
				} else if (!o1.getEndDateTime().equals(o2.getEndDateTime())) {
					return o1.getEndDateTime().compareTo(o2.getEndDateTime());
				} else {	
					return o1.getTitle().compareTo(o2.getTitle());
				}
			}
		};
		
		Collections.sort(taskList, dateComparator);
		saveExternal();
		logger.info("sorted and saved list");
		return taskList;
	}
	
	private void saveExternal() {
		try {
		FileStorage storage = FileStorage.getInstance();
		storage.save(taskList);
		} catch (NoSuchFileException e) {
			e.printStackTrace();
			logger.warning("unable to save sorted list to external file");
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("unable to save sorted list to external file");
		}
	}
}
