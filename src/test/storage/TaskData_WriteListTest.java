package test.storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import logic.TaskObject;
import storage.TaskData;
import test.TaskGenerator;

/**
 * @author Hang
 *
 */
public class TaskData_WriteListTest extends TaskData {

    TaskGenerator testTask = new TaskGenerator();
    /**
     * Test method for {@link storage.TaskData#writeList(java.util.ArrayList, java.lang.String)}.
     * @throws IOException
     */
    @Test
    public void testSubDir() throws IOException {
        String path = "./bin/testData.csv";
        writeToPath(path);
    }

    @Test
    public void testCurDir() throws IOException {
        String path = "./testData.csv";
        writeToPath(path);
    }

    @Test
    public void testParentDir() throws IOException {
        Path curDir = Paths.get(".").toAbsolutePath();
        String path = Paths.get( curDir.getParent().toString(), "testData.csv").toString();
        writeToPath(path);
    }

    @Test
    public void testAbsolute() throws IOException {
        Path curDir = Paths.get(".", "testData.csv").toAbsolutePath();
        String path = curDir.toString();
        writeToPath(path);
    }

    private void writeToPath(String path) {
        int result = testWrite(path);
        assertEquals("success", 0 , result);
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int testWrite(String filePath) {
        ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
        ArrayList<String> taskDataList = new ArrayList<String>();
        TaskObject task1 = testTask.getTask();
        String data1 = testTask.getLastData();
        TaskObject task2 = testTask.getTask();
        String data2 = testTask.getLastData();
        taskList.add(task1);
        taskList.add(task2);
        try {
            writeList(taskList, filePath);
        } catch (IOException e) {
            return 1;
        }
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            return 2;
        }
        String line = null;
        try {
            while ((line = fileReader.readLine()) != null) {
                taskDataList.add(line);
            }
        } catch (IOException e) {
            return 3;
        }
        try {
            fileReader.close();
        } catch (IOException e) {
            return 4;
        }
        assertEquals("task1", taskDataList.get(0), data1);
        assertEquals("task2", taskDataList.get(1), data2);
        assertEquals("Number of tasks", 2, taskDataList.size());
        return 0;
    }

}
