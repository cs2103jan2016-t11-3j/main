package test;

import java.util.ArrayList;

import logic.TaskObject;

public class TaskGenerator {
    public static Integer taskId = 0;

    public ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
    public ArrayList<String> dataList = new ArrayList<String>();

    public TaskObject getTask() {
        taskId += 1;
        TaskObject task = new TaskObject("task" + taskId.toString(), taskId);
        return task;
    }

    public String getLastData() {
        if (taskId == 0) {
            return null;
        }
        String data = "task" + taskId.toString() + ";0;0;0;0;;;" + taskId.toString() + ";";
        return data;
    }

    public ArrayList<TaskObject> getTaskList( int size) {
        taskList.clear();
        dataList.clear();
        for (int i = 0; i < size; i++) {
            taskList.add(getTask());
            dataList.add(getLastData());
        }
        return taskList;
    }
    public ArrayList<String> getLastDataList() {
        return dataList;
    }

}
