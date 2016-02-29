package parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	Parser tempParser = new Parser();

	@Test
	public void testAllocateCommandType() {
		tempParser.allocateCommandType("add homework IE2100 date: 29feb time: 9am");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals(900, tempParser.getStartTime());
		assertEquals(900, tempParser.getEndTime());
		assertEquals(20160229, tempParser.getStartDate());
		assertEquals(20160229, tempParser.getEndDate());
		reset();
		
		tempParser.allocateCommandType("add homework IE2100 date: 7/6-9/10 time: 10-1pm");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals(1000, tempParser.getStartTime());
		assertEquals(1300, tempParser.getEndTime());
		assertEquals(20160607, tempParser.getStartDate());
		assertEquals(20161009, tempParser.getEndDate());
		reset();
		
		tempParser.allocateCommandType("search 7/9/1403");
		//assertEquals("7/9/1403", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(14030907, tempParser.getStartDate());
		assertEquals(14030907, tempParser.getEndDate());
		reset();
		
		tempParser.allocateCommandType("edit 2 755pm");
		assertEquals(null, tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(1955, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
	}

	@Test
	public void testParseEdit() {
		tempParser.parseEdit("edit 2 755pm");
		assertEquals(null, tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(1955, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
	}

	@Test
	public void testParseAdd() {
		tempParser.parseAdd("add homework IE2100 date: 29feb time: 9am");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals(900, tempParser.getStartTime());
		assertEquals(900, tempParser.getEndTime());
		assertEquals(20160229, tempParser.getStartDate());
		assertEquals(20160229, tempParser.getEndDate());
		reset();
		
		tempParser.parseAdd("add homework IE2100 date: 7/6-9/10 time: 10-1pm");
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
	public void testSetCommandObjectToDelete() {
		tempParser.setCommandObjectToDelete("delete 2");
		assertEquals("2", tempParser.getTask());
	}

	@Test
	public void testExtractDeleteIndex() {
		assertEquals("2", tempParser.extractDeleteIndex("delete 2"));
		assertEquals("5", tempParser.extractDeleteIndex("delete 5"));
	}

	@Test
	public void testSetCommandObjectToSave() {
		tempParser.setCommandObjectToSave("save C://afagdagda");
		assertEquals("C://afagdagda", tempParser.getTask());
	}
	
	private void reset() {
		tempParser.resetTaskObj();
	}
}
