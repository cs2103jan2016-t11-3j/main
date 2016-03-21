
package parsertest;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.Parser;

public class ParserTest {
	
	Parser tempParser = new Parser();

	@Test
	public void testAllocateCommandType() throws Exception {
		tempParser.allocate("add homework IE2100 by 29feb 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals("2016-02-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals("incomplete", tempParser.getStatus());
		reset();
		
		tempParser.allocate("add homework IE2100 by tmr 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals("2016-03-22T09:00",tempParser.getStartDateTime().toString());
		assertEquals("incomplete", tempParser.getStatus());
		reset();
		
		tempParser.allocate("add prep 5pm lecture by 29feb 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("prep 5pm lecture", tempParser.getTask());
		assertEquals("2016-02-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals("incomplete", tempParser.getStatus());
		reset();
		
		tempParser.allocate("add prep 5pm lecture from 29feb 9am to 8pm");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("prep 5pm lecture", tempParser.getTask());
		assertEquals("2016-02-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals("2016-02-29T20:00",tempParser.getEndDateTime().toString());
		assertEquals("incomplete", tempParser.getStatus());
		reset();
		
		tempParser.allocate("add 5pm lecture every tuesday at 4pm until 9june");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("5pm lecture", tempParser.getTask());
		assertEquals("2016-03-22T16:00",tempParser.getStartDateTime().toString());
		//test for until
		assertEquals("incomplete", tempParser.getStatus());
		reset();
		
		tempParser.allocate("search hi 7/9/2016 7pm");
		assertEquals("hi", tempParser.getTask());
		assertEquals(1900, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(20160907, tempParser.getStartDate());
		assertEquals("2016-09-07T19:00",tempParser.getStartDateTime().toString());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("aagadfgad");
		assertEquals("aagadfgad", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("7.13pm");
		assertEquals("", tempParser.getTask());
		assertEquals(1913, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("edit 2 get task 755pm");
		assertEquals("get task", tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("edit 2 755pm");
		assertEquals("", tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		assertEquals("+999999999-12-31T19:55",tempParser.getStartDateTime().toString());
		reset();
		
		tempParser.allocate("save as C://mac/desktop");
		assertEquals("as C://mac/desktop", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		tempParser.allocate("done");
		assertEquals("", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		assertEquals("completed", tempParser.getStatus());
		reset();
		
		tempParser.allocate("incomplete");
		assertEquals("", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		assertEquals("incomplete", tempParser.getStatus());
		reset();
	}

	@Test
	public void testParseEdit() throws Exception {
		tempParser.parseEdit("edit 2 7.55pm");
		assertEquals("", tempParser.getTask());
		assertEquals(1955, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
	}

	@Test
	public void testParseAdd() throws Exception {
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
	public void testParseSearch() throws Exception {
		tempParser.parseSearch("search 8pm");
		//assertEquals("8pm", tempParser.getTask());
		assertEquals(2000, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		reset();
		
		
		tempParser.parseSearch("search 7/9/1903");
		//assertEquals("7/9/1403", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(19030907, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
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
	public void testExtractDeleteIndex() throws Exception {
		assertEquals(2, tempParser.extractDeleteIndex("delete 2"));
		assertEquals(5, tempParser.extractDeleteIndex("delete 5"));
	}

	@Test
	public void testSetCommandObjectToSave() throws Exception {
		tempParser.parseSave("save C://afagdagda");
		assertEquals("C://afagdagda", tempParser.getTask());
	}
	
	private void reset() {
		tempParser.resetTaskObj();
	}
}
