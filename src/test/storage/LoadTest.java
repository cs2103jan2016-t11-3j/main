package test.storage;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import common.TaskObject;
import storage.FileStorage;
import storage.IStorage;
import test.AssertHelper;

public class LoadTest {

    static String TEST_FILE = "data.csv";
    static String DELIMITER = ";";
    static String NEW_LINE = "\n";
    private static final String SAVE_FILE_NAME = "saveInfo.txt";
    ArrayList<TaskObject> loadedTasks = new ArrayList<TaskObject>();
    IStorage storageTest = FileStorage.getInstance();
    
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
            loadedTasks = storageTest.load();
            Files.delete(Paths.get(TEST_FILE));
        }
        catch (IOException e){
        }
        AssertHelper.assertTaskEquals("task1", task1 , loadedTasks.get(0));
        AssertHelper.assertTaskEquals("task2", task2 , loadedTasks.get(1));
        assertEquals("Length", 2, loadedTasks.size());
    }
    
    @Test
    public void testNoFile() throws NoSuchFileException, IOException {
        boolean noFile = false;
        try{
        loadedTasks = storageTest.load();        
        }
        catch (NoSuchFileException e) {
            noFile = true;
        }
        assertEquals("Length",  0 , loadedTasks.size());
        loadedTasks = storageTest.load();
        assertEquals("Length",  0 , loadedTasks.size());
        assertEquals("ExceptionNoFileCaught", false, noFile);
        }
    
    @Test 
    public void loadFromEmpty() throws IOException {
            FileWriter fileWriter = new FileWriter(TEST_FILE, false);
            fileWriter.close();
            fileWriter = new FileWriter(TEST_FILE, false);
            fileWriter.close();
            storageTest.load();
            loadedTasks = storageTest.load();
            Files.delete(Paths.get(TEST_FILE));
            assertEquals("Length", 0, loadedTasks.size());
    }
    
    @Test
    public void testInvalidDefaultPath() throws NoSuchFileException, IOException {
        int exception = 0;
        try {
        FileWriter fileWriter = new FileWriter(SAVE_FILE_NAME , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("fail");
        printWriter.close();
        }
        catch (IOException e){
            exception = 1;
        }
        try {
            loadedTasks = storageTest.load();
        } catch (NoSuchFileException e1) {
            exception = 2;
        } catch (IOException e1) {
            exception = 3;
        } 
        try {
            Files.delete(Paths.get(SAVE_FILE_NAME));
        } catch (IOException e) {
            exception = 4;
        }
        assertEquals("Length",  0 , loadedTasks.size());
        loadedTasks = storageTest.load();
        assertEquals("Length",  0 , loadedTasks.size());
        assertEquals("ExceptionFailToReadPath", 2, exception);
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

}
