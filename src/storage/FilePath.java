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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import common.AtfLogger;

public class FilePath {

    /**
     * Changes the default directory location to store the data file to the provided path.
     * <p>
     * @param directory Location of new directory to contain data file for saved tasks
     * @throws IOException Error saving new directory
     */
    protected static void changePreferedDirectory(String directory) throws IOException {
        Logger logger = AtfLogger.getLogger();
        mkdirIfNotExist(directory);
        checkDirectory(directory);
        writePreferredDirectory(directory);
        logger.info(String.format(Constants.LOG_CHANGE_PREFERED_DIR, directory));
    }

    /**
     * Returns the path of the file containing saved tasks. By default returns
     * the directory containing the program unless another directory has been defined.
     * <p>
     * @return String of path of the file containing saved tasks.
     * @throws FileNotFound No existing preferred directory specified.
     * @throws IOException Error reading file containing default path
     */
    protected static String getPath() throws FileNotFoundException , IOException {
        String directory = readPreferedDirectory();
        checkDirectory(directory);
        Path path = Paths.get(directory, Constants.DATA_FILENAME);
        return path.toString();
    }
    
    /**
     * 
     * @param directory
     * @throws InvalidPathException
     */
    protected static void checkDirectory(String directory) throws InvalidPathException {
        assert directory != null;
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
        if(!Files.exists(Constants.FILEPATH_DEFAULT_SAVE_)) {
            changePreferedDirectory(Constants.DEFAULT_DIRECTORY);
        }
    }

    private static String readPreferedDirectory() throws FileNotFoundException, IOException  {
        BufferedReader fileReader = new BufferedReader(
                new FileReader (Constants.FILEPATH_SAVEINFO.toString()));
        String directory = fileReader.readLine();
        fileReader.close();
        return directory;
    }
    
    private static void mkdirIfNotExist(String directory) {
        Logger logger = AtfLogger.getLogger();
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
            logger.info(String.format(Constants.LOG_MKDIR, directory));
        }
    }

    private static void writePreferredDirectory(String directory) throws IOException {
        FileWriter fileWriter = new FileWriter(Constants.FILEPATH_SAVEINFO.toString() , false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(directory);
        printWriter.close();
    }
}
