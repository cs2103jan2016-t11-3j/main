package test.storage;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

public class FilePathTest extends storage.FilePath {

    private static final String SAVE_FILE_NAME = "saveInfo.txt";
    private static final String DATA_FILE_NAME = "data.txt";

    @After
    public void tearDown() throws Exception {
        Path path = Paths.get(SAVE_FILE_NAME);
        Path path2 = Paths.get(".", "bin" , "save");
        try {
            Files.delete(path);
            Files.delete(path2);
        } catch (Exception e) {
            // Ignore
        }
        
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testGetPathNoFile() throws FileNotFoundException, IOException {
        getPath();
    }
    
    @Test
    public void testGetPath() throws NoSuchFileException, IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        writeSaveDir(saveDir);
        String actualFilePath = getPath();
        String expectedFilePath = Paths.get(saveDir, DATA_FILE_NAME).toString();
        Path path = Paths.get(saveDir , SAVE_FILE_NAME);
        assertEquals("Returned file Path", expectedFilePath , actualFilePath);
    }

    @Test
    public void testCheckValidFolder() {
        checkDirectory(".");
    }

    @Test(expected = InvalidPathException.class)
    public void testCheckInalidPath() {
        checkDirectory("fail");
    }
    
    @Test
    public void testChangeDirectory() throws IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        Path path = Paths.get(saveDir, "bin");
        Path expectedPath = Paths.get(saveDir, "bin" , DATA_FILE_NAME);
        changeDirectory(path.toString());
        assertEquals( "Change Directory to bin" , expectedPath.toString() , getPath() );
    }
    
    @Test
    public void testChangeNewDirectory() throws IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        Path path = Paths.get(saveDir, "bin" , "save");
        Path expectedPath = Paths.get(saveDir, "bin"  , "save" , DATA_FILE_NAME);
        changeDirectory(path.toString());
        assertEquals( "Change Directory to bin" , expectedPath.toString() , getPath() );
    }
        
    private void writeSaveDir(String directory) throws IOException {
        FileWriter fileWriter = new FileWriter(SAVE_FILE_NAME , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(directory.toString());
        printWriter.close();
    }


    
}


