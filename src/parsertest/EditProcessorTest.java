//@@author A0125003A
package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;
import parser.EditParser;

public class EditProcessorTest {

	EditParser EP = new EditParser();
	TaskObject Tempshit = new TaskObject();
	
	//edit specified time
	@Test
	public void editA() throws Exception {
		Tempshit = EP.process("14 9.13pm end");
		assertEquals("",Tempshit.getTitle());
		assertEquals("+999999999-12-31T23:59:59.999999999", Tempshit.getStartDateTime().toString());
		assertEquals("+999999999-12-31T21:13", Tempshit.getEndDateTime().toString());
		reset();
	}
	
	//edit time
	@Test
	public void editB() throws Exception {
		Tempshit = EP.process("4 by 9.13pm");
		assertEquals("+999999999-12-31T21:13", Tempshit.getStartDateTime().toString());
		assertEquals("+999999999-12-31T23:59:59.999999999", Tempshit.getEndDateTime().toString());
		reset();
	}
	
	//edit task 
	@Test
	public void editC() throws Exception {
		Tempshit = EP.process("4 st3131");
		assertEquals("st3131", Tempshit.getTitle());
		reset();
	}
	
	//edit event date
	@Test
	public void editD() throws Exception {
		Tempshit = EP.process("4 from 8june to 7july");
		assertEquals("2016-06-08T23:59:59.999999999", Tempshit.getStartDateTime().toString());
		assertEquals("2016-07-07T23:59:59.999999999", Tempshit.getEndDateTime().toString());
		reset();
	}
	
	//edit event time
	@Test
	public void editE() throws Exception {
		Tempshit = EP.process("4 from 8am to 9am");
		assertEquals("+999999999-12-31T08:00", Tempshit.getStartDateTime().toString());
		assertEquals("+999999999-12-31T09:00", Tempshit.getEndDateTime().toString());
		reset();
	}
	
	//edit event date time
	@Test
	public void editF() throws Exception {
		Tempshit = EP.process("4 from 8june 8.15am to 7/7 7pm");
		assertEquals("2016-06-08T08:15", Tempshit.getStartDateTime().toString());
		assertEquals("2016-07-07T19:00", Tempshit.getEndDateTime().toString());
		reset();
	}
	
	//edit interval
	@Test
	public void editH() throws Exception {
		Tempshit = EP.process("4 every friday");
		assertEquals(1, Tempshit.getInterval().getTimeInterval());
		assertEquals("WEEKLY", Tempshit.getInterval().getFrequency());
		reset();
	}
	
	//edit with no further inputs
	@Test
	public void editI() throws Exception {
		Tempshit = EP.process("14");
		assertEquals("", Tempshit.getTitle());
		reset();
	}
	
	private void reset() {
		Tempshit.resetAttributes();
	}
}
