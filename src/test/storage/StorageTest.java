package test.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Runs all tests case written for the storage component.
 * 
 * FilePathTest contains unit tests covering the methods of the filePath class.
 * FileStorageTests consist of test cases covering the APIs of the storage component.
 * 
 * Refer to the respective source files for descriptions of the individual test cases.
 * 
 * @author Hang
 *
 */

@RunWith(Suite.class)
@SuiteClasses({
    FilePathTest.class,
    FileStorageTest.class,
})

public class StorageTest {}
