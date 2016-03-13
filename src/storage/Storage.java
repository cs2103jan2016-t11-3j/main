package storage;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import common.TaskObject;

public interface Storage {

    /**
     * Writes tasks to storage. Overwrites existing tasks stored in storage.
     * <p>
     * @param taskList - The list of tasksObjects to be written
     * @return 
     * @throws IOException Error with saving file
     */
    public abstract void save(ArrayList<TaskObject> taskList) throws IOException;

    /**
     * Loads all saved tasks into storage from existing specified file.
     * <p>
     * @return List of TaskObjects
     * @throws IOException Error reading from Existing File
     */
    public abstract ArrayList<TaskObject> load() throws IOException;

    /**
     * Creates a copy of the file containing all stored task information at the specified directory.
     * <p>
     * The tasks stored in storage should first be updated using <code>load</code> or <code>save</code>
     * for the created copy to contain the most recent task information.
     * <p>
     * @param directory Path of directory to create the copy in
     * @param fileName Name of file to be created
     * @throws IOException Error Copying Existing Files
     * @throws InvalidPathException  The path specified cannot be used
     */
    public abstract void createCopy(String directory, String fileName) throws InvalidPathException, IOException;

    /**
     * <p>
     * @param directory
     * @throws IOException Error writing to specified directory
     * @throws InvalidPathException The specified directory cannot be used
     */
    public abstract void changeSaveLocation(String directory) throws InvalidPathException, IOException;

    

}
