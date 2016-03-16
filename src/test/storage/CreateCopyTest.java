package test.storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.Test;

import common.TaskObject;
import storage.FileStorage;
import storage.IStorage;
import test.AssertHelper;
import test.TaskGenerator;

public class CreateCopyTest {

    static final String fileName = "data.txt";
    IStorage testStorage = FileStorage.getInstance();
    TaskGenerator dummy = new TaskGenerator();
    ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
    ArrayList<String> dataList = new ArrayList<String>();
    ArrayList<String> expectedDataList = new ArrayList<String>();
    private final Path defaultPath = Paths.get(".", "data.txt");
    static final int size = 5;


    @Test
    public void testSubDir() throws IOException {
        String dir = "./bin/";
        testCreateDir(dir, fileName);
    }

    @Test
    public void testParent() throws IOException {
        Path path = Paths.get(".").toAbsolutePath().getParent();
        String dir = path.toString();
        testCreateDir(dir, fileName);
    }

    @Test
    public void testCurrent() throws IOException {
        String dir = ".";
        testCreateDir(dir, fileName);
    }

    @Test
    public void testNameConflict() throws IOException {
        String dir = ".";
        taskList = dummy.getTaskList(size);
        expectedDataList = dummy.getLastDataList();
        testStorage.save(taskList);
        testStorage.createCopy( dir , fileName);
        Path path = Paths.get(dir, fileName);
        dataList = readFile(path.toString());
        assertEquals(dir + "exists", true , Files.exists(path));
        Files.delete(path);
        AssertHelper.assertArrayListEquals( "subdir" ,size , expectedDataList, dataList);
    }

    @Test
    public void testNoExisting() throws IOException {
        String dir = "./bin/";
        Path path = Paths.get(dir, fileName);
        taskList.clear();
        expectedDataList.clear();;
        testStorage.createCopy( dir , fileName);
        assertEquals(dir + "exists", false , Files.exists(path));
    }

    @Test
    public void testInvalidDir() throws IOException {
        String dir = "fail";
        taskList = dummy.getTaskList(size);
        try {
            testStorage.save(taskList);
        } catch (NoSuchFileException e1) {
            // TODO Auto-generated catch block
        } catch (IOException e1) {
            // TODO Auto-generated catch block
        }
        Path path = Paths.get(dir, fileName);
        try {
            testStorage.createCopy( dir , fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            
        }
        assertEquals(dir + "exists", false , Files.exists(path));
        Files.delete(defaultPath);
    }

    private void testCreateDir(String dir, String fileName) throws IOException {
        taskList = dummy.getTaskList(size);
        expectedDataList = dummy.getLastDataList();
        testStorage.save(taskList);
        testStorage.createCopy( dir , fileName);
        Path path = Paths.get(dir, fileName);
        dataList = readFile(path.toString());
        assertEquals(dir + "exists", true , Files.exists(path));
        Files.delete(path);
        Files.delete(defaultPath);
        AssertHelper.assertArrayListEquals( "subdir" ,size , expectedDataList, dataList);
    }

    public ArrayList<String> readFile( String filePath ) throws IOException {
    ArrayList<String> taskDataList = new ArrayList<String>();
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


}
