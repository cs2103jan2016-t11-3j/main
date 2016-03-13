package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.Parser;

public class ParserTest {
	
	Parser tempParser = new Parser();

	@Test
	public void testAllocateCommandType() {
		tempParser.allocate("add homework IE2100 by 29feb 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals(900, tempParser.getStartTime());
		assertEquals(900, tempParser.getEndTime());
		assertEquals(20160229, tempParser.getStartDate());
		assertEquals(20160229, tempParser.getEndDate());
		assertEquals("undone", tempParser.getStatus());
		reset();
		
		tempParser.allocate("search 7/9/2016");
		assertEquals("7/9/2016", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(20160907, tempParser.getStartDate());
		assertEquals(20160907, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("edit 2 755pm");
		assertEquals("", tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("save as C://mac/desktop");
		assertEquals("as C://mac/desktop", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
	}

	@Test
	public void testParseEdit() {
		tempParser.parseEdit("edit 2 755pm");
		assertEquals("", tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
	}

	@Test
	public void testParseAdd() {
		tempParser.parseAdd("add homework IE2100 by 29feb 9am");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals(900, tempParser.getStartTime());
		assertEquals(900, tempParser.getEndTime());
		assertEquals(20160229, tempParser.getStartDate());
		assertEquals(20160229, tempParser.getEndDate());
		reset();
		
		tempParser.parseAdd("add homework IE2100 from 7/6 10am to 1pm 9/10");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals(1000, tempParser.getStartTime());
		assertEquals(1300, tempParser.getEndTime());
		assertEquals(20160607, tempParser.getStartDate());
		assertEquals(20161009, tempParser.getEndDate());
		reset();
		
	}

	@Test
	public void testParseSearch() {
		tempParser.parseSearch("search 8pm");
		//assertEquals("8pm", tempParser.getTask());
		assertEquals(2000, tempParser.getStartTime());
		assertEquals(2000, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.parseSearch("search 7/9/1403");
		//assertEquals("7/9/1403", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(14030907, tempParser.getStartDate());
		assertEquals(14030907, tempParser.getEndDate());
		reset();
	}

	@Test
	public void testIsSearch() {
		assertTrue(tempParser.isSearch("find hihi"));
		assertTrue(tempParser.isSearch("filter how are you"));
		assertTrue(tempParser.isSearch("display homewokr"));
		assertTrue(tempParser.isSearch("sort dinner"));
	}

	@Test
	public void testExtractDeleteIndex() {
		assertEquals(2, tempParser.extractDeleteIndex("delete 2"));
		assertEquals(5, tempParser.extractDeleteIndex("delete 5"));
	}

	@Test
	public void testSetCommandObjectToSave() {
		tempParser.parseSave("save C://afagdagda");
		assertEquals("C://afagdagda", tempParser.getTask());
	}
	
	private void reset() {
		tempParser.resetTaskObj();
	}
}
