//@@author A0125003A
package parsertest;

import static org.junit.Assert.*;

import java.time.LocalTime;
import org.junit.Test;
import parser.TimeParser;

public class TimeParserTest {
	
	private TimeParser TP = new TimeParser();
	boolean thrown;
	
	/*POSITIVE PARTITION*/
	/*case 1: test timeparser ability to read in hhmm(am/pm) format*/
	@Test
	public void testA() throws Exception {
		TP.processTime("8.01pm");
		assertEquals("20:01", TP.getTimeObject().toString());
		reset();
	}
	
	/*case 2: test timeparser ability to read in hhmm(am/pm) format*/
	@Test
	public void testB() throws Exception {
		TP.processTime("12.01am");
		assertEquals("00:01", TP.getTimeObject().toString());
		reset();
	}
	
	/*case 3: test timeparser ability to read in hhmm(hr) format*/
	@Test
	public void testC() throws Exception {
		TP.processTime("12:34hrs");
		assertEquals("12:34", TP.getTimeObject().toString());
		reset();
	}
	
	/*NEGATIVE PARTITION (random number without am/pm/hr)*/
	//case 4: test if exception is thrown on invalid time formats
	@Test(expected = Exception.class)
    public void testD() throws Exception {
		TP.processTime("1415");
	}
	
	//NOTE: overly huge timing will not be recognised by the regex for timing
	//but the backup here will catch for any slips
	
	//case 5: test if exception is thrown for overly huge timing
	@Test(expected = Exception.class)
    public void testE() throws Exception {
		TP.processTime("9999hr");
	}
	
	//case 6: test if exception is thrown for overly huge timing
	@Test(expected = Exception.class)
    public void testF() throws Exception {
		TP.processTime("99.99am");
	}
	
	//case 7: test boundary
	@Test(expected = Exception.class)
    public void testG() throws Exception {
		TP.processTime("13.00am");
	}
	
	private void reset() {
		TP.resetTime();
	}

	

}
