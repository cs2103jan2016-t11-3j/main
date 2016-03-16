package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import common.TaskObject;

public class TaskData {

    private static final String DELIMITER = ";";
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
    protected static void writeTasks(ArrayList<TaskObject> taskList, String filePath) throws IOException {
        assert filePath!= null;
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(taskList);
        writer.write(json + "\n");
        writer.close();
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
    static ArrayList<TaskObject> readTasks(String filePath) throws FileNotFoundException , IOException , JsonSyntaxException {
        assert(filePath!= null);
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        BufferedReader fileReader = new BufferedReader (new FileReader(filePath));
        Type typeOfTaskList = new TypeToken<ArrayList<TaskObject>>(){}.getType();
        Gson gson = new Gson();
        taskList = gson.fromJson(fileReader, typeOfTaskList);
        fileReader.close();
        return taskList;
    }

    
    /**
     * Convert task data into task objects
     * @param taskDataList
     * @return
     */
    @SuppressWarnings("unused")
    private static ArrayList<TaskObject> parseData(ArrayList<String> taskDataList) {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        Type typeOfTask = new TypeToken<TaskObject>(){}.getType();
        Gson gson = new Gson();
        for (String taskData : taskDataList) {           
            TaskObject task = gson.fromJson(taskData, typeOfTask);
            taskList.add(task);
        }
        return taskList;
    }
    
    /**
     * 
     * @param task
     * @param filePath
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private static void writeTask(TaskObject task, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true));
        FileWriter fileWriter = new FileWriter("data.csv" , true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        printWriter.print(task.getTitle().replace(";", ","));
        printWriter.print(DELIMITER);
        printWriter.print(task.getStartDate());
        printWriter.print(DELIMITER);
        printWriter.print(task.getEndDate());
        printWriter.print(DELIMITER);
        printWriter.print(task.getStartTime());
        printWriter.print(DELIMITER);
        printWriter.print(task.getEndTime());
        printWriter.print(DELIMITER);
        printWriter.print(task.getCategory().replace(";", ","));
        printWriter.print(DELIMITER);
        printWriter.print(task.getStatus().replace(";", ","));
        printWriter.print(DELIMITER);
        printWriter.print(task.getTaskId());
        printWriter.print(DELIMITER);
        if(task.getStartDateTime() != null) {
            printWriter.print(task.getStartDateTime().format(formatter));
        }
        printWriter.print(DELIMITER);
        if(task.getEndDateTime() != null) {
            printWriter.print(task.getEndDateTime().format(formatter));
        }
        printWriter.print(DELIMITER);
        printWriter.print(NEW_LINE);
        printWriter.close();
    }

}
