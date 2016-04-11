//@@author A0080510X

package Test.storage;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import storage.Constants;

public class FilePathTest extends storage.FilePath {

    private static final String DATA_FILE_NAME = Constants.FILENAME_DATA;

    @Before
    public void setUp() throws IOException {
        Path path = Constants.FILEPATH_SAVEINFO;
        Path path2 = Paths.get(".", "bin" , "save");
        Files.deleteIfExists(path);
        Files.deleteIfExists(path2);
        
    }
    
    
    @After
    public void tearDown() throws IOException {
        Path path = Constants.FILEPATH_SAVEINFO;
        Path path2 = Paths.get(".", "bin" , "save");
        Files.deleteIfExists(path);
        Files.deleteIfExists(path2);
        
    }
    /**
     * This is a case to detect that there is no existing preferred directory settings
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test(expected = FileNotFoundException.class)
    public void testGetPathNoFile() throws FileNotFoundException, IOException {
        getPath();
    }
    
    /**
     * This is a case to check that the corrected preferred directory settings is 
     * read when the settings have been written to disk previously.
     * @throws NoSuchFileException
     * @throws IOException
     */
    @Test
    public void testGetPath() throws NoSuchFileException, IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        writeSaveDir(saveDir);
        String actualFilePath = getPath();
        String expectedFilePath = Paths.get(saveDir, DATA_FILE_NAME).toString();
        assertEquals("Returned file Path", expectedFilePath , actualFilePath);
    }

    /**
     * Test case for checking that a valid folder can indeed be used by storage.
     */
    @Test
    public void testCheckValidFolder() {
        directoryValid(".");
    }

    /**
     * Test case for checking that an invalid folder is indeed not usable by storage.
     */
    public void testCheckInalidPath() {
        assertEquals(false, directoryValid("fail"));
    }
    
    /**
     * Test case to check that the change Directory command indeed changes the stored
     * settings on the file saved on disk.
     * @throws IOException
     */
    @Test
    public void testChangeDirectory() throws IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        Path path = Paths.get(saveDir, "bin");
        Path expectedPath = Paths.get(saveDir, "bin" , DATA_FILE_NAME);
        changePreferedDirectory(path.toString());
        assertEquals( "Change Directory to bin" , expectedPath.toString() , getPath() );
    }
    
    /**
     * Test case to check that the change Directory command indeed changes the stored 
     * settings on the file saved on disk and can create the new directory if it does not
     * already exist.
     * @throws IOException
     */
    @Test
    public void testChangeNewDirectory() throws IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        Path path = Paths.get(saveDir, "bin" , "save");
        Path expectedPath = Paths.get(saveDir, "bin"  , "save" , DATA_FILE_NAME);
        changePreferedDirectory(path.toString());
        assertEquals( "Change Directory to bin" , expectedPath.toString() , getPath() );
    }
        
    private void writeSaveDir(String directory) throws IOException {
        FileWriter fileWriter = new FileWriter(Constants.FILEPATH_SAVEINFO.toString() , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(directory.toString());
        printWriter.close();
    }


    
}


