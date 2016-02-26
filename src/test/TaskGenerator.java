package test;

import logic.TaskObject;

public class TaskGenerator {
    public static Integer taskId = 0;
    
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
}