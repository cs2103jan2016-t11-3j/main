package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.SearchParser;

public class SearchProcessorTest {

	SearchParser SP = new SearchParser();
	@Test
	public void testProcessSearchTerm() throws Exception {
		
		SP.process("7.13pm");
		assertEquals(1913, SP.getStartTime());
		assertEquals(-1, SP.getStartDate());
		assertEquals(-1, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("", SP.getTask());
		reset();
		
		SP.process("9june");
		assertEquals(-1, SP.getStartTime());
		assertEquals(20160609, SP.getStartDate());
		assertEquals(-1, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("", SP.getTask());
		reset();
		
		SP.process("buy food for everyone at 7pm");
		assertEquals(1900, SP.getStartTime());
		assertEquals(-1, SP.getStartDate());
		assertEquals(-1, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("buy food for everyone", SP.getTask());
		reset();
	}

	
	
	private void reset() {
		SP.resetAll();
	}

}
