package test.storage;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import logic.TaskObject;
import storage.FileStorage;
import storage.Storage;
import test.AssertHelper;
import test.TaskGenerator;

public class ChangeSaveTest {

    Storage testStorage = FileStorage.getInstance();
    TaskGenerator dummy = new TaskGenerator();
    ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
    ArrayList<String> dataList = new ArrayList<String>();
    ArrayList<String> expectedDataList = new ArrayList<String>();
    private final Path defaultPath = Paths.get(".", "data.csv");
    private final String fileName = "data.csv";
    static final int size = 5;
    static final int moveSize = 7;
    
    @Test
    public void testMoveSub() throws NoSuchFileException, IOException {
        String dir = "./bin/";
        Path moved = Paths.get(dir, fileName);
        taskList = dummy.getTaskList(size);
        expectedDataList = dummy.getLastDataList();
        testStorage.save(taskList);
        testStorage.changeSaveLocation(dir);
        AssertHelper.assertFileEquals("testMoveSub moved file", moved , expectedDataList);
        assertEquals("Existing deleted" , true , Files.notExists( defaultPath));
        try {
            Files.delete(Paths.get(".", "saveInfo.txt"));
            Files.delete(moved);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
