package Test;

import logic.TaskObject;

public class dummyTask {
    public static Integer taskId = 0;
    
    TaskObject getTask() {
        TaskObject task = new TaskObject("task" + taskId.toString(), taskId);
        return task;
    }
    
    String getLastData() {
        if (taskId == 0) {
            return null;
        }
        String data = "task" + taskId.toString() + ";0;0;0;0;;;" + taskId.toString() + ";";
        return data;
    }
}
