package test.storage;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class FilePathTest extends storage.FilePath {

    private static final String SAVE_FILE_NAME = "saveInfo.txt";
    private static final String DATA_FILE_NAME = "data.csv";
    
    @Test
    public void testChangeDirectory() {
        fail("Not yet implemented");
    }

    @Test(expected = NoSuchFileException.class)
    public void testGetPathNoFile() throws NoSuchFileException, IOException {
        getPath();
    }
    
    @Test
    public void testGetPath() throws NoSuchFileException, IOException {
        String saveDir = Paths.get(".").toAbsolutePath().normalize().toString();
        writeSaveDir(saveDir);
        String actualFilePath = getPath();
        String expectedFilePath = Paths.get(saveDir, DATA_FILE_NAME).toString();
        Path path = Paths.get(saveDir , SAVE_FILE_NAME);
        Files.delete(path);
        assertEquals("Returned file Path", expectedFilePath , actualFilePath);
    }

    @Test
    public void testCheckValidFolder() {
        boolean isUsable = checkPath(".");
        assertEquals("Folder is Usable", true, isUsable);
    }

    @Test
    public void testCheckInalidPath() {
        boolean isUsable = checkPath("fail");
        Paths.get("should fail");
        assertEquals("Folder is Usable", false, isUsable);
    }
        
    private void writeSaveDir(String directory) throws IOException {
        FileWriter fileWriter = new FileWriter(SAVE_FILE_NAME , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(directory.toString());
        printWriter.close();
    }

}


