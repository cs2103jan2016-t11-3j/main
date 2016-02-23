package storage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import logic.TaskObject;

public class StorageFile {
    
    private static final String DELIMITER = ";";
    private static final String NEW_LINE = "\n";
    private static PrintWriter printWriter;
    
    //to change
    public static String FILE_NAME = "data.csv";
    
    /**
     * WriteList
     * 
     * Overwrites storage file with tasks from taskList
     * 
     * @param taskList 
     * @return status
     */
    public static int WriteList(ArrayList<TaskObject> taskList) {
        
        clearFile();
        for (TaskObject task : taskList) {
            writeToFile(task);
        }
        return 0;
    
    }
    
    
    
    
    
    
   






    // HELPER
    
    private static void writeToFile(TaskObject task) {
        openWriter(true);
        printWriter.print(task.getTitle());
        printWriter.print(DELIMITER);
        printWriter.print(task.getStartDate());
        printWriter.print(DELIMITER);
        printWriter.print(task.getEndDate());
        printWriter.print(DELIMITER);
        printWriter.print(task.getStartTime());
        printWriter.print(DELIMITER);
        printWriter.print(task.getEndTime());
        printWriter.print(DELIMITER);
        printWriter.print(task.getCategory());
        printWriter.print(DELIMITER);
        printWriter.print(task.getStatus());
        printWriter.print(DELIMITER);
        printWriter.print(task.getTaskId());
        printWriter.print(DELIMITER);
        printWriter.print(NEW_LINE);
        closeWriter();
    }

    private static void clearFile() {
        openWriter(false);
        closeWriter();
    }
    
    private static void openWriter(boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, append);
            printWriter = new PrintWriter(fileWriter);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeWriter (){
        printWriter.close();
    }
    
}
