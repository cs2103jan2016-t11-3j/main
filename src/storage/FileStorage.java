package storage;

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
            TaskData.clearFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            TaskData.addTaskList(taskList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;   
    }

    @Override
    public ArrayList<TaskObject> load() throws IOException {
        ArrayList<String> taskDataList = TaskData.readData();
        ArrayList<TaskObject> taskList = TaskData.parseData(taskDataList);
        return taskList;
    }

}
