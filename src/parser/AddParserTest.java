package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddParserTest {
	
	AddParser AP = new AddParser();
	@Test
	public void testProcess() {
		AP.process("add buy cake for 7th june by 6 june 9am");
		assertEquals("buy cake for 7th june", AP.getTask());
		assertEquals(900, AP.getStartTime());
		assertEquals(20160606, AP.getStartDate());
		assertEquals(-1, AP.getEndTime());
		assertEquals(-1, AP.getEndDate());
		
		AP.process("add bdae party from 6 june 9am to 2359hr 6/6");
		assertEquals("bdae party", AP.getTask());
		assertEquals(900, AP.getStartTime());
		assertEquals(20160606, AP.getStartDate());
		assertEquals(2359, AP.getEndTime());
		assertEquals(20160606, AP.getEndDate());
		
		AP.process("add bdae party on 8 sept 12pm by 7sept 8am");
		assertEquals("bdae party on 8 sept 12pm", AP.getTask());
		assertEquals(800, AP.getStartTime());
		assertEquals(20160907, AP.getStartDate());
		assertEquals(-1, AP.getEndTime());
		assertEquals(-1, AP.getEndDate());
	}

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
