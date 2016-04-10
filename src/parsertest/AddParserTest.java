//@@author A0125003A
package parsertest;



import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import common.TaskObject;
import parser.AddParser;

public class AddParserTest {
	AddParser AP = new AddParser();
	TaskObject TO = new TaskObject();
	
	//--test adding floating task
	//partition for all general string without identifiable date time
	@Test
	public void testA() throws Exception {
		TO = AP.process("buy homework");
		
		//Basic Details
		assertEquals("buy homework", TO.getTitle());
		assertEquals(LocalDateTime.MAX, TO.getStartDateTime());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
		assertEquals("incomplete", TO.getStatus());
	}
	
	//--test adding deadline with date and time
	//all date time format should work if date time parser works, hence
	//it is considered the same test case
	@Test
	public void testB() throws Exception {
		TO = AP.process("buy eggs by next sat 19.13hrs");
		
		//Basic Details
		assertEquals("buy eggs", TO.getTitle());
		assertEquals("2016-04-23T19:13", TO.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
		assertEquals("incomplete", TO.getStatus());
	}
	
	//--test adding event with date and time
	//all date time format should work if date time parser works, hence
	//it is considered the same test case	
	@Test
	public void testC() throws Exception {
		TO = AP.process("bdae partee from next sun 9am to 10pm");
		
		//Basic Details
		assertEquals("bdae partee", TO.getTitle());
		assertEquals("2016-04-24T09:00", TO.getStartDateTime().toString());
		assertEquals("2016-04-24T22:00", TO.getEndDateTime().toString());
		assertEquals("incomplete", TO.getStatus());
	}
	
	//--test adding recurring deadline with date and time and fixed termination date
	@Test
	public void testD() throws Exception {
		TO = AP.process("dota time every friday at 11pm until 9 may");
		
		//Basic Details
		assertEquals("dota time", TO.getTitle());
		assertEquals("2016-04-15T23:00", TO.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
		assertEquals("incomplete", TO.getStatus());
		
		//Interval Details
		assertEquals("2016-05-09T23:59:59.999999999", TO.getInterval().getUntil().toString());
		assertEquals("WEEKLY", TO.getInterval().getFrequency());
		assertEquals(1, TO.getInterval().getTimeInterval());
	}
	
	//--test adding recurring deadline with date and time and specified count til termination date
	@Test
	public void testE() throws Exception {
		TO = AP.process("dota time every 2 friday at 11pm for 2 months");
		
		//Basic Details
		assertEquals("dota time", TO.getTitle());
		assertEquals("2016-04-15T23:00", TO.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
		assertEquals("incomplete", TO.getStatus());

		//Interval Details
		assertEquals(4, TO.getInterval().getCount());
		assertEquals("WEEKLY", TO.getInterval().getFrequency());
		assertEquals(2, TO.getInterval().getTimeInterval());
	}
	
	//--test adding recurring deadline without termination date
	@Test
	public void testF() throws Exception {
		TO = AP.process("dota time every saturday at 11pm");
		
		//Basic Details
		assertEquals("dota time", TO.getTitle());
		assertEquals("2016-04-16T23:00", TO.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
		assertEquals("incomplete", TO.getStatus());

		//Interval Details
		assertEquals(-1, TO.getInterval().getCount());
		assertEquals("WEEKLY", TO.getInterval().getFrequency());
		assertEquals(1, TO.getInterval().getTimeInterval());
		assertEquals(LocalDateTime.MAX, TO.getInterval().getUntil());
	}
	
	@Test
	public void testG() throws Exception {
		TO = AP.process("dota time every sunday");
		
		//Basic Details
		assertEquals("dota time", TO.getTitle());
		assertEquals("2016-04-17T23:59:59.999999999", TO.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
		assertEquals("incomplete", TO.getStatus());
		
		//Interval Details
		assertEquals("+999999999-12-31T23:59:59.999999999", TO.getInterval().getUntil().toString());
		assertEquals("WEEKLY", TO.getInterval().getFrequency());
		assertEquals(1, TO.getInterval().getTimeInterval());
	}
}
