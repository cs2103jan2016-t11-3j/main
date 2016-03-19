package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.DateTimeParser;

public class DateTimeProcessorTest {
	DateTimeParser DTP = new DateTimeParser();
	@Test
	public void testParseDateTime() throws Exception {
		//DTP.parseDateTime("by 9.13pm", false);
		//assertEquals(2113, DTP.getStartTime());
		
		DTP.parseDateTime("from 8 june to 7 july", false);
		assertEquals(20160608, DTP.getStartDate());
		assertEquals(20160707, DTP.getEndDate());
		
		DTP.parseDateTime("at 7pm", false);
		assertEquals(1900, DTP.getStartTime());
		
		DTP.parseDateTime("by 9.13pm", false);
		assertEquals(2113, DTP.getStartTime());
	}

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
		DTP.recur("every month from 5th march 9am to 11am until 9 jan 2017");
		//assertEquals(20170109,DTP.getStartDate());
	}
}
