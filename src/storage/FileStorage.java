package storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import logic.TaskObject;

public class FileStorage implements Storage {
    
    private static FileStorage instance = null;

    private FileStorage() {
    }

    public static FileStorage getInstance() {
        if (instance == null) {
            instance = new FileStorage();
        }
        return instance;
    }    

    @Override
    public int save(ArrayList<TaskObject> taskList) {
        try {
            TaskData.overWriteList(taskList);
        } catch (IOException e) {
            return 1;
        }
        return 0;   
    }

    @Override
    public ArrayList<TaskObject> load() throws FileNotFoundException, IOException {
        ArrayList<String> taskDataList = TaskData.readData();
        ArrayList<TaskObject> taskList = TaskData.parseData(taskDataList);
        return taskList;
    }
    
    @Override
    public int createCopy(String filePath) {
        ArrayList<String> taskDataList = null;
        try {
            taskDataList = TaskData.readData();
        } catch (FileNotFoundException e) {
            return 1;
        } catch (IOException e) {
            return 2;
        }
        ArrayList<TaskObject> taskList = TaskData.parseData(taskDataList);
        try {
            TaskData.writeList(taskList, filePath);
        } catch (IOException e) {
            return 3;
        }
        return 0;
    }
}
