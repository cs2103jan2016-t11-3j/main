package logic.timeOutput;

import common.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TimeOutputTest {
	private static ArrayList<TaskObject> testArray = new ArrayList<TaskObject>();
	private ArrayList<String> actualTimeOutput = new ArrayList<String>();
	private static ArrayList<String> expectedTimeOutput = new ArrayList<String>();

	@Test
	public void testA() {
		TaskObject taskOne = new TaskObject("deadline with time and date", "deadline", "incomplete", 1);
		taskOne.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(taskOne);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("by 16:00:00 on 2016-03-15");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	public void testB() {
		TaskObject taskTwo = new TaskObject("event with different dates", "event", "incomplete", 2);
		taskTwo.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		taskTwo.setStartDateTime(LocalDateTime.of(2016, 3, 12, 15, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(taskTwo);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 2016-03-12, from 15:00:00 to 16:00:00 on 2016-03-15");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}
	
	@Test
	public void testC() {
		TaskObject taskThree = new TaskObject("event in a single day", "event", "incomplete", 2);
		taskThree.setEndDateTime(LocalDateTime.of(2016, 3, 15, 16, 00));
		taskThree.setStartDateTime(LocalDateTime.of(2016, 3, 15, 15, 00));
		// of(int year, int month, int dayOfMonth, int hour, int minute)
		testArray.add(taskThree);
		TimeOutput.setTimeOutputForGui(testArray);
		for (int i = 0; i < testArray.size(); i++) {
			actualTimeOutput.add(testArray.get(i).getTimeOutputString());
		}
		expectedTimeOutput.add("on 2016-03-15, from 15:00:00 to 16:00:00");
		
		assertEquals(expectedTimeOutput, actualTimeOutput);
	}

}
