//@@author A0080510X

package storage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Constants used in the Storage Component.
 * @author Hang
 *
 */
public final class Constants {

    /**
     * The Default Directory is the working directory where the program is stored.
     */
    public static final String DEFAULT_DIRECTORY = ".";
    
    /**
     * ATF Directory is a directory created in the default directory to include all 
     * program files not involving task data created by the program.
     */
    public static final String ATF_DIRECTORY = "atf_files";
    
    /**
     * These constants define the default names of the data file and backup data file.
     */
    public static final String FILENAME_DATA = "data.txt";
    public static final String FILENAME_BACKUP_DATA = "atf_backup.json";
    public static final Path FILEPATH_BACKUP_DATA = Paths
            .get(DEFAULT_DIRECTORY, ATF_DIRECTORY , FILENAME_BACKUP_DATA);
    
    /**
     * These constants define the names and path of the file containing the user specified 
     * preferred save directory.
     */
    public static final String FILENAME_SAVEINFO = "saveInfo.txt";
    public static final Path FILEPATH_SAVEINFO = Paths.get(ATF_DIRECTORY, FILENAME_SAVEINFO);
    public static final Path FILEPATH_DEFAULT_SAVE = Paths
            .get(DEFAULT_DIRECTORY, ATF_DIRECTORY , FILENAME_SAVEINFO);
    
    /**
     * These constants define the log file and path of the log file used by the program.
     */
    public static final String FILENAME_LOG = "log.txt";
    public static final Path FILEPATH_LOGDIR = Paths.get(DEFAULT_DIRECTORY, ATF_DIRECTORY);
    public static final Path FILEPATH_LOGFILE = Paths.get(FILEPATH_LOGDIR.toString(), FILENAME_LOG);
    
    /**
     * These constants define the log messages used in storage.
     */
    static final String LOG_SAVED = "Tasks saved to: %s";
    static final String LOG_LOADED = "Tasks loaded from: %s";
    static final String LOG_MKDIR = "Directory created: %s";
    static final String LOG_CHANGE_PREFERED_DIR = "Directory changed to: %s";
    

    
    
    
}
