package storage;

import java.io.IOException;
import java.util.ArrayList;

import logic.TaskObject;

public interface Storage {
    
    
    /**
     * writeList
     * 
     * Overwrites storage file with tasks from taskList
     * 
     * @param taskList list of Tasks to be written 
     * @throws IOException
     */
    public abstract void writeList(ArrayList<TaskObject> taskList) throws IOException;
    
    /**
     * load
     * 
     * Returns all task data list stored in file as taskObjects
     * 
     * @return taskDataList list of taskObjects
     * @throws IOException
     */
    public abstract ArrayList<TaskObject> load() throws IOException;
    
}
