//@@author A0124636H

package logic.undoredo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import common.CommandObject;
import common.TaskObject;
import logic.Logic;

import static logic.constants.Index.*;
import static logic.constants.Strings.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UndoRedoTest {

	private static ArrayList<TaskObject> taskList = new ArrayList<TaskObject>();
	private static Deque<CommandObject> undoList = new ArrayDeque<CommandObject>();
	private static Deque<CommandObject> redoList = new ArrayDeque<CommandObject>();
	
	@Test
	public void populate() {
		TaskObject taskOne = new TaskObject("CS2106 Assignment 1", LocalDateTime.of(LocalDate.parse("2016-03-11"), LocalTime.parse("18:00")),
				"deadline", "completed", 1);
		CommandObject cmdOne = new CommandObject(INDEX_ADD, taskOne, 1);
		
		//TaskObject taskTwo = new TaskObject("Find internship", "floating", "incomplete", 2);
		CommandObject cmdTwo = new CommandObject(INDEX_DELETE, new TaskObject(), 1);
		
		TaskObject origTaskThree = new TaskObject("CS2103 Finals", LocalDateTime.of(LocalDate.parse("2016-04-25"), LocalTime.parse("07:00")),
				LocalDateTime.of(LocalDate.parse("2016-04-25"), LocalTime.parse("11:00")), "event", "incomplete", 3);
		origTaskThree.updateTaskDateTimesArray();
		TaskObject editTaskThree = new TaskObject("CS2103 Finals", LocalDateTime.of(LocalDate.parse("2016-04-25"), LocalTime.parse("09:00")),
				LocalDateTime.of(LocalDate.parse("2016-04-25"), LocalTime.parse("11:00")), "event", "incomplete", 3);
		CommandObject cmdThree = new CommandObject(INDEX_EDIT, editTaskThree, 1);
		
		taskList.add(origTaskThree);
		undoList.add(cmdOne);
		undoList.add(cmdTwo);
		undoList.add(cmdThree);

	}
	
	@Test // Test undo-delete (i.e. add)
	public void testA() {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		undoRedo.run(INDEX_UNDO);
		
		Logic logic = undoRedo.getUndo().getLogic();
		ArrayList<String> actualOutput = logic.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CS2106 Assignment 1. Task added is overdue.");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(2, logic.getTaskList().size());
		assertEquals(2, logic.getUndoList().size());
		assertEquals(1, logic.getRedoList().size());
	}
	
	@Test // Test undo-add (i.e. delete)
	public void testB() {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		undoRedo.run(INDEX_UNDO);

		Logic logic = undoRedo.getUndo().getLogic();
		ArrayList<String> actualOutput = logic.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted: CS2106 Assignment 1");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getUndoList().size());
		assertEquals(2, logic.getRedoList().size());
	}
	
	@Test // Test undo-edit
	public void testC() {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		undoRedo.run(INDEX_UNDO);

		Logic logic = undoRedo.getUndo().getLogic();
		ArrayList<String> actualOutput = logic.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Start time edited from '07:00' to '09:00'.");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(0, logic.getUndoList().size());
		assertEquals(3, logic.getRedoList().size());
	}
	
	@Test // Test redo-edit
	public void testD() {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		undoRedo.run(INDEX_REDO);

		Logic logic = undoRedo.getRedo().getLogic();
		ArrayList<String> actualOutput = logic.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Start time edited from '09:00' to '07:00'.");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(1, logic.getUndoList().size());
		assertEquals(2, logic.getRedoList().size());
	}
	
	@Test // Test redo-delete (i.e. add)
	public void testE() {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		undoRedo.run(INDEX_REDO);

		Logic logic = undoRedo.getRedo().getLogic();
		ArrayList<String> actualOutput = logic.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task added: CS2106 Assignment 1. Task added is overdue.");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(2, logic.getTaskList().size());
		assertEquals(2, logic.getUndoList().size());
		assertEquals(1, logic.getRedoList().size());
	}
	
	@Test // Test redo-add (i.e. delete)
	public void testF() {
		UndoRedo undoRedo = new UndoRedo(taskList, undoList, redoList);
		undoRedo.run(INDEX_REDO);

		Logic logic = undoRedo.getRedo().getLogic();
		ArrayList<String> actualOutput = logic.getOutput();
		ArrayList<String> expectedOutput = new ArrayList<String>();
		expectedOutput.add("Task deleted: CS2106 Assignment 1");
		
		assertEquals(expectedOutput, actualOutput);
		assertEquals(1, logic.getTaskList().size());
		assertEquals(3, logic.getUndoList().size());
		assertEquals(0, logic.getRedoList().size());
	}
	
}
