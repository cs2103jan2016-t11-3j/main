package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;

import parser.AddParser;

public class AddParserTest {
	
	AddParser AP = new AddParser();
	TaskObject TO = new TaskObject();
	@Test
	public void testProcess() throws Exception {
		
		AP.process("buy homework");
		assertEquals("buy homework", AP.getTask());
		
		TO = AP.process("be nice to merrel by today 9pm");
		assertEquals("be nice to merrel", AP.getTask());
		assertEquals("2016-03-29T21:00",TO.getStartDateTime().toString());
		assertEquals(-1, AP.getEndTime());
		assertEquals(-1, AP.getEndDate());
		
		TO = AP.process("5pm lecture every tuesday at 4pm until 9june");
		assertEquals("5pm lecture", AP.getTask());
		assertEquals("2016-04-05T16:00",TO.getStartDateTime().toString());
		assertTrue(TO.getIsRecurring());
		
		TO = AP.process("5pm lecture every thursday from 8am to 9am until 9june");
		assertEquals("5pm lecture", AP.getTask());
		assertEquals("2016-03-31T08:00",TO.getStartDateTime().toString());
		
		TO = AP.process("ie2100 hw by today");
		assertEquals("ie2100 hw", AP.getTask());
		assertEquals("2016-03-31T08:00",TO.getStartDateTime().toString());
		
	}
/*
	AP.process("buy cake for 7th june by 6 june 9am");
		assertEquals("buy cake for 7th june", AP.getTask());
		assertEquals(900, AP.getStartTime());
		assertEquals(20160606, AP.getStartDate());
		assertEquals(-1, AP.getEndTime());
		assertEquals(-1, AP.getEndDate());
		
		AP.process("bdae party from 6 june 9am to 2359hr 6/6");
		assertEquals("bdae party", AP.getTask());
		assertEquals(900, AP.getStartTime());
		assertEquals(20160606, AP.getStartDate());
		assertEquals(2359, AP.getEndTime());
		assertEquals(20160606, AP.getEndDate());
		
		AP.process("bdae party on 8 sept 12pm by 7sept 8am");
		assertEquals("bdae party on 8 sept 12pm", AP.getTask());
		assertEquals(800, AP.getStartTime());
		assertEquals(20160907, AP.getStartDate());
		assertEquals(-1, AP.getEndTime());
		assertEquals(-1, AP.getEndDate());
		
		AP.process("by 7sept 8am");
		assertEquals("", AP.getTask());
		assertEquals(800, AP.getStartTime());
		assertEquals(20160907, AP.getStartDate());
		assertEquals(-1, AP.getEndTime());
		assertEquals(-1, AP.getEndDate());
	*/
	@Test
	public void testSetTask() {
		//AP.setTask("add do this Wednesday");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this january");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this 13/04/2015");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this 13 march 1993");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this day");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this 3.45pm");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this 1600");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this 11061993");
		//assertEquals("add do this", AP.getTask());
		//AP.setTask("add do this 11-06/1993");
		//assertEquals("add do this", AP.getTask());
		
		//AP.setTask("add do this 11june1993");
		//assertEquals("add do this", AP.getTask());
		//AP.setTask("add do this 11 jun 1993");
		//assertEquals("add do this", AP.getTask());
		
//		AP.setTask("add do this before 1800hrs");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this from 7/11/2013 5pm to 7march 9pm");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this on 7th may 1900hr");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this every wednesday to thursday");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this every wednesday to thursday");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this by 8pm 8 june 2015 ");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this from 8pm 8 june 2015 to 9pm 11 june 2015");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this by 7/11/2013");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this on 8 may ");
//		assertEquals("do this", AP.getTask());
//		AP.setTask("add do this by 7.14pm ");
//		assertEquals("do this", AP.getTask()); 
		
	}


}
