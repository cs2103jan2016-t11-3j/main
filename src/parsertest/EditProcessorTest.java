package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;

import parser.EditParser;

public class EditProcessorTest {

	EditParser EP = new EditParser();
	TaskObject Tempshit = new TaskObject();
	@Test
	public void testProcessEdit() {
		Tempshit = EP.process("edit 4 by 9.13pm");
		assertEquals(2113, Tempshit.getStartTime());
		reset();
		
		Tempshit = EP.process("edit 4 st3131");
		assertEquals("st3131", Tempshit.getTitle());
		reset();
		
		Tempshit = EP.process("edit 4 from 8june to 7july");
		assertEquals(20160608, Tempshit.getStartDate());
		assertEquals(20160707, Tempshit.getEndDate());
		reset();
		
		Tempshit = EP.process("edit 4 9.13pm");
		assertEquals(2113, Tempshit.getStartTime());
		reset();
	}

	@Test
	public void testCleanString() {
		assertEquals("do this task", EP.cleanString("edit 3 do this task"));
		assertEquals("by 9.13pm", EP.cleanString("edit 4 by 9.13pm"));
	}
	
	@Test
	public void testIsDateTime() {
		assertTrue(EP.isDateTime("by 7 sept 8.40am"));
		assertTrue(EP.isDateTime("from 7 june 8am to 8pm 9june"));
		assertTrue(EP.isDateTime("by 9.13pm"));
		
		assertFalse(EP.isDateTime("Prep lecture for 7 june 8am"));
	}
	
	private void reset() {
		EP.resetList();
		EP.reset();
	}
}
