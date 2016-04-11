//@@author A0080510X

package test;

import java.util.ArrayList;

import common.TaskObject;

/**
 * This class helps to create a list of unique dummy tasks for testing.
 * @author Hang
 *
 */
public class TaskGenerator {
    public static Integer taskId = 0;

    public ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();

    public TaskObject getTask() {
        taskId += 1;
        TaskObject task = new TaskObject("task" + taskId.toString());
        task.setTaskId(taskId);
        return task;
    }

    public ArrayList<TaskObject> getTaskList( int size) {
        taskList.clear();
        for (int i = 0; i < size; i++) {
            taskList.add(getTask());
        }
        return taskList;
    }

}
