package parsertest;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import parser.DateParser;

public class DateProcessorTest {

	DateParser DP = new DateParser();
	
	@Test
	public void testProcessDate() throws Exception {
		/*POSITIVE VALUE PARTITION CASES*/
		/*case 1: test ability to read ddmm formats*/
		DP.parseDate("7/6");
		assertEquals("2016-06-07", DP.getDateObject().toString());
		reset();
		
		/*case 2: test ability to read ddmonthyyyy formats*/
		DP.parseDate("6 june 2014");
		assertEquals("2014-06-06", DP.getDateObject().toString());
		reset();
		
		/*case 3: test ability to read ddmmyyyy formats*/
		DP.parseDate("5/6/16");
		assertEquals("2016-06-05", DP.getDateObject().toString());
		reset();
		
		/*case 4: test ability to read relative dates*/
		DP.parseDate("next friday");
		assertEquals("2016-04-08", DP.getDateObject().toString());
		reset();
		
		/*case 5: boundary value for positive-value partition (31)*/
		DP.parseDate("31 july 2000");
		assertEquals("2000-07-31", DP.getDateObject().toString());
		reset();
		
		/*case 6: boundary value for positive-value partition (31)*/
		DP.parseDate("31/12/2000");
		assertEquals("2000-12-31", DP.getDateObject().toString());
		reset();
		
		DP.parseDate("everyday");
		assertEquals("2016-03-31", DP.getDateObject().toString());
		reset();
		
		DP.parseDate("today");
		assertEquals("2016-03-31", DP.getDateObject().toString());
		reset();

		/*NEGATIVE VALUE TEST CASES*/
		/*case 5: test ability to reject non-slash separated numbers*/
		try {
			DP.parseDate("7.6");
			assert false;
		} catch (Exception e) {
			assert true;
		}
		reset();
		
		/*case 6: test if exception is thrown when date is not valid*/
		try {
			DP.parseDate("51 july 1030");
			assert false;
		} catch (Exception e) {
			assert true;
		}
		reset();
		
		/*case 7: test if exception is thrown when date is at boundary*/
		try {
			DP.parseDate("32 july 1030");
			assert false;
		} catch (Exception e) {
			assert true;
		}
		reset();
	}

	@Test(expected = Exception.class)
    public void testProcessInvalidDate() throws Exception {
	    DP.parseDate("323 feb 2012");
	}
	
	
	@Test
	public void testHasMonth() {
		/*test for cases in the positive value partition*/
		assertTrue(DP.hasMonth("jannuary")); //misspelt
		assertTrue(DP.hasMonth(" November")); //untrimmed
		assertTrue(DP.hasMonth("feb")); //short forms
		assertTrue(DP.hasMonth("DeCemmber")); //inconsistent casing
		assertTrue(DP.hasMonth("3rd March 2015")); //full ddMonthyyyy format
		
		/*test for cases in the negative value partition*/
		assertFalse(DP.hasMonth("first month"));
	}

	@Test
	public void testHasSlash() {
		/*test for values in the positive partition*/
		assertTrue(DP.hasSlash("4/5/3"));
		
		/*tests for the values in the negative partition*/
		assertFalse(DP.hasSlash("4.5.3"));
		assertFalse(DP.hasSlash("5 june 05"));
	}
	
	@Test
	public void testIsRelative() {
		/*test for values in the positive partition (relative dates)*/
		assertTrue(DP.isRelative("tmr"));
		assertTrue(DP.isRelative("today"));
		assertTrue(DP.isRelative("next week"));
		assertTrue(DP.isRelative("next wednesday"));
		
	}
	
	@Test
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
	}
	
	//method resets the list and dates for testing purposes
	private void reset() {
		DP.resetDate();
		DP.clearList();
	}

}
