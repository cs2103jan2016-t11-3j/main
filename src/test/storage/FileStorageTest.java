/**
 * 
 */
package test.storage;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.TaskObject;

/**
 * @author Hang
 *
 */
public class FileStorageTest {

    
    public ArrayList<TaskObject> saveList1 = new ArrayList<TaskObject>();
    public ArrayList<TaskObject> saveList2 = new ArrayList<TaskObject>();
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
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
    public void testSave() {
        fail("Not yet implemented");
    }


    @Test
    public void testLoad() {
        fail("Not yet implemented");
    }

    @Test
    public void testCreateCopy() {
        fail("Not yet implemented");
    }


    @Test
    public void testChangeSaveLocation() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link storage.FileStorage#load(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testLoadStringString() {
        fail("Not yet implemented");
    }

}
