package storage;

import java.io.IOException;
import java.util.ArrayList;

import logic.TaskObject;

public interface Storage {
    
    /**
     * Overwrites storage file with tasks from taskList.
     * <p>
     * @param taskList - The list of tasksObjects to be written
     * @throws IOException - If unable to edit file
     * @return 0 - If save is successful
     * @return 1 - If unable to overwrite data file
     */
    public abstract int save(ArrayList<TaskObject> taskList);
    
    /**
     * Loads all task data list stored in file into storage.
     * <p>
     * @return 0 - If successful
     * @return 1 - No file is found
     * @return 2 - Error reading existing file
     */
    public abstract int load();
    
    /**
     * Returns loaded tasks stored in storage
     * <p>
     * @return taskList - ArrayList of taskObjects stored in storage
     */
    public abstract ArrayList<TaskObject> getTaskList();
    
    /**
     * Creates a copy of the saved task data file at the specified filePath location
     * <p>
     * @param filePath to create the copy
     * @return 0 if successful
     * @return 1 if existing file does not exist
     * @return 2 if unable to read existing file
     * @return 3 if unable to create copy
     */
    //TODO: test
    public abstract int createCopy(String directory, String fileName);
    
}
