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
     */
    public abstract void writeList(ArrayList<TaskObject> taskList) throws IOException;
    
    /**
     * Returns all task data list stored in file as taskObjects.
     * <p>
     * @return taskDataList - Tasks read from stored file
     * @throws IOException - If unable to open file
     */
    public abstract ArrayList<TaskObject> load() throws IOException;
    
}
