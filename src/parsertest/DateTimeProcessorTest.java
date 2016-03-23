package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;

import parser.DateTimeParser;

public class DateTimeProcessorTest {
	DateTimeParser DTP = new DateTimeParser();
	TaskObject TO = new TaskObject();
	
	@Test
	public void testParseDateTime() throws Exception {
		//DTP.parseDateTime("by 9.13pm", false);
		//assertEquals(2113, DTP.getStartTime());
		
		
	
		TO = DTP.parse("every saturday from 8am to 9am until 9june", true);
		assertTrue(TO.getIsRecurring());
		assertEquals("WEEKLY",TO.getInterval().getFrequency());
		assertEquals(1,TO.getInterval().getTimeInterval());
		assertEquals("",TO.getStartDateTime().toString());
		assertEquals("",TO.getEndDateTime().toString());
		
	}
	/*
	 * DTP.parseDateTime("from 8 june to 7 july", false);
		assertEquals(20160608, DTP.getStartDate());
		assertEquals(20160707, DTP.getEndDate());
		DTP.reset();
		
		DTP.parseDateTime("at 7pm", false);
		assertEquals(1900, DTP.getStartTime());
		DTP.reset();
		
		DTP.parseDateTime("by 9.13pm", false);
		assertEquals(2113, DTP.getStartTime());
		DTP.reset();
	  TO = DTP.parse("every monday 8pm until 9 jan 2017", true);
		assertTrue(TO.getIsRecurring());
		assertEquals("WEEKLY",TO.getInterval().getFrequency());
		assertEquals(1,TO.getInterval().getTimeInterval());
		DTP.reset();*/

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
