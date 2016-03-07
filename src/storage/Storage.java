package storage;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import common.TaskObject;

public interface Storage {

    /**
     * Writes tasks to storage. Overwrites existing tasks stored in storage.
     * <p>
     * @param taskList - The list of tasksObjects to be written
     * @return 
     * @throws IOException writing to default save location/Unable to locate default location
     * @throws NoSuchFileException If existing default location is invalid
     */
    public abstract void save(ArrayList<TaskObject> taskList) throws NoSuchFileException, IOException;

    /**
     * Loads all saved tasks into storage from existing specified file.
     * <p>
     * @return List of TaskObjects
     * @throws IOException Error reading from Existing File
     * @throws NoSuchFileException Current save location cannot be accessed/invalid
     */
    public abstract ArrayList<TaskObject> load() throws NoSuchFileException, IOException;

    /**
     * Returns loaded tasks stored in storage
     * <p>
     * @return taskList - ArrayList of taskObjects stored in storage
     */
    public abstract ArrayList<TaskObject> getTaskList();

    /**
     * Creates a copy of the file containing all stored task information at the specified directory.
     * <p>
     * The tasks stored in storage should first be updated using <code>load</code> or <code>save</code>
     * for the created copy to contain the most recent task information.
     * <p>
     * @param directory Path of directory to create the copy in
     * @param fileName Name of file to be created
     * @return Status
     * <li> 0 - If successful
     * <li> 1 - If specified directory is invalid
     * <li> 2 - if unable to write specified file
     * <li> 3 - If no tasks to write
     */
    public abstract int createCopy(String directory, String fileName);

    /**
     * <p>
     * @param directory
     * @return status
     * <li> 0 - If successful
     * <li> 1 - If unable to create copy at new location
     * <li> 2 - If no existing file to copy
     * <li> 3 - Cannot delete existing file
     * <li> 4 - Specified directory is invalid
     * <li> 5 - Error writing to specified directory
     */
    public abstract int changeSaveLocation(String directory);

}
