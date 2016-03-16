package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import common.AtfLogger;
import common.TaskObject;

public class TaskData {

    private static final String NEW_LINE = "\n";
    
    /**
     * Creates a file at the specified path containing details of the tasks to be stored.
     * If existing file at the specified path is present, it will be overwritten.
     * The taskObjects will written to the file in json format.
     * <p>
     * @param taskList An ArrayList containing all the taskObjects
     * @param filePath The file path to create the file
     * @throws IOException Error writing to specified path
     */
    protected static void writeTasks(ArrayList<TaskObject> taskList, String filePath) 
            throws IOException {
        if(filePath == null) {
            throw new InvalidPathException("Saving to invalid Path", filePath);
        }
        Logger logger = AtfLogger.getLogger();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(taskList);
        writer.write(json + NEW_LINE);
        writer.close();
        logger.info(String.format(Constants.LOG_SAVED, filePath));
    }
    
    /**
     * Reads the specified file and creates taskObjects from the task information stored in the file.
     * The task information needs to be stored in json format in the file.
     * <p>
     * @param filePath The path of the file containing the stored tasks information.
     * @return
     * @throws FileNotFoundException The specified file path does not exist
     * @throws IOException Error reading from existing file
     * @throws JsonSyntaxException File format not compatible with existing format
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
    
    /**
     * 
     * @param taskList
     * @param filePath
     * @throws IOException Fail to Delete
     */
    protected static void deleteTasks(ArrayList<TaskObject> taskList, String filePath) 
            throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
        
    }

}
