//@@author A0125003A
package parsertest;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import org.junit.Test;
import parser.DateParser;

public class DateParserTest {

	DateParser DP = new DateParser();
	
	/*POSITIVE VALUE PARTITION CASES*/		
	/*case 1: test ability to read ddmm formats*/
	@Test
	public void testA() throws Exception {	
		DP.parseDate("7/6");
		assertEquals("2016-06-07", DP.getDateObject().toString());
		reset();
	}
	
	/*case 2: test ability to read ddmonthyyyy formats*/
	@Test
	public void testB() throws Exception {
		DP.parseDate("6 june 2014");
		assertEquals("2014-06-06", DP.getDateObject().toString());
		reset();
	}
	
	/*case 3: test ability to read ddmmyyyy formats*/
	@Test
	public void testC() throws Exception {
		DP.parseDate("5/6/16");
		assertEquals("2016-06-05", DP.getDateObject().toString());
		reset();
	}
	
	/*case 4: test ability to read relative dates*/
	@Test
	public void testD() throws Exception {
		DP.parseDate("next friday");
		assertEquals("2016-04-15", DP.getDateObject().toString());
		reset();
	}
	
	/*case 5: boundary value for positive-value partition (31)*/
	@Test
	public void testE() throws Exception {
		DP.parseDate("31 july 2000");
		assertEquals("2000-07-31", DP.getDateObject().toString());
		reset();
	}
	
	/*case 6: boundary value for positive-value partition (31)*/
	@Test
	public void testF() throws Exception {
		DP.parseDate("31/12/2000");
		assertEquals("2000-12-31", DP.getDateObject().toString());
		reset();
	}
	
	/*case 7: reading relative dates*/
	@Test
	public void testG() throws Exception {
		DP.parseDate("everyday");
		assertEquals("2016-04-06", DP.getDateObject().toString());
		reset();
		
		DP.parseDate("today");
		assertEquals(LocalDate.now(), DP.getDateObject());
		reset();
		
		DP.parseDate("tonight");
        assertEquals(LocalDate.now(), DP.getDateObject());
        reset();
	}
	
	/*NEGATIVE VALUE TEST CASES*/
	/*case 8: test ability to reject non-symbol separated numbers*/
	@Test(expected = Exception.class)
    public void testH() throws Exception {
	    DP.parseDate("1 11 2012");
	}
	
	/*case 9: ability to reject out of bound dates. boundary being 31*/
	@Test(expected = Exception.class)
    public void testI() throws Exception {
	    DP.parseDate("32 feb 2012");
	}
	
	/*case 10: ability to reject non-conforming relative date formats*/
	@Test(expected = Exception.class)
    public void testJ() throws Exception {
	    DP.parseDate("next next monday");
	}
	
	//method resets the list and dates for testing purposes
	private void reset() {
		DP.resetDate();
	}

}
/*
 * @Test
	public void testProcessRelativeDate() throws Exception {
		
		DP.processRelativeDate("next tue");
		assertEquals("2016-04-05",DP.getDateObject().toString());
		reset();
		
		DP.processRelativeDate("tmr");
		assertEquals(LocalDate.now().plusDays(1), DP.getDateObject());
		reset();
		
		DP.processRelativeDate("next week");
		assertEquals(LocalDate.now().plusWeeks(1) ,DP.getDateObject());
		reset();
	}

	@Test
	public void testSetMonth() {
		//test 1: test non-correct input
		DP.setMonth("testing");
		assertEquals(-1, DP.getStartMonth());
		reset();
		
		//test 2: input with varying upper and lower case
		DP.setMonth("jAnuARy");
		assertEquals(1, DP.getStartMonth());
		reset();
		
		//test 3: same as test 2
		DP.setMonth("MAY");
		assertEquals(5, DP.getStartMonth());
		reset();
	}

	@Test
	public void testRemoveMonth() {
		
		//test 1: test removal of characters in date input
		assertEquals("3    2015", DP.removeMonth("3rd March 2015"));
		
		//test 2: test removal of characters in date input
		assertEquals("3 2015", DP.removeMonth("3june2015"));
	}

	@Test
	public void testSplitStringAndProcess() {
		//test 1: test with simple input "dd yyyy"
		DP.splitStringAndProcess("23 10");
		assertEquals(23, DP.getStartDay());
		assertEquals(10, DP.getStartYear());
		reset();
		
		//test 2: test with multiple spaces
		DP.splitStringAndProcess("23   2014");
		assertEquals(23, DP.getStartDay());
		assertEquals(2014, DP.getStartYear());
		reset();
		
	}

	@Test
	public void testSetMonthWithSlash() throws Exception {
		
		//test 1: test recognition of partially completed date
		DP.setMonthWithSlash("7/8");
		assertEquals(7, DP.getStartDay());
		assertEquals(8, DP.getStartMonth());
		assertEquals(-1, DP.getStartYear());
		reset();
		
		//test 3: test recognition of completed date
		DP.setMonthWithSlash("7/8/2013");
		assertEquals(7, DP.getStartDay());
		assertEquals(8, DP.getStartMonth());
		assertEquals(2013, DP.getStartYear());
		reset();
		
		//test 4: test recognition with spaces
		DP.setMonthWithSlash("7 / 8 /2013");
		assertEquals(7, DP.getStartDay());
		assertEquals(8, DP.getStartMonth());
		assertEquals(2013, DP.getStartYear());
		reset();
	}

	@Test
	public void testProcessMonthlessDate() {
		//test basic function of adding an integer as day
		DP.processMonthlessDate("16",0);
		assertEquals(16, DP.getStartDay());
		reset();
		
		//test ability to recognise wrong inputs
		DP.processMonthlessDate("testing", 0);
		assertEquals(-1, DP.getStartDay());
		reset();
		
		//test ability to recognize number with characters
		DP.processMonthlessDate("5th", 0);
		assertEquals(5, DP.getStartDay());
		reset();
		
		//test ability to allocate to correct type of day (start or end)
		DP.processMonthlessDate("5th", 1);
		assertEquals(-1, DP.getStartDay());
		assertEquals(5, DP.getEndDay());
		reset();
	
		//extra test of ability to recognise number with char
		DP.processMonthlessDate("3rd", 0);
		assertEquals(-1, DP.getEndDay());
		reset();
	}

	@Test
	public void testSetMonthInDataProcessor() {
		//test simple recognition
		assertEquals(12, DP.setMonthInDataProcessor("december"));
		
		//test ability to recognize non-dates
		assertEquals(-1, DP.setMonthInDataProcessor("testing"));
		
		//test ability to recognize misspelt dates
		assertEquals(1, DP.setMonthInDataProcessor("jannuaRy"));
		
		//test ability to recognize longer strings
		assertEquals(3, DP.setMonthInDataProcessor("3 March"));
	}*/
