package storage;

import java.io.FileNotFoundException;
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
     * Returns all task data list stored in file as taskObjects.
     * <p>
     * @return taskDataList - Tasks read from stored file
     * @throws IOException - If unable to open file/file does not exist
     */
    public abstract ArrayList<TaskObject> load() throws IOException;

    /**
     * Creates a copy of the saved task data file at the specified filePath location
     * 
     * @param filePath to create the copy
     * @return 0 if successful
     * @return 1 if existing file does not exist
     * @return 2 if unable to read existing file
     * @return 3 if unable to create copy
     */
    //TODO: test
    public abstract int createCopy(String filePath);
    
}
