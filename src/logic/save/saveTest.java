package logic.save;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws FileNotFoundException, IOException {
        Logic logic = new Logic();
        String path = Paths.get(Constants.ATF_DIRECTORY, Constants.DATA_FILENAME).toString();
        logic.run("change directory " + path);
        logic.run("add task");
        
        assertEquals( storage.FilePath.getPath() , path);
    }


}
