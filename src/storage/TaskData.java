package storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import logic.TaskObject;

public class TaskData {
    
    private static final String DELIMITER = ";";
    private static final String NEW_LINE = "\n";
    
    static void clearFile() throws IOException{
        String filePath = FilePath.getPath();
        FileWriter fileWriter = new FileWriter(filePath, false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.close();
    }

    static void addTaskList (ArrayList<TaskObject> taskList) throws IOException {
        for (TaskObject task : taskList) {
            addTask(task);
        }
    }

    static void addTask (TaskObject task) throws IOException{
        String filePath = FilePath.getPath();
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

    static ArrayList<String> readData() throws FileNotFoundException , IOException {
        ArrayList<String> taskDataList = new ArrayList<String>();  
        String filePath = FilePath.getPath();
        BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
        String line = null;
        while ((line = fileReader.readLine()) != null) {
            taskDataList.add(line);
        }
        fileReader.close();
        return taskDataList;
    }


}
