package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.DateProcessor;

public class DateProcessorTest {

	DateProcessor DP = new DateProcessor();
	
	@Test
	public void testProcessDate() {
		//test 1: test for ability to read uncompleted dates
		DP.processDate(" by 7/6", false);
		assertEquals(6, DP.getStartMonth());
		assertEquals(7, DP.getStartDay());
		assertEquals(2016, DP.getStartYear());
		assertEquals(20160607, DP.getStartDate());
		reset();
		
		//test 2: test for ability to read uncompleted dates for events
		DP.processDate("3 - 6 june 2014", false);
		assertEquals(6, DP.getStartMonth());
		assertEquals(3, DP.getStartDay());
		assertEquals(2014, DP.getStartYear());
		assertEquals(6, DP.getEndDay());
		assertEquals(6, DP.getEndMonth());
		assertEquals(2014, DP.getEndYear());
		reset();
		
		//test 3: test for ability to read slash format dates
		DP.processDate("from 5/6 to 8/9", false);
		assertEquals(6, DP.getStartMonth());
		assertEquals(5, DP.getStartDay());
		assertEquals(2016, DP.getStartYear());
		assertEquals(8, DP.getEndDay());
		assertEquals(9, DP.getEndMonth());
		assertEquals(2016, DP.getEndYear());
		reset();
		
		DP.processDate("29feb to 14march", false);
		assertEquals(2, DP.getStartMonth());
		assertEquals(29, DP.getStartDay());
		assertEquals(2016, DP.getStartYear());
		assertEquals(20160229, DP.getStartDate());
		reset();
		
		DP.processDate("29feb to 14march", true);
		assertEquals(2, DP.getStartMonth());
		assertEquals(29, DP.getStartDay());
		assertEquals(-1, DP.getStartYear());
		assertEquals(20160229, DP.getSearchDate());
		reset();
		
		DP.processDate("5/6/16", true);
		assertEquals(6, DP.getStartMonth());
		assertEquals(5, DP.getStartDay());
		assertEquals(16, DP.getStartYear());
		assertEquals(20160605, DP.getSearchDate());
		reset();
	}

	@Test
	public void testFurtherProcessDate() {
		//test 1: test for uncompleted date
		DP.convertToArray("3rd March");
		DP.furtherProcessDate();
		assertEquals(3, DP.getStartMonth());
		assertEquals(3, DP.getStartDay());
		assertEquals(-1, DP.getStartYear());
		reset();
		
		//test 2: test ability to read in "slash" formats
		DP.convertToArray("3/4");
		DP.furtherProcessDate();
		assertEquals(4, DP.getStartMonth());
		assertEquals(3, DP.getStartDay());
		assertEquals(-1, DP.getStartYear());
		reset();
		
		//test 3: ability to recognise events (start and end date)
		DP.convertToArray("3rd March to 14 may 2014");
		DP.furtherProcessDate();
		assertEquals(3, DP.getStartMonth());
		assertEquals(3, DP.getStartDay());
		assertEquals(-1, DP.getStartYear());
		assertEquals(14, DP.getEndDay());
		assertEquals(5, DP.getEndMonth());
		assertEquals(2014, DP.getEndYear());
		reset();
		
		//test 4: ability to recognise events in alternative format
		DP.convertToArray("11/6/1993 - 14/1/2014");
		DP.furtherProcessDate();
		assertEquals(6, DP.getStartMonth());
		assertEquals(11, DP.getStartDay());
		assertEquals(1993, DP.getStartYear());
		assertEquals(14, DP.getEndDay());
		assertEquals(1, DP.getEndMonth());
		assertEquals(2014, DP.getEndYear());
		reset();
		
	}

	
	@Test
	public void testConvertToArray() {
		//test ability to split string into start and end date
		DP.convertToArray("3rd march 2013 to 13 march 2014");
		assertEquals(2, DP.getListSize());
		assertEquals("3rd march 2013 ", DP.getListElement(0));
		assertEquals(" 13 march 2014", DP.getListElement(1));
		reset();
		
		//test ability to split string with "-"
		DP.convertToArray("3rd march 2013 - 13 march 2014");
		assertEquals(2, DP.getListSize());
		assertEquals("3rd march 2013 ", DP.getListElement(0));
		assertEquals(" 13 march 2014", DP.getListElement(1));
		reset();
		
		//test ability to recognize absence of end date
		DP.convertToArray("3rd march 2013 ");
		assertEquals(1, DP.getListSize());
		assertEquals("3rd march 2013 ", DP.getListElement(0));
		reset();
	}
	
	@Test
	public void testHasMonth() {
		//test ability to recognise months in various conditions
		//misspelt, full date format and short forms
		assertTrue(DP.hasMonth("jannuary"));
		assertTrue(DP.hasMonth(" November"));
		assertTrue(DP.hasMonth("feb"));
		assertTrue(DP.hasMonth("DeCemmber"));
		assertTrue(DP.hasMonth("3rd March 2015"));
	}

	@Test
	public void testHasSlash() {
		//test if dates have slashes
		assertTrue(DP.hasSlash("4/5/3"));
	}

	@Test
	public void testSetMonth() {
		//test 1: test non-correct input
		DP.setMonth("testing", 0);
		assertEquals(-1, DP.getStartMonth());
		reset();
		
		//test 2: input with varying upper and lower case
		DP.setMonth("jAnuARy", 0);
		assertEquals(1, DP.getStartMonth());
		reset();
		
		//test 3: same as test 2
		DP.setMonth("MAY", 0);
		assertEquals(5, DP.getStartMonth());
		assertEquals(5, DP.getEndMonth());
		reset();
		
		//test 4: check correctness of allocation start/end day
		DP.setMonth("MAY", 1);
		assertEquals(-1, DP.getStartMonth());
		assertEquals(5, DP.getEndMonth());
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
		DP.splitStringAndProcess("23 10", 0);
		assertEquals(23, DP.getStartDay());
		assertEquals(10, DP.getStartYear());
		assertEquals(23, DP.getEndDay());
		reset();
		
		//test 2: test with multiple spaces
		DP.splitStringAndProcess("23   2014", 0);
		assertEquals(23, DP.getStartDay());
		assertEquals(2014, DP.getStartYear());
		assertEquals(23, DP.getEndDay());
		reset();
		
	}

	@Test
	public void testSetMonthWithSlash() {
		
		//test 1: test recognition of partially completed date
		DP.setMonthWithSlash("7/8", 0);
		assertEquals(7, DP.getStartDay());
		assertEquals(8, DP.getStartMonth());
		assertEquals(-1, DP.getStartYear());
		reset();
		
		//test 2: test recognition of partially completed date
		DP.setMonthWithSlash("7/8", 1);
		assertEquals(7, DP.getEndDay());
		assertEquals(8, DP.getEndMonth());
		assertEquals(-1, DP.getEndYear());
		reset();
		
		//test 3: test recognition of completed date
		DP.setMonthWithSlash("7/8/2013", 0);
		assertEquals(7, DP.getStartDay());
		assertEquals(8, DP.getStartMonth());
		assertEquals(2013, DP.getStartYear());
		reset();
		
		//test 4: test recognition with spaces
		DP.setMonthWithSlash("7 / 8 /2013", 0);
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
