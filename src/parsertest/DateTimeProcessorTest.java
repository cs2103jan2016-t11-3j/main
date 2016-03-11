package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.DateTimeProcessor;

public class DateTimeProcessorTest {
	DateTimeProcessor DTP = new DateTimeProcessor();
	@Test
	public void testParseDateTime() {
		//DTP.parseDateTime("by 9.13pm", false);
		//assertEquals(2113, DTP.getStartTime());
		
		DTP.parseDateTime("from 8 june to 7 july", false);
		assertEquals(20160608, DTP.getStartDate());
		assertEquals(20160707, DTP.getEndDate());
		
		DTP.parseDateTime("at 7pm", false);
		assertEquals(1900, DTP.getStartTime());
		
	}

	@Test
	public void testSeparateDateTime() {
		//DTP.separateDateTime("on 7sept 8.45am", true);
	}
}
