//@@author A0080510X

package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import common.AtfLogger;
import common.TaskObject;


/**
 * TaskData contains methods for writing tasks onto disk and for loading tasks from disk.
 * Tasks stored on the disk will be represented in Json.
 * 
 * @@author A0080510X
 */
public class TaskData {

    private static final String NEW_LINE = "\n";
    
    /**
     * Creates a file at the specified path containing details of the tasks to be stored.
     * If existing file at the specified path is present, it will be overwritten.
     * The taskObjects will serialized and stored as Json representation in the file.
     * <p>
     * @param taskList An <code>ArrayList</code> containing all the task objects to be
     * stored into the file.
     * @param filePath The file path of the file to be created.
     * @throws IOException Error writing to specified path.
     */
    protected static void writeTasks(ArrayList<TaskObject> taskList, String filePath) 
            throws IOException {
        if(filePath == null) {
            throw new InvalidPathException("Saving to invalid Path", filePath);
        }
        Logger logger = AtfLogger.getLogger();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(taskList);
        writer.write(json + NEW_LINE);
        writer.close();
        logger.info(String.format(Constants.LOG_SAVED, filePath));
    }
    
    /**
     * Reads from the file at the specified filePath and creates taskObjects 
     * by deserializing the tasks stored in the file.
     * The specified file must contains tasks serialized in Json format.
     * <p>
     * @param filePath The path of the file containing the stored tasks information.
     * @return An <code>ArrayList</code> containing all the task objects that are 
     * read from the file. 
     * @throws FileNotFoundException The specified file path does not exist.
     * @throws IOException Error reading from existing file.
     * @throws JsonSyntaxException Error converting file contents to task objects. 
     * The file does not contain tasks that are correctly represented in Json.
     */
    static ArrayList<TaskObject> readTasks(String filePath) 
            throws FileNotFoundException, IOException, JsonSyntaxException {
        if(filePath == null) {
            throw new FileNotFoundException("No file to read from");
        }
        Logger logger = AtfLogger.getLogger();
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        BufferedReader fileReader = new BufferedReader (new FileReader(filePath));
        Type typeOfTaskList = new TypeToken<ArrayList<TaskObject>>(){}.getType();
        Gson gson = new Gson();
        taskList = gson.fromJson(fileReader, typeOfTaskList);
        fileReader.close();
        logger.info(String.format(Constants.LOG_LOADED, filePath));
        return taskList;
    }

}
