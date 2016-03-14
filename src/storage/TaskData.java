package storage;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import common.TaskObject;

public class TaskData {

    private static final String DELIMITER = ";";
    private static final String NEW_LINE = "\n";

    

    /**
     * Writes given tasks into specified path
     * <p>
     * @param taskList
     * @param filePath
     * @throws NoSuchFileException Invalid specified path
     * @throws IOException Error writing to specified path
     */
    protected static void writeList(ArrayList<TaskObject> taskList, String filePath) throws IOException {
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", false));
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(taskList);
        writer.write(json + "\n");
        writer.close();
        // for (TaskObject task : taskList) {
        //     writeTask(task, filePath);
        // }
    }

    
    /**
     * Retrieves the tasks stored from the specified filePath.
     * <p>
     * @param filePath Location to read the stored data from
     * @return ArrayList of TaskObjects stored in disk
     * @throws IOException 
     */
//    static ArrayList<TaskObject> getTasks(String filePath) throws IOException {
//        ArrayList<String> taskDataList = readData(filePath);
//        ArrayList<TaskObject> taskList = parseData(taskDataList);
 //       return taskList;
   // }
    

    /**
     * Deletes the file containing Stored Information
     * @param filePath 
     * @throws NoSuchFileException No existing file
     * @throws IOException Error deleting file
     */
    static void deleteData(String filePath) throws NoSuchFileException , IOException{
        Path path = Paths.get(filePath);
        Files.delete(path);
    }
    
    /**
     *
     * @param filePath 
     * @return
     * @throws IOException Error reading from existing file
     */
    static ArrayList<TaskObject> readTasks(String filePath) throws IOException {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        JsonReader jsonReader = new JsonReader(new FileReader(filePath));
        Type typeOfTaskList = new TypeToken<ArrayList<TaskObject>>(){}.getType();
        Gson gson = new Gson();
        taskList = gson.fromJson(jsonReader, typeOfTaskList);
      //  String line = null;
       // while ((line = fileReader.readLine()) != null) {
      //      taskDataList.add(line);
      //  }
      //  fileReader.close();
        return taskList;
    }

    /**
     * UNUSED
     */
    
    /**
     * Convert task data into task objects
     * @param taskDataList
     * @return
     */
    private static ArrayList<TaskObject> parseData(ArrayList<String> taskDataList) {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        Type typeOfTask = new TypeToken<TaskObject>(){}.getType();
        Gson gson = new Gson();
        for (String taskData : taskDataList) {
            
            
            TaskObject task = gson.fromJson(taskData, typeOfTask);
            taskList.add(task);
        //    String[] taskAttributes = taskData.split(DELIMITER, -1);
         //   TaskObject task = new StorageTask(taskAttributes);
          //  taskList.add(task);
        }
        return taskList;
    }
    
    /**
     * 
     * @param task
     * @param filePath
     * @throws IOException
     */
    private static void writeTask(TaskObject task, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt", true));
        Gson gson = new Gson();
        String json = gson.toJson(task);
        writer.write(json + "\n");
        writer.close();
        
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
