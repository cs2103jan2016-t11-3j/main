//@@author A0080510X

package storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Constants {

    public static final String DEFAULT_DIRECTORY = ".";
    public static final String ATF_DIRECTORY = "atf_files";
    
    public static final String FILENAME_DATA = "data.txt";
    public static final String FILENAME_BACKUP_DATA = "atf_backup.json";
    public static final Path FILEPATH_BACKUP_DATA = Paths
            .get(DEFAULT_DIRECTORY, ATF_DIRECTORY , FILENAME_BACKUP_DATA);
    
    public static final String FILENAME_SAVEINFO = "saveInfo.txt";
    public static final Path FILEPATH_SAVEINFO = Paths.get(ATF_DIRECTORY, FILENAME_SAVEINFO);
    public static final Path FILEPATH_DEFAULT_SAVE = Paths
            .get(DEFAULT_DIRECTORY, ATF_DIRECTORY , FILENAME_SAVEINFO);
    
    public static final String FILENAME_LOG = "log.txt";
    public static final Path FILEPATH_LOGDIR = Paths.get(DEFAULT_DIRECTORY, ATF_DIRECTORY);
    public static final Path FILEPATH_LOGFILE = Paths.get(FILEPATH_LOGDIR.toString(), FILENAME_LOG);
    
    static final String LOG_SAVED = "Tasks saved to: %s";
    static final String LOG_LOADED = "Tasks loaded from: %s";
    static final String LOG_MKDIR = "Directory created: %s";
    static final String LOG_CHANGE_PREFERED_DIR = "Directory changed to: %s";
    

    
    
    
}
