package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class SearchProcessorTest {

	SearchProcessor SP = new SearchProcessor();
	@Test
	public void testProcessSearchTerm() {
		SP.processSearchTerm("search 7.13pm");
		assertEquals(1913, SP.getStartTime());
		assertEquals(-1, SP.getStartDate());
		assertEquals(1913, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("7.13pm", SP.getTask());
		reset();
		
		SP.processSearchTerm("search buy food for everyone at 7pm");
		assertEquals(1900, SP.getStartTime());
		assertEquals(-1, SP.getStartDate());
		assertEquals(1900, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("buy food for everyone at 7pm", SP.getTask());
		reset();
	}

	@Test
	public void testHasNumber() {
		SP.convertToArray("search 9am");
		assertTrue(SP.hasNumber());
		reset();
		
		SP.convertToArray("search 9september2014");
		assertTrue(SP.hasNumber());
		reset();
		
		SP.convertToArray("search tmr go dinner with ben");
		assertFalse(SP.hasNumber());
		reset();
	}

	@Test
	public void testRemoveSearchKeyword() {
		assertEquals("leo won oscar!", SP.removeSearchKeyword("search leo won oscar!"));
	}

	@Test
	public void testIsDate() {
		assertTrue(SP.isDate("7 aug "));
		assertTrue(SP.isDate("9/8"));
		assertTrue(SP.isDate("decemmbeR"));
		assertTrue(SP.isDate("11june1993"));
		
		assertFalse(SP.isDate("2am"));
		assertFalse(SP.isDate("9auugust"));
	}

	@Test
	public void testConvertToArray() {
		SP.convertToArray("search 8 dec");
		assertEquals(3, SP.getListSize());
		reset();
		
		SP.convertToArray("search hallo hallo baby u called i cant hear a thing");
		assertEquals(11, SP.getListSize());
		reset();
	}

	@Test
	public void testConvertToTime() {
		SP.convertToTime("8pm", true);
		assertEquals(2000, SP.getStartTime());
		reset();
		
		SP.convertToTime("1800", false);
		assertEquals(1800, SP.getStartTime());
		reset();
	}

	@Test
	public void testConvertToDate() {
		SP.convertToDate("8 september 2012");
		assertEquals(20120908, SP.getStartDate());
		reset();
		
		SP.convertToDate("7/8/22");
		assertEquals(220807, SP.getStartDate());
		reset();
		
		SP.convertToDate("12dec99");
		assertEquals(991212, SP.getStartDate());
		reset();
	}

	@Test
	public void testHasMonth() {
		assertTrue(SP.hasMonth(" 8 august"));
	}

	@Test
	public void testSetMonthInDataProcessor() {
		assertEquals(8, SP.setMonthInDataProcessor(" 8 august"));
	}
	
	private void reset() {
		SP.clearList();
		SP.resetAll();
	}

}
