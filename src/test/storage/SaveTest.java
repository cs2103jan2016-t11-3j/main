package test.storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import logic.TaskObject;
import storage.FileStorage;
import storage.Storage;

public class SaveTest {

    static String TEST_FILE = "data.csv";
    static String DELIMITER = ";";
    static String NEW_LINE = "\n";

    @Test
    public void testWrite() {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        TaskObject task1 = new TaskObject("task1", 1);
        TaskObject task2 = new TaskObject("task2", 2);
        taskList.add(task1);
        taskList.add(task2);

        Storage testStorage = FileStorage.getInstance();
        int result = testStorage.save(taskList);


        ArrayList<String> writtenList = readFile();

        assertEquals("Success" , 0 , result);
        assertEquals("Length", 2, writtenList.size());
        assertEquals("Line 1", "task1;0;0;0;0;;;1;" , writtenList.get(0));
        assertEquals("Line 2", "task2;0;0;0;0;;;2;" , writtenList.get(1));

    }

    @Test
    public void testOverwrite(){
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        TaskObject task1 = new TaskObject("task3", 3);
        TaskObject task2 = new TaskObject("task4", 4);
        taskList.add(task1);
        taskList.add(task2);

        Storage testStorage = FileStorage.getInstance();
        int result = testStorage.save(taskList);

        ArrayList<String> writtenList = readFile();


        assertEquals("Success" , result , 0);
        assertEquals("Length", 2, writtenList.size());
        assertEquals("Line 3", "task3;0;0;0;0;;;3;" , writtenList.get(0));
        assertEquals("Line 4", "task4;0;0;0;0;;;4;" , writtenList.get(1));


    }

    private static ArrayList<String> readFile() {
        ArrayList<String> writtenList = new ArrayList<String>();
        BufferedReader fileReader = null;
        String line = "e";
        try {
            fileReader = new BufferedReader(new FileReader(TEST_FILE));
            while ((line = fileReader.readLine()) != null) {
                writtenList.add(line);
            }
            fileReader.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return writtenList;
    }
}
