package parsertest;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Test;

import parser.TimeParser;

public class TimeProcessorTest {
	
	private TimeParser TP = new TimeParser();
	boolean thrown;
	
	@Test
	public void testProcess() throws Exception {
		
		/*POSITIVE PARTITION*/
		/*case 1: test timeparser ability to read in hhmm(am/pm) format*/
		TP.processTime("8.01pm");
		assertEquals("20:01", TP.getTimeObject().toString());
		reset();
		
		/*case 2: test timeparser ability to read in hhmm(am/pm) format*/
		TP.processTime("8.01am");
		assertEquals("08:01", TP.getTimeObject().toString());
		reset();
		
		/*case 3: test timeparser ability to read in hhmm(hr) format*/
		TP.processTime("1415hr");
		assertEquals("14:15", TP.getTimeObject().toString());
		reset();
		
		/*NEGATIVE PARTITION (random number without am/pm/hr)*/
		/*case 4: test if exception is thrown on invalid dates*/
		try {
			TP.processTime("1415");
			assert false;
		} catch (Exception e) {
			assert true;
		}
		reset();
		
		/*case 5: test if exception is thrown for overly huge dates*/
		try {
			TP.processTime("9999hr");
			assert false;
		} catch (Exception e) {
			assert true;
		}
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
