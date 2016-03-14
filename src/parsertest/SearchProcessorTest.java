package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.SearchParser;

public class SearchProcessorTest {

	SearchParser SP = new SearchParser();
	@Test
	public void testProcessSearchTerm() {
		SP.process("7.13pm");
		assertEquals(1913, SP.getStartTime());
		assertEquals(-1, SP.getStartDate());
		assertEquals(-1, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("7.13pm", SP.getTask());
		reset();
		
		SP.process("buy food for everyone at 7pm");
		assertEquals(-1, SP.getStartTime());
		assertEquals(-1, SP.getStartDate());
		assertEquals(-1, SP.getEndTime());
		assertEquals(-1, SP.getEndDate());
		assertEquals("buy food for everyone at 7pm", SP.getTask());
		reset();
	}

	
	
	private void reset() {
		SP.resetAll();
	}

}
