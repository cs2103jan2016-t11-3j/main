package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.TimeParser;

public class TimeProcessorTest {
	
	private TimeParser TP = new TimeParser();
	
	@Test
	public void testProcess() {
		TP.processTime("8pm");
		assertEquals(2000, TP.getTime());
		assertEquals("20:00", TP.getTimeString());
		reset();
		
		TP.processTime("8.08pm");
		assertEquals(2008, TP.getTime());
		assertEquals("20:08", TP.getTimeString());
		reset();
		
		TP.furtherProcessTime("6.19pm");
		assertEquals(1819, TP.getTime());
		reset();
	}
	
	
	@Test
	public void testSetTime() {
		TP.setTime("6.19am", false);
		assertEquals(619, TP.getTime());
		reset();
		
		TP.setTime("6.19pm", true);
		assertEquals(1819, TP.getTime());
		reset();
	}
	
	
	public void testBooleans() {
		//assertTrue(TP.isAM("6.5am"));
		//assertTrue(TP.isPM("6.19pm"));
		//assertTrue(TP.isHr("1759hr"));
	}

	
	private void reset() {
		TP.resetTime();
	}

	

}
