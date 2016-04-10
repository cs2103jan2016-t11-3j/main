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
		
		TO = SP.process("search event");
		assertEquals("event", TO.getCategory());
	}
	
	/*case 3: searching for completed tasks*/
	@Test
	public void testC() throws Exception {
		TO = SP.process("search 8");
		assertEquals("", TO.getTitle());
		assertEquals(8, SP.getIndex());
	}
	
	/*case 3: searching for completed tasks*/
	@Test
	public void testD() throws Exception {
		TO = SP.process("next saturday");
		assertEquals("", TO.getTitle());
		assertEquals("2016-04-23T23:59:59.999999999", TO.getStartDateTime().toString());
	}
}
