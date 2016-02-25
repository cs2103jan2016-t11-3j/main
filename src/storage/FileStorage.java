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
    public int createCopy(String filePath , String fileName) {
        ArrayList<TaskObject> taskList;
        try {
            taskList = load();
        } catch (FileNotFoundException e1) {
            return 1;
        } catch (IOException e1) {
            return 2;
        }
        try {
            TaskData.writeList(taskList, filePath + "/" + fileName);
        } catch (IOException e) {
            return 3;
        }
        return 0;
    }
    
    public void changeSaveLocation(String savePath) {
        
    }
}
