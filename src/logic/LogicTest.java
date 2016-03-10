package logic;

import static org.junit.Assert.*;

import org.junit.Test;

import common.CommandObject;
import common.TaskObject;

import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class LogicTest {
	
	// Initialisation of Logic class
	ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	Deque<CommandObject> undoList = new ArrayDeque<CommandObject>();
	Deque<CommandObject> redoList = new ArrayDeque<CommandObject>();
	Logic logic = new Logic(taskList, undoList, redoList);	

	ArrayList<String> output = new ArrayList<String>();
	ArrayList<String> expectedOutput = new ArrayList<String>();
	CommandObject commandObj;
	TaskObject taskObj;
	
	@Test
	public void testUndoAfterAdd() {
		String test1 = "add lunch with bill clinton date: 8/3/2015 time: 12pm";
		logic.run(test1);
		printOutput();
		
		String test2 = "add visit the old folks' home date: 9/3/2015 time: 4pm";
		logic.run(test2);
		printOutput();
		
		String test3 = "add get my shit together";
		logic.run(test3);
		printOutput();
		
		String display = "display";
		logic.run(display);
		printOutput();
		
		String undo = "undo";
		logic.run(undo);
		printOutput();
		logic.run(undo);
		printOutput();
		
		logic.run(display);
		printOutput();
		
		//logic.run(test5);
		//printOutput();
		
		String redo = "redo";
		logic.run(redo);
		printOutput();
		
		logic.run(display);
		printOutput();
		
		logic.run(undo);
		printOutput();
		
		logic.run(display);
		printOutput();
		
	}
	
	/*
	@Test
	public void test() {
		String test1 = "add lunch with bill clinton date: 8/3/2015 time: 12pm";
		logic.run(test1);
		printOutput();
		
		String test2 = "add visit the old folks' home date: 9/3/2015 time: 4pm";
		logic.run(test2);
		printOutput();
		
		String test3 = "add get my shit together";
		logic.run(test3);
		printOutput();
		
		String test4 = "display";
		logic.run(test4);
		printOutput();
		
		String test5 = "undo";
		logic.run(test5);
		printOutput();
		
		String test6 = "display";
		logic.run(test6);
		printOutput();
	}
	
	
	
	
	@Test
	public void test() {
		
		// ADD FUNCTION WORKS
		String testUserInput = "add float float float";
		logic.run(testUserInput);
		printOutput();

		String testUserInput2 = "add meowwoofmoo";
		logic.run(testUserInput2);
		printOutput();
				
		// SEARCH FUNCTION WORKS, BUT 'CATEGORY' AND 'STATUS' ARE NOT FILLED
		String testUserInput3 = "search";
		logic.run(testUserInput3);
		printOutput();
		
		// EDIT FUNCTION WORKS
		String testUserInput4 = "edit 2 changed to this";
		logic.run(testUserInput4);
		printOutput();
		
		String testUserInput5 = "display";
		logic.run(testUserInput5);
		printOutput();
		
		
		// SEARCH FOR SPECIFIC KEYWORD WORKS
		String testUserInput6 = "search float";
		logic.run(testUserInput6);
		printOutput();
		
		String testUserInput7 = "add water is healthy";
		logic.run(testUserInput7);
		printOutput();
		
		String testUserInput8 = "add water occupies 70% of your body";
		logic.run(testUserInput8);
		printOutput();
		
		// DELETE FUNCTION WORKS
		String testUserInput9 = "delete 4";
		logic.run(testUserInput9);
		printOutput();
		
		


		String testUserInput19 = "undo";
		logic.run(testUserInput19);
		printOutput();
	
		String testUserInput11 = "add dinner with obama date:3/3/2015 time:8pm";
		logic.run(testUserInput11);
		printOutput();
		
		String testUserInput10 = "view";
		logic.run(testUserInput10);
		printOutput();
		
		
	}
*/
		
	
	private void printersForDebugging() {
		printOutput();
		System.out.println();
		printTaskList();
		System.out.println();
		printUndoList();
		System.out.println();
	}
	
	private void printTaskObjectFields(TaskObject taskObj) {
		System.out.println("title = " + taskObj.getTitle());
		System.out.println("start date = " + taskObj.getStartDate());
		System.out.println("end date = " + taskObj.getEndDate());
		System.out.println("start time = " + taskObj.getStartTime());
		System.out.println("end time = " + taskObj.getEndTime());
		System.out.println("category = " + taskObj.getCategory());
		System.out.println("status = " + taskObj.getStatus());
		System.out.println("task id = " + taskObj.getTaskId());
	}

	private void printOutput() {
		output = logic.getOutput();
		for (int i = 0; i< output.size(); i++) {
			System.out.println(output.get(i));
		}
		System.out.println();
	}
	
	private void printTaskList() {
		System.out.println("Task list:");
		for (int i = 0; i < taskList.size(); i++) {
			System.out.println("i = " + i + ", task item = " + taskList.get(i) + ", task id = " + taskList.get(i).getTaskId());
			printTaskObjectFields(taskList.get(i));
		}
	}

	/**
	 * Prints everything out in undoList by popping every element into a temporary 
	 * deque before shifting everything back. Prints from top of stack to bottom of stack
	 */
	private void printUndoList() {
		CommandObject temp;
		Deque<CommandObject> tempStack = new ArrayDeque<CommandObject> ();
		System.out.println("Undo list:");
		for (int i = 0; i < undoList.size(); i++) {
			temp = undoList.pop();
			System.out.println("i = " + i);
			System.out.println("command is " + temp.getCommandType());
			printTaskObjectFields(temp.getTaskObject());
			tempStack.push(temp);
		}
		recreateUndoList(tempStack);
	}
	
	private void recreateUndoList(Deque<CommandObject> tempStack) {
		while(!tempStack.isEmpty()) {
			undoList.push(tempStack.pop());
		}
	}
}
