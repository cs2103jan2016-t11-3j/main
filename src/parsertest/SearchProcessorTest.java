package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import common.TaskObject;

import parser.SearchParser;

public class SearchProcessorTest {

	SearchParser SP = new SearchParser();
	TaskObject TO = new TaskObject();
	
	@Test
	public void testProcessSearchTerm() throws Exception {
		
		SP.process("7.13pm");
		reset();
		
		
		
		TO = SP.process("search deadline");
		assertEquals("deadline", TO.getCategory());
		reset();
		
		TO = SP.process("search 151646");
		assertEquals("151646", SP.getIndex());
		assertEquals("deadline", TO.getCategory());
		reset();
	}

	
	
	private void reset() {
		SP.resetAll();
	}

}
