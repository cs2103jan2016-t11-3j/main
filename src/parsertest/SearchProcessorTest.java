package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.SearchProcessor;

public class SearchProcessorTest {

	SearchProcessor SP = new SearchProcessor();
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
		SP.clearList();
		SP.resetAll();
	}

}
