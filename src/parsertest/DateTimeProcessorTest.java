package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;
import parser.Constants;
import parser.DateTimeParser;

public class DateTimeProcessorTest {
	DateTimeParser DTP = new DateTimeParser();
	TaskObject TO = new TaskObject();
	
	@Test
	public void testParseDateTime() throws Exception {
		//DTP.parseDateTime("by 9.13pm", false);
		//assertEquals(2113, DTP.getStartTime());
		String temp = "every 2 hour by 7pm";
		assertTrue(temp.matches(Constants.REGEX_RECURRING_TASK_IDENTIFIER));
	
		
		
		
		TO = DTP.parse("by monday", false);
		assertEquals("2016-04-02T23:59:59.999999999", TO.getStartDateTime().toString());
		DTP.reset();
		TO.resetAttributes();
		
	}
	/*
	 * TO = DTP.parse("every day", true);
		assertTrue(TO.getIsRecurring());
		assertEquals("DAILY",TO.getInterval().getFrequency());
		assertEquals(1,TO.getInterval().getTimeInterval());
		//assertEquals("2016-04-01T23:59:59.999999999",TO.getStartDateTime().toString());
		//assertEquals("",TO.getEndDateTime().toString());
		
		
		TO = DTP.parse("every day from 28 june 8am to 9am until 9june", true);
		assertTrue(TO.getIsRecurring());
		assertEquals("DAILY",TO.getInterval().getFrequency());
		assertEquals(1,TO.getInterval().getTimeInterval());
		assertEquals("2016-06-28T08:00",TO.getStartDateTime().toString());
		assertEquals("2016-06-28T09:00",TO.getEndDateTime().toString());
		assertEquals("2016-06-09T23:59:59.999999999",TO.getInterval().getUntil().toString());
		TO.resetAttributes();
		DTP.reset();
		
		TO = DTP.parse("from 8 june to 7 july", false);
		assertEquals("2016-06-08T23:59:59.999999999", TO.getStartDateTime().toString());
		assertEquals("2016-07-07T23:59:59.999999999", TO.getEndDateTime().toString());
		DTP.reset();
		TO.resetAttributes();
		
		TO = DTP.parse("at 7pm", false);
		assertEquals("+999999999-12-31T19:00", TO.getStartDateTime().toString());
		DTP.reset();
		TO.resetAttributes();
		
		TO = DTP.parse("by 9.13pm", false);
		assertEquals("+999999999-12-31T19:13", TO.getStartDateTime().toString());
		DTP.reset();
		TO.resetAttributes();
		
	    TO = DTP.parse("every monday 8pm until 9 jan 2017", true);
		assertTrue(TO.getIsRecurring());
		assertEquals("WEEKLY",TO.getInterval().getFrequency());
		assertEquals(1,TO.getInterval().getTimeInterval());
		DTP.reset();
	 * */

	@Test
	public void testSeparateDateTime() {
		//DTP.separateDateTime("on 7sept 8.45am", true);
	}
	
	@Test
	public void testGetTaskType() {
		String temp = DTP.getTaskType("every 2 monday 8pm until next month").toString();
		assertEquals("recurring", temp);
		
		temp = DTP.getTaskType("every month 8pm until 9 jan 2017").toString();
		assertEquals("recurring", temp);
		
		temp = DTP.getTaskType("every month from 5th march 9am to 11am until 9 jan 2017").toString();
		assertEquals("recurring", temp);
	}
	
	@Test
	public void testRecur() throws Exception {
		//DTP.recur("every month from 5th march 9am to 11am until 9 jan 2017");
		//assertEquals(20170109,DTP.getStartDate());
	}
}
