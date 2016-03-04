package storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import logic.TaskObject;

public class FileStorage implements Storage {

    private static FileStorage instance = null;
    private static ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();

    private FileStorage() {
    }

    public static FileStorage getInstance() {
        if (instance == null) {
            instance = new FileStorage();
        }
        return instance;
    }

    @Override
    public int save(ArrayList<TaskObject> newTaskList) {
        try {
            TaskData.overWriteList(newTaskList);
        } catch (NoSuchFileException e) {
            return 1;
        } catch (IOException e) {
            return 2;
        }
        taskList = newTaskList;
        return 0;
    }

    @Override
    public ArrayList<TaskObject> load() throws NoSuchFileException, IOException {
        ArrayList<String> taskDataList = new ArrayList<String>();
        taskDataList = TaskData.readData();
        taskList = TaskData.parseData(taskDataList);
        return taskList;
    }

    @Override
    public int createCopy(String directory , String fileName) {
        if ( taskList.isEmpty() ) {
            return 1;
        }
        if ( !FilePath.isValidPath(directory) ) {
            return 2;
        }
        String filePath = FilePath.formPath(directory, fileName);
        try {
            TaskData.writeList(taskList, filePath);
        }  catch (IOException e) {
            return 3;
        }
        return 0;
    }

    @Override
    public ArrayList<TaskObject> getTaskList() {
        return taskList;
    }

    @Override
    public int changeSaveLocation (String directory) {
        if ( createCopy(directory, "data.csv") != 0 ) {
            return 1;
        }
        try {
            TaskData.deleteData();
        } catch (NoSuchFileException e) {
            return 2;
        } catch (IOException e) {
            return 3;
        }
        try {
            FilePath.changeDirectory(directory);
        } catch (FileNotFoundException e) {
            return 4;
        } catch (IOException e) {
            return 5;
        }
        return 0;

    }

    public void load(String directory) {

    }

}
