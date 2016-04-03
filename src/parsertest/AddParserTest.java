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
		assertEquals("2016-04-03T21:00",TO.getStartDateTime().toString());
		
		TO = AP.process("5pm lecture every tuesday at 4pm until 9june");
		assertEquals("5pm lecture", AP.getTask());
		assertEquals("2016-04-05T16:00",TO.getStartDateTime().toString());
		assertTrue(TO.getIsRecurring());
		
		TO = AP.process("5pm lecture every thursday from 8am to 9am until 9june");
		assertEquals("5pm lecture", AP.getTask());
		assertEquals("2016-04-07T08:00",TO.getStartDateTime().toString());
		
		TO = AP.process("ie2100 hw by today");
		assertEquals("ie2100 hw", AP.getTask());
		assertEquals("2016-04-03T23:59:59.999999999",TO.getStartDateTime().toString());
		
	}
}
