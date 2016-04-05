//@@author A0125003A
package parsertest;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import common.TaskObject;
import parser.Constants;
import parser.DateTimeParser;

public class DateTimeProcessorTest {
	DateTimeParser DTP = new DateTimeParser();
	TaskObject TO = new TaskObject();
	
	/*case 1: reading deadlines*/
	@Test
	public void testA() throws Exception {
		TO = DTP.parse("by 11 june 9am", true);
		assertEquals("2016-06-11T09:00", TO.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, TO.getEndDateTime());
	}
	
	/*case 2: reading event*/
	@Test
	public void testB() throws Exception {
		TO = DTP.parse("from 11 jan 9am to next fri 10am", true);
		assertEquals("2016-01-11T09:00", TO.getStartDateTime().toString());
		assertEquals("2016-04-15T10:00", TO.getEndDateTime().toString());
	}
	
	/*case 3: reading recurring event with termination date*/
	@Test
	public void testC() throws Exception {
		TO = DTP.parse("every monday at 8am until 9 may", true);
		assertEquals("2016-04-11T08:00", TO.getStartDateTime().toString());
		assertEquals("2016-05-09T23:59:59.999999999", TO.getInterval().getUntil().toString());
		DTP.reset();
		TO.resetAttributes();
	}
	
	/*case 4: reading recurring event with fixed repetition til termination date*/
	//test for multiple day input
	@Test
	public void testD() throws Exception {
		TO = DTP.parse("every monday, wednesday and friday at 8am for 10 weeks", true);
        assertEquals(10 ,TO.getInterval().getCount());
        assertEquals(1 ,TO.getInterval().getByDayArray()[1]);
        assertEquals(0 ,TO.getInterval().getByDayArray()[2]);
        assertEquals(1 ,TO.getInterval().getByDayArray()[3]);
        assertEquals(0 ,TO.getInterval().getByDayArray()[4]);
        assertEquals(1 ,TO.getInterval().getByDayArray()[5]);
        assertEquals(0 ,TO.getInterval().getByDayArray()[6]);
        assertEquals(0 ,TO.getInterval().getByDayArray()[7]);
        assertEquals("2016-04-11T08:00", TO.getStartDateTime().toString());
        assertEquals(10, TO.getInterval().getCount());
	}
	
	/*case 5: reading "everyday"*/
	@Test
	public void testE() throws Exception {
		TO = DTP.parse("everyday", true);
		assertEquals("2016-04-05T23:59:59.999999999", TO.getStartDateTime().toString());
		DTP.reset();
		TO.resetAttributes();
	}
	
	/*case 6: test for search input, which do not have keywords*/
	@Test
	public void testF() throws Exception {
		TO = DTP.parse("7pm", false);
        assertEquals("+999999999-12-31T19:00", TO.getStartDateTime().toString());
	}


}
