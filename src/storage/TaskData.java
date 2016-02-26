package storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import logic.TaskObject;

public class TaskData {
    
    private static final String DELIMITER = ";";
    private static final String NEW_LINE = "\n";
    
    /**
     * 
     * @param taskList
     * @throws NoSuchFileException Default location invalid
     * @throws IOException Unable to edit existing file/Unable read default location
     */
    static void overWriteList(ArrayList<TaskObject> taskList) throws NoSuchFileException , IOException {
        try {
            deleteData();
        } catch (NoSuchFileException e) {
            // Nothing to delete
        } catch (IOException e) {
            // Unable to overwrite existing data
        } 
        String path = null;
        try {
            path = FilePath.getPath();
        } catch (NoSuchFileException e) {
            // Invalid default location
        } catch (IOException e) {
            // Unable to get location to write file
        }
        try {
            addTaskList(taskList, path);
        } catch (IOException e) {
            // Unable to overwrite existing data
        }
    }
    /**
     * 
     * @return
     * @throws NoSuchFileException Specified default file does not exist
     * @throws IOException Error reading from existing file
     */
    static ArrayList<String> readData() throws NoSuchFileException, IOException {
        ArrayList<String> taskDataList = new ArrayList<String>();  
        String filePath = FilePath.getPath();
        try {
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
        String line = null;
        while ((line = fileReader.readLine()) != null) {
            taskDataList.add(line);
        }
        fileReader.close();
        } catch (FileNotFoundException e) {
            return taskDataList;
        }
        return taskDataList;
    }
    
    protected static void writeList(ArrayList<TaskObject> taskList, String filePath) throws NoSuchFileException 
                                                                                            , IOException {
        if ( filePath == null ) {
            throw new NoSuchFileException(filePath);
        }
        addTaskList(taskList, filePath);
    }
    
    static ArrayList<TaskObject> parseData(ArrayList<String> taskDataList) {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        for (String taskData : taskDataList) {
            String[] taskAttributes = taskData.split(DELIMITER);
            TaskObject task = new TaskObject( taskAttributes[0], Integer.parseInt(taskAttributes[7]));
            task.setStartDate(Integer.parseInt(taskAttributes[1]));
            task.setEndDate(Integer.parseInt(taskAttributes[2]));
            task.setStartTime(Integer.parseInt(taskAttributes[3]));
            task.setEndTime(Integer.parseInt(taskAttributes[4]));
            task.setCategory(taskAttributes[5]);
            task.setStatus(taskAttributes[6]);
            taskList.add(task);
        }
        return taskList;
    }
    /**
     * Deletes the file containing Stored Information
     * @throws NoSuchFileException No existing file
     * @throws IOException Error deleting file
     */
    static void deleteData() throws NoSuchFileException , IOException{
        Path path = Paths.get(FilePath.getPath());
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            // System.err.format("%s: no such" + " file or directory%n", path);
        } catch (DirectoryNotEmptyException x) {
            // Not to be called on Directories
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    } 
    
    private static void addTaskList (ArrayList<TaskObject> taskList, String path) throws IOException {
        for (TaskObject task : taskList) {
            addTask(task, path);
        }
    }

    private static void addTask(TaskObject task, String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath , true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
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
        printWriter.print(NEW_LINE);
        printWriter.close();
    }
}
