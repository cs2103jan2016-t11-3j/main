package storage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
    private static final String DATA_FILE_NAME = "data.csv";

    /**
     * Changes the default directory location to store the data file to the provided path.
     * <p>
     * @param directory Location of new directory to contain data file for saved tasks
     * @throws FileNotFoundException Invalid directory
     * @throws IOException Error saving new directory
     */
    static void changeDirectory(String directory) throws FileNotFoundException, IOException {
        if (!isValidPath(Paths.get(directory))) {
            throw new FileNotFoundException();
        }
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
     * Returns the default location if no file has been specified.
     * @throws NoSuchFileException Existing file path is invalid.
     * @throws IOException Error reading file containing default path
     */
    static String getPath() throws NoSuchFileException, IOException {
        Path path = Paths.get(".");
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader (SAVE_FILE_NAME));
            path = Paths.get(fileReader.readLine());
            fileReader.close();
        }  catch (FileNotFoundException e) {
            return path.resolve(DATA_FILE_NAME).toString();
        }
        if (!isValidPath(path)) {
            throw new NoSuchFileException(path.toString());
        }
        return path.resolve(DATA_FILE_NAME).toString();
    }

    static String getPath(String filePath, String fileName) {
        Path path = Paths.get(filePath);
        if (!isValidPath(path)) {
            return null;
        }
        return path.resolve(fileName).toString();
    }
    
    static boolean isValidPath (Path path) {
        return Files.exists(path);
    }

    static boolean isValidPath (String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

}
