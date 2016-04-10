//@@author A0080510X

package logic.save;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import logic.Logic;
import storage.Constants;

public class saveTest {

    static Path defaultSavePath = Paths.get(Constants.DEFAULT_DIRECTORY, Constants.FILENAME_DATA);
    static Path movedSavePath = Paths.get(Constants.DEFAULT_DIRECTORY, 
            Constants.ATF_DIRECTORY, Constants.FILENAME_DATA);
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        deleteInfo();
    }

    @After
    public void tearDown() throws Exception {
        deleteInfo();
    }

    @Test
    public void testChangePreferedDirectory() throws FileNotFoundException, IOException {
        Logic logic = new Logic();
        String expectedDirectory = Paths.get(Constants.DEFAULT_DIRECTORY, 
                Constants.ATF_DIRECTORY).toString();
        logic.run("add task1");
        logic.run("save to " + expectedDirectory);
        logic.run("add task2");
        
        BufferedReader fileReader = new BufferedReader(
                new FileReader (Constants.FILEPATH_SAVEINFO.toString()));
        String actualDirectory = fileReader.readLine();
        fileReader.close();     
        File movedFile = new File(movedSavePath.toString());
        assertEquals(expectedDirectory, actualDirectory);
        assertEquals(true, movedFile.exists());
    }
    
    private static void deleteInfo() throws IOException {
        Files.deleteIfExists(defaultSavePath);
        Files.deleteIfExists(movedSavePath);
        Files.deleteIfExists(Constants.FILEPATH_SAVEINFO);
    }

}
