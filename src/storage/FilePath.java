package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import common.AtfLogger;

public class FilePath {

    public static final String DEFAULT_DIRECTORY = ".";
    
    public static final String SAVE_FILENAME = "saveInfo.txt";
    public static final String DATA_FILE_NAME = "data.txt";
    public static final Path DEFAULT_SAVE_PATH = Paths.get(DEFAULT_DIRECTORY, SAVE_FILENAME);
    
    public static final String LOG_FILENAME = "log.txt";
    public static final String LOG_DIRECTORY = "atf_logs";
    public static final Path LOG_FILEPATH = Paths.get(DEFAULT_DIRECTORY, 
            LOG_DIRECTORY , LOG_FILENAME);
    
    /**
     * Changes the default directory location to store the data file to the provided path.
     * <p>
     * @param directory Location of new directory to contain data file for saved tasks
     * @throws IOException Error saving new directory
     */
    protected static void changeDirectory(String directory) throws IOException {
        Logger logger = AtfLogger.getLogger();
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        checkDirectory(directory);
        FileWriter fileWriter = new FileWriter(SAVE_FILENAME , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(directory);
        printWriter.close();
        logger.info(String.format("Directory changed to %s", directory));
    }

    /**
     * Returns the path of the file containing saved tasks. By default returns
     * the directory containing the program unless another directory has been defined.
     * <p>
     * @return String of path of the file containing saved tasks.
     * @throws NoSuchFileException Existing file path is invalid.
     * @throws IOException Error reading file containing default path
     */
    protected static String getPath() throws FileNotFoundException , IOException {
        String directory = getPreferedDirectory();
        checkDirectory(directory);
        Path path = Paths.get(directory, DATA_FILE_NAME);
        return path.toString();
    }
    
    protected static void checkDirectory(String directory) throws InvalidPathException {
        Path path = Paths.get(directory);
        if( !Files.isExecutable(path) || !Files.isWritable(path) || !Files.isReadable(path)) {
            throw new InvalidPathException(directory, "Cannot be used");
        }
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
    static void initializeDefaultSave() throws IOException {
        if(!Files.exists(DEFAULT_SAVE_PATH)) {
            changeDirectory(DEFAULT_DIRECTORY);
        }
    }
    
    private static String getPreferedDirectory() throws FileNotFoundException, IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader (SAVE_FILENAME));
        Path directory = Paths.get(fileReader.readLine());
        fileReader.close();
        return directory.toString();
    }
    
}
