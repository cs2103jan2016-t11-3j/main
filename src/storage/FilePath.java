package storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePath {

    private static final String SAVE_FILE_NAME = "saveInfo.txt";
    private static final String DATA_FILE_NAME = "data.txt";

    /**
     * Changes the default directory location to store the data file to the provided path.
     * <p>
     * @param directory Location of new directory to contain data file for saved tasks
     * @throws IOException Error saving new directory
     */
    protected static void changeDirectory(String directory) throws IOException {
        FileWriter fileWriter = new FileWriter(SAVE_FILE_NAME , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(directory.toString());
        printWriter.close();
    }

    /**
     * Returns the path of the file containing saved tasks. By default returns
     * the directory containing the program unless another directory has been defined.
     * <p>
     * @return String of path of the file containing saved tasks.
     * @throws NoSuchFileException Existing file path is invalid.
     * @throws IOException Error reading file containing default path
     */
    protected static String getPath() throws NoSuchFileException , IOException {
        Path path = Paths.get("." , SAVE_FILE_NAME);
        if(!Files.exists( path )) {
            throw new NoSuchFileException(path.toString() , null, "No saveInfo");
        }
        BufferedReader fileReader = new BufferedReader(new FileReader (SAVE_FILE_NAME));
        path = Paths.get(fileReader.readLine());
        fileReader.close();
        return path.resolve(DATA_FILE_NAME).toString();
    }
    
    /**
     * Checks if the specified filePath is writable and readable.  
     * @param filePath or path of file to check
     */
    protected static boolean checkPath(String filePath) {
        Path path = Paths.get(filePath);
        return Files.isReadable(path) && Files.isWritable(path);
    }

    /**
     * Sets the save location to the default if the save location has not been specified. 
     * The location where the data files created by the program will be set 
     * to the working directory containing the program. If the save location has already
     * been specified, it will not be changed. 
     * @throws IOException Error creating the file containing the save location
     */
    static void prepareDefaultSave() throws IOException {
        Path path = Paths.get("." , SAVE_FILE_NAME);
        if(!Files.exists( path )) {
            changeDirectory(".");
        }
    }
    
}
