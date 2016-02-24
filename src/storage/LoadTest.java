package storage;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Test;

import logic.TaskObject;

public class LoadTest {

    static String TEST_FILE = "data.csv";
    static String DELIMITER = ";";
    static String NEW_LINE = "\n";


    @Test
    public void test() {

        TaskObject task1 = new TaskObject("task1", 1);
        TaskObject task2 = new TaskObject("task2", 2);
        Storage storageTest = FileStorage.getInstance();
        ArrayList<TaskObject> loadedTasks = new ArrayList<TaskObject>();
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
        }
        catch (IOException e){
        }
        compareTasks("task1", task1 , loadedTasks.get(0));
        compareTasks("task2", task2 , loadedTasks.get(1));
        assertEquals("Length", 2, loadedTasks.size());
    }

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
