//@@author A0080510X

package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import common.TaskObject;

public class AssertHelper {

    public static void assertTaskEquals(String testDescription, TaskObject task1, TaskObject task2) {
        assertEquals(testDescription + " title", task1.getTitle(), task2.getTitle());
        assertEquals(testDescription + " start dateTime", task1.getStartDateTime(), task2.getEndDateTime());
        assertEquals(testDescription + " end dateTime", task1.getEndDateTime(), task2.getEndDateTime());
        assertEquals(testDescription + " category", task1.getCategory(), task2.getCategory());
        assertEquals(testDescription + " Status", task1.getStatus(), task2.getStatus());
        assertEquals(testDescription + " taskId", task1.getTaskId(), task2.getTaskId());
    }
    
    public static void assertTaskListEquals(String testDescription, 
            ArrayList<TaskObject> expectedTaskList, ArrayList<TaskObject> actualTaskList) {
        assertEquals(testDescription + "listSize" , expectedTaskList.size(), actualTaskList.size());
        int size = expectedTaskList.size();
        for (int i = 0; i < size; i++) {
            assertTaskEquals( testDescription+ "dataListContent" , expectedTaskList.get(i) , actualTaskList.get(i));
        }
    }

    public static void assertArrayListEquals(String message, int size, ArrayList<String> expectedDataList, ArrayList<String> dataList) {
        assertEquals(message + "listSize" , expectedDataList.size(), dataList.size());
        for (int i = 0; i < size; i++) {
            assertEquals( message+ "dataListContent" , expectedDataList.get(i) , dataList.get(i));
        }
    }

    }
