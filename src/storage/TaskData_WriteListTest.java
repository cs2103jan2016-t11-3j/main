/**
 * 
 */
package storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import Test.TaskGenerator;
import logic.TaskObject;

/**
 * @author Hang
 *
 */
public class TaskData_WriteListTest extends TaskData {

    TaskGenerator testTask = new TaskGenerator();
    ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
    /**
     * Test method for {@link storage.TaskData#writeList(java.util.ArrayList, java.lang.String)}.
     */
    @Test
    public void testWriteToSubDir() {
        TaskObject task1 = testTask.getTask();
        String data1 = testTask.getLastData();
        TaskObject task2 = testTask.getTask();
        String data2 = testTask.getLastData();
        taskList.add(task1);
        taskList.add(task2);
        try {
            writeList(taskList, "./bin/dataTest.csv");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<String> taskDataList = new ArrayList<String>();
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader("./bin/dataTest.csv"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line = null;
        try {
            while ((line = fileReader.readLine()) != null) {
                taskDataList.add(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals("task1", taskDataList.get(0), data1);
        assertEquals("task2", taskDataList.get(1), data2);
        
    }

}
