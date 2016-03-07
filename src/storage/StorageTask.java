package storage;

import common.TaskObject;

public class StorageTask extends TaskObject {
    
    /**
     * Creates taskObject from array of task attributes
     * @param taskAttributes
     */
    public StorageTask(String[] taskAttributes) {
        title = (taskAttributes[0]); 
        startDate = Integer.parseInt(taskAttributes[1]);
        endDate = Integer.parseInt(taskAttributes[2]);
        startTime = Integer.parseInt(taskAttributes[3]);
        endTime = Integer.parseInt(taskAttributes[4]);
        category = taskAttributes[5];
        status = taskAttributes[6];
        taskId = Integer.parseInt(taskAttributes[7]);
    }
    
    public StorageTask(String title, int taskId){
        this.title = title;
        this.startDate = 0;
        this.endDate = 0;
        this.startTime = 0;
        this.endTime = 0;
        this.category = "";
        this.status = "";
        this.taskId = taskId;
    }

}