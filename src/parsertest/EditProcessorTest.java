package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;
import parser.EditParser;

public class EditProcessorTest {

	EditParser EP = new EditParser();
	TaskObject Tempshit = new TaskObject();
	@Test
	public void testProcessEdit() throws Exception {
		Tempshit = EP.process("14 9.13pm end");
		assertEquals("",Tempshit.getTitle());
		assertEquals("+999999999-12-31T23:59:59.999999999", Tempshit.getStartDateTime().toString());
		assertEquals("+999999999-12-31T21:13", Tempshit.getEndDateTime().toString());
		reset();
		
		Tempshit = EP.process("4 by 9.13pm");
		assertEquals("+999999999-12-31T21:13", Tempshit.getStartDateTime().toString());
		assertEquals("+999999999-12-31T23:59:59.999999999", Tempshit.getEndDateTime().toString());
		reset();
		
		Tempshit = EP.process("4 st3131");
		assertEquals("st3131", Tempshit.getTitle());
		reset();
		
		Tempshit = EP.process("4 from 8june to 7july");
		assertEquals("2016-06-08T23:59:59.999999999", Tempshit.getStartDateTime().toString());
		assertEquals("2016-07-07T23:59:59.999999999", Tempshit.getEndDateTime().toString());
		reset();
		
		Tempshit = EP.process("4 from 8june 8.15am to 7/7 7pm");
		assertEquals("2016-06-08T08:15", Tempshit.getStartDateTime().toString());
		assertEquals("2016-07-07T19:00", Tempshit.getEndDateTime().toString());
		reset();
		
		Tempshit = EP.process("4 9.13pm");
		//assertEquals("+999999999-12-31T21:13", Tempshit.getStartDateTime().toString());
		//assertEquals(2113, Tempshit.getStartTime());
		reset();
		
		Tempshit = EP.process("4 every friday");
		assertEquals(1, Tempshit.getInterval().getTimeInterval());
		assertEquals("WEEKLY", Tempshit.getInterval().getFrequency());
		reset();
		
		Tempshit = EP.process("14");
		assertEquals("", Tempshit.getTitle());
		reset();
	}
	
	@Test
	public void testCleanString() {
		assertEquals("do this task", EP.cleanString("3 do this task"));
		assertEquals("by 9.13pm", EP.cleanString("4 by 9.13pm"));
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
		Tempshit.resetAttributes();
	}
}
