package test.storage;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import logic.TaskObject;
import storage.FileStorage;
import storage.Storage;

public class LoadTest {

    static String TEST_FILE = "data.csv";
    static String DELIMITER = ";";
    static String NEW_LINE = "\n";
    private static final String SAVE_FILE_NAME = "saveInfo.txt";
    ArrayList<TaskObject> loadedTasks = new ArrayList<TaskObject>();
    Storage storageTest = FileStorage.getInstance();
    
    @Test
    public void test() {

        TaskObject task1 = new TaskObject("task1", 1);
        TaskObject task2 = new TaskObject("task2", 2);
      
        try {
            FileWriter fileWriter = new FileWriter(TEST_FILE, false);
            fileWriter.close();
            fileWriter = new FileWriter(TEST_FILE, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print("task1;0;0;0;0;;;1;"); 
            printWriter.print(NEW_LINE);
            printWriter.print("task2;0;0;0;0;;;2;");
            printWriter.print(NEW_LINE);
            fileWriter.close();
            storageTest.load();
            loadedTasks = storageTest.getTaskList();
            Files.delete(Paths.get(TEST_FILE));
        }
        catch (IOException e){
        }
        compareTasks("task1", task1 , loadedTasks.get(0));
        compareTasks("task2", task2 , loadedTasks.get(1));
        assertEquals("Length", 2, loadedTasks.size());
    }
    
    @Test
    public void testNoFile() {
        int status = storageTest.load();
        loadedTasks = storageTest.getTaskList();
        assertEquals("Invalid Load" , 0 , status );
        assertEquals("Length",  0 , loadedTasks.size());
    }
    
    @Test
    public void testInvalidDefaultPath() throws IOException {
        FileWriter fileWriter = new FileWriter(SAVE_FILE_NAME , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("fail");
        printWriter.close();
        assertEquals("Invalid Load" , 1 , storageTest.load() );
        Files.delete(Paths.get(SAVE_FILE_NAME));
        
    }
    /*
    @Test
    public void testFailToRead() throws IOException {
        TaskGenerator dummyTask = new TaskGenerator();
        TaskObject task1 = dummyTask.getTask();
        FileWriter fileWriter = new FileWriter(TEST_FILE , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("existing");
        int status = storageTest.load();
        printWriter.close();
        assertEquals("Invalid Load" , 1 , status );
        compareTasks("task1", task1 , loadedTasks.get(0));
        assertEquals("Length",  1 , loadedTasks.size());
        Files.delete(Paths.get(TEST_FILE));
        Files.delete(Paths.get(SAVE_FILE_NAME));
    }
    */


    private void compareTasks(String testDescription, TaskObject task1, TaskObject task2) {
        assertEquals(testDescription + " title", task1.getTitle(), task2.getTitle());
        assertEquals(testDescription + " start date", task1.getStartDate(), task2.getStartDate());
        assertEquals(testDescription + " end date", task1.getEndDate(), task2.getEndDate());
        assertEquals(testDescription + " start time", task1.getStartTime(), task2.getStartTime());
        assertEquals(testDescription + " end time", task1.getEndTime(), task2.getEndTime());
        assertEquals(testDescription + " category", task1.getCategory(), task2.getCategory());
        assertEquals(testDescription + " Status", task1.getStatus(), task2.getStatus());
        assertEquals(testDescription + " taskId", task1.getTaskId(), task2.getTaskId());
    }

}
