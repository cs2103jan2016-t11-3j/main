package storage;

import java.io.FileNotFoundException;
import java.io.IOException;
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
            TaskData.overWriteList(taskList);
        } catch (IOException e) {
            return 1;
        }
        taskList = newTaskList;
        return 0;   
    }

    @Override
    public int load() {
        ArrayList<String> taskDataList = new ArrayList<String>();
        try {
            taskDataList = TaskData.readData();
        } catch (FileNotFoundException e) {
            return 1;
        } catch (IOException e) {
            return 2;
        }
        taskList = TaskData.parseData(taskDataList);
        return 0;
    }
    
    @Override
     public int createCopy(String directory , String fileName) {
        String filePath = FilePath.setPath(directory, fileName);
        try {
            TaskData.writeList(taskList, filePath);
        } catch (IOException e) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public ArrayList<TaskObject> getTaskList() {
        return taskList;
    }
    
    public void changeSaveLocation(String filePath) {
        
    }
    
    public void load(String directory) {
        
    }

}
