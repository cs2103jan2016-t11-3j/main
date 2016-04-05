package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;
import parser.SearchParser;

public class SearchProcessorTest {
	SearchParser SP = new SearchParser();
	TaskObject TO = new TaskObject();

	/*case 1: searching for completed tasks*/
	@Test
	public void testA() throws Exception {
		TO = SP.process("view done");
		assertEquals("completed", TO.getStatus());
		assertEquals("", TO.getTitle());
	}
	
	/*case 2: searching by categories*/
	@Test
	public void testB() throws Exception {
		TO = SP.process("search deadline");
		assertEquals("deadline", TO.getCategory());
		reset();
		
		TO = SP.process("search event");
		assertEquals("event", TO.getCategory());
		reset();
	}
	
	/*case 3: searching for completed tasks*/
	@Test
	public void testC() throws Exception {
		TO = SP.process("search 8");
		assertEquals("", TO.getTitle());
		assertEquals(8, SP.getIndex());
	}
	
	private void reset() {
		SP.resetAll();
	}

}
