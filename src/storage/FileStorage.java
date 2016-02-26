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
        } catch (NoSuchFileException e) {
            return 1;
        } catch (IOException e) {
            return 2;
        }
        taskList = TaskData.parseData(taskDataList);
        return 0;
    }
    
    @Override
     public int createCopy(String directory , String fileName) {
        String filePath = FilePath.formPath(directory, fileName);
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
    
    @Override
    public int changeSaveLocation (String directory) {
        createCopy(directory, "data.csv");
        try {
            TaskData.deleteData();
        } catch (NoSuchFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 2;
        }
        try {
            FilePath.changeDirectory(directory);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 3;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 4;
        }
        return 0;
        
    }
    
    public void load(String directory) {
        
    }

}
