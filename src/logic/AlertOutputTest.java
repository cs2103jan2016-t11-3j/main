package logic;

import static org.junit.Assert.*;

import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static logic.constants.Strings.*;
import static logic.constants.Index.*;

import common.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlertOutputTest {

	private static ArrayList<TaskObject> taskList = new ArrayList<TaskObject> ();
	private static ArrayList<String> expectedOutput = new ArrayList<String> ();
	private ArrayList<String> alertOutput = new ArrayList<String> ();
	
	@Test
	// Event with ending time on the same day
	public void testA() {
		TaskObject task = new TaskObject("event 1", LocalDateTime.of(LocalDate.now(), LocalTime.of(00, 00)), 
				LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59)), "event", "incomplete", 1);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Events today:");
		expectedOutput.add("Task: event 1; Time: 00:00 to 23:59 ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}

	@Test
	// Event with no start time, ending time on the same day
	public void testB() {
		TaskObject task = new TaskObject("event 2", LocalDateTime.of(LocalDate.now(), LocalTime.MAX), 
				LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59)), "event", "incomplete", 2);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Task: event 2; Time: to 23:59 ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}
	
	@Test
	// Event with no start time, no end time, on the same day
	public void testC() {
		TaskObject task = new TaskObject("event 3", LocalDateTime.of(LocalDate.now(), LocalTime.MAX), 
				LocalDateTime.of(LocalDate.now(), LocalTime.MAX), "event", "incomplete", 3);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Task: event 3; Time: not specified ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}

	@Test
	// Event with start time, no end time, on the same day
	public void testD() {
		TaskObject task = new TaskObject("event 4", LocalDateTime.of(LocalDate.now(), LocalTime.of(00, 00)), 
				LocalDateTime.of(LocalDate.now(), LocalTime.MAX), "event", "incomplete", 4);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Task: event 4; Time: from 00:00 today ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}
	
	@Test
	// Event with start time, end time, on different day
	public void testE() {
		TaskObject task = new TaskObject("event 5", LocalDateTime.of(LocalDate.now(), LocalTime.of(00, 00)), 
				LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(23, 59)), "event", "incomplete", 5);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Task: event 5; Time: 00:00 to 23:59 on 2016-03-23 ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}
	
	@Test 
	// Event with start time, no end time, on different day
	public void testF() {
		TaskObject task = new TaskObject("event 6", LocalDateTime.of(LocalDate.now(), LocalTime.of(00, 00)), 
				LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX), "event", "incomplete", 6);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Task: event 6; Time: 00:00 to 2016-03-23 ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}
	
	@Test
	// Event with no start time, no end time, on different day
	public void testG() {
		TaskObject task = new TaskObject("event 7", LocalDateTime.of(LocalDate.now(), LocalTime.MAX), 
				LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MAX), "event", "incomplete", 7);
		taskList.add(task);
		LogicStub testDriver = new LogicStub(taskList, alertOutput);
		testDriver.createAlertOutput(taskList);
		
		expectedOutput.add("Task: event 7; Time: to 2016-03-23 ");
		
		assertEquals(expectedOutput, testDriver.getAlertOutput());
	}
}
