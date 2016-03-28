
package parsertest;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import common.TaskObject;
import parser.Parser;

public class ParserTest {
	
	Parser tempParser = new Parser();
	

	@Test
	public void testAllocateCommandType() throws Exception {
		tempParser.allocate("add homework");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework", tempParser.getTask());
		assertEquals(LocalDateTime.MAX,tempParser.getStartDateTime());
		assertEquals(LocalDateTime.MAX, tempParser.getEndDateTime());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("floating", tempParser.getCategory());
		reset();
		
		
		/*ADD COMMAND POSITIVE-VALUE PARTITION*/
		/*case 1: adding deadline*/
		tempParser.allocate("add homework IE2100 by 29feb 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals("2016-02-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, tempParser.getEndDateTime());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("deadline", tempParser.getCategory());
		reset();
		
		/*case 2: adding deadline with relative date*/
		tempParser.allocate("add homework IE2100 by tomorrow 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals("2016-03-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, tempParser.getEndDateTime());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("deadline", tempParser.getCategory());
		reset();
		
		/*case 3: adding deadline with date/time in description*/
		tempParser.allocate("add prep 5pm lecture by 29feb 9am");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("prep 5pm lecture", tempParser.getTask());
		assertEquals("2016-02-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX, tempParser.getEndDateTime());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("deadline", tempParser.getCategory());
		reset();
		
		/*case 4: adding event*/
		tempParser.allocate("add prep 5pm lecture from 29feb 9am to 8pm");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("prep 5pm lecture", tempParser.getTask());
		assertEquals("2016-02-29T09:00",tempParser.getStartDateTime().toString());
		assertEquals("2016-02-29T20:00",tempParser.getEndDateTime().toString());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("event", tempParser.getCategory());
		reset();
		
		/*case 5: adding recurring task with relative start date*/
		tempParser.allocate("add 5pm lecture every friday at 4pm until 9june");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("5pm lecture", tempParser.getTask());
		assertEquals("2016-04-01T16:00",tempParser.getStartDateTime().toString());
		assertEquals("WEEKLY",tempParser.TO.getInterval().getFrequency());
		assertEquals(1,tempParser.TO.getInterval().getTimeInterval());
		assertEquals("2016-06-09T23:59:59.999999999",tempParser.TO.getInterval().getUntil().toString());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("deadline", tempParser.getCategory());
		reset();
		
		/*case 5: adding recurring task with relative start date*/
		tempParser.allocate("add 5pm lecture every wednesday from 8am to 9am until 9june");
		assertEquals(1, tempParser.getCommandType());
		assertEquals("5pm lecture", tempParser.getTask());
		assertEquals("2016-03-30T08:00",tempParser.getStartDateTime().toString());
		assertEquals("2016-03-30T09:00",tempParser.getEndDateTime().toString());
		assertEquals("WEEKLY",tempParser.TO.getInterval().getFrequency());
		assertEquals(1,tempParser.TO.getInterval().getTimeInterval());
		assertEquals("2016-06-09T23:59:59.999999999",tempParser.TO.getInterval().getUntil().toString());
		assertEquals("incomplete", tempParser.getStatus());
		assertEquals("event", tempParser.getCategory());
		reset();
		
		/*case 6: searches for normal user input*/
		tempParser.allocate("search hi 7/9/2016 7pm");
		assertEquals("hi", tempParser.getTask());
		assertEquals("2016-09-07T19:00",tempParser.getStartDateTime().toString());
		assertEquals(LocalDateTime.MAX,tempParser.getEndDateTime());
		reset();
		
		/*case 7: search for input without "search" keyword*/
		tempParser.allocate("aagadfgad");
		assertEquals("aagadfgad", tempParser.getTask());
		assertEquals(LocalDateTime.MAX, tempParser.getStartDateTime());
		assertEquals(LocalDateTime.MAX, tempParser.getEndDateTime());
		reset();
		
		tempParser.allocate("7.13pm");
		assertEquals("", tempParser.getTask());
		assertEquals("+999999999-12-31T19:13", tempParser.getStartDateTime().toString());
		reset();
		
		tempParser.allocate("edit 2 get task 755pm");
		assertEquals("get task", tempParser.getTask());
		assertEquals("+999999999-12-31T19:55", tempParser.getStartDateTime().toString());
		reset();
		
		tempParser.allocate("edit 2 755pm");
		assertEquals("", tempParser.getTask());
		assertEquals("+999999999-12-31T19:55",tempParser.getStartDateTime().toString());
		reset();
		
		tempParser.allocate("edit 12 every friday");
		assertEquals("", tempParser.getTask());
		assertEquals(12,tempParser.CO.getIndex());
		assertEquals("WEEKLY", tempParser.TO.getInterval().getFrequency());
		assertEquals(1, tempParser.TO.getInterval().getTimeInterval());
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
		
		tempParser.allocate("delete 4 all");
		assertEquals("all", tempParser.getTask());
		assertEquals(-1, tempParser.getStartTime());
		assertEquals(-1, tempParser.getEndTime());
		assertEquals(-1, tempParser.getStartDate());
		assertEquals(-1, tempParser.getEndDate());
		assertEquals(4, tempParser.CO.getIndex());
		reset();
		
		tempParser.allocate("search 3");
		assertEquals(2, tempParser.CO.getCommandType());
		assertEquals(3, tempParser.CO.getIndex());
		reset();
		
		tempParser.allocate("view");
		assertEquals(2, tempParser.CO.getCommandType());
		reset();
	}

	@Test
	public void testParseEdit() throws Exception {
		tempParser.parseEdit("edit 2 7.55pm");
		assertEquals("", tempParser.getTask());
		assertEquals("+999999999-12-31T19:55", tempParser.getStartDateTime().toString());
		reset();
	}

	@Test
	public void testParseAdd() throws Exception {
		tempParser.parseAdd("add homework IE2100 by 29feb 9am");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals("2016-02-29T09:00", tempParser.getStartDateTime().toString());
		reset();
		
		tempParser.parseAdd("add homework IE2100 from 7/6 10am to 1pm 9/10");
		assertEquals("homework IE2100", tempParser.getTask());
		assertEquals("2016-06-07T10:00", tempParser.getStartDateTime().toString());
		assertEquals("2016-10-09T13:00", tempParser.getEndDateTime().toString());
		reset();
		
	}

	@Test
	public void testParseSearch() throws Exception {
		tempParser.parseSearch("search 8pm");
		//assertEquals("8pm", tempParser.getTask());
		assertEquals("+999999999-12-31T20:00", tempParser.getStartDateTime().toString());
		reset();
		
		
		tempParser.parseSearch("search 7/9/1903");
		//assertEquals("7/9/1403", tempParser.getTask());
		assertEquals("1903-09-07T23:59:59.999999999", tempParser.getStartDateTime().toString());
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
