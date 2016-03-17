/**
 * 
 */
package test.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonSyntaxException;

import common.TaskObject;
import storage.Constants;
import storage.FileStorage;
import storage.IStorage;
import test.AssertHelper;
import test.TaskGenerator;

/**
 * @author Hang
 *
 */
public class FileStorageTest {

    
    public static ArrayList<TaskObject> taskList1 = new ArrayList<TaskObject>();
    public static ArrayList<TaskObject> taskList2 = new ArrayList<TaskObject>();
    public static String moveDir = Paths.get(Constants.ATF_DIRECTORY).toString();
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        TaskGenerator taskGen = new TaskGenerator();
        taskList1 = taskGen.getTaskList(5);
        taskList2 = taskGen.getTaskList(5);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Path path1 = Paths.get(Constants.DEFAULT_DIRECTORY, Constants.DATA_FILENAME);
        Path path2 = Paths.get(moveDir , "test");
        Path path3 = Paths.get(moveDir , Constants.DATA_FILENAME);
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        Files.deleteIfExists(path3);
        Files.deleteIfExists(Constants.FILEPATH_DEFAULT_SAVE_);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSaveLoad() throws IOException {
        IStorage storage = FileStorage.getInstance();
        storage.save(taskList1);
        ArrayList<TaskObject> actualTaskList = storage.load();
        AssertHelper.assertTaskListEquals("SaveLoad" , taskList1 , actualTaskList);
    }

    @Test
    public void testSaveLoadSeperate() throws IOException {
        IStorage storage = FileStorage.getInstance();
        storage.save(taskList1);
        storage = null;
        storage = FileStorage.getInstance();
        ArrayList<TaskObject> actualTaskList = storage.load();
        AssertHelper.assertTaskListEquals("SaveLoad" , taskList1 , actualTaskList);
    }

    @Test
    public void testOverWrite() throws IOException {
        IStorage storage = FileStorage.getInstance();
        storage.save(taskList1);
        storage.save(taskList2);
        ArrayList<TaskObject> actualTaskList = storage.load();
        AssertHelper.assertTaskListEquals("SaveLoad" , taskList2 , actualTaskList);
    }

    @Test
    public void testCreateCopyLoadFrom() throws IOException {
        IStorage storage = FileStorage.getInstance();
        storage.save(taskList1);
        storage.createCopy(moveDir, "test");
        ArrayList<TaskObject> actualTaskList = 
                storage.load(moveDir , "test");
        AssertHelper.assertTaskListEquals("CreateCopyLoadFrom" , taskList1 , actualTaskList);
    }


    @Test
    public void testChangeSaveLocation() throws InvalidPathException, JsonSyntaxException, FileNotFoundException, IOException {
        IStorage storage = FileStorage.getInstance();
        storage.save(taskList1);
        storage.changeSaveLocation(moveDir);
        storage.save(taskList2);
        ArrayList<TaskObject> actualTaskList = storage.load();
        AssertHelper.assertTaskListEquals("CreateCopyLoadFrom" , taskList2 , actualTaskList);
    }


}
