package test.storage;

import java.io.IOException;

import org.junit.Test;

public class StorageTest {

    @Test
    public void loadTest() throws IOException {
        for (int i = 0; i < 10; i++) {
            LoadTest load = new LoadTest();
            load.test();
            load.testNoFile();
            load.testInvalidDefaultPath();
        }
    }

    @Test
    public void saveTest() {
        for (int i = 0; i < 10; i++) {
            SaveTest save = new SaveTest();
            save.testWrite();
            save.testOverwrite();
        }
    }

    @Test
    public void CreateCopyTest() throws IOException {
        for (int i = 0; i < 10; i++) {
            CreateCopyTest copy = new CreateCopyTest();
            copy.testCurrent();
            copy.testInvalidDir();
            copy.testNameConflict();
            copy.testNoExisting();
            copy.testParent();
            copy.testSubDir();
        }
    }

}
