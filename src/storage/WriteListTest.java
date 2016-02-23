package storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import logic.TaskObject;

public class WriteListTest {

    private FileWriter writer;
    static String TEST_FILE = "data.csv";
    static String DELIMITER = ";";
    static String NEW_LINE = "\n";

    @Test
    public void test() {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>(); 
        TaskObject task1 = new TaskObject(1);
        task1.setTitle("task1");
        TaskObject task2 = new TaskObject(2);
        task2.setTitle("task2");
        taskList.add(task1);
        taskList.add(task2);

        StorageFile.WriteList(taskList);
        
        ArrayList<String> writtenList = readFile();
        
        assertEquals("Line 1", "task1;0;0;0;0;;;1;" , writtenList.get(0));
        assertEquals("Line 2", "task2;0;0;0;0;;;2;" , writtenList.get(1));
        
        //test list here
    }

    void writeList (ArrayList<TaskObject> taskList) {
        StorageFile.WriteList(taskList);        
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
