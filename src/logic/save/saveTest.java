//@@author A0080510X

package logic.save;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import logic.Logic;
import storage.Constants;

public class saveTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Path path1 = Paths.get(Constants.DEFAULT_DIRECTORY, Constants.FILENAME_DATA);
        Path path2 = Paths.get(Constants.DEFAULT_DIRECTORY, 
                Constants.ATF_DIRECTORY, Constants.FILENAME_DATA);
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        Files.deleteIfExists(Constants.FILEPATH_SAVEINFO);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Path path1 = Paths.get(Constants.DEFAULT_DIRECTORY, Constants.FILENAME_DATA);
        Path path2 = Paths.get(Constants.DEFAULT_DIRECTORY, 
                Constants.ATF_DIRECTORY, Constants.FILENAME_DATA);
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
        Files.deleteIfExists(Constants.FILEPATH_SAVEINFO);
    }

    @Test
    public void test() throws FileNotFoundException, IOException {
        Logic logic = new Logic();
        String expectedDirectory = Paths.get(Constants.DEFAULT_DIRECTORY, 
                Constants.ATF_DIRECTORY).toString();
        
        
        
        logic.run("save to " + expectedDirectory);
        logic.run("add one");
        
        BufferedReader fileReader = new BufferedReader(
                new FileReader (Constants.FILEPATH_SAVEINFO.toString()));
        String actualDirectory = fileReader.readLine();
        fileReader.close();
        
        assertEquals( expectedDirectory, actualDirectory);
    }


}
